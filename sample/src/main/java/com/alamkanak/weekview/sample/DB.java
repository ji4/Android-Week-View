package com.alamkanak.weekview.sample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.alamkanak.weekview.sample.FeedReaderContract.FeedEntry;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.Calendar;
import java.util.List;

/**
 * Created by chiaying.wu on 2017/7/7.
 */

public class DB extends BaseActivity{
    public DB() {
    }

    public static void saveData(Context context, long startTimeMillis, long endTimeMillis) {
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

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                FeedEntry.TABLE_NAME,
                FeedEntry.COLUMN_NAME_NULLABLE,
                values);

    }
    public static EventData getData(Context context) {
        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        EventData eventData = null;

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FeedEntry._ID,
                FeedEntry.COLUMN_NAME_TITLE,
                FeedEntry.START_TIME,
                FeedEntry.END_TIME,
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                FeedEntry.START_TIME/*COLUMN_NAME_UPDATED*/ + " DESC";

        Cursor cursor = db.query(
                FeedEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                FeedEntry.COLUMN_NAME_ENTRY_ID + "=?"/*selection*/,                                // The columns for the WHERE clause
                new String[]{"1"} /*selectionArgs*/,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();

            long startTimeMillisec = cursor.getLong(
                    cursor.getColumnIndexOrThrow(FeedEntry.START_TIME)
            );
            long endTimeMillisec = cursor.getLong(
                    cursor.getColumnIndexOrThrow(FeedEntry.END_TIME)
            );

            Calendar startTime = Calendar.getInstance();
            startTime.setTimeInMillis(startTimeMillisec);
            Log.d("jia", "startTime: " + String.valueOf(startTime));

            Calendar endTime = Calendar.getInstance();
            endTime.setTimeInMillis(endTimeMillisec);
            Log.d("jia", "endTime: " + String.valueOf(endTime));

            cursor.close();

            eventData = new EventData(startTime, endTime);

        }
        if(eventData!=null)
           return eventData.getEventData();
        return null;
    }
    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        return null;
    }
}
class EventData{
    Calendar startTime, endTime;

    public EventData(Calendar startTime, Calendar endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    EventData getEventData(){
        EventData eventData = new EventData(startTime, endTime);
        return eventData;
    }

}