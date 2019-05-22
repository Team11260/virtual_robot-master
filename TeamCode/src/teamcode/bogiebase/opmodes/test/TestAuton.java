package teamcode.bogiebase.opmodes.test;

import teamcode.bogiebase.hardware.Robot;
import teamcode.framework.abstractopmodes.AbstractAuton;

public class TestAuton extends AbstractAuton {

    Robot robot;

    @Override
    public void RegisterStates() {

    }

    @Override
    public void Init() {
        robot = new Robot();
    }

    @Override
    public void Run() {
        robot.driveTestSpline();
    }

    @Override
    public void Stop() {
        robot.stop();
    }
}
