package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.RobotMath;
import org.usfirst.frc.team3668.robot.Settings;

import edu.wpi.first.wpilibj.command.Command;

public class CmdBothShooter extends Command {
	private boolean _isFinished = false;
	private double _targetShootSpeed;
	private double _targetShootThrottle;
	private boolean _autoShoot;
	private double _shooterTargetSpeedWindowLower;
	private double _shooterTargetSpeedWindowUpper;
	private double _autoShootTime;
	private double _startAutoShootTime = 0;

	public CmdBothShooter(double feetPerSecondShootSpeed, boolean autoShoot, double autoShootTime) {
		// Use requires() here to declare subsystem dependencies
		requires(Robot.subShooter);
		requires(Robot.subFeeder);
		_targetShootSpeed = feetPerSecondShootSpeed;
		_autoShoot = autoShoot;
		_autoShootTime = autoShootTime;
	}

	public CmdBothShooter(double feetPerSecondShootSpeed) {
		// Use requires() here to declare subsystem dependencies
		requires(Robot.subShooter);
		requires(Robot.subFeeder);
		_targetShootSpeed = feetPerSecondShootSpeed;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		_targetShootThrottle = Robot.subShooter.motorSpeedValue(_targetShootSpeed);
		_shooterTargetSpeedWindowLower = _targetShootSpeed * Settings.shooterMotorSpeedWindowLowerPercentage;
		_shooterTargetSpeedWindowUpper = _targetShootSpeed * Settings.shooterMotorSpeedWindowUpperPercentage;
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		double leftMotorValue = calcShooterSpeed(Robot.subChassis.getLeftEncoderRate());
		double rightMotorValue = calcShooterSpeed(Robot.subChassis.getRightEncoderRate());

		Robot.subShooter.run(leftMotorValue, rightMotorValue);

		if (RobotMath.withinDeadBand(_targetShootThrottle, Settings.shooterDeadBandPercent, leftMotorValue)
				&& RobotMath.withinDeadBand(_targetShootThrottle, Settings.shooterDeadBandPercent, rightMotorValue)
				&& _autoShoot && deltaTime4Shooting() < _autoShootTime) {
			Robot.subFeeder.run(Settings.feederMotorSpeed);
			_isFinished = true;
		} else {
			Robot.subFeeder.run(0);
		}

	}

	private double calcShooterSpeed(double motorEncoderRate) {
		double motorValue = 0;
		double deltaRate = _targetShootSpeed - motorEncoderRate;
		if (motorValue < _shooterTargetSpeedWindowLower) {
			motorValue = 1.0;
		} else if (motorValue > _shooterTargetSpeedWindowLower && motorValue < _shooterTargetSpeedWindowUpper) {
			motorValue = _targetShootSpeed * (deltaRate * Settings.shooterProprotation);
		}
		return motorValue;
	}

	private double deltaTime4Shooting() {
		if (_startAutoShootTime == 0) {
			_startAutoShootTime = RobotMath.getTime();
		}
		double currentTime = RobotMath.getTime();
		return currentTime - _startAutoShootTime;
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return _isFinished;
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.subShooter.stop();
		Robot.subFeeder.run(0);

	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		_isFinished = true;
	}
}
