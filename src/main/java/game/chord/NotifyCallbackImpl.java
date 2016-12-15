package game.chord;

import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.service.NotifyCallback;
import de.uniba.wiai.lspi.util.logging.Logger;

/**
 * Created by FBeck on 15.12.2016.
 */
public class NotifyCallbackImpl implements NotifyCallback {
    private static Logger LOG = Logger.getLogger(NotifyCallbackImpl.class);

    public NotifyCallbackImpl() {}

    @Override
    public void retrieved(ID target) {
        LOG.debug(target);
    }

    @Override
    public void broadcast(ID source, ID target, Boolean hit) {
        LOG.debug(source + " " + target + " " + hit);
    }
}
