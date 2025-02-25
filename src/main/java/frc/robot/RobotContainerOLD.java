// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.OperatorConstants;
import frc.robot.config.ConfigLoader;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Feed;
import frc.robot.subsystems.Hang;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Joint;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.IntakeFeed;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;
import java.io.File;
import swervelib.SwerveInputStream;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LauncherSubsystem;
import frc.robot.subsystems.SwerveSubsystem;

import frc.robot.config.RobotConfig;


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a "declarative" paradigm, very
 * little robot logic should actually be handled in the {@link Robot} periodic methods (other than the scheduler calls).
 * Instead, the structure of the robot (including subsystems, commands, and trigger mappings) should be declared here.
 */



 
public class RobotContainer
{

  private final Elevator elevator;
  private final Joint joint;
  // private final Intake intake;
  // private final Feed feed;
  private final Shooter shooter;
  private final Hang hang;
  private final IntakeFeed intakeFeed;
  private final IntakeSubsystem intake = new IntakeSubsystem();
  private final ArmSubsystem arm = new ArmSubsystem();
  private final LauncherSubsystem launcher = new LauncherSubsystem();
  private final ClimberSubsystem climber = new ClimberSubsystem();
  
  CommandXboxController driverXbox = new CommandXboxController(0);
  CommandXboxController operatorXbox = new CommandXboxController(1);

  CommandXboxController operatorXbox = new CommandXboxController(1);
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  // Replace with CommandPS4Controller or CommandJoystick if needed
  final         CommandXboxController driverXbox = new CommandXboxController(0);
  // The robot's subsystems and commands are defined here...
  private final SwerveSubsystem       drivebase  = new SwerveSubsystem(new File(Filesystem.getDeployDirectory(),
                                                                                "swerve/neo"));

  /**
   * Converts driver input into a field-relative ChassisSpeeds that is controlled by angular velocity.
   */
  SwerveInputStream driveAngularVelocity = SwerveInputStream.of(drivebase.getSwerveDrive(),
                                                                () -> driverXbox.getLeftY() * -1,
                                                                () -> driverXbox.getLeftX() * -1)
                                                            .withControllerRotationAxis(driverXbox::getRightX)
                                                            .deadband(OperatorConstants.DEADBAND)
                                                            .scaleTranslation(0.8)
                                                            .allianceRelativeControl(true);

  /**
   * Clone's the angular velocity input stream and converts it to a fieldRelative input stream.
   */
  SwerveInputStream driveDirectAngle = driveAngularVelocity.copy().withControllerHeadingAxis(driverXbox::getRightX,
                                                                                             driverXbox::getRightY)
                                                           .headingWhile(true);

  /**
   * Clone's the angular velocity input stream and converts it to a robotRelative input stream.
   */
  SwerveInputStream driveRobotOriented = driveAngularVelocity.copy().robotRelative(true)
                                                             .allianceRelativeControl(false);

