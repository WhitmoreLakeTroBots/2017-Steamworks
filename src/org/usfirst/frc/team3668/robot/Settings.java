package org.usfirst.frc.team3668.robot;

public class Settings {

/////Chassis Settings
	
	public static final int chassisMotorRight1CanId = 4;
	
	public static final int chassisMotorRight2CanId = 3;

	public static final int chassisMotorLeft1CanId = 1;
	
	public static final int chassisMotorLeft2CanId = 2;
	
	public static final double chassisGearBoxRatio = 10.7/1; //motor rotations/ wheels rotations **MAIN ROBOT**
	
	public static final int chassisLeftEncoderDIOPortA = 0;
	
	public static final int chassisLeftEncoderDIOPortB = 1;
	
	public static final int chassisRightEncoderDIOPortA = 2;
	
	public static final int chassisRightEncoderDIOPortB = 3;
	
	public static final int chassisGyroAIOPort = 1;
	
	public static final int chassisGyroInitTime = 4;
	
	public static final double chassisGyroTolerance = 0.08;
	
	public static final double chassisGyroproportation = 0.01;
	
	public static final double chassisCmdDriveStraightWithGyroKp = 10;
	
	// ((Gear Box Output/Wheel Rotation) * (wheel diameter * PI))/tics per rotation
	public static final double chassisEncoderDistancePerPulse = (((39.0/42.0)*(6*Math.PI))/360.0); 	
	
	public static final double robotMaxInchesPerSecond = 115;
	
	public static final double chassisTurnSpeedModifier = 0.35;
	
	public static final double chassisTurnLogisticFunctionRate = 0.037;
	
	public static final double chassisTurnLogisticFunctionMidpoint = 11;
	
	public static final double chassisTurnLogisticFunctionMax = 0.8;
	
	public static final double chassisTurnLogisticStartupFunctionRate = 0.1;
	
	public static final double chassisTurnLogisticStartupFunctionMidpoint = 8.2;
	
	public static final double chassisTurnLogisticStartupFunctionMax = 1;
	
	public static final double chassisTurnValueMinimum = 0.5;
/////Joystick Settings

	
	public static final int joyDrive = 0;
	public static final int joyArticulator = 1;
	public static final int joyArticulatorShooterButton = 7;
	public static final int joyArticulatorClimbButton = 6;
	public static final int joyArticulatorSweepButtonIn = 3;
	public static final int joyArticulatorSweepButtonOut = 5;
	public static final int joyArticulatorFireShooterButton =1;  
	
	
/////Shooter Settings
	
	public static final int shooterMotorRightCanId = 7;
	
	public static final int shooterMotorLeftCanId = 9;
	
	public static final double shooterTargetLinearVelocity = 26;
	
	public static final int shooterLeftEncoderDIOPortA = 5;
	
	public static final int shooterLeftEncoderDIOPortB = 6;
	
	public static final int shooterRightEncoderDIOPortA = 7;
	
	public static final int shooterRightEncoderDIOPortB = 8;
	
	public static final double shooterEncoderDistancePerPulse = ((45.0/14.0)*(4.0*Math.PI))/(360.0*12.0);//Gear Ratio.  Circumference. PI. Number of Ticks per Rotation.  12... The number of inches in a foot you moron.
	
	public static final double shooterMotorSpeedWindowLowerPercentage = .9;
	
	public static final double shooterMotorSpeedWindowUpperPercentage = 1.1;
	
	public static final double shooterProprotation = 0.1;
	
	public static final double shooterDeadBandPercent = 0.03;
	
	public static final double shooterMotorReducedRate = 0.5;
		
	public static final double shooterMotorMaxSpeed = 32.0;//feet per second
	
	public static final double shooterReverseSpeed = -0.5;
/////Climber Settings
	
	public static final int climberMotor1CanId = 5;
	
	public static final int climberMotor2CanId = 6;
	
	public static final double climberMotorSpeed = 1;
	
/////Feeder Settings
	
	public static final int feederMotorCanId = 10;
		
	public static final double feederMotorSpeed = 1;
	
/////Autonomous Settings
	//Gears
	
	public static final int  autoCenterHeadingDegrees = 0;
	
	public static final int autoInchesPerSecond = -63;
	
	public static final int autoCenterInches2Baseline = 93;
	
	public static final double autoMoveInchesPerSecond = 63;
	
	public static final double autoInchesToBaseline = 93;
	
	public static final double autoLeftGearTurnDegrees = 45;
	
	public static final int  autoLeftGearStep2HeadingDegrees = 45; 
	
	public static final double autoLeftGearStep2Inches = 32;
	
	public static final double autoInchesLift2Boiler = 168.2;
	
	public static final double autoRightGearTurnDegrees = -45;
	
	public static final int  autoRightGearStep2HeadingDegrees = -45; 

	//Hopper

	public static final int  autoLeftHopperStep1HeadingDegrees = 0; 
	
	public static final int autoLeftHopperInchesPerSecond = 63;
	
	public static final double autoLeftHopperStep1Inches = 191;
	
	public static final double autoLeftHopperTurn1Degrees = -90;
		
	public static final double autoLeftHopperStep2Inches = 24;
	
	public static final int  autoLeftHopperStep2HeadingDegrees = 90; 
	
	public static final int  autoLeftHopperStep3HeadingDegrees = -90; //ASK ABOUt THIS IS IT NEGATIVE?

	public static final double autoLeftHopperStep3Inches = -365; //entire width of the field minus 20 inches for room to shoot/turn

	public static final double autoHopperTurn2Degrees = -90;

	public static final int  autoLeftHopperStep4HeadingDegrees = 0; //ASK ABOUt THIS IS IT NEGATIVE?

	public static final double autoLeftHopperStep4Inches = 175; //entire width of the field minus 20 inches for room to shoot/turn
	//Shooting
	public static final double autoKeyLineDistance2Shoot = 65;
	
	public static final double autoShootHeadingFromKey = -90;
	
	public static final double autoShooterTime = 5;


	
	public static enum colors{
		Red,Blue
		
	}
	public static enum action {
		centerGear,leftGear, rightGear,key, shootOnly, NOTHING
		
	
	}


	

/////Sweeper Settings
	
	public static final int sweeperMotorCanId = 8;
	
	public static final double sweeperMotorSpeed = -0.75;
	
	public static final double sweeperMotorReverseSpeed = -1 * sweeperMotorSpeed;
	
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
	public static final double profileThrottleTimeProportion = 0.4;
	public static final double profileRobotThrottleThreshold = 0.3;
	public static final double profileInitVelocity = 0;
	public static final String profileLogName = "//media//sda1//motionProfile";
	public static final String profileTestLogName = "logs\\motionProfileTestResults";
	public static final String profileLogLogName = "logTest";
	public static final String profileLogFileExtension = ".txt";
	
/////Vision
	
	public static final int visionImageWidthPixels = 640; 
	
	public static final int visionImageHeightPixels = 480;

	public static final int visionImageCenterXPixels = visionImageWidthPixels/2;
	
	public static final int visionImageCenterYPixels = visionImageHeightPixels/2;
	
	public static final double visionTargetWidth = 16/12; // In Feet

	//public static enum TurnType{
	//	pointL,pointR,SwingL,SwingR
	//}

}
