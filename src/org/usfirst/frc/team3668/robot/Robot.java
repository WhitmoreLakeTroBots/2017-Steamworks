
package org.usfirst.frc.team3668.robot;

import org.usfirst.frc.team3668.robot.Settings.action;
import org.usfirst.frc.team3668.robot.Settings.colors;
import org.usfirst.frc.team3668.robot.commands.CmdAutoShooter;
import org.usfirst.frc.team3668.robot.commands.CmdAutoTurnWithGyroWithVision;
import org.usfirst.frc.team3668.robot.commands.CmdBothAlignToBoiler;
import org.usfirst.frc.team3668.robot.commands.CmdBothDriveWithProfile;
import org.usfirst.frc.team3668.robot.commands.CmdBothDriveWithProfileAndGyro;
import org.usfirst.frc.team3668.robot.commands.CmdBothTurnWithProfile;
import org.usfirst.frc.team3668.robot.commands.CmdSwitchActiveCameraForVisionProcessing;
import org.usfirst.frc.team3668.robot.commands.CmdTeleopJoystickDrive;
import org.usfirst.frc.team3668.robot.commands.CmdTurnWithGyro;
import org.usfirst.frc.team3668.robot.commands.commandGroups.CmdGroupAutoCenter;
import org.usfirst.frc.team3668.robot.commands.commandGroups.CmdGroupAutoLeftGear;
import org.usfirst.frc.team3668.robot.commands.commandGroups.CmdGroupAutoRightGear;
import org.usfirst.frc.team3668.robot.commands.commandGroups.CmdGroupAutoShootFromKey;
import org.usfirst.frc.team3668.robot.subsystems.SubChassis;
import org.usfirst.frc.team3668.robot.subsystems.SubClimber;
import org.usfirst.frc.team3668.robot.subsystems.SubFeeder;
import org.usfirst.frc.team3668.robot.subsystems.SubShooter;
import org.usfirst.frc.team3668.robot.subsystems.SubSweeper;
import org.usfirst.frc.team3668.robot.visionProcessing.VisionProcessing;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// YOYOYO
public class Robot extends IterativeRobot {

	//private BoilerVisionProcessing boilerVisionProcessing = new BoilerVisionProcessing();
	//private GearVisionProcessing gearVisionProcessing = new GearVisionProcessing();
	private VisionProcessing visionProcessing = new VisionProcessing();
	public static final SubChassis subChassis = new SubChassis();
	public static final SubShooter subShooter = new SubShooter();
	public static final SubClimber subClimber = new SubClimber();
	public static final SubSweeper subSweeper = new SubSweeper();
	public static final SubFeeder subFeeder = new SubFeeder();
	public static boolean isDriveInverted = true;
	public static boolean cameraReversed = false; 
	public static OI oi;

	public static UsbCamera DashCamera;
	public static UsbCamera Camera2;
	Command autonomousCommand;
	Command teleopCommand = new CmdTeleopJoystickDrive();
	SendableChooser<action> autoChooser = new SendableChooser<>();
	SendableChooser<colors> autoColorChooser = new SendableChooser<>();
	Thread visionThread;
	CvSink cvSink;
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		oi = new OI();
//		Camera2 =  CameraServer.getInstance().startAutomaticCapture("Main Camera", 1);
//		DashCamera = CameraServer.getInstance().startAutomaticCapture("Schwennesen Camera", 0);
		autoColorChooser.addObject("Blue", colors.Blue);
		autoColorChooser.addObject("Red", colors.Red);
		SmartDashboard.putData("Color Chooser", autoColorChooser);
		
		autoChooser.addDefault("AUTO Center Gear Only", action.centerGear);
		autoChooser.addObject("AUTO Left Gear", action.leftGear);
		autoChooser.addObject("AUTO Right Gear", action.rightGear);
		autoChooser.addObject("AUTO Shoot From Key", action.key);
		autoChooser.addObject("AUTO Shoot Only", action.shootOnly);
		autoChooser.addObject("AUTO DO NOTHING; BE A FAILURE", action.NOTHING);
		SmartDashboard.putData("Action Chooser", autoChooser);
		// chooser.addDefault("Default Auto", new ExampleCommand());
		// chooser.addObject("My Auto", new MyAutoCommand());

		SmartDashboard.putData("TEST GYRO AND PROFILE FORWARDS",
				new CmdBothDriveWithProfileAndGyro(0, Settings.profileTestCruiseSpeed, Settings.profileTestDistance));
		
