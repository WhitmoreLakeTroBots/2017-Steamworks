package org.usfirst.frc.team3668.robot;

public class Settings {

/////Chassis Settings
	
	public static final int chassisMotorRight1CanId = 1;
	
	public static final int chassisMotorRight2CanId = 4;

	public static final int chassisMotorLeft1CanId = 2;
	
	public static final int chassisMotorLeft2CanId = 3;
	
	public static final double chassisGearBoxRatio = 10.7/1; //motor rotations/ wheels rotations **MAIN ROBOT**
	
	public static final int chassisLeftEncoderDIOPortA = 0;
	
	public static final int chassisLeftEncoderDIOPortB = 1;
	
	public static final int chassisRightEncoderDIOPortA = 2;
	
	public static final int chassisRightEncoderDIOPortB = 3;
	
	public static final int chassisGyroAIOPort = 1;
	
	public static final int chassisGyroInitTime = 4;
	
	public static final double chassisGyroTolerance = 0.01;
	
	public static final double chassisGyroproportation = 0.05;
	
	public static final double chassisCmdDriveStraightWithGyroKp = 10;
	
	// ((Gear Box Output/Wheel Rotation) * (wheel diameter * PI))/tics per rotation
	public static final double chassisEncoderDistancePerPulse = (((39.0/42.0)*(6*Math.PI))/360.0); 	
	
	public static final double robotMaxInchesPerSecond = 115;
	
	public static final double chassisTurnSpeedModifier = 0.5;
/////Joystick Settings

	
	public static final int joyDrive = 0;
	public static final int joyArticulator = 1;
	public static final int joyArticulatorShooterButton = 1;
	public static final int joyDriveProfilerButton = 1; 


/////Shooter Settings
	
	public static final int shooterMotorRightCanId = 5;
	
	public static final int shooterMotorLeftCanId = 6;
	
	public static final double shooterTargetLinearVelocity = 26;
	
	public static final int shooterLeftEncoderDIOPortA = 5;
	
	public static final int shooterLeftEncoderDIOPortB = 6;
	
	public static final int shooterRightEncoderDIOPortA = 7;
	
	public static final int shooterRightEncoderDIOPortB = 8;
	
	public static final double shooterEncoderDistancePerPulse = ((45.0/14.0)*(4.0*Math.PI))/(1440.0*12.0);//Gear Ratio.  Circumference. PI. Number of Ticks per Rotation.  12... The number of inches in a foot you moron.
	
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
	
	public static final int joyArticulatorFeedButton = 12; //subject to change
	
	public static final double feederMotorSpeed = 1;
	
/////Autonomous Settings
	
	public static final int  autoCenterHeadingDegrees = 0;
	
	public static final int autoCenterInchesPerSecond = -63;
	
	public static final int autoCenterInches = 93;
	
	public static final int  autoLeftGearStep1HeadingDegrees = 0; 
	
	public static final int autoLeftGearInchesPerSecond = -63;
	
	public static final double autoLeftGearStep1Inches = 93;
	
	public static final double autoLeftGearTurnDegrees = 45;
	
	public static final int  autoLeftGearStep2HeadingDegrees = 45; 
	
	public static final double autoLeftGearStep2Inches = 32;
	
	public static final double autoLeftGearStep3Inches = 22;

	public static final int  autoLeftHopperStep1HeadingDegrees = 0; 
	
	public static final int autoLeftHopperInchesPerSecond = 63;
	
	public static final double autoLeftHopperStep1Inches = 191;
	
	public static final double autoHopperTurnDegrees = 90;
	
	public static final double autoRightGearTurnDegrees = -45;
	
	public static final int  autoRightGearStep2HeadingDegrees = -45; 

	

/////Sweeper Settings
	
	public static final int sweeperMotorCanId = 9;
	
	public static final double sweeperMotorSpeed = 1;
	
	public static final double sweeperMotorReverseSpeed = -0.75;
	
	public static final int joyArticulatorSweepButtonIn = 3;
	
	public static final int joyArticulatorSweepButtonOut = 5;
	
/////Profiler Settings
	public static final double profileDistancedDeadband = 1;
	public static final double profileTestDistance = 72;
	public static final double profileTestDistanceSeg2 = -1*profileTestDistance;
	public static final double profileTestCruiseSpeed = robotMaxInchesPerSecond * 0.75;
	public static final double profileTestTurnDregees = 90;
	public static final double profileTestTurnCruiseSpeed = robotMaxInchesPerSecond * 0.75;
	public static final double profileTestRobotCirDia = 27.5; //30 ON REAL ROBOT
	public static final double profileDriveAccelration = 35;
	public static final double profileThrottleDistanceProportion = 0.08;
	public static final double profileThrottleTimeProportion = 0.2;
	public static final double profileRobotThrottleThreshold = 0.3;
	public static final double profileInitVelocity = 0;
	public static final String profileLogName = "logs\\motionProfile";
	public static final String profileTestLogName = "logs\\motionProfileTestResults";
	public static final String profileLogLogName = "logTest";
	public static final String profileLogFileExtension = ".txt";
	
	//public static enum TurnType{
	//	pointL,pointR,SwingL,SwingR
	//}

}
