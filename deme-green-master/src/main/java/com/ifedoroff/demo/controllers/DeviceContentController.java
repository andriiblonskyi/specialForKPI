package com.ifedoroff.demo.controllers;

import com.ifedoroff.demo.Constants;
import com.ifedoroff.demo.model.device.ChangeNameRequest;
import com.ifedoroff.demo.model.device.Device;
import com.ifedoroff.demo.repository.DeviceRepository;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/content")
public class DeviceContentController {

    @RequestMapping(method = RequestMethod.GET, value = "/loadDevicesList")
    String loadDevices() {
        DeviceRepository deviceRepository = new DeviceRepository();
        JSONObject jsonObject = new JSONObject();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Device> deviceList = deviceRepository.findByOwner(auth.getName());
        for (Device device : deviceList)
            jsonObject.append(device.getName(),new JSONObject(device));
        //
        String jsonStr = jsonObject.toString();
        return jsonStr;
    }

   @RequestMapping(method = RequestMethod.GET, value = "/devicesList")
   List<Device> getDevicesList() {
       DeviceRepository deviceRepository = new DeviceRepository();
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       List<Device> deviceList = deviceRepository.findByOwner(Constants.USER_NAME);
       return  deviceList;
   }

    @PostMapping("/changeName")
    boolean getSensorData(@RequestBody ChangeNameRequest changeNameRequest, Errors errors) {
        DeviceRepository deviceRepository = new DeviceRepository();
        return deviceRepository.updateName(changeNameRequest.getId(),changeNameRequest.getName());
    }



}
