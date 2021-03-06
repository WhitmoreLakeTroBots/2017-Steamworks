package org.usfirst.frc.team3668.robot.commands.commandGroups;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.Settings;
import org.usfirst.frc.team3668.robot.Settings.colors;
import org.usfirst.frc.team3668.robot.commands.CmdAutoShooter;
import org.usfirst.frc.team3668.robot.commands.CmdBothDriveWithProfileAndGyro;
import org.usfirst.frc.team3668.robot.commands.CmdTurnWithGyro;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class CmdGroupAutoShootFromKey extends CommandGroup {

    public CmdGroupAutoShootFromKey(colors selectedColor) {
    	requires(Robot.subChassis);
    	requires(Robot.subShooter);
  
    	double keyShootHeading;
    	
    	if(selectedColor == colors.Red){
    		keyShootHeading = Settings.autoShootHeadingFromKey * -1;
    	} else {
    		keyShootHeading = Settings.autoShootHeadingFromKey;
    	}
    	
    	//System.out.println("Called CmdGroup");
    	CommandGroup TryNEW = new CommandGroup();
    	TryNEW.addSequential(new CmdBothDriveWithProfileAndGyro(0,Settings.autoMoveInchesPerSecond, Settings.autoKeyLineDistance2Shoot));
    	TryNEW.addSequential(new CmdTurnWithGyro(/*keyShootHeading*/180));
    	TryNEW.addSequential(new CmdBothDriveWithProfileAndGyro(180,Settings.autoMoveInchesPerSecond, (Settings.autoKeyLineDistance2Shoot)));
    	TryNEW.addSequential(new CmdTurnWithGyro(/*keyShootHeading*/0));
//    	TryNEW.addSequential(new CmdAutoShooter(Settings.shooterTargetLinearVelocity));
        
    	addSequential(TryNEW);
    	
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
