package org.usfirst.frc.team3668.robot.commands.commandGroups;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.Settings;
import org.usfirst.frc.team3668.robot.commands.CmdAutoCenterPlaceGearWithVision;
import org.usfirst.frc.team3668.robot.commands.CmdBothShooter;
import org.usfirst.frc.team3668.robot.commands.CmdBothTurnWithProfile;
import org.usfirst.frc.team3668.robot.commands.CmdDriveStraightWithGyro;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class CmdGroupAutoRedLeftGear extends CommandGroup {//finished 

    public CmdGroupAutoRedLeftGear() {
requires(Robot.subChassis);

    	
    	addSequential(new CmdDriveStraightWithGyro(Settings.autoLeftGearStep1HeadingDegrees, Settings.autoLeftGearInchesPerSecond, Settings.autoLeftGearStep1Inches));
    	addSequential(new CmdBothTurnWithProfile(Settings.autoLeftGearTurnDegrees, Settings.autoLeftGearInchesPerSecond));
    	addSequential(new CmdAutoCenterPlaceGearWithVision());



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
