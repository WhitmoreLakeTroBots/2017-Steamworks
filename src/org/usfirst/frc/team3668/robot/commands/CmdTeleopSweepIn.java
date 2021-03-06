package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CmdTeleopSweepIn extends Command {

    public CmdTeleopSweepIn() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.subSweeper);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//SmartDashboard.putBoolean("Sweeping in", true);
      	Robot.subSweeper.sweep(); 
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	//SmartDashboard.putBoolean("Sweeping in", false);
    	Robot.subSweeper.stopSweep();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}