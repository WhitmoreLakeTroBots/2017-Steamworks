package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.RobotMap;
import org.usfirst.frc.team3668.robot.Settings;
import org.usfirst.frc.team3668.robot.motionProfile.Logger;
import org.usfirst.frc.team3668.robot.motionProfile.MotionProfiler;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CmdBothDriveWithProfile extends Command {
	// DRIVE THE ROBOT WITH MOTION PROFILER
	double MAXSPEED = Settings.robotMaxInchesPerSecond;
	double _distance;
	double _cruiseSpeed;
	boolean _finished = false;
	double _accerlation = Settings.profileDriveAccelration; // inches/sec/sec
	double _startTime;
	MotionProfiler mp;
	Logger log = new Logger(Settings.profileLogName);

	public CmdBothDriveWithProfile(double distance, double cruiseSpeed) {
		// given distance (inches) and cruise speed (inches per second) drive
		// with the motion profiler
		_distance = distance;
		_cruiseSpeed = cruiseSpeed;
		requires(Robot.subChassis);
		// System.out.println("Projected Accelration Time:\t" + mp._accelTime +
		// "\tProjected Cruise Time:\t" + mp._cruiseTime
		// + "\tProjected Deccelration Time:\t" + mp._deccelTime + "\tProjected
		// Length of Drive:\t" + mp._stopTime);
	}

	public void initialize() {
		mp = new MotionProfiler(_distance, Settings.profileInitVelocity, _cruiseSpeed, _accerlation);
		Robot.subChassis.resetBothEncoders();
		_startTime = getTime();
	}

	public void execute() {
		// execute the drive
		double deltaTime = getTime() - _startTime;
		double profileVelocity = mp.getProfileCurrVelocity(deltaTime);
		double throttlePos = (profileVelocity / MAXSPEED) + Settings.profileRobotThrottleThreshold;
		double frictionThrottlePos = frictionThrottle(throttlePos, deltaTime);
		SmartDashboard.putNumber("Throttle Position: ", throttlePos);
		Robot.subChassis.Drive(0,frictionThrottlePos);
		log.makeEntry(String.format(
				"Current Velocity: %1$.3f \t throttle: %2$.3f \t deltaTime: %3$.3f \t Total Disantce Travelled: %4$.3f",
				profileVelocity, throttlePos, deltaTime, mp.getTotalDistanceTraveled(deltaTime)));
		System.out.println(String.format(
				"CurrVel: %1$.3f \t throttle: %2$.3f \t Friction throttle: %5$.3f \t deltaTime: %3$.3f \t Disantce Travelled: %4$.3f",
				profileVelocity, throttlePos, deltaTime, mp.getTotalDistanceTraveled(deltaTime),frictionThrottlePos)
				+ String.format("\t AvgEncoder: %1$.3f \t Left Encoder: %2$.3f \t Right Encoder: %3$.3f",
						Robot.subChassis.getEncoderAvgDistInch(), Robot.subChassis.getLeftEncoderDistInch(),
						Robot.subChassis.getRightEncoderDistInch()));
		if (deltaTime > mp._stopTime) {
			_finished = true;
			end();
		}
	}

	public double frictionThrottle(double throttle, double time) {
		double deltaDist = mp.getTotalDistanceTraveled(time) - Robot.subChassis.getEncoderAvgDistInch();
		double frictionThrottleComp = deltaDist * Settings.profileThrottleDistanceProportion;
		throttle = throttle + frictionThrottleComp;
		// System.out.println(throttle);
		return throttle;
	}

	public double getTime() {
		return (System.nanoTime() / Math.pow(10, 9));
	}

	public boolean isFinished() {
		// ARE WE DONE?
		return _finished;
	}

	public void end() {
		// STOP THINGS THAT NEED TO BE STOPPED
		_finished = true;
		// log.write();
		log = null;
		System.out.println("Accelration Time:\t" + mp._accelTime + "Cruise Time:\t" + mp._cruiseTime
				+ "Deccelration Time:\t" + mp._deccelTime + "Length of Drive:\t" + mp._stopTime);
	}
}
