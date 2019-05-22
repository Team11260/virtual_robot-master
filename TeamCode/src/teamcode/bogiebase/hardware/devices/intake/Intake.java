package teamcode.bogiebase.hardware.devices.intake;

import teamcode.bogiebase.hardware.Constants;
import virtual_robot.hardware.DcMotor;
import virtual_robot.hardware.HardwareMap;
import virtual_robot.hardware.Servo;

public class Intake {

    private DcMotor intakeMotor;
    private Servo liftServo;

    public Intake(HardwareMap hardwareMap) {
        /*intakeMotor = hardwareMap.dcMotor.get("intake");
        intakeMotor.setDirection(DcMotor.Direction.REVERSE);
        intakeMotor.setPower(0);

        liftServo = hardwareMap.servo.get("intake_lift");
        liftServo.setPosition(Constants.INTAKE_LIFT_LOWERED_POSITION);*/
    }

    public void setLiftServoPosition(double position) {
        //liftServo.setPosition(position);
    }

    public void setIntakePower(double power) {
        //intakeMotor.setPower(power);
    }

    public void stop() {

    }
}