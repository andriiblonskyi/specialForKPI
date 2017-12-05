package com.ifedoroff.demo.repository;

import com.ifedoroff.demo.model.sensor.CriticalSensorValue;
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
 * Created by Rostik on 07.08.2017.
 */
@Repository
public class CriticalValueRepository {

    static final String REPOSITORY_NAME = "criticalValues";
    static ApplicationContext ctx =
            new AnnotationConfigApplicationContext(SpringMongoConfig.class);
    static MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

    @Async
    public void save(CriticalSensorValue criticalSensorValue)
    {
        mongoOperation.insert(criticalSensorValue,REPOSITORY_NAME);
    }

    public CriticalSensorValue findById(String sensorId)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where("sensorId").is(sensorId));
        CriticalSensorValue criticalSensorValue = mongoOperation.findOne(query,CriticalSensorValue.class,REPOSITORY_NAME);
        return  criticalSensorValue;
    }
    public List<CriticalSensorValue> findAll()
    {
        List<CriticalSensorValue> criticalSensorValues = mongoOperation.findAll(CriticalSensorValue.class,REPOSITORY_NAME);
        return  criticalSensorValues;
    }

    public boolean updateMin(String sensorId, int value)
    {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("sensorId").is(sensorId));
            CriticalSensorValue criticalSensorValue = mongoOperation.findOne(query,CriticalSensorValue.class,REPOSITORY_NAME);
            criticalSensorValue.setMin(value);
            mongoOperation.save(criticalSensorValue,REPOSITORY_NAME);
            return true;
        }
        catch (Exception ex)
        {
            System.out.println(new Date(System.currentTimeMillis()).toString() + " " + ex.getMessage());
            return false;
        }
    }

    public boolean updateMax(String sensorId, int value)
    {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("sensorId").is(sensorId));
            CriticalSensorValue criticalSensorValue = mongoOperation.findOne(query,CriticalSensorValue.class,REPOSITORY_NAME);
            criticalSensorValue.setMax(value);
            mongoOperation.save(criticalSensorValue,REPOSITORY_NAME);
            return true;
        }
        catch (Exception ex)
        {
            System.out.println(new Date(System.currentTimeMillis()).toString() + " " + ex.getMessage());
            return false;
        }
    }


}
