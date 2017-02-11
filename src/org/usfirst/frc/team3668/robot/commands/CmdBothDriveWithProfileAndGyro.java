package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.RobotMath;
import org.usfirst.frc.team3668.robot.Settings;
import org.usfirst.frc.team3668.robot.motionProfile.Logger;
import org.usfirst.frc.team3668.robot.motionProfile.MotionProfiler;

import edu.wpi.first.wpilibj.command.Command;

public class CmdBothDriveWithProfileAndGyro extends Command {

	double MAXSPEED = Settings.robotMaxInchesPerSecond;
	double _distance;
	double _cruiseSpeed;
	boolean _isFinished = false;
	double _accerlation = Settings.profileDriveAccelration; // inches/sec/sec
	double _startTime;
	double _requestedHeading;
	double _distanceSignum;
	double _absDistance;
	MotionProfiler mp;
	Logger log = new Logger(Settings.profileLogName);

	public CmdBothDriveWithProfileAndGyro(double requestedHeading, double cruiseSpeed, double distance) {
		requires(Robot.subChassis);
		_distance = distance;
		_absDistance = Math.abs(distance);
		_distanceSignum = Math.signum(distance);
		_cruiseSpeed = cruiseSpeed;
		_requestedHeading = requestedHeading;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		mp = new MotionProfiler(_absDistance, Settings.profileInitVelocity, _cruiseSpeed, _accerlation);
		Robot.subChassis.resetBothEncoders();
		System.out.println(String.format(
				"Projected Accelration Time: %1$.3f \tProjected Cruise Time: %2$.3f \t Projected Deccelration Time: %3$.3f \t Projected Length of Drive: %4$.3f",
				mp._accelTime, mp._cruiseTime, mp._deccelTime, mp._stopTime));
		_startTime = RobotMath.getTime();
		_isFinished  = false;
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		double deltaTime = RobotMath.getTime() - _startTime;
		double currentHeading = Robot.subChassis.gyroGetRawHeading();
		double turnValue = RobotMath.headingDelta(currentHeading, _requestedHeading, Settings.chassisCmdDriveStraightWithGyroKp);
		double profileVelocity = mp.getProfileCurrVelocity(deltaTime);
		double throttlePos = (profileVelocity / MAXSPEED);
		if(deltaTime < mp._accelTime){
			throttlePos = throttlePos + Settings.profileRobotThrottleThreshold;
		}
		double frictionThrottlePos = RobotMath.frictionThrottle(throttlePos, deltaTime, mp);
		String msg = String.format(
				"CurrVel: %1$.3f \t throttle: %2$.3f \t Friction throttle: %3$.3f \t deltaTime: %4$.3f \t Disantce Travelled: %5$.3f \t AvgEncoder: %6$.3f \t Left Encoder: %7$.3f \t Right Encoder: %8$.3f \t Gyro Raw Heading: %9$.3f \t Gyro Delta: %10$.3f",
				profileVelocity, throttlePos, frictionThrottlePos, deltaTime, mp.getTotalDistanceTraveled(),
				Robot.subChassis.getEncoderAvgDistInch(), Robot.subChassis.getLeftEncoderDistInch(),
				Robot.subChassis.getRightEncoderDistInch(), currentHeading, turnValue);

		Robot.subChassis.Drive((frictionThrottlePos*_distanceSignum), turnValue);

		log.makeEntry(msg);
		System.out.println(msg);

	if (Math.abs(Robot.subChassis.getEncoderAvgDistInch()) > _absDistance) {
			_isFinished = true;
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return _isFinished;
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.subChassis.Drive(0, 0);
		Robot.subChassis.resetBothEncoders();
		//System.out.println(String.format(
		//		"Accelration Time: %1$.3f \t Cruise Time: %2$.3f \t Deccelration Time: %3$.3f \t Length of Drive: %4$.3f",
		//		mp._accelTime, mp._cruiseTime, mp._deccelTime, mp._stopTime));
		mp = null;
	    log.write();
		log = null;
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}