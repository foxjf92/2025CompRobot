package frc.robot.subsystems;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.config.RobotConfig.IntakeConfig;
import frc.robot.config.RobotConfig.MotorConfig;
import frc.util.IMotorController;
import frc.util.MotorFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Intake extends SubsystemBase {
    private final XboxController controller;
    private final List<MotorData> motorDataList = new ArrayList<>();
    private final DigitalInput sensor;
    
    // Static map to store shared intake motors (key: CAN ID)
    private static final Map<Integer, IMotorController> sharedIntakeMotors = new HashMap<>();

    // Getter for Feed (or any other subsystem) to obtain a shared intake motor.
    public static IMotorController getSharedIntakeMotor(int canId) {
        return sharedIntakeMotors.get(canId);
    }

    // Setter used during initialization.
    private static void setSharedIntakeMotor(int canId, IMotorController motor) {
        sharedIntakeMotors.put(canId, motor);
    }

    public static class MotorData {
        public final IMotorController motor;
        public final boolean printEncoder;
        public final Integer triggerButton;
        public final Double speed;
        public boolean isOn = false;
        public boolean lastButtonState = false;

        public MotorData(IMotorController motor, boolean printEncoder, Integer triggerButton, Double speed) {
            this.motor = motor;
            this.printEncoder = printEncoder;
            this.triggerButton = triggerButton;
            this.speed = speed;
        }
    }

    public Intake(IntakeConfig config) {
        controller = new XboxController(config.controller);
        for (MotorConfig mc : config.motors) {
            // If this motor is flagged as an intake motor, let Intake create it and store it.
            if (mc.isIntakeMotor != null && mc.isIntakeMotor) {
                IMotorController motor = MotorFactory.createMotor(mc.id, mc.type, mc.currentLimit, mc.inverted);
                // Save it in our shared map.
                setSharedIntakeMotor(mc.id, motor);
                // Optionally, you can add it to motorDataList if Intake needs to use it too.
                // For example, if Intake should still toggle it:
                MotorData md = new MotorData(motor,
                        (mc.printEncoder != null) ? mc.printEncoder : false,
                        mc.triggerButton,
                        (mc.speed != null) ? mc.speed : 0.0);
                motorDataList.add(md);
            } else {
                // Normal (non-shared) intake motor.
                IMotorController motor = MotorFactory.createMotor(mc.id, mc.type, mc.currentLimit, mc.inverted);
                MotorData md = new MotorData(motor,
                        (mc.printEncoder != null) ? mc.printEncoder : false,
                        mc.triggerButton,
                        (mc.speed != null) ? mc.speed : 0.0);
                motorDataList.add(md);
            }
        }
        sensor = (config.sensor != null) ? new DigitalInput(config.sensor.port) : null;
    }
    
    public XboxController getController() {
        return controller;
    }

    // Intake trigger logic for non-shared motors (or even for shared ones, if Intake wants to control them).
    public void triggerByButton() {
        for (MotorData md : motorDataList) {
            // (Assuming Intake has its own control logic.)
            if (md.triggerButton == null || isIntakeFull()) {
                md.motor.set(0);
                continue;
            }
            boolean currentButtonState = controller.getRawButton(md.triggerButton);
            if (currentButtonState && !md.lastButtonState) {
                md.isOn = !md.isOn;
            }
            md.lastButtonState = currentButtonState;
            md.motor.set(md.isOn ? md.speed : 0);
        }
    }
    
    public boolean isIntakeFull() {
        return (sensor != null) && !sensor.get();
    }
}


// // Intake.java
// package frc.robot.subsystems;

// import edu.wpi.first.wpilibj.XboxController;
// import edu.wpi.first.wpilibj.DigitalInput;
// import edu.wpi.first.wpilibj2.command.SubsystemBase;
// import frc.robot.config.RobotConfig.IntakeConfig;
// import frc.robot.config.RobotConfig.MotorConfig;
// import frc.util.IMotorController;
// import frc.util.MotorFactory;
// import frc.util.RevMotorController;

// import java.util.ArrayList;
// import java.util.List;

// public class Intake extends SubsystemBase {
//     private final XboxController controller;
//     private final List<MotorData> motorDataList = new ArrayList<>();
//     private final DigitalInput sensor;
    

//     public static class MotorData {
//         public final IMotorController motor;
//         public final boolean printEncoder;
//         public final Integer triggerButton;
//         public final Double speed;
//         public boolean isOn = false;
//         public boolean lastButtonState = false;

//         public MotorData(IMotorController motor, boolean printEncoder, Integer triggerButton, Double speed) {
//             this.motor = motor;
//             this.printEncoder = printEncoder;
//             this.triggerButton = triggerButton;
//             this.speed = speed;
//         }
//     }

//     public Intake(IntakeConfig config) {
//         controller = new XboxController(config.controller);
//         for (MotorConfig mc : config.motors) {
//             IMotorController motor = MotorFactory.createMotor(mc.id, mc.type,mc.currentLimit,mc.inverted);
//             motor.setInverted(mc.inverted);  
            
//             Integer triggerButton = mc.triggerButton;
//             Double speed = (mc.speed != null) ? mc.speed : 0.0;
//             boolean shouldPrint = (mc.printEncoder != null) ? mc.printEncoder : false;
//             motorDataList.add(new MotorData(motor, shouldPrint, triggerButton, speed));
//         }
//         // Create sensor if configured.
//         sensor = (config.sensor != null) ? new DigitalInput(config.sensor.port) : null;
//     }

//     public XboxController getController() {
//         return controller;
//     }

   
//     public void triggerByButton() {
//         for (MotorData md : motorDataList) {
//             // If there's no trigger button, or the intake is full, ensure the motor is off.
//             if (md.triggerButton == null || isIntakeFull()) {
//                 md.motor.set(0);
//                 continue;
//             }
            
//             // Read the current state of the button.
//             boolean currentButtonState = controller.getRawButton(md.triggerButton);
            
//             // Toggle on rising edge (button pressed now, but wasn't pressed previously).
//             if (currentButtonState && !md.lastButtonState) {
//                 md.isOn = !md.isOn;
//             }
            
//             // Update last button state for the next iteration.
//             md.lastButtonState = currentButtonState;
            
//             // Set motor speed based on the toggle state.
//             md.motor.set(md.isOn ? md.speed : 0);
//         }
//     }
    
//     public void spinTestMotor(double speed) {
//         if (!motorDataList.isEmpty()) {
//             motorDataList.get(0).motor.set(speed);
//         }
//     }

//     public void printEncoderValues() {
//         for (MotorData md : motorDataList) {
//             if (md.printEncoder) {
//                 System.out.println("Intake Motor CAN ID " + md.motor.getDeviceId() +
//                                    " Encoder: " + md.motor.getEncoderPosition());
//             }
//         }
//     }

//     public boolean isIntakeFull() {
//         System.out.println(sensor.get());
//         return (sensor != null) && !sensor.get();
//     }
// }
