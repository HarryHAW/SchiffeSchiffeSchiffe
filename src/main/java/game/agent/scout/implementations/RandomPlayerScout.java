package game.agent.scout.implementations;

import de.uniba.wiai.lspi.chord.data.ID;
import game.agent.scout.PlayerScout;
import game.game.Game;

import java.util.Random;

/**
 * Created by beckf on 22.12.2016.
 *
 * Stragey to find next player as target - random
 */
public class RandomPlayerScout implements PlayerScout{
    private Random random;
    private Game game;

    public RandomPlayerScout(Random random, Game game) {
        this.random = random;
        this.game = game;
    }

    @Override
    public ID getTarget() {
        ID playerID = game.getSelf();
        while(playerID.compareTo(game.getSelf()) == 0) {
            playerID = game.getPlayers().get(random.nextInt(game.getPlayers().size()));
        }
        return playerID;

    }
}
