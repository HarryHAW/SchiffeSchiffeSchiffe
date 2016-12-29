package game.game.ships.implementations;

import game.game.Game;
import game.game.ships.Distributor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by FBeck on 29.12.2016.
 */
public class StratgicDistributor implements Distributor {
    public static final int CLUSTERMAXFIELDRADIUS = 3;
    public static final int CLUSTERMINDISTANCE = 15;
    public static final int MAXCLUSTER = 2;
    public static final int MINCLUSTER = 1;
    public static final int MAXSHIPSPERCLUSTER = 3;
    public static final int MINSHIPSPERCLUSTER = 2;
    public static final int MAXSHIPSINCLUSTER = 5;

    public StratgicDistributor() {
    }

    @Override
    public Map<Integer, Boolean> distributeShips() {
        Map<Integer, Boolean> distribution = new HashMap<>();

        int cluster = 0;
        while (cluster < MINCLUSTER || cluster > MAXCLUSTER) {
            cluster = Game.RANDOM.nextInt(MAXCLUSTER) + 1;
        }

        List<Integer> clusterPoints = new ArrayList<>();
        for (int i = 0; i < cluster; i++) {
            int point = Game.RANDOM.nextInt(Game.FIELDS) + 1;
            if (!clusterPoints.isEmpty()) {
                while (enoughDistanceToPoints(point, clusterPoints)) {
                    point = Game.RANDOM.nextInt(Game.FIELDS) + 1;
                }
            }
            clusterPoints.add(point);
        }

        List<Integer> clusterSize = new ArrayList<>();
        while (addIntegerList(clusterSize) < MINSHIPSPERCLUSTER * clusterPoints.size() || addIntegerList(clusterSize) > MAXSHIPSINCLUSTER) {
            clusterSize = new ArrayList<>();
            for (Integer clusterPoint : clusterPoints) {
                int size = Game.RANDOM.nextInt(MAXSHIPSPERCLUSTER) + 1;
                while(size < MINSHIPSPERCLUSTER) {
                    size = Game.RANDOM.nextInt(MAXSHIPSPERCLUSTER) + 1;
                }
                clusterSize.add(size);
            }
        }

        for (int i = 0; i < clusterPoints.size(); i++) {
            distribution.putAll(ClusterDistributor.distributeNumberOfShipsInRadiusAroundPoint(clusterSize.get(i), CLUSTERMAXFIELDRADIUS, clusterPoints.get(i)));
        }

        distribution.putAll(RandomDistributor.distributeNumberOfShips(Game.SHIPS - addIntegerList(clusterSize)));
        return distribution;
    }

    private boolean enoughDistanceToPoints(int point, List<Integer> clusterPoints) {
        boolean enoughDistance = true;
        for (Integer clusterPoint : clusterPoints) {
            if (Math.abs(clusterPoint - point) < CLUSTERMINDISTANCE) {
                enoughDistance = false;
            }
        }
        return enoughDistance;
    }

    private int addIntegerList(List<Integer> list) {
        int value = 0;
        for (Integer integer : list) {
            value = value + integer;
        }
        return value;
    }
}
