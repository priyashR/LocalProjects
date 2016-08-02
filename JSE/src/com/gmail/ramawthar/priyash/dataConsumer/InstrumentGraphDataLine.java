package com.gmail.ramawthar.priyash.dataConsumer;

import java.sql.Date;

public class InstrumentGraphDataLine {    
    private Date dataDate;
    private double lineValue0;
    private double lineValue1;
    private double lineValue2;
    private double lineValue3;
    private double lineValue4;
    private double lineValue5;
    private GraphLineTypes lineType;
    
    public InstrumentGraphDataLine(Date dataDate, double lineValue0, double lineValue1, double lineValue2, double lineValue3, double lineValue4, double lineValue5, GraphLineTypes lineType) {
        super();
        
        this.dataDate   = dataDate  ;
        this.lineValue0 = lineValue0;
        this.lineValue1 = lineValue1;
        this.lineValue2 = lineValue2;
        this.lineValue3 = lineValue3;
        this.lineValue4 = lineValue4;
        this.lineValue5 = lineValue5;
        this.lineType   = lineType  ;        
        
    }

    public Date getDataDate() {
        return dataDate;
    }

    public double getLineValue0() {
        return lineValue0;
    }

    public double getLineValue1() {
        return lineValue1;
    }

    public double getLineValue2() {
        return lineValue2;
    }

    public double getLineValue3() {
        return lineValue3;
    }

    public double getLineValue4() {
        return lineValue4;
    }

    public double getLineValue5() {
        return lineValue5;
    }

    public GraphLineTypes getLineType() {
        return lineType;
    }
}
