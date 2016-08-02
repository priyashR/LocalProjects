package com.gmail.ramawthar.priyash.dataConsumer;

import com.gmail.ramawthar.priyash.dataLoader.ExceptionTypes;

import com.tictactec.ta.lib.*;

import java.sql.Date;
import java.sql.ResultSet;


public class ProcessData {
    
    private double[] input;
    private int inputInt[];
    private double output[];
    private int outputInt[];
    private MInteger outBegIdx; 
    private MInteger outNbElement;
    private RetCode retCode;
    private Core lib;
    private int lookback;        
    private JSEDAOInt JSEDB;

    
    public ProcessData() {
        super();
        lib = new Core();
        input = new double[200];
        inputInt = new int[200];        
        output = new double[200];
        outputInt = new int[200];
        outBegIdx = new MInteger();
        outNbElement = new MInteger();
        
        JSEDB = new MySqlInstrumentDAO();
    }
    
    
    //testing-------testing-------testing-------testing-------testing-------testing-------testing-------testing-------testing-------testing
    public void testSimpleCall() {        
                
        // Create Input/Output arrays.
        input[0] = 180.1;
        input[1] = 999.7;
        input[2] = 500.5;
        input[3] = 122.7;
        input[4] = 133.5;
        input[5] = 17.7;
        //6
        //7
        //8
        //9
        //10
        
        Instrument ADI = new Instrument("ADI");
        ADI.loadInstrumentHist("2013-02-07","");
        double[] r = ADI.getDblRulingPrice();
             
        // Do the TA function call
        //retCode = lib.max( 3, 15, input, 16,
        retCode = lib.max( 3, 15, r, 16,
                           outBegIdx, outNbElement,
                           output );
        
        // Test the results.
       /* assertEquals( retCode, RetCode.Success);
        assertEquals( outBegIdx.value, 1 );
        assertEquals( outNbElement.value, 2 );
        assertEquals( output[0], 2.0 );
        assertEquals( output[1], 1.5 );
        */
        System.out.println( retCode.toString());
        System.out.println(  "1?: "+outBegIdx.value );
        System.out.println(  "2?: "+outNbElement.value );
        for (int i = 0; i<outNbElement.value; i++){
            System.out.println(  i+": "+output[i]  );
        }
        
        lookback = lib.maxLookback(2);
    }      
    //testing-------testing-------testing-------testing-------testing-------testing-------testing-------testing-------testing-------testing
 
    public String calcRSI(int lookBack, String rangeFrom, String rangeTo, double outRes[], Instrument outInstrument){
        
        Instrument JSEInstrument = outInstrument;
        String loadRes = JSEInstrument.loadInstrumentHist(rangeFrom,rangeTo);
        if (!(loadRes.equalsIgnoreCase("SUCCESS")))
            return loadRes;
        
        double[] resultDblSet = JSEInstrument.getDblRulingPrice();
        
        MInteger outBegin = new MInteger();           
        MInteger outNBElem = new MInteger();
        double [] rsiRes = new double[resultDblSet.length];
        retCode = lib.rsi(0,resultDblSet.length-1,resultDblSet,lookBack,outBegin,outNBElem,rsiRes);
        
        //comment from here
        for (int i=0;i<resultDblSet.length;i++){
            System.out.println("The value for index "+i+" is "+resultDblSet[i]);
        }
            
        System.out.println("The number of elements is: "+resultDblSet.length);
        
        System.out.println("retCode: "+retCode);
        Date[] dates = JSEInstrument.getDates();
        for (int i=0;i<(rsiRes.length-lookBack);i++){
            System.out.println("The value for  "+dates[i+lookBack]+"("+i+")"+" is "+rsiRes[i]);
        }
        System.out.println("The number of elements is: "+rsiRes.length+"("+outNBElem.value+")");
        System.out.println("Start index: "+"("+outBegin.value+")");
        //comment to here
        
        System.arraycopy(rsiRes, 0, outRes, 0, rsiRes.length);
        outInstrument = JSEInstrument;
        
        return "SUCCESS";
    }  
    //Add ad hoc method showRSIGraph here!!!
    
    //may be a redundatn method - possibly remove
    public void postData(Instrument instrument, boolean overwriteData, int lookBack, String colName, double calcRes[]){
        if (overwriteData){
            System.out.println(instrument.postToFormulaeTable(colName, calcRes, lookBack, false));
        }else{
            System.out.println(instrument.postToFormulaeTable(colName, calcRes, lookBack));
        }
    }  
    
    public String calcRSI14(String instrument, boolean overwriteData){
        double outRes[] = new double[100000];
        Instrument outJSEInstrument = new Instrument(instrument.toUpperCase());;          
        String calcResult = (calcRSI(14, "", "", outRes, outJSEInstrument));
        if (calcResult.equalsIgnoreCase("SUCCESS")){
            postData(outJSEInstrument, overwriteData, 14, "RSI14", outRes);
        }else
            return calcResult;
        return "calcRSI14 Calculated and posted.";
    }
    
