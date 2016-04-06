package com.slima.marvelh19.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.slima.marvelh19.app.MarvelCharactersApp;
import com.slima.marvelh19.model.characters.CharacterResult;
import com.slima.marvelh19.model.characters.ComicsResults;
import com.slima.marvelh19.model.characters.EventsResult;
import com.slima.marvelh19.model.characters.SeriesResult;
import com.slima.marvelh19.model.characters.StoriesResult;
import com.slima.marvelh19.model.characters.Url;
import com.slima.marvelh19.network.MarvelNetworkServices;

import java.io.IOException;
import java.util.List;

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

        MarvelCharactersApp.getIoC(MarvelCharactersApp.getAppContext()).inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle extras = intent.getExtras();

        String action = extras.getString(ACTION_EXTRAS);

        switch (DownloadActions.fromString(action)) {
            case LOAD_CHARACTER:
                // load character
                loadCharacter(extras);
                break;
            case LOAD_INIT:
                // load loadInit stuff
                loadInit(extras);
                break;
            case LOAD_MORE:
                // load more
                loadMore(extras);
                break;
            case LOAD_SEARCH:
                // load more
                loadSearch(extras);
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

        //FIXME: replace this for a Future task or similar
        new AsyncTask<Void, Integer, List<CharacterResult>>() {
            @Override
            protected List<CharacterResult> doInBackground(Void... params) {
                List<CharacterResult> characters = null;
                try {
                    // load more characters
                    characters = new MarvelNetworkServices().getCharacters(LOAD_MORE_TOTAL_REQUEST, (int) count);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return characters;
            }

            @Override
            protected void onPostExecute(List<CharacterResult> characterResults) {

               persistCharactersList(characterResults);

            }
        }.execute();

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

            //FIXME: replace this for a Future task or similar
            new AsyncTask<Void, Integer, List<CharacterResult>>() {
                @Override
                protected List<CharacterResult> doInBackground(Void... params) {
                    List<CharacterResult> characters = null;
                    try {
                        // load more characters
                        characters = new MarvelNetworkServices().getCharactersByName(characterSearchName);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return characters;
                }

                @Override
                protected void onPostExecute(List<CharacterResult> characterResults) {

                    persistCharactersList(characterResults);

                }
            }.execute();
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

        new AsyncTask<Void, Integer, List<CharacterResult>>() {
            @Override
            protected List<CharacterResult> doInBackground(Void... params) {
                List<CharacterResult> characters = null;
                try {
                    characters = new MarvelNetworkServices().getCharacters(LOAD_MORE_TOTAL_REQUEST, 0);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return characters;
            }

            @Override
            protected void onPostExecute(List<CharacterResult> characterResults) {

                persistCharactersList(characterResults);

            }
        }.execute();

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


        new AsyncTask<Void, Integer, List<ComicsResults>>() {
            @Override
            protected List<ComicsResults> doInBackground(Void... params) {
                List<ComicsResults> characters = null;
                try {
                    characters = new MarvelNetworkServices().getComicsForCharacterId(characterId);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return characters;
            }

            @Override
            protected void onPostExecute(List<ComicsResults> comicsResultses) {

                persistComicsList(comicsResultses, characterId);

            }
        }.execute();


        new AsyncTask<Void, Integer, List<SeriesResult>>() {
            @Override
            protected List<SeriesResult> doInBackground(Void... params) {
                List<SeriesResult> series = null;
                try {
                    series = new MarvelNetworkServices().getSeriesForCharacterId(characterId);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return series;
            }

            @Override
            protected void onPostExecute(List<SeriesResult> characterResults) {

               persistSeriesList(characterResults, characterId);
            }
        }.execute();

        new AsyncTask<Void, Integer, List<StoriesResult>>() {
            @Override
            protected List<StoriesResult> doInBackground(Void... params) {
                List<StoriesResult> series = null;
                try {
                    series = new MarvelNetworkServices().getStoriesForCharacterId(characterId);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return series;
            }

            @Override
            protected void onPostExecute(List<StoriesResult> characterResults) {

               persistStoriesList(characterResults, characterId);
            }
        }.execute();

        new AsyncTask<Void, Integer, List<EventsResult>>() {
            @Override
            protected List<EventsResult> doInBackground(Void... params) {
                List<EventsResult> series = null;
                try {
                    series = new MarvelNetworkServices().getEventsForCharacterId(characterId);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return series;
            }

            @Override
            protected void onPostExecute(List<EventsResult> characterResults) {

               persistEventsList(characterResults, characterId);
            }
        }.execute();


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
