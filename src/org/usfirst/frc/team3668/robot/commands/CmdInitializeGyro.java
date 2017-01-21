package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.Settings;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class CmdInitializeGyro extends Command {

	private boolean _isFinished = false;
	
    public CmdInitializeGyro() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.subChassis.initializeGyro();
    	Robot.subChassis.gyroTimerStart();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	_isFinished = Robot.subChassis.gyroTimerElapsed(Settings.chassisGyroInitTime);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return _isFinished;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.subChassis.gyroTimerStop();
    	Robot.subChassis.gyroTimerReset();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
