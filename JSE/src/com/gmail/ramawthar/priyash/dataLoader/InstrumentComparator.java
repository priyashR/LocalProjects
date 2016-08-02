package com.gmail.ramawthar.priyash.dataLoader;

import com.gmail.ramawthar.priyash.dataConsumer.InstrumentFormulaData;

import java.util.Comparator;

public class InstrumentComparator implements Comparator<InstrumentFormulaData>{
    private ComparatorKeys comparatorKey = null;
    public InstrumentComparator(ComparatorKeys comparatorKey) {
        this.comparatorKey = comparatorKey;
    }

    public void setComparatorKey(ComparatorKeys comparatorKey) {
        this.comparatorKey = comparatorKey;
    }

    public ComparatorKeys getComparatorKey() {
        return comparatorKey;
    }
    private int compareRSI(InstrumentFormulaData ifd, InstrumentFormulaData ifd1){
        if (ifd.getDataForDate(ifd.getToDate()).getColumnVal("RSI14") > ifd1.getDataForDate(ifd1.getToDate()).getColumnVal("RSI14"))
            return 1;
        //else if (ifd.getDataForDate(ifd.getToDate()).getColumnVal("RSI14") < ifd1.getDataForDate(ifd1.getToDate()).getColumnVal("RSI14"))
            return -1;        
    }
    // Overriding the compare method to sort the age 
    public int compare(InstrumentFormulaData ifd, InstrumentFormulaData ifd1){
        
        if (comparatorKey == null)
            return 0;
        //put case here for each comparator key
        if (comparatorKey == ComparatorKeys.PERCCHANGE){
            return compareRSI(ifd, ifd1);
        }
        
        //add more intelligent logic here for comparisons

        
        return 0;
    }    
}
