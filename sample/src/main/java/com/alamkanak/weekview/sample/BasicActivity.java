package com.alamkanak.weekview.sample;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * A basic example of how to use week view library.
 * Created by Raquib-ul-Alam Kanak on 1/3/2014.
 * Website: http://alamkanak.github.io
 */
public class BasicActivity extends BaseActivity {
    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        Log.d("jia", "onMonthChange()");


        // Populate the week view with some events.
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

        //Get saved data from DB
        EventData eventData = DB.getData(getApplicationContext());
        if(eventData != null) {
            WeekViewEvent event = new WeekViewEvent(1, getEventTitle(eventData.startTime), eventData.startTime, eventData.endTime);
            event.setColor(getResources().getColor(R.color.event_color_01));
            events.add(event);
        }

        return events;

    }

}
