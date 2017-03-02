package org.usfirst.frc.team3668.robot.subsystems;

import org.usfirst.frc.team3668.robot.commands.CmdCameraManganer;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class SubCamera extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new CmdCameraManganer());
    }
}

