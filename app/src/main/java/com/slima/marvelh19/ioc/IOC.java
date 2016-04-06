package com.slima.marvelh19.ioc;

import com.slima.marvelh19.activities.BaseActivity;
import com.slima.marvelh19.app.MarvelCharactersApp;
import com.slima.marvelh19.services.DownloadManager;
import com.slima.marvelh19.services.DownloadService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Component  for the Dagger2 injection mechaninsm
 * <p/>
 * Created by sergio.lima on 03/04/2016.
 */
@Singleton
@Component(modules = {IOCModules.class})
public interface IOC {

    // must explicitly inject(..) all the activities or classes that contain the '@inject stuff'
    void inject(BaseActivity activity);

    void inject(DownloadService serice);

    // expose the download manager and app context through IOC
    DownloadManager downloadManager();

    MarvelCharactersApp appContext();
}
