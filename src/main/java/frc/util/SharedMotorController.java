package frc.util;

public class SharedMotorController {
    private final IMotorController motor;
    private double intakeSpeed = 0;
    private double feedSpeed = 0;
    private boolean intakeActive = false;
    private boolean feedActive = false;

    public SharedMotorController(IMotorController motor) {
        this.motor = motor;
    }

    /**
     * Called by the Intake subsystem.
     * @param speed the speed command from intake.
     * @param active whether the intake command is active (i.e. button pressed)
     */
    public void setIntakeSpeed(double speed, boolean active) {
        intakeSpeed = speed;
        intakeActive = active;
        updateMotor();
    }

    /**
     * Called by the Feed subsystem.
     * @param speed the speed command from feed.
     * @param active whether the feed command is active (i.e. button pressed)
     */
    public void setFeedSpeed(double speed, boolean active) {
        feedSpeed = speed;
        feedActive = active;
        updateMotor();
    }

    /**
     * Update the motor output.
     * Feed takes priority if active; otherwise, use intake command.
     */
    private void updateMotor() {
        double output = 0;
        if (feedActive) {
            output = feedSpeed;
        } else if (intakeActive) {
            output = intakeSpeed;
        }
        motor.set(output);
    }
}
