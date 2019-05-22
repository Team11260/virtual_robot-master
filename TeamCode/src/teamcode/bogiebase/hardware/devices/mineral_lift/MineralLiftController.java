package teamcode.bogiebase.hardware.devices.mineral_lift;

import teamcode.bogiebase.hardware.Constants;
import teamcode.framework.util.SubsystemController;
import virtual_robot.util.time.ElapsedTime;

import static teamcode.bogiebase.hardware.Constants.*;
import static teamcode.bogiebase.hardware.RobotState.*;

public class MineralLiftController extends SubsystemController {

    private MineralLift mineralLift;
    private boolean isMovingDown = false;
    private int[] liftValues = {-1, -1, -1, -1, -1};

    private ElapsedTime cycleTimer, moveTime;

    public MineralLiftController() {
        init();
    }

    public synchronized void init() {
        opModeSetup();

        mineralLift = new MineralLift(hardwareMap);

        cycleTimer = new ElapsedTime();
        cycleTimer.reset();

        moveTime = new ElapsedTime();
        moveTime.reset();
    }

    public synchronized void update() {
        if (isMovingDown) {
            int currentValue = mineralLift.getCurrentPosition();

            if (mineralLift.getPower() == -1 && currentValue < MINERAL_LIFT_SLOW_SPEED_TRIGGER_POSITION) {
                mineralLift.setLiftMotorPowerNoEncoder(-MINERAL_LIFT_SLOW_SPEED);
            }

            if (liftValues[0] == -1) {
                liftValues[0] = 10000;
                liftValues[1] = 0;
                liftValues[2] = -10000;
                liftValues[3] = -20000;
                liftValues[4] = -30000;
                return;
            }

            liftValues[4] = liftValues[3];
            liftValues[3] = liftValues[2];
            liftValues[2] = liftValues[1];
            liftValues[1] = liftValues[0];
            liftValues[0] = currentValue;

            if (atPosition(liftValues[0], liftValues[1], MINERAL_LIFT_DOWN_DETECT_ENCODER_COUNTS) && atPosition(liftValues[1], liftValues[2], MINERAL_LIFT_DOWN_DETECT_ENCODER_COUNTS) &&
                    atPosition(liftValues[2], liftValues[3], MINERAL_LIFT_DOWN_DETECT_ENCODER_COUNTS) && atPosition(liftValues[3], liftValues[4], MINERAL_LIFT_DOWN_DETECT_ENCODER_COUNTS)) {
                telemetry.addData("Mineral lift down finished in", moveTime.milliseconds());
                mineralLift.resetPosition();
                liftValues[0] = -1;
                liftValues[1] = -1;
                liftValues[2] = -1;
                liftValues[3] = -1;
                liftValues[4] = -1;
                currentMineralLiftState = MineralLiftState.COLLECT_POSITION;
                isMovingDown = false;
                return;
            }
        }
    }

    public synchronized void stop() {
        mineralLift.stop();
    }

    public synchronized void autonLowerLiftSequence() {
        mineralLift.setTargetPosition(MINERAL_LIFT_AUTON_RAISED_POSITION);

        delay(500);

        mineralLift.setAngleServoPosition(MINERAL_LIFT_ANGLE_SERVO_HORIZONTAL_POSITION);

        delay(1000);

        mineralLift.setLiftMotorPowerNoEncoder(-MINERAL_LIFT_SLOW_SPEED);

        delay(1000);

        mineralLift.resetPosition();
    }

    public synchronized void autonMoveToCollectPositionSequence() {
        moveTime.reset();

        closeGate();

        isMovingDown = true;

        mineralLift.setAngleServoPosition(MINERAL_LIFT_ANGLE_SERVO_HORIZONTAL_POSITION);

        mineralLift.setLiftMotorPowerNoEncoder(-MINERAL_LIFT_SLOW_SPEED);

        delay(2000);

        mineralLift.resetPosition();
    }

