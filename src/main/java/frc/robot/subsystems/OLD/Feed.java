package frc.robot.subsystems;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.config.RobotConfig.FeedConfig;
import frc.robot.config.RobotConfig.MotorConfig;
import frc.util.IMotorController;
import frc.util.MotorFactory;
import java.util.ArrayList;
import java.util.List;

public class Feed extends SubsystemBase {
    private final XboxController controller;
    private final List<MotorData> motorDataList = new ArrayList<>();

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

    public Feed(FeedConfig config) {
        controller = new XboxController(config.controller);
        for (MotorConfig mc : config.motors) {
            IMotorController motor;
            // If the motor is flagged as an intake motor, get the shared instance from Intake.
            if (mc.isIntakeMotor != null && mc.isIntakeMotor) {
                motor = Intake.getSharedIntakeMotor(mc.id);
                if (motor == null) {
                    // In case Intake hasn’t initialized it yet, fall back to creating it.
                    motor = MotorFactory.createMotor(mc.id, mc.type, mc.currentLimit, mc.inverted);
                }
            } else {
                motor = MotorFactory.createMotor(mc.id, mc.type, mc.currentLimit, mc.inverted);
            }
            boolean shouldPrint = (mc.printEncoder != null) ? mc.printEncoder : false;
            Integer triggerButton = mc.triggerButton;
            Double speed = (mc.speed != null) ? mc.speed : 0.0;
            motorDataList.add(new MotorData(motor, shouldPrint, triggerButton, speed));
        }
    }

    public XboxController getController() {
        return controller;
    }

    // Feed trigger logic; this method now commands all motors in Feed's config.
    // For a motor flagged as isIntakeMotor, this is the sole place that sends commands.
    public void triggerByButton() {
        for (MotorData md : motorDataList) {
            boolean buttonPressed = (md.triggerButton != null && controller.getRawButton(md.triggerButton));
            md.motor.set(buttonPressed ? md.speed : 0);
        }
    }

    public void spinTestMotor(double speed) {
        if (!motorDataList.isEmpty()) {
            motorDataList.get(0).motor.set(speed);
        }
    }

    public void printEncoderValues() {
        for (MotorData md : motorDataList) {
            if (md.printEncoder) {
                System.out.println("Feed Motor CAN ID " + md.motor.getDeviceId() +
                                   " Encoder: " + md.motor.getEncoderPosition());
            }
        }
    }
}




// // Feed.java
// package frc.robot.subsystems;

// import edu.wpi.first.wpilibj.XboxController;
// import edu.wpi.first.wpilibj2.command.SubsystemBase;
// import frc.robot.config.RobotConfig.FeedConfig;
// import frc.robot.config.RobotConfig.MotorConfig;
// import frc.util.IMotorController;
// import frc.util.MotorFactory;
// import java.util.ArrayList;
// import java.util.List;

// public class Feed extends SubsystemBase {
//     private final XboxController controller;
//     private final List<MotorData> motorDataList = new ArrayList<>();

//     public static class MotorData {
//         public final IMotorController motor;
//         public final boolean printEncoder;
//         public final Integer triggerButton;
//         public final Double speed;
//         public MotorData(IMotorController motor, boolean printEncoder,Integer triggerButton, Double speed) {
//             this.motor = motor;
//             this.printEncoder = printEncoder;
//             this.triggerButton = triggerButton;
//             this.speed = speed;
//         }
//     }

//     public Feed(FeedConfig config) {
//         controller = new XboxController(config.controller);
//         for (MotorConfig mc : config.motors) {
            
//             IMotorController motor = MotorFactory.createMotor(mc.id, mc.type,mc.currentLimit,mc.inverted);
//             boolean shouldPrint = (mc.printEncoder != null) ? mc.printEncoder : false;
//             Integer triggerButton = mc.triggerButton;
//             Double speed = (mc.speed != null) ? mc.speed : 0.0;
//             motorDataList.add(new MotorData(motor, shouldPrint,triggerButton, speed));
//         }
//     }

//     public XboxController getController() {
//         return controller;
//     }

//     public void spinTestMotor(double speed) {
//         if (!motorDataList.isEmpty()) {
//             motorDataList.get(0).motor.set(speed);
//         }
//     }

//     public void printEncoderValues() {
//         for (MotorData md : motorDataList) {
//             if (md.printEncoder) {
//                 System.out.println("Feed Motor CAN ID " + md.motor.getDeviceId() +
//                                    " Encoder: " + md.motor.getEncoderPosition());
//             }
//         }
//     }

//     public void triggerByButton() {
//         for (MotorData md : motorDataList) {
//             // If triggerButton is configured and pressed, set the motor to its speed.
//             if (md.triggerButton != null && controller.getRawButton(md.triggerButton)) {
//                 md.motor.set(md.speed);
//             } else {
//                 md.motor.set(0);
//             }
//         }
//     }
// }
