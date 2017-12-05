package com.ifedoroff.demo.model.sensor;

/**
 * Created by Rostik on 03.08.2017.
 */
public class SensorResponse {
    private int value;
    private String date;

    public SensorResponse(int value, String date)
    {
        this.value = value;
        this.date = date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
