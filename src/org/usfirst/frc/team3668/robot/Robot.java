
package org.usfirst.frc.team3668.robot;

import org.usfirst.frc.team3668.robot.commands.*;
import org.usfirst.frc.team3668.robot.commands.commandGroups.CmdGroupAutoBlueLeftGear;
import org.usfirst.frc.team3668.robot.commands.commandGroups.CmdGroupBlueAutoCenter;
import org.usfirst.frc.team3668.robot.subsystems.*;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// YOYOYO
public class Robot extends IterativeRobot {

	public static final SubChassis subChassis = new SubChassis();
	public static final SubShooter subShooter = new SubShooter();
	public static final SubClimber subClimber = new SubClimber();
	public static final SubSweeper subSweeper = new SubSweeper();
	public static final SubFeeder subFeeder = new SubFeeder();
	public static OI oi;

	Command autonomousCommand;
	Command teleopCommand = new CmdTeleopJoystickDrive();
	SendableChooser<Command> autoChooser = new SendableChooser<>();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		oi = new OI();
		// chooser.addDefault("Default Auto", new ExampleCommand());
		// chooser.addObject("My Auto", new MyAutoCommand());

		SmartDashboard.putData("Test the Profile!",
				new CmdBothDriveWithProfile(Settings.profileTestDistance, Settings.profileTestCruiseSpeed));
		SmartDashboard.putData("CmdDriveByGyro1", new CmdDriveStraightWithGyro(0, 63, 72));
		//SmartDashboard.putData("CmdDriveByGyro2", new CmdDriveStraightWithGyro(-180, -80, -72));
		SmartDashboard.putData("TEST GYRO AND PROFILE",
				new CmdBothDriveWithProfileAndGyro(0, Settings.profileTestCruiseSpeed, Settings.profileTestDistance));

		autoChooser.addObject("Center Gear", new CmdGroupBlueAutoCenter());
		autoChooser.addObject("Left Gear", new CmdGroupAutoBlueLeftGear());
		// SmartDashboard.putData("Auto mode", autoChooser);
		RobotMap.Init();

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
		autonomousCommand = autoChooser.getSelected();

		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */
		// schedule the autonomous command (example)
		if (teleopCommand != null) {
			teleopCommand.cancel();
		}
		if (autonomousCommand != null) {
			autonomousCommand.start();
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
