package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.RobotMath;
import org.usfirst.frc.team3668.robot.Settings;
import org.usfirst.frc.team3668.robot.visionProcessing.VisionData;
import org.usfirst.frc.team3668.robot.visionProcessing.VisionProcessing;

public class CmdBothVisionDriveWithProfileAndGyro extends CmdBothDriveWithProfileAndGyro {
	//private double _distance;
	private boolean _isFinished = false;
	//private boolean goHalf;
    public CmdBothVisionDriveWithProfileAndGyro(/*boolean half*/) {
    	super(Settings.autoMoveInchesPerSecond,VisionProcessing.getVisionData().distToTarget);
    	//_distance = distance;
    	//goHalf = half;
    	requires(Robot.subChassis);
    }

    @Override
    protected void initialize() {
    	
//    	if(goHalf){
//    		_distance = VisionProcessing.getGearCalculatedDistanceFromTarget() - 48;
//    	} else {
//    		_distance = VisionProcessing.getGearCalculatedDistanceFromTarget() -12;
//    	}
//        _isFinished = false;
//        if(_distance < 24){
//        	_isFinished = true;
//        }
    }
    
    @Override
    protected void execute() {
    	VisionData data = VisionProcessing.getVisionData();
    	double time = RobotMath.getTime();
		double turnValue = 0;
		if (Math.abs(time - data.lastWriteTime) < Settings.visionExpirationTime) {
			_visionAngle = data.angleToTarget;
			turnValue = RobotMath.visionHeadingDelta(_visionAngle, -Settings.chassisDriveVisionGyroKp);
		} else if ((time - data.lastWriteTime) > Settings.visionExpirationTime) {
			turnValue = RobotMath.visionHeadingDelta(_visionAngle, -Settings.chassisDriveVisionGyroKp);
		}
    	if(Robot.subChassis.getABSEncoderAvgDistInch() < _distance){
    		System.err.println("Turn Value: " + turnValue + "\t Vision Angle: " + data.distToTarget);
    		Robot.subChassis.Drive(0.5, turnValue);
    	} else {
    		Robot.subChassis.Drive(0, 0);
    		_isFinished = true;
    	}
    }

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		Robot.subChassis.Drive(0, 0);
		return _isFinished;
	}
}
