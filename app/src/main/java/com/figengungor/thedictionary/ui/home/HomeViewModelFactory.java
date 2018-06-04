package com.figengungor.thedictionary.ui.home;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;

import com.figengungor.thedictionary.data.DataManager;

/**
 * Created by figengungor on 5/21/2018.
 */

public class HomeViewModelFactory implements ViewModelProvider.Factory {

    DataManager dataManager;

    Application application;

    public HomeViewModelFactory(Application application, DataManager dataManager) {
        this.application = application;
        this.dataManager = dataManager;
    }

    @Override
    public HomeViewModel create(Class modelClass) {
        return new HomeViewModel(application, dataManager);
    }
}
