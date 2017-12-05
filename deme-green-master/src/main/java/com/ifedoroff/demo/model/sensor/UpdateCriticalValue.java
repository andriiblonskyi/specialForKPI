package com.ifedoroff.demo.model.sensor;

/**
 * Created by Rostik on 08.08.2017.
 */
public class UpdateCriticalValue {
    private String sensorId;
    private int value;

    public String getSensorId() {
        return sensorId;
    }

    public int getValue() {
        return value;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
