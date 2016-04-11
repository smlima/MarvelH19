package com.slima.marvelh19.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.MarvelGeneratedDatabaseHolder;
import com.slima.marvelh19.R;

/**
 * Just a nice clean splash screen similar to the mockups
 * will trigger a download of few characters just to have data
 * when reaching the list characters screen
 *
 * Created by sergio.lima on 03/04/2016.
 */
public class SplashActivity extends BaseActivity{

    /** time to wati before triggering the activity transition */
    public static final int DELAY_MILLIS = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        // configure the progress bar... because there's no wireframes to this... will keep it as it is
        ProgressBar viewById = (ProgressBar) findViewById(R.id.progressBar2);
        viewById.incrementProgressBy(10);
        viewById.setMax(100);
        viewById.setProgress(30);
        viewById.setIndeterminate(true);

        // do loadInit stuff
        initLoading();

        // mode on to next activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // Just to simulate a loading time
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);

                // default , no animation
                startActivity(intent);

                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                finish();
            }
        }, DELAY_MILLIS);


    }

    /**
     * Initial loading on the splash screeen
     */
    private void initLoading() {

        FlowManager.initModule(MarvelGeneratedDatabaseHolder.class);

        // prefetch data
        this.downloadManager.loadInit();

    }
}
