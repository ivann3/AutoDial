package com.example.ivan.com.application;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by ivan on 15/6/18.
 */
public class TimesObserver implements Observer {

    private static TaskAsync taskAsync;

    public void setTaskAsync(TaskAsync task){

        this.taskAsync = task;
    }

    @Override
    public void update(Observable observable, Object data) {

        this.taskAsync.execute();
    }
}