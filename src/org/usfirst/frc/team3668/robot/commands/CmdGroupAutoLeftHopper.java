package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Settings;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class CmdGroupAutoLeftHopper extends CommandGroup {

    public CmdGroupAutoLeftHopper() { /*// !!!!!!!! THIS IS NOT FINISHED !!!!!!!! //*/
    	
    	addSequential(new CmdDriveStraightWithGyro(Settings.autoLeftHopperStep1HeadingDegrees, Settings.autoLeftHopperInchesPerSecond, Settings.autoLeftHopperStep1Inches));
    	addSequential(new CmdBothTurnWithProfile(Settings.autoLeftHopperTurnDegrees, Settings.autoLeftHopperInchesPerSecond, Settings.TurnType.pointL ));

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
