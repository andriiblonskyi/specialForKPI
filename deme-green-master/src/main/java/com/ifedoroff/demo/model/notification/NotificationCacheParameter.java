package com.ifedoroff.demo.model.notification;


/**
 * Created by Rostik on 28.07.2017.
 */
public class NotificationCacheParameter {
    private String deviceId;

    public NotificationCacheParameter(String deviceId)
    {
        this.deviceId = deviceId;
    }


    public String getDeviceId() {
        return deviceId;
    }
}
