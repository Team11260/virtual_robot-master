package teamcode.bogiebase.hardware.devices.robot_lift;

import teamcode.bogiebase.hardware.Constants;
import teamcode.framework.userhardware.outputs.SlewDcMotor;
import virtual_robot.hardware.DcMotor;
import virtual_robot.hardware.HardwareMap;
import virtual_robot.hardware.Servo;

public class RobotLift {

    private SlewDcMotor liftMotor;

    private Servo ratchetServo;

    public RobotLift(HardwareMap hardwareMap) {
        /*liftMotor = new SlewDcMotor(hardwareMap.dcMotor.get("robot_lift"));
        liftMotor.setSlewSpeed(2);
        liftMotor.setDirection(DcMotor.Direction.REVERSE);
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setPower(0);

        ratchetServo = hardwareMap.servo.get("lift_pawl");
        ratchetServo.setPosition(Constants.ROBOT_LIFT_PAWL_ENGAGED);*/
    }

    public void setLiftPower(double power) {
        //liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //liftMotor.setPower(power);
    }

    public void setLiftNoEncoderPower(double power) {
        //liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //liftMotor.setPower(power);
    }

    public void setPosition(int position) {
        //liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //liftMotor.setPower(0.5);
    }

    public int getTargetPosition() {
        return 0;
    }

    public int getCurrentPosition() {
        //return liftMotor.getCurrentPosition();
        return 0;
    }

    public void setServoPosition(double servoPosition) {
        //ratchetServo.setPosition(servoPosition);
    }

    public void stop() {
        //liftMotor.stop();
    }
}
