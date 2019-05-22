package teamcode.framework.abstractopmodes;

import virtual_robot.controller.LinearOpMode;
import virtual_robot.hardware.HardwareMap;
import virtual_robot.hardware.Telemetry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public abstract class AbstractOpMode extends LinearOpMode {

    private List<Exception> exceptions = Collections.synchronizedList(new ArrayList<>());

    //Setup OpMode instance to allow other classes to access hardwareMap and Telemetry
    private static AbstractOpMode thisOpMode;
    private static HardwareMap hardwareMap;

    public static Telemetry getTelemetry() {
        return thisOpMode.telemetry;
    }

    public static HardwareMap getHardwareMap() {
        return hardwareMap;
    }

    public AbstractOpMode() {
        thisOpMode = this;
    }

    public void runOpMode() {
        hardwareMap = super.hardwareMap;

        runOpmode();
    }

    public abstract void runOpmode();

    public static void delay(int millis) {
        if(Thread.currentThread().isInterrupted()) return;
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static boolean isOpModeActive() {
        return thisOpMode.opModeIsActive();
    }

    public static void staticThrowException(Exception e){
        thisOpMode.throwException(e);
    }

    public void throwException(Exception e) {
        exceptions.add(e);
    }

    public void checkException() {
        for (Exception e : exceptions) {
            telemetry.update();
            for (StackTraceElement element : e.getStackTrace()) {
                if (element.toString().contains("org.firstinspires.ftc.teamcode")) {
                    telemetry.addData("", element.toString().replace("org.firstinspires.ftc.teamcode.", ""));
                }
            }
            switch (e.getClass().getSimpleName()) {
                case "NullPointerException": {
                    telemetry.update();
                    AbstractOpMode.delay(500);
                    NullPointerException exception = (NullPointerException) e;
                    throw exception;
                }
                case "IllegalArgumentException": {
                    telemetry.update();
                    AbstractOpMode.delay(500);
                    IllegalArgumentException exception = (IllegalArgumentException) e;
                    throw exception;
                }
                case "ArrayIndexOutOfBoundsException": {
                    telemetry.update();
                    AbstractOpMode.delay(500);
                    ArrayIndexOutOfBoundsException exception = (ArrayIndexOutOfBoundsException) e;
                    throw exception;
                }
                case "ConcurrentModificationException": {
                    telemetry.update();
                    AbstractOpMode.delay(500);
                    ConcurrentModificationException exception = (ConcurrentModificationException) e;
                    throw exception;
                }
                case "IllegalStateException": {
                    telemetry.update();
                    AbstractOpMode.delay(500);
                    IllegalStateException exception = (IllegalStateException) e;
                    throw exception;
                }
                case "ExecutionException": {
                    telemetry.update();
                    AbstractOpMode.delay(500);
                    NullPointerException exception = new NullPointerException();
                    exception.setStackTrace(e.getCause().getStackTrace());
                    throw exception;
                }
                default: {
                    telemetry.update();
                    AbstractOpMode.delay(500);
                    NullPointerException exception = new NullPointerException(e.getMessage());
                    exception.setStackTrace(e.getStackTrace());
                    throw exception;
                }
            }
        }
    }
}
