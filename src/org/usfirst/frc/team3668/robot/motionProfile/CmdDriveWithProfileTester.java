package org.usfirst.frc.team3668.robot.motionProfile;

import org.usfirst.frc.team3668.robot.Settings;
import org.usfirst.frc.team3668.robot.commands.CmdBothDriveWithProfile;

public class CmdDriveWithProfileTester {
	static CmdBothDriveWithProfile cmdDrive = new CmdBothDriveWithProfile(Settings.profileTestDistance,Settings.profileTestCruiseSpeed);
	static CmdDriveWithProfileTester driveTester = new CmdDriveWithProfileTester();
	
	public CmdDriveWithProfileTester(){
		test();
	}
	
	void test (){
		while(cmdDrive.isFinished() == false){
			cmdDrive.execute();
			try {
				Thread.sleep((long)10);
			} catch (InterruptedException e) {
				e.printStackTrace();
     		}
			
	}
		cmdDrive = null;
	}
	public static void main(String[] args) {
		driveTester.test();
	}
}
