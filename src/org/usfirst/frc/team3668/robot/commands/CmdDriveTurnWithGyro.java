package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.RobotMath;
import org.usfirst.frc.team3668.robot.Settings;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class CmdDriveTurnWithGyro extends Command {
	private double _headingDegrees;
	private double _inchesPerSecond;
	private double _inches;
	private boolean _isFinished;
	private double _initialHeading;
	
    public CmdDriveTurnWithGyro(double headingDegrees) {
        // Use requires() here to declare subsystem dependencies
         requires(Robot.subChassis);
         _headingDegrees = headingDegrees;
         Robot.subChassis.resetBothEncoders();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	_initialHeading = Robot.subChassis.gyroGetRawHeading();
    	_isFinished = false;
//    	Robot.subChassis.resetBothEncoders();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double currentHeading = Robot.subChassis.gyroGetRawHeading();
    	double startTime = RobotMath.getTime();
    	double headingDegreesRelativeToRobotOrientation = RobotMath.normalizeAngles(_initialHeading + _headingDegrees);
//    	double turnValueFast = RobotMath.headingDelta(currentHeading, headingDegreesRelativeToRobotOrientation, 50);
//    	double turnValueSlow = RobotMath.headingDelta(currentHeading, headingDegreesRelativeToRobotOrientation, 20);
    	double headingDeltaTurn = RobotMath.headingDeltaTurn(currentHeading, headingDegreesRelativeToRobotOrientation);
    	double headingDiffFromInit = RobotMath.headingDeltaTurn(currentHeading, _initialHeading);
    	boolean turnCompleted = RobotMath.gyroAngleWithinMarginOfError(currentHeading, headingDegreesRelativeToRobotOrientation);
    	double deltaHeading = Math.abs(Math.abs(currentHeading) - Math.abs(headingDegreesRelativeToRobotOrientation));
    	double turnValue = 0.8 * RobotMath.turnLogisticFunction(headingDiffFromInit, Settings.chassisTurnLogisticStartupFunctionRate, Settings.chassisTurnLogisticStartupFunctionMidpoint, Settings.chassisTurnLogisticStartupFunctionMax, false) * RobotMath.turnLogisticFunction(headingDeltaTurn, Settings.chassisTurnLogisticFunctionRate, Settings.chassisTurnLogisticFunctionMidpoint, Settings.chassisTurnLogisticFunctionMax, true);
    	double turnValueSignum = Math.signum(turnValue);
    	SmartDashboard.putNumber("Desired Heading Relative: ", headingDegreesRelativeToRobotOrientation);
    	SmartDashboard.putBoolean("Turn Completed: ", turnCompleted);
    	System.out.println("Left Encoder: " + Robot.subChassis.getLeftEncoderDistInch() + "\t Right Encoder: " + Robot.subChassis.getRightEncoderDistInch());
//    	SmartDashboard.putNumber("Turn Value Fast: ", turnValueFast);
//    	SmartDashboard.putNumber("Turn Value Slow: ", turnValueSlow);
    	SmartDashboard.putNumber("TurnValue: ", RobotMath.timeThrottle(turnValue, RobotMath.getTime()-startTime, startTime));
    	if(turnValue < Settings.chassisTurnValueMinimum){
    		turnValue = Settings.chassisTurnValueMinimum * turnValueSignum;
    	}
    	if(!turnCompleted){
    		Robot.subChassis.Drive(0, turnValue);
    	} else if(turnCompleted){
    		_isFinished = true;
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
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.subChassis.Drive(0, 0);
    	_isFinished = true;
    }
}
