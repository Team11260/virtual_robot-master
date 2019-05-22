package teamcode.bogiebase.hardware.devices.intake;

import teamcode.framework.abstractopmodes.AbstractOpMode;
import teamcode.framework.util.SubsystemController;

import static teamcode.bogiebase.hardware.Constants.*;
import static teamcode.bogiebase.hardware.RobotState.*;

public class IntakeController extends SubsystemController {

    private Intake intake;

    public IntakeController() {
        init();
    }

    public synchronized void init() {

        opModeSetup();

        intake = new Intake(hardwareMap);
    }

    public synchronized void update() {

    }

    public synchronized void stop() {
        intake.stop();
    }

    public synchronized void autonIntakeSequence() {
        while ((!currentPath.getCurrentSegment().getName().equals("drive to minerals") &&
                !currentPath.getCurrentSegment().getName().equals("back up from minerals")) && AbstractOpMode.isOpModeActive());
        beginIntaking();
        while ((currentPath.getCurrentSegment().getName().equals("drive to minerals") ||
                currentPath.getCurrentSegment().getName().equals("back up from minerals")) && AbstractOpMode.isOpModeActive());
        finishIntaking();
    }

    public synchronized void beginIntaking() {
        if (currentIntakeLiftState == IntakeLiftState.LOWERED)
            intake.setIntakePower(INTAKE_FORWARD_POWER);
        else lowerIntake();
    }

    public synchronized void finishIntaking() {
        intake.setIntakePower(INTAKE_STOP_POWER);
    }

    public synchronized void reverseIntake() {
        if (currentIntakeLiftState == IntakeLiftState.LOWERED)
            intake.setIntakePower(INTAKE_REVERSE_POWER);
    }

    public synchronized void lowerIntake() {
        currentIntakeLiftState = IntakeLiftState.IN_MOTION;
        intake.setLiftServoPosition(INTAKE_LIFT_LOWERED_POSITION);
        intake.setIntakePower(INTAKE_REVERSE_POWER);
        delay(250);
        intake.setIntakePower(INTAKE_FORWARD_POWER);
        currentIntakeLiftState = IntakeLiftState.LOWERED;
    }

    public synchronized void liftIntake() {
        currentIntakeLiftState = IntakeLiftState.IN_MOTION;
        intake.setLiftServoPosition(INTAKE_LIFT_RAISED_POSITION);
        intake.setIntakePower(INTAKE_LOWER_POWER);
        delay(1000);
        intake.setIntakePower(INTAKE_STOP_POWER);
        currentIntakeLiftState = IntakeLiftState.RAISED;
    }
}