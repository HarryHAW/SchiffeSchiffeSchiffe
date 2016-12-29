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

        shipAt.putAll(distributeNumberOfShips(Game.SHIPS));

        return shipAt;
    }

    public static Map<Integer, Boolean> distributeNumberOfShips(int number){
        Map<Integer, Boolean> distribution = new HashMap<>();
        for(int i = 0; i < number; i++){
            int toAdd = Game.RANDOM.nextInt(Game.FIELDS);
            while(distribution.containsKey(toAdd)){
                toAdd = Game.RANDOM.nextInt(Game.FIELDS);
            }
            distribution.put(toAdd, true);
        }
        return distribution;
    }
}
