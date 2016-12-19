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

    public History() {
        this.broadcasts = new HashMap<>();
    }

    public boolean addEntry(Broadcast broadcast) {
        broadcasts.put(broadcast.getTransaction(), broadcast);
        return true;
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
