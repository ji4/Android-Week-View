package com.alamkanak.weekview.sample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.alamkanak.weekview.sample.FeedReaderContract.FeedEntry;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by chiaying.wu on 2017/7/7.
 */

public class DB extends BaseActivity{
    public DB() {
    }

    public static void saveData(Context context, long startTimeMillis, long endTimeMillis, String eventName, String eventTarget, String eventLocation) {
        //若要存取您的資料庫，請啟動 SQLiteOpenHelper 的子類別：
        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(context);

        //將資訊置入資料庫中
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_ENTRY_ID, 1);
        values.put(FeedEntry.COLUMN_NAME_TITLE, "myEvent");
        values.put(FeedEntry.START_TIME, startTimeMillis);
        values.put(FeedEntry.END_TIME, endTimeMillis);
        values.put(FeedEntry.COLUMN_NAME_EVENT_NAME, eventName);
        values.put(FeedEntry.COLUMN_NAME_EVENT_TARGET, eventTarget);
        values.put(FeedEntry.COLUMN_NAME_EVENT_LOCATION, eventLocation);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                FeedEntry.TABLE_NAME,
                FeedEntry.COLUMN_NAME_NULLABLE,
                values);

        Log.d("jia", "newRowId:　"+newRowId);

    }


    public static ArrayList<EventData> getData(Context context, String selection, String selectionArgs) {
        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FeedEntry._ID,
                FeedEntry.COLUMN_NAME_TITLE,
                FeedEntry.START_TIME,
                FeedEntry.END_TIME,
                FeedEntry.COLUMN_NAME_EVENT_NAME,
                FeedEntry.COLUMN_NAME_EVENT_TARGET,
                FeedEntry.COLUMN_NAME_EVENT_LOCATION
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                FeedEntry.START_TIME/*COLUMN_NAME_UPDATED*/ + " DESC";

        Cursor cursor;
        if(selectionArgs == null) { //Select all
            cursor = db.query(
                    FeedEntry.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    FeedEntry.COLUMN_NAME_TITLE + "=?",                                // The columns for the WHERE clause
                    new String[]{"myEvent"} ,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );
        }
        else{
            cursor = db.query(
                    FeedEntry.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    selection + "=?",                                // The columns for the WHERE clause
                    new String[]{selectionArgs} ,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

        }

        ArrayList<EventData> eventDataArrayList = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();
            getAndAddDataToList(cursor, eventDataArrayList);
            while(cursor.moveToNext()) {
                getAndAddDataToList(cursor, eventDataArrayList);
            }
            cursor.close();



        }
        if(!eventDataArrayList.isEmpty()) {
            return eventDataArrayList;
        }
        return null;
    }

    public static void getAndAddDataToList(Cursor cursor, ArrayList eventDataArrayList){
        long eventId = cursor.getLong(
                cursor.getColumnIndexOrThrow(FeedEntry._ID)
        );
        long startTimeMillisec = cursor.getLong(
                cursor.getColumnIndexOrThrow(FeedEntry.START_TIME)
        );
        long endTimeMillisec = cursor.getLong(
                cursor.getColumnIndexOrThrow(FeedEntry.END_TIME)
        );
        String eventName = cursor.getString(
                cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_EVENT_NAME)
        );
        String eventTarget = cursor.getString(
                cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_EVENT_TARGET)
        );
        String eventLocation = cursor.getString(
                cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_EVENT_LOCATION)
        );

        Calendar startTime = Calendar.getInstance();
        startTime.setTimeInMillis(startTimeMillisec);
        Log.d("jia", "startTime: " + String.valueOf(startTime));

        Calendar endTime = Calendar.getInstance();
        endTime.setTimeInMillis(endTimeMillisec);
        Log.d("jia", "endTime: " + String.valueOf(endTime));

        EventData eventData = new EventData(eventId, startTime, endTime, eventName, eventTarget, eventLocation);
        eventDataArrayList.add(eventData.getEventData());
    }

    public static void updateData(Context context, long eventId, long startTimeMillis, long endTimeMillis, String eventName, String eventTarget, String eventLocation){
        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_ENTRY_ID, 1);
        values.put(FeedEntry.COLUMN_NAME_TITLE, "myEvent");
        values.put(FeedEntry.COLUMN_NAME_EVENT_NAME, eventName);
        values.put(FeedEntry.START_TIME, startTimeMillis);
        values.put(FeedEntry.END_TIME, endTimeMillis);
        values.put(FeedEntry.COLUMN_NAME_EVENT_TARGET, eventTarget);
        values.put(FeedEntry.COLUMN_NAME_EVENT_LOCATION, eventLocation);

        // Which row to update, based on the ID
        String selection = FeedEntry._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(eventId) };

        int count = db.update(
                FeedEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        Log.d("count", String.valueOf(count));
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        return null;
    }
}
class EventData{
    long eventId;
    Calendar startTime, endTime;
    String eventName, eventTarget, eventLocation;

    public EventData(long eventId, Calendar startTime, Calendar endTime, String eventName, String eventTarget, String eventLocation) {
        this.eventId = eventId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventName = eventName;
        this.eventTarget = eventTarget;
        this.eventLocation = eventLocation;
    }

    EventData getEventData(){
        return this;
    }

}