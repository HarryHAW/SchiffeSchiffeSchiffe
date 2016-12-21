package game.game.player;

import de.uniba.wiai.lspi.chord.com.Broadcast;
import de.uniba.wiai.lspi.chord.data.ID;
import game.game.player.map.Field;
import game.game.player.map.GameMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by beckf on 17.12.2016.
 */
public class Player {

    private ID player;
    private ID predecessor;

    private GameMap gameMap;
    private List<Field> shootAt;

    public Player(ID player, ID predecessor) {
        this.player = player;
        this.predecessor = predecessor;

        this.gameMap = new GameMap(predecessor, player);
        this.shootAt = new ArrayList<>();
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

    public void addBroadcast(Broadcast broadcast) {
        gameMap.addBroadcast(broadcast);
    }

    public List<Field> getShootAt() {
        return shootAt;
    }

    public void addShoot(Field field) {
        shootAt.add(field);
    }

    public List<Field> getFields(){
        return gameMap.getFields();
    }

    public Field getFieldForID(ID id){
        return gameMap.getFieldForID(id);
    }

    public boolean didIDoLastShoot(){
        boolean iDidIt = false;
        if(!shootAt.isEmpty() && shootAt.get(shootAt.size()-1).getFieldID() == gameMap.getLastShip().getFieldID()){
            iDidIt = true;
        }
        return iDidIt;
    }

    public boolean belongsIDToPlayer(ID id){
        return id.isInInterval(predecessor, player.addPowerOfTwo(0));
    }

    @Override
    public String toString() {
        return "Player{" +
                "predecessor=" + predecessor +
                ", player=" + player +
                '}';
    }
}
