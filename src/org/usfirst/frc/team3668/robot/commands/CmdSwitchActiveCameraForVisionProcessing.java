package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Settings;
import org.usfirst.frc.team3668.robot.visionProcessing.VisionProcessing;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class CmdSwitchActiveCameraForVisionProcessing extends Command {
	private static boolean _isFinished;
	private static Settings.cameraName currentCameraValue;
	private static Settings.cameraName previousCameraValue;
	private static Settings.cameraName defaultCameraValue;

	public CmdSwitchActiveCameraForVisionProcessing() {
		defaultCameraValue = Settings.cameraName.boilerCamera;
	}

	protected void initialize() {
		_isFinished = false;
	}

	protected void execute() {
		if(previousCameraValue == Settings.cameraName.boilerCamera){
			currentCameraValue = Settings.cameraName.gearCamera;
		} else if(previousCameraValue == Settings.cameraName.gearCamera){
			currentCameraValue = Settings.cameraName.boilerCamera;
		} else {
			currentCameraValue = defaultCameraValue;
		}
		VisionProcessing.setSwitchCameraValue(currentCameraValue);
		previousCameraValue = currentCameraValue;
		_isFinished = true;
	}

	protected boolean isFinished() {
		return _isFinished;
	}

	protected void end() {
	}

	protected void interrupted() {
		_isFinished = true;
	}
}
