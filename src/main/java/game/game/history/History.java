package game.game.history;

import de.uniba.wiai.lspi.chord.com.Broadcast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by beckf on 17.12.2016.
 */
public class History {
    private Map<Integer, Broadcast> broadcasts;

    public History(){
        this.broadcasts = new HashMap<>();
    }

    public void addEntry(Broadcast broadcast){
        broadcasts.put(broadcast.getTransaction(), broadcast);
    }


}
