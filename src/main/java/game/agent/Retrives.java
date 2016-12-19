package game.agent;

import de.uniba.wiai.lspi.chord.data.ID;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FBeck on 19.12.2016.
 */
public class Retrives {
    List<ID> ids;

    public Retrives(){
        this.ids = new ArrayList<>();
    }

    public synchronized ID get(){
        ID id = null;
        if(!ids.isEmpty()){
            id = ids.get(0);
            ids.remove(0);
        }
        return id;
    }

    public synchronized void put(ID id){
        ids.add(id);
    }
}
