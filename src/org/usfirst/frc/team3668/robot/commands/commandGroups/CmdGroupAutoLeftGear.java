package org.usfirst.frc.team3668.robot.commands.commandGroups;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.Settings;
import org.usfirst.frc.team3668.robot.Settings.colors;
import org.usfirst.frc.team3668.robot.commands.CmdAutoCenterPlaceGearWithVision;
import org.usfirst.frc.team3668.robot.commands.CmdBothDriveWithProfileAndGyro;
import org.usfirst.frc.team3668.robot.commands.CmdBothTurnWithProfile;
import org.usfirst.frc.team3668.robot.commands.CmdTurnWithGyro;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CmdGroupAutoLeftGear extends CommandGroup {//finished 

    public CmdGroupAutoLeftGear(colors color) {
    	requires(Robot.subChassis);
		requires(Robot.subFeeder);
    	
    	addSequential(new CmdBothDriveWithProfileAndGyro(0, Settings.autoMoveInchesPerSecond, -(Settings.autoInchesToBaseline-Settings.chassisInchesFromBumper2Pivot)));
    	addSequential(new CmdTurnWithGyro(Settings.autoLeftGearTurnDegrees));
    	//addSequential(new CmdAutoCenterPlaceGearWithVision());
    	addSequential(new CmdBothDriveWithProfileAndGyro(Settings.autoLeftGearTurnDegrees, Settings.autoMoveInchesPerSecond, -(Settings.autoGearInchesToLift - Settings.chassisLengthOfRobot)));

        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    }
}
