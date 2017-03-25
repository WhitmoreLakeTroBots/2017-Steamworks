
package org.usfirst.frc.team3668.robot;

import org.usfirst.frc.team3668.robot.Settings.action;
import org.usfirst.frc.team3668.robot.Settings.colors;
import org.usfirst.frc.team3668.robot.commands.CmdAutoShooter;
import org.usfirst.frc.team3668.robot.commands.CmdBothDriveWithProfile;
import org.usfirst.frc.team3668.robot.commands.CmdBothDriveWithProfileAndGyro;
import org.usfirst.frc.team3668.robot.commands.CmdBothTurnWithProfile;
import org.usfirst.frc.team3668.robot.commands.CmdBothVisionTurnWithGyro;
import org.usfirst.frc.team3668.robot.commands.CmdTeleopJoystickDrive;
import org.usfirst.frc.team3668.robot.commands.CmdTurnWithGyro;
import org.usfirst.frc.team3668.robot.commands.commandGroups.CmdGroupAutoCenter;
import org.usfirst.frc.team3668.robot.commands.commandGroups.CmdGroupAutoLeftGear;
import org.usfirst.frc.team3668.robot.commands.commandGroups.CmdGroupAutoRightGear;
import org.usfirst.frc.team3668.robot.commands.commandGroups.CmdGroupAutoShootFromKey;
import org.usfirst.frc.team3668.robot.commands.commandGroups.CmdGroupGearVision;
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

	public static final VisionProcessing visionProcessing = new VisionProcessing();
	public static final SubChassis subChassis = new SubChassis();
	public static final SubShooter subShooter = new SubShooter();
	public static final SubClimber subClimber = new SubClimber();
	public static final SubSweeper subSweeper = new SubSweeper();
	public static final SubFeeder subFeeder = new SubFeeder();
	public static boolean isDriveInverted = true;
	public static boolean cameraReversed = false; 
	public static OI oi;

	public static UsbCamera DashCamera;
	public static UsbCamera ThatOtherCamera;
	Command autonomousCommand;
	Command teleopCommand = new CmdTeleopJoystickDrive();
	SendableChooser<action> autoChooser = new SendableChooser<>();
	SendableChooser<colors> autoColorChooser = new SendableChooser<>();
//	Thread visionThread;
//	CvSink cvSink;
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		oi = new OI();
		ThatOtherCamera = CameraServer.getInstance().startAutomaticCapture("Upper Camera", 1);
		ThatOtherCamera.setFPS(Settings.visionCameraFPS);
		ThatOtherCamera.setResolution(Settings.visionImageWidthPixels, Settings.visionImageHeightPixels);
		DashCamera = CameraServer.getInstance().startAutomaticCapture("Gear Camera", 0);
		DashCamera.setFPS(Settings.visionCameraFPS);
		DashCamera.setResolution(Settings.visionImageWidthPixels, Settings.visionImageHeightPixels);
		autoColorChooser.addObject("Blue", colors.Blue);
		autoColorChooser.addDefault("Red", colors.Red);
		SmartDashboard.putData("Color Chooser", autoColorChooser);
		
		autoChooser.addDefault("AUTO Center Gear Only", action.centerGear);
		autoChooser.addObject("AUTO Left Gear", action.leftGear);
		autoChooser.addObject("AUTO Right Gear", action.rightGear);
		autoChooser.addObject("AUTO Gear With Vision", action.visionGear);
		autoChooser.addObject("AUTO Shoot From Key", action.key);
		autoChooser.addObject("AUTO Shoot Only", action.shootOnly);
		autoChooser.addObject("AUTO DO NOTHING; BE A FAILURE", action.NOTHING);
		SmartDashboard.putData("Action Chooser", autoChooser);

		SmartDashboard.putData("TEST GYRO AND PROFILE FORWARDS",
				new CmdBothDriveWithProfileAndGyro(0, Settings.profileTestCruiseSpeed, Settings.profileTestDistance));
		
		SmartDashboard.putData("TURN WITH PROFILE: 90 DEGREES", new CmdBothTurnWithProfile(90, Settings.profileTestTurnCruiseSpeed));
		SmartDashboard.putData("TURN WITH PROFILE: -90 DEGREES", new CmdBothTurnWithProfile(-90, Settings.profileTestTurnCruiseSpeed));
		SmartDashboard.putData("TURN WITH PROFILE: 180 DEGREES",new CmdBothTurnWithProfile(180, Settings.profileTestTurnCruiseSpeed));
		SmartDashboard.putData("TURN WITH PROFILE: 0 DEGREES", new CmdBothTurnWithProfile(0, Settings.profileTestTurnCruiseSpeed));
	
		
		SmartDashboard.putData("TURN WITH DRIVE PROFILE: 90 DEGREES", new CmdBothDriveWithProfile(21.5984494934, Settings.profileTestTurnCruiseSpeed));
		SmartDashboard.putData("TURN WITH DRIVE PROFILE: 180 DEGREES", new CmdBothDriveWithProfile(43.1968998685,Settings.profileTestTurnCruiseSpeed));
		
		SmartDashboard.putData("Turn With Gyro: 90", new CmdTurnWithGyro(90));
		SmartDashboard.putData("Turn With Gyro: 180", new CmdTurnWithGyro(180));
		SmartDashboard.putData("Turn With Gyro: 270", new CmdTurnWithGyro(270));
