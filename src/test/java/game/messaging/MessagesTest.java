package game.messaging;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by FBeck on 21.12.2016.
 */
public class MessagesTest {
    @Test
    public void putSortTest() throws Exception {
        Messages messages = new Messages();
        Message message1 = new Message(Message.RETRIEVE, null);
        Message message2 = new Message(Message.BROADCAST, null);
        Message message3 = new Message(Message.AGENT, null);
        Message message4 = new Message(Message.BROADCAST, null);
        Message message5 = new Message(Message.AGENT, null);
        Message message6 = new Message(Message.RETRIEVE, null);

        messages.put(message1);
        messages.put(message2);
        messages.put(message3);
        messages.put(message4);
        messages.put(message5);
        messages.put(message6);

        for(int i = 0; i < 6; i++){
            if(i < 2){
                assertEquals(messages.get().message(), Message.AGENT);
            }
            else if( i < 4){
                assertEquals(messages.get().message(), Message.BROADCAST);
            }
            else {
                assertEquals(messages.get().message(), Message.RETRIEVE);
            }
        }
    }

}