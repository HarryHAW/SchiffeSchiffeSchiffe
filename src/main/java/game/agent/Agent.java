package game.agent;

import de.uniba.wiai.lspi.chord.com.Broadcast;
import de.uniba.wiai.lspi.chord.com.Node;
import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;
import de.uniba.wiai.lspi.util.logging.Logger;
import game.agent.shooter.Shooter;
import game.coap.Coap;
import game.game.Game;
import game.messaging.Message;
import game.messaging.Messages;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by beckf on 17.12.2016.
 */
public class Agent implements Runnable {
    private static Logger LOG = Logger.getLogger(Agent.class);

    private ChordImpl chord;
    private Messages messages;
    private Coap coap;

    private Game game;
    private Shooter shooter;

    public Agent(ChordImpl chord, Messages messages, Coap coap) {
        this.chord = chord;
        this.messages = messages;
        this.coap = coap;

        this.game = new Game();
        this.shooter = new Shooter(game);
    }

    public void initAgent() {
        LOG.info("InitAgent");
        ID self = chord.getID();
        LOG.info("Got self from chord: " + self);
        ID predecessor = chord.getPredecessorID();
        LOG.info("Got predecessor from chord: " + predecessor);
        List<ID> fingerTable = new ArrayList<>();
        for (Node node : chord.getFingerTable()) {
            fingerTable.add(node.getNodeID());
            LOG.info("Got id from finger tabel: " + node.getNodeID());
        }

        game.initGame(self, predecessor, fingerTable);
        coap.changeColorTo(Coap.GREEN);
    }

    @Override
    public void run() {
        if(game.determineIfFirst()){
            shoot();
        }
        while (game.isRunning()) {
            Message message = messages.get();
            if (message.message() == Message.RETRIEVE) {
                broadcast(message.getRetrieve());
                //TODO
                if(game.getOwnShips() < Game.SHIPS/2){
                    coap.changeColorTo(Coap.BLUE);
                }
                else if(game.getOwnShips() < Game.SHIPS){
                    coap.changeColorTo(Coap.VIOLET);
                }
                else {
                    coap.changeColorTo(Coap.RED);
                }
                shoot();
            } else if (message.message() == Message.BROADCAST) {
                game.addBroadcast(message.getBroadcast());
            } else if (message.message() == Message.AGENT) {
                game.insertPlayer(message.getAgent());
            }
        }
        Broadcast shoot = game.getFinalShoot();
        LOG.info("Final Shoot " + shoot);
        System.out.println("Final Shoot " + shoot);
    }

    private void broadcast(ID gotShootAt){
        LOG.info("Got shoot at " + gotShootAt);
        final Boolean hit = game.gotShootAt(gotShootAt);
        //Thread broadcaster = new Thread(() -> {
            //LOG.info("Send Broadcast");
            chord.broadcast(gotShootAt, hit);
        //});
        //broadcaster.start();
    }

    private void shoot(){
        final ID shootAt = shooter.getTarget();
        LOG.info("Going to shoot at " + shootAt);
        Thread shooter = new Thread(() -> {
            chord.retrieve(shootAt);
        });
        shooter.start();
    }
}
