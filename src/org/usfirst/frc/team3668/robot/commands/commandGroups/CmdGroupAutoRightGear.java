package org.usfirst.frc.team3668.robot.commands.commandGroups;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.Settings;
import org.usfirst.frc.team3668.robot.Settings.colors;
import org.usfirst.frc.team3668.robot.commands.CmdBothDriveWithProfileAndGyro;
import org.usfirst.frc.team3668.robot.commands.CmdTurnWithGyro;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CmdGroupAutoRightGear extends CommandGroup { // finished

	public CmdGroupAutoRightGear(colors color) {
		double colorBasedInches2BaseLine;
		double colorBasedInches2Lift;
		double colorBasedTurnDegrees;
		requires(Robot.subChassis);
		requires(Robot.subFeeder);

		if (color == Settings.colors.Red) {
			colorBasedInches2BaseLine = Settings.autoRightRedInchesToBaseline;
			colorBasedInches2Lift = Settings.autoRightGearInchesToLiftRed;
			colorBasedTurnDegrees = Settings.autoRightGearTurnDegreesRed;
		} else {
			colorBasedInches2BaseLine = Settings.autoRightBlueInchesToBaseline;
			colorBasedInches2Lift = Settings.autoRightGearInchesToLiftBlue;
			colorBasedTurnDegrees = Settings.autoRightGearTurnDegreesBlue;
		}

		addSequential(new CmdBothDriveWithProfileAndGyro(0, Settings.autoMoveInchesPerSecond,
				-(colorBasedInches2BaseLine - Settings.chassisInchesFromBumper2Pivot)));
		addSequential(new CmdTurnWithGyro(colorBasedTurnDegrees));
		addSequential(new CmdBothDriveWithProfileAndGyro(colorBasedTurnDegrees, Settings.autoMoveInchesPerSecond,
				-(colorBasedInches2Lift - Settings.chassisLengthOfRobot)));
		// addSequential(new CmdAutoCenterPlaceGearWithVision()); }
	}
}
