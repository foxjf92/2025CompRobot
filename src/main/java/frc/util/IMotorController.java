package frc.util;

public interface IMotorController {
    void set(double speed);
    void setInverted(boolean inverted);
    int getDeviceId();
    // Optional follow functionality.
    void follow(IMotorController leader);

    double getEncoderPosition();
}
