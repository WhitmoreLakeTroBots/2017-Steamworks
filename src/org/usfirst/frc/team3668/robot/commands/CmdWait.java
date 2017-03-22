package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.RobotMath;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class CmdWait extends Command {
	private boolean _isFinished;
	private double _waitTime;
	private double _timeAtInit;
    public CmdWait(double waitTime) {
    	_waitTime = waitTime;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	_timeAtInit = RobotMath.getTime();
    	_isFinished = false;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if((RobotMath.getTime() - _timeAtInit) > _waitTime){
    		_isFinished = true;
    	}
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
