package frc.util;

import java.util.HashMap;
import java.util.Map;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkBase.PersistMode;

/**
 * A factory class to create or retrieve cached motor controller instances.
 */
public class MotorFactory {
    // Cache to store already-created motors by their CAN ID.
    private static final Map<Integer, IMotorController> motorCache = new HashMap<>();

    /**
     * Creates (or retrieves from cache) an IMotorController based on the provided parameters.
     *
     * @param id           the CAN ID of the motor.
     * @param type         the motor type, e.g. "rev".
     * @param currentLimit optional current limit in amps; if null, no limit is set.
     * @param inverted     whether the motor should be inverted.
     * @return an IMotorController instance for the specified motor.
     */
    public static IMotorController createMotor(int id, String type, Integer currentLimit, boolean inverted) {
        // If we already created this motor, just return it.
        if (motorCache.containsKey(id)) {
            return motorCache.get(id);
        }

        IMotorController motor;
        // Create a REV motor if type is "rev".
        if ("rev".equalsIgnoreCase(type)) {
            RevMotorController revMotor = new RevMotorController(id);

            // Configure the motor using SparkMaxConfig.
            SparkMaxConfig config = new SparkMaxConfig();
            config.inverted(inverted);
            
            // If a current limit is specified, set it in the config.
            if (currentLimit != null) {
                config.smartCurrentLimit(currentLimit);
            }
            
            // Apply the configuration to the motor.
            revMotor.getMotor().configure(config, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
            
            motor = revMotor;
        } else {
            // Add more motor types as needed (TalonFX, TalonSRX, etc.)
            throw new IllegalArgumentException("Unsupported motor type: " + type);
        }

        // Put the newly created motor in the cache so we don't recreate it.
        motorCache.put(id, motor);
        return motor;
    }
}


// package frc.util;

// public class MotorFactory {

//     /**
//      * Creates and returns an IMotorController based on the provided motor type.
//      *
//      * @param id   the CAN ID for the motor controller.
//      * @param type a string representing the motor type (e.g., "rev").
//      * @return an instance of IMotorController.
//      */
//     public static IMotorController createMotor(int id, String type) {
//         if (type.equalsIgnoreCase("rev")) {
//             return new RevMotorController(id);
//         }
//         // Add additional motor types here (e.g., TalonFX, TalonSRX, etc.)
//         throw new IllegalArgumentException("Unsupported motor type: " + type);
//     }
// }



// package frc.util;

// public class MotorFactory {

//     /**
//      * Creates and returns an IMotorController based on the provided motor type.
//      *
//      * @param id   the CAN ID for the motor controller.
//      * @param type a string representing the motor type (e.g., "rev").
//      * @return an instance of IMotorController.
//      */
//     public static IMotorController createMotor(int id, String type) {
//         if (type.equalsIgnoreCase("rev")) {
//             return new RevMotorController(id);
//         }
//         // Add additional motor types here (e.g., TalonFX, TalonSRX, etc.)
//         throw new IllegalArgumentException("Unsupported motor type: " + type);
//     }
// }
