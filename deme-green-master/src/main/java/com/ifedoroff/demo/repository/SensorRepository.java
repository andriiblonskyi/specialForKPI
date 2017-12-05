package com.ifedoroff.demo.repository;

import com.ifedoroff.demo.model.sensor.Sensor;
import com.ifedoroff.demo.security.SpringMongoConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Rostik on 30.07.2017.
 */
@Repository
public class SensorRepository {
    static final String REPOSITORY_NAME = "sensors";
    static ApplicationContext ctx =
            new AnnotationConfigApplicationContext(SpringMongoConfig.class);
    static MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");


    public List<Sensor> findByOwner(String owner)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where("owner").is(owner));
        List<Sensor> sensorsList = mongoOperation.find(query,Sensor.class,REPOSITORY_NAME);
        return  sensorsList;
    }

    public Sensor findBySensorId(String sensorId)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where("sensorId").is(sensorId));
        Sensor sensor = mongoOperation.findOne(query,Sensor.class,REPOSITORY_NAME);
        return  sensor;
    }

    public List<Sensor> findAll()
    {
        List<Sensor> sensorsList = mongoOperation.findAll(Sensor.class,REPOSITORY_NAME);
        return  sensorsList;
    }
    @Async
    public void save(Sensor sensor)
    {
        mongoOperation.insert(sensor,REPOSITORY_NAME);
    }
}
