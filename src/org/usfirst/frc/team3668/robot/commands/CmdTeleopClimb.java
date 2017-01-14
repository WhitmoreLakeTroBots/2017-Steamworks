package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class CmdTeleopClimb extends Command {

	private boolean _isFinished = false;
    public CmdTeleopClimb() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	Robot.subClimber.Climb();
    	
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	
        return _isFinished;
    }

    // Called once after isFinished returns true
    protected void end() {
    	
    	Robot.subClimber.StopClimb();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	
   _isFinished = true;
        
    }
}
