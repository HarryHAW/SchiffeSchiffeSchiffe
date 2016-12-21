package game.messaging;

import de.uniba.wiai.lspi.chord.com.Broadcast;
import de.uniba.wiai.lspi.chord.data.ID;

/**
 * Created by beckf on 20.12.2016.
 */
public class Message implements Comparable<Message>{
    public static final int RETRIEVE = 30;
    public static final int BROADCAST = 20;
    public static final int AGENT = 10;

    private int message;
    private Object object;

    public Message(int message, Object object){
        this.message = message;
        this.object = object;
    }

    public int message(){
        return message;
    }

    public ID getRetrieve(){
        ID id = null;
        if(message == RETRIEVE){
            id = (ID) object;
        }
        return id;
    }

    public Broadcast getBroadcast(){
        Broadcast broadcast = null;
        if(message == BROADCAST){
            broadcast = (Broadcast) object;
        }
        return broadcast;
    }

    public ID getAgent(){
        ID id = null;
        if(message == AGENT){
            id = (ID) object;
        }
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;

        Message message1 = (Message) o;

        return message == message1.message;
    }

    @Override
    public int hashCode() {
        return message;
    }


    @Override
    public int compareTo(Message o) {
        return message - o.message;
    }
}
