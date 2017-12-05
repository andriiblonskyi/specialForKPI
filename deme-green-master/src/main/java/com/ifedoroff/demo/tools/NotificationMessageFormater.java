package com.ifedoroff.demo.tools;

import java.util.Date;

/**
 * Created by Rostik on 31.07.2017.
 */
public class NotificationMessageFormater {

    public  static String getCriticalMessageMin( String type, int value)
    {
        String sensorType = type.equals("temperature") ? "температуры воздуха": type.equals("soil") ? "температуры земли": type.equals("humidity") ? "влажности": type.equals("illumination") ? "освещенности" : "<default>";
        return "On sensor "+ type+" value [" + value + "] ( <= critical minimum)";
    }
    public  static String getCriticalMessageMax( String type, int value)
    {
        String sensorType = type.equals("temperature") ? "температуры воздуха": type.equals("soil") ? "температуры земли": type.equals("humidity") ? "влажности": type.equals("illumination") ? "освещенности" : "<default>";
        return "On sensor "+ type+" value [" + value + "] ( >= critical minimum)";
    }
    public static String getTitle(String sensorId)
    {
        return "Sensor [" + sensorId + "] " + new Date(System.currentTimeMillis()).toString();
    }

    public static String getDisableMessage( String type)
    {
        String sensorType = type.equals("temperature") ? "температуры воздуха": type.equals("soil") ? "температуры земли": type.equals("humidity") ? "влажности": type.equals("illumination") ? "освещенности" : "<default>";
        return "Sensor "+ type+" is offline.";
    }
}
