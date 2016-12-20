package game.chord;

import de.uniba.wiai.lspi.chord.com.Broadcast;
import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.NotifyCallback;
import de.uniba.wiai.lspi.util.logging.Logger;
import game.messaging.Message;
import game.messaging.Messages;

/**
 * Created by FBeck on 15.12.2016.
 */
public class NotifyCallbackImpl implements NotifyCallback {
    private static Logger LOG = Logger.getLogger(NotifyCallbackImpl.class);

    private Messages messages;

    public NotifyCallbackImpl(Messages messages) {
        this.messages = messages;
    }

    @Override
    public void retrieved(ID target) {
        LOG.debug(target);
        messages.put(new Message(Message.RETRIEVE, target));
    }

    /*@Override
    public void broadcast(ID source, ID target, Boolean hit) {
        LOG.debug(source + " " + target + " " + hit);
    }*/

    @Override
    public void broadcast(Broadcast broadcast) {
        LOG.debug(broadcast);
        messages.put(new Message(Message.BROADCAST, broadcast));
    }
}
