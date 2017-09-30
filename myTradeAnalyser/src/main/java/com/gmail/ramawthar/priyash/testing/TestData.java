package com.gmail.ramawthar.priyash.testing;

import java.util.ArrayList;
import java.util.List;

import com.gmail.ramawthar.priyash.analysis.Instrument;

public class TestData
{
   List<InputData> allInputData;
   InputData inputNegData; // new double[100];

   InputData inputZeroData; // new double[100];

   InputData inputRandFltEpsilon; // new double[100];

   InputData inputRandDblEpsilon; // new double[100];

   InputData inputRandomData; // new double[2000];
   
   Instrument instrument;

   final static double output[][] = new double[10][2000];

   final static int output_int[][] = new int[10][2000];

   public final static double DBL_EPSILON = 2.2204460492503131e-016;

   public final static double FLT_EPSILON = 1.192092896e-07;

   /* min/max value for a TA_Integer */
   public final static int TA_INTEGER_MIN = Integer.MIN_VALUE + 1;
   public final static int TA_INTEGER_MAX = Integer.MAX_VALUE;

   /* min/max value for a TA_Real 
    *
    * Use fix value making sense in the
    * context of TA-Lib (avoid to use DBL_MIN
    * and DBL_MAX standard macro because they
    * are troublesome with some compiler).
    */
   public final static double TA_REAL_MIN = (-3e+37);
   public final static double TA_REAL_MAX = (3e+37);

   /* A value outside of the min/max range 
    * indicates an undefined or default value.
    */
   public final static double TA_INTEGER_DEFAULT = Integer.MIN_VALUE;
   public final static double TA_REAL_DEFAULT = (-4e+37);
/*
   TestData()
   {
      allInputData = new ArrayList<InputData>();
      inputNegData = new InputData("Negative Data", 100);
      allInputData.add(inputNegData);
      inputZeroData = new InputData("Zero Data", 100);
      allInputData.add(inputZeroData);
      inputRandFltEpsilon = new InputData("Random Float Epsilon", 100);
      allInputData.add(inputRandFltEpsilon);
      inputRandDblEpsilon = new InputData("Random Double Epsilon", 100);
      allInputData.add(inputRandDblEpsilon);
      inputRandomData = new InputData("Random Data", 2000);
      allInputData.add(inputRandomData);

      for (int i = 0; i < inputNegData.size(); i++)
      {
         inputNegData.setData(i, -((double) i), -((float) i), -i);
      }

      for (int i = 0; i < inputZeroData.size(); i++)
      {
         inputZeroData.setData(i, 0.0, (float) 0.0, 0);
      }

      for (int i = 0; i < inputRandomData.size(); i++)
      {
         double rand = Math.random() / 97.234;
         inputRandomData.setData(i, rand, (float) rand, (int) rand);
      }

      for (int i = 0; i < inputRandFltEpsilon.size(); i++)
      {
         int sign = ((int) Math.random()) % 2;
         double data = (sign != 0 ? 1.0 : -1.0) * (FLT_EPSILON);
         inputRandFltEpsilon.setData(i, data, (float) data,
               sign != 0 ? TA_INTEGER_MIN : TA_INTEGER_MAX);
      }
      
//edited and fixed PR 300917
      for (int i = 0; i < inputRandDblEpsilon.size(); i++)
      {
         int sign = (int) ((Math.random()*10) % 2);
         double data = (sign != 0 ? 1.0 : -1.0) * (DBL_EPSILON);
         inputRandDblEpsilon.setData(i, data, (float) data, sign != 0 ? 1 : -1);
      }
   }
   
   public List<InputData> getAllInputData()
   {
      return allInputData;
   }
   */
   int[] MTN = {590,555,569,538,538,560,545,587,633,630,628,655,599,536,515,510,500,500,485,480,489,490,490,490,490,490,490,500,475,475,475,475,464,462,469,473,460,457,457,468,475,484,460
		   ,440,441,440,445,442,435,443,444,438,431,463,490,480,479,490,465,430,415,420,418,410,402,404,400,395,404,399,390,375,375,370,375,375,379,380,373,389,391,394,399,400,400,395
		   ,380,380,376,361,380,400};
   
   int[] ADI = {70,70,70,70,70,70,70,70,60,66,66,72,80,80,80,80,100,100,90,110,119,115,115,105,110,110,110,115,105,105,105,95,95,95,105,130
		   ,120,121,120,120,120,114,114,114,107,105,106,105,105,105,105,105,105,95,91,94,120,120,110,110,100,102,102,101,102,105,113,112
		   ,110,110,110,110,110,110,110,106,105,105,110,104,100};
   
   public TestData(Instrument i){
	   super();	   
	   this.instrument = i;
   }
   
   private double[] copyFromIntArray(int[] source) {
	    double[] dest = new double[source.length];
	    for(int i=0; i<source.length; i++) {
	        dest[i] = source[i];
	    }
	    return dest;
	}

   
   public int[] getIntData(){
	   switch (instrument){
	   case ADI:
		   return this.ADI;
	   case MTN:
		   return this.MTN;
	default: return null;
	   }
   }

   public double[] getDoubleData(){
	   switch (instrument){
	   case ADI:
		   return copyFromIntArray(this.ADI);
	   case MTN:
		   return copyFromIntArray(this.MTN);
	default: return null;
	   }
   }
   
}
