package com.gmail.ramawthar.priyash.dataLoader;

import java.sql.Connection;

import java.util.List;

public interface ExceptionLogger {
    public void addLog(String messageType, String messageValue);
    public void infoLog(String messageValue, ExceptionFlags overrideFlag);
    public void infoLog(String messageValue);
    public void commitToDataStore();
    public void setCacheSize(int cacheSize);
    public int getCacheSize();
    public List<String[][]> getLog();
    public List<String[][]> getFullBatchLog(String batchId);
    public String getCurrentBatchId();
    public void setConn(Connection conn);
}
