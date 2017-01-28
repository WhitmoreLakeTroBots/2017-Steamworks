package org.usfirst.frc.team3668.robot;

public class RobotMath {
	public static double properModulus(double quotient, double divisor) {
		double j = quotient % divisor;
		if (Math.signum(j) == -1) {
			return j = j + divisor;
		} else {
			return j;
		}
	}

	public static double normalizeAngles(double angle) {
		if(angle > 180){
			return angle -360;
		} else if (angle < -180){
			return angle + 360;
		} else {
			return angle;
		}
		
//		double k = angle / 360.0;
//		double normalizedAngle = properModulus(k, 1) * 360;
//		return normalizedAngle;
//		if(normalizedAngle < 180){
//			return normalizedAngle;
//		}else {
//			return normalizedAngle-360;
//		}
	}
	public static double headingDelta(double currentHeading, double desiredHeading){
		double headingDelta = normalizeAngles(currentHeading - desiredHeading);
		double commandedTurnRate = headingDelta / Settings.chassisCmdDriveStraightWithGyroKp;
		return commandedTurnRate;
	}
	public static boolean gyroAngleWithinMarginOfError(double currentHeading, double desiredHeading) {
		if (currentHeading > normalizeAngles(desiredHeading - Settings.chassisGyroTolerance)
				&& currentHeading < normalizeAngles(desiredHeading + Settings.chassisGyroTolerance)) {
			return true;
		} else {
			return false;
		}
	}
}
