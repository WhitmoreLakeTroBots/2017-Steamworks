package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.RobotMath;
import org.usfirst.frc.team3668.robot.Settings;
import org.usfirst.frc.team3668.robot.visionProcessing.VisionProcessing;

public class CmdAutoTurnWithGyroWithGearVision extends CmdTurnWithGyro{
	private static double _angleToTurn;
	private static boolean _foundTarget;
	private static double _timeAtInit;
	public CmdAutoTurnWithGyroWithGearVision() {
		super(0);
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
			_angleToTurn = VisionProcessing.getGearCalculatedAngleFromTarget();		
			_headingDegrees = _angleToTurn;
		}
		if(_foundTarget){
			super.execute();	
		}
		if(RobotMath.getTime() - _timeAtInit > Settings.visionProcessingTimeOut){
			_isFinished = true;
		}
		
	}
}
