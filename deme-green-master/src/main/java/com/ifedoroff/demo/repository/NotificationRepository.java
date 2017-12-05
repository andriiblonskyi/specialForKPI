package com.ifedoroff.demo.repository;

import com.ifedoroff.demo.model.notification.Notification;
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
 * Created by Rostik on 31.07.2017.
 */
@Repository
public class NotificationRepository {

    static final String REPOSITORY_NAME = "notifications";
    static ApplicationContext ctx =
            new AnnotationConfigApplicationContext(SpringMongoConfig.class);
    static MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

    public List<Notification> findByUser(String user)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where("user").is(user));
        List<Notification> notificationList = mongoOperation.find(query,Notification.class,REPOSITORY_NAME);
        return  notificationList;
    }

    @Async
    public void save(Notification notification)
    {
        mongoOperation.insert(notification,REPOSITORY_NAME);
    }

}
