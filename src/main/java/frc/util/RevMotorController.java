package frc.util;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkBase.PersistMode;

public class RevMotorController implements IMotorController {
    private final SparkMax motor;
    
    public RevMotorController(int id) {
        // Create a SparkMax on the specified CAN ID with a brushless motor type.
        motor = new SparkMax(id, MotorType.kBrushless);
    }
    
    @Override
    public void set(double speed) {
        motor.set(speed);
    }
    
    /**
     * Instead of using the deprecated setInverted(boolean) method,
     * we configure inversion via SparkMaxConfig.
     * This call creates a new configuration, sets the inversion flag via
     * the inverted(boolean) method, and then applies it to the motor.
     */
    @Override
    public void setInverted(boolean inverted) {
        SparkMaxConfig config = new SparkMaxConfig();
        config.inverted(inverted); // Call the method rather than assigning a field.
        motor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
    }
    
    @Override
    public int getDeviceId() {
        return motor.getDeviceId();
    }
    
    @Override
    public double getEncoderPosition() {
        return motor.getEncoder().getPosition();
    }

    
    /**
     * The new SparkMax API does not support a direct follow() method.
     * If follower behavior is needed, it must be configured using vendor-specific methods.
     */
    @Override
    public void follow(IMotorController leader) {
        throw new UnsupportedOperationException(
            "Follow is not supported in the new SparkMax API. " +
            "Please configure follower mode via SparkMaxConfig or other vendor methods."
        );
    }
    
    // Optionally, provide access to the underlying SparkMax instance.
    public SparkMax getMotor() {
        return motor;
    }
}


// package frc.util;

// import com.revrobotics.spark.SparkMax;
// import com.revrobotics.spark.SparkLowLevel.MotorType;
// import com.revrobotics.spark.config.SparkMaxConfig;
// import com.revrobotics.spark.SparkBase.ResetMode;
// import com.revrobotics.spark.SparkBase.PersistMode;

// public class RevMotorController implements IMotorController {
//     private final SparkMax motor;
    
//     public RevMotorController(int id) {
//         // Create a SparkMax on the specified CAN ID with a brushless motor type.
//         motor = new SparkMax(id, MotorType.kBrushless);
//     }
    
//     @Override
//     public void set(double speed) {
//         motor.set(speed);
//     }
    
//     /**
//      * Instead of using the deprecated setInverted(boolean) method,
//      * we configure inversion via SparkMaxConfig.
//      * Note: This call creates a new configuration, sets the inversion flag,
//      * and then applies it to the motor. In a complete implementation you might
//      * merge this with other configuration settings.
//      */
//     @Override
//     public void setInverted(boolean inverted) {
//         SparkMaxConfig config = new SparkMaxConfig();
//         // Set the inversion property on the configuration.
//         config.inverted = inverted;
//         motor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);
//     }
    
//     @Override
//     public int getDeviceId() {
//         return motor.getDeviceId();
//     }
    
//     /**
//      * The new SparkMax API does not support a direct follow() method.
//      * If follower behavior is needed, it must be configured using vendor-specific methods.
//      */
//     @Override
//     public void follow(IMotorController leader) {
//         throw new UnsupportedOperationException(
//             "Follow is not supported in the new SparkMax API. " +
//             "Please configure follower mode via SparkMaxConfig or other vendor methods."
//         );
//     }
    
//     // Optionally, provide access to the underlying SparkMax instance.
//     public SparkMax getMotor() {
//         return motor;
//     }
// }
