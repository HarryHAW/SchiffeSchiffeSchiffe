package learn.algorithm.qLearning;

/**
 * Created by beckf on 22.12.2016.
 */
public class QLearning {
    private static final Policy policy = Policy.BEST;

    private Rewarder rewarder;

    public QLearning(Rewarder rewarder) {
        this.rewarder = rewarder;
    }

    public void initialize() {

    }

    // https://webdocs.cs.ualberta.ca/~sutton/book/ebook/node65.html
    public void algorithm(State state) {
        while (!state.isFinal()) {
           //TODO explorationRate is set Randomly!
            int i = state.getAction(policy, 2);
            State subsequentState = state.takeAction(i);
            double reward = rewarder.getReward(state, subsequentState);
            state.updateAction(reward, subsequentState);
            state = subsequentState;
        }
    }
}