    public String calcMOM(int lookBack, String rangeFrom, String rangeTo, double outMom[], Instrument outInstrument){
        
        Instrument JSEInstrument = outInstrument;
        String loadRes = JSEInstrument.loadInstrumentHist(rangeFrom,rangeTo);
        if (!(loadRes.equalsIgnoreCase("SUCCESS")))
            return loadRes;
        
        double[] resultDblSet = JSEInstrument.getDblRulingPrice(); 
    
        
        MInteger outBegin = new MInteger();           
        MInteger outNBElem = new MInteger();
        double [] momRes = new double[resultDblSet.length];
        retCode = lib.mom(0,resultDblSet.length-1,resultDblSet,lookBack,outBegin,outNBElem,momRes);    
        
        //comment from here
        for (int i=0;i<resultDblSet.length;i++){
            System.out.println("The value for index "+i+" is "+resultDblSet[i]);
        }
            
        System.out.println("The number of elements is: "+resultDblSet.length);
        
        System.out.println("retCode: "+retCode);
        Date[] dates = JSEInstrument.getDates();
        for (int i=0;i<(momRes.length-lookBack);i++){
            System.out.println("The value for  "+dates[i+lookBack]+"("+i+")"+" is "+momRes[i]);
        }
        System.out.println("The number of elements is: "+momRes.length+"("+outNBElem.value+")");
        System.out.println("Start index: "+"("+outBegin.value+")");
        //comment to here
        
        System.arraycopy(momRes, 0, outMom, 0, momRes.length);
        outInstrument = JSEInstrument;
        return "SUCCESS";
    }    
    //Add ad hoc method showMOMGraph here!!!
    
    public String calcMOM14(String instrument, boolean overwriteData){
        double resMom[] = new double[100000];
        Instrument outJSEInstrument = new Instrument(instrument.toUpperCase());   
        String calcResult = (calcMOM(14, "", "", resMom, outJSEInstrument));
        if (calcResult.equalsIgnoreCase("SUCCESS")){
            postData(outJSEInstrument, overwriteData, 14, "MOM14", resMom);
        }else
            return calcResult;
        return "calcMOM14 Calculated and posted.";        
    }  

    public String calcMACD(int fast, int slow, int signal, String rangeFrom, String rangeTo, double outMACD[],double outMACDSignal[],double outMACDHist[], Instrument outJSEInstrument ){
        //fast - 12days
        //slow - 26days
        //signal - 9days
        
        Instrument JSEInstrument = outJSEInstrument;
        String loadRes = JSEInstrument.loadInstrumentHist(rangeFrom,rangeTo);
        if (!(loadRes.equalsIgnoreCase("SUCCESS")))
            return loadRes;
        
        double[] resultDblSet = JSEInstrument.getDblRulingPrice(); 
    
        
        MInteger outBegin = new MInteger();           
        MInteger outNBElem = new MInteger();
        double [] macd = new double[resultDblSet.length];
        double [] macdSignal = new double[resultDblSet.length];
        double [] macdHist = new double[resultDblSet.length];
        retCode = lib.macd(0,resultDblSet.length-1,resultDblSet,fast,slow,signal,outBegin,outNBElem,macd,macdSignal,macdHist);  
        
        //comment from here
        for (int i=0;i<resultDblSet.length;i++){
            System.out.println("The value for index "+i+" is "+resultDblSet[i]);
        }
            
        System.out.println("The number of elements is: "+resultDblSet.length);
        
        System.out.println("retCode: "+retCode);
        Date[] dates = JSEInstrument.getDates();
        for (int i=0;i<(macd.length);i++){
            System.out.println("The value for MACD "+dates[i]+"("+i+")"+" is "+macd[i]+" - Signal: "+macdSignal[i]+" - Histogram: "+macdHist[i]);
        }
        System.out.println("The number of elements is: "+macd.length+"("+outNBElem.value+")");
        System.out.println("Start index: "+"("+outBegin.value+")");
        //comment to here
        System.arraycopy(macd, 0, outMACD, 0, outMACD.length);
        System.arraycopy(macdSignal, 0, outMACDSignal, 0, outMACD.length);
        System.arraycopy(macdHist, 0, outMACDHist, 0, outMACD.length);
        outJSEInstrument = JSEInstrument;
        return "SUCCESS";
    }    
    //Add ad hoc method showMACDGraph here!!!
    
    public String calcMACDdflt(String instrument, boolean overwriteData){
        double resMACD[] = new double[100000];
        double resMACDSignal[] = new double[100000];
        double resMACDhist[] = new double[100000];
        Instrument outJSEInstrument = new Instrument(instrument.toUpperCase());   
        String calcResult = (calcMACD(12, 26, 9, "", "", resMACD, resMACDSignal, resMACDhist, outJSEInstrument));
        if (calcResult.equalsIgnoreCase("SUCCESS")){
            postData(outJSEInstrument, overwriteData, 0, "MACDdflt", resMACD);
            postData(outJSEInstrument, overwriteData, 0, "MACDSignaldflt", resMACDSignal);
            postData(outJSEInstrument, overwriteData, 0, "MACDhistdflt", resMACDhist);
        }else
            return calcResult;
        return "calcMACDdflt Calculated and posted.";             
    }  
    
    public String updateAllInstruments(){
        ResultSet resultSet = JSEDB.getManifestSet();
        try {
            while (resultSet.next()){
                System.out.println("updating data for " + resultSet.getString("instrument"));
                //RSI
                calcRSI14(resultSet.getString("instrument"),false);
                //MOM
                //MACD
                //etc....
            }
        } catch (Exception e){
            (JSEDB.getUtil().getEx()).addLog(ExceptionTypes.ERROR.toString(), "ProcessData.updateAllinstruments: "+e.getMessage());
            return e.getMessage();
        }
        return "SUCCESS";
    }
    public static void main(String args[]){
        ProcessData p = new ProcessData();
        System.out.println(p.calcRSI14("ADI",false));
        

                   
        //NEED TO TEST MOM
    }
}
