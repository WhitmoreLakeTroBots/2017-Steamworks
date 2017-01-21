package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.RobotMath;
import org.usfirst.frc.team3668.robot.Settings;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class CmdDriveWithGyro extends Command {
	private double _degrees;
	private double _inchesPerSecond;
	private double _inches;
	private boolean _isFinished;
    public CmdDriveWithGyro(double degrees, double inchesPerSecond, double inches) {
        // Use requires() here to declare subsystem dependencies
         requires(Robot.subChassis);
         _degrees = degrees;
         _inchesPerSecond = inchesPerSecond;
         _inches = inches;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.subChassis.resetBothEncoders();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	double distanceTraveled = Robot.subChassis.getEncoderAvgDistInch();
//    	System.out.println();
    	SmartDashboard.putNumber("Distance Travelled: ", distanceTraveled);
    	Robot.subChassis.Drive(_inchesPerSecond /Settings.robotMaxInchesPerSecond, 0);    	
    	if(distanceTraveled > _inches){
    		end();
    	}
    	
    	
    	

//    	_isFinished = (RobotMath.gyroAngleWithinMarginOfError(Robot.subChassis.gyroGetRawHeading(), _degrees));
    }
 

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return _isFinished;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.subChassis.Drive(0, 0);
    	_isFinished = true;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.subChassis.Drive(0, 0);
    	_isFinished = true;
    }
}
