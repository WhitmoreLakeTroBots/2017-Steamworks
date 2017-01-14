package org.usfirst.frc.team3668.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.RobotDrive;
//Codspeed!

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	
	public static CANTalon chassisMotorLeft1 = new CANTalon(Settings.chassisMotorLeft1CanId);
    public static CANTalon chassisMotorRight1 = new CANTalon(Settings.chassisMotorRight1CanId);
	public static CANTalon chassisMotorLeft2 = new CANTalon(Settings.chassisMotorLeft2CanId);
    public static CANTalon chassisMotorRight2 = new CANTalon(Settings.chassisMotorRight2CanId);

    public static RobotDrive chassisRobotDrive = new RobotDrive(chassisMotorLeft1 , chassisMotorLeft2 , chassisMotorRight1 , chassisMotorRight2 );

    
}
