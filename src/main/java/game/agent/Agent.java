package game.agent;

import de.uniba.wiai.lspi.chord.service.Chord;
import game.game.Game;

/**
 * Created by beckf on 17.12.2016.
 */
public class Agent implements Runnable {

    private Chord chord;

    private Broadcasts broadcasts;
    private Retrives retrives;

    private Game game;

    public Agent(Chord chord, Broadcasts broadcasts, Retrives retrives){
        this.chord = chord;

        this.broadcasts = broadcasts;
        this.retrives = retrives;


    }

    @Override
    public void run() {

    }
}
