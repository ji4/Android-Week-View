package com.alamkanak.weekview.sample;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.alamkanak.weekview.sample.FeedReaderContract.FeedEntry;

public class DisplayEvents extends BaseActivity {
    private boolean eventMatches(WeekViewEvent event, int year, int month) {
        return (event.getStartTime().get(Calendar.YEAR) == year && event.getStartTime().get(Calendar.MONTH) == month - 1) || (event.getEndTime().get(Calendar.YEAR) == year && event.getEndTime().get(Calendar.MONTH) == month - 1);
    }


    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

        //Get saved data from DB
        ArrayList<EventData> eventDataArrayList = DB.getData(getApplicationContext(), FeedEntry.COLUMN_NAME_TITLE, null);
        if(eventDataArrayList != null) {
            int i = 0;
            while(i < eventDataArrayList.size()) {
                if(eventDataArrayList.get(i).eventName == null)
                    eventDataArrayList.get(i).eventName = getString(R.string.displayevents_notitle);
                WeekViewEvent event = new WeekViewEvent(eventDataArrayList.get(i).eventId, eventDataArrayList.get(i).eventName, eventDataArrayList.get(i).startTime, eventDataArrayList.get(i).endTime);
                event.setColor(getResources().getColor(R.color.event_color_01));
                events.add(event);
                i++;
            }
        }

        // Return only the events that matches newYear and newMonth.
        List<WeekViewEvent> matchedEvents = new ArrayList<>();
        for (WeekViewEvent event : events) {
            if (eventMatches(event, newYear, newMonth)) {
                matchedEvents.add(event);
            }
        }
        return matchedEvents;
    }

}
