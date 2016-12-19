package game.agent;

import de.uniba.wiai.lspi.chord.com.Node;
import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;
import game.game.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by beckf on 17.12.2016.
 */
public class Agent implements Runnable {

    private ChordImpl chord;

    private Broadcasts broadcasts;
    private Retrives retrives;

    private Game game;

    public Agent(ChordImpl chord, Broadcasts broadcasts, Retrives retrives) {
        this.chord = chord;

        this.broadcasts = broadcasts;
        this.retrives = retrives;

        this.game = new Game();
    }

    public void initAgent() {
        ID self = chord.getID();
        ID predecessor = chord.getPredecessorID();
        List<ID> fingerTable = new ArrayList<>();
        for (Node node : chord.getFingerTable()) {
            fingerTable.add(node.getNodeID());
        }

        game.initGame(self, predecessor, fingerTable);

        //TODO if first add pseudo retrieve
    }

    @Override
    public void run() {
        //empty Broadcasts

        //empty Retrieve
    }
}
