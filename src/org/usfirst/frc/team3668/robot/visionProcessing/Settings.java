package org.usfirst.frc.team3668.robot.visionProcessing;

public class Settings {
	public static enum cameraName{
		boilerCamera, gearCamera, noProcess
	}
	
	public static final int visionImageWidthPixels = 640; 
	
	public static final int visionImageHeightPixels = 360;

	public static final int visionImageCenterXPixels = visionImageWidthPixels/2;
	
	public static final int visionImageCenterYPixels = visionImageHeightPixels/2;
	
	public static final double boilerVisionTargetWidth = 16; // In inches
	
	public static final double gearVisionTargetWidth = 2; // In inches
	
	public static final int cameraExposure = 20;
	
//	public static final int cameraBrightness = 0;
	
	public static final int cameraFrameRate = 8;
	
	public static final double visionProcessingTimeOut = 1.5;
}
