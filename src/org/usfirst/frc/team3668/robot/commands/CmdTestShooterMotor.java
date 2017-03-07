package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.RobotMap;
import org.usfirst.frc.team3668.robot.Settings;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class CmdTestShooterMotor extends Command {

    public CmdTestShooterMotor() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.subShooter);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.subShooter.run(Settings.shooterTargetThrottle, Settings.shooterTargetThrottle);
    	
    	SmartDashboard.putDouble("Left Shooter Encoder Rate", Robot.subShooter.shooterLeftLinearSpeed());
    	SmartDashboard.putDouble("Right Shooter Encoder Rate", (Robot.subShooter.shooterRightLinearSpeed()));
    	SmartDashboard.putDouble("Left Shooter Encoder Distance", RobotMap.shooterLeftMotorEncoder.getDistance());
    	SmartDashboard.putDouble("Right Shooter Encoder Distance", RobotMap.shooterRightMotorEncoder.getDistance());
    	SmartDashboard.putDouble("Left Shooter Motor Thottle", RobotMap.shooterMotorLeft.get());
    	SmartDashboard.putDouble("Right Shooter Motor Throttle", RobotMap.shooterMotorRight.get());
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
    	end();
    }
}
