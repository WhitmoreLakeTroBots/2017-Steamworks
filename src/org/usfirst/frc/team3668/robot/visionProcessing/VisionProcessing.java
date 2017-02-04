package org.usfirst.frc.team3668.robot.visionProcessing;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team3668.robot.Settings;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;

public class VisionProcessing {
	
	Thread visionThread;
	
public void start(){
	
	GripPipeline gripPipeline = new GripPipeline();
	visionThread = new Thread(() -> {
		// Get the UsbCamera from CameraServer
		UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
		// Set the resolution
		camera.setResolution(Settings.visionImageWidthPixels, Settings.visionImageHeightPixels);
		camera.setExposureManual(25);
		camera.setBrightness(55);

		// Get a CvSink. This will capture Mats from the camera
		CvSink cvSink = CameraServer.getInstance().getVideo();
		// Setup a CvSource. This will send images back to the Dashboard
		CvSource outputStream = CameraServer.getInstance().putVideo("VisionProcessing", Settings.visionImageWidthPixels, Settings.visionImageHeightPixels);

		// Mats are very memory expensive. Let's reuse this Mat.
		Mat mat = new Mat();
//		boolean isImgWritten = false;
//		int imgCounter = 1;

		// This cannot be 'true'. The program will never exit if it is. This
		// lets the robot stop this thread when restarting robot code or
		// deploying.
		while (!Thread.interrupted()) {
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			// Tell the CvSink to grab a frame from the camera and put it
			// in the source mat. If there is an error notify the output.
			if (cvSink.grabFrame(mat) == 0) {
				// Send the output the error.
				outputStream.notifyError(cvSink.getError());
				// skip the rest of the current iteration
				continue;
			}

//			if (isImgWritten == false) {
//				Imgcodecs.imwrite("/media/sda1/before" + imgCounter + ".jpeg", mat);
//			}

			gripPipeline.process(mat);

			if (!gripPipeline.filterContoursOutput().isEmpty()) {

				for (MatOfPoint contour : gripPipeline.filterContoursOutput()) {

					Rect boundingBox = Imgproc.boundingRect(contour);
					Imgproc.rectangle(mat, new Point(boundingBox.x - 2, boundingBox.y - 2),
							new Point(boundingBox.x + boundingBox.width + 2,
									boundingBox.y + boundingBox.height + 2),
							new Scalar(255, 255, 255), 2);
					double boxCenterOffsetX = ((boundingBox.width / 2) + boundingBox.x)- Settings.visionImageCenterXPixels;
					
				}

				
			}
//			if (isImgWritten == false) {
//				Imgcodecs.imwrite("/media/sda1/after" + imgCounter + ".jpeg", mat);
//				imgCounter++;
//			}
//			if (imgCounter > 10) {
//				isImgWritten = true;
//			}

			// Give the output stream a new image to display
			outputStream.putFrame(mat);
		}
	});
	
	visionThread.setDaemon(true);
	visionThread.start();	
}
public void stop(){
	visionThread.interrupt();
	visionThread = null;
}
}
