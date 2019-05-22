package teamcode.framework.util;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StateMachine {

    private ArrayList<State> states = new ArrayList<>(), activeStates = new ArrayList<>(), finishedStates = new ArrayList<>(), startingStates = new ArrayList<>();

    private ExecutorService service;

    public StateMachine() {
        service = Executors.newCachedThreadPool();
        activeStates.add(new State("start", "", () -> true));
    }

    public synchronized void prepare() throws StateConfigurationException {
        for (State state : states) {
            if (state instanceof PathState) continue;

            boolean noPrevious = true;

            for (State checkState : states) {
                if (state.getPreviousState().equals(checkState.getName())) noPrevious = false;
            }

            if (state.getPreviousState().equals("start")) noPrevious = false;

            if (noPrevious)
                throw new StateConfigurationException(state.getName(), state.getPreviousState());
        }
    }

    public synchronized boolean update() {
        for (State activeState : activeStates) {
            if (activeState.isDone()) {
                activeState.getValue();
                finishedStates.add(activeState);
                for (State state : states) {
                    if (state.getPreviousState().equals(activeState.getName())) fire(state);
                }
            }
        }

        activeStates.removeAll(finishedStates);
        activeStates.addAll(startingStates);

        finishedStates.clear();
        startingStates.clear();

        if (activeStates.size() <= 0) return false;
        return true;
    }

    public synchronized void addFinishedState(String stateName) {
        for (State state : states) {
            if (state.getPreviousState().equals(stateName)) fire(state);
        }
    }

    private synchronized void fire(State state) throws RuntimeException {
        if (state.getRun() != null) {
            startingStates.add(state);
            state.setFuture(service.submit(state.getRun()));
        }
    }

    public synchronized void addState(State state) {
        states.add(state);
    }

    public synchronized void shutdown() {
        for (State activeState : activeStates) {
            activeState.cancel();
        }
    }
}