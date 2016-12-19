package game.chord;

import de.uniba.wiai.lspi.chord.com.Broadcast;
import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.NotifyCallback;
import de.uniba.wiai.lspi.util.logging.Logger;
import game.agent.Broadcasts;
import game.agent.Retrives;

/**
 * Created by FBeck on 15.12.2016.
 */
public class NotifyCallbackImpl implements NotifyCallback {
    private static Logger LOG = Logger.getLogger(NotifyCallbackImpl.class);

    private Broadcasts broadcasts;
    private Retrives retrives;

    public NotifyCallbackImpl(Broadcasts broadcasts, Retrives retrives) {
        this.broadcasts = broadcasts;
        this.retrives = retrives;
    }

    @Override
    public void retrieved(ID target) {
        LOG.debug(target);
        retrives.put(target);
    }

    /*@Override
    public void broadcast(ID source, ID target, Boolean hit) {
        LOG.debug(source + " " + target + " " + hit);
    }*/

    @Override
    public void broadcast(Broadcast broadcast) {
        LOG.debug(broadcast);
        broadcasts.put(broadcast);
    }
}
