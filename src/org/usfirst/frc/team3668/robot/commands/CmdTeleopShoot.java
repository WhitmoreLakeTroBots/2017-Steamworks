package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.RobotMath;
import org.usfirst.frc.team3668.robot.Settings;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class CmdTeleopShoot extends Command {

	private double _targetLinearSpeed;
	private double _targetThrottle;
	
    public CmdTeleopShoot(double feetPerSecondShootSpeed) {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.subShooter);
        _targetLinearSpeed = feetPerSecondShootSpeed;
        
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	_targetThrottle = Robot.subShooter.motorSpeedValue(_targetLinearSpeed);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double leftMotorValue = RobotMath.calcShooterSpeed(Robot.subChassis.getLeftEncoderRate(), _targetLinearSpeed);
		double rightMotorValue = RobotMath.calcShooterSpeed(Robot.subChassis.getRightEncoderRate(),_targetLinearSpeed);
		
    	Robot.subShooter.run(Settings.shooterTargetThrottle, Settings.shooterTargetThrottle);
    	SmartDashboard.putDouble("Left Shooter Encoder:", Robot.subShooter.shooterLeftLinearSpeed());
    	SmartDashboard.putDouble("Right Shooter Encoder", Robot.subShooter.shooterRightLinearSpeed());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.subShooter.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.subShooter.stop();
    }
}
