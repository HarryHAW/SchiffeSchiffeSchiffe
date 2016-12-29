package game.game.player;

import de.uniba.wiai.lspi.chord.com.Broadcast;
import de.uniba.wiai.lspi.chord.data.ID;
import game.game.player.map.Field;
import game.game.player.map.GameMap;
import game.game.player.statistics.Shoot;
import game.game.player.statistics.Statistics;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by beckf on 17.12.2016.
 */
public class Player {

    private ID player;
    private ID predecessor;

    private GameMap gameMap;
    private Statistics statistics;
    //private List<Field> shootAt;

    public Player(ID player, ID predecessor) {
        this.player = player;
        this.predecessor = predecessor;

        this.gameMap = new GameMap(predecessor, player);
        this.statistics = new Statistics();
        //this.shootAt = new ArrayList<>();
    }

    public ID getPlayer() {
        return player;
    }

    public ID getPredecessor() {
        return predecessor;
    }

    public int getWater() {
        return gameMap.getWater();
    }

    public int getUnknown() {
        return gameMap.getUnknown();
    }

    public int getShip() {
        return gameMap.getShip();
    }

    public void addBroadcast(ID shooter, Broadcast broadcast) {
        Field field = gameMap.addBroadcast(broadcast);
        if(broadcast.getTransaction() != -1) {
            Shoot shoot = new Shoot(shooter, broadcast.getTarget(), broadcast.getHit(), broadcast.getTransaction(), field);
            statistics.addShoot(shoot);
        }
    }

    /*public List<Field> getShootAt() {
        return shootAt;
    }

    public void addShoot(Field field) {
        shootAt.add(field);
    }*/

    public List<Field> getFields(){
        return gameMap.getFields();
    }

    public Field getFieldForID(ID id){
        return gameMap.getFieldForID(id);
    }

    /*public boolean didIDoLastShoot(){
        boolean iDidIt = false;
        if(!shootAt.isEmpty() && shootAt.get(shootAt.size()-1).getFieldID() == gameMap.getLastShip().getFieldID()){
            iDidIt = true;
        }
        return iDidIt;
    }*/

    public boolean belongsIDToPlayer(ID id){
        return id.isInInterval(predecessor, player.addPowerOfTwo(0));
    }

    public void updateFirstShootInGame(ID startingPlayer, Broadcast broadcast){
        statistics.updateFirstShootInGame(new Shoot(startingPlayer, broadcast.getTarget(), broadcast.getHit(), broadcast.getTransaction()));
    }

    public Shoot getDestroyingShoot(){
        return statistics.getDestroyingShoot();
    }

    @Override
    public String toString() {
        return "Player{" +
                "predecessor=" + predecessor +
                ", player=" + player +
                '}';
    }
}
