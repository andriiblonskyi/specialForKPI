package com.ifedoroff.demo;

import com.ifedoroff.demo.services.DataHandlerService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import static java.lang.System.out;

public class DataServer {

    static ServerSocket listener;

    public static void run() throws Exception {
        System.out.println(new Date(System.currentTimeMillis()).toString() +" [DATA SERVER] The capitalization server is running..." );
        java.net.InetAddress addr = java.net.InetAddress.getLocalHost();
        listener = new ServerSocket(9090, 0, addr);
        System.out.println(new Date(System.currentTimeMillis()).toString() +" [DATA SERVER] " + "IP : [ " + listener.getInetAddress() + " ];\t" + "PORT : [ " + listener.getLocalPort() + " ];" );

        try {
            while (true) {
                new Capitalizer(listener.accept()).start();
            }
        } finally {
            stop();
        }
    }

    public static void stop() throws IOException {
        listener.close();
        listener = null;
        System.out.println(new Date(System.currentTimeMillis()).toString() +" [DATA SERVER] Stop server.." );

    }

    private static void log(String message) {
        out.println(message);
    }

    private static class Capitalizer extends Thread {


        private Socket socket;
        public Capitalizer(Socket socket) {
            this.socket = socket;
        }


        public void handle(String input) throws Exception
        {
            DataHandlerService dataHandlerService = new DataHandlerService();
            dataHandlerService.handleValue(input);
        }
        public void run() {
            try {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                String input;
                while (true) {
                    input = in.readLine();
                    if (input == null || input.equals(".")) {
                        break;
                    }
                    handle(input);
                }
            } catch (IOException e) {
                System.out.println(new Date(System.currentTimeMillis()).toString() +" [DATA SERVER] Error handle :" + e.getMessage() );

            } catch (Exception e) {
                System.out.println(new Date(System.currentTimeMillis()).toString() +" [DATA SERVER] Error handle :" + e.getMessage() );

            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println(new Date(System.currentTimeMillis()).toString() +" [DATA SERVER] Error closing :" + e.getMessage() );
                }
            }
        }


    }
}
