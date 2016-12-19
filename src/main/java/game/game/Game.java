package game.game;

import de.uniba.wiai.lspi.chord.com.Broadcast;
import de.uniba.wiai.lspi.chord.data.ID;
import game.game.player.Player;
import game.game.history.History;

import java.math.BigInteger;
import java.util.*;

/**
 * Created by beckf on 17.12.2016.
 */
public class Game {
    public static final ID MIN_ID = ID.valueOf(BigInteger.ZERO);
    public static final ID MAX_ID = ID.valueOf(BigInteger.valueOf(2).pow(160).subtract(BigInteger.ONE));
    public static final String FIELDS = "100";

    private Map<ID, Player> playerMap;
    private History history;

    private ID self;

    public Game(ID self, ID pred) {
        this.playerMap = new HashMap<>();
        this.history = new History();
        this.self = self;

        addplayer(self, pred);
        addplayer(pred, self);
    }

    private void addplayer(ID player, ID predecessor) {
        playerMap.put(player, new Player(player, predecessor, history.getHistoryForPlayer(player)));
    }

    public void insertPlayer(ID newPlayer) {
        ID player = findSuccesor(newPlayer);
        addplayer(newPlayer, playerMap.get(player).getPredecessor());
        addplayer(player, newPlayer);
    }

    public ID findSuccesor(ID id) {
        List<ID> ids = new ArrayList<>(playerMap.keySet());
        ids.add(id);
        Collections.sort(ids);

        int succesor = (ids.indexOf(id) + 1) % ids.size();
        return ids.get(succesor);
    }

    public void addBroadcast(Broadcast broadcast) {
        ID source = broadcast.getSource();
        if (history.addEntry(broadcast)) {
            Player player = playerMap.get(source);
            if (player != null) {
                player.addBroadcast(broadcast);
            } else {
                insertPlayer(source);
            }
        }
    }
}
