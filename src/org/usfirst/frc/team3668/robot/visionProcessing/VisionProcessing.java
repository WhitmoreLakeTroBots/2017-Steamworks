package org.usfirst.frc.team3668.robot.visionProcessing;

import java.util.Arrays;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
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
	private static Settings.cameraName _switchActiveCamera = Settings.cameraName.noProcess;
	private static double totalContourWidth = 0;
	private static double averageMidpoint = 0;
	private static double averageContourWidth = 0;
	private static double distFromTarget = 0;
	private static double angleOffCenter = 0;
	private static double averageArea = 0;
	private static int imgCounter = 0;
	private static double totalMidpoint = 0;
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
					imgCounter = 0;
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
		Rect[] boundingBoxArray;
		int contourCounter = 0;
		int boilerImgCounter = 0;
		if (cvSink.grabFrame(mat) != 0) {
			System.err.println("Processing Boiler Image");
			boilerGripPipeline.process(mat);
			if (!boilerGripPipeline.filterContoursOutput().isEmpty()) {
				boilerImgCounter++;
				boundingBoxArray = new Rect[boilerGripPipeline.filterContoursOutput().size()];
				for (MatOfPoint contour : boilerGripPipeline.filterContoursOutput()) {
					Rect boundingBox = Imgproc.boundingRect(contour);
					boundingBoxArray[contourCounter] = boundingBox;
					contourCounter++;
				}
				Arrays.sort(boundingBoxArray, new RectangleComparator());
				for (Rect rect : boundingBoxArray) {
					System.err.println(rect.area() + "  ");
					Imgproc.rectangle(mat, new Point(rect.x - 2, rect.y - 2),
							new Point(rect.x + rect.width + 2, rect.y + rect.width + 2), new Scalar(255, 255, 255), 2);
				}
				// Imgcodecs.imwrite("/media/sda1/image" + boilerImgCounter +
				// ".jpeg", mat);
				if (boundingBoxArray.length >= 2) {
					averageMidpoint = (((boundingBoxArray[0].width / 2) + boundingBoxArray[0].x)
							+ ((boundingBoxArray[1].width / 2) + boundingBoxArray[1].x)) / 2;
					averageArea = (boundingBoxArray[0].area() + boundingBoxArray[1].area()) / 2;
					averageContourWidth = (boundingBoxArray[0].width + boundingBoxArray[1].width) / 2;
					distFromTarget = RobotMath.boilerWidthOfContoursToDistanceInFeet(averageContourWidth);
					angleOffCenter = RobotMath.boilerAngleToTurnWithVisionProfiling(averageContourWidth,
							averageMidpoint);
					SmartDashboard.putNumber("Average Width: ", averageContourWidth);
					SmartDashboard.putNumber("Average Area: ", averageArea);
					SmartDashboard.putNumber("Average Midpoint: ", averageMidpoint);
					contourCounter = 0;
				}
			}
			synchronized (lockObject) {
				_boilerCalculatedAngleFromMidpoint = angleOffCenter;
				_boilerCalculatedDistanceFromTarget = distFromTarget;
			}
		}
	}

	private static void processGearCamera() {
		Rect[] boundingBoxArray;
		if (cvSink.grabFrame(mat) != 0) {
			System.err.println("Processing Gear Image");
			gearGripPipeline.process(mat);
			boundingBoxArray = new Rect[gearGripPipeline.filterContoursOutput().size()];
			if (!gearGripPipeline.filterContoursOutput().isEmpty()) {
				for (MatOfPoint contour : gearGripPipeline.filterContoursOutput()) {
					Rect boundingBox = Imgproc.boundingRect(contour);
					boundingBoxArray[imgCounter] = boundingBox;
					imgCounter++;
				}
				Arrays.sort(boundingBoxArray, new RectangleComparator());
				for (Rect rect : boundingBoxArray) {
					System.err.println(rect.area() + "  ");
				}
				averageMidpoint = (((boundingBoxArray[0].width / 2) + boundingBoxArray[0].x)
						+ ((boundingBoxArray[1].width / 2) + boundingBoxArray[1].x)) / 2;
				averageArea = (boundingBoxArray[0].area() + boundingBoxArray[1].area()) / 2;
				averageContourWidth = (boundingBoxArray[0].width + boundingBoxArray[1].width) / 2;
				distFromTarget = RobotMath.gearWidthOfContoursToDistanceInFeet(averageContourWidth);
				angleOffCenter = RobotMath.gearAngleToTurnWithVisionProfiling(averageContourWidth, averageMidpoint);
				SmartDashboard.putNumber("Average Width: ", averageContourWidth);
				SmartDashboard.putNumber("Average Area: ", averageArea);
				SmartDashboard.putNumber("Average Midpoint: ", averageMidpoint);
			}

			synchronized (lockObject) {
				_gearCalculatedAngleFromMidpoint = angleOffCenter;
				_gearCalculatedDistanceFromTarget = distFromTarget;
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

}