package org.usfirst.frc.team3668.robot.visionProcessing;


public class VisionMath {
	public static double boilerWidthOfContoursToDistanceInFeet(double averageWidthOfContours) {
		//Need to recreate exponential function for area, not distance, and also use accurate readings from both real vision targets and actual camera position on robot.
		return 2.544834 + 77.97764 * Math.pow(Math.E, -0.03725993 * (averageWidthOfContours)) * 12;
	}
	public static double boilerAngleToTurnWithVisionProfiling(double averageWidthOfContours, double midpointOfContour){
		double pixelsPerInch = averageWidthOfContours/VisionSettings.boilerVisionTargetWidth;
		double distanceFromCenter = Math.abs(midpointOfContour - VisionSettings.visionImageCenterXPixels);
		double distanceSignum = Math.signum(midpointOfContour - VisionSettings.visionImageCenterXPixels);
		double oppositeSideLength = distanceFromCenter / pixelsPerInch;
		double adjacentSideLength = boilerWidthOfContoursToDistanceInFeet(averageWidthOfContours);
		double angle = (Math.atan(oppositeSideLength / adjacentSideLength)) * 180 / Math.PI;
		return angle * distanceSignum;
	}
	public static double gearWidthOfContoursToDistanceInFeet(double averageWidthOfContours){
		//Need to create different equation to predict distance. Also change to use area instead of just width.
//		return 2.544834 + 77.97764 * Math.pow(Math.E, -0.03725993*(averageWidthOfContours));
//		return (10.9766 * Math.pow(0.967492, averageWidthOfContours)) * 12;
		return ((26.479 * (Math.pow(2, -0.16051 * (averageWidthOfContours-1.5039)))) + 2.294) * 12;
	}
	public static double gearAngleToTurnWithVisionProfiling(double averageWidthOfContours, double midpointOfContour){
		double pixelsPerInch = averageWidthOfContours/VisionSettings.gearVisionTargetWidth;
		double distanceFromCenter = Math.abs(midpointOfContour - VisionSettings.visionImageCenterXPixels);
		double distanceSignum = Math.signum(midpointOfContour - VisionSettings.visionImageCenterXPixels);
		double oppositeSideLength = distanceFromCenter / pixelsPerInch;
		double adjacentSideLength = gearWidthOfContoursToDistanceInFeet(averageWidthOfContours);
		double angle = (Math.atan(oppositeSideLength/adjacentSideLength))*180/Math.PI;
		return angle*distanceSignum;
	}

}
