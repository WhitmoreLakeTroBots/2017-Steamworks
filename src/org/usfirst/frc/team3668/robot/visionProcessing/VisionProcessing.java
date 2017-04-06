package org.usfirst.frc.team3668.robot.visionProcessing;

import java.util.Arrays;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team3668.robot.RobotMath;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;

public class VisionProcessing {

	Thread visionThread;
	private static boolean _resetCamera;
	private static boolean _takePicture;
	private static int _photoDistance;
	private static int _cameraExposureValue = Settings.cameraExposure;
	private static Object lockObject = new Object();
	private static Settings.cameraName _switchActiveCamera = Settings.cameraName.noProcess;
	private static double averageMidpoint = 0;
	private static double averageContourWidth = 0;
	private static double distFromTarget = 0;
	private static double angleOffCenter = 0;
	private static int imgCounter = 0;
	private static boolean foundGearTarget;
	private static Mat mat = new Mat();
	private static GearGripPipeline gearGripPipeline = new GearGripPipeline();
	private static CvSink cvSink = null;
	private static VisionData _visionData = new VisionData();
	private UsbCamera usbCamera = null;

	public void start() {
		System.err.println("Vision Processing Started");
		visionThread = new Thread(() -> {
			usbCamera = CameraServer.getInstance().startAutomaticCapture(0);
			usbCamera.setResolution(Settings.visionImageWidthPixels, Settings.visionImageHeightPixels);
			cvSink = CameraServer.getInstance().getVideo(usbCamera);
			requestCameraReset(_cameraExposureValue, _photoDistance);
			while (!Thread.interrupted()) {
				if (_resetCamera) {
					usbCamera.setExposureManual(_cameraExposureValue);
					cvSink.grabFrame(mat);
					resetCameraComplete();
				}
				processGearCamera();
				if (_takePicture) {
					Imgcodecs.imwrite("/media/sda1/image" + imgCounter + "-" + _cameraExposureValue + "-dist-"
							+ _photoDistance + ".jpeg", mat);
					setTakePicture(false);
				}

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
		System.err.println("Stopping Vision");
		if (visionThread != null) {
			visionThread.interrupt();
			visionThread = null;
		}
		usbCamera = null;
	}

	private static void processGearCamera() {
		MatOfPoint[] contourArray;
		int contourCounter = 0;
		angleOffCenter = 0;
		distFromTarget = 0;
		foundGearTarget = false;
		if (cvSink.grabFrame(mat) != 0) {
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
					averageContourWidth = (upperTargetBoundingBox.width + lowerTargetBoundingBox.width) / 2;
					distFromTarget = VisionMath.gearWidthOfContoursToDistanceInFeet(averageContourWidth);
					angleOffCenter = VisionMath.gearAngleToTurnWithVisionProfiling(averageContourWidth,
							averageMidpoint);
					foundGearTarget = (Math.abs(1 - (upperTargetArea / lowerTargetArea)) < 0.2);
					imgCounter++;
				}
			}
			setVisionData(RobotMath.getTime(), distFromTarget, angleOffCenter, foundGearTarget);
		}
	}

	public static void setSwitchCameraValue(Settings.cameraName cameraNumber) {
		synchronized (lockObject) {
			_switchActiveCamera = cameraNumber;
		}
	}

	public static void setTakePicture(boolean takePicture) {
		synchronized (lockObject) {
			_takePicture = takePicture;
		}
	}

	public static Settings.cameraName getCurrentCamera() {
		Settings.cameraName switchValue;
		synchronized (lockObject) {
			switchValue = _switchActiveCamera;
		}
		return switchValue;
	}

	public static void requestCameraReset(int exposure, int distance) {
		synchronized (lockObject) {
			_resetCamera = true;
			_cameraExposureValue = exposure;
			_photoDistance = distance;
		}
	}

	private static void resetCameraComplete() {
		synchronized (lockObject) {
			_resetCamera = false;
		}
	}

	private static void setVisionData(double timeTaken, double distToTarget, double angleToTarget,
			boolean foundTarget) {
		synchronized (lockObject) {
			_visionData = new VisionData();
			_visionData.lastWriteTime = timeTaken;
			_visionData.distToTarget = distToTarget;
			_visionData.angleToTarget = angleToTarget;
			_visionData.foundTarget = foundTarget;
		}
	}

	public static VisionData getVisionData() {
		synchronized (lockObject){
			return _visionData;
		}
	}
}