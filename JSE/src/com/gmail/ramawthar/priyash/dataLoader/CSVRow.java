package com.gmail.ramawthar.priyash.dataLoader;

import java.sql.Connection;

import java.sql.PreparedStatement;

import java.util.List;

public interface CSVRow {
    public void constructFromStrings(List<String> values, ExceptionLogger ex);
    public List<String> getValues();
    public String uploadData(JSEUtil util, String specialData);
}
