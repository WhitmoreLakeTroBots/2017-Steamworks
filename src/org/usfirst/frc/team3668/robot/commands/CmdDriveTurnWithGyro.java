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
//	private double _inchesPerSecond;
//	private double _inches;
	private boolean _isFinished;
	private double _initialHeading;
	private double _startTime;
//	private double _stopTime;
	
    public CmdDriveTurnWithGyro(double headingDegrees) {
        // Use requires() here to declare subsystem dependencies
         requires(Robot.subChassis);
         _headingDegrees = headingDegrees;
         Robot.subChassis.resetBothEncoders();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	_startTime = RobotMath.getTime();
    	_initialHeading = Robot.subChassis.gyroGetRawHeading();
    	_isFinished = false;
//    	Robot.subChassis.resetBothEncoders();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double currentHeading = Robot.subChassis.gyroGetRawHeading();
    	double headingDegreesRelativeToRobotOrientation = RobotMath.normalizeAngles(_initialHeading + _headingDegrees);
//    	double turnValueFast = RobotMath.headingDelta(currentHeading, headingDegreesRelativeToRobotOrientation, 50);
//    	double turnValueSlow = RobotMath.headingDelta(currentHeading, headingDegreesRelativeToRobotOrientation, 20);
    	double deltaTime = RobotMath.getTime() - _startTime;
    	double headingDeltaTurn = RobotMath.headingDeltaTurn(currentHeading, headingDegreesRelativeToRobotOrientation);
    	boolean turnCompleted = RobotMath.gyroAngleWithinMarginOfError(currentHeading, headingDegreesRelativeToRobotOrientation);
    	double headingDeltaFromStart = RobotMath.headingDeltaTurn(currentHeading, _initialHeading);
//    	double deltaHeading = Math.abs(Math.abs(currentHeading) - Math.abs(headingDegreesRelativeToRobotOrientation));
    	double turnValueLogistic = RobotMath.turnLogisticFunction(headingDeltaTurn);
    	double signumTurnValue = Math.signum(turnValueLogistic);
    	SmartDashboard.putNumber("Desired Heading Relative: ", headingDegreesRelativeToRobotOrientation);
    	double timeTurnComp = deltaTime/20 /*Settings.chassisTurnTimeProportion*/;
    	SmartDashboard.putBoolean("Turn Completed: ", turnCompleted);
    	System.out.println("Left Encoder: " + Robot.subChassis.getLeftEncoderDistInch() + "\t Right Encoder: " + Robot.subChassis.getRightEncoderDistInch());
    	SmartDashboard.putNumber("Mathed turn value: ", (turnValueLogistic*RobotMath.turnExponentialFunction(headingDeltaFromStart))+(timeTurnComp*signumTurnValue));
    	SmartDashboard.putNumber("Logistic: ", turnValueLogistic);
    	SmartDashboard.putNumber("Exponential: ", RobotMath.turnExponentialFunction(headingDeltaFromStart));
    	SmartDashboard.putNumber("Time Comp: ", timeTurnComp*signumTurnValue);
    	
    	if(!turnCompleted){
    		Robot.subChassis.Drive(0, (turnValueLogistic*RobotMath.turnExponentialFunction(headingDeltaFromStart))+(timeTurnComp*signumTurnValue));
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
