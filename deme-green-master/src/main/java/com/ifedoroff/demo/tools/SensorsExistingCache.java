package com.ifedoroff.demo.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rostik on 06.08.2017.
 */
public class SensorsExistingCache {
    private static List<String> sensorsId = new ArrayList<>();

    public static void put(String id)
    {
        if (!sensorsId.contains(id))
            sensorsId.add(id);
    }

    public static boolean contains(String id)
    {
        return sensorsId.contains(id);
    }

}
