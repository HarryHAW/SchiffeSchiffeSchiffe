package game.agent;

import de.uniba.wiai.lspi.chord.com.Broadcast;
import de.uniba.wiai.lspi.chord.com.Node;
import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;
import de.uniba.wiai.lspi.util.logging.Logger;
import game.agent.scout.Scout;
import game.agent.scout.ScoutType;
import game.coap.Coap;
import game.game.Game;
import game.game.player.statistics.Shoot;
import game.messaging.Message;
import game.messaging.Messages;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by beckf on 17.12.2016.
 */
public class Agent implements Runnable {
    public static final ScoutType PLAYERSOUTTYPE = ScoutType.RANDOM;
    public static final ScoutType FIELDSCOUTTYPE = ScoutType.RANDOM;

    private static Logger LOG = Logger.getLogger(Agent.class);

    private ChordImpl chord;
    private Messages messages;
    private Coap coap;

    private Game game;
    private Scout scout;

    private boolean running;

    public Agent(ChordImpl chord, Messages messages, Coap coap) {
        this.chord = chord;
        this.messages = messages;
        this.coap = coap;

        this.game = new Game();
        Random random = new Random(System.nanoTime());
        this.scout = new Scout(random, game);
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

        running = true;
        LOG.info("Running is: " + running);
    }

    @Override
    public void run() {
        if(game.determineIfFirst()){
            shoot();
        }
        while (running) {
            Message message = messages.get();
            if (message.message() == Message.RETRIEVE) {
                broadcast(message.getRetrieve());
                //TODO
                coap();
                shoot();
            } else if (message.message() == Message.BROADCAST) {
                game.addBroadcast(message.getBroadcast());
            } else if (message.message() == Message.AGENT) {
                game.insertPlayer(message.getAgent());
            }
            determineRunning();
        }
        LOG.info("END");
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

    //shoot at field of other player
    private void shoot(){
        final ID shootAt = scout.getTarget();
        LOG.info("Going to shoot at " + shootAt);
        Thread shooter = new Thread(() -> {
            chord.retrieve(shootAt);
        });
        shooter.start();
    }

    // set led-color via coap
    private void coap(){
        if(game.getOwnShips() < Game.SHIPS/2){
            coap.changeColorTo(Coap.BLUE);
        }
        else if(game.getOwnShips() < Game.SHIPS){
            coap.changeColorTo(Coap.VIOLET);
        }
        else {
            coap.changeColorTo(Coap.RED);
        }
    }

    private void determineRunning(){
        for(ID player: game.getDestoryedPlayer()){
            Shoot finalShoot = game.getDestroyingShootForPlayer(player);
            if(game.didIShootHim(player)){
                LOG.info("Player " + player + " was destroyed by me!!!");
                running = false;
            }
            else {
                LOG.info("Player " + player + " was destroyed by " + finalShoot.getShooter() + "!!!");
            }
            LOG.info("Player was destoryed in Round " + finalShoot.getTransactionNumber() + "!!!");
            LOG.info("Final Shoot was on " + finalShoot.getTarget() + "!!!");
        }
    }
}
