package com.ifedoroff.demo.services;

import com.ifedoroff.demo.DataServer;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Rostik on 13.08.2017.
 */
public class DataServerManagerService {
    private static Timer timer = new Timer();
    public static void start()
    {
        System.out.println(new Date(System.currentTimeMillis()).toString() + " [DATA SERVER MANAGER] Start server re-starter..");
        timer.schedule (new DataServerTask(),0,1000*60*60);
    }

    static class DataServerTask extends TimerTask {
        public void run() {
            restart();
        }
        private static void restart()
        {
            System.out.println(new Date(System.currentTimeMillis()).toString() + " [DATA SERVER MANAGER] Restarting server..");
            try {
                DataServer.stop();
                Thread.sleep(1000);
                DataServer.run();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    }
}
