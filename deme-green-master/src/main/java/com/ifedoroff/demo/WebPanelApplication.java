package com.ifedoroff.demo;

import com.ifedoroff.demo.model.device.Device;
import com.ifedoroff.demo.model.sensor.Sensor;
import com.ifedoroff.demo.repository.DeviceRepository;
import com.ifedoroff.demo.repository.SensorRepository;
import com.ifedoroff.demo.services.DataServerManagerService;
import com.ifedoroff.demo.services.SensorStatusMonitorService;
import com.ifedoroff.demo.tools.DevicesExistingCache;
import com.ifedoroff.demo.tools.SensorsExistingCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class WebPanelApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebPanelApplication.class, args);

		try {
			loadDevices();
			loadSensors();
			SensorStatusMonitorService.start();
			DataServerManagerService.start();
			DataServer.run();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	static void loadDevices()
	{
		DeviceRepository deviceRepository = new DeviceRepository();
		List<Device> deviceList = deviceRepository.findByOwner(Constants.USER_NAME);
		for (Device device : deviceList)
			DevicesExistingCache.put(device.getDeviceId());
	}

	static void loadSensors()
	{
		SensorRepository sensorRepository = new SensorRepository();
		List<Sensor> sensorList = sensorRepository.findAll();
		for(Sensor sensor : sensorList)
			SensorsExistingCache.put(sensor.getSensorId());
	}


}
