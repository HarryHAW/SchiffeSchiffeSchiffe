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

    private int lastTransactionNumber;
    private Broadcast lastBroadcast;

    public History() {
        this.broadcasts = new HashMap<>();
    }

    public Broadcast getLastBroadcast() {
        return lastBroadcast;
    }

    public boolean addEntry(Broadcast broadcast) {
        boolean added = false;
        if(lastBroadcast != null
                && lastBroadcast.getSource().compareTo(broadcast.getSource()) == 0
                && lastBroadcast.getTarget().compareTo(broadcast.getTarget()) == 0
                && lastBroadcast.getHit() == broadcast.getHit()){
            if(lastBroadcast.getTransaction() == -1){
                lastBroadcast = broadcast;
                lastTransactionNumber = broadcast.getTransaction();
            }
        }
        else {
            if(lastBroadcast != null) {
                broadcasts.put(lastBroadcast.getTransaction(), lastBroadcast);
            }
            lastBroadcast = broadcast;
            if(broadcast.getTransaction() != -1){
                lastTransactionNumber = broadcast.getTransaction();
            }
            added = true;
        }
        return added;
    }

    public Broadcast getNewestDestroyingBroadcastOfPlayer(ID playerID){
        if(lastBroadcast == null){
            return null;
        }
        Broadcast broadcast = lastBroadcast;
        while (broadcast.getSource().compareTo(playerID) != 0 && broadcast.getHit() != true){
            broadcast = getBroadcastOfTransactionNumber(broadcast.getTransaction() - 1);
        }
        return broadcast;
    }

    public List<Broadcast> getHistoryForPlayer(ID playerID) {
        List<Broadcast> playerHistory = new ArrayList<>();
        for (Map.Entry<Integer, Broadcast> entry : broadcasts.entrySet()) {
            if (entry.getValue().getSource().equals(playerID)) {
                playerHistory.add(entry.getValue());
            }
        }
        if(lastBroadcast != null && lastBroadcast.getSource().compareTo(playerID) == 0){
            playerHistory.add(lastBroadcast);
        }
        return playerHistory;
    }

    public List<Broadcast> getLastIBroadcast(int number){
        List<Broadcast> lastBroadcasts = new ArrayList<>();
        int transaction = lastTransactionNumber;
        if(lastBroadcast.getTransaction() != -1){
            lastBroadcasts.add(lastBroadcast);
            number--;
            transaction--;
        }
        lastBroadcasts.addAll(getLastBroadcastsOfTransactionNumber(number, transaction));
        return lastBroadcasts;
    }

    public Broadcast getBroadcastOfTransactionNumber(int transactionNumber){
        Broadcast broadcast = null;
        while (!broadcasts.containsKey(transactionNumber) && transactionNumber >= 0){
            transactionNumber--;
        }
        broadcast = broadcasts.get(transactionNumber);
        return broadcast;
    }

    public List<Broadcast> getLastBroadcastsOfTransactionNumber(int number, int transactionNumber){
        List<Broadcast> lastBroadcasts = new ArrayList<>();
        for(int i = 0; i < number && transactionNumber > 0; i++){
            Broadcast broadcast = getBroadcastOfTransactionNumber(transactionNumber);
            lastBroadcasts.add(broadcast);
            transactionNumber = broadcast.getTransaction() - 1;
        }
        return lastBroadcasts;
    }

    public Broadcast getBroadcastBevorBroadcast(Broadcast broadcast){
        int transaction = broadcast.getTransaction() - 1;
        if(broadcast.getTransaction() < 0){
            transaction = lastTransactionNumber;
        }
        return getBroadcastOfTransactionNumber(transaction);
    }
}
