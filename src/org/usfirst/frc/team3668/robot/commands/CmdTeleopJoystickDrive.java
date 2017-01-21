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
    	//double angleX = RobotMap.imu.getAngleX();
    	//double degreeX = Math.toDegrees(angleX);
    	//System.out.println("New Gyro X: " + RobotMap.imu.getAngleX() +"\t New Gyro Degrees X: "+ degreeX+"\t New Gyro Y: " + RobotMap.imu.getAngleY() + "\t New Gyro Z: " + RobotMap.imu.getAngleZ()+"\t New Gyro Angle: "+RobotMap.imu.getAngle());
    	//System.out.println("Gyro Pitch: " + RobotMap.imu.getPitch() + "\t Gyro Roll: " + RobotMap.imu.getRoll()+ "\t Gyro Yaw: "+RobotMap.imu.getYaw()+"\t Gyro Angle: "+RobotMap.imu.getAngle());
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
