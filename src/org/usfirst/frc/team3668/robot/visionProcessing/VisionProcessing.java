package org.usfirst.frc.team3668.robot.visionProcessing;

import java.util.Arrays;

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
	private static boolean _foundBoilerTarget;
	private static boolean _foundGearTarget;
	private static Settings.cameraName _switchActiveCamera = Settings.cameraName.noProcess;
	private static double totalContourWidth = 0;
	private static double averageMidpoint = 0;
	private static double averageContourWidth = 0;
	private static double distFromTarget = 0;
	private static double angleOffCenter = 0;
	private static double averageArea = 0;
	private static int imgCounter = 0;
	private static double totalMidpoint = 0;
	private static boolean foundBoilerTarget;
	private static boolean foundGearTarget;
	private static Mat mat = new Mat();
	private static BoilerGripPipeline boilerGripPipeline = new BoilerGripPipeline();
	private static GearGripPipeline gearGripPipeline = new GearGripPipeline();
	private static CvSink cvSink = null;
	private boolean initializedCamera = false;

	public void start() {
		initializedCamera = false;
		System.err.println("Vision Processing Started");
		visionThread = new Thread(() -> {
			UsbCamera usbCamera = null;
			Settings.cameraName previousCameraValue = Settings.cameraName.noProcess;

			// UsbCamera gearCamera =
			// CameraServer.getInstance().startAutomaticCapture(1);

			// gearCamera.setResolution(Settings.visionImageWidthPixels,
			// Settings.visionImageHeightPixels);
			// gearCamera.setExposureManual(Settings.cameraExposure);
			// gearCamera.setBrightness(Settings.cameraBrightness);
			// gearCamera.setFPS(Settings.cameraFrameRate);

			// CvSink gearCvSink =
			// CameraServer.getInstance().getVideo(gearCamera);
			while (!Thread.interrupted()) {
				Settings.cameraName switchValue = getCurrentCamera();
				boolean cameraValueChanged = switchValue != previousCameraValue;
				// System.err.println("Camera value changed: " +
				// cameraValueChanged);
				if (cameraValueChanged) {
					System.err.println("Camera initialized.");
					previousCameraValue = switchValue;
					usbCamera = CameraServer.getInstance().startAutomaticCapture((int) switchValue.ordinal());
					usbCamera.setResolution(Settings.visionImageWidthPixels, Settings.visionImageHeightPixels);
					// usbCamera.setExposureManual(Settings.cameraExposure);
					// usbCamera.setBrightness(Settings.cameraBrightness);
					usbCamera.setFPS(Settings.cameraFrameRate);
					cvSink = CameraServer.getInstance().getVideo(usbCamera);
					initializedCamera = true;
				}
				// System.err.println("Current Camera: " + switchValue.name());
				if (initializedCamera) {
					if (cvSink.grabFrame(mat) != 0) {
						switch (switchValue) {
						case boilerCamera:
							processBoilerImage();
							break;
						case gearCamera:
							processGearCamera();
							break;
						case noProcess:
							break;
						}
					}

					if (mat != null) {
						mat.release();
					}
					totalContourWidth = 0;
					averageMidpoint = 0;
					averageContourWidth = 0;
					distFromTarget = 0;
					angleOffCenter = 0;
					averageArea = 0;
				}
			}
		});

		visionThread.setDaemon(true);
		visionThread.setPriority(Thread.MIN_PRIORITY);
		visionThread.start();
	}

	public void stop() {
		if (visionThread != null) {
			visionThread.interrupt();
			visionThread = null;
		}
	}

	private static void processBoilerImage() {
		MatOfPoint[] contourArray;
		int contourCounter = 0;
		angleOffCenter = 0;
		distFromTarget = 0;
		foundBoilerTarget = false;
		if (cvSink.grabFrame(mat) != 0) {
			System.err.println("Processing Boiler Image");
			boilerGripPipeline.process(mat);
			if (!boilerGripPipeline.filterContoursOutput().isEmpty()) {
				contourArray = new MatOfPoint[boilerGripPipeline.filterContoursOutput().size()];
				for (MatOfPoint contour : boilerGripPipeline.filterContoursOutput()) {
					contourArray[contourCounter] = contour;
					contourCounter++;
				}
				Arrays.sort(contourArray, new ContourComparator());
				if (contourArray.length >= 2) {
					MatOfPoint upperTarget = contourArray[0];
					MatOfPoint lowerTarget = contourArray[1];
					Rect upperTargetBoundingBox = Imgproc.boundingRect(upperTarget);
					Rect lowerTargetBoundingBox = Imgproc.boundingRect(lowerTarget);
					double upperTargetArea = Imgproc.contourArea(upperTarget);
					double lowerTargetArea = Imgproc.contourArea(lowerTarget);

//					// Save image w/o modification
//					Imgcodecs.imwrite("/media/sda1/imageWithoutBoxes" + imgCounter + ".jpeg", mat);
//					// Draw all the contours. (I don't know how to draw
//					// specified contours with how this is structured)
//					Imgproc.drawContours(mat, boilerGripPipeline.filterContoursOutput(), -1, new Scalar(4, 232, 4), 2);
//					// Draw largest contour's bounding box
//					Imgproc.rectangle(mat, new Point(upperTargetBoundingBox.x - 2, upperTargetBoundingBox.y - 2),
//							new Point(upperTargetBoundingBox.x + upperTargetBoundingBox.width + 2,
//									upperTargetBoundingBox.y + upperTargetBoundingBox.height + 2),
//							new Scalar(255, 255, 255), 2);
//					// Draw second largest contour's bounding box
//					Imgproc.rectangle(mat, new Point(lowerTargetBoundingBox.x - 2, lowerTargetBoundingBox.y - 2),
//							new Point(lowerTargetBoundingBox.x + lowerTargetBoundingBox.width + 2,
//									lowerTargetBoundingBox.y + lowerTargetBoundingBox.height + 2),
//							new Scalar(255, 255, 255), 2);
//					Imgcodecs.imwrite("/media/sda1/image" + imgCounter + ".jpeg", mat);
					
					
					System.err.println(imgCounter);
					System.err.println(upperTargetArea);
					System.err.println(lowerTargetArea);
					averageMidpoint = (((upperTargetBoundingBox.width / 2) + upperTargetBoundingBox.x)
							+ ((lowerTargetBoundingBox.width / 2) + lowerTargetBoundingBox.y)) / 2;
					averageArea = (upperTargetArea + lowerTargetArea) / 2;
					averageContourWidth = (upperTargetBoundingBox.width + lowerTargetBoundingBox.width) / 2;
					//Rewrite equation to use area
					distFromTarget = RobotMath.boilerWidthOfContoursToDistanceInFeet(averageContourWidth);
					angleOffCenter = RobotMath.boilerAngleToTurnWithVisionProfiling(averageContourWidth,
							averageMidpoint);
					foundBoilerTarget = (Math.abs(1.45 - (upperTargetArea / lowerTargetArea)) < 0.2);
					contourCounter = 0;
					imgCounter++;
				}

			}
			synchronized (lockObject) {
				_boilerCalculatedAngleFromMidpoint = angleOffCenter;
				_boilerCalculatedDistanceFromTarget = distFromTarget;
				_foundBoilerTarget = foundBoilerTarget;
			}

		}
	}

	private static void processGearCamera() {
		MatOfPoint[] contourArray;
		int contourCounter = 0;
		angleOffCenter = 0;
		distFromTarget = 0;
		foundGearTarget = false;
		if (cvSink.grabFrame(mat) != 0) {
			System.err.println("Processing Gear Image");
			gearGripPipeline.process(mat);
			contourArray = new MatOfPoint[gearGripPipeline.filterContoursOutput().size()];
			if (!gearGripPipeline.filterContoursOutput().isEmpty()) {
				for (MatOfPoint contour : gearGripPipeline.filterContoursOutput()) {
					contourArray[contourCounter] = contour;
					contourCounter++;
				}
				Arrays.sort(contourArray, new ContourComparator());
				if (contourArray.length >= 2) {
					MatOfPoint upperTarget = contourArray[0];
					MatOfPoint lowerTarget = contourArray[1];
					Rect upperTargetBoundingBox = Imgproc.boundingRect(upperTarget);
					Rect lowerTargetBoundingBox = Imgproc.boundingRect(lowerTarget);
					double upperTargetArea = Imgproc.contourArea(upperTarget);
					double lowerTargetArea = Imgproc.contourArea(lowerTarget);
					
					averageMidpoint = (((upperTargetBoundingBox.width / 2) + upperTargetBoundingBox.x)
							+ ((lowerTargetBoundingBox.width / 2) + lowerTargetBoundingBox.x)) / 2;
					averageArea = (upperTargetArea + lowerTargetArea) / 2;
					averageContourWidth = (upperTargetBoundingBox.width + lowerTargetBoundingBox.width) / 2;
					//Rewrite the code to use area as opposed to width
					distFromTarget = RobotMath.gearWidthOfContoursToDistanceInFeet(averageContourWidth);
					angleOffCenter = RobotMath.gearAngleToTurnWithVisionProfiling(averageContourWidth, averageMidpoint);
					foundGearTarget = (Math.abs(1 - (upperTargetArea / lowerTargetArea)) < 0.2);
				}
			}

			synchronized (lockObject) {
				_gearCalculatedAngleFromMidpoint = angleOffCenter;
				_gearCalculatedDistanceFromTarget = distFromTarget;
				_foundGearTarget = foundGearTarget;
			}
		}
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

	public static void setSwitchCameraValue(Settings.cameraName cameraNumber) {
		synchronized (lockObject) {
			_switchActiveCamera = cameraNumber;
		}
	}

	public static Settings.cameraName getCurrentCamera() {
		Settings.cameraName switchValue;
		synchronized (lockObject) {
			switchValue = _switchActiveCamera;
		}
		return switchValue;
	}

	public static boolean hasFoundBoilerTarget() {
		boolean foundBoilerTarget;
		synchronized (lockObject) {
			foundBoilerTarget = _foundBoilerTarget;
		}
		return foundBoilerTarget;
	}
	public static boolean hasFoundGearTarget() {
		boolean foundGearTarget;
		synchronized (lockObject) {
			foundGearTarget = _foundGearTarget;
		}
		return foundGearTarget;
	}

}