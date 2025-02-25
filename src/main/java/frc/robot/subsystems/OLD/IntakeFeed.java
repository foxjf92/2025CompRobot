package frc.robot.subsystems;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.config.RobotConfig.IntakeFeedConfig;
import frc.robot.config.RobotConfig.IntakeFeedMotorConfig;
import frc.util.IMotorController;
import frc.util.MotorFactory;
import java.util.ArrayList;
import java.util.List;

public class IntakeFeed extends SubsystemBase {
    private final XboxController controller;
    private final DigitalInput sensor; // Optional sensor (for example, to check if intake is full)
    private final List<MotorData> motorDataList = new ArrayList<>();

    public static class MotorData {
        public final IMotorController motor;
        public final Integer intakeTriggerButton;
        public final Integer feedTriggerButton;
        public final double intakeSpeed;
        public final double feedSpeed;
        public final boolean intakeInverted;
        public final boolean feedInverted;
        // For intake toggle logic:
        public boolean isIntakeOn = false;
        public boolean lastIntakeButtonState = false;

        public MotorData(IMotorController motor,
                         Integer intakeTriggerButton,
                         Integer feedTriggerButton,
                         double intakeSpeed,
                         double feedSpeed,
                         boolean intakeInverted,
                         boolean feedInverted) {
            this.motor = motor;
            this.intakeTriggerButton = intakeTriggerButton;
            this.feedTriggerButton = feedTriggerButton;
            this.intakeSpeed = intakeSpeed;
            this.feedSpeed = feedSpeed;
            this.intakeInverted = intakeInverted;
            this.feedInverted = feedInverted;
        }
    }

    /**
     * The IntakeFeedConfig should include:
     * - controller (int)
     * - sensor (SensorConfig) (optional)
     * - a list of IntakeFeedMotorConfig items where each motor config includes:
     *       id, type, currentLimit, inverted,
     *       intakeTriggerButton, feedTriggerButton, intakeSpeed, feedSpeed,
     *       intakeInverted, feedInverted, printEncoder (optional)
     */
    public IntakeFeed(IntakeFeedConfig config) {
        controller = new XboxController(config.controller);
        sensor = (config.sensor != null) ? new DigitalInput(config.sensor.port) : null;
        for (IntakeFeedMotorConfig mc : config.motors) {
            // Create the motor once using MotorFactory.
            IMotorController motor = MotorFactory.createMotor(mc.id, mc.type, mc.currentLimit, mc.inverted);
            // Retrieve the new fields from config.
            Integer intakeBtn = mc.intakeTriggerButton; // may be null
            Integer feedBtn   = mc.feedTriggerButton;     // may be null
            double intakeSpd  = (mc.intakeSpeed != null) ? mc.intakeSpeed : 0.0;
            double feedSpd    = (mc.feedSpeed != null) ? mc.feedSpeed : 0.0;
            boolean intakeInv = (mc.intakeInverted != null) ? mc.intakeInverted : false;
            boolean feedInv   = (mc.feedInverted != null) ? mc.feedInverted : false;
            motorDataList.add(new MotorData(motor, intakeBtn, feedBtn, intakeSpd, feedSpd, intakeInv, feedInv));
        }
    }

    public XboxController getController() {
        return controller;
    }

    /**
     * This method updates the motor outputs based on button inputs.
     * Feed control (momentary) takes priority over intake (toggle).
     */
    public void triggerByButton() {
        for (MotorData md : motorDataList) {
            // If the intake is full, stop the motor immediately.
            if (isIntakeFull()) {
                md.motor.set(0);
                continue;
            }
            
            // Check if a feed trigger is defined and pressed.
            boolean feedPressed = (md.feedTriggerButton != null && controller.getRawButton(md.feedTriggerButton));
            if (feedPressed) {
                double cmd = md.feedSpeed;
                if (md.feedInverted) {
                    cmd = -cmd;
                }
                md.motor.set(cmd);
            } else {
                // Otherwise, use intake toggle logic.
                if (md.intakeTriggerButton != null) {
                    boolean intakePressed = controller.getRawButton(md.intakeTriggerButton);
                    if (intakePressed && !md.lastIntakeButtonState) {
                        md.isIntakeOn = !md.isIntakeOn;
                    }
                    md.lastIntakeButtonState = intakePressed;
                    double cmd = md.isIntakeOn ? md.intakeSpeed : 0;
                    if (md.intakeInverted) {
                        cmd = -cmd;
                    }
                    md.motor.set(cmd);
                } else {
                    md.motor.set(0);
                }
            }
        }
    }
    

    public void printEncoderValues() {
        for (MotorData md : motorDataList) {
            System.out.println("Motor CAN ID " + md.motor.getDeviceId() +
                               " Encoder: " + md.motor.getEncoderPosition());
        }
    }

    /**
     * Optional: Check if the intake is full.
     */
    public boolean isIntakeFull() {
        return (sensor != null) && !sensor.get();
    }
}
