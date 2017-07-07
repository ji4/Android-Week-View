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
    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        return null;
    }
}
