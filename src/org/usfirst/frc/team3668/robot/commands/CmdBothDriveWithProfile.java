package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.motionProfile.Logger;
import org.usfirst.frc.team3668.robot.motionProfile.MotionProfiler;
import org.usfirst.frc.team3668.robot.motionProfile.ProfileSettings;

import edu.wpi.first.wpilibj.command.Command;

public class CmdBothDriveWithProfile extends Command {
	// DRIVE THE ROBOT WITH MOTION PROFILER
	double MAXSPEED = ProfileSettings.MAXSPEED;
	double _distance;
	double _cruiseSpeed;
	boolean _finished = false;
	double _accerlation = ProfileSettings.driveAccelration; //inches/sec/sec
	double _startTime;
	MotionProfiler mp;
	Logger log = new Logger(ProfileSettings.motionProfileLogName);
	
	
	public CmdBothDriveWithProfile(double distance, double cruiseSpeed) {
		// given distance (inches) and cruise speed (inches per second) drive
		// with the motion profiler
		_distance = distance;
		_cruiseSpeed = cruiseSpeed;
		//System.out.println("Projected Accelration Time:\t" + mp._accelTime + "\tProjected Cruise Time:\t" + mp._cruiseTime
		//		+ "\tProjected Deccelration Time:\t" + mp._deccelTime + "\tProjected Length of Drive:\t" + mp._stopTime);
	}
	
	public void initialize(){
		mp = new MotionProfiler(_distance, ProfileSettings.initVelocity, _cruiseSpeed, _accerlation);
		_startTime = getTime();
	}

	public void execute() {
		// execute the drive
		String msg;
		double deltaTime = getTime() - _startTime;
		double profileVelocity = mp.getProfileCurrVelocity(deltaTime);
		double throttlePos = profileVelocity / MAXSPEED;
		msg = "throttle-pos = " + throttlePos;
		
		log.makeEntry("Current Velocity: " + profileVelocity + "\t" + msg + "\t deltaTime: " + deltaTime + "\t Total Disantce Travelled: "+mp.getTotalDistanceTraveled());
		if (deltaTime > mp._stopTime) {
			_finished = true;
			end();
		}
	}
	
	public double getTime(){
		return (System.nanoTime() / Math.pow(10,9));
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
