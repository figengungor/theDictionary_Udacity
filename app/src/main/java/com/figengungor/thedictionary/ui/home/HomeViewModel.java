package com.figengungor.thedictionary.ui.home;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.ComponentName;

import com.figengungor.thedictionary.R;
import com.figengungor.thedictionary.ui.widget.SearchHistoryWidgetProvider;
import com.figengungor.thedictionary.data.DataManager;
import com.figengungor.thedictionary.data.model.LexicalEntry;
import com.figengungor.thedictionary.data.model.Result;
import com.figengungor.thedictionary.data.model.SearchResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by figengungor on 5/21/2018.
 */

public class HomeViewModel extends AndroidViewModel {

    MutableLiveData<List<LexicalEntry>> lexicalEntries;
    MutableLiveData<Boolean> isLoading;
    MutableLiveData<Throwable> error;

    DataManager dataManager;

    public HomeViewModel(Application application, DataManager dataManager) {
        super(application);
        this.dataManager = dataManager;
        lexicalEntries = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        error = new MutableLiveData<>();
    }

    public void search(final String word) {
        isLoading.setValue(true);
        lexicalEntries.setValue(null);
        error.setValue(null);

        dataManager.search(word, new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful()) {
                    SearchResponse searchResponse = response.body();
                    if (searchResponse.getResults() != null && searchResponse.getResults().size() > 0) {
                        Result result = searchResponse.getResults().get(0);
                        if (result.getLexicalEntries() != null && result.getLexicalEntries().size() > 0) {
                            lexicalEntries.setValue(result.getLexicalEntries());
                            saveSearchHistoryEntry(word);
                            error.setValue(null);
                        } else {
                            error.setValue(new Throwable(getApplication().getString(R.string.empty_result)));
                        }
                    } else {
                        error.setValue(new Throwable(getApplication().getString(R.string.empty_result)));
                    }
                } else {
                    if (response.code() == 404)
                        error.setValue(new Throwable(getApplication().getString(R.string.empty_result)));
                    else {
                        error.setValue(new Throwable(getApplication().getString(R.string.server_error)));
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                isLoading.setValue(false);
                error.setValue(t);
            }
        });
    }

    public void saveSearchHistoryEntry(String entry) {
        dataManager.saveSearchHistoryEntry(entry);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplication());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this.getApplication(), SearchHistoryWidgetProvider.class));
        //Trigger data update to handle the ListView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.historyRv);
    }

    public MutableLiveData<List<LexicalEntry>> getLexicalEntries() {
        return lexicalEntries;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<Throwable> getError() {
        return error;
    }
}
