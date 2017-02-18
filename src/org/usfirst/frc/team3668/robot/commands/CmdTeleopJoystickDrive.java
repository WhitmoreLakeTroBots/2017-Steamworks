package org.usfirst.frc.team3668.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team3668.robot.OI;
import org.usfirst.frc.team3668.robot.Robot;
import org.usfirst.frc.team3668.robot.RobotMap;

/**
 *
 */
public class CmdTeleopJoystickDrive extends Command {

    public CmdTeleopJoystickDrive() {
        // Use requires() here to declare subsystem dependencies
          requires(Robot.subChassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    	//RobotMap.imu.reset();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.subChassis.Drive(OI.joyDrive);
    	System.out.println("left encoder dist: " + Robot.subChassis.getLeftEncoderDistInch() + "\t right encoder dist: " + Robot.subChassis.getRightEncoderDistInch());
    	//System.out.println("Gyro Angle: " + RobotMap.imu.getYaw());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
