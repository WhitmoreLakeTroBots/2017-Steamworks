package org.usfirst.frc.team3668.robot;

import org.usfirst.frc.team3668.robot.commands.CmdInitializeGyro;
import org.usfirst.frc.team3668.robot.commands.CmdInvertDrive;
import org.usfirst.frc.team3668.robot.commands.CmdReverseShootMech;
import org.usfirst.frc.team3668.robot.commands.CmdTeleopClimb;
import org.usfirst.frc.team3668.robot.commands.CmdTeleopFeed;
import org.usfirst.frc.team3668.robot.commands.CmdTeleopShoot;
import org.usfirst.frc.team3668.robot.commands.CmdTeleopSweepIn;
import org.usfirst.frc.team3668.robot.commands.CmdTeleopSweepOut;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	//// CREATING BUTTONS
	// One type of button is a joystick button which is any button on a
	//// joystick.
	// You create one by telling it which joystick it's on and which button
	// number it is.
	// Joystick stick = new Joystick(port);
	// Button button = new JoystickButton(stick, buttonNumber);

	// There are a few additional built in buttons you can use. Additionally,
	// by subclassing Button you can create custom triggers and bind those to
	// commands the same as any other Button.

	//// TRIGGERING COMMANDS WITH BUTTONS
	// Once you have a button, it's trivial to bind it to a button in one of
	// three ways:

	// Start the command when the button is pressed and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenPressed(new ExampleCommand());

	// Run the command while the button is being held down and interrupt it once
	// the button is released.
	// button.whileHeld(new ExampleCommand());

	// Start the command when the button is released and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenReleased(new ExampleCommand());

	public static Joystick joyDrive = new Joystick(Settings.joyDrive);
	public static Joystick joyArticulator = new Joystick(Settings.joyArticulator);

	public static Button invertDriveButton = new JoystickButton(joyDrive, Settings.joyDriveInvert);
	
	public static Button spinShooterButton = new JoystickButton(joyArticulator, Settings.joyArticulatorShooterButton);
	public static Button fireShooterButton = new JoystickButton(joyArticulator, Settings.joyArticulatorFireShooterButton);
	public static Button reverseShooterMech = new JoystickButton(joyArticulator, Settings.joyArticulatorReverseShooterMech);
	public static Button climberButton = new JoystickButton(joyArticulator, Settings.joyArticulatorClimbButton);
	public static Button sweeperButtonIn = new JoystickButton(joyArticulator, Settings.joyArticulatorSweepButtonIn);
	public static Button sweeperButtonOut = new JoystickButton(joyArticulator, Settings.joyArticulatorSweepButtonOut);
	
	
	//J-Flork Messin' around
//	public static Button lol1 = new JoystickButton(joyDrive, 11);
//	public static Button lol2 = new JoystickButton(joyDrive, 10);
	public OI() {
		climberButton.whileHeld(new CmdTeleopClimb());
		sweeperButtonIn.whileHeld(new CmdTeleopSweepIn());
		sweeperButtonOut.whileHeld(new CmdTeleopSweepOut());
		spinShooterButton.toggleWhenPressed(new CmdTeleopShoot(Settings.shooterTargetLinearVelocity));
		fireShooterButton.whileHeld(new CmdTeleopFeed());
		reverseShooterMech.whileHeld(new CmdReverseShootMech());
		
		invertDriveButton.whenPressed(new CmdInvertDrive());
//		lol1.whenPressed(new JFlorkCheatCommandOne());
//		lol2.whenPressed(new JFlorkCheatCommandTwo());
		SmartDashboard.putData("InitializeGyro",new CmdInitializeGyro());		
	}
}