package org.usfirst.frc.team3668.robot.subsystems;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.RobotMap;
import org.usfirst.frc.team3668.robot.RobotMath;
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
		double joyX = stick.getX();
		double joyY = stick.getY();
		if(Robot.isDriveInverted){
			RobotMap.chassisRobotDrive.arcadeDrive(-joyY, joyX, true);
		} if(!Robot.isDriveInverted) {
		RobotMap.chassisRobotDrive.arcadeDrive(joyY,joyX, true);
		}
	}
	
	public void Drive(double move, double rotate){
		RobotMap.chassisRobotDrive.arcadeDrive(move, rotate);
	}

	public double getEncoderAvgDistInch() {
		return (RobotMap.chassisEncoderLeft.getDistance() + RobotMap.chassisEncoderRight.getDistance()) / 2;
	}
	
	public double getABSEncoderAvgDistInch(){
		return (Math.abs(RobotMap.chassisEncoderLeft.getDistance()) + Math.abs(RobotMap.chassisEncoderRight.getDistance()))/2;
	}

	public double getLeftEncoderDistInch() {
		return RobotMap.chassisEncoderLeft.getDistance();
	}

	public double getRightEncoderDistInch() {
		return RobotMap.chassisEncoderRight.getDistance();
	}
	
	public double getEncoderAvgRate(){
		return (RobotMap.chassisEncoderLeft.getRate() + RobotMap.chassisEncoderRight.getRate())/2;
	}
	
	public double getLeftEncoderRate(){
		return RobotMap.chassisEncoderLeft.getRate();
	}
	
	public double getRightEncoderRate(){
		return RobotMap.chassisEncoderRight.getRate();
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
	public void resetGyro(){
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
	
	public double gyroGetRawHeading(){
		return RobotMath.normalizeAngles(RobotMap.chassisGyro.getAngle());
	}
	public double gyroGetUnnormalizedHeading(){
		return RobotMap.chassisGyro.getAngle();
	}
}