    public synchronized void autonMoveToDumpPositionSequence() {
        currentPath.pause();
        moveTime.reset();
        telemetry.addData("", "Mineral lift up start");
        telemetry.update();
        currentMineralLiftState = MineralLiftState.IN_MOTION;
        mineralLift.setAngleServoPosition(MINERAL_LIFT_ANGLE_SERVO_DUMP_POSITION);
        mineralLift.setLiftMotorPowerNoEncoder(1);
        while (mineralLift.getCurrentPosition() < MINERAL_LIFT_DUMP_POSITION && moveTime.milliseconds() < 3000 && opModeIsActive());
        currentMineralLiftState = MineralLiftState.DUMP_POSITION;
        mineralLift.setLiftMotorPower(0);
        telemetry.addData("Mineral up finished in", moveTime.milliseconds());
        telemetry.update();
        currentPath.resume();
    }

    public synchronized void moveToCollectPosition() {
        moveTime.reset();
        if (mineralLift.getDistance() < 10) return;
        currentMineralLiftState = MineralLiftState.IN_MOTION;
        mineralLift.setLiftMotorPowerNoEncoder(-MINERAL_LIFT_FULL_SPEED);
        isMovingDown = true;
        mineralLift.setAngleServoPosition(MINERAL_LIFT_ANGLE_SERVO_HORIZONTAL_POSITION);
        delay(200);
    }

    public synchronized void moveToDumpPosition() {
        isMovingDown = false;
        moveTime.reset();
        telemetry.addData("", "Mineral lift up start");
        telemetry.update();
        currentMineralLiftState = MineralLiftState.IN_MOTION;
        mineralLift.setLiftMotorPowerNoEncoder(MINERAL_LIFT_FULL_SPEED);
        delay(200);
        mineralLift.setAngleServoPosition(MINERAL_LIFT_ANGLE_SERVO_VERTICAL_POSITION);
        while (mineralLift.getCurrentPosition() < MINERAL_LIFT_DUMP_ANGLE_TRIGGER_POSITION && moveTime.milliseconds() < 2000);
        mineralLift.setAngleServoPosition(MINERAL_LIFT_ANGLE_SERVO_DUMP_POSITION);
        while (mineralLift.getCurrentPosition() < MINERAL_LIFT_DUMP_POSITION && moveTime.milliseconds() < 2000);
        currentMineralLiftState = MineralLiftState.DUMP_POSITION;
        mineralLift.setLiftMotorPower(0);
        telemetry.addData("Mineral up finished in", moveTime.milliseconds());
        telemetry.update();
    }

    public synchronized void openGate() {
        mineralLift.setGateServoPosition(MINERAL_LIFT_GATE_OPEN_POSITION);
        currentMineralGatePosition = MineralGatePosition.OPEN;
        telemetry.addData("Cycle time", cycleTimer.seconds());
        cycleTimer.reset();
    }

    public synchronized void closeGate() {
        mineralLift.setGateServoPosition(MINERAL_LIFT_GATE_CLOSED_POSITION);
        currentMineralGatePosition = MineralGatePosition.CLOSED;
    }

    public synchronized void toggleGate() {
        if (currentMineralGatePosition == MineralGatePosition.OPEN) closeGate();
        else if (currentMineralLiftState == MineralLiftState.DUMP_POSITION) openGate();
    }

    public synchronized void setAngleServoPositionDump() {
        mineralLift.setAngleServoPosition(Constants.MINERAL_LIFT_ANGLE_SERVO_DUMP_POSITION);
    }

    public synchronized void setAngleServoPositionHorizontal() {
        mineralLift.setAngleServoPosition(Constants.MINERAL_LIFT_ANGLE_SERVO_HORIZONTAL_POSITION);
    }

    public synchronized void setAngleServoPositionVertical() {
        mineralLift.setAngleServoPosition(Constants.MINERAL_LIFT_ANGLE_SERVO_VERTICAL_POSITION);
    }

    public synchronized void setAngleServoPosition(double position) {
        mineralLift.setAngleServoPosition(position);
    }
}
