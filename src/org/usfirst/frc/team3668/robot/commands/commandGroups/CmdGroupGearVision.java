package org.usfirst.frc.team3668.robot.commands.commandGroups;

import org.usfirst.frc.team3668.robot.commands.CmdBothVisionTurnWithGyro;
import org.usfirst.frc.team3668.robot.commands.CmdTurnWithGyro;
import org.usfirst.frc.team3668.robot.commands.CmdWait;
import org.usfirst.frc.team3668.robot.visionProcessing.VisionProcessing;
import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.Settings;
import org.usfirst.frc.team3668.robot.commands.CmdBothDriveWithProfileAndGyro;
import org.usfirst.frc.team3668.robot.commands.CmdBothVisionDriveWithProfileAndGyro;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class CmdGroupGearVision extends CommandGroup {
	private double distance;
	private double heading;

	public CmdGroupGearVision() {

		//addSequential(new CmdWait(1));
		addSequential(new CmdBothDriveWithProfileAndGyro(0, Settings.autoMoveInchesPerSecond,
				(Settings.autoRightRedInchesToBaseline - Settings.chassisInchesFromBumper2Pivot)));
		addSequential(new CmdTurnWithGyro(Settings.autoRightGearTurnDegreesRed));
		addSequential(new CmdBothVisionDriveWithProfileAndGyro());
	}
	
	@Override
	protected void initialize(){
		//Robot.visionProcessing.start();
	}
	
	@Override
	protected void end(){
		//Robot.visionProcessing.stop();
	}
}