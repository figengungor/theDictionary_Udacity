package com.figengungor.thedictionary.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.figengungor.thedictionary.R;
import com.figengungor.thedictionary.ui.home.HomeActivity;

/**
 * Implementation of App Widget functionality.
 */
public class SearchHistoryWidgetProvider extends AppWidgetProvider {

    private static final String LIST_ITEM_CLICKED = "com.figengungor.thedictionary.LIST_ITEM_CLICKED";
    public static final String EXTRA_SEARCH_ENTRY = "search_entry";

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.search_history_widget_provider);
        Intent intent = new Intent(context, SearchHistoryListWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        remoteViews.setRemoteAdapter(R.id.historyRv,
                intent);
        remoteViews.setEmptyView(R.id.historyRv, R.id.emptyTv);

        Intent startActivityIntent = new Intent(context, SearchHistoryWidgetProvider.class);
        startActivityIntent.setAction(LIST_ITEM_CLICKED);
        PendingIntent startActivityPendingIntent = PendingIntent.getBroadcast(context, appWidgetId, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.historyRv, startActivityPendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.historyRv);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (LIST_ITEM_CLICKED.equals(intent.getAction())) {
            String searchEntry = intent.getExtras().getString(EXTRA_SEARCH_ENTRY);
            Intent startActivityIntent = new Intent(context, HomeActivity.class);
            startActivityIntent.putExtra(EXTRA_SEARCH_ENTRY, searchEntry);
            startActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(startActivityIntent);
        }
    }

}