package game.game.ships.implementations;

import game.game.Game;
import game.game.ships.Distributor;
import game.game.ships.MyShips;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by beckf on 22.12.2016.
 */
public class RandomDistributor implements Distributor {
    public RandomDistributor() {
    }

    @Override
    public Map<Integer, Boolean> distributeShips() {
        Map<Integer, Boolean> shipAt = new HashMap<>();
        Random random = new Random(System.nanoTime());

        while (shipAt.size() < Game.SHIPS) {
            int position = random.nextInt(Game.FIELDS);
            while (MyShips.isShipAt(shipAt, position)) {
                position = random.nextInt(Game.FIELDS);
            }
            shipAt.put(position, true);
        }

        return shipAt;
    }
}
