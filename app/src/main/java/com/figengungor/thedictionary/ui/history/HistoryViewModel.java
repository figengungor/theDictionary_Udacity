package com.figengungor.thedictionary.ui.history;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.ComponentName;
import android.os.AsyncTask;

import com.figengungor.thedictionary.R;
import com.figengungor.thedictionary.ui.widget.SearchHistoryWidgetProvider;
import com.figengungor.thedictionary.data.local.AppDatabase;
import com.figengungor.thedictionary.data.local.SearchHistoryEntry;

import java.util.List;

/**
 * Created by figengungor on 5/29/2018.
 */

public class HistoryViewModel extends AndroidViewModel {

    LiveData<List<SearchHistoryEntry>> historyList;
    AppDatabase appDatabase;

    public HistoryViewModel(Application application, AppDatabase appDatabase) {
        super(application);
        this.appDatabase = appDatabase;
        historyList = appDatabase.searchHistoryDao().loadAllSearchHistoryEntries();
    }

    public LiveData<List<SearchHistoryEntry>> getHistoryList() {
        return historyList;
    }


    public void deleteAllHistory(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.searchHistoryDao().deleteAll();
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(HistoryViewModel.this.getApplication());
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(HistoryViewModel.this.getApplication(), SearchHistoryWidgetProvider.class));
                //Trigger data update to handle the ListView widgets and force a data refresh
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.historyRv);
                return null;
            }

            @Override
            protected void onPostExecute(Void voids) {
            }
        }.execute();

    }

    public void deleteSearchHistoryEntry(final SearchHistoryEntry item) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.searchHistoryDao().deleteSearchHistoryEntry(item);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(HistoryViewModel.this.getApplication());
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(HistoryViewModel.this.getApplication(), SearchHistoryWidgetProvider.class));
                //Trigger data update to handle the ListView widgets and force a data refresh
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.historyRv);
                return null;
            }

            @Override
            protected void onPostExecute(Void voids) {
            }
        }.execute();
    }
}
