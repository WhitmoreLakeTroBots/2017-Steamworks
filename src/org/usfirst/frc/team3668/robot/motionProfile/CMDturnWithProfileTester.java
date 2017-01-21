package org.usfirst.frc.team3668.robot.motionProfile;

import org.usfirst.frc.team3668.robot.Settings;
import org.usfirst.frc.team3668.robot.commands.CmdBothTurnWithProfile;

public class CMDturnWithProfileTester {
	static CMDturnWithProfileTester driveTester = new CMDturnWithProfileTester();
	
	public CMDturnWithProfileTester(){
		test();
	}
	
	void test (){
		CmdBothTurnWithProfile cmdTurn = new CmdBothTurnWithProfile(Settings.profileTestTurnDregees, Settings.profileTestTurnCruiseSpeed, Settings.TurnType.SwingR);
		while(cmdTurn.isFinished() == false){
			cmdTurn.execute();
			try {
				Thread.sleep((long)10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
	}
		cmdTurn = null;
	}
	
	
	public static void main(String[] args) {
		driveTester.test();
	}
}
