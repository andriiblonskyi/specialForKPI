package com.ifedoroff.demo.controllers;

import com.ifedoroff.demo.model.sensor.*;
import com.ifedoroff.demo.repository.CriticalValueRepository;
import com.ifedoroff.demo.repository.SensorParameterRepository;
import com.ifedoroff.demo.repository.SensorRepository;
import com.ifedoroff.demo.tools.DataHandlerCache;
import com.ifedoroff.demo.tools.RepositoryNameFormater;
import org.json.JSONObject;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/content")
public class SensorContentController {

    static SensorRepository sensorRepository = new SensorRepository();

    @PostMapping("/sensorsList")
    public List<Sensor> getSensorsList(@RequestBody String deviceId, Errors errors) {
        List<Sensor> sensorList = sensorRepository.findByOwner(deviceId);

        System.out.println(new Date(System.currentTimeMillis()).toString() +" [CONTROLLER] Selected "+sensorList.size() + " sensors;");

        return sensorList;
    }

    @PostMapping("/sensorData")
    int getSensorData(@RequestBody String sensorId, Errors errors) {
        JSONObject jsonObject = new JSONObject();
        Sensor sensor = new Sensor();
        sensor.setSensorId(sensorId);
        jsonObject.append("value", DataHandlerCache.getValue(sensor).getValue());
        SensorParameter parameter = DataHandlerCache.getValue(sensor);
        if (parameter == null)
            return 0;
        String jsonStr = jsonObject.toString();
        return parameter.getValue();
    }

    @PostMapping("/dayof")
    List<SensorResponse> getDayOf(@RequestBody SensorDayOfCriteria search, Errors errors) {

        try {
            SensorRepository sensorRepository = new SensorRepository();
            Sensor sensor = sensorRepository.findBySensorId(search.getSensorId());
            SensorParameterRepository sensorParameterRepository = new SensorParameterRepository();
            List<SensorResponse> list = sensorParameterRepository.findDay(RepositoryNameFormater.formatCustomRepositoryName(sensor.getType(),search.getSensorId()),search.getPattern());
            System.out.println(new Date(System.currentTimeMillis()).toString() +" [API] Selected "+list.size()+" approximated values for dayof [" +search.getSensorId() + "];");
            return list;
        }
        catch (Exception ex)
        {
            System.out.println(new Date(System.currentTimeMillis()).toString() + " [API] Error searching for dayof approximate ["+search.getSensorId() + "]:\n" + ex.getMessage() + "\n" + ex.getLocalizedMessage());
            return  null;
        }

    }



    @PostMapping("/updateMin")
    boolean updateMinValue(@RequestBody UpdateCriticalValue search, Errors errors) {
        try {
            CriticalValueRepository criticalValueRepository = new CriticalValueRepository();
            criticalValueRepository.updateMin(search.getSensorId(),search.getValue());
            return true;
        }
        catch (Exception ex)
        {
            System.out.println(new Date(System.currentTimeMillis()).toString() +" [CONTROLLER] Error update min ["+search.getSensorId() + "]:\n" + ex.getMessage() + "\n" + ex.getLocalizedMessage());
            return false;
        }


    }

    @PostMapping("/updateMax")
    boolean updateMaxValue(@RequestBody UpdateCriticalValue search, Errors errors) {
        try {
            CriticalValueRepository criticalValueRepository = new CriticalValueRepository();
            criticalValueRepository.updateMax(search.getSensorId(),search.getValue());
            return true;
        }
        catch (Exception ex)
        {
            System.out.println(new Date(System.currentTimeMillis()).toString() +" [CONTROLLER] Error update max ["+search.getSensorId() + "]:\n" + ex.getMessage() + "\n" + ex.getLocalizedMessage());

            return false;
        }

    }



}
