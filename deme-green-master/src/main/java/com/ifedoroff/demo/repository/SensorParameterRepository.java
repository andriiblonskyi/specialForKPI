package com.ifedoroff.demo.repository;

import com.ifedoroff.demo.model.sensor.SensorParameter;
import com.ifedoroff.demo.model.sensor.SensorResponse;
import com.ifedoroff.demo.security.SpringMongoConfig;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Rostik on 02.08.2017.
 */
@Repository
public class SensorParameterRepository {

    static ApplicationContext ctx =
            new AnnotationConfigApplicationContext(SpringMongoConfig.class);
    static MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

    @Async
    public void save(SensorParameter sensorParameter, String collection)
    {
        try {

            mongoOperation.insert(sensorParameter,collection);
        }
        catch (Exception ex)
        {
            System.out.println(new Date().toString() + " " + ex.getMessage());
        }

    }
    static SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    static SimpleDateFormat yearFormat = new SimpleDateFormat("dd-MM-yyyy");
    static SimpleDateFormat hourFormat = new SimpleDateFormat("HH");

    public Date getDate(String repoName)
    {
        Date date = new Date(System.currentTimeMillis());
        DBCursor cursor = mongoOperation.getCollection(repoName).find().sort(new BasicDBObject("_id", -1)).limit(1);
        while (cursor.hasNext())
        {
            DBObject obj = cursor.next();
            SensorParameter par = mongoOperation.getConverter().read(SensorParameter.class,obj);
            ObjectId id = new ObjectId(par.getId());
            date = id.getDate();
            break;
        }
        return date;
    }
    public List<SensorResponse> findDay(String repoName)
    {
        //approximate +
        Date today = new Date(System.currentTimeMillis());
        String pattern = yearFormat.format(today);
        Map<String,List<Integer>> values = new HashMap<>();
        System.out.println(new Date(System.currentTimeMillis()).toString() + " [SENSOR PARAM REPOSITORY] Searching for pattern : " + pattern);
        Query query = new Query();
         query.addCriteria(Criteria.where("additionsDate").is(pattern));
        List<SensorResponse> sensorResponseList = new ArrayList<>();
        DBCursor cursor = mongoOperation.getCollection(repoName).find(query.getQueryObject()).sort(new BasicDBObject("_id", -1));
        while (cursor.hasNext())
        {
            DBObject obj = cursor.next();
            SensorParameter par = mongoOperation.getConverter().read(SensorParameter.class,obj);
            if (values.containsKey(par.getBelongingHour()))
            {
                values.get(par.getBelongingHour()).add(par.getValue());
            }
            else
            {
                List<Integer> tmp = new ArrayList<>();
                tmp.add(par.getValue());
                values.put(par.getBelongingHour(), tmp);
            }
        }
        for (Map.Entry<String,List<Integer>> item : values.entrySet())
        {
            int sum = 0;
            for (int val : item.getValue())
                sum+=val;
            sum /= item.getValue().size();
            String date  = pattern + " " + item.getKey() +":00:00";
            SensorResponse response = new SensorResponse(sum,date);
            sensorResponseList.add(response);
        }
        System.out.println(new Date(System.currentTimeMillis()).toString() + " [SENSOR PARAM REPOSITORY] Selected " + sensorResponseList.size() + " elements (day) from " + repoName);
        return  sensorResponseList;
    }

