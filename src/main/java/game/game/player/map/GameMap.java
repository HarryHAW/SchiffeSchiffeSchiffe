package game.game.player.map;

import de.uniba.wiai.lspi.chord.data.ID;
import game.game.Game;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by beckf on 17.12.2016.
 */
public class GameMap {
    private final String FIELDS = "100";

    private List<Field> fieldList;

    private ID highBoarder;
    private ID lowBoarder;

    public GameMap(ID lowBoarder, ID highBoarder){
        this.highBoarder = highBoarder;
        this.lowBoarder = lowBoarder;

        this.fieldList = createFields(lowBoarder, highBoarder);
    }

    public List<Field> getFieldList() {
        return fieldList;
    }

    public ID getHighBoarder() {
        return highBoarder;
    }

    public ID getLowBoarder() {
        return lowBoarder;
    }

    private List<Field> createFields(ID lowBoarder, ID highBoarder){
        List<Field> fieldList = new ArrayList<>();

        BigInteger highBoarderBigInteger = highBoarder.toBigInteger();
        BigInteger lowBoarderBigInteger = lowBoarder.toBigInteger();

        BigInteger range = highBoarderBigInteger.subtract(lowBoarderBigInteger);

        if(highBoarder.compareTo(lowBoarder) < 0){
            range = highBoarderBigInteger.subtract(Game.MAX_ID.toBigInteger()).add(lowBoarderBigInteger);
        }

        BigInteger[] fieldSize = range.divideAndRemainder(new BigInteger(FIELDS));
        BigInteger halfFieldSize = fieldSize[0].divide(new BigInteger("2"));

        BigInteger lowFieldBoarder = lowBoarderBigInteger;
        BigInteger highFieldBoarder = lowFieldBoarder.add(fieldSize[0]).add(fieldSize[1]).mod(Game.MAX_ID.toBigInteger());

        while(highBoarderBigInteger.compareTo(highFieldBoarder) != 0){
            fieldList.add(new Field(ID.valueOf(lowFieldBoarder), ID.valueOf(highFieldBoarder), ID.valueOf(lowFieldBoarder.add(halfFieldSize))));
            lowFieldBoarder = highFieldBoarder;
            highFieldBoarder = lowFieldBoarder.add(fieldSize[0]).mod(Game.MAX_ID.toBigInteger());
        }
        fieldList.add(new Field(ID.valueOf(lowFieldBoarder), ID.valueOf(highFieldBoarder.subtract(BigInteger.ONE)), ID.valueOf(lowFieldBoarder.add(halfFieldSize))));
        return fieldList;
    }
}
