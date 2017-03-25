package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.RobotMath;
import org.usfirst.frc.team3668.robot.Settings;
import org.usfirst.frc.team3668.robot.visionProcessing.VisionData;
import org.usfirst.frc.team3668.robot.visionProcessing.VisionProcessing;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CmdBothVisionDriveWithProfileAndGyro extends CmdBothDriveWithProfileAndGyro {
	// private double _distance;
	private boolean _isFinished = false;
	private double _distance;

	// private boolean goHalf;
	public CmdBothVisionDriveWithProfileAndGyro(/* boolean half */) {
		 super(Settings.autoMoveVisionInchesPreSecond,-(VisionProcessing.getVisionData().distToTarget + Settings.profileVisionAddition));
		// _distance = distance;
		// goHalf = half;
		requires(Robot.subChassis);
	}

	@Override
	protected double headingDelta(double currentHeading){
		double retVal = 0;
		double time = RobotMath.getTime();
		VisionData data = VisionProcessing.getVisionData();
		_visionDistance = data.distToTarget;
		double deltaDist = _distance - Robot.subChassis.getEncoderAvgDistInch();
		if (data.foundTarget && deltaDist > Settings.vision2CloseThreshold
				&& Math.abs(time - data.lastWriteTime) < Settings.visionExpirationTime) {
			_visionAngle = data.angleToTarget;
			_requestedHeading = RobotMath.normalizeAngles(_visionAngle + currentHeading);
		}
		
		System.err.println("Using Overriden headingDelta()");
		retVal = RobotMath.headingDelta(currentHeading, _requestedHeading, Settings.chassisDriveStraightGyroKp);
		
		if (deltaDist < Settings.vision2CloseThreshold) {
			retVal = 0;
		}
		return retVal;
	}
	
//	protected void initialize() {
//
//		// if(goHalf){
//		// _distance = VisionProcessing.getGearCalculatedDistanceFromTarget() -
//		// 48;
//		// } else {
//		Robot.subChassis.resetBothEncoders();
//		_distance = VisionProcessing.getVisionData().distToTarget;
//		SmartDashboard.putNumber("Distance at init: ", _distance);
//		// }
//		// _isFinished = false;
//		// if(_distance < 24){
//		// _isFinished = true;
//		// }
//	}
//
//	protected void execute() {
//		VisionData data = VisionProcessing.getVisionData();
//		double time = RobotMath.getTime();
//		double turnValue;
//		if (Math.abs(time - data.lastWriteTime) < Settings.visionExpirationTime) {
//			_visionAngle = data.angleToTarget;
//		}
//		
//		turnValue = RobotMath.visionHeadingDelta(_visionAngle, -Settings.chassisDriveVisionGyroKp);
//		
//		if (Robot.subChassis.getABSEncoderAvgDistInch() < _distance) {
////			System.err.println("Turn Value: " + turnValue + "\t Vision Angle: " + data.angleToTarget +"\t Vision Distance: "+ data.distToTarget+ "\t Encoder Value: " + Robot.subChassis.getABSEncoderAvgDistInch() + "\t Is Finished: " + _isFinished);
//			Robot.subChassis.Drive(0.35, turnValue);
//		} else {
//			Robot.subChassis.Drive(0, 0);
//			_isFinished = true;
//		}
//	}
//
//	@Override
//	protected boolean isFinished() {
//		// TODO Auto-generated method stub
//		return _isFinished;
//	}
//	@Override
//	protected void end() {
//		// TODO Auto-generated method stub
//		Robot.subChassis.Drive(0, 0);
//	}
}
