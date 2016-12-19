package game.game.player.map;

import de.uniba.wiai.lspi.chord.com.Broadcast;
import de.uniba.wiai.lspi.chord.data.ID;
import game.game.Game;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by beckf on 17.12.2016.
 */
public class GameMap {
    private List<Field> unknownList;
    private List<Field> waterList;
    private List<Field> shipList;
    private List<Field> shootAt;

    private ID highBoarder;
    private ID lowBoarder;

    public GameMap(ID lowBoarder, ID highBoarder) {
        this.highBoarder = highBoarder;
        this.lowBoarder = lowBoarder;

        this.unknownList = createFields(lowBoarder, highBoarder);
        this.waterList = new ArrayList<>();
        this.shipList = new ArrayList<>();
    }

    public ID getHighBoarder() {
        return highBoarder;
    }

    public ID getLowBoarder() {
        return lowBoarder;
    }

    public List<Field> getUnknownList() {
        return unknownList;
    }

    public List<Field> getWaterList() {
        return waterList;
    }

    public List<Field> getShipList() {
        return shipList;
    }

    private List<Field> createFields(ID lowBoarder, ID highBoarder) {
        List<Field> fieldList = new ArrayList<>();

        BigInteger highBoarderBigInteger = highBoarder.toBigInteger();
        BigInteger lowBoarderBigInteger = lowBoarder.toBigInteger();

        BigInteger range = highBoarderBigInteger.subtract(lowBoarderBigInteger);

        if (highBoarder.compareTo(lowBoarder) < 0) {
            range = Game.MAX_ID.toBigInteger().subtract(lowBoarderBigInteger).add(highBoarderBigInteger);
        } else if (highBoarder.compareTo(lowBoarder) == 0){
            range = Game.MAX_ID.toBigInteger();
        }

        BigInteger[] fieldSize = range.divideAndRemainder(new BigInteger(Game.FIELDS));
        BigInteger halfFieldSize = fieldSize[0].divide(new BigInteger("2"));

        BigInteger lowFieldBoarder = lowBoarderBigInteger;
        BigInteger highFieldBoarder = lowFieldBoarder.add(fieldSize[0]).mod(Game.MAX_ID.toBigInteger());

        BigInteger highBoarderBigIntegerWithoutRest = highBoarderBigInteger.subtract(fieldSize[1]);

        while (highBoarderBigIntegerWithoutRest.compareTo(highFieldBoarder) != 0) {
            fieldList.add(new Field(ID.valueOf(lowFieldBoarder), ID.valueOf(highFieldBoarder), ID.valueOf(lowFieldBoarder.add(halfFieldSize))));
            lowFieldBoarder = highFieldBoarder;
            highFieldBoarder = lowFieldBoarder.add(fieldSize[0]).mod(Game.MAX_ID.toBigInteger());
        }
        fieldList.add(new Field(ID.valueOf(lowFieldBoarder), ID.valueOf(highFieldBoarder.add(fieldSize[1])), ID.valueOf(lowFieldBoarder.add(halfFieldSize))));
        return fieldList;
    }

    public void addHistory(List<Broadcast> broadcasts) {
        for (Broadcast broadcast : broadcasts) {
            addBroadcast(broadcast);
        }
    }

    public void addBroadcast(Broadcast broadcast) {
        int i = 0;
        boolean belongsToField = false;
        while (!belongsToField && i < unknownList.size()) {
            belongsToField = unknownList.get(i).checkID(broadcast.getTarget());
            if (belongsToField) {
                if (broadcast.getHit()) {
                    shipList.add(unknownList.get(i));
                    unknownList.remove(i);
                } else {
                    waterList.add(unknownList.get(i));
                    unknownList.remove(i);
                }
            }
            i++;
        }
    }

    public void addShoot(Field field) {
        shipList.add(field);
    }
}
