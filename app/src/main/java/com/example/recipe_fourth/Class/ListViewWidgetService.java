package com.example.recipe_fourth.Class;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.recipe_fourth.MyWidget;
import com.example.recipe_fourth.R;
import com.example.recipe_fourth.ui.Activity.MainActivity;
import com.example.recipe_fourth.ui.Activity.RecipeDetails;

import java.util.ArrayList;

public class ListViewWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListViewRemoteViewsFactory(this.getApplicationContext(), intent);
    }

}

class ListViewRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;

    private ArrayList<String> records;



    public ListViewRemoteViewsFactory(Context context, Intent intent) {

        mContext = context;

    }

    // Initialize the data set.

    public void onCreate() {

        records=new ArrayList<String>();

    }

    // Given the position (index) of a WidgetItem in the array, use the item's text value in

    // combination with the app widget item XML file to construct a RemoteViews object.

    public RemoteViews getViewAt(int position) {

        // position will always range from 0 to getCount() - 1.
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.list_items);
        // feed row
        String data=records.get(position);
        rv.setTextViewText(R.id.item, data);
        Bundle extras = new Bundle();
        extras.putInt(MyWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtra("homescreen_meeting",data);
        fillInIntent.putExtras(extras);
        // Make it possible to distinguish the individual on-click
        Intent configIntent = new Intent(mContext, MainActivity.class);
        PendingIntent configPendingIntent = PendingIntent.getActivity(mContext, 0, configIntent, 0);
        rv.setOnClickPendingIntent(R.id.appwidget_text, configPendingIntent);
        // action of a given item
        rv.setOnClickFillInIntent(R.id.list_widget_items, fillInIntent);
        rv.setOnClickPendingIntent(R.id.btn_add_widget,configPendingIntent);

        // Return the RemoteViews object.

        return rv;

    }

    public int getCount(){

        if(records==null){
            records=new ArrayList<>();
        }
        Log.e("size=",records.size()+"");

        return records.size();

    }
    public void onDataSetChanged(){

        records= RecipeDetails.arrayList;


    }

    public int getViewTypeCount(){

        return 1;

    }

    public long getItemId(int position) {

        return position;

    }

    public void onDestroy(){
        if(records==null){
            records=new ArrayList<>();
        }
        records.clear();

    }

    public boolean hasStableIds() {

        return true;

    }

    public RemoteViews getLoadingView() {

        return null;

    }

}

