package game.game.player.map;

import de.uniba.wiai.lspi.chord.data.ID;

/**
 * Created by beckf on 17.12.2016.
 */
public class Field {
    private ID highBoarder;
    private ID lowBoarder;
    private ID shootAt;

    private FieldType type;

    public Field(ID lowBoarder, ID highBoarder, ID shootAt){
        this.highBoarder = highBoarder;
        this.lowBoarder = lowBoarder;
        this.shootAt = shootAt;

        this.type = FieldType.UNKNOWN;
    }

    public ID getHighBoarder() {
        return highBoarder;
    }

    public ID getLowBoarder() {
        return lowBoarder;
    }

    public ID getShootAt() {
        return shootAt;
    }

    public FieldType getType() {
        return type;
    }
}
