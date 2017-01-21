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
	private double _targetLinearSpeed;
	private double _shooterSpeedFactored;
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    public void setTargetLinearSpeed(double targetLinearSpeed){
    	_targetLinearSpeed = targetLinearSpeed;
    	_shooterSpeedFactored = _targetLinearSpeed * Settings.shooterMotorSpeedMarginOfError;
    }
    public void run() {
    	double shooterLeftLinearSpeed = shooterLeftLinearSpeed();
    	double shooterRightLinearSpeed = shooterRightLinearSpeed();
    	if(shooterLeftLinearSpeed < _shooterSpeedFactored){
    		RobotMap.shooterMotorLeft.set(Settings.shooterMotorMaxVoltagePercent);	
    	} else if(shooterLeftLinearSpeed >= _shooterSpeedFactored && shooterLeftLinearSpeed <= _targetLinearSpeed){
    		RobotMap.shooterMotorLeft.set(Settings.shooterMotorReducedVoltagePercent);
    	}else{
    		RobotMap.shooterMotorLeft.set(0);
    	}
    	if(shooterRightLinearSpeed < _shooterSpeedFactored){
    		RobotMap.shooterMotorRight.set(Settings.shooterMotorMaxVoltagePercent);	
    	} else if(shooterRightLinearSpeed >= _shooterSpeedFactored && shooterRightLinearSpeed <= _targetLinearSpeed){
    		RobotMap.shooterMotorRight.set(Settings.shooterMotorReducedVoltagePercent);
    	}else{
    		RobotMap.shooterMotorRight.set(0);
    	}
    }
    public double shooterLeftLinearSpeed(){
    	return RobotMap.shooterLeftMotorEncoder.getRate();
    }
    public double shooterRightLinearSpeed(){
    	return RobotMap.shooterRightMotorEncoder.getRate();
    }
    public boolean readyToShoot(){
    	return(shooterLeftLinearSpeed() > _shooterSpeedFactored && shooterRightLinearSpeed() > _shooterSpeedFactored); 
    }
    public void stop(){
    	RobotMap.shooterMotorLeft.set(0);
    	RobotMap.shooterMotorRight.set(0);
    }
}

