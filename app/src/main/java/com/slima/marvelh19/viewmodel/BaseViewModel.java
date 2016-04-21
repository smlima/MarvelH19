package com.slima.marvelh19.viewmodel;

import android.databinding.BaseObservable;

/**
 * Base View model
 * Could be useful for abstracting some stuff
 *
 * Created by sergio.lima on 31/03/2016.
 */
public abstract class BaseViewModel extends BaseObservable {


    public abstract void preload();

    public abstract void reload();

    public void start(){
        // override if requirred
    }

    public void stop() {
        // override if required
    }

}
