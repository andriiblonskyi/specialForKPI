package com.ifedoroff.demo.model.sensor;

/**
 * Created by Rostik on 06.08.2017.
 */
public class SensorsValuesResponse {
    String id;
    String type;
    int value;

    public SensorsValuesResponse(String id,String type,int value)
    {
        this.id = id;
        this.type = type;
        this.value = value;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    public String getType() {
        return type;
    }
}
