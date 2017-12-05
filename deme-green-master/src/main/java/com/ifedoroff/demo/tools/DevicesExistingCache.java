package com.ifedoroff.demo.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rostik on 06.08.2017.
 */
public class DevicesExistingCache {
    private static List<String> devicesId = new ArrayList<>();

    public static void put(String id)
    {
        if (!devicesId.contains(id))
            devicesId.add(id);
    }

    public static boolean contains(String id)
    {
        return devicesId.contains(id);
    }
}
