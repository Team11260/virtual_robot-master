package teamcode.framework.userhardware;

import teamcode.framework.abstractopmodes.AbstractOpMode;

public class PIDController {
    private double p, i, d, iVal, lastError = 0, ilimit = 1, minimumOutput = 0;
    private boolean logging = false;

    public PIDController() {
        this(1, 1, 1);
    }

    public PIDController(double P, double I, double D) {
        this(P, I, D, 1);
    }

    public PIDController(double P, double I, double D, double Ilimit) {
        p = P;
        i = I;
        d = D;
        ilimit = Ilimit;
    }

    public PIDController(double P, double I, double D, double Ilimit, double minOutput) {
        p = P;
        i = I;
        d = D;
        ilimit = Ilimit;
        minimumOutput = Math.abs(minOutput);
    }

    public void setMinimumOutput(double minOutput) {
        minimumOutput = Math.abs(minOutput);
    }

    public void setLogging(boolean logging) {
        this.logging = logging;
    }

    public double getError() {
        return lastError;
    }

    public double output(double target, double current) {
        double error = target - current, out;
        out = PTerm(error) + ITerm(error) + DTerm(error);
        lastError = error;
        if (out > 0 && out < minimumOutput) out = minimumOutput;
        if (out < 0 && out > -minimumOutput) out = -minimumOutput;
        return out;
    }

    private double PTerm(double error) {
        if (logging)
            AbstractOpMode.getTelemetry().addData("P", error * (p / 1000));
        return error * (p / 1000);
    }

    private double ITerm(double error) {
        iVal = (iVal + error) * (i / 1000);
        if (iVal < -ilimit) iVal = -ilimit;
        if (iVal > ilimit) iVal = ilimit;
        if (logging) AbstractOpMode.getTelemetry().addData("I", iVal);
        return iVal;
    }

    private double DTerm(double error) {
        if (logging)
            AbstractOpMode.getTelemetry().addData("D", (error - lastError) * (d / 1000));
        return (error - lastError) * (d / 1000);
    }

    public void reset() {
        lastError = 0;
        iVal = 0;
    }
}