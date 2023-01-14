// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.function.BooleanSupplier;


import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;

import frc.robot.commands.*;

import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final CommandXboxController driverController = new CommandXboxController(Constants.CONTROLLER_PORT);
  private final DriveTrainSubsystem m_DriveTrainSubsystem = new DriveTrainSubsystem(driverController);
  private final SensorSubsystem m_SensorSubsystem = new SensorSubsystem();
  private final AutonomousCommand m_AutonomousCommand = new AutonomousCommand(m_DriveTrainSubsystem, m_SensorSubsystem);
  private final pidSetLeftCommand moveLeftCommand = new pidSetLeftCommand(m_DriveTrainSubsystem);
  private final pidSetRightCommand moveRightCommand = new pidSetRightCommand(m_DriveTrainSubsystem);
  private final CalibrateGyro gyroCalibrate = new CalibrateGyro(m_SensorSubsystem);
  
  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    BooleanSupplier rightDeadzoneSupplier = () ->  driverController.getRightY() > Constants.DEADZONE;
    BooleanSupplier leftDeadzoneSupplier = () -> driverController.getLeftX() > Constants.DEADZONE;
    
    Trigger rightTrigger = driverController.rightStick();
    rightTrigger.and(rightDeadzoneSupplier);
    rightTrigger.whileTrue(moveRightCommand);

    Trigger leftTrigger = driverController.leftStick();
    leftTrigger.and(leftDeadzoneSupplier);
    leftTrigger.whileTrue(moveLeftCommand);

    Trigger calibrateTrigger = driverController.b();
    calibrateTrigger.onTrue(gyroCalibrate);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public AutonomousCommand getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return m_AutonomousCommand;
  }

  public double deadzone(double doubleArgument) {
    if(Math.abs(doubleArgument) < Constants.DEADZONE) return 0;
    else return doubleArgument;
  }
}