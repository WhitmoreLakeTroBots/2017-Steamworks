package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Settings;
import org.usfirst.frc.team3668.robot.visionProcessing.VisionProcessing;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class CmdSwitchActiveCameraForVisionProcessing extends Command {
	private boolean _isFinished;
	private Settings.cameraName currentCameraValue;
	private Settings.cameraName previousCameraValue;
	private Settings.cameraName defaultCameraValue;
	private int i;
	public CmdSwitchActiveCameraForVisionProcessing() {
		defaultCameraValue = Settings.cameraName.boilerCamera;
	}

	protected void initialize() {
		previousCameraValue = VisionProcessing.getCurrentCamera();
		_isFinished = false;
	}

	protected void execute() {
		if (!_isFinished) {
			if (previousCameraValue == Settings.cameraName.boilerCamera) {
				currentCameraValue = Settings.cameraName.gearCamera;
			} else if (previousCameraValue == Settings.cameraName.gearCamera) {
				currentCameraValue = Settings.cameraName.boilerCamera;
			} else {
				currentCameraValue = defaultCameraValue;
			}
			VisionProcessing.setSwitchCameraValue(currentCameraValue);
			previousCameraValue = currentCameraValue;
			System.err.println("Switching active camera.");
			System.err.println("Current Camera: " + currentCameraValue.name());
			_isFinished = true;
			i++;
			System.err.println(i);
		}
	}

	protected boolean isFinished() {
		return _isFinished;
	}

	protected void end() {
		i=0;
	}

	protected void interrupted() {
		_isFinished = true;
	}
}
