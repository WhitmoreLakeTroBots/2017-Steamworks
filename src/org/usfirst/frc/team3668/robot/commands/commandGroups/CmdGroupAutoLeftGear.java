package org.usfirst.frc.team3668.robot.commands.commandGroups;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.Settings;
import org.usfirst.frc.team3668.robot.Settings.colors;
import org.usfirst.frc.team3668.robot.commands.CmdAutoCenterPlaceGearWithVision;
import org.usfirst.frc.team3668.robot.commands.CmdBothDriveWithProfileAndGyro;
import org.usfirst.frc.team3668.robot.commands.CmdBothTurnWithProfile;
import org.usfirst.frc.team3668.robot.commands.CmdTurnWithGyro;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CmdGroupAutoLeftGear extends CommandGroup {// finished

	public CmdGroupAutoLeftGear(colors color) {
		double colorBasedInches2BaseLine;
		double colorBasedInches2Lift;
		double colorBasedTurnDegrees;
		requires(Robot.subChassis);
		requires(Robot.subFeeder);

		if (color == Settings.colors.Red) {
			colorBasedInches2BaseLine = Settings.autoLeftRedInchesToBaseline;
			colorBasedInches2Lift = Settings.autoLeftGearInchesToLiftRed;
			colorBasedTurnDegrees = Settings.autoLeftGearTurnDegreesRed;
		} else {
			colorBasedInches2BaseLine = Settings.autoLeftBlueInchesToBaseline;
			colorBasedInches2Lift = Settings.autoLeftGearInchesToLiftBlue;
			colorBasedTurnDegrees = Settings.autoLeftGearTurnDegreesBlue;
		}

		addSequential(new CmdBothDriveWithProfileAndGyro(0, Settings.autoMoveInchesPerSecond,
				-(colorBasedInches2BaseLine - Settings.chassisInchesFromBumper2Pivot)));
		addSequential(new CmdTurnWithGyro(colorBasedTurnDegrees));
		addSequential(new CmdBothDriveWithProfileAndGyro(colorBasedTurnDegrees, Settings.autoMoveInchesPerSecond,
				-(colorBasedInches2Lift - Settings.chassisLengthOfRobot)));
		// addSequential(new CmdAutoCenterPlaceGearWithVision());
	}
}
