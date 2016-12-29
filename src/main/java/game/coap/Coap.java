package game.coap;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

/**
 * Created by FBeck on 21.12.2016.
 */
public class Coap {
    private static final boolean coap = false;
    public static final String OFF = "0";
    public static final String ON = "1";
    public static final String BLUE = "b";
    public static final String RED = "r";
    public static final String GREEN = "g";
    public static final String VIOLET = "v";
    public static String COLORSTATE = "";
    private CoapClient coapClient;

    public Coap(String coap){
        if(this.coap) {
            coapClient = new CoapClient(coap);
        }
    }

    public void changeColorTo(String color){
        if(coap) {
            if (COLORSTATE != color) {
                //  CoapResponse response = coapClient.put(OFF, MediaTypeRegistry.TEXT_PLAIN);
                if (color == VIOLET) {
                    coapClient.put(BLUE, MediaTypeRegistry.TEXT_PLAIN);
                    coapClient.put(RED, MediaTypeRegistry.TEXT_PLAIN);
                } else {
                    coapClient.put(color, MediaTypeRegistry.TEXT_PLAIN);
                }
                COLORSTATE = color;
            }
        }
    }

}