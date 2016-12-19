import de.uniba.wiai.lspi.chord.com.CommunicationException;
import de.uniba.wiai.lspi.chord.com.Node;
import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.data.URL;
import de.uniba.wiai.lspi.chord.service.Chord;
import de.uniba.wiai.lspi.chord.service.ServiceException;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;
import de.uniba.wiai.lspi.util.logging.Logger;
import game.chord.NotifyCallbackImpl;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by FBeck on 15.12.2016.
 */
public class Main {
    private static Logger LOG = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        String protocol = URL.KNOWN_PROTOCOLS.get(URL.SOCKET_PROTOCOL);

        if (args[0].equals("debug")) {
            // Hier Chord etc. initialisieren
            de.uniba.wiai.lspi.chord.service.PropertiesLoader
                    .loadPropertyFile();

            URL localURL = null;
            try {
                localURL = new URL(protocol + "://localhost:8080/");
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            ChordImpl chord = new de.uniba.wiai.lspi.chord.service.impl.ChordImpl();
            chord.setCallback(new NotifyCallbackImpl());
            //chord.setCallback(new NotifyCallbackImpl(chord));

            try {
                chord.create(localURL);
                System.out.println(chord.getID().toString().substring(0, 5));
            } catch (ServiceException e) {
                throw new RuntimeException("Could not create DHT !", e);
            }

            for (int i = 0; i < 10; i++) {

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                URL localURL2 = null;
                try {
                    localURL2 = new URL(protocol + "://localhost:8" + (181 + i)
                            + "/");
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }

                URL bootstrapURL = null;
                try {
                    bootstrapURL = new URL(protocol + "://localhost:8080/");
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }

                Chord chord2 = new de.uniba.wiai.lspi.chord.service.impl.ChordImpl();
                chord2.setCallback(new NotifyCallbackImpl());
                try {
                    chord2.join(localURL2, bootstrapURL);
                    System.out.println(chord2.getID().toString()
                            .substring(0, 5));
                } catch (ServiceException e) {
                    throw new RuntimeException(" Could not join DHT ! ", e);
                }
            }

            System.out
                    .println("-----------------------------------------------------------");

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            Random rnd = new Random();
            BigInteger i = new BigInteger(160, rnd);



            chord.retrieve(ID.valueOf(i));
            chord.broadcast(ID.valueOf(i),true);

        }
        //else {
//			URL bootstrapURL;
//			try {
//				bootstrapURL = new URL(protocol + "://" + args[0] + "/");
//			} catch (MalformedURLException e) {
//				throw new RuntimeException(e);
//			}
//
//			URL localURL;
//			try {
//				bootstrapURL = new URL(protocol + "://" + args[0] + "/");
//			} catch (MalformedURLException e) {
//				throw new RuntimeException(e);
//			}
//
//			Chord chord = new de.uniba.wiai.lspi.chord.service.impl.ChordImpl();
//			chord.setCallback(new MyNotifyCallbackImpl(chord));
//			try {
//				chord.join(localURL, bootstrapURL);
//				System.out.println(chord.getID().toString()
//						.substring(0, 5));
//			} catch (ServiceException e) {
//				throw new RuntimeException(" Could not join DHT ! ", e);
//			}
        //}

    }
}
