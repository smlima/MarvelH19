package com.slima.marvelh19.ioc;

import com.slima.marvelh19.app.MarvelCharactersApp;
import com.slima.marvelh19.services.DownloadManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sergio.lima on 03/04/2016.
 */

@Module
public class IOCModules {

    MarvelCharactersApp app;

    public IOCModules(MarvelCharactersApp app) {
        this.app = app;
    }

    @Provides
    @Singleton
    DownloadManager provideDownloadManager() {
        return new DownloadManager(app);
    }

    @Provides
    MarvelCharactersApp provideAppContext(){
        return app;
    }
}
