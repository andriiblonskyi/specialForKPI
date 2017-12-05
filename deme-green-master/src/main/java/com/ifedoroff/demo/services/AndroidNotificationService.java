package com.ifedoroff.demo.services;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Created by Rostik on 28.07.2017.
 */
@Service
public class AndroidNotificationService {

    

    private static final String FIREBASE_SERVER_KEY = "AAAAX3F728s:APA91bE7AYQsSFWyLjLIM83Hm-FJS4xdOsFBly1pX1PYk2LIFsPjkCfIOIqVkluMP9HetckDy7xhNtaOJBad4fXXXdtm2SIGIHdwU30rSh4Sayka676Y1gc9AX_wZY6u_5TpID16l0sP";
    private static final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";
    private final String TOPIC = "Test_notification";

    @Async
    private CompletableFuture<String> sendToFirebase(HttpEntity<String> entity) {

        RestTemplate restTemplate = new RestTemplate();
        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + FIREBASE_SERVER_KEY));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
        restTemplate.setInterceptors(interceptors);

        String firebaseResponse = restTemplate.postForObject(FIREBASE_API_URL, entity, String.class);

        return CompletableFuture.completedFuture(firebaseResponse);
    }

    public ResponseEntity<String> send(String title, String message) throws JSONException {

        System.out.println(new Date(System.currentTimeMillis()).toString() +" [NOTIFICATION SERVICE] Sending [" + title + "] ["+message+"];");


        JSONObject body = new JSONObject();
        body.put("to", "/topics/" + TOPIC);
        body.put("priority", "high");

        JSONObject notification = new JSONObject();
        notification.put("title", title);
        notification.put("body", message);
        body.put("notification", notification);

        HttpEntity<String> request = new HttpEntity<>(body.toString());

        CompletableFuture<String> pushNotification = sendToFirebase(request);
        CompletableFuture.allOf(pushNotification).join();

        try {
            String firebaseResponse = pushNotification.get();
            return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
        } catch (InterruptedException ex) {
            System.out.println(new Date(System.currentTimeMillis()).toString() +" [NOTIFICATION SERVICE] Error sending \n" + ex.getMessage() + "\n" + ex.getLocalizedMessage());
        } catch (ExecutionException ex) {
            System.out.println(new Date(System.currentTimeMillis()).toString() +" [NOTIFICATION SERVICE] Error sending \n" + ex.getMessage() + "\n" + ex.getLocalizedMessage());
        }

        return new ResponseEntity<>("Push Notification ERROR!", HttpStatus.BAD_REQUEST);
    }
}
