package org.usfirst.frc.team3668.robot.visionProcessing;

import java.util.Comparator;

import org.opencv.core.MatOfPoint;
import org.opencv.imgproc.Imgproc;

public class ContourComparator implements Comparator<MatOfPoint> {

	@Override
	public int compare(MatOfPoint o1, MatOfPoint o2) {
		return Imgproc.contourArea(o1) > Imgproc.contourArea(o2) ? -1 : Imgproc.contourArea(o1) > Imgproc.contourArea(o2) ? 0 : 1;
	}

}
