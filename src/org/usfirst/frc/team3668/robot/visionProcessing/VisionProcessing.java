package org.usfirst.frc.team3668.robot.visionProcessing;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team3668.robot.RobotMath;
import org.usfirst.frc.team3668.robot.Settings;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class VisionProcessing {

	Thread visionThread;
	private static double _boilerCalculatedDistanceFromTarget;
	private static double _boilerCalculatedAngleFromMidpoint;
	private static Object lockObject = new Object();
	private static double _gearCalculatedDistanceFromTarget;
	private static double _gearCalculatedAngleFromMidpoint;
	private static Settings.cameraName _switchActiveCamera;

	public void start() {

		BoilerGripPipeline boilerGripPipeline = new BoilerGripPipeline();
		GearGripPipeline gearGripPipeline = new GearGripPipeline();
		visionThread = new Thread(() -> {
			UsbCamera usbCamera = null;
			CvSink cvSink = null;
			double totalContourWidth = 0;
			double averageMidpoint = 0;
			double averageContourWidth = 0;
			double distFromTarget = 0;
			double angleOffCenter = 0;
			int imgCounter = 0;
			double totalMidpoint = 0;
			Settings.cameraName previousCameraValue = Settings.cameraName.boilerCamera;
//			System.err.println("Running Vision Processing");
			
//			UsbCamera gearCamera = CameraServer.getInstance().startAutomaticCapture(1);

			
//			gearCamera.setResolution(Settings.visionImageWidthPixels, Settings.visionImageHeightPixels);
//			gearCamera.setExposureManual(Settings.cameraExposure);
//			gearCamera.setBrightness(Settings.cameraBrightness);
//			gearCamera.setFPS(Settings.cameraFrameRate);



//			CvSink gearCvSink = CameraServer.getInstance().getVideo(gearCamera);
			Mat mat = new Mat();

			while (!Thread.interrupted()) {
				Settings.cameraName switchValue = getSwitchValue();
				boolean cameraValueChanged = switchValue != previousCameraValue;
				if(cameraValueChanged){
					previousCameraValue = switchValue;
					usbCamera = CameraServer.getInstance().startAutomaticCapture(switchValue.ordinal());
					usbCamera.setResolution(Settings.visionImageWidthPixels, Settings.visionImageHeightPixels);
					usbCamera.setExposureManual(Settings.cameraExposure);
					usbCamera.setBrightness(Settings.cameraBrightness);
					usbCamera.setFPS(Settings.cameraFrameRate);
					cvSink = CameraServer.getInstance().getVideo(usbCamera);
				}
				if (cvSink.grabFrame(mat) != 0) {
					System.err.println("Processing Boiler Image");
					switch(switchValue){
					case boilerCamera:
						processBoilerImage();
						break;
					case gearCamera:
						processGearCamera();
						break;
					}
									}
				totalContourWidth = 0;
				imgCounter = 0;
				totalMidpoint = 0;
				averageContourWidth = 0;
				distFromTarget = 0;
				angleOffCenter = 0;

//				if (gearCvSink.grabFrame(mat) != 0) {
//					System.err.println("Processing Gear Image");
//					gearGripPipeline.process(mat);
//					if (!gearGripPipeline.filterContoursOutput().isEmpty()) {
//						for (MatOfPoint contour : gearGripPipeline.filterContoursOutput()) {
//							Rect boundingBox = Imgproc.boundingRect(contour);
//							double boxMidpoint = ((boundingBox.width / 2) + boundingBox.x);
//							averageMidpoint = averageMidpoint + boxMidpoint;
//							totalContourWidth = totalContourWidth + boundingBox.width;
//							imgCounter++;
//						}
//						averageContourWidth = totalContourWidth / imgCounter;
//						averageMidpoint = averageMidpoint / imgCounter;
//						distFromTarget = RobotMath.gearWidthOfContoursToDistanceInFeet(averageContourWidth);
//						angleOffCenter = RobotMath.gearAngleToTurnWithVisionProfiling(averageContourWidth, averageMidpoint);
//					}
//
//					synchronized (lockObject) {
//						_gearCalculatedAngleFromMidpoint = angleOffCenter;
//						_gearCalculatedDistanceFromTarget = distFromTarget;
//					}
//
//					if (mat != null) {
//						mat.release();
//					}
//				}
				totalContourWidth = 0;
				imgCounter = 0;
				averageMidpoint = 0;
				averageContourWidth = 0;
				distFromTarget = 0;
				angleOffCenter = 0;
			}
		});

		visionThread.setDaemon(true);
		visionThread.setPriority(Thread.MIN_PRIORITY);
		visionThread.start();
	}

	public void stop() {
		visionThread.interrupt();
		visionThread = null;
	}
	
	private static void processBoilerImage(){
		
	}
	
	private static void processGearCamera(){
		
	}

	public static double getBoilerCalculatedDistanceFromTarget() {
		double boilerCalculatedDistanceFromTarget;

		synchronized (lockObject) {
			boilerCalculatedDistanceFromTarget = _boilerCalculatedDistanceFromTarget;
		}

		return boilerCalculatedDistanceFromTarget;
	}

	public static double getBoilerCalculatedAngleFromMidpoint() {
		double boilerCalculatedAngleFromMidpoint;

		synchronized (lockObject) {
			boilerCalculatedAngleFromMidpoint = _boilerCalculatedAngleFromMidpoint;
		}

		return boilerCalculatedAngleFromMidpoint;
	}

	public static double getGearCalculatedAngleFromTarget() {
		double gearCalculatedAngleFromMidpoint;

		synchronized (lockObject) {
			gearCalculatedAngleFromMidpoint = _gearCalculatedAngleFromMidpoint;
		}

		return gearCalculatedAngleFromMidpoint;
	}

	public static double getGearCalculatedDistanceFromTarget() {
		double gearCalculatedDistanceFromTarget;

		synchronized (lockObject) {
			gearCalculatedDistanceFromTarget = _gearCalculatedDistanceFromTarget;
		}

		return gearCalculatedDistanceFromTarget;
	}
	
	public static void setSwitchCameraValue(Settings.cameraName cameraNumber){
		synchronized (lockObject) {
			_switchActiveCamera = cameraNumber;
		}
	}
	private static Settings.cameraName getSwitchValue(){
		Settings.cameraName switchValue;
		synchronized (lockObject) {
			switchValue = _switchActiveCamera;
		}
		return switchValue;
	}

}