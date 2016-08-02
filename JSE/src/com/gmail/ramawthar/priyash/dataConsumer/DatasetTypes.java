package com.gmail.ramawthar.priyash.dataConsumer;

public enum DatasetTypes {
    RULINGPRICE, RSI14,RSI,OLHC;//add formula types in here
    public static DatasetTypes getDatasetFromString(String formula){
        if (formula.equalsIgnoreCase("RSI")){
            return DatasetTypes.RSI;
        }else if(formula.equalsIgnoreCase("RULINGPRICE")){
            return DatasetTypes.RULINGPRICE;
        }else if(formula.equalsIgnoreCase("RSI14")){
            return DatasetTypes.RSI14;
        }else if(formula.equalsIgnoreCase("OLHC")){
            return DatasetTypes.OLHC;
        }
        return null;
    }
}
