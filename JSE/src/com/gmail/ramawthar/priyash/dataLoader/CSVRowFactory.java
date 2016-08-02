package com.gmail.ramawthar.priyash.dataLoader;

import java.util.List;

public class CSVRowFactory {
    public static CSVRow retrieveCSVRow(String rowType, List<String> values, ExceptionLogger ex){
        if (rowType == "HISTORY") {
            CSVRow newRow = new HistoryCSVRow();
            newRow.constructFromStrings(values, ex);
            return newRow;
        } else if (rowType == "DAILY") {
            CSVRow newRow = new DailyCSVRow();
            newRow.constructFromStrings(values, ex);
            return newRow;
        } else if (rowType == "TEST") {
            CSVRow newRow = new TestCSVRow();
            newRow.constructFromStrings(values, ex);
            return newRow;
        }
        return null;
    }
}
