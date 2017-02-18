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

	public void start() {

		BoilerGripPipeline boilerGripPipeline = new BoilerGripPipeline();
		visionThread = new Thread(() -> {
			// Get the UsbCamera from CameraServer
			UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
			// Set the resolution
			camera.setResolution(Settings.visionImageWidthPixels, Settings.visionImageHeightPixels);
			camera.setExposureManual(25);
			camera.setBrightness(0);
			int totalContourWidth = 0;
			int imgCounter = 0;
			double averageMidpoint = 0;
			// Get a CvSink. This will capture Mats from the camera
			CvSink cvSink = CameraServer.getInstance().getVideo();
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
				if (cvSink.grabFrame(mat) == 0) {
					// Send the output the error.
					// skip the rest of the current iteration
					continue;
				}

				boilerGripPipeline.process(mat);
				if (!boilerGripPipeline.filterContoursOutput().isEmpty()) {
					for (MatOfPoint contour : boilerGripPipeline.filterContoursOutput()) {
						Rect boundingBox = Imgproc.boundingRect(contour);
						double boxMidpoint = ((boundingBox.width / 2) + boundingBox.x);
						averageMidpoint = averageMidpoint  +boxMidpoint;
						totalContourWidth = totalContourWidth + boundingBox.width;
						imgCounter++;
					}
					SmartDashboard.putNumber("Calculated Angle From Center: ", RobotMath.angleToTurnWithVisionProfiling(totalContourWidth/imgCounter, averageMidpoint));
					SmartDashboard.putNumber("Calculated Distance From Target: ", RobotMath.widthOfContoursToDistanceInFeet(totalContourWidth/imgCounter));
				}
				if (mat != null) {
					mat.release();
				}
				totalContourWidth = 0;
				imgCounter = 0;
				averageMidpoint = 0;
			}
		});

		visionThread.setDaemon(true);
		visionThread.start();
	}

	public void stop() {
		visionThread.interrupt();
		visionThread = null;
	}
}
