package com.ifedoroff.demo.tools;

/**
 * Created by Rostik on 31.07.2017.
 */
public class RepositoryNameFormater {

    public static String formatHumidityRepositoryName(String deviceId) {return "humidity"+"["+deviceId+"]";}
    public static String formatSoilRepositoryName(String deviceId)
    {
        return "soil"+"["+deviceId+"]";
    }
    public static String formatTemperatureRepositoryName(String deviceId) {return "temperature"+"["+deviceId+"]";}
    public static String formatIlluminationRepositoryName(String deviceId) {return "illumination"+"["+deviceId+"]";}
    public static String formatCustomRepositoryName(String type,String deviceId) {return type+"["+deviceId+"]";}
}
