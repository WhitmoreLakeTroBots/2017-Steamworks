package org.usfirst.frc.team3668.robot.subsystems;

import org.usfirst.frc.team3668.robot.RobotMap;
import org.usfirst.frc.team3668.robot.commands.CmdTeleopJoystickDrive;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Subsystem;


public class SubChassis extends Subsystem {

    public void initDefaultCommand() {
        setDefaultCommand(new CmdTeleopJoystickDrive());
    }
    
    public void Drive(Joystick stick) {
    	double JoyX = stick.getX();
    	double JoyY = stick.getY();
    	double Xsign = Math.signum(JoyX);
    	double Ysign = Math.signum(JoyY);
    	double NewX = JoyX * JoyX * Xsign;
    	double NewY = JoyY * JoyY * Ysign;
    	RobotMap.chassisRobotDrive.arcadeDrive(NewY, NewX);
    }
    
    public double getEncoderAvgDist(){
    	return (RobotMap.chassisEncoderLeft.getDistance() + RobotMap.chassisEncoderRight.getDistance())/2;
    }
    
    public double getLeftEncoderDist(){
    	return RobotMap.chassisEncoderLeft.getDistance();
    }
    
    public double getRightEncoderDist(){
    	return RobotMap.chassisEncoderRight.getDistance();
    }
    
    public void resetBothEncoders(){
    	RobotMap.chassisEncoderLeft.reset();
    	RobotMap.chassisEncoderRight.reset();
    }
    
    public void resetLeftEncoder(){
    	RobotMap.chassisEncoderLeft.reset();
    }
    
    public void resetRightEncoder(){
    	RobotMap.chassisEncoderRight.reset();
    }
}

