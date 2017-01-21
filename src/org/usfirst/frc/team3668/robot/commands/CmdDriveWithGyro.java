package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class CmdDriveWithGyro extends Command {

	private double _desiredHeading;
	
    public CmdDriveWithGyro(double desiredHeading) {
        // Use requires() here to declare subsystem dependencies
         requires(Robot.subChassis);
         _desiredHeading = desiredHeading;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
