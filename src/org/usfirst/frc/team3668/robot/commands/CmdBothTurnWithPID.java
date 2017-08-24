package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.RobotMath;
import org.usfirst.frc.team3668.robot.Settings;
import org.usfirst.frc.team3668.robot.motionProfile.Logger;
import org.usfirst.frc.team3668.robot.motionProfile.MotionProfiler;
import org.usfirst.frc.team3668.robot.PID;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CmdBothTurnWithPID extends Command{
	
	double _degrees;
	double _absDegrees;
	double _distance;
	double _degreeSignum;
	double _cruiseSpeed;
	double _accerlation = Settings.profileDriveAccelration;
	double _startTime;
	double _abortTime;
	boolean _isFinished = false;
	private MotionProfiler mp;
	private PID pid;
	
	public CmdBothTurnWithPID (double degrees, double speed){
		requires(Robot.subChassis);
		_degrees = degrees;
		_absDegrees = Math.abs(degrees);
		_cruiseSpeed = speed;
	}
	
	private double calcDist(double degrees, double diameter){
		return Math.PI * diameter * (degrees / 360);
	}
	
	protected double calcDegrees(double arcDist, double diameter){
		return 360 * (arcDist / (Math.PI * diameter));
	}
	
	protected void initialize(){
		_distance = calcDist(_absDegrees, Settings.profileRobotDiameter);
		_degreeSignum = Math.signum(_degrees);
		mp = new MotionProfiler(_distance, Settings.profileInitVelocity, _cruiseSpeed, _accerlation);
		pid = new PID(Settings.profileTurnKp, Settings.profileTurnKi, Settings.profileTurnKd);
		_startTime = RobotMath.getTime();
		_abortTime = Math.abs(_distance) / _cruiseSpeed;
		System.err.println(String.format(
				"Projected Accelration Time: %1$.3f \tProjected Cruise Time: %2$.3f \t Projected Deccelration Time: %3$.3f \t Projected Length of Drive: %4$.3f \t Given Distance: %5$.3f",
				mp._accelTime, mp._cruiseTime, mp._deccelTime, mp._stopTime, _distance));
	}
	
	protected void execute(){
		double deltaTime = RobotMath.getTime() - _startTime;
		double profileDist = mp.getTotalDistanceTraveled(deltaTime);
		double profileDegrees = calcDegrees(profileDist, Settings.profileRobotDiameter) * _degreeSignum;
		double currentHeading = Robot.subChassis.gyroGetRawHeading();
		double profileVelocity = mp.getProfileCurrVelocity(deltaTime);
		double throttlePos = (profileVelocity / Settings.robotMaxInchesPerSecond);
		double pidVal = pid.calcPID(profileDegrees, currentHeading);
		double finalThrottle = throttlePos + pidVal;
		
		String msg = String.format(
				"ProVel: %1$.3f \t throttle: %2$.3f \t Time: %3$.3f \t ProfileX: %4$.3f \t ProDe: %11$.3f \t Gyro: %5$.3f \t PID: %6$.3f \t P: %7$.3f \t I: %8$.3f \t D: %9$.3f \t Final Throttle: %10$.3f",
				profileVelocity, throttlePos, deltaTime, mp.getTotalDistanceTraveled(deltaTime), currentHeading, pidVal, pid.getPError(), pid.getIError(), pid.getDError(), finalThrottle, profileDegrees);
		System.err.println(msg);
		
		Robot.subChassis.DriveMan(finalThrottle * _degreeSignum, -finalThrottle * _degreeSignum);
		
		if( currentHeading < _degrees + Settings.profileMovementThreshold && currentHeading > _degrees - Settings.profileMovementThreshold){
			_isFinished = true;
		}
	}
	
	protected boolean isFinished(){
		return _isFinished;
	}
	
	protected void end(){
		Robot.subChassis.Drive(0, 0);
		Robot.subChassis.resetBothEncoders();
		System.out.println("CmdBothTurnWithPID is Finished");
		System.err.println(String.format(
				"Projected Accelration Time: %1$.3f \tProjected Cruise Time: %2$.3f \t Projected Deccelration Time: %3$.3f \t Projected Length of Drive: %4$.3f \t Given Distance: %5$.3f",
				mp._accelTime, mp._cruiseTime, mp._deccelTime, mp._stopTime, _distance));
	}

	protected void interrupted (){
		end();
	}
}
