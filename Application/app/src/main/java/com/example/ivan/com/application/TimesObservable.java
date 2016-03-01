package com.example.ivan.com.application;

import android.util.Log;

import java.util.Observable;

public class TimesObservable extends Observable {

    private  int times ;

    public  int getTimes(){

        return this.times;
    }

    public void setTimes(int i){
        if (this.times!=i){

            this.times = i ;

            Log.d("LOG_TAG_TIMES",String.valueOf(i));

            setChanged();

            notifyObservers();
        }


    }
}
