package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.RobotMap;
import org.usfirst.frc.team3668.robot.Settings;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CmdTeleopShoot extends Command {

	private double _targetLinearSpeed;
	private double _targetThrottle;
	static double p = Settings.shooterControlPIDkp;
	static double i = Settings.shooterControlPICki;
	static double d = Settings.shooterControlPIDkd;
	double f = Settings.shooterControlPIDkf;
	
    public CmdTeleopShoot(double feetPerSecondShootSpeed/*, double p, double i, double d, double f*/) {
        // Use requires() here to declare subsystem dependencies
    	//super(p,i,d);
    	requires(Robot.subShooterLeft);
    	requires(Robot.subShooterRight);
        _targetLinearSpeed = feetPerSecondShootSpeed;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.subShooterRight.resetShooterEncoder();
    	Robot.subShooterLeft.resetShooterEncoder();
    	Robot.subShooterRight.setSetpoint(Settings.shooterTargetLinearVelocity);
    	Robot.subShooterLeft.setSetpoint(Settings.shooterTargetLinearVelocity);
    	Robot.subShooterRight.enable();
    	Robot.subShooterLeft.enable();
    	_targetThrottle = Robot.subShooterRight.motorSpeedValue(_targetLinearSpeed);
    }
    
//	@Override
//	protected double returnPIDInput() {
//		return 0;
//	}
//
//	@Override
//	protected void usePIDOutput(double output) {
//		
//	}

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

    	SmartDashboard.putNumber("Left Shooter Encoder Rate", Robot.subShooterLeft.shooterLeftLinearSpeed());
    	SmartDashboard.putNumber("Right Shooter Encoder Rate", Robot.subShooterRight.shooterRightLinearSpeed());
    	SmartDashboard.putNumber("Left Shooter Encoder Distance", RobotMap.shooterLeftMotorEncoder.getDistance());
    	SmartDashboard.putNumber("Right Shooter Encoder Distance", RobotMap.shooterRightMotorEncoder.getDistance());
    	SmartDashboard.putNumber("Left Shooter Motor Thottle", RobotMap.shooterMotorLeft.get());
    	SmartDashboard.putNumber("Right Shooter Motor Throttle", RobotMap.shooterMotorRight.get());
    	SmartDashboard.putNumber("Shoot Controller Output Right", Robot.subShooterRight.getPosition());
    	SmartDashboard.putNumber("Shoot Controller Output Left", Robot.subShooterLeft.getPosition());
    	SmartDashboard.putNumber("Set Point Right", Robot.subShooterRight.getSetpoint());
    	SmartDashboard.putNumber("Set Point Left", Robot.subShooterLeft.getSetpoint());
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.subShooterRight.disable();
    	Robot.subShooterLeft.disable();
    	Robot.subShooterRight.stop();
    	Robot.subShooterLeft.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
