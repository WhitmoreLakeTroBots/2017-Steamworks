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
	MotionProfiler mp;
	Logger log = new Logger(Settings.profileLogName);

	public CmdBothDriveWithProfileAndGyro(double requestedHeading, double cruiseSpeed, double distance) {
		requires(Robot.subChassis);
		_distance = distance;
		_cruiseSpeed = cruiseSpeed;
		_requestedHeading = requestedHeading;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		mp = new MotionProfiler(_distance, Settings.profileInitVelocity, _cruiseSpeed, _accerlation);
		Robot.subChassis.resetBothEncoders();
		System.out.println(String.format(
				"Projected Accelration Time: %1$.3f \tProjected Cruise Time: %2$.3f \t Projected Deccelration Time: %3$.3f \t Projected Length of Drive: %4$.3f",
				mp._accelTime, mp._cruiseTime, mp._deccelTime, mp._stopTime));
		_startTime = getTime();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		double deltaTime = getTime() - _startTime;
		double currentHeading = Robot.subChassis.gyroGetRawHeading();
		double turnValue = RobotMath.headingDelta(currentHeading, _requestedHeading);
		double profileVelocity = mp.getProfileCurrVelocity(deltaTime);
		double throttlePos = (profileVelocity / MAXSPEED) + Settings.profileRobotThrottleThreshold;
		double frictionThrottlePos = frictionThrottle(throttlePos);
		String msg = String.format(
				"CurrVel: %1$.3f \t throttle: %2$.3f \t Friction throttle: %3$.3f \t deltaTime: %4$.3f \t Disantce Travelled: %5$.3f \t AvgEncoder: %6$.3f \t Left Encoder: %7$.3f \t Right Encoder: %8$.3f \t Gyro Raw Heading: %9$.3f \t Gyro Delta: %10$.3f",
				profileVelocity, throttlePos, frictionThrottlePos, deltaTime, mp.getTotalDistanceTraveled(),
				Robot.subChassis.getEncoderAvgDistInch(), Robot.subChassis.getLeftEncoderDistInch(),
				Robot.subChassis.getRightEncoderDistInch(), currentHeading, turnValue);

		Robot.subChassis.Drive(frictionThrottlePos, turnValue);

		// log.makeEntry(msg);
		System.out.println(msg);

	if (Robot.subChassis.getEncoderAvgDistInch() > _distance) {
			_isFinished = true;
			end();
		}
	}

	public double frictionThrottle(double throttle) {
		double deltaDist = mp.getTotalDistanceTraveled() - Robot.subChassis.getEncoderAvgDistInch();
		double frictionThrottleComp = deltaDist * Settings.profileThrottleProportion;
		throttle = throttle + frictionThrottleComp;
		return throttle;
	}

	public double getTime() {
		return (System.nanoTime() / Math.pow(10, 9));
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
		// log.write();
		log = null;
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}