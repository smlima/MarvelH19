package com.slima.marvelh19.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.slima.marvelh19.app.MarvelCharactersApp;
import com.slima.marvelh19.services.DownloadManager;

import javax.inject.Inject;

/**
 * Created by sergio.lima on 03/04/2016.
 */
public class BaseActivity extends AppCompatActivity {

    @Inject
    DownloadManager downloadManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MarvelCharactersApp.getIoC(this).inject(this);

    }

    public DownloadManager getDownloadManager() {
        return downloadManager;
    }
}
