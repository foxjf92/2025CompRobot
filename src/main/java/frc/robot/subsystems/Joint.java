// Joint.java
package frc.robot.subsystems;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.config.RobotConfig.JointConfig;
import frc.robot.config.RobotConfig.MotorConfig;
import frc.robot.config.RobotConfig.EncoderPositionConfig;
import frc.util.IMotorController;
import frc.util.MotorFactory;
import java.util.ArrayList;
import java.util.List;

public class Joint extends SubsystemBase {
    private final XboxController controller;
    private final List<MotorData> motorDataList = new ArrayList<>();
    private final List<EncoderPositionConfig> encoderConfigs;

    public static class MotorData {
        public final IMotorController motor;
        public final boolean printEncoder;
        public MotorData(IMotorController motor, boolean printEncoder) {
            this.motor = motor;
            this.printEncoder = printEncoder;
        }
    }

    public Joint(JointConfig config) {
        controller = new XboxController(config.controller);
        for (MotorConfig mc : config.motors) {
            IMotorController motor = MotorFactory.createMotor(mc.id, mc.type,mc.currentLimit,mc.inverted);
            boolean shouldPrint = (mc.printEncoder != null) ? mc.printEncoder : false;
            motorDataList.add(new MotorData(motor, shouldPrint));
        }
        encoderConfigs = (config.encoderPositions != null) ? config.encoderPositions : new ArrayList<>();
    }

    public XboxController getController() {
        return controller;
    }

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
        double gain = 0.2;
        double output = gain * error;
        output = Math.max(-0.5, Math.min(0.5, output));
        motorDataList.get(0).motor.set(output);
        // System.out.println("Joint: current=" + currentPosition + ", target=" + target +
        //                    ", error=" + error + ", output=" + output);
    }

    public void printEncoderValues() {
        for (MotorData md : motorDataList) {
            if (md.printEncoder) {
                System.out.println("Joint Motor CAN ID " + md.motor.getDeviceId() +
                                   " Encoder: " + md.motor.getEncoderPosition());
            }
        }
    }
}
