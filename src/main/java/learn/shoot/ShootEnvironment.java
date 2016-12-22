package learn.shoot;

import java.util.Random;

/**
 * Created by beckf on 22.12.2016.
 */
public class ShootEnvironment {
    private static final int RANDOMRUNS = 10;

    private Random random;

    public double getRandomDouble(){
        double value = 0;
        for (int i = 0; i < RANDOMRUNS; i++) {
            value = value + random.nextDouble();
        }
        value = value / RANDOMRUNS;
        return value;
    }

    public int getRandomInteger(int bound){
        return 0;
    }
}
