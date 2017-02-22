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
	private static double _midpointOfContours;
	private static Object lockObject = new Object();
	private static double _gearCalculatedDistanceFromTarget;
	private static double _gearCalculatedAngleFromMidpoint;
	
	
	public void start() {

		BoilerGripPipeline boilerGripPipeline = new BoilerGripPipeline();
		GearGripPipeline gearGripPipeline = new GearGripPipeline();
		visionThread = new Thread(() -> {
			// Get the UsbCamera from CameraServer
			UsbCamera boilerCamera = CameraServer.getInstance().startAutomaticCapture(0);
			UsbCamera gearCamera = CameraServer.getInstance().startAutomaticCapture(1);

			// Set the resolution
			boilerCamera.setResolution(Settings.visionImageWidthPixels, Settings.visionImageHeightPixels);
			boilerCamera.setExposureManual(25);
			boilerCamera.setBrightness(0);
			gearCamera.setResolution(Settings.visionImageWidthPixels, Settings.visionImageHeightPixels);
			gearCamera.setExposureManual(25);
			gearCamera.setBrightness(0);

			double totalContourWidth = 0;
			double averageMidpoint = 0;
			double averageContourWidth = 0;
			double distFromTarget = 0;
			double angleOffCenter = 0;
			int imgCounter = 0;
			double totalMidpoint = 0;
			// Get a CvSink. This will capture Mats from the camera
			CvSink boilerCvSink = CameraServer.getInstance().getVideo(boilerCamera);
			CvSink gearCvSink = CameraServer.getInstance().getVideo(gearCamera);

			// Setup a CvSource. This will send images back to the Dashboard
			// CvSource outputStream =
			// CameraServer.getInstance().putVideo("VisionProcessing",
			// Settings.visionImageWidthPixels,
			// Settings.visionImageHeightPixels);

			// Mats are very memory expensive. Let's reuse this Mat.
			Mat mat = new Mat();
			// boolean isImgWritten = false;
			// int imgCounter = 1;

			// This cannot be 'true'. The program will never exit if it is. This
			// lets the robot stop this thread when restarting robot code or
			// deploying.
			while (!Thread.interrupted()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// Tell the CvSink to grab a frame from the camera and put it
				// in the source mat. If there is an error notify the output.
				if (boilerCvSink.grabFrame(mat) == 0) {
					// Send the output the error.
					// skip the rest of the current iteration
					continue;
				}

				boilerGripPipeline.process(mat);
				if (!boilerGripPipeline.filterContoursOutput().isEmpty()) {
					for (MatOfPoint contour : boilerGripPipeline.filterContoursOutput()) {
						Rect boundingBox = Imgproc.boundingRect(contour);
						double boxMidpoint = ((boundingBox.width / 2) + boundingBox.x);
						totalMidpoint = totalMidpoint + boxMidpoint;
						totalContourWidth = totalContourWidth + boundingBox.width;
						imgCounter++;
					}
					averageMidpoint = totalMidpoint / imgCounter;
					averageContourWidth = totalContourWidth / imgCounter;
					distFromTarget = RobotMath.boilerWidthOfContoursToDistanceInFeet(averageContourWidth);
					angleOffCenter = RobotMath.boilerAngleToTurnWithVisionProfiling(averageContourWidth, averageMidpoint);
				}
				synchronized (lockObject) {
					_boilerCalculatedAngleFromMidpoint = distFromTarget;
					_boilerCalculatedDistanceFromTarget = angleOffCenter;
				}
				if (mat != null) {
					mat.release();
				}
				SmartDashboard.putNumber("Calculated Angle From Center: ", angleOffCenter);
				SmartDashboard.putNumber("Calculated Distance From Target: ", distFromTarget);

				totalContourWidth = 0;
				imgCounter = 0;
				totalMidpoint = 0;
				averageContourWidth = 0;

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// Tell the CvSink to grab a frame from the camera and put it
				// in the source mat. If there is an error notify the output.
				if (gearCvSink.grabFrame(mat) == 0) {
					// Send the output the error.
					// skip the rest of the current iteration
					continue;
				}

				gearGripPipeline.process(mat);
				if (!gearGripPipeline.filterContoursOutput().isEmpty()) {
					for (MatOfPoint contour : gearGripPipeline.filterContoursOutput()) {
						Rect boundingBox = Imgproc.boundingRect(contour);
						double boxMidpoint = ((boundingBox.width / 2) + boundingBox.x);
						averageMidpoint = averageMidpoint + boxMidpoint;
						totalContourWidth = totalContourWidth + boundingBox.width;
						imgCounter++;
					}
					averageContourWidth = totalContourWidth / imgCounter;
					averageMidpoint = averageMidpoint / imgCounter;
					SmartDashboard.putNumber("middle of target ", averageMidpoint / 640);
				}

				synchronized (lockObject) {
					_midpointOfContours = averageMidpoint;
				}
				totalContourWidth = 0;
				imgCounter = 0;
				averageMidpoint = 0;
				averageContourWidth = 0;

				if (mat != null) {
					mat.release();
				}
			}

		});

		visionThread.setDaemon(true);
		visionThread.start();
	}

	public void stop() {
		visionThread.interrupt();
		visionThread = null;
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

}