package learn.shoot;

import game.game.Game;
import learn.algorithm.qLearning.Policy;
import learn.algorithm.qLearning.State;

import java.util.*;

/**
 * Created by beckf on 22.12.2016.
 */
public class ShootState implements State {

    private transient ShootEnvironment shootEnvironment;

    private transient List<Integer> unknown;
    private transient List<Integer> ships;

    private Map<Integer, Double> actions;
    private List<Integer> bestActions;
    private double bestActionValue;

    public ShootState() {
    }

    private ShootState(List<Integer> unknown,
                       List<Integer> ships, Map<Integer, Double> actions,
                       List<Integer> bestActions, double bestActionValue) {
        this.shootEnvironment = shootEnvironment;
        this.unknown = unknown;
        this.ships = ships;

        this.actions = actions;
        this.bestActions = bestActions;
        this.bestActionValue = bestActionValue;
    }

    public ShootEnvironment getShootEnvironment() {
        return shootEnvironment;
    }

    public List<Integer> getUnknown() {
        return unknown;
    }

    public List<Integer> getShips() {
        return ships;
    }

    public Map<Integer, Double> getActions() {
        return actions;
    }

    public void setShootEnvironment(ShootEnvironment shootEnvironment) {
        this.shootEnvironment = shootEnvironment;
    }

    public void setUnknown(List<Integer> unknown) {
        this.unknown = unknown;
    }

    public void setShips(List<Integer> ships) {
        this.ships = ships;
    }

    public double getBestActionValue() {
        return bestActionValue;
    }

    @Override
    public int getAction(Policy policy, double explorationRate) {
        Collections.shuffle(bestActions);
        int action = bestActions.get(0);
        double random = shootEnvironment.getRandomDouble();
        if (random <= explorationRate) {
            int ran = shootEnvironment.getRandomInteger(unknown.size());
            action = unknown.get(ran);
        }
        return action;
    }

    @Override
    public State takeAction(int action) {
        return shootEnvironment.shootAt(action);
    }

    @Override
    public void updateAction(int action, double reward, double bestActionValue, double alpha, double gamma) {
        double actionValue = actions.get(action) + alpha * (reward + gamma * bestActionValue - actions.get(action));

        actions.put(action, actionValue);
        if (bestActions.contains(action)) {
            this.bestActionValue = Double.NEGATIVE_INFINITY;
            for (Map.Entry<Integer, Double> entry : actions.entrySet()) {
                if (entry.getValue() == this.bestActionValue) {
                    this.bestActions.add(entry.getKey());
                } else if (entry.getValue() > this.bestActionValue) {
                    this.bestActions = new ArrayList<>();
                    this.bestActions.add(entry.getKey());
                    this.bestActionValue = entry.getValue();
                }
            }
        }

        shootEnvironment.updateShootState(this);
    }

    @Override
    public boolean isFinal() {
        boolean isFinal = false;
        if (ships.size() == Game.SHIPS) {
            isFinal = true;
        }
        return isFinal;
    }

    @Override
    public double getReward(State subsequentState) {
        ShootState shootState = (ShootState) subsequentState;
        double reward = -2;
        if (unknown.size() > shootState.getUnknown().size()) {
            if (ships.size() < shootState.getShips().size()) {
                reward = 5;
            }
        }
        return reward;
    }

    public String toID() {
        return toID(unknown, ships);
    }

    public static String toID(List<Integer> unknown, List<Integer> ships) {
        String id = "";
        for (int i = 0; i < Game.FIELDS; i++) {
            int x = i + 1;
            if (unknown.contains(i)) {
                id = id + "0";
            } else if (ships.contains(i)) {
                id = id + "1";
            } else {
                id = id + "2";
            }
        }
        return id;
    }

    public static ShootState generateNewShootState(List<Integer> unknown, List<Integer> ships) {
        Map<Integer, Double> actions = new HashMap<>();

        for (Integer i : unknown) {
            actions.put(i, 0.0);
        }

        double bestValue = 0.0;
        int i = 0;
        if (!unknown.isEmpty()) {
            i = unknown.get(0);
        }

        return new ShootState(unknown, ships, actions, unknown, bestValue);
    }
}
