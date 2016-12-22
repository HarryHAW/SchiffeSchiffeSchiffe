package game.game.ships;

import game.game.Game;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by beckf on 20.12.2016.
 */
public class MyShips {

    private Map<Integer, Boolean> shipAt;

    public MyShips() {
        this.shipAt = new HashMap<>();
    }

    public void initMyShips() {
        //TODO
        Random random = new Random();
        for (int i = 0; i < Game.SHIPS; i++) {
            int position = random.nextInt(Game.FIELDS);
            while (isShipAt(position)) {
                position = random.nextInt(Game.FIELDS);
            }
            shipAt.put(position, true);
        }
    }

    public boolean isShipAt(int field) {
        Boolean isShipAt = shipAt.get(field);
        if (isShipAt == null) {
            isShipAt = false;
        }
        return isShipAt;
    }
}