  SwerveInputStream driveAngularVelocityKeyboard = SwerveInputStream.of(drivebase.getSwerveDrive(),
                                                                        () -> -driverXbox.getLeftY(),
                                                                        () -> -driverXbox.getLeftX())
                                                                    .withControllerRotationAxis(() -> driverXbox.getRawAxis(
                                                                        2))
                                                                    .deadband(OperatorConstants.DEADBAND)
                                                                    .scaleTranslation(0.8)
                                                                    .allianceRelativeControl(true);
  // Derive the heading axis with math!
  SwerveInputStream driveDirectAngleKeyboard     = driveAngularVelocityKeyboard.copy()
                                                                               .withControllerHeadingAxis(() ->
                                                                                                              Math.sin(
                                                                                                                  driverXbox.getRawAxis(
                                                                                                                      2) *
                                                                                                                  Math.PI) *
                                                                                                              (Math.PI *
                                                                                                               2),
                                                                                                          () ->
                                                                                                              Math.cos(
                                                                                                                  driverXbox.getRawAxis(
                                                                                                                      2) *
                                                                                                                  Math.PI) *
                                                                                                              (Math.PI *
                                                                                                               2))
                                                                               .headingWhile(true);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer()
  {

    // RobotConfig config = ConfigLoader.loadConfig("/home/lvuser/deploy/robotConfig.json");
    // if (config == null) {
    //   DriverStation.reportError("RobotConfig is null! Using drivebase only.", false);
    //   elevator = null;
    //   joint = null;
    //   // intake = null;
    //   // feed = null;
    //   shooter = null;
    //   hang = null;
    //   intakeFeed = null;

    // } else {
    //   elevator = new Elevator(config.elevator);
    //   joint = new Joint(config.joint);
    //   // intake = new Intake(config.intake);
    //   // feed = new Feed(config.feed);
    //   shooter = new Shooter(config.shooter);
    //   hang = new Hang(config.hang);
    //   intakeFeed = new IntakeFeed(config.intakeFeed);
    // }




    // Configure the trigger bindings
    configureBindings();
    DriverStation.silenceJoystickConnectionWarning(true);
    NamedCommands.registerCommand("test", Commands.print("I EXIST"));
    AbsoluteDriveAdv closedAbsoluteDriveAdv = new AbsoluteDriveAdv(drivebase,
                                                                   () -> -MathUtil.applyDeadband(driverXbox.getLeftY(),
                                                                                                OperatorConstants.LEFT_Y_DEADBAND),
                                                                   () -> -MathUtil.applyDeadband(driverXbox.getLeftX(),
                                                                                                OperatorConstants.LEFT_X_DEADBAND),
                                                                   () -> MathUtil.applyDeadband(driverXbox.getRightX(),
                                                                                                OperatorConstants.RIGHT_X_DEADBAND),
                                                                   driverXbox.getHID()::getYButtonPressed,
                                                                   driverXbox.getHID()::getAButtonPressed,
                                                                   driverXbox.getHID()::getXButtonPressed,
                                                                   driverXbox.getHID()::getBButtonPressed);

    drivebase.setDefaultCommand(closedAbsoluteDriveAdv);
    arm.setDefaultCommand(armLaunch);
    intake.setDefaultCommand(intakeStill);
    launcher.setDefaultCommand(launchStill);
    climber.setDefaultCommand(climbStill);


  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary predicate, or via the
   * named factories in {@link edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for
   * {@link CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller PS4}
   * controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight joysticks}.
   */
  private void configureBindings()
  {

    Command driveFieldOrientedDirectAngle      = drivebase.driveFieldOriented(driveDirectAngle);
    Command driveFieldOrientedAnglularVelocity = drivebase.driveFieldOriented(driveAngularVelocity);
    Command driveRobotOrientedAngularVelocity  = drivebase.driveFieldOriented(driveRobotOriented);
    Command driveSetpointGen = drivebase.driveWithSetpointGeneratorFieldRelative(
        driveDirectAngle);
    Command driveFieldOrientedDirectAngleKeyboard      = drivebase.driveFieldOriented(driveDirectAngleKeyboard);
    Command driveFieldOrientedAnglularVelocityKeyboard = drivebase.driveFieldOriented(driveAngularVelocityKeyboard);
    Command driveSetpointGenKeyboard = drivebase.driveWithSetpointGeneratorFieldRelative(
        driveDirectAngleKeyboard);



    // if (elevator != null) {
    //   elevator.setDefaultCommand(new RunCommand(() -> {
    //     elevator.controlByEncoderTriggers();
    //     elevator.printEncoderValues();
        
    
    //   }, elevator));
    // }
    // if (joint != null) {
    //   joint.setDefaultCommand(new RunCommand(() -> {
    //     joint.controlByEncoderTriggers();
    //     joint.printEncoderValues();
        
    //   }, joint));
    // }
    // if (hang != null) {
    //   hang.setDefaultCommand(new RunCommand(() -> {
    //     hang.controlByEncoderTriggers();
    //     hang.printEncoderValues();
    //   }, hang));
    // }
    // if (shooter != null) {
    //   shooter.setDefaultCommand(new RunCommand(() -> {
    //     shooter.printEncoderValues();
    //     shooter.triggerByButton();
    //   }, shooter));
    // }
    // if (intakeFeed != null) {
    //   intakeFeed.setDefaultCommand(new RunCommand(() -> {
    //     intakeFeed.printEncoderValues();
    //     intakeFeed.triggerByButton();
    //   }, intakeFeed));
    // }
    // if (intake != null) {
    //   intake.setDefaultCommand(new RunCommand(() -> {
    //   intake.triggerByButton();

    //   }, intake));
    // }
    // if (feed != null) {
    //   feed.setDefaultCommand(new RunCommand(() -> {
    //   feed.triggerByButton();
    //   }, feed));
    // }




    


    if (RobotBase.isSimulation())
    {
      drivebase.setDefaultCommand(driveFieldOrientedDirectAngleKeyboard);
    } else
    {
      drivebase.setDefaultCommand(driveFieldOrientedAnglularVelocity);
    }

    if (Robot.isSimulation())
    {
      driverXbox.start().onTrue(Commands.runOnce(() -> drivebase.resetOdometry(new Pose2d(3, 3, new Rotation2d()))));
      driverXbox.button(1).whileTrue(drivebase.sysIdDriveMotorCommand());

    }
    if (DriverStation.isTest())
    {
      drivebase.setDefaultCommand(driveFieldOrientedAnglularVelocity); // Overrides drive command above!

      driverXbox.x().whileTrue(Commands.runOnce(drivebase::lock, drivebase).repeatedly());
      driverXbox.y().whileTrue(drivebase.driveToDistanceCommand(1.0, 0.2));
      driverXbox.start().onTrue((Commands.runOnce(drivebase::zeroGyro)));
      driverXbox.back().whileTrue(drivebase.centerModulesCommand());
      driverXbox.leftBumper().onTrue(Commands.none());
      driverXbox.rightBumper().onTrue(Commands.none());
    } else
    {
      driverXbox.a().onTrue((Commands.runOnce(drivebase::zeroGyro)));
      driverXbox.x().onTrue(Commands.runOnce(drivebase::addFakeVisionReading));
      driverXbox.b().whileTrue(
          drivebase.driveToPose(
              new Pose2d(new Translation2d(4, 4), Rotation2d.fromDegrees(0)))
                              );
      driverXbox.start().whileTrue(Commands.none());
      driverXbox.back().whileTrue(Commands.none());
      driverXbox.leftBumper().whileTrue(Commands.runOnce(drivebase::lock, drivebase).repeatedly());
      driverXbox.rightBumper().onTrue(Commands.none());
    }

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand()
  {
    // An example command will be run in autonomous
    return drivebase.getAutonomousCommand("New Auto");
  }

  public void setMotorBrake(boolean brake)
  {
    drivebase.setMotorBrake(brake);
  }

  // Optional getters.
  public Elevator getElevator() {
    return elevator;
  }

  public Joint getJoint() {
    return joint;
  }

  // public Intake getIntake() {
  //   return intake;
  // }

  // public Feed getFeed() {
  //   return feed;
  // }

  public Shooter getShooter() {
    return shooter;
  }

  public Hang getHang() {
    return hang;
  }

  public SwerveSubsystem getDrivebase() {
    return drivebase;
  }
}











// // Copyright (c) FIRST and other WPILib contributors.
// // Open Source Software; you can modify and/or share it under the terms of
// // the WPILib BSD license file in the root directory of this project.

// package frc.robot;

// import com.pathplanner.lib.auto.NamedCommands;
// import edu.wpi.first.math.geometry.Pose2d;
// import edu.wpi.first.math.geometry.Rotation2d;
// import edu.wpi.first.math.geometry.Translation2d;
// import edu.wpi.first.wpilibj.DriverStation;
// import edu.wpi.first.wpilibj.Filesystem;
// import edu.wpi.first.wpilibj.RobotBase;
// import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj2.command.Commands;
// import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
// import edu.wpi.first.wpilibj2.command.button.Trigger;
// import frc.robot.Constants.OperatorConstants;
// import frc.robot.subsystems.swervedrive.SwerveSubsystem;
// import java.io.File;
// import swervelib.SwerveInputStream;

// /**
//  * This class is where the bulk of the robot should be declared. Since Command-based is a "declarative" paradigm, very
//  * little robot logic should actually be handled in the {@link Robot} periodic methods (other than the scheduler calls).
//  * Instead, the structure of the robot (including subsystems, commands, and trigger mappings) should be declared here.
//  */
// public class RobotContainer
// {

//   // Replace with CommandPS4Controller or CommandJoystick if needed
//   final         CommandXboxController driverXbox = new CommandXboxController(0);
//   // The robot's subsystems and commands are defined here...
//   private final SwerveSubsystem       drivebase  = new SwerveSubsystem(new File(Filesystem.getDeployDirectory(),
//                                                                                 "swerve/neo"));

//   /**
//    * Converts driver input into a field-relative ChassisSpeeds that is controlled by angular velocity.
//    */
//   SwerveInputStream driveAngularVelocity = SwerveInputStream.of(drivebase.getSwerveDrive(),
//                                                                 () -> driverXbox.getLeftY() * -1,
//                                                                 () -> driverXbox.getLeftX() * -1)
//                                                             .withControllerRotationAxis(driverXbox::getRightX)
//                                                             .deadband(OperatorConstants.DEADBAND)
//                                                             .scaleTranslation(0.8)
//                                                             .allianceRelativeControl(true);

//   /**
//    * Clone's the angular velocity input stream and converts it to a fieldRelative input stream.
//    */
//   SwerveInputStream driveDirectAngle = driveAngularVelocity.copy().withControllerHeadingAxis(driverXbox::getRightX,
//                                                                                              driverXbox::getRightY)
//                                                            .headingWhile(true);

//   /**
//    * Clone's the angular velocity input stream and converts it to a robotRelative input stream.
//    */
//   SwerveInputStream driveRobotOriented = driveAngularVelocity.copy().robotRelative(true)
//                                                              .allianceRelativeControl(false);

//   SwerveInputStream driveAngularVelocityKeyboard = SwerveInputStream.of(drivebase.getSwerveDrive(),
//                                                                         () -> -driverXbox.getLeftY(),
//                                                                         () -> -driverXbox.getLeftX())
//                                                                     .withControllerRotationAxis(() -> driverXbox.getRawAxis(
//                                                                         2))
//                                                                     .deadband(OperatorConstants.DEADBAND)
//                                                                     .scaleTranslation(0.8)
//                                                                     .allianceRelativeControl(true);
//   // Derive the heading axis with math!
//   SwerveInputStream driveDirectAngleKeyboard     = driveAngularVelocityKeyboard.copy()
//                                                                                .withControllerHeadingAxis(() ->
//                                                                                                               Math.sin(
//                                                                                                                   driverXbox.getRawAxis(
//                                                                                                                       2) *
//                                                                                                                   Math.PI) *
//                                                                                                               (Math.PI *
//                                                                                                                2),
//                                                                                                           () ->
//                                                                                                               Math.cos(
//                                                                                                                   driverXbox.getRawAxis(
//                                                                                                                       2) *
//                                                                                                                   Math.PI) *
//                                                                                                               (Math.PI *
//                                                                                                                2))
//                                                                                .headingWhile(true);

//   /**
//    * The container for the robot. Contains subsystems, OI devices, and commands.
//    */
//   public RobotContainer()
//   {
//     // Configure the trigger bindings
//     configureBindings();
//     DriverStation.silenceJoystickConnectionWarning(true);
//     NamedCommands.registerCommand("test", Commands.print("I EXIST"));
//   }

//   /**
//    * Use this method to define your trigger->command mappings. Triggers can be created via the
//    * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary predicate, or via the
//    * named factories in {@link edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for
//    * {@link CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller PS4}
//    * controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight joysticks}.
//    */
//   private void configureBindings()
//   {

//     Command driveFieldOrientedDirectAngle      = drivebase.driveFieldOriented(driveDirectAngle);
//     Command driveFieldOrientedAnglularVelocity = drivebase.driveFieldOriented(driveAngularVelocity);
//     Command driveRobotOrientedAngularVelocity  = drivebase.driveFieldOriented(driveRobotOriented);
//     Command driveSetpointGen = drivebase.driveWithSetpointGeneratorFieldRelative(
//         driveDirectAngle);
//     Command driveFieldOrientedDirectAngleKeyboard      = drivebase.driveFieldOriented(driveDirectAngleKeyboard);
//     Command driveFieldOrientedAnglularVelocityKeyboard = drivebase.driveFieldOriented(driveAngularVelocityKeyboard);
//     Command driveSetpointGenKeyboard = drivebase.driveWithSetpointGeneratorFieldRelative(
//         driveDirectAngleKeyboard);

//     if (RobotBase.isSimulation())
//     {
//       drivebase.setDefaultCommand(driveFieldOrientedDirectAngleKeyboard);
//     } else
//     {
//       drivebase.setDefaultCommand(driveFieldOrientedAnglularVelocity);
//     }

//     if (Robot.isSimulation())
//     {
//       driverXbox.start().onTrue(Commands.runOnce(() -> drivebase.resetOdometry(new Pose2d(3, 3, new Rotation2d()))));
//       driverXbox.button(1).whileTrue(drivebase.sysIdDriveMotorCommand());

//     }
//     if (DriverStation.isTest())
//     {
//       drivebase.setDefaultCommand(driveFieldOrientedAnglularVelocity); // Overrides drive command above!

//       driverXbox.x().whileTrue(Commands.runOnce(drivebase::lock, drivebase).repeatedly());
//       driverXbox.y().whileTrue(drivebase.driveToDistanceCommand(1.0, 0.2));
//       driverXbox.start().onTrue((Commands.runOnce(drivebase::zeroGyro)));
//       driverXbox.back().whileTrue(drivebase.centerModulesCommand());
//       driverXbox.leftBumper().onTrue(Commands.none());
//       driverXbox.rightBumper().onTrue(Commands.none());
//     } else
//     {
//       driverXbox.a().onTrue((Commands.runOnce(drivebase::zeroGyro)));
//       driverXbox.x().onTrue(Commands.runOnce(drivebase::addFakeVisionReading));
//       driverXbox.b().whileTrue(
//           drivebase.driveToPose(
//               new Pose2d(new Translation2d(4, 4), Rotation2d.fromDegrees(0)))
//                               );
//       driverXbox.start().whileTrue(Commands.none());
//       driverXbox.back().whileTrue(Commands.none());
//       driverXbox.leftBumper().whileTrue(Commands.runOnce(drivebase::lock, drivebase).repeatedly());
//       driverXbox.rightBumper().onTrue(Commands.none());
//     }

//   }

//   /**
//    * Use this to pass the autonomous command to the main {@link Robot} class.
//    *
//    * @return the command to run in autonomous
//    */
//   public Command getAutonomousCommand()
//   {
//     // An example command will be run in autonomous
//     return drivebase.getAutonomousCommand("New Auto");
//   }

//   public void setMotorBrake(boolean brake)
//   {
//     drivebase.setMotorBrake(brake);
//   }
// }
