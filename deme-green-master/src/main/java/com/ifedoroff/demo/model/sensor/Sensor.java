package com.ifedoroff.demo.model.sensor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Rostik on 30.07.2017.
 */
@Document(collection = "sensors")
public class Sensor {
    @Id
    private String _id;

    private String owner;
    private String type;
    private String sensorId;

    public String getSensorId() {
        return sensorId;
    }

    public String getOwner() {
        return owner;
    }

    public String getType() {
        return type;
    }

    public String get_Id() {
        return _id;
    }

    public void set_Id(String _id) {
        this._id = _id;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }
}
