package com.example.recipe_fourth;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.recipe_fourth.Class.ListViewWidgetService;
import com.example.recipe_fourth.ui.Activity.MainActivity;

import java.util.ArrayList;

import io.paperdb.Paper;

public class MyWidget extends AppWidgetProvider  {

    static  String CLICK_ACTION="CLIKED";
    public static final String UPDATE_MEETING_ACTION = "android.appwidget.action.APPWIDGET_UPDATE";

    public static final String EXTRA_ITEM = "com.example.edockh.EXTRA_ITEM";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        Paper.init(context);
        ArrayList<String> ingredient=Paper.book().read("INGREDIENTS");
        String ID=Paper.book().read("ID");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_widget);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int i = 0; i < appWidgetIds.length; ++i) {

            // Set up the intent that starts the ListViewService, which will

            // provide the views for this collection.

            Intent intent = new Intent(context, ListViewWidgetService.class);

            // Add the app widget ID to the intent extras.

            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));


            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.my_widget);


            rv.setRemoteAdapter(appWidgetIds[i], R.id.list_widget_items, intent);


            Intent configIntent = new Intent(context, MainActivity.class);
            PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);


            PendingIntent startActivityPendingIntent = PendingIntent.getActivity(
                    context, 0, configIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            rv.setOnClickPendingIntent(R.id.btn_add_widget,configPendingIntent);
            rv.setPendingIntentTemplate(R.id.btn_add_widget, startActivityPendingIntent);


            rv.setEmptyView(R.id.list_widget_items, R.id.empty_view);


            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);

        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);

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

        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(UPDATE_MEETING_ACTION)) {
            int appWidgetIds[] = mgr.getAppWidgetIds(new ComponentName(context,MyWidget.class));
            Log.e("received", intent.getAction());

             mgr.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_widget_items);
        }
        super.onReceive(context, intent);

    }
   /* protected void onMessage(Context context, Intent data) {

        Intent intent_meeting_update=new  Intent(context,MyWidget.class);
        intent_meeting_update.setAction(MyWidget.UPDATE_MEETING_ACTION);

        sendBroadcast(intent_meeting_update);

    }*/

}

