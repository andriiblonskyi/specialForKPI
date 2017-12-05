package com.ifedoroff.demo.api;

import com.ifedoroff.demo.model.sensor.Sensor;
import com.ifedoroff.demo.model.sensor.SensorParameter;
import com.ifedoroff.demo.model.sensor.SensorsValuesResponse;
import com.ifedoroff.demo.repository.SensorRepository;
import com.ifedoroff.demo.tools.DataHandlerCache;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Rostik on 03.08.2017.
 */
@RestController
@RequestMapping("/api/{deviceId}")
public class SensorApiRestController {


    static SensorRepository sensorRepository = new SensorRepository();

    @RequestMapping(method = RequestMethod.GET, value = "/sensorsList")
    public List<Sensor> getSensorsList(@PathVariable String deviceId) {
        List<Sensor> sensorList = sensorRepository.findByOwner(deviceId);
        System.out.println(new Date(System.currentTimeMillis()).toString() + " [API] Selected " + sensorList.size() + " sensors for " + "[" +deviceId + "];");
        return sensorList;
    }

        @RequestMapping(method = RequestMethod.GET, value = "/lastSensorsValue")
        public List<SensorsValuesResponse> getLastSensorsValueList(@PathVariable String deviceId) {
        Map<Sensor,SensorParameter> tmp = DataHandlerCache.getCacheByDeviceId(deviceId);
        List<SensorsValuesResponse> response = new ArrayList<>();
        for (Map.Entry<Sensor,SensorParameter> item : tmp.entrySet())
            response.add(new SensorsValuesResponse(item.getKey().getSensorId(),item.getKey().getType(),item.getValue().getValue()));
        System.out.println(new Date(System.currentTimeMillis()).toString() + " [API] Selected " + response.size() + " values for " + "[" +deviceId + "];");
        return  response;
    }

}
