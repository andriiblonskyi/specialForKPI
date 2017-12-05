package com.ifedoroff.demo;

import org.apache.log4j.Logger;


public class LoggFactory {

    public  static  void debug(String msg)
    {
        Logger.getLogger(LoggFactory.class).debug(msg);
    }

    public  static  void debug(String msg, Class<?> type)
    {
        Logger.getLogger(type).debug(msg);
    }

    public  static  void error(String msg)
    {
        Logger.getLogger(LoggFactory.class).error(msg);
    }

    public  static  void error(String msg, Class<?> type)
    {
        Logger.getLogger(type).error(msg);
    }

}
