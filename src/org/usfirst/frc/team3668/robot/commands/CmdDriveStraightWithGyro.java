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
public class CmdDriveStraightWithGyro extends Command {
	private double _headingDegrees;
	private double _inchesPerSecond;
	private double _inches;
	private double _rotationDirection;
	private boolean _isFinished;
	
    public CmdDriveStraightWithGyro(double headingDegrees, double inchesPerSecond, double inches) {
        // Use requires() here to declare subsystem dependencies
         requires(Robot.subChassis);
         _headingDegrees = headingDegrees;
         _inchesPerSecond = inchesPerSecond;
         _inches = inches;
         Robot.subChassis.resetBothEncoders();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    	
//    	Robot.subChassis.resetBothEncoders();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double distanceTravelled = Robot.subChassis.getEncoderAvgDistInch();
    	double currentHeading = Robot.subChassis.gyroGetRawHeading();
    	double throttle = _inchesPerSecond /Settings.robotMaxInchesPerSecond;
    	boolean distanceReached = Math.abs(distanceTravelled) > Math.abs(_inches);
    	boolean headingMaintained = RobotMath.gyroAngleWithinMarginOfError(currentHeading, _headingDegrees);
    	double turnValue = RobotMath.headingDelta(currentHeading, _headingDegrees, Settings.chassisDriveStraightGyroKp);
    	if(!distanceReached){
    		Robot.subChassis.Drive(throttle, turnValue);
    	} else if(distanceReached || !headingMaintained){
    		Robot.subChassis.Drive(0, turnValue);
    	} else if(distanceReached && headingMaintained){
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
