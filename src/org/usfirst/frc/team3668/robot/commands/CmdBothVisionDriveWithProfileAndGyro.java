package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.Settings;
import org.usfirst.frc.team3668.robot.visionProcessing.VisionProcessing;

import edu.wpi.first.wpilibj.command.Command;

public class CmdBothVisionDriveWithProfileAndGyro extends CmdBothDriveWithProfileAndGyro {
	//private double _distance;
	//private boolean _isFinished;
	//private boolean goHalf;
    public CmdBothVisionDriveWithProfileAndGyro(/*boolean half*/) {
    	super(Settings.autoMoveInchesPerSecond,VisionProcessing.getGearCalculatedDistanceFromTarget());
    	//_distance = distance;
    	//goHalf = half;
    	
    }

    //protected void initialize() {
    	
//    	if(goHalf){
//    		_distance = VisionProcessing.getGearCalculatedDistanceFromTarget() - 48;
//    	} else {
//    		_distance = VisionProcessing.getGearCalculatedDistanceFromTarget() -12;
//    	}
//        _isFinished = false;
//        if(_distance < 24){
//        	_isFinished = true;
//        }
//    }
//    protected void execute() {
//    	if(Robot.subChassis.getABSEncoderAvgDistInch() < _distance){
//    		Robot.subChassis.Drive(0.5, 0);
//    	} else {
//    		Robot.subChassis.Drive(0, 0);
//    		_isFinished = true;
//    	}
//    }

//	@Override
//	protected boolean isFinished() {
//		// TODO Auto-generated method stub
//		return _isFinished;
//	}
}
