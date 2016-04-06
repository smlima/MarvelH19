package com.slima.marvelh19.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Database object.
 * Contains the name and version. used throughout the db models
 * make sure to update the version number when major changes occurs on DB
 * <p/>
 * <p/>
 * Created by sergio.lima on 03/04/2016.
 */
@Database(name = MarvelDatabase.NAME, version = MarvelDatabase.VERSION)
public class MarvelDatabase {
    public static final String NAME = "MarvelDatabase"; // carefull  with .(dots) on name
    public static final int VERSION = 8;
}
