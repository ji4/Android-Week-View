package com.alamkanak.weekview.sample;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class EditEvent extends AppCompatActivity {
    Button btn_startDate, btn_startTime, btn_endDate, btn_endTime;
    DatePickerDialog startDatePickerDialog, endDatePickerDialog;
    TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        findAllViews();

        setStartAndEndTime();

        GregorianCalendar calendar = new GregorianCalendar();
        startDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            //將設定的日期顯示出來
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String dayOfWeek = getDayOfWeek(year, monthOfYear, dayOfMonth - 1);
                monthOfYear += 1; //monthOfYear starts from 0
                btn_startDate.setText(year + " 年 " + monthOfYear + " 月 " + dayOfMonth + " 日 " + dayOfWeek);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));


        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            //將時間轉為12小時製顯示出來
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                btn_startTime.setText((hourOfDay > 12 ? hourOfDay - 12 : hourOfDay)
                + ":" + minute + " " + (hourOfDay > 12 ? "下午" : "上午"));
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(calendar.MINUTE),
                false);

        endDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() { //not completely set yet
            //將設定的日期顯示出來
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                String dayOfWeek = getDayOfWeek(year, monthOfYear, dayOfMonth - 1);
                monthOfYear += 1; //monthOfYear starts from 0
                btn_endDate.setText(year + " 年 " + monthOfYear + " 月 " + dayOfMonth + " 日 " + dayOfWeek);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));



        //Button Click Listeners
        btn_startDate.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDatePickerDialog.show();
            }
        });

        btn_startTime.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog.show();
            }
        });

        btn_endDate.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                endDatePickerDialog.show();
            }
        });






    }

    public String getDayOfWeek(int year, int monthOfYear, int dayOfMonth){
        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
        Date date = new Date(year, monthOfYear, dayOfMonth);
        String dayOfWeek = simpledateformat.format(date);
        return dayOfWeek;
    }

    public void findAllViews(){
        //Find all views.
        btn_startDate = (Button) findViewById(R.id.activity_edit_event_btn_startDate);
        btn_startTime = (Button) findViewById(R.id.activity_edit_event_btn_startTime);
        btn_endDate = (Button) findViewById(R.id.activity_edit_event_btn_endDate);
        btn_endTime = (Button) findViewById(R.id.activity_edit_event_btn_endTime);
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
