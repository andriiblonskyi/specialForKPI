package com.ifedoroff.demo.model.notification;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Rostik on 31.07.2017.
 */
@Document(collection = "notifications")
public class Notification {
    @Id
    private String id;
    private String user;
    private String deviceId;
    private String sensorType;
    private String msg;
    private String title;


    public String getDeviceId() {
        return deviceId;
    }

    public String getSensorType() {
        return sensorType;
    }

    public String getMsg() {
        return msg;
    }

    public String getTitle() {
        return title;
    }

    public String getUser() {
        return user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
