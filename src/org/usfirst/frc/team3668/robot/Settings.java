package org.usfirst.frc.team3668.robot;

public class Settings {

/////Chassis Settings
	
	public static final int chassisMotorRight1CanId = 1;
	
	public static final int chassisMotorRight2CanId = 4;

	public static final int chassisMotorLeft1CanId = 2;
	
	public static final int chassisMotorLeft2CanId = 3;
	
	public static final double chassisGearRatio = 10.7/1; //motor revs/ wheels revs **MAIN ROBOT**
	
	public static final int chassisLeftEncoderDIOPortA = 1;
	
	public static final int chassisLeftEncoderDIOPortB = 2;
	
	public static final int chassisRightEncoderDIOPortA = 3;
	
	public static final int chassisRightEncoderDIOPortB = 4;
	
	//public static final double chassisEncoderDistancePerPulse = MATH;
	
/////Joystick Settings
	
	public static final int joyDrive = 0;
	public static final int joyArticulator = 1;
	public static final int joyArticulatorShooterButton = 1;


/////Shooter Settings
	
	public static final int shooterMotorRightCanId = 5;
	
	public static final int shooterMotorLeftCanId = 6;
	
	public static final double shooterTargetLinearVelocity = 26;
	
	public static final int shooterLeftEncoderDIOPortA = 5;
	
	public static final int shooterLeftEncoderDIOPortB = 6;
	
	public static final int shooterRightEncoderDIOPortA = 7;
	
	public static final int shooterRightEncoderDIOPortB = 8;
	
	public static final double shooterEncoderDistancePerPulse = ((45/14)*(4*Math.PI))/(1440*12);//Gear Ratio.  Circumference. PI. Number of Ticks per Rotation.  12... The number of inches in a foot you moron.
	
	public static final double shooterMotorSpeedMarginOfError = .9;
	
	public static final double shooterMotorMaxVoltagePercent = 1;

	public static final double shooterMotorReducedVoltagePercent = .5;
/////Climber Settings
	
	public static final int climberMotor1CanId = 7;
	
	public static final int climberMotor2CanId = 8;
	
	public static final double climberMotorSpeed = 1;
	
	public static final int joyArticulatorClimbButton = 6;
	
/////Feeder Settings
	
	public static final int feederMotorCanId = 10;
	
	public static final double feederMotorSpeed = 1;
/////Sweeper Settings
	
	public static final int sweeperMotorCanId = 9;
	
	public static final double sweeperMotorSpeed = 1;
	
	public static final double sweeperMotorReverseSpeed = -0.75;
	
	public static final int joyArticulatorSweepButtonIn = 3;
	
	public static final int joyArticulatorSweepButtonOut = 5;

}
