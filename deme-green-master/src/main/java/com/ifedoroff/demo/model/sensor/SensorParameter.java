package com.ifedoroff.demo.model.sensor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Rostik on 27.07.2017.
 */
@Document
public class SensorParameter {

    @Id
    private String id;

    private int value;
    private String belongingHour;
    private String additionsDate;

    private int voltage;
    public SensorParameter (int value,int voltage)
    {
        this.value = value;
        this.voltage = voltage;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    public void setAdditionsDate(String additionsDate) {
        this.additionsDate = additionsDate;
    }

    public void setBelongingHour(String belongingHour) {
        this.belongingHour = belongingHour;
    }

    public String getId() {
        return id;
    }

    public int getVoltage() {
        return voltage;
    }


    public int getValue() {
        return value;
    }

    public String getAdditionsDate() {
        return additionsDate;
    }

    public String getBelongingHour() {
        return belongingHour;
    }
}
