package com.ifedoroff.demo.repository;

import com.ifedoroff.demo.Constants;
import com.ifedoroff.demo.model.device.Device;
import com.ifedoroff.demo.security.SpringMongoConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Rostik on 30.07.2017.
 */
@Repository
public class DeviceRepository {

    static final String REPOSITORY_NAME = "devices";
    static ApplicationContext ctx =
            new AnnotationConfigApplicationContext(SpringMongoConfig.class);
    static MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

    public List<Device> findByOwner(String owner)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where("owner").is(Constants.USER_NAME));
        List<Device> deviceList = mongoOperation.find(query,Device.class,REPOSITORY_NAME);
        return  deviceList;
    }

    public int count()
    {
        return mongoOperation.findAll(Device.class,REPOSITORY_NAME).size();
    }

    @Async
    public void save(Device device)
    {
        mongoOperation.insert(device,REPOSITORY_NAME);
    }

    public List<Device> findByName(String name)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        List<Device> deviceList = mongoOperation.find(query,Device.class,REPOSITORY_NAME);
        return  deviceList;
    }

    public boolean updateName(String deviceId, String name)
    {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("deviceId").is(deviceId));
            Device device = mongoOperation.findOne(query,Device.class,REPOSITORY_NAME);
            device.setName(name);
            mongoOperation.save(device,REPOSITORY_NAME);
            return true;
        }
        catch (Exception ex)
        {
            System.out.println(new Date(System.currentTimeMillis()).toString() + " " + ex.getMessage());
            return false;
        }

    }

    public void updateCount(String deviceId,int count)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where("deviceId").is(deviceId));
        Device device = mongoOperation.findOne(query,Device.class,REPOSITORY_NAME);
        device.setSensorsCount(count);
        mongoOperation.save(device,REPOSITORY_NAME);
    }
}
