package learn.shoot;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.uniba.wiai.lspi.chord.data.ID;
import game.game.Game;
import game.game.player.Player;
import game.game.player.map.Field;
import learn.algorithm.qLearning.QLearning;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by beckf on 22.12.2016.
 */
public class ShootEnvironment {
    private static final int RANDOMRUNS = 10;
    public static final double ALPHA = 0.1;
    public static final double GAMMA = 0.1;
    public static final double EXPLORATIONRATE = 0.10;
    private static final String FILE = "scout.txt";

    private Gson gson;
    private Random random;

    private Map<String, ShootState> shootStates;

    private Game game;

    private List<Integer> unknown;
    private List<Integer> ships;
    private List<Integer> water;

    public ShootEnvironment(Map<String, ShootState> shootStates){
        this.gson = new Gson();
        this.random = new Random(System.nanoTime());
        this.game = new Game();
        this.unknown = new ArrayList<>();
        this.ships = new ArrayList<>();
        this.water = new ArrayList<>();
        initiateShootEnvironment(shootStates);
    }

    public List<Integer> getUnknown() {
        return unknown;
    }

    public List<Integer> getShips() {
        return ships;
    }

    public List<Integer> getWater() {
        return water;
    }

    public Map<String, ShootState> getShootStates() {
        return shootStates;
    }

    public void setUnknown(List<Integer> unknown) {
        this.unknown = unknown;
    }

    public void setShips(List<Integer> ships) {
        this.ships = ships;
    }

    public void setWater(List<Integer> water) {
        this.water = water;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public void initiateShootEnvironment(Map<String, ShootState> shootStates) {
        String str = "";
        try
        {
            File file = new File(FILE);
            // Open an input stream
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            str = new String(data, "UTF-8");
        }
        // Catches any error conditions
        catch (IOException e)
        {
            System.err.println ("Unable to read from file");
            System.exit(-1);
        }


        //shootStates = gson
        Type listTypeStations = new TypeToken<Map<String, ShootState>>(){}.getType();
        //shootStates = gson.fromJson(str, listTypeStations);
        this.shootStates = shootStates;

        byte[] one = hexStringToByteArray("49FD2733CFA85F4FEFED99204F9A126549397A79");
        BigInteger two = new BigInteger(one);
        ID id = ID.valueOf(two);
        game.initGame(id, id, new ArrayList<>());
    }

    public void run(){
        Player player = game.getPlayer(game.getSelf());

        List<Integer> unknown = new ArrayList<>();
        List<Integer> ships = new ArrayList<>();

        for (Field fields: player.getFields()) {
            if(fields.isUnknown()){
                unknown.add(fields.getFieldID());
            }
            else if(fields.isShip()){
                ships.add(fields.getFieldID());
            }
        }

        ShootState shootState = ShootState.generateNewShootState(unknown, ships);
        shootState.setShootEnvironment(this);
        shootState.setUnknown(unknown);
        shootState.setShips(ships);

        QLearning qLearning = new QLearning();
        qLearning.algorithm(shootState);
        this.unknown.add(game.getPlayer(game.getSelf()).getUnknown());
        this.water.add(game.getPlayer(game.getSelf()).getWater());
        this.ships.add(game.getPlayer(game.getSelf()).getShip());
        //System.out.println("Ship: " + game.getPlayer(game.getSelf()).getShip() + " Unknown: " + game.getPlayer(game.getSelf()).getUnknown() + " Water: " + game.getPlayer(game.getSelf()).getWater());
    }

    public void save(){
        String str = gson.toJson(shootStates);
        try
        {
            File file = new File(FILE);
            // Open an input stream
            FileOutputStream fou = new FileOutputStream(file);
            fou.write(str.getBytes());
            fou.close();
        }
        // Catches any error conditions
        catch (IOException e)
        {
            System.err.println ("Unable to read from file");
            System.exit(-1);
        }
    }

    public double getRandomDouble(){
        double value = 0;
        for (int i = 0; i < RANDOMRUNS; i++) {
            value = value + random.nextDouble();
        }
        value = value / RANDOMRUNS;
        return value;
    }

    public int getRandomInteger(int bound){
        return random.nextInt(bound);
    }

    public ShootState shootAt(int field){
        Player player = game.getPlayer(game.getSelf());
        Field field1 = player.getFields().get(field);
        ID shoot = field1.getShootAt();

        boolean hit = game.gotShootAt(field1.getShootAt());

        List<Integer> unknown = new ArrayList<>();
        List<Integer> ships = new ArrayList<>();

        for (Field fields: player.getFields()) {
            if(fields.isUnknown()){
                unknown.add(fields.getFieldID());
            }
            else if(fields.isShip()){
                ships.add(fields.getFieldID());
            }
        }

        String id = ShootState.toID(unknown, ships);
        if(!shootStates.containsKey(id)){
             shootStates.put(id, ShootState.generateNewShootState(unknown, ships));
        }
        ShootState shootState = shootStates.get(id);
        shootState.setShootEnvironment(this);
        shootState.setUnknown(unknown);
        shootState.setShips(ships);
        return shootState;
    }

    public static void main(String[] args) {
        Map<String, ShootState> shootStates = new HashMap<>();
        List<Integer> w = new ArrayList<>();
        List<Integer> u = new ArrayList<>();
        List<Integer> s = new ArrayList<>();
        for(int i = 0; i < 100000; i++) {
            ShootEnvironment shootEnvironment = new ShootEnvironment(shootStates);
            shootEnvironment.run();
            shootStates = shootEnvironment.getShootStates();
            //shootEnvironment.save();
            if(i%100 == 0){
                int water = addList(shootEnvironment.getWater()) / shootEnvironment.getWater().size();
                int unknwon = addList(shootEnvironment.getUnknown()) / shootEnvironment.getUnknown().size();
                int ships = addList(shootEnvironment.getShips()) / shootEnvironment.getShips().size();
                shootEnvironment.setWater(new ArrayList<>());
                shootEnvironment.setShips(new ArrayList<>());
                shootEnvironment.setUnknown(new ArrayList<>());
                w.add(water);
                u.add(unknwon);
                s.add(ships);
                System.out.println("%%%%Ship: " + ships + " Unknown: " + unknwon + " Water: " + water);
            }
        }
        int water = addList(w) / w.size();
        int unknwon = addList(u) / u.size();
        int ships = addList(s) / s.size();
        System.out.println("%%%%%%%%Ship: " + ships + " Unknown: " + unknwon + " Water: " + water);
    }

    public static int addList(List<Integer> list){
        int x = 0;
        for (Integer integer: list) {
            x = x + integer;
        }
        return x;
    }
}
