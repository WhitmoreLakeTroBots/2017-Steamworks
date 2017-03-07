package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.RobotMap;
import org.usfirst.frc.team3668.robot.RobotMath;
import org.usfirst.frc.team3668.robot.Settings;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class CmdTurnWithGyro extends Command {
	protected double _headingDegrees;
	protected boolean _isFinished = false;
	private double _initialHeading;
	private boolean _turnCompleted = false;
	private boolean _hasRecurred = false;
    public CmdTurnWithGyro(double headingDegrees) {
        // Use requires() here to declare subsystem dependencies
         requires(Robot.subChassis);
         _headingDegrees = headingDegrees;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	_initialHeading = Robot.subChassis.gyroGetRawHeading();
    	_isFinished = false;
    	_turnCompleted = false;
    	Robot.subChassis.resetBothEncoders();
    }
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double requestedHeading = RobotMath.normalizeAngles(_headingDegrees);
    	double currentHeading = Robot.subChassis.gyroGetRawHeading();
    	double initialHeadingDelta = RobotMath.normalizeAngles(requestedHeading - _initialHeading);
    	double headingDeltaRelativeToRobot = RobotMath.normalizeAngles(initialHeadingDelta + _initialHeading);
    	double currentHeadingDeltaTurn = RobotMath.headingDeltaTurn(currentHeading, headingDeltaRelativeToRobot);
    	double headingDiffFromInit = RobotMath.headingDeltaTurn(_initialHeading, currentHeading);
    	_turnCompleted = RobotMath.gyroAngleWithinMarginOfError(currentHeading, headingDeltaRelativeToRobot);
    	double logisticTurnValue = RobotMath.turnLogisticFunction(currentHeadingDeltaTurn, Settings.chassisTurnLogisticFunctionRate, Settings.chassisTurnLogisticFunctionMidpoint, Settings.chassisTurnLogisticFunctionMax, true);
    	double startupLogisticTurnValue = RobotMath.turnLogisticFunction(headingDiffFromInit, Settings.chassisTurnLogisticStartupFunctionRate, Settings.chassisTurnLogisticStartupFunctionMidpoint, Settings.chassisTurnLogisticStartupFunctionMax, false);
    	double turnValue = logisticTurnValue;
    	double turnValueSignum = Math.signum(turnValue);
    	double finalTurnValue = turnValue;
    	if(Math.abs(turnValue) < Settings.chassisTurnValueMinimum){
    		finalTurnValue = Settings.chassisTurnValueMinimum * turnValueSignum;
    	}
    	System.err.println("Time: " + RobotMath.getTime() + "\t Turn Value: " + turnValue  + "\t Current Heading Delta: " + currentHeadingDeltaTurn + "\t Current Heading: " + currentHeading);
    	SmartDashboard.putBoolean("Finished Turning? ", _isFinished);
    	SmartDashboard.putBoolean("Turn Completed? ", _turnCompleted);
    	if(!_turnCompleted){
    		Robot.subChassis.Drive(0, (-finalTurnValue));
    	} else if(_turnCompleted && !_hasRecurred){
    		_turnCompleted = false;
    		_hasRecurred = true;
    	} else if(_turnCompleted && _hasRecurred){
    		_isFinished = true;
    		System.err.println("We finished");
    	}
    }
 

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return _isFinished;
    
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.subChassis.Drive(0, 0);
    	Robot.subChassis.resetBothEncoders();
    	_isFinished = false;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.subChassis.Drive(0, 0);
    	_isFinished = true;
    }
}
