package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.visionProcessing.VisionProcessing;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class CmdBothAlignToBoiler extends Command {
	private double _distanceToDrive;
	private double _angleToTurn;
	private boolean _isFinished = false;
	public CmdBothAlignToBoiler() {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		requires(Robot.subChassis);

	}

	// Called just before this Command runs the first time
	protected void initialize() {
//		_isFinished = true;
			_distanceToDrive = VisionProcessing.getBoilerCalculatedDistanceFromTarget()*12;
			_angleToTurn = VisionProcessing.getBoilerCalculatedAngleFromMidpoint();
		
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {

		System.err.println("Distance requested: " + _distanceToDrive);
		System.err.println("Inches Traveled : " + Robot.subChassis.getABSEncoderAvgDistInch());
		// addSequential(new CmdBothTurnWithProfile(_angleToTurn, 0.8 *
		// Math.signum(_angleToTurn)));
		// addSequential(new CmdBothDriveWithProfile(_distanceToDrive, 0.8));
		if (Robot.subChassis.getABSEncoderAvgDistInch() < _distanceToDrive) {
			Robot.subChassis.Drive(0.5, 0);
		} else {
			Robot.subChassis.Drive(0, 0);
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
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}