		SmartDashboard.putData("TURN WITH PROFILE: 90 DEGREES", new CmdBothTurnWithProfile(90, Settings.profileTestTurnCruiseSpeed));
		SmartDashboard.putData("TURN WITH PROFILE: -90 DEGREES", new CmdBothTurnWithProfile(-90, Settings.profileTestTurnCruiseSpeed));
		SmartDashboard.putData("TURN WITH PROFILE: 180 DEGREES",new CmdBothTurnWithProfile(180, Settings.profileTestTurnCruiseSpeed));
		SmartDashboard.putData("TURN WITH PROFILE: 0 DEGREES", new CmdBothTurnWithProfile(0, Settings.profileTestTurnCruiseSpeed));
	
	
		
		SmartDashboard.putData("TURN WITH DRIVE PROFILE: 90 DEGREES", new CmdBothDriveWithProfile(21.5984494934, Settings.profileTestTurnCruiseSpeed));
		SmartDashboard.putData("TURN WITH DRIVE PROFILE: 180 DEGREES", new CmdBothDriveWithProfile(43.1968998685,Settings.profileTestTurnCruiseSpeed));
		
		SmartDashboard.putData("Turn With Jerry-Rigged Turn Code? 90?", new CmdTurnWithGyro(90));
		SmartDashboard.putData("Turn With Jerry-Rigged Turn Code? 180?", new CmdTurnWithGyro(180));
		SmartDashboard.putData("Turn With Jerry-Rigged Turn Code? 270?", new CmdTurnWithGyro(270));
		SmartDashboard.putData("Test Vision Boiler Command: ", new CmdAutoTurnWithGyroWithVision());
//		boilerVisionProcessing.start();
//		gearVisionProcessing.start();
//		SmartDashboard.getNumber("Desired Shoot Speed (feet/sec): ", 0);

		//SmartDashboard.putData("CmdDriveByGyro2", new CmdDriveStraightWithGyro(-180, -80, -72));
		SmartDashboard.putData("TEST GYRO AND PROFILE BACKWARDS",
				new CmdBothDriveWithProfileAndGyro(0, Settings.profileTestCruiseSpeed, Settings.profileTestDistanceSeg2));
		SmartDashboard.putData("Test Vision Boiler: ", new CmdBothAlignToBoiler());
		// SmartDashboard.putData("Auto mode", autoChooser);
		RobotMap.Init();

//		visionThread = new Thread(() -> {
			
			// Get the UsbCamera from CameraServer
//			UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
//			// Set the resolution
//			camera.setResolution(640, 480);
//			camera.setExposureManual(25);
//			camera.setBrightness(55);

			// Get a CvSink. This will capture Mats from the camera
//			cvSink = CameraServer.getInstance().getVideo();
//		});
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */

	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
		visionProcessing.stop();

	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {

		action selectedAction = (action) autoChooser.getSelected();
		colors selectedColor = (colors) autoColorChooser.getSelected();
		
		switch(selectedAction){
		case centerGear:
			autonomousCommand = new CmdGroupAutoCenter();
			break;
		case leftGear:
			autonomousCommand = new CmdGroupAutoLeftGear(selectedColor);
			break;
		case rightGear:
			autonomousCommand = new CmdGroupAutoRightGear(selectedColor);
			break;
		case key:
			autonomousCommand = new CmdGroupAutoShootFromKey(selectedColor);
			break;
		case shootOnly:
			autonomousCommand = new CmdAutoShooter(Settings.shooterTargetLinearVelocity);
		case NOTHING:
			autonomousCommand = null;
		}

		if (teleopCommand != null) {
			teleopCommand.cancel();
		}
		if (autonomousCommand != null) {
			autonomousCommand.start();
			System.out.println("auto command started");
		}
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (teleopCommand != null) {
			teleopCommand.start();
		}
		if (autonomousCommand != null) {
			autonomousCommand.cancel();
		}
		visionProcessing.start();		
		SmartDashboard.putData("Switch Camera Processing: ", new CmdSwitchActiveCameraForVisionProcessing());
	}
	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		SmartDashboard.putNumber("Distance from target(gear): ", VisionProcessing.getGearCalculatedDistanceFromTarget());
		SmartDashboard.putNumber("Angle from target(gear): ", VisionProcessing.getGearCalculatedAngleFromTarget());
		SmartDashboard.putNumber("Distance from target(boiler): ", VisionProcessing.getBoilerCalculatedDistanceFromTarget());
		SmartDashboard.putNumber("Angle from target(boiler): ", VisionProcessing.getBoilerCalculatedAngleFromMidpoint());
		if(VisionProcessing.getCurrentCamera() != null){
			SmartDashboard.putString("CurrentCamera: ", VisionProcessing.getCurrentCamera().toString());
			SmartDashboard.putNumber("Camera Value: ", (int)VisionProcessing.getCurrentCamera().ordinal());
		}
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {

		LiveWindow.run();
	}
	
}
