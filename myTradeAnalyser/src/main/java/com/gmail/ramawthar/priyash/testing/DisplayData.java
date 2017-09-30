package com.gmail.ramawthar.priyash.testing;

public class DisplayData {
	/*
	   double[] doubleData;
	   float[] floatData;
	   int[] intData;
	   ReturnClass
	*/ 
	private static boolean mute = false;
	public static void displayLine(String line){
		if (!mute){
		System.out.println(line);
		}
	}

}
