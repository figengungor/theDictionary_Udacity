package com.figengungor.thedictionary.ui.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by figengungor on 6/2/2018.
 */

public class SearchHistoryListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new SearchHistoryListRemoteViewsFactory(getApplicationContext(), intent));
    }
}
