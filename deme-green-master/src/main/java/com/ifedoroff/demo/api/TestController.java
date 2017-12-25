package com.ifedoroff.demo.api;

import com.ifedoroff.demo.model.sensor.CriticalSensorValue;
import com.ifedoroff.demo.repository.CriticalValueRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("api/store")
public class TestController {


    @RequestMapping(method = RequestMethod.GET, value = "/getAllCritical")
    List<CriticalSensorValue> storeData() {
        CriticalValueRepository criticalValueRepository = new CriticalValueRepository();
        return criticalValueRepository.findAll();
    }



}
