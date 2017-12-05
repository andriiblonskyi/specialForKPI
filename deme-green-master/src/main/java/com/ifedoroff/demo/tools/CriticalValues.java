package com.ifedoroff.demo.tools;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rostik on 07.08.2017.
 */
public class CriticalValues {
    static Map<String,Integer> minValues = new HashMap<>();
    static Map<String,Integer> maxValues = new HashMap<>();

    static
    {
        minValues.put("temperature",15);
        maxValues.put("temperature",30);

        minValues.put("soil",15);
        maxValues.put("soil",25);

        minValues.put("humidity",55);
        maxValues.put("humidity",75);


        minValues.put("illumination",6);
        maxValues.put("illumination",25);


    }

    public static int getMinValue(String type)
    {
        return  minValues.get(type);
    }

    public static int getMaxValue(String type)
    {
        return maxValues.get(type);
    }
}
