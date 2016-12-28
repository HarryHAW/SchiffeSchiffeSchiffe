package game.game.ships.implementations;

import game.game.Game;
import game.game.ships.Distributor;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by beckf on 22.12.2016.
 */
public class LinearDistributor implements Distributor {
    public LinearDistributor() {
    }

    @Override
    public Map<Integer, Boolean> distributeShips() {
        Random random = new Random(System.nanoTime());
        int start = random.nextInt(90);

        Map<Integer, Boolean> shipAt = new HashMap<>();
        for(int i = 0; i < Game.SHIPS; i++){
            shipAt.put(start++, true);
        }
        return shipAt;
    }
}
