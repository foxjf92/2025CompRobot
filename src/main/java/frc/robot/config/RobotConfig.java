package frc.robot.config;

import java.util.List;

public class RobotConfig {

    public ElevatorConfig elevator;
    public JointConfig joint;
    public IntakeConfig intake;
    public FeedConfig feed;
    public ShooterConfig shooter;
    public HangConfig hang;
    
    // New combined config for IntakeFeed subsystem:
    public IntakeFeedConfig intakeFeed;

    // Used by all subsystems for motor configuration
    public static class MotorConfig {
        public int id;
        public String type;           // For example: "rev"
        public Integer triggerButton;
        public boolean inverted;
        public Boolean followLeader;
        public Integer leaderId;
        public Boolean isIntakeMotor;
        public Boolean printEncoder;
        public Double speed;
        public Integer currentLimit;      
    }

    // New MotorConfig for the combined IntakeFeed subsystem.
    public static class IntakeFeedMotorConfig {
        public int id;
        public String type;  // e.g., "rev"
        public boolean inverted; // base inversion (if needed; you can ignore if using separate ones)
        public Integer intakeTriggerButton;  // trigger for intake (toggle)
        public Integer feedTriggerButton;    // trigger for feed (momentary)
        public Double intakeSpeed;           // speed for intake function
        public Double feedSpeed;             // speed for feed function
        public Boolean intakeInverted;       // if true, reverse intake commands
        public Boolean feedInverted;         // if true, reverse feed commands
        public Integer currentLimit;      
        public Boolean printEncoder;
    }

    // For subsystems with preset encoder positions (elevator, joint, hang)
    public static class EncoderPositionConfig {
        public double position;
        public Integer triggerButton;
    }

    // For sensor configurations (like in intake)
    public static class SensorConfig {
        public int port;
    }

    // Each subsystem’s configuration
    public static class ElevatorConfig {
        public int controller; 
        public List<MotorConfig> motors;
        public List<EncoderPositionConfig> encoderPositions;
    }

    public static class JointConfig {
        public int controller;
        public List<MotorConfig> motors;
        public List<EncoderPositionConfig> encoderPositions;
    }

    public static class IntakeConfig {
        public int controller;
        public List<MotorConfig> motors;
        public SensorConfig sensor;
    }

    public static class FeedConfig {
        public int controller;
        public List<MotorConfig> motors;
    }

    public static class ShooterConfig {
        public int controller;
        public List<MotorConfig> motors;
    }

    public static class HangConfig {
        public int controller;
        public List<MotorConfig> motors;
        public List<EncoderPositionConfig> encoderPositions;
    }

    // New combined IntakeFeed config.
    public static class IntakeFeedConfig {
        public int controller;
        public List<IntakeFeedMotorConfig> motors;
        public SensorConfig sensor;
    }
}


// package frc.robot.config;

// import java.util.List;

// public class RobotConfig {

//     public ElevatorConfig elevator;
//     public JointConfig joint;
//     public IntakeConfig intake;
//     public FeedConfig feed;
//     public ShooterConfig shooter;
//     public HangConfig hang;

//     // Used by all subsystems for motor configuration
//     public static class MotorConfig {
//         public int id;
//         public String type;           // For example: "rev"
//         public Integer triggerButton;
//         public boolean inverted;
//         // For subsystems that allow following (like elevator and shooter)
//         public Boolean followLeader;
//         public Integer leaderId;
//         // For feed: mark if this motor is one of the intake motors used for feeding
//         public Boolean isIntakeMotor;
//         // New field: whether to print the encoder value for this motor.
//         public Boolean printEncoder;
//         public Double speed;
//         public Integer currentLimit;      

//     }

//     // For subsystems with preset encoder positions (elevator, joint, hang)
//     public static class EncoderPositionConfig {
//         public double position;
//         public Integer triggerButton;
//     }

//     // For sensor configurations (like in intake)
//     public static class SensorConfig {
//         public int port;
//     }

//     // Each subsystem’s configuration
//     public static class ElevatorConfig {
//         public int controller; // USB port number (0,1,2,…)
//         public List<MotorConfig> motors;
//         public List<EncoderPositionConfig> encoderPositions;
//     }

//     public static class JointConfig {
//         public int controller;
//         public List<MotorConfig> motors;
//         public List<EncoderPositionConfig> encoderPositions;
//     }

//     public static class IntakeConfig {
//         public int controller;
//         public List<MotorConfig> motors;
//         public SensorConfig sensor;
//     }

//     public static class FeedConfig {
//         public int controller;
//         public List<MotorConfig> motors;
//     }

//     public static class ShooterConfig {
//         public int controller;
//         public List<MotorConfig> motors;
//     }

//     public static class HangConfig {
//         public int controller;
//         public List<MotorConfig> motors;
//         public List<EncoderPositionConfig> encoderPositions;
//     }
    
// }


// package frc.robot.config;

// import java.util.List;

// public class RobotConfig {

//     public ElevatorConfig elevator;
//     public JointConfig joint;
//     public IntakeConfig intake;
//     public FeedConfig feed;
//     public ShooterConfig shooter;
//     public HangConfig hang;

//     // Used by all subsystems for motor configuration
//     public static class MotorConfig {
//         public int id;
//         public String type;           // For example: "rev"
//         public Integer triggerButton;
//         public boolean inverted;
//         // For subsystems that allow following (like elevator and shooter)
//         public Boolean followLeader;
//         public Integer leaderId;
//         // For feed: mark if this motor is one of the intake motors used for feeding
//         public Boolean isIntakeMotor;
//         // New field: whether to print the encoder value for this motor.
//         public Boolean printEncoder;
//         public Double speed;
//         public Integer currentLimit;      

//     }

//     // For subsystems with preset encoder positions (elevator, joint, hang)
//     public static class EncoderPositionConfig {
//         public double position;
//         public Integer triggerButton;
//     }

//     // For sensor configurations (like in intake)
//     public static class SensorConfig {
//         public int port;
//     }

//     // Each subsystem’s configuration
//     public static class ElevatorConfig {
//         public int controller; // USB port number (0,1,2,…)
//         public List<MotorConfig> motors;
//         public List<EncoderPositionConfig> encoderPositions;
//     }

//     public static class JointConfig {
//         public int controller;
//         public List<MotorConfig> motors;
//         public List<EncoderPositionConfig> encoderPositions;
//     }

//     public static class IntakeConfig {
//         public int controller;
//         public List<MotorConfig> motors;
//         public SensorConfig sensor;
//     }

//     public static class FeedConfig {
//         public int controller;
//         public List<MotorConfig> motors;
//     }

//     public static class ShooterConfig {
//         public int controller;
//         public List<MotorConfig> motors;
//     }

//     public static class HangConfig {
//         public int controller;
//         public List<MotorConfig> motors;
//         public List<EncoderPositionConfig> encoderPositions;
//     }
    
// }
