package com.alamkanak.weekview.sample;

import android.provider.BaseColumns;

import java.util.Calendar;

/**
 * Created by chiaying.wu on 2017/7/6.
 */

public class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "calenderEvents";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "event";
        public static final String COLUMN_NAME_NULLABLE = "nodata";
        public static final String startTime = "startTime";
        public static final String endTime = "endTime";
//        public static final String COLUMN_NAME_SUBTITLE = "subtitle";



    }
}
