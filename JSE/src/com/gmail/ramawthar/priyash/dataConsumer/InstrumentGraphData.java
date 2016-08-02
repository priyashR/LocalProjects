package com.gmail.ramawthar.priyash.dataConsumer;

import java.sql.Date;

public class InstrumentGraphData {
    
    private InstrumentGraphDataLine dataLines[];
    private String title;
    private String Instrument;
    private String xAxisLabel;
    private String yAxisLabel;
    private boolean createLegend;
    private boolean createToolTips;
    private boolean createURL;
    private DatasetTypes dataSetType;//RSI/MACD/RulingPrice/OLHC/etc.
    
    public InstrumentGraphData() {
        super();
    }    
    public InstrumentGraphData(InstrumentGraphDataLine dataLines[], String title, String Instrument, String xAxisLabel, String yAxisLabel, boolean createLegend, boolean createToolTips, boolean createURL, DatasetTypes dataSetType) {
        this.dataLines = dataLines;
        this.title =          title;       
        this.Instrument =     Instrument;  
        this.xAxisLabel =     xAxisLabel;  
        this.yAxisLabel =     yAxisLabel;  
        this.createLegend =   createLegend;
        this.createToolTips = createToolTips;
        this.createURL =      createURL;
        this.dataSetType =    dataSetType;        
        
    }

    public String getTitle() {
        return title;
    }

    public String getInstrument() {
        return Instrument;
    }

    public String getXAxisLabel() {
        return xAxisLabel;
    }

    public String getYAxisLabel() {
        return yAxisLabel;
    }

    public boolean isCreateLegend() {
        return createLegend;
    }

    public boolean isCreateToolTips() {
        return createToolTips;
    }

    public boolean isCreateURL() {
        return createURL;
    }

    public DatasetTypes getDataSetType() {
        return dataSetType;
    }

    public InstrumentGraphDataLine[] getDataLines() {
        return dataLines;
    }
}
