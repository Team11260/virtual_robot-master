package teamcode.framework.userhardware.outputs;

import teamcode.framework.abstractopmodes.AbstractOpMode;
import virtual_robot.hardware.DcMotor;

public class SlewDcMotor implements DcMotor, Runnable {
    //Motor
    private DcMotor motor;

    private double slewSpeed = 0.1;
    private double lastSpeed = 0;
    private double currentSpeed = 0;
    private double SetPower = 0;
    private int encoderOffset = 0;

    //Threading
    private boolean running = true;
    private Thread t;

    //Motor
    public SlewDcMotor(DcMotor motor) {
        this.motor = motor;

        //Threading
        t = new Thread(this, "motor " + motor.toString());
        t.start();
    }

    //Threading
    @Override
    public void run() {
        while (running) {
            motorSlew(getSetPower());
            AbstractOpMode.delay(5);
        }
        shutDown();
    }

    public void stop() {
        running = false;
    }

    private void shutDown() {
        running = false;
        setSlewSpeed(0);
        setPower(0);
        setCurrentPosition(0);
        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    //Motor
    public void setCurrentPosition(int position) {
        motor.setMode(RunMode.STOP_AND_RESET_ENCODER);
        encoderOffset = position;
    }

    public void setSlewSpeed(double slewSpeed) {
        this.slewSpeed = slewSpeed;
    }

    @Override
    public void setDirection(Direction direction) {
        motor.setDirection(direction);
    }

    @Override
    public Direction getDirection() {
        return motor.getDirection();
    }

    @Override
    public void setPower(double SetPower) {
        this.SetPower = SetPower;
    }

    @Override
    public double getPower() {
        return motor.getPower();
    }

    public double getSetPower() {
        return SetPower;
    }

    private void motorSlew(double newSpeed) {
        currentSpeed = lastSpeed;
        lastSpeed = newSpeed;
        //If no speed change stop function
        if (currentSpeed == newSpeed) {
            return;
        }
        if (currentSpeed > newSpeed) {
            //We are slowing down
            if (currentSpeed - newSpeed < slewSpeed) {
                motor.setPower(newSpeed);
                return;
            }
            motor.setPower(currentSpeed - slewSpeed);
            lastSpeed = (currentSpeed - slewSpeed);
            return;
        }
        if (currentSpeed < newSpeed) {
            //We are speeding up
            if (newSpeed - currentSpeed < slewSpeed) {
                motor.setPower(newSpeed);
                return;
            }
            motor.setPower(currentSpeed + slewSpeed);
            lastSpeed = (currentSpeed + slewSpeed);
            return;
        }
    }

    @Override
    public int getCurrentPosition() {
        return motor.getCurrentPosition() + encoderOffset;
    }

    @Override
    public void setMode(RunMode mode) {
        if (mode == RunMode.STOP_AND_RESET_ENCODER) {
            encoderOffset = 0;
        }
        motor.setMode(mode);
    }

    @Override
    public RunMode getMode() {
        return motor.getMode();
    }
}
