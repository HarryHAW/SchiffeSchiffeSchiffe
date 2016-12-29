package game.game.ships.implementations;

import game.game.Game;
import game.game.ships.Distributor;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by FBeck on 29.12.2016.
 */
public class ClusterDistributor implements Distributor{
    public ClusterDistributor(){
    }


    @Override
    public Map<Integer, Boolean> distributeShips() {
        int point = Game.RANDOM.nextInt(Game.FIELDS);
        int radius = Game.SHIPS;
        Map<Integer, Boolean> distribution = new HashMap<>();

        distribution.putAll(distributeNumberOfShipsInRadiusAroundPoint(Game.SHIPS, radius, point));
        return distribution;
    }

    public static Map<Integer, Boolean> distributeNumberOfShipsInRadiusAroundPoint(int number, int radius, int point){
        if(Game.FIELDS - point < radius){
            point = Game.FIELDS - radius;
        }
        else if(point < radius){
            point = radius;
        }

        if(number > 2 * radius){
            number = 2 * radius;
        }

        int start = point - radius;

        //TODO evt random + 1
        int end = 2 * radius;
        Map<Integer, Boolean> distribution = new HashMap<>();
        for(int i = 0; i < number; i++){
            int toAdd = Game.RANDOM.nextInt(end) + start;
            while(distribution.containsKey(toAdd)){
                toAdd = Game.RANDOM.nextInt(end) + start;
            }
            distribution.put(toAdd, true);
        }

        return distribution;
    }
}
