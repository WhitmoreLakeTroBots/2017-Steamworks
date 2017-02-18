package org.usfirst.frc.team3668.robot.subsystems;

import org.usfirst.frc.team3668.robot.RobotMap;
import org.usfirst.frc.team3668.robot.Settings;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class SubSweeper extends Subsystem {
	
	public void sweep() {
		
	RobotMap.sweeperMotor.set(Settings.sweeperMotorSpeed);

	}

public void reverseSweep() {
	
	RobotMap.sweeperMotor.set(Settings.sweeperMotorReverseSpeed);

	
}
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

	public void stopSweep() {
	
	RobotMap.sweeperMotor.set(0);
	}

	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

