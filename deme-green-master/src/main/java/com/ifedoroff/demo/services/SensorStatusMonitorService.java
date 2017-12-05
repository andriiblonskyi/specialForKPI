package com.ifedoroff.demo.services;

import com.ifedoroff.demo.model.sensor.Sensor;
import com.ifedoroff.demo.model.sensor.SensorParameter;
import com.ifedoroff.demo.repository.SensorParameterRepository;
import com.ifedoroff.demo.tools.DataHandlerCache;
import com.ifedoroff.demo.tools.NotificationMessageFormater;
import com.ifedoroff.demo.tools.RepositoryNameFormater;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by Rostik on 09.08.2017.
 */
public class SensorStatusMonitorService {

    private static Timer timer = new Timer();
    public static void start()
    {
        System.out.println(new Date(System.currentTimeMillis()).toString() + " [SENSORS STATUS MONITOR] Start status monitor..");
        timer.schedule (new MonitorTask(),0,1000*60*15);
    }

    static class MonitorTask extends TimerTask {
        public void run() {
            monitor();
        }
        private static void monitor()
        {
            System.out.println(new Date(System.currentTimeMillis()).toString() + " [SENSORS STATUS MONITOR] Start checking status sensors..");
            Map<Sensor,SensorParameter> tmp = DataHandlerCache.getCache();
            SensorParameterRepository sensorParameterRepository = new SensorParameterRepository();
            if (tmp.size() > 0)
            {
                for (Map.Entry<Sensor,SensorParameter> item : tmp.entrySet())
                {
                    Date date =sensorParameterRepository.getDate(RepositoryNameFormater.formatCustomRepositoryName(item.getKey().getType(),item.getKey().getSensorId()));
                    if (Math.abs(getDateDiff(date,new Date(),TimeUnit.MINUTES)) >= 15)
                        sendNotification(item.getKey().getSensorId(),item.getKey().getType());
                }
            }

        }

        private static void sendNotification(String sensorId,String type)
        {
            System.out.println(new Date(System.currentTimeMillis()).toString() + " [SENSORS STATUS MONITOR] Sensor with id["+sensorId+"] is disable.");
            String title = NotificationMessageFormater.getTitle(sensorId);
            String msg = NotificationMessageFormater.getDisableMessage(type);
            AndroidNotificationService androidNotificationService = new AndroidNotificationService();
            androidNotificationService.send(title,msg);
        }

        private static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
            long diffInMillies = date2.getTime() - date1.getTime();
            return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
        }
    }



}
