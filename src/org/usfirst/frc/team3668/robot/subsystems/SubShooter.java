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
    public double motorSpeedValue(double targetLinearSpeed){
    	return targetLinearSpeed/Settings.shooterMotorMaxSpeed;
    }
    public void run(double leftMotorValue, double rightMotorValue) {
    	RobotMap.shooterMotorLeft.set(leftMotorValue);
    	RobotMap.shooterMotorRight.set(-1 * rightMotorValue); // INVERT RIGHT MOTOR
    }
    public double shooterLeftLinearSpeed(){
    	return RobotMap.shooterLeftMotorEncoder.getRate();
    }
    public double shooterRightLinearSpeed(){
    	return RobotMap.shooterRightMotorEncoder.getRate();
    }
    public void stop(){
    	RobotMap.shooterMotorLeft.set(0);
    	RobotMap.shooterMotorRight.set(0);
    }
}

