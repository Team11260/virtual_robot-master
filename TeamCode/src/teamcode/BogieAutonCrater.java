package teamcode;

import teamcode.bogiebase.hardware.Constants;
import teamcode.bogiebase.hardware.Robot;
import teamcode.bogiebase.hardware.RobotState;
import teamcode.framework.abstractopmodes.AbstractAuton;
import teamcode.framework.util.PathState;
import teamcode.framework.util.State;

public class BogieAutonCrater extends AbstractAuton {

    Robot robot;

    @Override
    public void RegisterStates() {
        /*addState(new PathState("finish lowering robot lift", "turn to gold mineral", robot.finishRobotLiftToBottomSequenceCallable()));
        addState(new PathState("intaking pause", "drive to minerals", () -> {
            while (!RobotState.currentPath.getCurrentSegment().getName().equals("back up from minerals"))
                ;
            RobotState.currentPath.pause();
            delay(Constants.NORMAL_INTAKING_DELAY);
            RobotState.currentPath.resume();
            return true;
        }));
        addState(new PathState("begin intaking", "turn to gold mineral", robot.beginIntakingCallable()));
        addState(new PathState("finish intaking", "turn to wall", robot.finishIntakingCallable()));
        addState(new PathState("stop drive to wall", "large drive to wall", robot.autonDriveToWallSequenceCallable()));
        addState(new PathState("drop marker", "drive to depot", robot.dropMarkerCallable()));*/
    }

    @Override
    public void Init() {
        //Init robot
        robot = new Robot();
    }

    @Override
    public void InitLoop(int loop) {

    }

    @Override
    public void Run() {
        //Deposit team marker and drive to crater
        /*robot.runDrivePath(Constants.collectRightMineralDump);
        robot.runDrivePath(Constants.dumpMineral);
        robot.runDrivePath(Constants.craterSideToCrater);*/
        /*robot.runDrivePath(Constants.collectRightMineral);
        robot.runDrivePath(Constants.craterSideToDepotDoubleSample);
        robot.runDrivePath(Constants.collectRightMineralDoubleSample);
        robot.runDrivePath(Constants.depotToCraterDoubleSample);*/
        telemetry.addData("", "Starting spline");
        telemetry.update();
        robot.driveTestSpline();
    }

    @Override
    public void Stop() {
        robot.stop();
    }
}
