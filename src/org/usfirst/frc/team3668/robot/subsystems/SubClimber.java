package org.usfirst.frc.team3668.robot.subsystems;

import org.usfirst.frc.team3668.robot.RobotMap;
import org.usfirst.frc.team3668.robot.Settings;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class SubClimber extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

	public void climbFixedSpeed() {
		RobotMap.climberMotor1.set(Settings.climberMotorSpeed);
		RobotMap.climberMotor2.set(Settings.climberMotorSpeed);
	}
	
	public void joyClimb(Joystick joy){
		RobotMap.climberMotor1.set(Math.abs(joy.getY()));
		RobotMap.climberMotor2.set(Math.abs(joy.getY()));
	}
	
public void StopClimb() {
		
		RobotMap.climberMotor1.set(0);
		RobotMap.climberMotor2.set(0);

	}

	
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

