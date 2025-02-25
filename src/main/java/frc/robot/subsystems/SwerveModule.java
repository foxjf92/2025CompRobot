package frc.robot.subsystems;
import edu.wpi.first.wpilibj.AnalogInput;

public class SwerveModule {
    private final AnalogInput thriftyEncoder;

    public SwerveModule(int analogPort){
        thriftyEncoder = new AnalogInput(analogPort);
    }
    public double getRawAbsolutePositionRadians(){
        double voltage = thriftyEncoder.getVoltage();
        return voltage*(2*Math.PI / 5);
    }
}
