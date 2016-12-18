package game.game;

import de.uniba.wiai.lspi.chord.data.ID;
import game.game.player.Player;
import game.game.history.History;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by beckf on 17.12.2016.
 */
public class Game {
    public static final ID MIN_ID = ID.valueOf(BigInteger.ZERO);
    public static final ID MAX_ID = ID.valueOf(BigInteger.valueOf(2).pow(160).subtract(BigInteger.ONE));

    private List<Player> playerList;
    private History history;
}
