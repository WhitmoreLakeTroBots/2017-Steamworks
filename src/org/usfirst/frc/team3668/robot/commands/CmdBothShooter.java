package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.RobotMap;
import org.usfirst.frc.team3668.robot.Settings;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class CmdBothShooter extends Command {
	private boolean _isFinished = false;
	private double _targetShootSpeed;
	private double _shooterMotorSpeed;
	private double _shooterMotorSpeedReduced;
	private double _shooterTargetSpeedFactored;

	public CmdBothShooter(double feetPerSecondShootSpeed) {
		// Use requires() here to declare subsystem dependencies
		requires(Robot.subShooter);
		requires(Robot.subFeeder);
		_targetShootSpeed = feetPerSecondShootSpeed;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		_shooterMotorSpeed = Robot.subShooter.motorSpeedValue(_targetShootSpeed);
		_shooterMotorSpeedReduced = _shooterMotorSpeed * Settings.shooterMotorReducedRate;
		_shooterTargetSpeedFactored = _targetShootSpeed * Settings.shooterMotorSpeedMarginOfError;
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		double shooterLeftLinearSpeed = Robot.subShooter.shooterLeftLinearSpeed();
		double shooterRightLinearSpeed = Robot.subShooter.shooterRightLinearSpeed();
		double leftMotorValue;
		double rightMotorValue;
		 if(shooterLeftLinearSpeed < _shooterTargetSpeedFactored){
			 leftMotorValue = _shooterMotorSpeed;
		 } else if(shooterLeftLinearSpeed >= _shooterTargetSpeedFactored &&
		 shooterLeftLinearSpeed <= _shooterTargetSpeedFactored){
			 leftMotorValue = _targetShootSpeed;
		 }else{
		 leftMotorValue = 0;
		 }
		 if(shooterRightLinearSpeed < _shooterTargetSpeedFactored){
			 rightMotorValue = _shooterMotorSpeed;
		 } else if(shooterRightLinearSpeed >= _shooterTargetSpeedFactored &&
		 shooterRightLinearSpeed <= _targetShootSpeed){
			 rightMotorValue = _shooterMotorSpeedReduced;
		 }else{
			 rightMotorValue = 0;
		 }
		Robot.subShooter.run(leftMotorValue, rightMotorValue);
		if (shooterLeftLinearSpeed > _shooterMotorSpeedReduced && shooterRightLinearSpeed > _shooterMotorSpeedReduced) {
			Robot.subFeeder.run(Settings.feederMotorSpeed);
		} else{
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
		Robot.subFeeder.run(0);

	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		_isFinished = true;
	}
}
