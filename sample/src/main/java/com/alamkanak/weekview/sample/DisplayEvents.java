package com.alamkanak.weekview.sample;

import android.util.Log;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A basic example of how to use week view library.
 * Created by Raquib-ul-Alam Kanak on 1/3/2014.
 * Website: http://alamkanak.github.io
 */
public class DisplayEvents extends BaseActivity {
    private boolean eventMatches(WeekViewEvent event, int year, int month) {
        return (event.getStartTime().get(Calendar.YEAR) == year && event.getStartTime().get(Calendar.MONTH) == month - 1) || (event.getEndTime().get(Calendar.YEAR) == year && event.getEndTime().get(Calendar.MONTH) == month - 1);
    }


    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

        //Get saved data from DB
        ArrayList<EventData> eventDataArrayList = DB.getData(getApplicationContext(), null);
        Log.d("jia", "eventDataArrayList: " + String.valueOf(eventDataArrayList));
        if(eventDataArrayList != null) {
            Log.d("jia", "eventDataArrayList != null");
            int i = 0;
            while(i < eventDataArrayList.size()) {
                WeekViewEvent event = new WeekViewEvent(1, getEventTitle(eventDataArrayList.get(i).startTime), eventDataArrayList.get(i).startTime, eventDataArrayList.get(i).endTime);
                Log.d("jia", "startTime:　" + eventDataArrayList.get(i).startTime);
                Log.d("jia", "endTime:　  " + eventDataArrayList.get(i).endTime);
                Log.d("jia", "eventName:　" + eventDataArrayList.get(i).eventName);
                event.setColor(getResources().getColor(R.color.event_color_01));
                events.add(event);
                Log.d("jia", "i: "+i);
                i++;
            }
        }

        // Return only the events that matches newYear and newMonth.
        List<WeekViewEvent> matchedEvents = new ArrayList<WeekViewEvent>();
        for (WeekViewEvent event : events) {
            if (eventMatches(event, newYear, newMonth)) {
                matchedEvents.add(event);
            }
        }
        return matchedEvents;
    }

}
