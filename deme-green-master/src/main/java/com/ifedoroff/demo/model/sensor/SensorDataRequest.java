package com.ifedoroff.demo.model.sensor;

/**
 * Created by Rostik on 29.07.2017.
 */
public class SensorDataRequest {
    //change to id
    private String owner;
    private String type;

    public String getOwner() {
        return owner;
    }

    public String getType() {
        return type;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setType(String type) {
        this.type = type;
    }
}
