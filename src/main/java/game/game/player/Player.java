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

    public Player(ID player, ID predecessor, List<Broadcast> history) {
        this.player = player;
        this.predecessor = predecessor;

        this.gameMap = new GameMap(player, predecessor);

        this.shootAt = new ArrayList<>();

        gameMap.addHistory(history);
    }

    public ID getPlayer() {
        return player;
    }

    public ID getPredecessor() {
        return predecessor;
    }

    public void addBroadcast(Broadcast broadcast) {
        gameMap.addBroadcast(broadcast);
    }
}
