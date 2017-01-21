package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.RobotMath;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class CmdDriveWithGyro extends Command {
	private double _degrees;
	private double _inchesPerSecond;
	private boolean _isFinished;
    public CmdDriveWithGyro(double degrees, double inchesPerSecond) {
        // Use requires() here to declare subsystem dependencies
         requires(Robot.subChassis);
         _degrees = degrees;
         _inchesPerSecond = inchesPerSecond;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.subChassis.getEncoderAvgDistInch();
    	
    	
    	
    	
    	Robot.subChassis.Drive(_inchesPerSecond, _degrees);
    	_isFinished = RobotMath.gyroAngleWithinMarginOfError(Robot.subChassis.gyroGetRawHeading(), _degrees);
    }
 

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return _isFinished;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
