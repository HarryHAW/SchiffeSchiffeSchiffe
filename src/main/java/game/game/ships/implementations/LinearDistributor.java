package game.game.ships.implementations;

import game.game.Game;
import game.game.ships.Distributor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by beckf on 22.12.2016.
 */
public class LinearDistributor implements Distributor {
    public LinearDistributor() {
    }

    @Override
    public Map<Integer, Boolean> distributeShips() {
        Map<Integer, Boolean> shipAt = new HashMap<>();
        for(int i = 0; i < Game.SHIPS; i++){
            shipAt.put(i, true);
        }
        return shipAt;
    }
}
