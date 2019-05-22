package teamcode.framework.abstractopmodes;

import teamcode.bogiebase.hardware.RobotState;
import teamcode.framework.util.State;
import teamcode.framework.util.StateConfigurationException;
import teamcode.framework.util.StateMachine;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class AbstractAuton extends AbstractOpMode {

    private StateMachine stateMachine = new StateMachine();

    private static AbstractAuton abstractAutonNew;

    private int initLoops = 0;

    public AbstractAuton() {
        abstractAutonNew = this;
    }

    @Override
    public void runOpmode() {

        RobotState.currentMatchState = RobotState.MatchState.AUTONOMOUS;

        ExecutorService service = Executors.newSingleThreadExecutor();

        Callable<Boolean> InitThread = () -> {
            try {
                Init();
            } catch (Exception e) {
                throwException(e);
            }
            return true;
        };
        Callable<Boolean> InitLoopThread = () -> {
            try {
                InitLoop();
                InitLoop(initLoops);
            } catch (Exception e) {
                throwException(e);
            }
            return true;
        };

        Future<Boolean> CurrentFuture;

        //calls user init
        CurrentFuture = service.submit(InitThread);

        while (!isStopRequested() && !opModeIsActive()) {
            checkException();

            if (CurrentFuture.isDone()) {
                initLoops++;
                CurrentFuture = service.submit(InitLoopThread);
            }
        }

        while (!isStopRequested() && !CurrentFuture.isDone()) checkException();

        RegisterStates();

        try {
            stateMachine.prepare();
        } catch (StateConfigurationException e) {
            throwException(e);
        }

        addState(new State("run", "start", () -> {
            Run();
            return true;
        }));

        boolean stateMachineActive = true;

        while (opModeIsActive() && stateMachineActive) {
            checkException();

            stateMachineActive = stateMachine.update();
        }

        //TODO remake our shutdown procedure
        CurrentFuture.cancel(true);
        stateMachine.shutdown();

        while (!service.isTerminated()) {
            service.shutdownNow();
            checkException();
        }

        Stop();
    }

    public abstract void RegisterStates();

    public abstract void Init();

    public void InitLoop() {

    }

    public void InitLoop(int loops) {

    }

    public abstract void Run();

    public void Stop() {
    }

    public void addState(State state) {
        stateMachine.addState(state);
    }

    public static void addFinishedState(String state) {
        abstractAutonNew.stateMachine.addFinishedState(state);
    }
}