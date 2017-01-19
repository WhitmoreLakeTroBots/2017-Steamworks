package org.usfirst.frc.team3668.robot.subsystems;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.RobotMap;
import org.usfirst.frc.team3668.robot.Settings;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class SubFeeder extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    public void run(double speed) {
    	if(Robot.subShooter.readyToShoot()){
    		RobotMap.feederMotor.set(speed);
    	} else {
    		RobotMap.feederMotor.set(0);
    	}
    }
}

