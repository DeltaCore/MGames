package net.ccmob.math;

public class Utils {

	public static int randomInt(int low, int high){
		return (int) (Math.random() * (high - low) + low);
	}
	
	public static double randomDouble(int low, int high){
		return (Math.random() * (high - low) + low);
	}
	
}
