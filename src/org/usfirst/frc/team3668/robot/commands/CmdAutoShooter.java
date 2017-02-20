package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.RobotMath;
import org.usfirst.frc.team3668.robot.Settings;

import edu.wpi.first.wpilibj.command.Command;

public class CmdAutoShooter extends Command {
	private boolean _isFinished = false;
	private double _targetShootSpeed;
	private double _targetShootThrottle;

	public CmdAutoShooter(double feetPerSecondShootSpeed) {
		// Use requires() here to declare subsystem dependencies
		requires(Robot.subShooter);
		requires(Robot.subFeeder);
		_targetShootSpeed = feetPerSecondShootSpeed;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		_targetShootThrottle = Robot.subShooter.motorSpeedValue(_targetShootSpeed);
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		double leftMotorValue = RobotMath.calcShooterSpeed(Robot.subChassis.getLeftEncoderRate(), _targetShootSpeed, _targetShootThrottle);
		double rightMotorValue = RobotMath.calcShooterSpeed(Robot.subChassis.getRightEncoderRate(),_targetShootSpeed, _targetShootThrottle);

		Robot.subShooter.run(leftMotorValue, rightMotorValue);

		if (RobotMath.withinDeadBand(_targetShootThrottle, Settings.shooterDeadBandPercent, leftMotorValue)
				&& RobotMath.withinDeadBand(_targetShootThrottle, Settings.shooterDeadBandPercent, rightMotorValue)) {
			Robot.subFeeder.run(Settings.feederMotorSpeed);
			//_isFinished = true;
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
		//_isFinished = true;
		end();
	}
}
