// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.function.BooleanSupplier;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.kauailabs.navx.frc.AHRS;

import java.lang.Math;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
  private final AHRS gyro = new AHRS(SPI.Port.kMXP);
  private final TankDriveSubsystemBase leftDriveSubsystem = new TankDriveSubsystemBase(Constants.PRIMARY_LEFT_MOTOR_PORT, Constants.SECONDARY_LEFT_MOTOR_PORT, ControlMode.Position);
  private final TankDriveSubsystemBase rightDriveSubsystem = new TankDriveSubsystemBase(Constants.PRIMARY_RIGHT_MOTOR_PORT, Constants.SECONDARY_RIGHT_MOTOR_PORT, true, true, ControlMode.Position);
  private final pidSetMotor leftDriveCommand = new pidSetMotor(leftDriveSubsystem, driverController, Constants.CONTROLLER_LEFT_AXIS);
  private final pidSetMotor rightDriveCommand = new pidSetMotor(rightDriveSubsystem, driverController, Constants.CONTROLLER_RIGHT_AXIS);
  private final AutonomousCommand m_AutonomousCommand = new AutonomousCommand(gyro, rightDriveSubsystem, leftDriveSubsystem);
  

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  RobotContainer() {
    // Configure the button bindings
    gyro.calibrate();
    while (gyro.isCalibrating()) {
      SmartDashboard.putBoolean("Gyro Calibratig", gyro.isCalibrating());
    }
    buildShuffleboard();
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    BooleanSupplier rightThumbstickSupplier = () ->  deadzone(driverController.getRawAxis(Constants.CONTROLLER_RIGHT_AXIS));
    BooleanSupplier leftThumbstickSupplier = () ->  deadzone(driverController.getRawAxis(Constants.CONTROLLER_LEFT_AXIS));

    BooleanSupplier rightMotorSupplier = () -> rightDriveSubsystem.getEncoder() == 0;
    BooleanSupplier leftMotorSupplier = () -> leftDriveSubsystem.getEncoder() == 0;

    Trigger rightDriveTrigger = new Trigger(rightThumbstickSupplier).and(rightMotorSupplier);
    rightDriveTrigger.whileTrue(rightDriveCommand);

    Trigger leftDriveTrigger = new Trigger(leftThumbstickSupplier).and(leftMotorSupplier);
    leftDriveTrigger.whileTrue(leftDriveCommand);
    
  }
  /**
   * Use this to pass the autonomous command to the main {@link Robot} class
   *
   * @return the command to run in autonomous
   */
  public AutonomousCommand getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return m_AutonomousCommand;
  }

  public boolean deadzone(double doubleArgument) {
    if(Math.abs(doubleArgument) - Constants.JOYSTICK_CENTERPOINT < Constants.DEADZONE) return false;
    else return true;
  }

  private void buildDriverTab() {
    ShuffleboardTab motors = Shuffleboard.getTab("SmartDashboard");
    motors.add("Motor Voltage", true).withPosition(4, 0).withWidget(BuiltInWidgets.kTextView);
    ShuffleboardTab pidOutput = Shuffleboard.getTab("SmartDashboard");
    pidOutput.add("PID output", true).withPosition(4, 0).withWidget(BuiltInWidgets.kTextView);
    ShuffleboardTab gyroPosition = Shuffleboard.getTab("SmartDashboard");
    gyroPosition.add("Current Gyro Angle", true).withPosition(4, 0).withWidget(BuiltInWidgets.kTextView);
  }

  private void buildShuffleboard() {
    buildDriverTab();
  }
}