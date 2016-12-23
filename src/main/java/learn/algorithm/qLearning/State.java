package learn.algorithm.qLearning;

/**
 * Created by beckf on 22.12.2016.
 */
public interface State {

    double getBestActionValue();
    int getAction(Policy policy, double explorationRate);
    State takeAction(int action);

    void updateAction(int action, double reward, double bestActionValue, double alpha, double gamma);
    boolean isFinal();

    double getReward(State subsequentState);
}
