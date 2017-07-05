package com.alamkanak.weekview.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditEvent extends AppCompatActivity {
    Button btn_startDate, btn_startTime, btn_endDate, btn_endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        //Find all views.
        btn_startDate = (Button) findViewById(R.id.activity_edit_event_btn_startDate);
        btn_startTime = (Button) findViewById(R.id.activity_edit_event_btn_startTime);
        btn_endDate = (Button) findViewById(R.id.activity_edit_event_btn_endDate);
        btn_endTime = (Button) findViewById(R.id.activity_edit_event_btn_endTime);


        setStartAndEndTime();





    }

    public void setStartAndEndTime(){
        long TIME_INTERVAL = 600000; //10 mins
        //start date
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy 年 MM 月 dd 日, EEE");
        Date currentDate = new Date(System.currentTimeMillis());
        String strDate = dateFormatter.format(currentDate);
        btn_startDate.setText(strDate);

        //start time
        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
        Date currentTime = new Date(System.currentTimeMillis());
        String strStartTime = timeFormatter.format(currentTime);
        btn_startTime.setText(strStartTime);

        //end date
        Date endDate = new Date(System.currentTimeMillis() + TIME_INTERVAL);
        String strEndDate = dateFormatter.format(currentDate);
        btn_endDate.setText(strEndDate);

        //end time
        Date endTime = new Date(System.currentTimeMillis() + TIME_INTERVAL);
        String strEndTime = timeFormatter.format(endTime);
        btn_endTime.setText(strEndTime);


    }

}
