package frc.robot.config;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.Reader;

public class ConfigLoader {

    /**
     * Loads the JSON configuration file and returns a RobotConfig object.
     *
     * @param filename the absolute path to the JSON config file (e.g., "/home/lvuser/robotConfig.json")
     * @return a RobotConfig instance or null if an error occurs.
     */
    public static RobotConfig loadConfig(String filename) {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(filename)) {
            RobotConfig config = gson.fromJson(reader, RobotConfig.class);
            return config;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
