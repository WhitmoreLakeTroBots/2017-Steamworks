package org.usfirst.frc.team3668.robot;

import org.usfirst.frc.team3668.robot.motionProfile.MotionProfiler;

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
		if (angle > 180) {
			return angle - 360;
		} else if (angle < -180) {
			return angle + 360;
		} else {
			return angle;
		}

		// double k = angle / 360.0;
		// double normalizedAngle = properModulus(k, 1) * 360;
		// return normalizedAngle;
		// if(normalizedAngle < 180){
		// return normalizedAngle;
		// }else {
		// return normalizedAngle-360;
		// }
	}

	public static double headingDelta(double currentHeading, double desiredHeading, double proportion) {
		double headingDelta = normalizeAngles(currentHeading - desiredHeading);
		double commandedTurnRate = headingDelta / proportion;
		return commandedTurnRate;
	}

	public static double headingDeltaTurn(double currentHeading, double desiredHeading) {
		return normalizeAngles(currentHeading - desiredHeading);
	}

	public static double turnLogisticFunction(double angle, double rate, double midpoint, double max,
			boolean useSignum) {
		double signum = Math.signum(angle);
		double abs = Math.abs(angle);
		double a = -rate * (abs - midpoint);
		double b = Math.pow(Math.E, a);
		if (useSignum) {
			return (max / (1 + b)) * signum;
		} else {
			return max / (1 + b);
		}
	}
	public static double turnExponentialFunction(double deltaHeadingFromInit){
		double abs = Math.abs(deltaHeadingFromInit);
		double z = -(abs+1.2);
//		double a = Math.pow(z, 5/2);
		double b = 1/z;
		return b+1;
	}
	public static boolean gyroAngleWithinMarginOfError(double currentHeading, double desiredHeading) {
		if (desiredHeading - currentHeading < Settings.chassisGyroTolerance) {
			return true;
		} else {
			return false;
		}
	}

	public static double frictionThrottle(double throttle, double deltaTime, MotionProfiler mp) {
		double deltaDist = mp.getTotalDistanceTraveled() - Math.abs(Robot.subChassis.getABSEncoderAvgDistInch());
		double frictionThrottleComp = deltaDist * Settings.profileThrottleDistanceProportion;
		double deltaDeltaTime = deltaTime - mp._stopTime;
		double timeThrottleComp = 0;
		if (Math.signum(deltaDeltaTime) == 1) {
			timeThrottleComp = deltaDeltaTime * Settings.profileThrottleTimeProportion;
		}
		throttle = throttle + frictionThrottleComp + timeThrottleComp;
		return throttle;
	}

	public static double timeThrottle(double throttle, double deltaTime, double startTime) {
		double deltaDeltaTime = deltaTime - startTime;
		double timeThrottleComp = 0;
		if (Math.signum(deltaDeltaTime) == 1) {
			timeThrottleComp = deltaDeltaTime * Settings.profileThrottleTimeProportion;
		}
		return throttle + timeThrottleComp;
	}

	public static double getTime() {
		return (System.nanoTime() / Math.pow(10, 9));
	}

	public static boolean withinDeadBand(double bandValue, double deadBandPercent, double currValue) {
		boolean inBand = false;
		double lowerLimit = bandValue * (1 - deadBandPercent);
		double upperLimit = bandValue * (1 + deadBandPercent);

		if (currValue >= lowerLimit && currValue <= upperLimit) {
			inBand = true;
		}

		return inBand;
	}
}