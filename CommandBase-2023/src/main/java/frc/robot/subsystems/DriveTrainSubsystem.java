package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

//This drivetrain code is built in accordance with last year's tankdrive drivetrain.
public class DriveTrainSubsystem extends SubsystemBase {
    private final WPI_VictorSPX frontleftMotor;
    private final WPI_VictorSPX backleftMotor;
    private final WPI_VictorSPX frontrightMotor;
    private final WPI_VictorSPX backrightMotor;//right and left motors should move together.
    public final CommandXboxController controller;
    
    //Assuming speed is in percentage.
    //FYI: 1: double is just a better float. 2: speed is a percentage but as a decimal.
    public DriveTrainSubsystem(CommandXboxController _controller) {
        controller = _controller;
        frontleftMotor = new WPI_VictorSPX(Constants.FRONT_LEFT_MOTOR_PORT);
        backleftMotor = new WPI_VictorSPX(Constants.BACK_LEFT_MOTOR_PORT);
        frontrightMotor = new WPI_VictorSPX(Constants.FRONT_RIGHT_MOTOR_PORT);
        backrightMotor = new WPI_VictorSPX(Constants.BACK_RIGHT_MOTOR_PORT);
        backrightMotor.follow(frontrightMotor);
        backleftMotor.follow(frontleftMotor);
    }

    public void activateLeft() {
        frontleftMotor.set(ControlMode.PercentOutput, Constants.AutoSpeed);
    }

    public void activateRight() {
        frontrightMotor.set(ControlMode.PercentOutput, Constants.AutoSpeed);
    }

    public void pidActivateRight(double _activateSpeed) {
        frontrightMotor.set(ControlMode.PercentOutput, _activateSpeed);
    }

    public void pidActivateLeft(double _activateSpeed) {
        frontleftMotor.set(ControlMode.PercentOutput, _activateSpeed);
    }
    
    public void stopDriving() { // Brings the robot to a standstill.
        backrightMotor.set(ControlMode.PercentOutput, 0);
        frontleftMotor.set(ControlMode.PercentOutput, 0);
    }

    public double leftMeasurement() {
        return frontleftMotor.getActiveTrajectoryArbFeedFwd();
    }

    public double rightMeasurment() {
        return frontrightMotor.getActiveTrajectoryArbFeedFwd();
    }
    
    @Override
    public void periodic() {
      // This method will be called once per scheduler run

    }
}