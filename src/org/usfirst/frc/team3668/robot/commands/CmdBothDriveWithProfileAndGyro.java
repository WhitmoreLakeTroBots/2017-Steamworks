package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.RobotMath;
import org.usfirst.frc.team3668.robot.Settings;
import org.usfirst.frc.team3668.robot.motionProfile.Logger;
import org.usfirst.frc.team3668.robot.motionProfile.MotionProfiler;
import org.usfirst.frc.team3668.robot.PID;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CmdBothDriveWithProfileAndGyro extends Command {

	double MAXSPEED = Settings.robotMaxInchesPerSecond;
	double _distance;
	double _cruiseSpeed;
	boolean _isFinished = false;
	double _accerlation = Settings.profileDriveAccelration; // inches/sec/sec
	double _startTime;
	double _requestedHeading = 0;
	double _distanceSignum;
	double _absDistance;
	double _abortTime;
	boolean _isRunaway;
	MotionProfiler mp;
	Logger log = new Logger(Settings.profileLogName);
	PID pid = new PID(Settings.profileKp, Settings.profileKi, Settings.profileKd);

	public CmdBothDriveWithProfileAndGyro(double requestedHeading, double cruiseSpeed, double distance) {
		requires(Robot.subChassis);
		_distance = distance;
		_absDistance = Math.abs(distance);
		_distanceSignum = Math.signum(distance);
		_cruiseSpeed = cruiseSpeed;
		_requestedHeading = requestedHeading;
	}

	public CmdBothDriveWithProfileAndGyro(double cruiseSpeed, double distance) {
		requires(Robot.subChassis);
		_distance = distance;
		_absDistance = Math.abs(distance);
		_distanceSignum = Math.signum(distance);
		_cruiseSpeed = cruiseSpeed;
	}
	
	protected void ProfileMockConstructor(double Speed, double distance){
		_distance = distance;
		_absDistance = Math.abs(distance);
		_distanceSignum = Math.signum(distance);
		_cruiseSpeed = Speed;		
	}
	
	// Called just before this Command runs the first time
	protected void initialize() {
		mp = new MotionProfiler(_absDistance, Settings.profileInitVelocity, _cruiseSpeed, _accerlation);
		Robot.subChassis.resetBothEncoders();
		System.err.println(String.format(
				"Projected Accelration Time: %1$.3f \tProjected Cruise Time: %2$.3f \t Projected Deccelration Time: %3$.3f \t Projected Length of Drive: %4$.3f \t Given Distance: %5$.3f",
				mp._accelTime, mp._cruiseTime, mp._deccelTime, mp._stopTime, _distance));
		_startTime = RobotMath.getTime();
		_abortTime = Math.abs(_distance) / _cruiseSpeed;
		_isFinished = false;
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		double encoderVal = Robot.subChassis.getLeftEncoderDistInch();
		double profileDist = mp.getTotalDistanceTraveled();
		double deltaTime = RobotMath.getTime() - _startTime;
		double currentHeading = Robot.subChassis.gyroGetRawHeading();
		double turnValue = headingDelta(currentHeading);
		double profileVelocity = mp.getProfileCurrVelocity(deltaTime);
		double throttlePos = (profileVelocity / MAXSPEED);
		double pidVal = pid.calcPID(profileDist, encoderVal);
		double finalThrottle = throttlePos + pidVal;
		
		String msg = String.format(
				"CurrVel: %1$.3f \t throttle: %2$.3f \t deltaTime: %3$.3f \t Disantce Travelled: %4$.3f \t AvgEncoder: %5$.3f \t PID Value: %10$.3f \t D Value: %11$.3f \t Final Throttle: %12$.3f",
				profileVelocity, throttlePos, deltaTime, mp.getTotalDistanceTraveled(),
				encoderVal, Robot.subChassis.getLeftEncoderDistInch(),
				Robot.subChassis.getRightEncoderDistInch(), currentHeading, turnValue, pidVal, pid.calcD(profileDist, encoderVal), finalThrottle);
		//FULL LOG MESSAGE: CurrVel: %1$.3f \t throttle: %2$.3f \t deltaTime: %3$.3f \t Disantce Travelled: %4$.3f \t AvgEncoder: %5$.3f \t Left Encoder: %6$.3f \t Right Encoder: %7$.3f \t Gyro Raw Heading: %8$.3f \t Turn Value: %9$.3f \t PID Value: %10$.3f \t P Value: %11$.3f \t Final Throttle: %12$.3f
		System.err.println(msg);
		//log.makeEntry(msg);
		SmartDashboard.putNumber("Drive Left Encoder:", Robot.subChassis.getLeftEncoderDistInch());
		SmartDashboard.putNumber("Drive Right Encoder", Robot.subChassis.getRightEncoderDistInch());

		Robot.subChassis.Drive((finalThrottle * _distanceSignum), 0/*turnValue*/);

		if (deltaTime > _abortTime && Robot.subChassis.getEncoderAvgDistInch() == 0) {
			_isFinished = true;
			Robot.subChassis._isSafe2Move = false;
		}
		if (encoderVal < _absDistance + Settings.profileMovementThreshold && encoderVal > _absDistance - Settings.profileMovementThreshold) {
			_isFinished = true;
		}
	}

	protected double headingDelta(double currentHeading) {
		return RobotMath.headingDelta(currentHeading, _requestedHeading, Settings.chassisDriveStraightGyroKp);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return _isFinished;
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.subChassis.Drive(0, 0);
		Robot.subChassis.resetBothEncoders();
		System.out.println("CmdBothDriveWithProfileAndGyro is Finished");
		// mp = null;
		// log.write();
		// log = null;
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}