//		boilerVisionProcessing.start();
//		boilerVisionProcessing.start();
//		gearVisionProcessing.start();
//		SmartDashboard.getNumber("Desired Shoot Speed (feet/sec): ", 0);

//		SmartDashboard.putData("TEST GYRO AND PROFILE FORWARDS",
//				new CmdBothDriveWithProfileAndGyro(0, Settings.profileTestCruiseSpeed, Settings.profileTestDistance));
//		
//		SmartDashboard.putData("TURN WITH PROFILE: 90 DEGREES", new CmdBothTurnWithProfile(90, Settings.profileTestTurnCruiseSpeed));
//		SmartDashboard.putData("TURN WITH PROFILE: -90 DEGREES", new CmdBothTurnWithProfile(-90, Settings.profileTestTurnCruiseSpeed));
//		SmartDashboard.putData("TURN WITH PROFILE: 180 DEGREES",new CmdBothTurnWithProfile(180, Settings.profileTestTurnCruiseSpeed));
//		SmartDashboard.putData("TURN WITH PROFILE: 0 DEGREES", new CmdBothTurnWithProfile(0, Settings.profileTestTurnCruiseSpeed));
//	
//		
//		SmartDashboard.putData("TURN WITH DRIVE PROFILE: 90 DEGREES", new CmdBothDriveWithProfile(21.5984494934, Settings.profileTestTurnCruiseSpeed));
//		SmartDashboard.putData("TURN WITH DRIVE PROFILE: 180 DEGREES", new CmdBothDriveWithProfile(43.1968998685,Settings.profileTestTurnCruiseSpeed));
//
//		SmartDashboard.putData("TEST GYRO AND PROFILE BACKWARDS",
//				new CmdBothDriveWithProfileAndGyro(0, Settings.profileTestCruiseSpeed, Settings.profileTestDistanceSeg2));

		SmartDashboard.putData("Gear Vision Test: ", new CmdGroupGearVision());
		RobotMap.Init();
//		visionProcessing.start();
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
		subChassis.resetGyro();
		action selectedAction = (action) autoChooser.getSelected();
		colors selectedColor = (colors) autoColorChooser.getSelected();
		// DON'T FORGET THE BREAK!!!
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
		case visionGear:
			autonomousCommand = new CmdGroupGearVision();
			break;
		case key:
			autonomousCommand = new CmdGroupAutoShootFromKey(selectedColor);
			break;
		case shootOnly:
			autonomousCommand = new CmdAutoShooter(Settings.shooterTargetLinearVelocity);
			break;
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
//		visionProcessing.stop();
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
		SmartDashboard.putNumber("Unnormalized value: ", subChassis.gyroGetUnnormalizedHeading());
//		SmartDashboard.putNumber("Current Gyro Normalization: ", subChassis.gyroGetRawHeading());
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
}
