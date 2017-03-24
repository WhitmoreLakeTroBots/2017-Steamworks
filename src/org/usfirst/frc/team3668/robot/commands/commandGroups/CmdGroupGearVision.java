package org.usfirst.frc.team3668.robot.commands.commandGroups;

import org.usfirst.frc.team3668.robot.commands.CmdBothVisionTurnWithGyro;
import org.usfirst.frc.team3668.robot.commands.CmdTurnWithGyro;
import org.usfirst.frc.team3668.robot.commands.CmdWait;
import org.usfirst.frc.team3668.robot.visionProcessing.VisionProcessing;
import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.commands.CmdBothVisionDriveWithProfileAndGyro;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class CmdGroupGearVision extends CommandGroup {
	private double distance;
	private double heading;

	public CmdGroupGearVision() {
		Robot.visionProcessing.start();
		// distance = VisionProcessing.getGearCalculatedDistanceFromTarget();
		// heading = VisionProcessing.getGearCalculatedAngleFromTarget();
		// System.err.println(heading);
		addSequential(new CmdBothVisionTurnWithGyro());
		addSequential(new CmdWait(1));
		addSequential(new CmdBothVisionDriveWithProfileAndGyro());
		// heading = VisionProcessing.getGearCalculatedAngleFromTarget();
		// distance = VisionProcessing.getGearCalculatedDistanceFromTarget();
		// addSequential(new CmdWait(1));
		// addSequential(new CmdBothVisionTurnWithGyro());
		// addSequential(new CmdBothVisionDriveWithProfileAndGyro());
		Robot.visionProcessing.stop();
	}
}