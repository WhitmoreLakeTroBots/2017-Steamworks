package org.usfirst.frc.team3668.robot.subsystems;

import org.usfirst.frc.team3668.robot.RobotMap;
import org.usfirst.frc.team3668.robot.Settings;

import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class SubShooter extends PIDSubsystem {
	
	public SubShooter(double p, double i, double d, double f) {
		super("shooter",p, i, d, f);
	}
	
    public void initDefaultCommand() {
        //setDefaultCommand(new MySpecialCommand());
    }
    public double motorSpeedValue(double targetLinearSpeed){
    	return targetLinearSpeed/Settings.shooterMotorMaxSpeed;
    }
    public void run(double leftMotorValue, double rightMotorValue) {
    	RobotMap.shooterMotorLeft.set(1 * leftMotorValue);
    	RobotMap.shooterMotorRight.set(-1 * rightMotorValue); // INVERT RIGHT MOTOR
    }
    public double shooterLeftLinearSpeed(){
    	return RobotMap.shooterLeftMotorEncoder.getRate();
    }
    public double shooterRightLinearSpeed(){
    	return RobotMap.shooterRightMotorEncoder.getRate();
    }
    
    public void resetShooterEncoders(){
    	RobotMap.shooterLeftMotorEncoder.reset();
    	RobotMap.shooterRightMotorEncoder.reset();
    }
    
    public void stop(){
    	RobotMap.shooterMotorLeft.set(0);
    	RobotMap.shooterMotorRight.set(0);
    }
    
	@Override
	protected double returnPIDInput() {
		return ((shooterLeftLinearSpeed() + shooterRightLinearSpeed())/2);
	}
	
	@Override
	public void usePIDOutput(double output) {
		RobotMap.shooterMotorLeft.pidWrite(output);
		RobotMap.shooterMotorRight.pidWrite(output);
	}
}