package org.usfirst.frc.team3668.robot.subsystems;

import org.usfirst.frc.team3668.robot.RobotMap;
import org.usfirst.frc.team3668.robot.commands.CmdTeleopJoystickDrive;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class SubChassis extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new CmdTeleopJoystickDrive());
    }
    
    public void Drive(Joystick stick) {
    	
    	RobotMap.chassisRobotDrive.arcadeDrive(stick);
    	
    }
}

