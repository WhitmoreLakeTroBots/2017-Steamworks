package org.usfirst.frc.team3668.robot.commands;

import org.usfirst.frc.team3668.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class CmdCameraManganer extends Command {

	private boolean justSwitched = false;
	private boolean lastLoopVal = Robot.isDriveInverted;
	
    public CmdCameraManganer() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.subCamera);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(lastLoopVal != Robot.isDriveInverted){
    		justSwitched = true;
    	}
    	
    	if(justSwitched && Robot.isFrontCamera){
    		Robot.cvSinkCamera.setSource(Robot.backCam);
    		Robot.isFrontCamera = !Robot.isFrontCamera;
    		justSwitched = false;
    	} else if (justSwitched && !Robot.isFrontCamera){
    		Robot.cvSinkCamera.setSource(Robot.frontCam);
    		Robot.isFrontCamera = !Robot.isFrontCamera;
    		justSwitched = false;
    	}
    	
    	Robot.cvSinkCamera.grabFrame(Robot.cameraImage);
    	Robot.cvCameraSource.putFrame(Robot.cameraImage);
    	Robot.cameraImage.release();
    	lastLoopVal = Robot.isDriveInverted;
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
