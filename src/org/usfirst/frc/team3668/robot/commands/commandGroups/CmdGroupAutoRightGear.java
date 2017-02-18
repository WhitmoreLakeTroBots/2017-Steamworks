package org.usfirst.frc.team3668.robot.commands.commandGroups;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.Settings;
import org.usfirst.frc.team3668.robot.Settings.colors;
import org.usfirst.frc.team3668.robot.commands.CmdAutoCenterPlaceGearWithVision;
import org.usfirst.frc.team3668.robot.commands.CmdBothDriveWithProfileAndGyro;
import org.usfirst.frc.team3668.robot.commands.CmdBothTurnWithProfile;
import org.usfirst.frc.team3668.robot.commands.CmdDriveStraightWithGyro;
import org.usfirst.frc.team3668.robot.commands.CmdTurnWithGyro;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CmdGroupAutoRightGear extends CommandGroup { //finished 

    public CmdGroupAutoRightGear(colors color) {
        requires(Robot.subChassis);
		requires(Robot.subShooter);
		requires(Robot.subFeeder);
		
    	CommandGroup TryNEW = new CommandGroup();
    	
    	TryNEW.addSequential(new CmdDriveStraightWithGyro(0, Settings.autoMoveInchesPerSecond, Settings.autoInchesToBaseline));
    	TryNEW.addSequential(new CmdBothTurnWithProfile(Settings.autoRightGearTurnDegrees, Settings.autoMoveInchesPerSecond));
    	TryNEW.addSequential(new CmdAutoCenterPlaceGearWithVision());
    	TryNEW.addSequential(new CmdDriveStraightWithGyro(Settings.autoRightGearStep2HeadingDegrees, Settings.autoMoveInchesPerSecond, Settings.autoInchesLift2Boiler));
//    	addSequential(new CmdBothShooter());

    	
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
