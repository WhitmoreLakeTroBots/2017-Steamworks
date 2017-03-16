
package org.usfirst.frc.team3668.robot;

import org.opencv.core.Mat;
import org.usfirst.frc.team3668.robot.Settings.action;
import org.usfirst.frc.team3668.robot.Settings.colors;
import org.usfirst.frc.team3668.robot.commands.CmdAutoShooter;
import org.usfirst.frc.team3668.robot.commands.CmdBothAlignToBoiler;
import org.usfirst.frc.team3668.robot.commands.CmdBothDriveWithProfile;
import org.usfirst.frc.team3668.robot.commands.CmdBothDriveWithProfileAndGyro;
import org.usfirst.frc.team3668.robot.commands.CmdBothTurnWithProfile;
import org.usfirst.frc.team3668.robot.commands.CmdCameraManganer;
import org.usfirst.frc.team3668.robot.commands.CmdTeleopJoystickDrive;
import org.usfirst.frc.team3668.robot.commands.commandGroups.CmdGroupAutoCenter;
import org.usfirst.frc.team3668.robot.commands.commandGroups.CmdGroupAutoLeftGear;
import org.usfirst.frc.team3668.robot.commands.commandGroups.CmdGroupAutoRightGear;
import org.usfirst.frc.team3668.robot.commands.commandGroups.CmdGroupAutoShootFromKey;
import org.usfirst.frc.team3668.robot.subsystems.SubChassis;
import org.usfirst.frc.team3668.robot.subsystems.SubClimber;
import org.usfirst.frc.team3668.robot.subsystems.SubFeeder;
import org.usfirst.frc.team3668.robot.subsystems.SubShooter;
import org.usfirst.frc.team3668.robot.subsystems.SubSweeper;
import org.usfirst.frc.team3668.robot.subsystems.SubCamera;
//import org.usfirst.frc.team3668.robot.visionProcessing.BoilerVisionProcessing;
//import org.usfirst.frc.team3668.robot.visionProcessing.GearVisionProcessing;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
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
	public static final SubChassis subChassis = new SubChassis();
	public static final SubCamera subCamera = new SubCamera();
	public static final SubShooter subShooter = new SubShooter(Settings.shooterControllerKp,
			Settings.shooterControllerKi, Settings.shooterControllerKd, Settings.shooterControllerKf);
	public static final SubClimber subClimber = new SubClimber();
	public static final SubSweeper subSweeper = new SubSweeper();
	public static final SubFeeder subFeeder = new SubFeeder();
	public static boolean isDriveInverted = true;
	public static boolean isFrontCamera = true;
	public static OI oi;
	public static UsbCamera frontCam;
	public static UsbCamera backCam;
	public static CvSink cvSinkCamera;
	public static CvSource cvCameraSource;
	public static Mat cameraImage = new Mat();

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
		
		frontCam = CameraServer.getInstance().startAutomaticCapture("FrontCam", 0);
		frontCam.setResolution(320, 240);
	    frontCam.setFPS(10);
	    frontCam.setExposureManual(75);
	    backCam = CameraServer.getInstance().startAutomaticCapture("BackCam", 1);
	    backCam.setResolution(320, 240);
	    backCam.setFPS(10);
	    backCam.setExposureAuto();
	    backCam.setExposureManual(75);
	    
	    cvSinkCamera =  CameraServer.getInstance().getVideo(frontCam);
	    cvCameraSource = CameraServer.getInstance().putVideo("CurrentCamera", 320, 240);
		
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

		SmartDashboard.putData("TEST GYRO AND PROFILE FORWARDS",
				new CmdBothDriveWithProfileAndGyro(0, Settings.profileTestCruiseSpeed, Settings.profileTestDistance));

		SmartDashboard.putData("TURN WITH PROFILE: 90 DEGREES",
				new CmdBothTurnWithProfile(90, Settings.profileTestTurnCruiseSpeed));
		SmartDashboard.putData("TURN WITH PROFILE: -90 DEGREES",
				new CmdBothTurnWithProfile(-90, Settings.profileTestTurnCruiseSpeed));
		SmartDashboard.putData("TURN WITH PROFILE: 180 DEGREES",
				new CmdBothTurnWithProfile(180, Settings.profileTestTurnCruiseSpeed));
		SmartDashboard.putData("TURN WITH PROFILE: 0 DEGREES",
				new CmdBothTurnWithProfile(0, Settings.profileTestTurnCruiseSpeed));

		SmartDashboard.putData("TURN WITH DRIVE PROFILE: 90 DEGREES",
				new CmdBothDriveWithProfile(21.5984494934, Settings.profileTestTurnCruiseSpeed));
		SmartDashboard.putData("TURN WITH DRIVE PROFILE: 180 DEGREES",
				new CmdBothDriveWithProfile(43.1968998685, Settings.profileTestTurnCruiseSpeed));
		SmartDashboard.putData("Test Switch Active Camera", new CmdCameraManganer());
		// boilerVisionProcessing.start();
		// gearVisionProcessing.start();
		// SmartDashboard.getNumber("Desired Shoot Speed (feet/sec): ", 0);

		// SmartDashboard.putData("CmdDriveByGyro2", new
		// CmdDriveStraightWithGyro(-180, -80, -72));
		SmartDashboard.putData("TEST GYRO AND PROFILE BACKWARDS", new CmdBothDriveWithProfileAndGyro(0,
				Settings.profileTestCruiseSpeed, Settings.profileTestDistanceSeg2));
		SmartDashboard.putData("Test Vision Boiler: ", new CmdBothAlignToBoiler());
		// SmartDashboard.putData("Auto mode", autoChooser);
		RobotMap.Init();

		// visionThread = new Thread(() -> {
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

		switch (selectedAction) {
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
			autonomousCommand = new CmdAutoShooter(Settings.shooterTargetLinearVelocity, Settings.shooterControllerKp,
					Settings.shooterControllerKi, Settings.shooterControllerKd, Settings.shooterControllerKf);
			break;
		case NOTHING:
			autonomousCommand = null;
			break;
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
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		SmartDashboard.putNumber("Current Heading: ", subChassis.gyroGetRawHeading());
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
}
