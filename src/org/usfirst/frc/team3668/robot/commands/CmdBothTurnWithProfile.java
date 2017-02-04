package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.RobotMath;
import org.usfirst.frc.team3668.robot.Settings;
import org.usfirst.frc.team3668.robot.motionProfile.Logger;
import org.usfirst.frc.team3668.robot.motionProfile.MotionProfiler;

import edu.wpi.first.wpilibj.command.Command;

public class CmdBothTurnWithProfile extends Command{
	double MAXSPEED = Settings.robotMaxInchesPerSecond;
	double _distance;
	double _cruiseSpeed;
	boolean _isFinished = false;
	double _acceleration = Settings.profileDriveAccelration; //inches/sec/sec
	double _startTime;
	double _requestedHeading;
	double _currentHeading;
	double _startHeading;
	double _deltaDegrees;
	double _deltaDegreesSignum;
	MotionProfiler mp;
	Logger log = new Logger(Settings.profileLogName);
	
	public CmdBothTurnWithProfile(double degrees, double cruiseSpeed) {
		// given distance (inches) and cruise speed (inches per second) turn
		// with motion profiler
		_requestedHeading = degrees;
		_cruiseSpeed = cruiseSpeed;
	}

	protected void initialize() {
		Robot.subChassis.resetBothEncoders();
		_startTime = RobotMath.getTime();
		_startHeading = Robot.subChassis.gyroGetRawHeading();
		calcDeltaDegrees();
		_distance = calcTurnDist();
		mp = new MotionProfiler(Math.abs(_distance), Settings.profileInitVelocity, _cruiseSpeed, _acceleration);
		System.out.println(String.format(
				"Projected Accelration Time: %1$.3f \tProjected Cruise Time: %2$.3f \t Projected Deccelration Time: %3$.3f \t Projected Length of Drive: %4$.3f",
				mp._accelTime, mp._cruiseTime, mp._deccelTime, mp._stopTime));
		System.out.println(String.format("Total distance to travel: %1$.3f \t Delta Degrees: %2$.3f", _distance, _deltaDegrees));
	}
	
	public void execute() {
		// execute the drive
		double deltaTime = RobotMath.getTime() - _startTime;
		double profileVelocity = mp.getProfileCurrVelocity(deltaTime);
		_currentHeading = Robot.subChassis.gyroGetRawHeading();
		double throttlePos = (profileVelocity / MAXSPEED);
		if(deltaTime < mp._accelTime){
			throttlePos = throttlePos + Settings.profileRobotThrottleThreshold;
		}
		double frictionThrottlePos = RobotMath.frictionThrottle(throttlePos, deltaTime, mp);
		
		Robot.subChassis.Drive(0, (frictionThrottlePos * _deltaDegreesSignum));
		
		String msg = String.format(
				"CurrVel: %1$.3f \t throttle: %2$.3f \t Friction throttle: %3$.3f \t deltaTime: %4$.3f \t Disantce Travelled: %5$.3f \t ABSAvgEncoder: %6$.3f \t Left Encoder: %7$.3f \t Right Encoder: %8$.3f \t Gyro Raw Heading: %9$.3f",
				profileVelocity, throttlePos, frictionThrottlePos, deltaTime, mp.getTotalDistanceTraveled(),
				Robot.subChassis.getABSEncoderAvgDistInch(), Robot.subChassis.getLeftEncoderDistInch(),
				Robot.subChassis.getRightEncoderDistInch(), _currentHeading);
		System.out.println(msg);
		//log.makeEntry(msg);
		
		if (Robot.subChassis.getABSEncoderAvgDistInch() >= _distance) {
			_isFinished = true;
		}
	}
	
	
	protected void calcDeltaDegrees(){
		_deltaDegrees = RobotMath.normalizeAngles(_startHeading + _requestedHeading);
		_deltaDegreesSignum = Math.signum(_deltaDegrees);
	}
	
	protected double calcTurnDist(){
		return (Settings.profileTestRobotCirDia) * Math.PI * (_deltaDegrees / 360);
	}
	
	
	public boolean isFinished(){
		//ARE WE DONE?
		return _isFinished;
	}
	
	public void end(){
		//STOP THINGS THAT NEED TO BE STOPPED
		_isFinished = true;
		//log.write();
		log = null;
		System.out.println("Accelration Time:\t" + mp._accelTime + "Cruise Time:\t" + mp._cruiseTime
				+ "Deccelration Time:\t" + mp._deccelTime + "Length of Drive:\t" + mp._stopTime);
		Robot.subChassis.resetBothEncoders();
	}
	
	protected void interrupted() {
		end();
	}
}
