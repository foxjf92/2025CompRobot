// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.config.ConfigLoader;
import frc.robot.config.RobotConfig;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Joint;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Feed;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Hang;

public class Robot extends TimedRobot {

  private static Robot instance;
  private Command m_autonomousCommand;
  
  // Subsystem fields.
  private Elevator elevator;
  private Joint joint;
  private Intake intake;
  private Feed feed;
  private Shooter shooter;
  private Hang hang;

  private RobotContainer m_robotContainer;
  private Timer disabledTimer;

  public Robot() {
    instance = this;
  }

  public static Robot getInstance() {
    return instance;
  }

  @Override
  public void robotInit() {
    m_robotContainer = new RobotContainer();
    disabledTimer = new Timer();
    if (isSimulation()) {
      DriverStation.silenceJoystickConnectionWarning(true);
    }
    // Load configuration from JSON.
    // RobotConfig config = ConfigLoader.loadConfig("/home/lvuser/deploy/robotConfig.json");
    // if (config != null) {
    //   elevator = new Elevator(config.elevator);
    //   joint = new Joint(config.joint);
    //   intake = new Intake(config.intake);
    //   feed = new Feed(config.feed);
    //   shooter = new Shooter(config.shooter);
    //   hang = new Hang(config.hang);
    // } else {
    //   System.out.println("RobotConfig is null!");
    // }
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }

  @Override
  public void disabledInit() {
    m_robotContainer.setMotorBrake(true);
    disabledTimer.reset();
    disabledTimer.start();
  }

  @Override
  public void disabledPeriodic() {
    if (disabledTimer.hasElapsed(Constants.DrivebaseConstants.WHEEL_LOCK_TIME)) {
      m_robotContainer.setMotorBrake(false);
      disabledTimer.stop();
      disabledTimer.reset();
    }
  }

  @Override
  public void autonomousInit() {
    m_robotContainer.setMotorBrake(true);
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {
    // Autonomous code here.
  }

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    } else {
      CommandScheduler.getInstance().cancelAll();
    }
  }

  /**
   * In teleopPeriodic, we delegate control to subsystems that use encoder triggers.
   * For example, we let the Elevator, Joint, and Hang subsystems check their own trigger buttons and
   * optionally print their encoder values (based on each motor's configuration).
   */
  @Override
  public void teleopPeriodic() {
    // if (elevator != null) {
    //   elevator.controlByEncoderTriggers();
    //   elevator.printEncoderValues();
    // }
    // if (joint != null) {
    //   joint.controlByEncoderTriggers();
    //   joint.printEncoderValues();
    // }
    // if (hang != null) {
    //   hang.controlByEncoderTriggers();
    //   hang.printEncoderValues();
    // }
    // Other subsystems (e.g., intake, feed, shooter) may be controlled by commands.
  }

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {
  }

  @Override
  public void simulationInit() {
  }

  @Override
  public void simulationPeriodic() {
  }
}






// // Copyright (c) FIRST and other WPILib contributors.
// // Open Source Software; you can modify and/or share it under the terms of
// // the WPILib BSD license file in the root directory of this project.

// package frc.robot;

// import edu.wpi.first.wpilibj.DriverStation;
// import edu.wpi.first.wpilibj.TimedRobot;
// import edu.wpi.first.wpilibj.Timer;
// import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj2.command.CommandScheduler;
// import frc.robot.config.ConfigLoader;
// import frc.robot.config.RobotConfig;
// import frc.robot.subsystems.Elevator;
// import frc.robot.subsystems.Joint;
// import frc.robot.subsystems.Intake;
// import frc.robot.subsystems.Feed;
// import frc.robot.subsystems.Shooter;
// import frc.robot.subsystems.Hang;

// public class Robot extends TimedRobot {

//   private static Robot instance;
//   private Command m_autonomousCommand;
  
//   // Subsystem fields.
//   private Elevator elevator;
//   private Joint joint;
//   private Intake intake;
//   private Feed feed;
//   private Shooter shooter;
//   private Hang hang;

//   private RobotContainer m_robotContainer;
//   private Timer disabledTimer;

//   public Robot() {
//     instance = this;
//   }

//   public static Robot getInstance() {
//     return instance;
//   }

//   @Override
//   public void robotInit() {
//     m_robotContainer = new RobotContainer();
//     disabledTimer = new Timer();
//     if (isSimulation()) {
//       DriverStation.silenceJoystickConnectionWarning(true);
//     }
//     // Load configuration from the deploy directory.
//     RobotConfig config = ConfigLoader.loadConfig("/home/lvuser/deploy/robotConfig.json");
//     if (config != null) {
//       elevator = new Elevator(config.elevator);
//       joint = new Joint(config.joint);
//       intake = new Intake(config.intake);
//       feed = new Feed(config.feed);
//       shooter = new Shooter(config.shooter);
//       hang = new Hang(config.hang);
//     } else {
//       System.out.println("RobotConfig is null!");
//     }
//   }

//   @Override
//   public void robotPeriodic() {
//     CommandScheduler.getInstance().run();
//   }

//   @Override
//   public void disabledInit() {
//     m_robotContainer.setMotorBrake(true);
//     disabledTimer.reset();
//     disabledTimer.start();
//   }

//   @Override
//   public void disabledPeriodic() {
//     if (disabledTimer.hasElapsed(Constants.DrivebaseConstants.WHEEL_LOCK_TIME)) {
//       m_robotContainer.setMotorBrake(false);
//       disabledTimer.stop();
//       disabledTimer.reset();
//     }
//   }

//   @Override
//   public void autonomousInit() {
//     m_robotContainer.setMotorBrake(true);
//     m_autonomousCommand = m_robotContainer.getAutonomousCommand();
//     if (m_autonomousCommand != null) {
//       m_autonomousCommand.schedule();
//     }
//   }

//   @Override
//   public void autonomousPeriodic() {
//     // Autonomous code here.
//   }

//   @Override
//   public void teleopInit() {
//     if (m_autonomousCommand != null) {
//       m_autonomousCommand.cancel();
//     } else {
//       CommandScheduler.getInstance().cancelAll();
//     }
//   }

//   /**
//    * In teleopPeriodic we delegate control to the elevator subsystem:
//    * - It uses encoder position trigger buttons (e.g., button 3 and 4) to determine a target position.
//    * - A simple proportional controller computes an output based on the difference.
//    * - The elevator then commands its leader motor accordingly.
//    * - If the "printEncoders" flag is true, it prints the encoder values for all motors.
//    */
//   @Override
//   public void teleopPeriodic() {
//     if (elevator != null) {
//       elevator.controlByEncoderTriggers();
//       elevator.printEncoderValues();
//     } else {
//       System.out.println("Elevator subsystem is null!");
//     }
//   }

//   @Override
//   public void testInit() {
//     CommandScheduler.getInstance().cancelAll();
//   }

//   @Override
//   public void testPeriodic() {
//   }

//   @Override
//   public void simulationInit() {
//   }

//   @Override
//   public void simulationPeriodic() {
//   }
// }
