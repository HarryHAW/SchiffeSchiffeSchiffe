package game.agent.scout.implementations;

import de.uniba.wiai.lspi.chord.data.ID;
import game.agent.scout.FieldScout;
import game.game.Game;
import game.game.player.Player;
import game.game.player.map.Field;

import java.util.Random;

/**
 * Created by admin on 22.12.2016.
 */
public class StrategicFieldScout implements FieldScout {
    private Random random;
    private Game game;

    public StrategicFieldScout(Random random, Game game) {
        this.random = random;
        this.game = game;
    }

    @Override
    public Field getTarget(ID playerID) {
        Player player = game.getPlayer(playerID);
        Field field = player.getFields().get(0);
        //while(!field.isUnknown() || player.getShootAt().contains(field)) {
        while(!field.isUnknown()) {
                field = player.getFields().get(random.nextInt(player.getFields().size()));
        }
        return field;
    }
}
