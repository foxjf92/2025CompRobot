package frc.util;

import java.util.HashMap;
import java.util.Map;

public class SharedMotorManager {
    private static final Map<Integer, SharedMotorController> sharedMotors = new HashMap<>();

    /**
     * Retrieve a shared motor controller. If one doesn’t exist for the given ID, it creates one.
     *
     * @param id    The CAN ID.
     * @param motor The underlying IMotorController instance.
     * @return The SharedMotorController for that CAN ID.
     */
    public static SharedMotorController getSharedMotor(int id, IMotorController motor) {
        if (!sharedMotors.containsKey(id)) {
            sharedMotors.put(id, new SharedMotorController(motor));
        }
        return sharedMotors.get(id);
    }
}
