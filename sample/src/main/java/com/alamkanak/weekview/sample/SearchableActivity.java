package com.alamkanak.weekview.sample;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import com.alamkanak.weekview.sample.FeedReaderContract.FeedEntry;


public class SearchableActivity extends AppCompatActivity {
    ArrayAdapter<String> myAdapter;
    ListView listView;
    String[] dataArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        listView = (ListView) findViewById(R.id.listview);

        Intent it = getIntent();
        String query = it.getStringExtra("query");



        //Get saved data from DB
        ArrayList<EventData> eventDataArrayList = DB.getData(getApplicationContext(), FeedEntry.COLUMN_NAME_EVENT_NAME, query);
//        myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, eventDataArrayList);
//        listView.setAdapter(myAdapter);

        if(eventDataArrayList != null) {
            int i = 0;
            while(i < eventDataArrayList.size()) {
                dataArray = new String[eventDataArrayList.size()];
                dataArray[i]= eventDataArrayList.get(i).eventName;
//                WeekViewEvent event = new WeekViewEvent(1, BaseActivity.getEventTitle(eventDataArrayList.get(i).startTime), eventDataArrayList.get(i).startTime, eventDataArrayList.get(i).endTime);
//                event.setColor(getResources().getColor(R.color.event_color_01));
//                events.add(event);
                i++;
            }
            myAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataArray);
            listView.setAdapter(myAdapter);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);

        // Configure the search info and add any event listeners...

        return super.onCreateOptionsMenu(menu);
    }
}
