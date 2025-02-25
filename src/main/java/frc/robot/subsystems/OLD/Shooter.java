package frc.robot.subsystems;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.config.RobotConfig.ShooterConfig;
import frc.robot.config.RobotConfig.MotorConfig;
import frc.util.IMotorController;
import frc.util.MotorFactory;
import java.util.ArrayList;
import java.util.List;

public class Shooter extends SubsystemBase {
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

    public Shooter(ShooterConfig config) {
        // Use the controller port defined in the config
        controller = new XboxController(config.controller);
        for (MotorConfig mc : config.motors) {
            IMotorController motor = MotorFactory.createMotor(mc.id, mc.type,mc.currentLimit,mc.inverted);
            boolean shouldPrint = (mc.printEncoder != null) ? mc.printEncoder : false;
            // Retrieve trigger button and speed from the config.
            Integer triggerButton = mc.triggerButton;
            Double speed = (mc.speed != null) ? mc.speed : 0.0;
            motorDataList.add(new MotorData(motor, shouldPrint, triggerButton, speed));
        }
    }

    public XboxController getController() {
        return controller;
    }

    public void spinTestMotor(double speed) {
        if (!motorDataList.isEmpty()) {
            motorDataList.get(0).motor.set(speed);
        }
    }

    public void printEncoderValues() {
        for (MotorData md : motorDataList) {
            if (md.printEncoder) {
                System.out.println("Shooter Motor CAN ID " + md.motor.getDeviceId() +
                                   " Encoder: " + md.motor.getEncoderPosition());
            }
        }
    }

    /**
     * New method that checks each motor’s trigger button.
     * If the button is pressed, the motor is driven at its configured speed.
     */
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



// // Shooter.java
// package frc.robot.subsystems;

// import edu.wpi.first.wpilibj.XboxController;
// import edu.wpi.first.wpilibj2.command.SubsystemBase;
// import frc.robot.config.RobotConfig.ShooterConfig;
// import frc.robot.config.RobotConfig.MotorConfig;
// import frc.util.IMotorController;
// import frc.util.MotorFactory;
// import java.util.ArrayList;
// import java.util.List;

// public class Shooter extends SubsystemBase {
//     private final XboxController controller;
//     private final List<MotorData> motorDataList = new ArrayList<>();

//     public static class MotorData {
//         public final IMotorController motor;
//         public final boolean printEncoder;
//         public MotorData(IMotorController motor, boolean printEncoder) {
//             this.motor = motor;
//             this.printEncoder = printEncoder;
//             this.triggerButton = triggerButton;
//             this.speed = speed;
//         }
//     }

//     public Shooter(ShooterConfig config) {
//         controller = new XboxController(config.controller);
//         for (MotorConfig mc : config.motors) {
//             IMotorController motor = MotorFactory.createMotor(mc.id, mc.type);
//             motor.setInverted(mc.inverted);
//             boolean shouldPrint = (mc.printEncoder != null) ? mc.printEncoder : false;
//             motorDataList.add(new MotorData(motor, shouldPrint));
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
//                 System.out.println("Shooter Motor CAN ID " + md.motor.getDeviceId() +
//                                    " Encoder: " + md.motor.getEncoderPosition());
//             }
//         }
//     }
// }
