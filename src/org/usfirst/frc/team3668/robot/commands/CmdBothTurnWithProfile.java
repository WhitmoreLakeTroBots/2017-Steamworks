package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.RobotMath;
import org.usfirst.frc.team3668.robot.Settings;
import org.usfirst.frc.team3668.robot.Settings.TurnType;
import org.usfirst.frc.team3668.robot.motionProfile.Logger;
import org.usfirst.frc.team3668.robot.motionProfile.MotionProfiler;

import edu.wpi.first.wpilibj.command.Command;

public class CmdBothTurnWithProfile extends Command{
	double MAXSPEED = Settings.robotMaxInchesPerSecond;
	double _distance;
	double _cruiseSpeed;
	boolean _finished = false;
	double _accerlation = Settings.profileDriveAccelration; //inches/sec/sec
	double _startTime;
	double rightMotorScalar;
	double leftMotorScalar;
	double _degrees;
	MotionProfiler mp;
	Logger log = new Logger(Settings.profileLogName);
	TurnType turn;
	
	public CmdBothTurnWithProfile(double degrees, double cruiseSpeed, TurnType type) {
		// given distance (inches) and cruise speed (inches per second) turn
		// with motion profiler
		_degrees = degrees;
		_distance = calcTurnDist(type);
		_cruiseSpeed = cruiseSpeed;
		calcTurnScalar(type);
		System.out.println("Projected Accelration Time:\t" + mp._accelTime + "\tProjected Cruise Time:\t" + mp._cruiseTime
				+ "\tProjected Deccelration Time:\t" + mp._deccelTime + "\tProjected Length of Drive:\t" + mp._stopTime);
	}

	protected void initialize() {
		mp = new MotionProfiler(_distance, Settings.profileInitVelocity, _cruiseSpeed, _accerlation);
		_startTime = RobotMath.getTime();
		System.out.println("Total distance to travel: " + _distance);

	}
	
	public void execute() {
		// execute the drive
		String msg;
		double deltaTime = RobotMath.getTime() - _startTime;
		double profileVelocity = mp.getProfileCurrVelocity(deltaTime);
		double throttlePos = (profileVelocity / MAXSPEED);
		if(deltaTime < mp._accelTime){
			throttlePos = throttlePos + Settings.profileRobotThrottleThreshold;
		}
		double frictionThrottlePos = RobotMath.frictionThrottle(throttlePos, deltaTime, mp);
		Robot.subChassis.DriveDirect((frictionThrottlePos * rightMotorScalar), (frictionThrottlePos * leftMotorScalar));
		msg = "Right-throttle-pos = " + rightMotorScalar*(profileVelocity / MAXSPEED) + " Left-throttle-pos = " + leftMotorScalar*(profileVelocity / MAXSPEED);
		//System.out.println(msg);
		if (deltaTime > mp._stopTime) {
			_finished = true;
			end();
		}
	}
	
	public double calcTurnDist(TurnType type){
		double retVal = 0;
		if(type.compareTo(Settings.TurnType.pointL) == 0 || type.compareTo(Settings.TurnType.pointR) == 0){
			retVal = (Settings.profileTestRobotCirDia/2) * Math.PI * (_degrees / 360);
		}
		if(type.compareTo(Settings.TurnType.SwingL) == 0 || type.compareTo(Settings.TurnType.SwingR) == 0){
			retVal = Settings.profileTestRobotCirDia * Math.PI * (_degrees / 360);
		}
		return retVal;
	}
	
	public void calcTurnScalar(TurnType type){
		if(type.compareTo(Settings.TurnType.pointR) == 0){
			rightMotorScalar = -1;
			leftMotorScalar = 1;	
		}
		if(type.compareTo(Settings.TurnType.pointL) == 0){
			rightMotorScalar = 1;
			leftMotorScalar = -1;
		}
		if(type.compareTo(Settings.TurnType.SwingR) == 0){
			rightMotorScalar = 0;
			leftMotorScalar = 1;
		}
		if(type.compareTo(Settings.TurnType.SwingL) == 0){
			rightMotorScalar = 1;
			leftMotorScalar = 0;
			
		}
	}
	
	public boolean isFinished(){
		//ARE WE DONE?
		return _finished;
	}
	
	public void end(){
		//STOP THINGS THAT NEED TO BE STOPPED
		_finished = true;
		log.write();
		log = null;
		System.out.println("Accelration Time:\t" + mp._accelTime + "Cruise Time:\t" + mp._cruiseTime
				+ "Deccelration Time:\t" + mp._deccelTime + "Length of Drive:\t" + mp._stopTime);
	}
}
