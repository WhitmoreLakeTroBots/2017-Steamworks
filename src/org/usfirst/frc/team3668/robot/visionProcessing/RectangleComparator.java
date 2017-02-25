package org.usfirst.frc.team3668.robot.visionProcessing;

import java.util.Comparator;

import org.opencv.core.Rect;

public class RectangleComparator implements Comparator<Rect> {

	@Override
	public int compare(Rect o1, Rect o2) {
		return o1.area() > o2.area() ? -1 : o1.area() > o2.area() ? 0 : 1;
	}

}
