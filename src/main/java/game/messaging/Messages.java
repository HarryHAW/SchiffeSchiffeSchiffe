package game.messaging;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by beckf on 20.12.2016.
 */
public class Messages {
    List<Message> messages;

    public Messages() {
        this.messages = new ArrayList<>();
    }

    public synchronized Message get() {
        while (messages.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        Message message = messages.get(messages.size() - 1);
        messages.remove(messages.size() - 1);
        return message;
    }

    public synchronized void put(Message message) {
        messages.add(message);
        notifyAll();
    }
}