package com.ifedoroff.demo.services;

import com.ifedoroff.demo.Constants;
import com.ifedoroff.demo.model.device.Device;
import com.ifedoroff.demo.model.notification.Notification;
import com.ifedoroff.demo.model.sensor.CriticalSensorValue;
import com.ifedoroff.demo.model.sensor.Sensor;
import com.ifedoroff.demo.model.sensor.SensorParameter;
import com.ifedoroff.demo.repository.CriticalValueRepository;
import com.ifedoroff.demo.repository.DeviceRepository;
import com.ifedoroff.demo.repository.SensorParameterRepository;
import com.ifedoroff.demo.repository.SensorRepository;
import com.ifedoroff.demo.tools.*;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rostik on 31.07.2017.
 */
@Service
public class DataHandlerService {

    private static Map<Integer,String> packetValueToType = new HashMap<>();

    static {
        packetValueToType.put(1,"temperature");
        packetValueToType.put(2,"humidity");
        packetValueToType.put(3,"illumination");
        packetValueToType.put(4,"soil");
    }

    static SimpleDateFormat yearFormat = new SimpleDateFormat("dd-MM-yyyy");
    static SimpleDateFormat hourFormat = new SimpleDateFormat("HH");

    public void handleValue(String packet)
    {
        try {
            System.out.println(new Date(System.currentTimeMillis()).toString() +" [DATA HANDLER] Handle package " + packet);
            int[] values = stringToInt(packet);

            String type = getType(values[0]);
            long idSensor = getIdSensor(values[0],values[1],values[2],values[3]);
            int value = getValue(values[4],type);
            int voltage = getVoltage(values[5]);
            int id = values[6];

            String repoName = type.equals("temperature") ? RepositoryNameFormater.formatTemperatureRepositoryName(String.valueOf(idSensor)): type.equals("soil") ? RepositoryNameFormater.formatSoilRepositoryName(String.valueOf(idSensor)): type.equals("humidity") ? RepositoryNameFormater.formatHumidityRepositoryName(String.valueOf(idSensor)): type.equals("illumination") ? RepositoryNameFormater.formatIlluminationRepositoryName(String.valueOf(idSensor)) : "none";
            System.out.println(new Date(System.currentTimeMillis()).toString() +" [DATA HANDLER] VALUE repoName:["+repoName+"] value:[" + value + "] sensorId:[" +idSensor + "];" );

            DeviceRepository deviceRepository = new DeviceRepository();
            SensorRepository sensorRepository = new SensorRepository();

            if (!DevicesExistingCache.contains(String.valueOf(id)))
            {
                Device device = new Device();
                device.setName("Шлюз "+ deviceRepository.count());
                device.setOwner(Constants.USER_NAME);
                device.setDeviceId(String.valueOf(id));
                device.setSensorsCount(1);
                device.setStatus(true);

                deviceRepository.save(device);
                DevicesExistingCache.put(String.valueOf(id));
            }
            else
            {
                if (!SensorsExistingCache.contains(String.valueOf(idSensor)))
                {
                    int sensorsCount = sensorRepository.findByOwner(String.valueOf(id)).size() + 1;
                    deviceRepository.updateCount(String.valueOf(id),sensorsCount);
                }
            }
            CriticalValueRepository criticalValueRepository = new CriticalValueRepository();
            if (!SensorsExistingCache.contains(String.valueOf(idSensor)))
            {
                Sensor sensor = new Sensor();
                sensor.setSensorId(String.valueOf(idSensor));
                sensor.setOwner(String.valueOf(id));
                sensor.setType(type);
                sensorRepository.save(sensor);
                SensorsExistingCache.put(String.valueOf(idSensor));


                CriticalSensorValue criticalSensorValue = new CriticalSensorValue();
                criticalSensorValue.setSensorId(String.valueOf(idSensor));
                criticalSensorValue.setMax(CriticalValues.getMaxValue(type));
                criticalSensorValue.setMin(CriticalValues.getMinValue(type));
                criticalValueRepository.save(criticalSensorValue);
                checkValue(criticalSensorValue,value,type);
            }
            else
            {
                CriticalSensorValue criticalSensorValue =  criticalValueRepository.findById(String.valueOf(idSensor));
                checkValue(criticalSensorValue,value,type);
            }

            SensorParameter parameter = new SensorParameter(value,voltage);
            Date currentDate = new Date(System.currentTimeMillis());
            System.out.println(new Date(System.currentTimeMillis()).toString() +" [DATA HANDLER] Current date:" + currentDate.toString() );
            parameter.setAdditionsDate(yearFormat.format(currentDate));
            parameter.setBelongingHour(hourFormat.format(currentDate));


            SensorParameterRepository sensorParameterRepository = new SensorParameterRepository();
            sensorParameterRepository.save(parameter,repoName);

            Sensor sensor = new Sensor();
            sensor.setType(type);
            sensor.setOwner(Integer.toString(id));
            sensor.setSensorId(String.valueOf(idSensor));

            DataHandlerCache.put(sensor,parameter);
        }
        catch (Exception ex)
        {
            System.out.println(ex.toString());
        }

    }

    private void checkValue(CriticalSensorValue criticalSensorValue, int value,String type)
    {
        String title = NotificationMessageFormater.getTitle(criticalSensorValue.getSensorId());
        AndroidNotificationService androidNotificationService = new AndroidNotificationService();
        if (value >= criticalSensorValue.getMax())
        {
            String message = NotificationMessageFormater.getCriticalMessageMax(type,value);
            androidNotificationService.send(title,message);
            System.out.println(new Date(System.currentTimeMillis()).toString() +" [DATA HANDLER] value >= critical");
            Notification notification = new Notification();
            notification.setMsg(message);
            notification.setSensorType(type);
            notification.setUser(Constants.USER_NAME);
            notification.setDeviceId("1");
            notification.setTitle(title);
            UnreadNotificationCache.put(notification);
        }
        else if (value <= criticalSensorValue.getMin())
        {
            String message = NotificationMessageFormater.getCriticalMessageMin(type,value);
            androidNotificationService.send(title,message);
            System.out.println(new Date(System.currentTimeMillis()).toString() +" [DATA HANDLER] value <= critical");
            Notification notification = new Notification();
            notification.setMsg(message);
            notification.setSensorType(type);
            notification.setUser(Constants.USER_NAME);
            notification.setDeviceId("1");
            notification.setTitle(title);
            UnreadNotificationCache.put(notification);
        }
    }

    private int[] stringToInt(String str)
    {
        String[] array = str.split(":");
        return Arrays.asList(array).stream().mapToInt(Integer::parseInt).toArray();
    }

    private long getIdSensor(int x0,int x1,int x2,int x3)
    {
        final long value = (long)x0*1000000000 + (long)x1*1000000 + (long)x2*1000+(long)x3;
        return value;
    }

    private int getId(int x1,int x2)
    {
        return x1*1000000 + x2*1000;
    }

    private String getType(int x)
    {
        return packetValueToType.getOrDefault(x,"");
    }
    private int getValue(int x,String type)
    {
        return type.equals("temperature") ? x-128: type.equals("soil") ? x-128: type.equals("humidity") ? x: type.equals("illumination") ? x*x : -1;
    }

    private int getVoltage(int x)
    {
        return (x+190)*10;
    }
}
