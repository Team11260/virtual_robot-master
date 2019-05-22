package teamcode.framework.userhardware.inputs.sensors;

import teamcode.framework.abstractopmodes.AbstractOpMode;
import virtual_robot.hardware.GyroSensor;
import virtual_robot.hardware.HardwareMap;
import virtual_robot.util.time.ElapsedTime;

public class IMU {

    private GyroSensor imu;

    private ElapsedTime GyroTimeOut;

    private boolean newValue = false;
    private double heading = 0;

    private final Object lock = new Object();

    public IMU(HardwareMap hwMap) {

        imu = hwMap.gyroSensor.get("gyro_sensor");
        imu.init();

        AbstractOpMode.getTelemetry().addData("IMU initializing", imu.toString());
    }

    public double getHeading() {
        return imu.getHeading();
    }
}