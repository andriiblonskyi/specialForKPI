package com.ifedoroff.demo.tools;

import com.ifedoroff.demo.model.sensor.Sensor;
import com.ifedoroff.demo.model.sensor.SensorParameter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rostik on 30.07.2017.
 */
public class DataHandlerCache {

    private static Map<Sensor,SensorParameter> cache = new HashMap<>();


    public static SensorParameter getValue(Sensor sensor)
    {
        for (Sensor key : cache.keySet())
        {
            if (key.getSensorId().equals(sensor.getSensorId()))
                return cache.get(key);
        }
        return null;
    }

    public static Map<Sensor,SensorParameter> getCache()
    {
        return cache;
    }

    public static Map<Sensor,SensorParameter> getCacheByDeviceId(String deviceId)
    {
        Map<Sensor,SensorParameter> tmp = new HashMap<>();
        for (Map.Entry<Sensor,SensorParameter> item : cache.entrySet())
        {
            if (item.getKey().getOwner().equals(deviceId))
                tmp.put(item.getKey(),item.getValue());
        }
        return tmp;
    }

    public static void put(Sensor sensor, SensorParameter value)
    {
        try {
        Sensor key = null;
        for (Map.Entry<Sensor,SensorParameter> item : cache.entrySet())
        {
            if (item.getKey().getSensorId().equals(sensor.getSensorId()))
            {
                key = item.getKey();
                break;
            }
        }
        if (key != null)
            cache.remove(key);
        cache.put(sensor,value);
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }

    }


}
