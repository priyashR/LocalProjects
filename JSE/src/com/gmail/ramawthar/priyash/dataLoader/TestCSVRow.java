package com.gmail.ramawthar.priyash.dataLoader;

import java.sql.Connection;

import java.util.List;
import java.util.Vector;

public class TestCSVRow implements CSVRow{
    private String     name;
    private int        age;
    private float      shoeSize;

    @Override
    public void constructFromStrings(List<String> values, ExceptionLogger ex) {

        String _name      = values.get(0);
        int _age;
        if (values.get(1).equals("")){
            _age = 0;
        }else{
            _age = Integer.parseInt(values.get(1));
        }
        float  _shoeSize  = Float.parseFloat(values.get(2));

        this.name = _name;
        this.age = _age;
        this.shoeSize = _shoeSize;
        
    }

    @Override
    public List<String> getValues() {
        Vector<String> values = new Vector<String>();
        values.add(name);
        values.add(Integer.toString(age));
        values.add(Float.toString(shoeSize));
        return values;   
    }
    
    public String uploadData(JSEUtil util, String currentDate){
        return null;
    }

}
