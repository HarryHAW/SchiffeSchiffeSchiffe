package game.agent.scout;

import de.uniba.wiai.lspi.chord.data.ID;
import game.agent.Agent;
import game.agent.scout.implementations.RandomFieldScout;
import game.agent.scout.implementations.RandomPlayerScout;
import game.game.Game;
import game.game.player.map.Field;

import java.util.Random;

/**
 * Created by beckf on 17.12.2016.
 */
public class Scout {

    private Random random;
    private Game game;

    private PlayerScout playerScout;
    private FieldScout fieldScout;

    public Scout(Random random, Game game) {
        this.random = random;
        this.game = game;

        determinScouts();
    }

    private void determinScouts(){
        if(Agent.PLAYERSOUTTYPE == ScoutType.RANDOM){
            playerScout = new RandomPlayerScout(random, game);
        }

        if(Agent.FIELDSCOUTTYPE == ScoutType.RANDOM){
            fieldScout = new RandomFieldScout(random, game);
        }
    }


    public ID getTarget(){
        //TODO
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ID playerID = playerScout.getTarget();
        Field field = fieldScout.getTarget(playerID);
        game.addShootOnPlayer(playerID, field);
        return field.getShootAt();
    }
}
