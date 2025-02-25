// Elevator.java
package frc.robot.subsystems;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.config.RobotConfig.ElevatorConfig;
import frc.robot.config.RobotConfig.MotorConfig;
import frc.robot.config.RobotConfig.EncoderPositionConfig;
import frc.util.IMotorController;
import frc.util.MotorFactory;
import java.util.ArrayList;
import java.util.List;

public class Elevator extends SubsystemBase {
    private final XboxController controller;
    // Pair each motor with its printEncoder flag.
    private final List<MotorData> motorDataList = new ArrayList<>();
    private final List<EncoderPositionConfig> encoderConfigs;

    // Helper class that stores a motor and its printEncoder flag.
    public static class MotorData {
        public final IMotorController motor;
        public final boolean printEncoder;
        public final Integer triggerButton;
        public final Double speed;
        
        public MotorData(IMotorController motor, boolean printEncoder, Integer triggerButton, Double speed) {
            this.motor = motor;
            this.printEncoder = printEncoder;
            this.triggerButton = triggerButton;
            this.speed = speed;
        }
        
    }

    public Elevator(ElevatorConfig config) {
        controller = new XboxController(config.controller);
        for (MotorConfig mc : config.motors) {
            IMotorController motor = MotorFactory.createMotor(mc.id, mc.type,mc.currentLimit,mc.inverted);
            // Use printEncoder flag if provided; default to false.
            boolean shouldPrint = (mc.printEncoder != null) ? mc.printEncoder : false;
            // Retrieve trigger button and speed from the config.
            Integer triggerButton = mc.triggerButton;
            Double speed = (mc.speed != null) ? mc.speed : 0.0;
            motorDataList.add(new MotorData(motor, shouldPrint, triggerButton, speed));
        }
        encoderConfigs = (config.encoderPositions != null) ? config.encoderPositions : new ArrayList<>();
    }

    public XboxController getController() {
        return controller;
    }

    // Example method to control the elevator using encoder trigger buttons.
    public void controlByEncoderTriggers() {
        if (motorDataList.isEmpty()) return;
        double currentPosition = motorDataList.get(0).motor.getEncoderPosition();
        double target = currentPosition;
        for (EncoderPositionConfig ec : encoderConfigs) {
            if (ec.triggerButton != null && controller.getRawButton(ec.triggerButton)) {
                target = ec.position;
                break;
            }
        }
        double error = target - currentPosition;
        double gain = 0.2; // Adjust gain as needed.
        double output = gain * error;
        output = Math.max(-0.5, Math.min(0.5, output));
        motorDataList.get(0).motor.set(output);
        // System.out.println("Elevator: current=" + currentPosition + ", target=" + target +
        //                    ", error=" + error + ", output=" + output);
    }

    // For testing: command the leader motor with a given speed.
    public void spinTestMotor(double speed) {
        if (!motorDataList.isEmpty()) {
            motorDataList.get(0).motor.set(speed);
        }
    }

    // Print encoder values for motors with printEncoder set to true.
    public void printEncoderValues() {
        for (MotorData md : motorDataList) {
            if (md.printEncoder) {
                System.out.println("Elevator Motor CAN ID " + md.motor.getDeviceId() +
                                   " Encoder: " + md.motor.getEncoderPosition());
            }
        }
    }

    public void triggerByButton() {
        for (MotorData md : motorDataList) {
            // If triggerButton is configured and pressed, set the motor to its speed.
            if (md.triggerButton != null && controller.getRawButton(md.triggerButton)) {
                md.motor.set(md.speed);
            } else {
                md.motor.set(0);
            }
        }
    }
}