    public List<SensorResponse> findDay(String repoName,String pattern)
    {
        Map<String,List<Integer>> values = new HashMap<>();
        System.out.println(new Date(System.currentTimeMillis()).toString() + " [SENSOR PARAM REPOSITORY] Searching for pattern : " + pattern);
        Query query = new Query();
        query.addCriteria(Criteria.where("additionsDate").is(pattern));
        List<SensorResponse> sensorResponseList = new ArrayList<>();
        DBCursor cursor = mongoOperation.getCollection(repoName).find(query.getQueryObject()).sort(new BasicDBObject("_id", -1));
        while (cursor.hasNext())
        {
            DBObject obj = cursor.next();
            SensorParameter par = mongoOperation.getConverter().read(SensorParameter.class,obj);
            if (values.containsKey(par.getBelongingHour()))
            {
                values.get(par.getBelongingHour()).add(par.getValue());
            }
            else
            {
                List<Integer> tmp = new ArrayList<>();
                tmp.add(par.getValue());
                values.put(par.getBelongingHour(), tmp);
            }
        }
        for (Map.Entry<String,List<Integer>> item : values.entrySet())
        {
            int sum = 0;
            for (int val : item.getValue())
                sum+=val;
            sum /= item.getValue().size();
            String date  = pattern + " " + item.getKey() +":00:00";
            SensorResponse response = new SensorResponse(sum,date);
            sensorResponseList.add(response);
        }
        System.out.println(new Date(System.currentTimeMillis()).toString() + " [SENSOR PARAM REPOSITORY] Selected " + sensorResponseList.size() + " elements (of day) from " + repoName);
        return  sensorResponseList;
    }


    public List<SensorResponse> findWeek(String repoName)
    {
        List<SensorResponse> sensorResponseList = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -14);
        List<String> dates = new ArrayList<>();
        for(int i = 0; i< 14; i++){
            cal.add(Calendar.DAY_OF_YEAR, 1);
            dates.add(yearFormat.format(cal.getTime()));
        }
        for (String date : dates)
        {
            System.out.println(new Date(System.currentTimeMillis()).toString() + "  [SENSOR PARAM REPOSITORY] Searching for " + date);
            List<Integer> dayValues = new ArrayList<>();
            Query query = new Query();
            query.addCriteria(Criteria.where("additionsDate").is(date));
            DBCursor cursor = mongoOperation.getCollection(repoName).find(query.getQueryObject());
            while (cursor.hasNext())
            {
                DBObject obj = cursor.next();
                SensorParameter par = mongoOperation.getConverter().read(SensorParameter.class,obj);
                dayValues.add(par.getValue());
            }
            if (dayValues.size() > 0)
            {
                int sum = 0;
                for (int value : dayValues)
                    sum+=value;
                sum /=dayValues.size();
                SensorResponse response = new SensorResponse(sum, date + " 12:00:00");
                sensorResponseList.add(response);
            }
        }
        System.out.println(new Date(System.currentTimeMillis()).toString() + " [SENSOR PARAM REPOSITORY] Selected " + sensorResponseList.size() + " elements (week) from " + repoName);
        return  sensorResponseList;

    }
    /*
    List<SensorResponse> sensorResponseList = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -7);
        List<String> date   s = new ArrayList<>();
        for(int i = 0; i< 7; i++){
            cal.add(Calendar.DAY_OF_YEAR, 1);
            dates.add(yearFormat.format(cal.getTime()));
        }
        for (String date : dates)
        {
            System.out.println("searching for " + date);
            List<Integer> dayValues = new ArrayList<>();
            Query query = new Query();
            query.addCriteria(Criteria.where("additionsDate").is(date));
            DBCursor cursor = mongoOperation.getCollection(repoName).find(query.getQueryObject());
            while (cursor.hasNext())
            {
                DBObject obj = cursor.next();
                SensorParameter par = mongoOperation.getConverter().read(SensorParameter.class,obj);
                dayValues.add(par.getValue());
            }

            if (dayValues.size() > 2)
            {
                int sum =0;
                int i;
                for (i = 0; i < dayValues.size()/2; i++)
                    sum += dayValues.get(i);
                //here
                sum /= i;

                SensorResponse response = new SensorResponse(sum, date + " 00:00:00");
                sensorResponseList.add(response);
                sum = 0;
                for (i = dayValues.size()/2; i < dayValues.size(); i++)
                    sum += dayValues.get(i);
                sum /= i;

                response = new SensorResponse(sum, date + " 12:00:00");
                sensorResponseList.add(response);
            }

        }


        System.out.println("Selected " + sensorResponseList.size() + " elements (week) from " + repoName);
        return  sensorResponseList;*/
}
