package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.RobotMath;
import org.usfirst.frc.team3668.robot.Settings;

import edu.wpi.first.wpilibj.command.Command;

public class CmdBothReverseShooter extends Command {

	protected boolean _isFinished = false;
	private double _startTime = 0;
	private double _time = 1;
	
    public CmdBothReverseShooter() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.subShooterRight);
    }

    public CmdBothReverseShooter(double time) {
        requires(Robot.subShooterRight);
        _time = time;
        _startTime = RobotMath.getTime();
    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double deltaTime = 0;
    	if(_startTime != 0){
    		deltaTime = RobotMath.getTime() - _startTime;
    	}
    	if(deltaTime < _time){
    	Robot.subShooterRight.run(Settings.shooterReverseSpeed);
    	Robot.subShooterLeft.run(Settings.shooterReverseSpeed);
    	} else {
    		_isFinished = true;
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return _isFinished;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.subShooterRight.stop();
    	Robot.subShooterLeft.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
