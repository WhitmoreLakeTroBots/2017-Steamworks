package org.usfirst.frc.team3668.robot.subsystems;

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
		RobotMap.chassisRobotDrive.arcadeDrive(stick, true);
	}

	public double getEncoderAvgDistInch() {
		return (RobotMap.chassisEncoderLeft.getDistance() + RobotMap.chassisEncoderRight.getDistance()) / 2;
	}

	public double getLeftEncoderDistInch() {
		return RobotMap.chassisEncoderLeft.getDistance();
	}

	public double getRightEncoderDistInch() {
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
	
	public double gyroGetRawHeading(){
		return RobotMath.normalizeAngles(RobotMath.properModulus(RobotMap.chassisGyro.getAngle(), 1));
	}
	
	public void Drive(double move, double rotate){
		RobotMap.chassisRobotDrive.arcadeDrive(move, rotate);
	}
}
