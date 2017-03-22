package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.visionProcessing.VisionProcessing;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class CmdBothVisionTurnWithGyro extends CmdTurnWithGyro {

	public CmdBothVisionTurnWithGyro() {
		super(0);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		_headingDegrees = VisionProcessing.getVisionData().angleToTarget + Robot.subChassis.gyroGetRawHeading();
		super.initialize();
	}
}
