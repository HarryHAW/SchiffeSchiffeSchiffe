package game.game.player.map;

import de.uniba.wiai.lspi.chord.data.ID;
import org.junit.Test;

import java.math.BigInteger;

/**
 * Created by beckf on 18.12.2016.
 */
public class GameMapTest {

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    @Test
    public void createFieldsTest(){
        byte[] one = hexStringToByteArray("49FD2733CFA85F4FEFED99204F9A126549397A79");
        byte[] two = hexStringToByteArray("5CC6B3F72BEE687B303C59C1E6EAD14CDE949C58");

        BigInteger test1 = new BigInteger(one);
        BigInteger test2 = new BigInteger(two);

        BigInteger test3 = test1.add(test2);
        BigInteger test4 = test2.subtract(test1);

        new GameMap(ID.valueOf(new BigInteger(one)), ID.valueOf(new BigInteger(two)));
    }

}