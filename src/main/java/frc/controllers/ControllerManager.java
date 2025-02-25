package frc.controllers;

import edu.wpi.first.wpilibj.XboxController;
import java.util.HashMap;
import java.util.Map;

public class ControllerManager {
    private static ControllerManager instance;
    private final Map<Integer, XboxController> controllers;

    private ControllerManager() {
        controllers = new HashMap<>();
    }

    public static ControllerManager getInstance() {
        if (instance == null) {
            instance = new ControllerManager();
        }
        return instance;
    }

    /**
     * Returns the XboxController for the given USB port. If it does not exist yet, it will be created.
     *
     * @param port the USB port number (0,1,2,…)
     * @return an XboxController instance.
     */
    public XboxController getController(int port) {
        if (!controllers.containsKey(port)) {
            controllers.put(port, new XboxController(port));
        }
        return controllers.get(port);
    }
}
