package org.usfirst.frc.team3668.robot.subsystems;

import org.usfirst.frc.team3668.robot.RobotMap;
import org.usfirst.frc.team3668.robot.commands.CmdTeleopJoystickDrive;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

public class SubChassis extends Subsystem {

	private Timer _gyroTimer = new Timer();

	public void initDefaultCommand() {
		setDefaultCommand(new CmdTeleopJoystickDrive());
	}

	public void Drive(Joystick stick) {
		RobotMap.chassisRobotDrive.arcadeDrive(stick);
	}

	public double getEncoderAvgDist() {
		return (RobotMap.chassisEncoderLeft.getDistance() + RobotMap.chassisEncoderRight.getDistance()) / 2;
	}

	public double getLeftEncoderDist() {
		return RobotMap.chassisEncoderLeft.getDistance();
	}

	public double getRightEncoderDist() {
		return RobotMap.chassisEncoderRight.getDistance();
	}

	public void resetBothEncoders() {
		RobotMap.chassisEncoderLeft.reset();
		RobotMap.chassisEncoderRight.reset();
	}

	public void resetLeftEncoder() {
		RobotMap.chassisEncoderLeft.reset();
	}

	public void resetRightEncoder() {
		RobotMap.chassisEncoderRight.reset();
	}

	public void initializeGyro() {
		RobotMap.chassisGyro.initGyro();
		RobotMap.chassisGyro.calibrate();
		RobotMap.chassisGyro.reset();
	}

	public void gyroTimerStart() {
		_gyroTimer.start();
	}

	public boolean gyroTimerElapsed(int seconds) {
		return _gyroTimer.hasPeriodPassed(seconds);
	}

	public void gyroTimerStop() {
		_gyroTimer.stop();
	}
	
	public void gyroTimerReset(){
		_gyroTimer.reset();
	}
	public double properModulusForGyroHeading(double angle){
		double i = angle / 360.0;
		double j = i % 1.0;
		if(Math.signum(j)==-1){
			j = j + 1;
		}	
		return j*360;
	}
	
	public double gyroGetHeading(){
		return properModulusForGyroHeading(RobotMap.chassisGyro.getAngle());
	}
	
	
	
	public double gyroCurrentDifferenceFromDesired(double desiredHeading){
		return properModulusForGyroHeading(gyroGetHeading() - desiredHeading);
	}
public void driveByGyro (){
		
	}
}
