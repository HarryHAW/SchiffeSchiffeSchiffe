package learn.algorithm.qLearning;

/**
 * Created by beckf on 22.12.2016.
 */
public interface Rewarder {

    double getReward(State state, State subsequentState);
}
