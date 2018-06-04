package com.figengungor.thedictionary.ui.history;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;

import com.figengungor.thedictionary.data.local.AppDatabase;

/**
 * Created by figengungor on 5/29/2018.
 */

public class HistoryViewModelFactory implements ViewModelProvider.Factory  {

    AppDatabase appDatabase;
    Application application;

    public HistoryViewModelFactory(Application application, AppDatabase appDatabase) {
        this.application = application;
        this.appDatabase = appDatabase;
    }

    @Override
    public HistoryViewModel create(Class modelClass) {
        return new HistoryViewModel(application, appDatabase);
    }

}
