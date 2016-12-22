package game.game;

import de.uniba.wiai.lspi.chord.com.Broadcast;
import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.util.logging.Logger;
import game.game.history.History;
import game.game.player.Player;
import game.game.player.map.Field;
import game.game.player.map.MyShips;

import java.math.BigInteger;
import java.util.*;

/**
 * Created by beckf on 17.12.2016.
 */
public class Game {
    private static Logger LOG = Logger.getLogger(Game.class);

    public static final ID MIN_ID = ID.valueOf(BigInteger.ZERO);
    public static final ID MAX_ID = ID.valueOf(BigInteger.valueOf(2).pow(160).subtract(BigInteger.ONE));
    public static final int FIELDS = 100;
    public static final int SHIPS = 10;

    private Map<ID, Player> playerMap;
    private History history;

    private List<ID> player;
    private ID self;
    private MyShips myShips;

    private boolean running;
    private Broadcast finalShoot;

    public Game() {
        this.playerMap = new HashMap<>();
        this.history = new History();
        this.player = new ArrayList<>();
        this.myShips = new MyShips();
    }

    public ID getSelf() {
        return self;
    }

    public int getOwnShips(){
        return playerMap.get(self).getShip();
    }

    public List<ID> getPlayers() {
        return player;
    }

    public Player getPlayer(ID id) {
        return playerMap.get(id);
    }

    public boolean isRunning() {
        return running;
    }

    public Broadcast getFinalShoot() {
        return finalShoot;
    }

    public void initGame(ID self, ID pred, List<ID> players) {
        LOG.info("InitGame");

        this.self = self;
        LOG.info("Self is: " + self);

        if (!players.contains(self)) {
            players.add(self);
        }
        if (!players.contains(pred)) {
            players.add(pred);
        }
        Collections.sort(players);

        for (int i = 0; i < players.size(); i++) {
            if (i == 0) {
                addplayer(players.get(i), players.get(players.size() - 1));
            } else {
                addplayer(players.get(i), players.get(i - 1));
            }
        }
        LOG.info("Player after init: " + player.size());

        myShips.initMyShips();
        this.running = true;
        LOG.info("Running is: " + running);
    }

    private void addplayer(ID player, ID predecessor) {
        Player newPlayer = new Player(player, predecessor);
        playerMap.put(player, newPlayer);
        if (!this.player.contains(player)) {
            this.player.add(player);
        }
        history.getHistoryForPlayer(player).forEach(this::addBroadcast);
        LOG.info("Player added: " + newPlayer.toString());
    }

    public void insertPlayer(ID newPlayerID) {
        LOG.info("Insert new Player: " + newPlayerID);
        ID playerID = findSuccesor(newPlayerID);
        Player player = playerMap.get(playerID);
        List<Field> shootsOnPlayer = player.getShootAt();
        addplayer(newPlayerID, player.getPredecessor());
        addplayer(playerID, newPlayerID);
        List<Player> players = new ArrayList<>();
        players.add(playerMap.get(playerID));
        players.add(playerMap.get(newPlayerID));
        for (Field field: shootsOnPlayer) {
            for (Player p: players){
                if(p.belongsIDToPlayer(field.getShootAt())){
                    p.addShoot(p.getFieldForID(field.getShootAt()));
                }
            }
        }
        LOG.info("Player after insert: " + this.player.size());
    }

    public ID findSuccesor(ID id) {
        List<ID> ids = new ArrayList<>(playerMap.keySet());
        ids.add(id);
        Collections.sort(ids);

        int successor = (ids.indexOf(id) + 1) % ids.size();
        return ids.get(successor);
    }

    public void addBroadcast(Broadcast broadcast) {
        LOG.info("Got Broadcast " + broadcast);
        ID source = broadcast.getSource();
        if (history.addEntry(broadcast)) {
            if (playerMap.containsKey(source)) {
                playerMap.get(source).addBroadcast(broadcast);
            } else {
                insertPlayer(source);
            }
            running = determineRunning();
            if (!running) {
                finalShoot = broadcast;
            }
        }
    }

    public boolean gotShootAt(ID id) {
        LOG.debug("Called GotShootAt with id: " + id);
        boolean hit = false;
        Player self = playerMap.get(this.self);
        Field field = self.getFieldForID(id);
        LOG.debug("GotShootAt found this Field: " + field);
        if(field == null){
            System.out.println("...");
        }
        if (field.isUnknown()) {
            hit = myShips.isShipAt(field.getFieldID());
        }
        addBroadcast(new Broadcast(this.self, this.self, id, -1, hit));
        return hit;
    }

    public boolean determineRunning() {
        boolean running = true;
        for (Map.Entry<ID, Player> entry : playerMap.entrySet()) {
            if (!isPlayerAlive(entry.getValue()) && didIShootHim(entry.getKey())) {
                running = !entry.getValue().didIDoLastShoot();
            }
        }
        return running;
    }

    public boolean isPlayerAlive(Player player) {
        boolean alive = true;
        if (player.getShip() == SHIPS) {
            alive = false;
        }
        return alive;
    }

    public boolean didIShootHim(ID player) {
        Broadcast destroyingBroadcast = history.getNewestDestroyingBroadcastOfPlayer(player);
        Broadcast broadcastBevor = history.getBroadcastBevorBroadcast(destroyingBroadcast);
        boolean iShootLast = broadcastBevor.getSource().compareTo(self) == 0;
        return iShootLast;
    }

    public boolean determineIfFirst() {
        Player self = playerMap.get(this.self);
        return MIN_ID.isInInterval(self.getPredecessor(), self.getPlayer());
    }

    public void addShootOnPlayer(ID player, Field shoot) {
        playerMap.get(player).addShoot(shoot);
    }
}
