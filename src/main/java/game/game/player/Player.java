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

    private GameMap gameMap;
    private List<Field> shootAt;
    private List<Field> destroyedShips;

    public Player(ID highBoarder, ID lowBoarder, List<Broadcast> history){
        this.gameMap = new GameMap(highBoarder, lowBoarder);

        this.shootAt = new ArrayList<>();
        this.destroyedShips = new ArrayList<>();

        rewindHistory(history);
    }

    private void rewindHistory(List<Broadcast> history){

    }
}
