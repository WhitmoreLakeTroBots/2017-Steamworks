package org.usfirst.frc.team3668.robot.subsystems;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.RobotMap;
import org.usfirst.frc.team3668.robot.RobotMath;
import org.usfirst.frc.team3668.robot.Settings;
import org.usfirst.frc.team3668.robot.commands.CmdTeleopJoystickDrive;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

public class SubChassis extends Subsystem {

	private Timer _gyroTimer = new Timer();
	public boolean _isSafe2Move = true;
	
	public void initDefaultCommand() {
		setDefaultCommand(new CmdTeleopJoystickDrive());
	}

	public void Drive(Joystick stick) {
		double joyX = stick.getX();
		double joyY = stick.getY();
		if(Robot.isDriveInverted){
			RobotMap.chassisRobotDrive.arcadeDrive(-joyY, -joyX, true);
		} if(!Robot.isDriveInverted) {
		RobotMap.chassisRobotDrive.arcadeDrive(joyY,-joyX, true);
		}
	}
	
	public void Drive(double move, double rotate){
		if(_isSafe2Move){
//		move = limit(move);
//		rotate = limit(rotate);
//		double leftMotorSpeed;
//		double rightMotorSpeed;
//			if (move > 0.0) {
//			      if (rotate > 0.0) {
//			        leftMotorSpeed = move - rotate;
//			        rightMotorSpeed = Math.max(move, rotate);
//			      } else {
//			        leftMotorSpeed = Math.max(move, -rotate);
//			        rightMotorSpeed = move + rotate;
//			      }
//		    } else {
//			      if (rotate > 0.0) {
//			        leftMotorSpeed = -Math.max(-move, rotate);
//			        rightMotorSpeed = move + rotate;
//			      } else {
//			        leftMotorSpeed = move - rotate;
//			        rightMotorSpeed = -Math.max(-move, -rotate);
//			      }
//			}
//		RobotMap.chassisMotorLeft1.set(leftMotorSpeed);
//		RobotMap.chassisMotorLeft2.set(leftMotorSpeed);
//		RobotMap.chassisMotorRight1.set(rightMotorSpeed);
//		RobotMap.chassisMotorRight2.set(rightMotorSpeed);
		RobotMap.chassisRobotDrive.arcadeDrive(move, rotate);
		}
	}

	  protected static double limit(double num) {
		    if (num > 1.0) {
		      return 1.0;
		    }
		    if (num < -1.0) {
		      return -1.0;
		    }
		    return num;
		  }

	
	public double getEncoderAvgDistInch() {
		double retVal = 0;
		double leftDistance = RobotMap.chassisEncoderLeft.getDistance();
		double rightDistance = RobotMap.chassisEncoderRight.getDistance();
		if(leftDistance < Settings.chassisEncoderDeadValueThreshold){
			retVal = rightDistance;
		} else if (rightDistance < Settings.chassisEncoderDeadValueThreshold){
			retVal = leftDistance;
		} else {
			retVal = (leftDistance + rightDistance) / 2;
		}
		return retVal;
	}
	
	public double getABSEncoderAvgDistInch(){
		double retVal = 0;
		double leftDistance = Math.abs(RobotMap.chassisEncoderLeft.getDistance());
		double rightDistance = Math.abs(RobotMap.chassisEncoderRight.getDistance());
		if(leftDistance < Settings.chassisEncoderDeadValueThreshold){
			retVal = rightDistance;
		} else if (rightDistance < Settings.chassisEncoderDeadValueThreshold){
			retVal = leftDistance;
		} else {
			retVal = (leftDistance + rightDistance) / 2;
		}
		return retVal;
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