package com.ifedoroff.demo.model.sensor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Rostik on 07.08.2017.
 */
@Document
public class CriticalSensorValue {

    @Id
    private String _id;
    private String sensorId;
    private int max;
    private int min;

    public void setMax(int max) {
        this.max = max;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    public String getSensorId() {
        return sensorId;
    }

    public String get_id() {
        return _id;
    }
}
