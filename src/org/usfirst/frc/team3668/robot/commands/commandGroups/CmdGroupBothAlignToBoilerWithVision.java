package org.usfirst.frc.team3668.robot.commands.commandGroups;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.commands.CmdBothDriveWithProfile;
import org.usfirst.frc.team3668.robot.commands.CmdBothTurnWithProfile;
import org.usfirst.frc.team3668.robot.visionProcessing.BoilerVisionProcessing;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class CmdGroupBothAlignToBoilerWithVision extends CommandGroup {
	private static double _distanceToDrive;
	private static double _angleToTurn;
	
	public CmdGroupBothAlignToBoilerWithVision() {
        requires(Robot.subChassis);    
        synchronized(BoilerVisionProcessing.lockObject){
        _distanceToDrive = BoilerVisionProcessing._boilerCalculatedDistanceFromTarget;
        _angleToTurn = BoilerVisionProcessing._boilerCalculatedAngleFromMidpoint;
        }
//        addSequential(new CmdBothTurnWithProfile(_angleToTurn, 0.8 * Math.signum(_angleToTurn)));
        addSequential(new CmdBothDriveWithProfile(_distanceToDrive, 0.8));
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