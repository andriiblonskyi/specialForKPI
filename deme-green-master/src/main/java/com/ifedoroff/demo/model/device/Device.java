package com.ifedoroff.demo.model.device;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Rostik on 29.07.2017.
 */
@Document(collection = "devices")
public class Device {

    @Id
    private String id;

    private String deviceId;
    private boolean status;
    private String name;
    private int sensorsCount;
    private String owner;

    public String getDeviceId() {
        return deviceId;
    }

    public int getSensorsCount() {
        return sensorsCount;
    }

    public String getOwner() {
        return owner;
    }

    public boolean getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setSensorsCount(int sensorsCount) {
        this.sensorsCount = sensorsCount;
    }

    public void setStatus(boolean enabled) {
        this.status = enabled;
    }

    public void setId(String id) {
        this.id = id;
    }
}
