package org.usfirst.frc.team3668.robot.subsystems;

import org.usfirst.frc.team3668.robot.RobotMap;
import org.usfirst.frc.team3668.robot.Settings;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class SubShooter extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void run(double rimSpeed ) {
    	RobotMap.shooterMotorLeft.set(rimSpeed);
    	RobotMap.shooterMotorRight.set(rimSpeed);

    }
}

