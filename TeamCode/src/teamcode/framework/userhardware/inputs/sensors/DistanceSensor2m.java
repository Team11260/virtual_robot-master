package teamcode.framework.userhardware.inputs.sensors;


import teamcode.framework.abstractopmodes.AbstractOpMode;
import virtual_robot.hardware.DistanceSensor;
import virtual_robot.util.navigation.DistanceUnit;

public class DistanceSensor2m {

    //private com.qualcomm.hardware.rev.Rev2mDistanceSensor timeOfFlightSensor;
    private DistanceSensor distanceSensor;

    public DistanceSensor2m(String name) {
        distanceSensor = AbstractOpMode.getHardwareMap().get(DistanceSensor.class, name);
        //timeOfFlightSensor = (com.qualcomm.hardware.rev.Rev2mDistanceSensor) distanceSensor;
    }

    public double getDistanceIN() {
        return (distanceSensor.getDistance(DistanceUnit.INCH));
    }
}
