package com.slima.marvelh19.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.slima.marvelh19.model.characters.CharacterResult;
import com.slima.marvelh19.model.characters.ComicsResults;
import com.slima.marvelh19.model.characters.EventsResult;
import com.slima.marvelh19.model.characters.SeriesResult;
import com.slima.marvelh19.model.characters.StoriesResult;
import com.slima.marvelh19.model.characters.Url;
import com.slima.marvelh19.network.MarvelNetworkServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * Download service that will fetch the requested data from the marvel site
 * and insert it into the DB
 * <p/>
 * <p/>
 * Created by sergio.lima on 03/04/2016.
 */
public class DownloadService extends IntentService {

    /**
     * LOG tag
     */
    static final String TAG = "DownloadService";
    /**
     * KEY: for the action
     */
    static final String ACTION_EXTRAS = "ACTION";
    /**
     * KEY for the Character ID to fetch the details
     */
    static final String ACTION_ID = "ID";
    /**
     * KEY for the search name action
     */
    static final String ACTION_SEARCH_NAME = "SEARCH_NAME";
    /**
     * Number of characters to request at once
     */
    public static final int LOAD_MORE_TOTAL_REQUEST = 20;

    /**
     * ACtions available
     */
    enum DownloadActions {
        LOAD_INIT, LOAD_MORE, LOAD_CHARACTER, LOAD_SEARCH, DO_NOTHING;

        static public DownloadActions fromString(String action) {
            switch (action.toUpperCase()) {
                case "LOAD_CHARACTER":
                    return LOAD_CHARACTER;
                case "LOAD_MORE":
                    return LOAD_MORE;
                case "LOAD_INIT":
                    return LOAD_INIT;
                case "LOAD_SEARCH":
                    return LOAD_SEARCH;
                default:
                    return DO_NOTHING;
            }
        }
    }

    @Inject
    DownloadManager downloadManager;

    /**
     * Constructor of service
     */
    public DownloadService() {
        super(TAG);

        //Log.d(TAG, "DownloadService() called with: " + "");

        //MarvelCharactersApp.getIoC(MarvelCharactersApp.getAppContext()).inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // reuse the service
        setIntentRedelivery(true);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle extras = intent.getExtras();

        String action = extras.getString(ACTION_EXTRAS);

        switch (DownloadActions.fromString(action)) {
            case LOAD_CHARACTER:
                // load character
                loadCharacterAsync(extras);
                break;
            case LOAD_INIT:
                // load loadInit stuff
                loadInit(extras);
                break;
            case LOAD_MORE:
                // load more
                loadMoreAsync(extras);
                break;
            case LOAD_SEARCH:
                // load more
                loadSearchAsync(extras);
                break;
            case DO_NOTHING:
            default:
                // do nothing
        }

    }

    /**
     * persists the character into the DB
     *
     * @param characterResult the character
     */
    private void persisteCharacter(CharacterResult characterResult) {
        // insert db
        if (!characterResult.exists()) {
            characterResult.save();

            for (Url url : characterResult.getUrls()) {
                Log.d(TAG, "Saving URL: " + url);
                url.associateCharacter(characterResult);
                url.save();
            }
        } else {
            // verify if an update is required
        }
    }

    /**
     * Persist a list of characters
     *
     * @param characterResultList
     */
    private void persistCharactersList(List<CharacterResult> characterResultList) {
        if (characterResultList != null && !characterResultList.isEmpty()) {
            for (CharacterResult characterResult : characterResultList) {
                persisteCharacter(characterResult);
            }
        }
    }

    /**
     * persist the comics
     * @param comicsResultList
     * @param characterId
     */
    private void persistComicsList(List<ComicsResults> comicsResultList, Integer characterId) {
        if (comicsResultList != null) {
            for (ComicsResults comicsResults : comicsResultList) {
                comicsResults.setCharacterId(characterId);

                // insert db
                if (!comicsResults.exists()) {
                    comicsResults.save();
                } else {
                    // verify if an update is required
                }
            }
        }
    }

    /**
     *
     * @param eventsResults
     * @param characterId
     */
    private void persistEventsList(List<EventsResult> eventsResults, Integer characterId) {
        if (eventsResults != null) {
            for (EventsResult comicsResults : eventsResults) {
                comicsResults.setCharacterId(characterId);

                // insert db
                if (!comicsResults.exists()) {
                    comicsResults.save();
                } else {
                    // verify if an update is required
                }
            }
        }
    }

    /**
     *
     * @param eventsResults
     * @param characterId
     */
    private void persistStoriesList(List<StoriesResult> eventsResults, Integer characterId) {
        if (eventsResults != null) {
            for (StoriesResult comicsResults : eventsResults) {
                comicsResults.setCharacterId(characterId);

                // insert db
                if (!comicsResults.exists()) {
                    comicsResults.save();
                } else {
                    // verify if an update is required
                }
            }
        }
    }

    /**
     *
     * @param eventsResults
     * @param characterId
     */
    private void persistSeriesList(List<SeriesResult> eventsResults, Integer characterId) {
        if (eventsResults != null) {
            for (SeriesResult comicsResults : eventsResults) {
                comicsResults.setCharacterId(characterId);

                // insert db
                if (!comicsResults.exists()) {
                    comicsResults.save();
                } else {
                    // verify if an update is required
                }
            }
        }
    }


    /**
     * load more characters from marvel
     *
     * @param extras
     */
    private void loadMore(Bundle extras) {

        Log.d(TAG, "loadInit() called with: " + "extras = [" + extras + "]");

        final long count = SQLite.select().from(CharacterResult.class).query().getCount();


        try {
            persistCharactersList(new MarvelNetworkServices().getCharacters(LOAD_MORE_TOTAL_REQUEST, (int) count));
        } catch (IOException e) {
            Log.e(TAG, "loadMore: ", e);
            e.printStackTrace();
        }

    }

