package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.RobotMap;
import org.usfirst.frc.team3668.robot.RobotMath;
import org.usfirst.frc.team3668.robot.Settings;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CmdAutoShooter extends PIDCommand {
	private boolean _isFinished = false;
	private double _targetShootSpeed;
	private double _targetShootThrottle;
	private PIDController shootControlRight;
	private PIDController shootControlLeft;

	public CmdAutoShooter(double feetPerSecondShootSpeed, double p, double i, double d, double f) {
		// Use requires() here to declare subsystem dependencies
		super(p, i, d);
		requires(Robot.subShooter);
		requires(Robot.subFeeder);
		_targetShootSpeed = feetPerSecondShootSpeed;
		shootControlRight = new PIDController(p, i, d, f, RobotMap.shooterRightMotorEncoder,
				RobotMap.shooterMotorRight);
		shootControlRight.setContinuous(true);
		shootControlLeft = new PIDController(p, i, d, f, RobotMap.shooterLeftMotorEncoder, RobotMap.shooterMotorLeft);
		shootControlLeft.setContinuous(true);

	}

	// Called just before this Command runs the first time
	protected void initialize() {
		shootControlRight.setSetpoint(_targetShootSpeed);
		shootControlRight.enable();
		shootControlLeft.setSetpoint(-_targetShootSpeed);
		shootControlLeft.enable();
		_targetShootThrottle = Robot.subShooter.motorSpeedValue(_targetShootSpeed);
	}

	@Override
	protected double returnPIDInput() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void usePIDOutput(double output) {
		// TODO Auto-generated method stub

	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		// double leftMotorValue =
		// RobotMath.calcShooterSpeed(Robot.subChassis.getLeftEncoderRate(),
		// _targetShootSpeed, _targetShootThrottle);
		// double rightMotorValue =
		// RobotMath.calcShooterSpeed(Robot.subChassis.getRightEncoderRate(),_targetShootSpeed,
		// _targetShootThrottle);
		//
		// Robot.subShooter.run(leftMotorValue, rightMotorValue);
		//
		
    	SmartDashboard.putDouble("Left Shooter Encoder Rate", Robot.subShooter.shooterLeftLinearSpeed());
    	SmartDashboard.putDouble("Right Shooter Encoder Rate", (Robot.subShooter.shooterRightLinearSpeed()));
    	SmartDashboard.putDouble("Left Shooter Encoder Distance", RobotMap.shooterLeftMotorEncoder.getDistance());
    	SmartDashboard.putDouble("Right Shooter Encoder Distance", RobotMap.shooterRightMotorEncoder.getDistance());
    	SmartDashboard.putDouble("Left Shooter Motor Thottle", RobotMap.shooterMotorLeft.get());
    	SmartDashboard.putDouble("Right Shooter Motor Throttle", RobotMap.shooterMotorRight.get());
    	SmartDashboard.putDouble("Shoot Controller Output Right", shootControlRight.get());
    	SmartDashboard.putDouble("Shoot Controller Output Left", shootControlLeft.get());
    	SmartDashboard.putDouble("Set Point Right", shootControlRight.getSetpoint());
    	SmartDashboard.putDouble("Set Point Left", shootControlLeft.getSetpoint());
		
		if (Robot.subShooter.shooterRightLinearSpeed() >= shootControlRight.get()
				&& Robot.subShooter.shooterLeftLinearSpeed() <= shootControlLeft
						.get() /*
								 * RobotMath.withinDeadBand(
								 * _targetShootThrottle,
								 * Settings.shooterDeadBandPercent,
								 * leftMotorValue) && RobotMath.withinDeadBand(
								 * _targetShootThrottle,
								 * Settings.shooterDeadBandPercent,
								 * rightMotorValue)
								 */) {
			Robot.subFeeder.run(Settings.feederMotorSpeed);
			// _isFinished = true;
		} else {
			Robot.subFeeder.run(0);
		}

	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return _isFinished;
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.subShooter.stop();
		Robot.subFeeder.stopFeed();

	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		// _isFinished = true;
		end();
	}
}
