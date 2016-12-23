package game.game.ships;

import game.game.Game;
import game.game.ships.implementations.LinearDistributor;
import game.game.ships.implementations.RandomDistributor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by beckf on 20.12.2016.
 */
public class MyShips {

    private Map<Integer, Boolean> shipAt;

    public MyShips() {
        this.shipAt = new HashMap<>();
    }

    public void initMyShips() {
        Distributor distributor = new RandomDistributor();
        if(Game.SHIPDISTRIBUTION == DistributionType.LINEAR){
            distributor = new LinearDistributor();
        }

        shipAt = distributor.distributeShips();
    }

    public boolean isShipAt(int field) {
        return isShipAt(shipAt, field);
    }

    public static boolean isShipAt(Map<Integer, Boolean> shipAt, int field){
        Boolean isShipAt = shipAt.get(field);
        if (isShipAt == null) {
            isShipAt = false;
        }
        return isShipAt;
    }
}
