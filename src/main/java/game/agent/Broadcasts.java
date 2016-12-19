package game.agent;

import de.uniba.wiai.lspi.chord.com.Broadcast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FBeck on 19.12.2016.
 */
public class Broadcasts {
    List<Broadcast> broadcasts;

    public Broadcasts(){
        this.broadcasts = new ArrayList<>();
    }

    public synchronized Broadcast get(){
        Broadcast broadcast = null;
        if(!broadcasts.isEmpty()){
            broadcast = broadcasts.get(0);
            broadcasts.remove(0);
        }
        return broadcast;
    }

    public synchronized void put(Broadcast broadcast){
        broadcasts.add(broadcast);
    }
}
