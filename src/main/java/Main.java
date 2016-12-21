import de.uniba.wiai.lspi.chord.data.URL;
import de.uniba.wiai.lspi.chord.service.PropertiesLoader;
import de.uniba.wiai.lspi.chord.service.ServiceException;
import de.uniba.wiai.lspi.chord.service.impl.ChordImpl;
import de.uniba.wiai.lspi.util.logging.Logger;
import game.agent.Agent;
import game.chord.EnemyCrawler;
import game.chord.NotifyCallbackImpl;
import game.messaging.Messages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

/**
 * Created by FBeck on 15.12.2016.
 */
public class Main {
    private static Logger LOG = Logger.getLogger(Main.class);
    private final static String PROTOCOL = URL.KNOWN_PROTOCOLS.get(URL.SOCKET_PROTOCOL);

    private ChordImpl chord;
    private EnemyCrawler enemyCrawler;
    private Agent agent;

    private String urlPart = "localhost";
    private String lurlPart = "localhost";
    private int port = 8080;
    private int lport = 8081;

    private boolean didCOJ;

    public static void main(String[] args) {
       /* String protocol = URL.KNOWN_PROTOCOLS.get(URL.SOCKET_PROTOCOL);

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

        }*/
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

        Main main = new Main();
        main.run();
    }

    private void run() {
        build();

        BufferedReader br = null;

        try {

            br = new BufferedReader(new InputStreamReader(System.in));
            first(br);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            throw new RuntimeException(" Could not join DHT ! ", e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void build(){
        PropertiesLoader.loadPropertyFile();
        chord = new ChordImpl();
        Messages messages = new Messages();
        NotifyCallbackImpl notifyCallback = new NotifyCallbackImpl(messages);
        chord.setCallback(notifyCallback);
        enemyCrawler = new EnemyCrawler(chord, messages);
        agent = new Agent(chord, messages);
    }

    public void start(){
        agent.initAgent();
        //enemyCrawler.run();
        agent.run();
    }

    private void first(BufferedReader br) throws IOException, ServiceException {
        System.out.println("What to do:");
        boolean run = true;
        while (run) {
            String input = br.readLine();

            if ("create".equals(input) && !didCOJ) {
                second(br);
                URL url = buildURL(urlPart, port);
                chord.create(url);
            }
            else if ("join".equals(input) && !didCOJ) {
                second(br);
                chord.join(buildURL(lurlPart, lport), buildURL(urlPart, port));
            }
            else if ("start".equals(input)) {
                start();
            }

            else if ("exit".equals(input)) {
                System.out.println("Exit!");
                run = false;
            }
        }
    }

    private void second(BufferedReader br) throws IOException {
        boolean run = true;
        System.out.println("url: " + urlPart + " port: " + port);
        System.out.println("lurl: " + lurlPart + " lport: " + lport);
        while (run) {
            String input = br.readLine();

            if ("url".equals(input)) {
                urlPart = br.readLine();
                System.out.println("url: " + urlPart);
            }
            else if ("port".equals(input)) {
                port = Integer.parseInt(br.readLine());
                System.out.println("port: " + port);
            }
            else if ("show".equals(input)) {
                System.out.println("url: " + urlPart + " port: " + port);
            }

            else if ("lurl".equals(input)) {
                lurlPart = br.readLine();
                System.out.println("lurl: " + lurlPart);
            }
            else if ("lport".equals(input)) {
                lport = Integer.parseInt(br.readLine());
                System.out.println("lport: " + lport);
            }
            else if ("lshow".equals(input)) {
                System.out.println("lurl: " + lurlPart + " lport: " + lport);
            }

            else if ("exit".equals(input)) {
                System.out.println("Exit!");
                run = false;
            }

            else {
                lport = lport + Integer.parseInt(input);
                System.out.println("lurl: " + lurlPart + " lport: " + lport);
                System.out.println("Exit!");
                run = false;
            }
        }
    }

    private URL buildURL(String urlPart, int port) {
        URL url = null;
        try {
            url = new URL(PROTOCOL + "://" + urlPart + ":" + port + "/");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return url;
    }
}