    /**
     * load more characters from marvel
     *
     * @param extras
     */
    private void loadSearch(Bundle extras) {

        Log.d(TAG, "loadSearch() called with: " + "extras = [" + extras + "]");

        final String characterSearchName = extras.getString(ACTION_SEARCH_NAME);

        if (characterSearchName != null && characterSearchName.length() > 1) {

            try {
                // load more characters
                persistCharactersList( new MarvelNetworkServices().getCharactersByName(characterSearchName));

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    MyExecutor myExecutoer = new MyExecutor();

    List<FutureTask> taskslist = new ArrayList<>(5);

    static int count =0;
    /**
     * load more characters from marvel
     *
     * @param extras
     */
    public void loadSearchAsync(Bundle extras) {

        Log.d(TAG, "loadSearchAsync() called with: " + "extras = [" + extras + "]");

        final String characterSearchName = extras.getString(ACTION_SEARCH_NAME);

        if (characterSearchName != null && characterSearchName.length() > 1) {

            FutureTask task = new FutureTask(new Callable() {
                @Override
                public Object call() throws Exception {

                    persistCharactersList( new MarvelNetworkServices().getCharactersByName(characterSearchName));

                    return null;
                }
            });

            addTaskToQueue(task);

        }

    }

    private void loadMoreAsync(Bundle extras) {

        Log.d(TAG, "loadMoreAsync() called with: " + "extras = [" + extras + "]");


        FutureTask task = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {

                final long count = SQLite.select().from(CharacterResult.class).query().getCount();

                persistCharactersList(new MarvelNetworkServices().getCharacters(LOAD_MORE_TOTAL_REQUEST, (int) count));

                return null;
            }
        });

        addTaskToQueue(task);

    }

    /**
     * Load character details
     *
     * @param extras intent bundle of extras
     */
    private void loadCharacterAsync(Bundle extras) {

        Log.d(TAG, "loadCharacter() called with: " + "extras = [" + extras + "]");

        if (!isNetworkAvailable(this)) {
            Log.e(TAG, " Does not have internet connection... aborting...");
            return;
        }

        final Integer characterId = extras.getInt(ACTION_ID);

        FutureTask task = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                persistComicsList(new MarvelNetworkServices().getComicsForCharacterId(characterId), characterId);
                return null;
            }
        });
        FutureTask task2 = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                persistSeriesList(new MarvelNetworkServices().getSeriesForCharacterId(characterId), characterId);
                return null;
            }
        });
        FutureTask task3 = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                persistStoriesList(new MarvelNetworkServices().getStoriesForCharacterId(characterId), characterId);
                return null;
            }
        });
        FutureTask task4 = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                persistEventsList(new MarvelNetworkServices().getEventsForCharacterId(characterId), characterId);
                return null;
            }
        });

        // clear and stopp all other tasks. this one has higher priority
        for (FutureTask futureTask : taskslist) {
            futureTask.cancel(true);
        }
        taskslist.clear();

        addTaskToQueue(task);
        addTaskToQueue(task2);
        addTaskToQueue(task3);
        addTaskToQueue(task4);

    }

    /**
     *
     * @param task
     */
    private void addTaskToQueue(FutureTask task) {
        synchronized (taskslist) {
            if (myExecutoer.getQueue().remainingCapacity() > 0) {
                // reference for the future
                taskslist.add(task);
                // execute it
                myExecutoer.execute(task);
            } else {

                try {
                    myExecutoer.awaitTermination(2000, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (myExecutoer.getQueue().remainingCapacity() > 0) {
                    // reference for the future
                    taskslist.add(task);
                    // execute it
                    myExecutoer.execute(task);
                }
            }
        }
    }

    /**
     * loadInit load of few marvel characters
     *
     * @param extras
     */
    private void loadInit(Bundle extras) {

        Log.d(TAG, "loadInit() called with: " + "extras = [" + extras + "]");

        final long count = SQLite.select().from(CharacterResult.class).query().getCount();

        // only load loadInit if dont have any (less than 5) character on DB
        if (count > 5L) {
            Log.d(TAG, "loadInit() returned: " + count);

            return;
        }

        Log.d(TAG, "loadInit() returned: " + count + " , less than 5 start Downalods ");

        if (!isNetworkAvailable(this)) {
            Log.e(TAG, " Does not have internet connection... aborting...");
            return;
        }

        try {
            persistCharactersList( new MarvelNetworkServices().getCharacters(LOAD_MORE_TOTAL_REQUEST, 0));

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Load character details
     *
     * @param extras intent bundle of extras
     */
    private void loadCharacter(Bundle extras) {

        Log.d(TAG, "loadCharacter() called with: " + "extras = [" + extras + "]");

        if (!isNetworkAvailable(this)) {
            Log.e(TAG, " Does not have internet connection... aborting...");
            return;
        }

        final Integer characterId = extras.getInt(ACTION_ID);

        try {
            persistComicsList(new MarvelNetworkServices().getComicsForCharacterId(characterId), characterId);
            persistSeriesList(new MarvelNetworkServices().getSeriesForCharacterId(characterId), characterId);
            persistStoriesList(new MarvelNetworkServices().getStoriesForCharacterId(characterId), characterId);
            persistEventsList(new MarvelNetworkServices().getEventsForCharacterId(characterId), characterId);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Check if network is available
     *
     * @param context the context
     * @return true we've got internet ;), false otherwise ;(
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
