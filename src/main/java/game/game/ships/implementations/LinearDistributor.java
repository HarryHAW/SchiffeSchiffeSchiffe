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
        int start = Game.RANDOM.nextInt(Game.FIELDS);

        Map<Integer, Boolean> shipAt = new HashMap<>();
        shipAt.putAll(distributeNumberOfShipsStartingAt(Game.SHIPS, start));
        return shipAt;
    }

    public static Map<Integer, Boolean> distributeNumberOfShipsStartingAt(int number, int start){
        if(Game.FIELDS - start < number){
            start = Game.FIELDS - number;
        }

        Map<Integer, Boolean> distribution = new HashMap<>();
        for(int i = 0; i < number; i++){
            distribution.put(start++, true);
        }
        return distribution;
    }
}
