package learn.algorithm.qLearning;

/**
 * Created by beckf on 22.12.2016.
 */
public interface State {

    int getAction(Policy policy, double explorationRate);
    State takeAction(int action);

    void updateAction(double reward, State state);
    boolean isFinal();
}
