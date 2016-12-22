package learn.shoot;

import learn.algorithm.qLearning.Policy;
import learn.algorithm.qLearning.State;

import java.util.List;
import java.util.Map;

/**
 * Created by beckf on 22.12.2016.
 */
public class ShootState implements State {
    private ShootEnvironment shootEnvironment;

    private List<Integer> unknown;
    private List<Integer> ships;

    private Map<Integer, Double> actions;
    private Integer bestAction;
    private double bestActionValue;

    public ShootState(ShootEnvironment shootEnvironment, List<Integer> unknown,
                      List<Integer> ships, Map<Integer, Double> actions,
                      Integer bestAction, double bestActionValue) {
        this.shootEnvironment = shootEnvironment;
        this.unknown = unknown;
        this.ships = ships;

        this.actions = actions;
        this.bestAction = bestAction;
        this.bestActionValue = bestActionValue;
    }

    @Override
    public int getAction(Policy policy, double explorationRate) {
        int action = bestAction;
        if(shootEnvironment.getRandomDouble() <= explorationRate){

        }
        return 0;
    }

    @Override
    public State takeAction(int action) {
        return null;
    }

    @Override
    public void updateAction(double reward, State state) {

    }

    @Override
    public boolean isFinal() {
        return false;
    }
}
