package org.usfirst.frc.team3668.robot.commands.commandGroups;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.RobotMap;
import org.usfirst.frc.team3668.robot.Settings;
import org.usfirst.frc.team3668.robot.commands.CmdBothDriveWithProfile;
import org.usfirst.frc.team3668.robot.commands.CmdBothDriveWithProfileAndGyro;
import org.usfirst.frc.team3668.robot.commands.CmdBothTurnWithProfile;
import org.usfirst.frc.team3668.robot.commands.CmdDoNothing;
import org.usfirst.frc.team3668.robot.commands.CmdTurnWithGyro;
import org.usfirst.frc.team3668.robot.visionProcessing.VisionProcessing;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class CmdGroupBothAlignToBoilerWithVision extends CommandGroup {
	private static double _distanceToDrive;
	private static double _angleToTurn;
	public CmdGroupBothAlignToBoilerWithVision() {
		requires(Robot.subChassis);
		_distanceToDrive = VisionProcessing.getBoilerCalculatedDistanceFromTarget();
		_angleToTurn = VisionProcessing.getBoilerCalculatedAngleFromMidpoint();
		if (_distanceToDrive != 0 && _angleToTurn != 0 && VisionProcessing.hasFoundBoilerTarget()) {
			addSequential(new CmdTurnWithGyro(_angleToTurn));
			addSequential(new CmdTurnWithGyro(_angleToTurn));
			addSequential(new CmdBothDriveWithProfileAndGyro(_angleToTurn,
					Settings.robotMaxInchesPerSecond / 2, _distanceToDrive - Settings.shooterDistanceFromBoiler));
		} else{
			addSequential(new CmdDoNothing());
		}
	}
}