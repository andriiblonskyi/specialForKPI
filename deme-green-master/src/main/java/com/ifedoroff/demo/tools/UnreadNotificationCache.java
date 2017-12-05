package com.ifedoroff.demo.tools;

import com.ifedoroff.demo.model.notification.Notification;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rostik on 31.07.2017.
 */
public class            UnreadNotificationCache {

        static List<Notification> unreadedNotificationsCache = new ArrayList<>();

        public static int getNotificationCount(String userName)
        {
            int i = 0;
            for (Notification item : unreadedNotificationsCache)
                if (item.getUser().equals(userName))
                        i+=1;
            return i;
        }

        public static List<Notification> getNotifications(String userName)
        {
                List<Notification> notifications = new ArrayList<>();
                for (Notification item : unreadedNotificationsCache)
                        if (item.getUser().equals(userName))
                                notifications.add(item);
                return notifications;
        }

        public static boolean put(Notification notification)
        {
                return unreadedNotificationsCache.add(notification);
        }


        public static boolean clear(String userName)
        {
                List<Notification> forDeleting = new ArrayList<>();
                for (Notification item : unreadedNotificationsCache)
                        if (item.getUser().equals(userName))
                                forDeleting.add(item);
                for (Notification item : forDeleting)
                        unreadedNotificationsCache.remove(item);
                return true;
        }
}
