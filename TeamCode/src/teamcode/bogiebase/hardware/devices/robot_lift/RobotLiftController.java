package teamcode.bogiebase.hardware.devices.robot_lift;

import teamcode.framework.abstractopmodes.AbstractOpMode;
import teamcode.framework.util.SubsystemController;

import static teamcode.bogiebase.hardware.Constants.*;
import static teamcode.bogiebase.hardware.RobotState.*;

public class RobotLiftController extends SubsystemController {

    private RobotLift robotLift;

    public RobotLiftController() {
        init();
    }

    public synchronized void init() {
        opModeSetup();

        robotLift = new RobotLift(hardwareMap);
    }

    public synchronized void update() {

    }

    public synchronized void stop() {
        robotLift.stop();
    }

    public synchronized void robotLiftUp() {
        robotLift.setLiftPower(1);
    }

    public synchronized void robotLiftStop() {
        // robot is hanging
        robotLift.setLiftPower(0);
        robotLift.setServoPosition(ROBOT_LIFT_PAWL_ENGAGED);
    }

    public synchronized void robotLiftDown() {
        robotLift.setPosition(robotLift.getTargetPosition() + ROBOT_LIFT_RELEASE_PAWL_POSITION);
        robotLift.setServoPosition(ROBOT_LIFT_PAWL_RELEASED);
        delay(1000);
        robotLift.setLiftPower(-0.5);
    }

    public synchronized void raiseLift() {
        robotLift.setPosition(ROBOT_LIFT_RAISED_POSITION);
        currentRobotLiftState = RobotLiftState.RAISED;
    }

    public synchronized void autonLowerLiftSequence() {
        currentRobotLiftState = RobotLiftState.IN_MOTION;

        delay(ROBOT_LIFT_AUTON_DELAY);

        robotLift.setPosition(ROBOT_LIFT_RELEASE_PAWL_POSITION);
        robotLift.setServoPosition(ROBOT_LIFT_PAWL_RELEASED);

        delay(ROBOT_LIFT_AUTON_DELAY);

        robotLift.setLiftNoEncoderPower(ROBOT_LIFT_LOWER_POWER);

        while (AbstractOpMode.isOpModeActive() && (robotLift.getCurrentPosition() >= ROBOT_LIFT_LOWERED_POSITION)) ;

        robotLift.setPosition(ROBOT_LIFT_LOWERED_POSITION);
    }

    public synchronized void autonFinishLowerLiftSequence() {
        robotLift.setLiftPower(0);
        robotLift.setServoPosition(ROBOT_LIFT_PAWL_ENGAGED);
        telemetry.addData("Encoder", robotLift.getCurrentPosition());
        currentRobotLiftState = RobotLiftState.LOWERED;
    }

    public synchronized void resetLiftPosition() {
        robotLift.setPosition(0);
        delay(1000);
        robotLift.setLiftPower(0);
    }
}
