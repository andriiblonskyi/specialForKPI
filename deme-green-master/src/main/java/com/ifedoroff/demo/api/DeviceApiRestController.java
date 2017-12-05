package com.ifedoroff.demo.api;

import com.ifedoroff.demo.Constants;
import com.ifedoroff.demo.model.device.Device;
import com.ifedoroff.demo.repository.DeviceRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * Created by Rostik on 03.08.2017.
 */
@RestController
@RequestMapping("/api")
public class DeviceApiRestController {

    @RequestMapping(method = RequestMethod.GET, value = "/devicesList")
    List<Device> getDevicesList() {
        DeviceRepository deviceRepository = new DeviceRepository();
        List<Device> deviceList = deviceRepository.findByOwner(Constants.USER_NAME);
        System.out.println(new Date(System.currentTimeMillis()).toString() + " [API] Selected " + deviceList.size() + " devices;");
        return  deviceList;
    }


}
