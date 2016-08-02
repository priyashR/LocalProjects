
package com.gmail.ramawthar.priyash.dataLoader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;

public interface JSEUtil {
    public List<CSVRow> readData(RowType rowType, String filePath) throws Exception;
    public String checkManifest(String instrument) throws SQLException;
    public void createNewInstrumentTable(String instrument);
    public Double uploadCSV(List<CSVRow> fileData, String additionalInfo);
    public Connection getConn();
    public void closeConn();
    public String getBatchId();
    public ExceptionLogger getEx();
    public void removeAllBatches(String batchId);
    public void removeBatch(String batchId, String instrument);
    public void commitUtil();
    public String addNewFormulaeColumn(String columnName, String columnType, String dynamicInd);
    public String removeFormulaeColumn(String columnName);
    public String checkFormulaeManifest(String columnName) throws SQLException;
    public String dynamicFormulaCheck(String formula) throws SQLException;
    public ResultSet getFormulaData(String instrument, String formula) throws SQLException;
    
    public String addInsToSelectionQueue(String instrument);
    public String[] getSelectionQueue();
}
