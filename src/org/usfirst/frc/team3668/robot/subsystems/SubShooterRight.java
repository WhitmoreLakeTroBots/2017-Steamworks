package org.usfirst.frc.team3668.robot.subsystems;

import org.usfirst.frc.team3668.robot.RobotMap;
import org.usfirst.frc.team3668.robot.Settings;

import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class SubShooterRight extends PIDSubsystem {
	
	public SubShooterRight() {
		super("Shooter", Settings.shooterControlPIDkp, Settings.shooterControlPICki, Settings.shooterControlPIDkd, Settings.shooterControlPIDkf);
	}
	
    public void initDefaultCommand() {
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public double motorSpeedValue(double targetLinearSpeed){
    	return targetLinearSpeed/Settings.shooterMotorMaxSpeed;
    }
    public void run(double rightMotorValue) {
    	RobotMap.shooterMotorRight.set(-1 * rightMotorValue); // INVERT RIGHT MOTOR
    }
    
    public double shooterRightLinearSpeed(){
    	return RobotMap.shooterRightMotorEncoder.getRate();
    }
    
    public void resetShooterEncoder(){
    	RobotMap.shooterRightMotorEncoder.reset();
    }
    
    public void stop(){
    	RobotMap.shooterMotorRight.set(0);
    }
    
	@Override
	protected double returnPIDInput() {
		return RobotMap.shooterRightMotorEncoder.getRate();
	}
	
	@Override
	public void usePIDOutput(double output) {
		RobotMap.shooterMotorRight.pidWrite(output);
	}
}