package com.figengungor.thedictionary.data;

import android.app.Application;
import android.os.AsyncTask;

import com.figengungor.thedictionary.BuildConfig;
import com.figengungor.thedictionary.data.local.AppDatabase;
import com.figengungor.thedictionary.data.local.SearchHistoryEntry;
import com.figengungor.thedictionary.data.model.SearchResponse;
import com.figengungor.thedictionary.data.remote.OxfordService;
import com.figengungor.thedictionary.data.remote.OxfordServiceFactory;

import java.util.Date;

import retrofit2.Callback;

/**
 * Created by figengungor on 5/20/2018.
 */

public class DataManager {

    private static DataManager instance;
    private OxfordService oxfordService;
    private AppDatabase appDatabase;

    private DataManager(Application application) {
        this.oxfordService = OxfordServiceFactory.createService();
        this.appDatabase = AppDatabase.getInstance(application);
    }

    public static DataManager getInstance(Application application) {
        if (instance == null) instance = new DataManager(application);
        return instance;
    }

    public void search(String word, Callback<SearchResponse> listener) {
        oxfordService.search("en", word, BuildConfig.OXFORD_APP_ID, BuildConfig.OXFORD_APP_KEY).enqueue(listener);
    }

    public void saveSearchHistoryEntry(final String entry){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                SearchHistoryEntry searchHistoryEntry = appDatabase.searchHistoryDao().loadSearchHistoryEntryByEntry(entry);
                if(searchHistoryEntry!=null){
                    appDatabase.searchHistoryDao().updateSearchHistoryEntry(new SearchHistoryEntry(entry, new Date()));
                } else {
                    appDatabase.searchHistoryDao().insertSearchHistoryEntry(new SearchHistoryEntry(entry, new Date()));
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void voids) {
            }
        }.execute();

    }

}
