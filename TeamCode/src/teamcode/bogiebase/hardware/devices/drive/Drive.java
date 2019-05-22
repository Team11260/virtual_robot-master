package teamcode.bogiebase.hardware.devices.drive;

import com.acmerobotics.roadrunner.control.PIDCoefficients;
import org.jetbrains.annotations.NotNull;
import teamcode.framework.userhardware.inputs.sensors.IMU;
import virtual_robot.hardware.DcMotor;
import virtual_robot.hardware.HardwareMap;
import virtual_robot.hardware.Servo;

import java.util.Arrays;
import java.util.List;

public class Drive extends TankDriveImpl {

    private DcMotor leftMotor, rightMotor;
    private IMU imu;
    private Servo servoMarker;

    public Drive(HardwareMap hardwareMap) {

        imu = new IMU(hardwareMap);

        //Motors
        leftMotor = hardwareMap.dcMotor.get("left_motor");
        rightMotor = hardwareMap.dcMotor.get("right_motor");

        //Motor Set Up
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        rightMotor.setDirection(DcMotor.Direction.FORWARD);

        //Encoders
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftMotor.setPower(0);
        rightMotor.setPower(0);


        //servoMarker = hardwareMap.servo.get("servo_marker");
        //servoMarker.setPosition(RobotState.currentMatchState == RobotState.MatchState.AUTONOMOUS ? Constants.DRIVE_TEAM_MARKER_RETRACTED : Constants.DRIVE_TEAM_MARKER_TELEOP_RETRACTED);
    }

    public void setPower(double l, double r) {
        leftMotor.setPower(l);
        rightMotor.setPower(r);
    }

    public void resetAngleToZero() {

    }

    public void setTargetPosition(int position) {


    }

    public void setTargetPosition(int leftPosition, int rightPosition) {


    }

    public void setMode(DcMotor.RunMode mode) {
        leftMotor.setMode(mode);
        rightMotor.setMode(mode);
    }

    public void setPosisionP(double p) {
        //leftMotor.setPositionPIDFCoefficients(p);
        //rightMotor.setPositionPIDFCoefficients(p);
    }

    public int getLeftPosition() {
        return leftMotor.getCurrentPosition();
    }

    public int getRightPosition() {
        return rightMotor.getCurrentPosition();
    }

    public double getLeftPower() {
        return leftMotor.getPower();
    }

    public double getRightPower() {
        return rightMotor.getPower();
    }

    public void setMarkerServo(double servoPosition) {
        //servoMarker.setPosition(servoPosition);
    }

    public double getHeading() {
        return imu.getHeading();
    }

    public boolean isGyroCalibrated() {
        if (imu == null) return false;
        return true;
    }

    @Override
    public PIDCoefficients getPIDCoefficients(DcMotor.RunMode runMode) {
        return new PIDCoefficients();
    }

    @Override
    public void setPIDCoefficients(DcMotor.RunMode runMode, PIDCoefficients coefficients) {

    }

    @Override
    public double getExternalHeading() {
        return getHeading();
    }

    @NotNull
    @Override
    public List<Double> getWheelPositions() {
        return Arrays.asList((double) leftMotor.getCurrentPosition(), (double) rightMotor.getCurrentPosition());
    }

    @Override
    public void setMotorPowers(double v, double v1) {
        setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        setPower(v, v1);
    }
}