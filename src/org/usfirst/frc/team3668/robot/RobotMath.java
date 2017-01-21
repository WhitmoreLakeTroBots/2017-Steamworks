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
		double k = angle / 360.0;
		return properModulus(k, 1) * 360;
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
