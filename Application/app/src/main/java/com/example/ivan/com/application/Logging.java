package com.example.ivan.com.application;

import android.os.Environment;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * Created by ivan on 15/6/20.
 */
public class Logging {

    private  Logger gLogger;

    public void configLog(String name,String tag)
    {
         LogConfigurator logConfigurator = new LogConfigurator();

        logConfigurator.setFileName(Environment.getExternalStorageDirectory()
                + File.separator + "Log" + name + ".log");

        // Set the root log level
        logConfigurator.setRootLevel(Level.DEBUG);
        // Set log level of a specific logger
        logConfigurator.setLevel("org.apache", Level.ERROR);

        //默认输出log的格式
//        logConfigurator.setFilePattern("%d - [%p::%c::%C] - %m%n");

        //%d表示时间戳
        logConfigurator.setFilePattern("%d - %m%n");

        logConfigurator.configure();

        setgLogger(tag);

    }


    public void setgLogger(String tag){

        gLogger = Logger.getLogger(tag);
    }


    public  Logger getgLogger(){

        return gLogger;
    }

}
