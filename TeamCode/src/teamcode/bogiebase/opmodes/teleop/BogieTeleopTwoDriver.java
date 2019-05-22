package teamcode.bogiebase.opmodes.teleop;

import teamcode.bogiebase.hardware.Robot;
import teamcode.framework.abstractopmodes.AbstractTeleop;

public class BogieTeleopTwoDriver extends AbstractTeleop {

    private Robot robot;

    @Override
    public void RegisterEvents() {
        ////////////////Gamepad 1////////////////
        ////////Drive////////
        addEventHandler("1_b_down", robot.dropMarkerCallable());

        ////////////////Gamepad 2////////////////
        ////////Intake////////
        addEventHandler("2_b_down", robot.beginIntakingCallable());

        addEventHandler("2_b_up", robot.finishIntakingCallable());

        addEventHandler("2_x_down", robot.reverseIntakeCallable());

        addEventHandler("2_x_up", robot.finishIntakingCallable());

        addEventHandler("2_dpu_down", robot.liftIntakeCallable());

        addEventHandler("2_dpd_down", robot.lowerIntakeCallable());

        ///////Mineral Lift////////
        addEventHandler("2_rt_down", robot.moveMineralLiftToCollectPositionCallable());

        addEventHandler("2_rb_down", robot.moveMineralLiftToDumpPositionCallable());

        addEventHandler("2_y_down", robot.toggleMineralGateCallable());

        addEventHandler("2_dpl_down", robot.setAngleServoPositionDumpCallable());

        addEventHandler("2_dpr_down", robot.setAngleServoPositionHorizontalCallable());


        ////////Robot Lift////////
        addEventHandler("2_lb_down", robot.robotLiftUpCallable());

        addEventHandler("2_lb_up", robot.robotLiftStopCallable());

        addEventHandler("2_lt_down", robot.robotLiftUpCallable());

        addEventHandler("2_lt_up", robot.robotLiftStopCallable());
    }

    @Override
    public void UpdateEvents() {
        robot.setDrivePower(-gamepad1.left_stick_y, -gamepad1.right_stick_y);
    }

    @Override
    public void Init() {
        robot = new Robot();
    }

    @Override
    public void Loop() {
        robot.updateAll();
        telemetry.update();
    }

    @Override
    public void Stop() {
        robot.stop();
    }
}