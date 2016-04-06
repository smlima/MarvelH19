package com.slima.marvelh19.services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.slima.marvelh19.R;
import com.slima.marvelh19.app.MarvelCharactersApp;

import javax.inject.Inject;

/**
 * Download manager responsible for handling dispatching the
 * requests to doanload data to the download service
 * <p/>
 * Created by sergio.lima on 03/04/2016.
 */
public class DownloadManager {

    /**
     * log tag
     */
    static final String TAG = "DownloadManager";

    /**
     * Application context
     */
    private Context mContext;

    @Inject
    public DownloadManager(MarvelCharactersApp context) {
        mContext = context;
        Log.d(TAG, "Constructor");

    }

    /**
     * Initial download request
     * normally at start of the app
     * the download service will only dispatch this one if there's no
     * data in the DB
     */
    public void loadInit() {

        if (!DownloadService.isNetworkAvailable(mContext)) {
            Log.e(TAG, "loadInit: Does not have internet connection");
            Toast.makeText(mContext, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(mContext, DownloadService.class);
        Bundle extras = new Bundle();

        extras.putString(DownloadService.ACTION_EXTRAS, DownloadService.DownloadActions.LOAD_INIT.name());

        intent.putExtras(extras);
        mContext.startService(intent);

        Log.d(TAG, "loadInit() called with: " + "" + intent);

    }

    /**
     * Load more Characters from the Marvel Site
     */
    public void loadMore() {

        if (!DownloadService.isNetworkAvailable(mContext)) {
            Log.e(TAG, "loadMore: Does not have internet connection");
            Toast.makeText(mContext, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(mContext, DownloadService.class);
        Bundle extras = new Bundle();

        extras.putString(DownloadService.ACTION_EXTRAS, DownloadService.DownloadActions.LOAD_MORE.name());

        intent.putExtras(extras);
        mContext.startService(intent);

        Log.d(TAG, "loadMore() called with: " + "" + intent);

    }

    /**
     * Load character comics details
     *
     * @param comicId the comic ID
     */
    public void loadCharacterComics(Integer comicId) {

        if (!DownloadService.isNetworkAvailable(mContext)) {
            Log.e(TAG, "loadCharacterComics: Does not have internet connection");
            Toast.makeText(mContext, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(mContext, DownloadService.class);
        Bundle extras = new Bundle();

        extras.putString(DownloadService.ACTION_EXTRAS, DownloadService.DownloadActions.LOAD_CHARACTER.name());
        extras.putInt(DownloadService.ACTION_ID, comicId);

        intent.putExtras(extras);
        mContext.startService(intent);

        Log.d(TAG, "loadCharacterComics() called with: " + "" + intent);

    }

    /**
     * Load character search request
     *
     * @param byName search characters by name
     */
    public void loadSearchCharacter(String byName) {

        if (!DownloadService.isNetworkAvailable(mContext)) {
            Log.e(TAG, "loadSearchCharacter: Does not have internet connection");
            Toast.makeText(mContext, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(mContext, DownloadService.class);
        Bundle extras = new Bundle();

        extras.putString(DownloadService.ACTION_EXTRAS, DownloadService.DownloadActions.LOAD_SEARCH.name());
        extras.putString(DownloadService.ACTION_SEARCH_NAME, byName);

        intent.putExtras(extras);
        mContext.startService(intent);

        Log.d(TAG, "loadSearchCharacter() called with: " + "" + intent);

    }

}
