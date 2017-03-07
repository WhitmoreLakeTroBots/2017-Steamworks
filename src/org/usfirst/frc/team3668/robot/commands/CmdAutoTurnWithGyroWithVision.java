package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.RobotMath;
import org.usfirst.frc.team3668.robot.Settings;
import org.usfirst.frc.team3668.robot.visionProcessing.VisionProcessing;

public class CmdAutoTurnWithGyroWithVision extends CmdTurnWithGyro{
	private static double _angleToTurn;
	private static boolean _foundTarget;
	private static double _timeAtInit;
	public CmdAutoTurnWithGyroWithVision() {
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
		if (VisionProcessing.hasFoundBoilerTarget() && !_foundTarget) {
			_foundTarget = true;
			_angleToTurn = VisionProcessing.getBoilerCalculatedAngleFromMidpoint();		
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
