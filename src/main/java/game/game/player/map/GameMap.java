package game.game.player.map;

import de.uniba.wiai.lspi.chord.com.Broadcast;
import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.util.logging.Logger;
import game.game.Game;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by beckf on 17.12.2016.
 */
public class GameMap {
    private static Logger LOG = Logger.getLogger(GameMap.class);
    private List<Field> fields;

    private ID highBoarder;
    private ID lowBoarder;

    private int unknown;
    private int ship;
    private int water;

    private Field lastShip;

    public GameMap(ID lowBoarder, ID highBoarder) {
        this.highBoarder = highBoarder;
        this.lowBoarder = lowBoarder;

        this.fields = createFields(lowBoarder, highBoarder);
        this.unknown = fields.size();
    }

    public List<Field> getFields() {
        return fields;
    }

    public ID getHighBoarder() {
        return highBoarder;
    }

    public ID getLowBoarder() {
        return lowBoarder;
    }

    public int getUnknown() {
        return unknown;
    }

    public int getShip() {
        return ship;
    }

    public int getWater() {
        return water;
    }

    public Field getLastShip() {
        return lastShip;
    }

    public void addShip(Field field){
        unknown--;
        ship++;
        lastShip = field;
        LOG.debug("Unknown: " + unknown + " Ships: " + ship + " Water: " + water);

    }

    public void addWater(){
        unknown--;
        water++;
    }

    private List<Field> createFields(ID lowBoarder, ID highBoarder) {
        List<Field> fields = new ArrayList<>();

        BigInteger highBoarderBigInteger = highBoarder.toBigInteger();
        BigInteger lowBoarderBigInteger = lowBoarder.toBigInteger();

        BigInteger range = highBoarderBigInteger.subtract(lowBoarderBigInteger);

        if (highBoarder.compareTo(lowBoarder) < 0) {
            range = Game.MAX_ID.toBigInteger().subtract(lowBoarderBigInteger).add(highBoarderBigInteger);
        } else if (highBoarder.compareTo(lowBoarder) == 0) {
            range = Game.MAX_ID.toBigInteger();
        }

        BigInteger[] fieldSize = range.divideAndRemainder(new BigInteger(String.valueOf(Game.FIELDS)));
        BigInteger halfFieldSize = fieldSize[0].divide(new BigInteger("2"));

        BigInteger lowFieldBoarder = lowBoarderBigInteger;
        BigInteger highFieldBoarder = lowFieldBoarder.add(fieldSize[0]).mod(Game.MAX_ID.toBigInteger());

        BigInteger highBoarderBigIntegerWithoutRest = highBoarderBigInteger.subtract(fieldSize[1]);

        int i = 0;
        while (highBoarderBigIntegerWithoutRest.compareTo(highFieldBoarder) != 0) {
            fields.add(new Field(i, ID.valueOf(lowFieldBoarder), ID.valueOf(highFieldBoarder), ID.valueOf(lowFieldBoarder.add(halfFieldSize))));
            lowFieldBoarder = highFieldBoarder;
            highFieldBoarder = lowFieldBoarder.add(fieldSize[0]).mod(Game.MAX_ID.toBigInteger());
            i++;
        }
        fields.add(new Field(i, ID.valueOf(lowFieldBoarder), ID.valueOf(highFieldBoarder.add(fieldSize[1])), ID.valueOf(lowFieldBoarder.add(halfFieldSize))));
        return fields;
    }

    public void addBroadcast(Broadcast broadcast) {
        Field field = getFieldForID(broadcast.getTarget());
        if (field.isUnknown()) {
            if (broadcast.getHit()) {
                LOG.info("A Ship of Player " + broadcast.getSource() + " got destroyed!");
                field.toShip();
                addShip(field);
            } else {
                LOG.debug("Missed at " + broadcast.getTarget() + " of Player " + broadcast.getSource());
                field.toWater();
                addWater();
            }
        }
        else if(field.isWater()) {
            if (broadcast.getHit()) {
                LOG.error("Water changed to Ship " + broadcast.getSource());
                field.toShip();
                addShip(field);
            }
        }
    }

    public Field getFieldForID(ID id) {
        Field foundField = null;
        for (Field field : fields) {
            if (field.checkID(id)) {
                foundField = field;
            }
        }
        return foundField;
    }
}
