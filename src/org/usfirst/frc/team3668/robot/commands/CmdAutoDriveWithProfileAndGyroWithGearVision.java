package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.RobotMath;
import org.usfirst.frc.team3668.robot.Settings;
import org.usfirst.frc.team3668.robot.visionProcessing.VisionProcessing;

public class CmdAutoDriveWithProfileAndGyroWithGearVision extends CmdBothDriveWithProfileAndGyro{
	private static boolean _foundTarget;
	private static double _timeAtInit;
	private static double _distanceToTarget;
	public CmdAutoDriveWithProfileAndGyroWithGearVision() {
		super(0,Settings.autoMoveInchesPerSecond,0);
	}
	@Override
	public void initialize(){
		super.initialize();
		_foundTarget = false;
		_timeAtInit = RobotMath.getTime();
	}
	@Override
	public void execute(){
		if (VisionProcessing.hasFoundGearTarget() && !_foundTarget) {
			_foundTarget = true;
			_distanceToTarget = VisionProcessing.getGearCalculatedDistanceFromTarget();
			_distance = _distanceToTarget;
		}
		if(_foundTarget){
			super.execute();	
		}
		if(RobotMath.getTime() - _timeAtInit > Settings.visionProcessingTimeOut){
			_isFinished = true;
		}
		
	}
}
