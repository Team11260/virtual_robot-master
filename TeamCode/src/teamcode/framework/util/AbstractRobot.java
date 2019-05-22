package teamcode.framework.util;

import teamcode.framework.abstractopmodes.AbstractOpMode;
import virtual_robot.hardware.Telemetry;

public abstract class AbstractRobot {

    public Telemetry telemetry;

    public AbstractRobot() {
        telemetry = AbstractOpMode.getTelemetry();
    }

    public abstract void stop();

    public void delay(int time) {
        AbstractOpMode.delay(time);
    }
}
