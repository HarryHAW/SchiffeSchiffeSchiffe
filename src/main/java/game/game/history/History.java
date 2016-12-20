package game.game.history;

import de.uniba.wiai.lspi.chord.com.Broadcast;
import de.uniba.wiai.lspi.chord.data.ID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by beckf on 17.12.2016.
 */
public class History {
    private Map<Integer, Broadcast> broadcasts;

    private Broadcast lastBroadcast;

    public History() {
        this.broadcasts = new HashMap<>();
    }

    public Broadcast getLastBroadcast() {
        return lastBroadcast;
    }

    public boolean addEntry(Broadcast broadcast) {
        boolean added = false;
        if(broadcasts.get(broadcast.getTransaction()) == null) {
            broadcasts.put(broadcast.getTransaction(), broadcast);
            lastBroadcast = broadcast;
            added = true;
        }
        return added;
    }

    public List<Broadcast> getHistoryForPlayer(ID playerID) {
        List<Broadcast> playerHistory = new ArrayList<>();
        for (Map.Entry<Integer, Broadcast> entry : broadcasts.entrySet()) {
            if (entry.getValue().getSource().equals(playerID)) {
                playerHistory.add(entry.getValue());
            }
        }
        return playerHistory;
    }
}
