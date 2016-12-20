package game.chord;

import de.uniba.wiai.lspi.chord.com.CommunicationException;
import de.uniba.wiai.lspi.chord.com.Node;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;
import game.messaging.Message;
import game.messaging.Messages;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by beckf on 17.12.2016.
 */
public class EnemyCrawler implements Runnable {

    private ChordImpl chord;

    private Messages messages;

    public EnemyCrawler(ChordImpl chord, Messages messages) {
        this.chord = chord;

        this.messages = messages;
    }

    @Override
    public void run() {
        Set<Node> nodes = new HashSet<>();
        boolean foundNew = true;

        Node myNode = chord.getNode();
        nodes.add(myNode);

        while (foundNew) {
            foundNew = false;
            try {
                Node node = myNode.findSuccessor(myNode.getNodeID().addPowerOfTwo(2));
                while (!node.equals(myNode)) {
                    nodes.add(node);
                    if(!nodes.contains(node)){
                        messages.put(new Message(Message.AGENT, node.getNodeID()));
                        foundNew = true;
                    }
                    node = node.findSuccessor(node.getNodeID().addPowerOfTwo(2));
                }
            } catch (CommunicationException e) {
                e.printStackTrace();
            }
        }
    }
}
