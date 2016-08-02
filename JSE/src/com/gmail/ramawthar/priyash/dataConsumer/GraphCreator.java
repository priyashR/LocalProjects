package com.gmail.ramawthar.priyash.dataConsumer;

import com.gmail.ramawthar.priyash.dataLoader.ExceptionTypes;

import java.awt.Color;

import java.sql.Date;

import java.sql.SQLException;

import java.text.SimpleDateFormat;

import java.util.Iterator;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;

public class GraphCreator {
    public GraphCreator() {
        super();
    }
    private XYDataset createDataset(InstrumentGraphData graphData[]) {
        TimeSeries s1 = null;
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        for (int i = 0; i < graphData.length; i++){
            s1 = new TimeSeries(graphData[i].getTitle());
            for (int j = 0; j < graphData[i].getDataLines().length; j++){
                s1.add(new Day(graphData[i].getDataLines()[j].getDataDate()), graphData[i].getDataLines()[j].getLineValue0());                
            }   
            dataset.addSeries(s1);
        }
        return dataset;
    }
    
    private JFreeChart createChart(InstrumentGraphData graphData[], String title, String xAxisLabel, String yAxisLabel, boolean createLegend, boolean createToolTips, boolean createURL ) {

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
            title,      //"Legal & General Unit Trust Prices",  // title
            xAxisLabel, //"Date",             // x-axis label
            yAxisLabel, //"Price Per Unit",   // y-axis label
            createDataset(graphData),            // data
            createLegend, //true,               // create legend?
            createToolTips, //true,               // generate tooltips?
            createURL //false               // generate URLs?
        );

        chart.setBackgroundPaint(Color.white);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);

        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(true);
            renderer.setDrawSeriesLineAsPath(true);
        }

        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));

        return chart;

    } 
    
    public JPanel createPanel(InstrumentGraphData[] graphData) {
        String title = "Ruling price for "+graphData[0].getTitle();
        
        for (int i=1; i < graphData.length; i++){
            title = title+", "+graphData[i].getTitle();
        }
        JFreeChart chart = createChart(graphData, title, graphData[0].getXAxisLabel(), graphData[0].getYAxisLabel(), graphData[0].isCreateLegend(), graphData[0].isCreateToolTips(), graphData[0].isCreateURL());
        ChartPanel panel = new ChartPanel(chart);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        return panel;
    }
    
    //test the method below
    //This method is for when you run formulae on the fly and the resultant data is not saved, but requires a graphical display
    public InstrumentGraphData createGraphData(double[] dataValues, Date[] dataDates, String formula, Integer offset){
        //Add some exception handling
        InstrumentGraphDataLine [] dateLines = new InstrumentGraphDataLine[dataDates.length-offset];
        InstrumentGraphData rulingGraphData;
        int counter = 0;
        for (int i = 0; i<dataDates.length-offset; i++){
            System.out.println(" dataDates[i]: "+dataDates[i+offset]+" dataValues[i]: "+dataValues[i]);
            dateLines[counter++] = new InstrumentGraphDataLine(dataDates[i+offset], dataValues[i], 0,0,0,0,0, GraphLineTypes.FORMULA);
            
        }            
        rulingGraphData = new InstrumentGraphData(dateLines, formula, formula, "Date", "Ruling Price", true, true, false, DatasetTypes.getDatasetFromString(formula));
        System.out.println(" rulingGraphData: "+rulingGraphData.getDataLines().length);
        return rulingGraphData;

    }
    public String createTimeLine(InstrumentGraphData[] graphData, GraphTypes graphType){ 
        
        ChartFrame chart = new ChartFrame("Instrument graph", createPanel(graphData));
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
        
        return "SUCCESS";        
    }
    public String createComboTimeLine(InstrumentGraphData graphData[], InstrumentGraphData graphData2[],GraphTypes graphType){        
        return "SUCCESS";        
    }
    public String createCandleStick(InstrumentGraphData graphDataO[], InstrumentGraphData graphDataL[], InstrumentGraphData graphDataH[], InstrumentGraphData graphDataC[]){        
        return "SUCCESS";        
    }     

    public static void main(String [] args){
        /*Original test:
        System.out.println("lets start graphing");
        InstrumentGraphData[] graphs = new InstrumentGraphData[2];
        Instrument ADI = new Instrument("ADI");
        Instrument BCX = new Instrument("BCX");
        System.out.println(ADI.loadInstrumentHist("2013-08-07","2014-05-26"));
        System.out.println(BCX.loadInstrumentHist("2013-08-07","2014-05-26"));
        InstrumentGraphData ADIData = ADI.getRulingPricegraphData();
        InstrumentGraphData BCXData = BCX.getRulingPricegraphData();
        graphs[0] = ADIData;
        graphs[1] = BCXData;
        InstrumentGraphDataLine[] ADILines = ADIData.getDataLines();
        for (int i = 0; i < ADILines.length; i++){
            System.out.println("date: "+ ADILines[i].getDataDate()+" ruling: "+ADILines[i].getLineValue0());
        }
        
        GraphCreator graph1 = new GraphCreator();

        String createTimeLine = graph1.createTimeLine(graphs, GraphTypes.MULTI);
        /*
        int[] rP = ADI.getIntRulingPrice();
        Date[] dates = ADI.getDates();

        for (int i= 0; i<rP.length;i++){
            System.out.println("Index: "+i+" value: "+rP[i]+" For date: "+dates[i]);
        }
        /
        */
        /* Test the method createGraphData for graphs based on data run on the fly: (It works :)*/
        
        GraphCreator graph1 = new GraphCreator();
        ProcessData onTheFly = new ProcessData();
        Instrument ADI = new Instrument("ADI");
        double[] resultant = new double[1000];
        onTheFly.calcRSI(14, "2013-08-07", "2014-05-26", resultant, ADI);
        Instrument ADI1 = new Instrument("ADI");
        double[] resultant1 = new double[1000];
        
        onTheFly.calcRSI(133, "2013-08-07", "2014-05-26", resultant1, ADI1);
        System.out.println("resultant1: "+resultant1.length);
        System.out.println("ADI1.getDates(): "+ADI1.getDates().length);
        
        InstrumentGraphData[] graphs = new InstrumentGraphData[2];
        graphs[0] = graph1.createGraphData(resultant, ADI.getDates(), "ADI-RSI14", 14);
        graphs[1] = graph1.createGraphData(resultant, ADI.getDates(), "ADI-RSI20", 133);
        
        System.out.println(graphs[0].getDataLines().length);
        String createTimeLine = graph1.createTimeLine(graphs, GraphTypes.MULTI);
    }

}
