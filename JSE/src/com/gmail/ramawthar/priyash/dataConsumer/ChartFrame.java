package com.gmail.ramawthar.priyash.dataConsumer;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.ui.ApplicationFrame;

public class ChartFrame extends ApplicationFrame {
    public ChartFrame(String string, JPanel graphPanel) {
        super(string);
        ChartPanel chartPanel = (ChartPanel) graphPanel;
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);        
    }
}
