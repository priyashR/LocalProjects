package com.gmail.ramawthar.priyash.testing;

import java.util.List;

import com.gmail.ramawthar.priyash.responses.ReturnClass;

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
	
	public static void displayDuobleData(double[] doubleData){
		if (!mute){
			System.out.println("The number of elements is: "+doubleData.length);
			for (int i = 0; i < doubleData.length; i++){
			System.out.println("index: "+i+" value: "+doubleData[i]);
			}
		}
	}
	
	public static void displayFloatData(float[] floatData){
		if (!mute){
			System.out.println("The number of elements is: "+floatData.length);
			for (int i = 0; i < floatData.length; i++){
			System.out.println("index: "+i+" value: "+floatData[i]);
			}
		}
	}
	
	public static void displayIntData(int[] intData){
		if (!mute){
			System.out.println("The number of elements is: "+intData.length);
			for (int i = 0; i < intData.length; i++){
			System.out.println("index: "+i+" value: "+intData[i]);
			}
		}
	}
	
	public static void displayIntData(ReturnClass r){
		if (!mute){
		System.out.println("Status: "+r.getStatus());
		System.out.println("Description: "+r.getDescription());
		}
	}
	
	public static void displayInputData(InputData i){
		if (!mute){
			DisplayData.displayDuobleData(i.getDoubleData());
			DisplayData.displayFloatData(i.getFloatData());
			DisplayData.displayIntData(i.getIntData());
		}
	}
	
	public static void displayInputDataList(List<InputData>  li){
		if (!mute){
			for (InputData i : li) {
				if (i.getName().equalsIgnoreCase("Random Double Epsilon")){
					DisplayData.displayLine("list: "+i.getName());
					DisplayData.displayLine("Double data");
					DisplayData.displayDuobleData(i.getDoubleData());
					DisplayData.displayLine("Float data");
					DisplayData.displayFloatData(i.getFloatData());
					DisplayData.displayLine("Int data");
					DisplayData.displayIntData(i.getIntData());
				}
			}
		}
	}
	
}
