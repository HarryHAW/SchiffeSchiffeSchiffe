package learn.algorithm.qLearning;

import learn.shoot.ShootEnvironment;

/**
 * Created by beckf on 22.12.2016.
 */
public class QLearning {
    private static final Policy policy = Policy.BEST;

    public QLearning() {}

    // https://webdocs.cs.ualberta.ca/~sutton/book/ebook/node65.html
    public void algorithm(State state) {
        while (!state.isFinal()) {
            int action = state.getAction(policy, ShootEnvironment.EXPLORATIONRATE);
            State subsequentState = state.takeAction(action);
            double reward = state.getReward(subsequentState);
            state.updateAction(action, reward, subsequentState.getBestActionValue(), ShootEnvironment.ALPHA, ShootEnvironment.GAMMA);
            state = subsequentState;
        }
    }
}
