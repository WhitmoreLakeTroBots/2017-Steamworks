package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.RobotMap;
import org.usfirst.frc.team3668.robot.RobotMath;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class CmdTeleopShoot extends PIDCommand {

	private double _targetLinearSpeed;
	private double _targetThrottle;
	private PIDController shootControlRight;
	private PIDController shootControlLeft;
	
    public CmdTeleopShoot(double feetPerSecondShootSpeed, double p, double i, double d, double f) {
        // Use requires() here to declare subsystem dependencies
    	super(p, i, d);
    	shootControlRight = new PIDController(p, i, d, f, RobotMap.shooterRightMotorEncoder, RobotMap.shooterMotorRight);
    	shootControlRight.setContinuous(true);
    	shootControlLeft = new PIDController(p, i, d, f, RobotMap.shooterLeftMotorEncoder, RobotMap.shooterMotorLeft);
    	shootControlLeft.setContinuous(true);
    	requires(Robot.subShooter);
        _targetLinearSpeed = feetPerSecondShootSpeed;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	shootControlRight.setSetpoint(_targetLinearSpeed);
    	shootControlRight.enable();
    	shootControlLeft.setSetpoint(-_targetLinearSpeed);
    	shootControlLeft.enable();
    	_targetThrottle = Robot.subShooter.motorSpeedValue(_targetLinearSpeed);
    }
    
	@Override
	protected double returnPIDInput() {
		return 0;
	}

	@Override
	protected void usePIDOutput(double output) {
		
	}

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
//    	double leftMotorValue = RobotMath.calcShooterSpeed(Robot.subShooter.shooterLeftLinearSpeed(),_targetLinearSpeed, _targetThrottle);
//		double rightMotorValue = RobotMath.calcShooterSpeed(Robot.subShooter.shooterRightLinearSpeed(),_targetLinearSpeed, _targetThrottle);
//		
//		Robot.subShooter.run(leftMotorValue, rightMotorValue);
//    	//Robot.subShooter.run(Settings.shooterTargetThrottle, Settings.shooterTargetThrottle);
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

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	shootControlLeft.disable();
    	shootControlRight.disable();
    	Robot.subShooter.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
