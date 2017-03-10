package org.usfirst.frc.team3668.robot.subsystems;

import org.usfirst.frc.team3668.robot.RobotMap;
import org.usfirst.frc.team3668.robot.Settings;

import edu.wpi.first.wpilibj.command.PIDSubsystem;

/**
 *
 */
public class SubShooterLeft extends PIDSubsystem {

    // Initialize your subsystem here
    public SubShooterLeft() {
    	super(Settings.shooterControlPIDkp, Settings.shooterControlPICki, Settings.shooterControlPIDkd);
        // Use these to get going:
        // setSetpoint() -  Sets where the PID controller should move the system
        //                  to
        // enable() - Enables the PID controller.
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
	@Override
	protected double returnPIDInput() {
		return RobotMap.shooterLeftMotorEncoder.getRate();
	}
	
	@Override
	public void usePIDOutput(double output) {
		RobotMap.shooterMotorLeft.pidWrite(output);
	}
	
    public double shooterLeftLinearSpeed(){
    	return RobotMap.shooterLeftMotorEncoder.getRate();
    }
	
    public double motorSpeedValue(double targetLinearSpeed){
    	return targetLinearSpeed/Settings.shooterMotorMaxSpeed;
    }
    public void run(double leftMotorValue) {
    	RobotMap.shooterMotorLeft.set(leftMotorValue);
    }
    
    public void resetShooterEncoder(){
    	RobotMap.shooterLeftMotorEncoder.reset();
    }
    
    public void stop(){
    	RobotMap.shooterMotorLeft.set(0);
    }
}