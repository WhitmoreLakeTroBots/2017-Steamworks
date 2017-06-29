package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.RobotMath;
import org.usfirst.frc.team3668.robot.Settings;
import org.usfirst.frc.team3668.robot.visionProcessing.VisionData;
import org.usfirst.frc.team3668.robot.visionProcessing.VisionProcessing;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CmdBothVisionDriveWithProfileAndGyro extends CmdBothDriveWithProfileAndGyro {
	
	private double _visionAngle;
	VisionData initData = VisionProcessing.getVisionData();
	// private boolean goHalf;
	
	public CmdBothVisionDriveWithProfileAndGyro(/* boolean half */) {
		super(Settings.autoMoveVisionInchesPreSecond,(/*VisionProcessing.getVisionData().distToTarget +*/ Settings.profileVisionAddition));
		System.err.println("Constructor Vision Distance " + VisionProcessing.getVisionData().distToTarget);
		// _distance = distance;
		// goHalf = half;
		requires(Robot.subChassis);
	}

	@Override
	protected void initialize() {
		VisionData initData = VisionProcessing.getVisionData();
		ProfileMockConstructor(Settings.autoMoveVisionInchesPreSecond, (initData.distToTarget + Settings.profileVisionAddition));
		System.err.println("Constructor Vision Distance " + initData.distToTarget);
		super.initialize();
	}
	
	@Override
	protected double headingDelta(double currentHeading){
		double retVal = 0;
		double time = RobotMath.getTime();
		VisionData data = VisionProcessing.getVisionData();
		double deltaDist = _distance - Robot.subChassis.getEncoderAvgDistInch();
		if (data.foundTarget && deltaDist > Settings.vision2CloseThreshold
				&& Math.abs(time - data.lastWriteTime) < Settings.visionExpirationTime) {
			_visionAngle = data.angleToTarget;
			_requestedHeading = RobotMath.normalizeAngles(_visionAngle + currentHeading);
		}
		
		System.err.println(String.format("Vision Angle: %1$.3f \t Vision Distance: %2$.3f", _visionAngle, data.distToTarget));
		retVal = RobotMath.headingDelta(currentHeading, _requestedHeading, Settings.visionTurnProportion);
		
		return retVal;
	}
}
