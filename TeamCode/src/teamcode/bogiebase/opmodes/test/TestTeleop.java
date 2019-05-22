package teamcode.bogiebase.opmodes.test;

import teamcode.framework.abstractopmodes.AbstractTeleop;

public class TestTeleop extends AbstractTeleop {
    @Override
    public void RegisterEvents() {
        addEventHandler("a_down", () -> {
            telemetry.addData("", "a down");
            telemetry.update();
            return true;
        });
    }

    @Override
    public void UpdateEvents() {

    }

    @Override
    public void Init() {
        telemetry.update();
    }

    @Override
    public void Loop() {

    }

    @Override
    public void Stop() {

    }
}
