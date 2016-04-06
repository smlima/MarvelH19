package com.slima.marvelh19.app;

import android.app.Application;
import android.content.Context;

import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.slima.marvelh19.ioc.DaggerIOC;
import com.slima.marvelh19.ioc.IOC;
import com.slima.marvelh19.ioc.IOCModules;

/**
 * The application class.
 * <p/>
 * Contains everything needed like app reference, and start of DbFlow
 * and Dagger. Because its the entry point of the app.
 * <p/>
 * Created by sergio.lima on 03/04/2016.
 */
public class MarvelCharactersApp extends Application {

    /**
     * IOC handler
     */
    public IOC mIOC;

    /**
     * app instance
     */
    private static MarvelCharactersApp sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        init();
    }

    /**
     * loadInit stuff required for DBFLow, IOC
     */
    private void init() {

        FlowManager.init(this);
        FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);

        mIOC = DaggerIOC.builder()
                .iOCModules(new IOCModules(this))
                .build();

    }

    /**
     * Get the IOC component that gives access to the Dagger2 injection mechanism
     *
     * @param context the app context
     * @return the ioc component
     */
    public static IOC getIoC(Context context) {
        return ((MarvelCharactersApp) context.getApplicationContext()).mIOC;
    }

    /**
     * get the Application context
     *
     * @return this app reference
     */
    public static MarvelCharactersApp getAppContext() {
        return sInstance;
    }
}
