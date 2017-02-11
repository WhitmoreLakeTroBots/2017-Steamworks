package org.usfirst.frc.team3668.robot.commands.commandGroups;

import org.usfirst.frc.team3668.robot.Settings;
import org.usfirst.frc.team3668.robot.commands.CmdBothTurnWithProfile;
import org.usfirst.frc.team3668.robot.commands.CmdDriveStraightWithGyro;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class CmdGroupAutoRedLeftHopper extends CommandGroup {

    public CmdGroupAutoRedLeftHopper() { /*// !!!!!!!! THIS IS NOT FINISHED !!!!!!!! //*/ //needs to be peer edited lol i suck at programming soooo JRC2-8-17
    	//ask question about - or + degrees in settings step 1 and 2 heading degrees
    	
    	addSequential(new CmdDriveStraightWithGyro(Settings.autoLeftHopperStep1HeadingDegrees, Settings.autoLeftHopperInchesPerSecond, Settings.autoLeftHopperStep1Inches));
    	addSequential(new CmdBothTurnWithProfile(Settings.autoLeftHopperTurn1Degrees, Settings.autoLeftHopperInchesPerSecond));
    	addSequential(new CmdDriveStraightWithGyro(Settings.autoLeftHopperStep2HeadingDegrees, Settings.autoLeftHopperInchesPerSecond, Settings.autoLeftHopperStep2Inches));
    	addSequential(new CmdDriveStraightWithGyro(Settings.autoLeftHopperStep3HeadingDegrees, Settings.autoLeftHopperInchesPerSecond, Settings.autoLeftHopperStep3Inches));
    	addSequential(new CmdBothTurnWithProfile(Settings.autoHopperTurn2Degrees, Settings.autoLeftHopperInchesPerSecond));
    	addSequential(new CmdDriveStraightWithGyro(Settings.autoLeftHopperStep4HeadingDegrees, Settings.autoLeftHopperInchesPerSecond, Settings.autoLeftHopperStep4Inches));




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
