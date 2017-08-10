package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.Settings;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class CmdReverseShootMech extends Command {

	private boolean _isFinished = false;
	
    public CmdReverseShootMech() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.subShooter);
        requires(Robot.subFeeder);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.subShooter.run(Settings.shooterReverseSpeed, Settings.shooterReverseSpeed);
    	Robot.subFeeder.run(Settings.feederReverseSpeed);
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
    end();
    }
}
