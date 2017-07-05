package org.usfirst.frc.team3668.robot;

public class PID {
	double _Kp;
	double _Ki;
	double _Kd;
	
	double iError = 0;
	double dError = 0;
	double lastError = 0;
	
	public PID (double Kp, double Ki, double Kd){
		_Kp = Kp;
		_Ki = Ki;
		_Kd = Kd;
	}
	
	public double calcError (double target, double current){
		return target - current;
	}
	
	public double calcP (double target, double current){
		double error = calcError(target, current);
		return _Kp * error;
	}
	
	public double calcP (double error){
		return _Kp * error;
	}
	
	public double calcI (double target, double current){
		double error = calcError(target, current);
		iError = iError + error;
		return _Ki * iError;
	}
	
	public double calcI (double error){
		iError = iError + error;
		return _Ki * iError;
	}
	
	public double calcD (double target, double current){
		double error = calcError(target, current);
		dError = error - lastError;
		lastError = error;
		return _Kd * dError;
	}
	
	public double calcD (double error){
		dError = error - lastError;
		lastError = error;
		return _Kd * dError;
	}
	
	public double calcPID (double target, double current){
		double error = calcError(target, current);
		double p = calcP(error);
		double i = calcI(error);
		double d = calcD(error);
		lastError = error;
		return p + i + d;
	}
}