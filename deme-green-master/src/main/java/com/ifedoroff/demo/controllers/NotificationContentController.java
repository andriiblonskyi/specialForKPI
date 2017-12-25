package com.ifedoroff.demo.controllers;

import com.ifedoroff.demo.Constants;
import com.ifedoroff.demo.model.notification.Notification;
import com.ifedoroff.demo.repository.NotificationRepository;
import com.ifedoroff.demo.tools.UnreadNotificationCache;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/content")
public class NotificationContentController {


    static NotificationRepository notificationRepository = new NotificationRepository();

    @RequestMapping(method = RequestMethod.GET, value = "/notificationsCount")
    String getNotificationsCount() {
        JSONObject jsonObject = new JSONObject();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        int count = UnreadNotificationCache.getNotificationCount(Constants.USER_NAME);
        jsonObject.put("count",count);
        String jsonStr = jsonObject.toString();
        return jsonStr;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/notifications")
    List<Notification> getNotifications() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Notification> notifications = UnreadNotificationCache.getNotifications(auth.getName());
        UnreadNotificationCache.clear(auth.getName());
        return notifications;
    }

}
