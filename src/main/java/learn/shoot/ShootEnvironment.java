package learn.shoot;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import de.uniba.wiai.lspi.chord.data.ID;
import game.game.Game;
import game.game.player.Player;
import game.game.player.map.Field;
import game.game.ships.MyShips;
import learn.algorithm.qLearning.QLearning;
import org.bson.Document;

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
    public static final double ALPHA = 0.1;
    public static final double GAMMA = 0.1;
    public static final double EXPLORATIONRATE = 0.10;
    private static final String FILE = "scout.txt";

    private Gson gson;

    //private Game game;
    private MyShips myShips;
    private List<Integer> unknownFields;
    private List<Integer> shipFields;

    private List<Integer> unknown;
    private List<Integer> ships;
    private List<Integer> water;

    MongoCollection<Document> collection;

    public ShootEnvironment() {
        this.gson = new Gson();
        //this.game = new Game();
        this.myShips = new MyShips();
        this.unknownFields = new ArrayList<>();
        this.shipFields = new ArrayList<>();
        this.unknown = new ArrayList<>();
        this.ships = new ArrayList<>();
        this.water = new ArrayList<>();
        initiateShootEnvironment();
    }

    public ShootEnvironment(MongoCollection<Document> collection) {
        this.gson = new Gson();
        //this.game = new Game();
        this.myShips = new MyShips();
        this.unknownFields = new ArrayList<>();
        this.shipFields = new ArrayList<>();
        this.unknown = new ArrayList<>();
        this.ships = new ArrayList<>();
        this.water = new ArrayList<>();
        this.collection = collection;
        initiateShootEnvironment();
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
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public void initiateShootEnvironment() {
        if (collection == null) {
            MongoClient mongo = new MongoClient("localhost", 27017);
            MongoDatabase db = mongo.getDatabase("schiffe");
            collection = db.getCollection("scout");
        }

        byte[] one = hexStringToByteArray("49FD2733CFA85F4FEFED99204F9A126549397A79");
        BigInteger two = new BigInteger(one);
        ID id = ID.valueOf(two);
        //game.initGame(id, id, new ArrayList<>());
        myShips.initMyShips();
        for(int i = 0; i < Game.FIELDS; i++){
            unknownFields.add(i);
        }
    }

    public void run() {
        //Player player = game.getPlayer(game.getSelf());

        /*List<Integer> unknown = new ArrayList<>();
        List<Integer> ships = new ArrayList<>();

        for (Field fields : player.getFields()) {
            if (fields.isUnknown()) {
                unknown.add(fields.getFieldID());
            } else if (fields.isShip()) {
                ships.add(fields.getFieldID());
            }
        }*/
        List<Integer> unknown = new ArrayList<>();
        unknown.addAll(unknownFields);
        List<Integer> ships = new ArrayList<>();
        ships.addAll(shipFields);

        String id = ShootState.toID(unknown, ships);
        ShootState shootState = getShootState(id);
        if (shootState == null) {
            createShootState(ShootState.generateNewShootState(unknown, ships));
            shootState = getShootState(id);
        }
        shootState.setShootEnvironment(this);
        shootState.setUnknown(unknown);
        shootState.setShips(ships);

        QLearning qLearning = new QLearning();
        qLearning.algorithm(shootState);
        this.unknown.add(unknownFields.size());
        this.water.add(Game.FIELDS - unknownFields.size() - shipFields.size());
        this.ships.add(shipFields.size());
        //System.out.println("Ship: " + game.getPlayer(game.getSelf()).getShip() + " Unknown: " + game.getPlayer(game.getSelf()).getUnknown() + " Water: " + game.getPlayer(game.getSelf()).getWater());
    }

    public ShootState shootAt(int field) {
        /*Player player = game.getPlayer(game.getSelf());
        Field field1 = player.getFields().get(field);
        ID shoot = field1.getShootAt();

        boolean hit = game.gotShootAt(field1.getShootAt());
        */
        if(unknownFields.contains(field)){
            unknownFields.remove((Object) field);
            if(myShips.isShipAt(field)) {
                shipFields.add(field);
            }
        }
        else {
            System.out.println("dd");
        }

        List<Integer> unknown = new ArrayList<>();
        unknown.addAll(unknownFields);
        List<Integer> ships = new ArrayList<>();
        ships.addAll(shipFields);

        String id = ShootState.toID(unknown, ships);
        ShootState shootState = getShootState(id);

        if (shootState == null) {
            createShootState(ShootState.generateNewShootState(unknown, ships));
            shootState = getShootState(id);
        }
        shootState.setShootEnvironment(this);
        shootState.setUnknown(unknown);
        shootState.setShips(ships);
        return shootState;
    }

    public ShootState getShootState(String shootStateID) {
        Document query = new Document();
        query.put("id", shootStateID);

        FindIterable<Document> found = collection.find(query);
        Document foundDocument = found.first();
        ShootState state = null;
        if (foundDocument != null) {
            state = gson.fromJson(foundDocument.getString("json"), ShootState.class);
        }
        return state;
    }

    public void createShootState(ShootState shootState) {
        Document document = new Document();
        document.put("id", shootState.toID());
        document.put("json", gson.toJson(shootState));

        collection.insertOne(document);
    }

    public void updateShootState(ShootState shootState) {
        Document query = new Document();
        query.put("id", shootState.toID());

        Document update = new Document();
        update.put("$set", new Document().append("json", gson.toJson(shootState)));

        collection.updateOne(query, update);
    }

    public static void main(String[] args) {
        MongoClient mongo = new MongoClient("localhost", 27017);
        MongoDatabase db = mongo.getDatabase("schiffe");
        MongoCollection<Document> collection = db.getCollection("scout");
        //List<Integer> w = new ArrayList<>();
        //List<Integer> u = new ArrayList<>();
        //List<Integer> s = new ArrayList<>();
        //for (int i = 0; i < 100000; i++) {
        int i = 0;
        while (true) {
            ShootEnvironment shootEnvironment = new ShootEnvironment(collection);
            shootEnvironment.run();
            //shootEnvironment.save();
            if (i % 100 == 0) {
                int water = addList(shootEnvironment.getWater()) / shootEnvironment.getWater().size();
                int unknwon = addList(shootEnvironment.getUnknown()) / shootEnvironment.getUnknown().size();
                int ships = addList(shootEnvironment.getShips()) / shootEnvironment.getShips().size();
                shootEnvironment.setWater(new ArrayList<>());
                shootEnvironment.setShips(new ArrayList<>());
                shootEnvironment.setUnknown(new ArrayList<>());
                //w.add(water);
                //u.add(unknwon);
                //s.add(ships);
                System.out.println("%%%%Ship: " + ships + " Unknown: " + unknwon + " Water: " + water);
            }
            i++;
        }
        //int water = addList(w) / w.size();
        //int unknwon = addList(u) / u.size();
        //int ships = addList(s) / s.size();
        //System.out.println("%%%%%%%%Ship: " + ships + " Unknown: " + unknwon + " Water: " + water);
    }

    public static int addList(List<Integer> list) {
        int x = 0;
        for (Integer integer : list) {
            x = x + integer;
        }
        return x;
    }
}
