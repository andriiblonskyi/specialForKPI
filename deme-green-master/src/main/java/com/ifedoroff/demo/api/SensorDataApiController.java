package com.ifedoroff.demo.api;

import com.ifedoroff.demo.model.sensor.*;
import com.ifedoroff.demo.repository.CriticalValueRepository;
import com.ifedoroff.demo.repository.SensorParameterRepository;
import com.ifedoroff.demo.repository.SensorRepository;
import com.ifedoroff.demo.tools.DataHandlerCache;
import com.ifedoroff.demo.tools.RepositoryNameFormater;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Rostik on 07.08.2017.
 */
@RestController
@RequestMapping("/api/{sensorId}")
public class SensorDataApiController {

    static SensorParameterRepository sensorParameterRepository = new SensorParameterRepository();

    @RequestMapping(method = RequestMethod.GET, value = "/last")
    SensorResponse getLast(@PathVariable String sensorId) {

        Sensor sensor = new Sensor();
        sensor.setSensorId(sensorId);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        SensorParameter parameter = DataHandlerCache.getValue(sensor);
        if (parameter != null)
        {
            ObjectId id = new ObjectId(parameter.getId());
            SensorResponse response = new SensorResponse(parameter.getValue(),format.format(id.getDate()));
            System.out.println(new Date(System.currentTimeMillis()).toString() +" [API] Selected last value [" + parameter.getValue() + "] date:["+format.format(id.getDate())+"] sensors for " + "[" +sensorId + "];");
            return  response;
        }
        else
        {
            System.out.println(new Date(System.currentTimeMillis()).toString() +" [API] Selected value not exist " + "[" +sensorId + "];");
            return null;
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/day")
    List<SensorResponse> getDay(@PathVariable String sensorId) {

        try {
            SensorRepository sensorRepository = new SensorRepository();
            Sensor sensor = sensorRepository.findBySensorId(sensorId);

            List<SensorResponse> list = sensorParameterRepository.findDay(RepositoryNameFormater.formatCustomRepositoryName(sensor.getType(),sensorId));
            System.out.println(new Date(System.currentTimeMillis()).toString() +" [API] Selected "+list.size()+" approximated values for day [" +sensorId + "];");
            return list;
        }
        catch (Exception ex)
        {
            System.out.println(new Date(System.currentTimeMillis()).toString() + " [API] Error searching for day approximate ["+sensorId + "]:\n" + ex.getMessage() + "\n" + ex.getLocalizedMessage());
            return  null;
        }

    }


    @RequestMapping(method = RequestMethod.GET, value = "/week")
    List<SensorResponse> getWeek(@PathVariable String sensorId) {
        try {
            SensorRepository sensorRepository = new SensorRepository();
            Sensor sensor = sensorRepository.findBySensorId(sensorId);
            List<SensorResponse> list = sensorParameterRepository.findWeek(RepositoryNameFormater.formatCustomRepositoryName(sensor.getType(),sensorId));
            System.out.println(new Date(System.currentTimeMillis()).toString() + " [API] Selected "+list.size()+" approximated values for week [" +sensorId + "];");
            return list;
        }
        catch (Exception ex)
        {
            System.out.println(new Date(System.currentTimeMillis()).toString() + " [API] Error searching for week approximate ["+sensorId + "]:\n" + ex.getMessage() + "\n" + ex.getLocalizedMessage());
            return  null;
        }

    }

    @RequestMapping(method = RequestMethod.GET, value = "/criticalValues")
    CriticalSensorValue getCriticalValue(@PathVariable String sensorId) {
        try {
            CriticalValueRepository criticalValueRepository = new CriticalValueRepository();
            CriticalSensorValue criticalSensorValue = criticalValueRepository.findById(sensorId);
            System.out.println(new Date(System.currentTimeMillis()).toString() + " [API] Selected min["+criticalSensorValue.getMin() +"], max["+criticalSensorValue.getMax()+ "] critical values [" +sensorId + "];");
            return criticalSensorValue;
        }
        catch (Exception ex)
        {
            System.out.println( new Date(System.currentTimeMillis()).toString() + " [API] Error searching for week approximate ["+sensorId + "]:\n" + ex.getMessage() + "\n" + ex.getLocalizedMessage());
            return  null;
        }


    }

}
