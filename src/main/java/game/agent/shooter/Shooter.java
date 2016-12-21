package game.agent.shooter;

import de.uniba.wiai.lspi.chord.data.ID;
import game.game.Game;
import game.game.player.Player;
import game.game.player.map.Field;

import java.util.Random;

/**
 * Created by beckf on 17.12.2016.
 */
public class Shooter {

    public Game game;

    public Shooter(Game game) {
        this.game = game;
    }

    public ID getTarget(){
        //TODO
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Random random = new Random(System.nanoTime());
        ID playerID = game.getSelf();
        while(playerID.compareTo(game.getSelf()) == 0) {
            playerID = game.getPlayers().get(random.nextInt(game.getPlayers().size()));
        }

        Player player = game.getPlayer(playerID);
        Field field = player.getFields().get(0);
        while(!field.isUnknown() || player.getShootAt().contains(field)) {
            field = player.getFields().get(random.nextInt(player.getFields().size()));
        }
        game.addShootOnPlayer(playerID, field);
        return field.getShootAt();
    }
}
