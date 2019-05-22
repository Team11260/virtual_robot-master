package teamcode.bogiebase.hardware.devices.mineral_lift;

import teamcode.bogiebase.hardware.Constants;
import teamcode.bogiebase.hardware.RobotState;
import teamcode.framework.userhardware.inputs.sensors.DistanceSensor2m;
import teamcode.framework.userhardware.outputs.SlewDcMotor;
import virtual_robot.hardware.DcMotor;
import virtual_robot.hardware.HardwareMap;
import virtual_robot.hardware.Servo;

public class MineralLift {

    private SlewDcMotor liftMotor;

    private Servo gateServo, angleServo;

    private DistanceSensor2m distanceSensor;

    public MineralLift(HardwareMap hardwareMap) {
        /*liftMotor = new SlewDcMotor(hardwareMap.dcMotor.get("mineral_lift"));
        liftMotor.setDirection(DcMotor.Direction.FORWARD);
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setPower(0);

        liftMotor.setSlewSpeed(2);

        gateServo = hardwareMap.servo.get("mineral_gate");
        gateServo.setPosition(Constants.MINERAL_LIFT_GATE_CLOSED_POSITION);

        angleServo = hardwareMap.servo.get("sorter_angle");
        angleServo.setPosition(RobotState.currentMatchState == RobotState.MatchState.AUTONOMOUS ? Constants.MINERAL_LIFT_ANGLE_SERVO_VERTICAL_POSITION : Constants.MINERAL_LIFT_ANGLE_SERVO_HORIZONTAL_POSITION);

        distanceSensor = new DistanceSensor2m("Distance1");*/
    }

    public double getDistance() {
        //return distanceSensor.getDistanceIN();
        return 0;
    }

    public void setTargetPosition(int position) {
        //liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //liftMotor.setPower(Constants.MINERAL_LIFT_FULL_SPEED);
    }

    public void setCurrentPosition(int position) {
        //liftMotor.setCurrentPosition(position);
    }

    public int getCurrentPosition() {
        //return liftMotor.getCurrentPosition();
        return 0;
    }

    public double getPower() {
        //return liftMotor.getPower();
        return 0;
    }


    public void setLiftMotorPower(double power) {
        //liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //liftMotor.setPower(power);
    }

    public void setLiftMotorPowerNoEncoder(double power) {
        //liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //liftMotor.setPower(power);
    }

    public void resetPosition() {
        //liftMotor.setPower(0);
        //liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void setGateServoPosition(double position) {
        //gateServo.setPosition(position);
    }

    public void setAngleServoPosition(double position) {
        //angleServo.setPosition(position);
    }

    public void stop() {
        //liftMotor.stop();
    }
}
