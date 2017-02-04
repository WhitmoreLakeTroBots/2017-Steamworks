package org.usfirst.frc.team3668.robot.subsystems;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.RobotMap;
import org.usfirst.frc.team3668.robot.Settings;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class SubFeeder extends Subsystem {

	public void feed(){
		RobotMap.feederMotor.set(Settings.feederMotorSpeed);
	}
	
	
	public void stopFeed(){
		RobotMap.feederMotor.set(0);
	}
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    public void run(double speed) {
    		RobotMap.feederMotor.set(speed);
    }
}

