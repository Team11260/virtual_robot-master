package teamcode.framework.abstractopmodes;


import teamcode.bogiebase.hardware.RobotState;
import teamcode.framework.util.Emitter;
import virtual_robot.util.time.ElapsedTime;


import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class AbstractTeleop extends AbstractOpMode {

    //Setup gamepad
    private Emitter emitter = new Emitter();
    private ElapsedTime emitTime;
    private long emitTimeOffset = 0;
    private int emitLoop = 0;

    ButtonStateMap states;
    FloatStateMap floatStates;

    public AbstractTeleop() {

    }

    @Override
    public void runOpmode() {

        RobotState.currentMatchState = RobotState.MatchState.TELEOP;

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
            } catch (Exception e) {
                throwException(e);
            }
            return true;
        };
        Callable<Boolean> StartThread = () -> {
            try {
                Start();
            } catch (Exception e) {
                throwException(e);
            }
            return true;
        };
        Callable<Boolean> LoopThread = () -> {
            try {
                Loop();
            } catch (Exception e) {
                throwException(e);
            }
            return true;
        };

        Future<Boolean> CurrentFuture;

        //sets up emitter
        emitTime = new ElapsedTime();
        states = new ButtonStateMap();
        floatStates = new FloatStateMap();

        //calls user init
        CurrentFuture = service.submit(InitThread);

        int initLoops = 0;

        while (!isStopRequested() && !opModeIsActive()) {
            checkException();

            if (CurrentFuture.isDone()) {
                initLoops++;
                CurrentFuture = service.submit(InitLoopThread);
            }
        }

        while (!isStopRequested() && !CurrentFuture.isDone()) ;

        if (!isStopRequested()) {
            checkException();

            CurrentFuture = service.submit(StartThread);
        }

        while (!isStopRequested() && !CurrentFuture.isDone()) ;

        RegisterEvents();

        emitTime.reset();

        while (opModeIsActive()) {
            checkException();

            //checks the gamepad for changes
            checkEvents();

            //calls user loop
            if (CurrentFuture.isDone()) {
                CurrentFuture = service.submit(LoopThread);
            }
        }

        //AbstractOpMode.stopRequested();

        //TODO remake our shutdown procedure
        CurrentFuture.cancel(true);
        emitter.shutdown();

        while (!service.isTerminated()) {
            service.shutdownNow();
            checkException();
        }

        Stop();
    }

    public abstract void RegisterEvents();

    public abstract void UpdateEvents();

    public abstract void Init();

    public void InitLoop() {

    }

    public void Start() {

    }

    public abstract void Loop();

    public abstract void Stop();

    public void addEventHandler(String name, Callable event) {
        emitter.on(name, event);
    }

    public void pauseEvent(String name) {
        emitter.pauseEvent(name);
    }

    public void resumeEvent(String name) {
        emitter.resumeEvent(name);
    }

    public void removeEvent(String name) {
        emitter.removeEvent(name);
    }

    private void checkEvents() {
        if (emitTime.milliseconds() - emitTimeOffset < 1) return;
        emitTimeOffset++;
        if (emitTimeOffset > 60000) {
            emitTimeOffset = 0;
            emitTime.reset();
        }

        // boolean buttons
        checkBooleanInput("1_a", gamepad1.a);
        checkBooleanInput("1_b", gamepad1.b);
        checkBooleanInput("1_x", gamepad1.x);
        checkBooleanInput("1_y", gamepad1.y);
        checkBooleanInput("1_lb", gamepad1.left_bumper);
        checkBooleanInput("1_rb", gamepad1.right_bumper);
        checkBooleanInput("1_dpl", gamepad1.dpad_left);
        checkBooleanInput("1_dpr", gamepad1.dpad_right);
        checkBooleanInput("1_dpu", gamepad1.dpad_up);
        checkBooleanInput("1_dpd", gamepad1.dpad_down);
        checkBooleanInput("1_start", gamepad1.start);
        checkBooleanInput("1_back", gamepad1.back);
        checkBooleanInput("1_lsb", gamepad1.left_stick_button);
        checkBooleanInput("1_rsb", gamepad1.right_stick_button);

        // float buttons
        checkFloatInput("1_lt", gamepad1.left_trigger);
        checkFloatInput("1_rt", gamepad1.right_trigger);
        checkFloatInput("1_lsx", gamepad1.left_stick_x);
        checkFloatInput("1_lsy", gamepad1.left_stick_y);
        checkFloatInput("1_rsx", gamepad1.right_stick_x);
        checkFloatInput("1_rsy", gamepad1.right_stick_y);

        //Check user events
        UpdateEvents();

        //Update value for repeat on checkBooleanInput
        emitLoop++;
        if (emitLoop > 10000) {
            emitLoop = 0;
        }
    }

    public void checkBooleanInput(String name, boolean val) {
        if (name.contains("down")) {
            if (val) {
                emitter.emit(name);
            }
        } else if (name.contains("up")) {
            if (!val) {
                emitter.emit(name);
            }
        } else if (states.isChanged(name, val)) {
            emitter.emit(val ? name + "_down" : name + "_up");
            states.change(name, val);
        }
    }

    public void checkBooleanInput(String name, boolean val, int repeat) {
        if (emitLoop % repeat == 0) {
            checkBooleanInput(name, val);
        }
    }

    public void checkFloatInput(String name, float val) {
        if (floatStates.isChanged(name, val)) {
            emitter.emit(name + "_change");
            floatStates.change(name, val);
        }
    }

    // This is a lazy map. It only tracks buttons that have been pressed. It can
    // easily scale to handle as many buttons as you have unique names.
    class ButtonStateMap {

        ConcurrentHashMap<String, Boolean> state;

        ButtonStateMap() {
            state = new ConcurrentHashMap<String, Boolean>();
        }

        boolean isChanged(String name, boolean newVal) {
            if (!state.containsKey(name)) {
                state.put(name, newVal);
                return newVal;
            }

            return state.get(name) != newVal;
        }

        void change(String name, boolean newVal) {
            state.replace(name, newVal);
        }
    }

    // This is a float variant of a ButtonStateMap.
    class FloatStateMap {

        ConcurrentHashMap<String, Float> state;

        FloatStateMap() {
            state = new ConcurrentHashMap<String, Float>();
        }

        boolean isChanged(String name, float newVal) {
            if (!state.containsKey(name)) {
                // If it's not 0, it's changed
                state.put(name, newVal);
                return newVal == 0.0;
            }

            return state.get(name) != newVal;
        }

        void change(String name, float newVal) {
            state.replace(name, newVal);
        }
    }
}