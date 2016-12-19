package game.game.player.map;

import de.uniba.wiai.lspi.chord.com.Broadcast;
import de.uniba.wiai.lspi.chord.data.ID;

/**
 * Created by beckf on 17.12.2016.
 */
public class Field {
    private ID highBoarder;
    private ID lowBoarder;
    private ID shootAt;

    public Field(ID lowBoarder, ID highBoarder, ID shootAt) {
        this.highBoarder = highBoarder;
        this.lowBoarder = lowBoarder;
        this.shootAt = shootAt;
    }

    public ID getShootAt() {
        return shootAt;
    }

    public boolean checkID(ID id) {
        if (id.isInInterval(lowBoarder, highBoarder.addPowerOfTwo(0))) {
            return true;
        }
        return false;
    }
}
