package com.alamkanak.weekview.sample;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.alamkanak.weekview.WeekViewEvent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class EditEvent extends BaseActivity {
    Button btn_startDate, btn_startTime, btn_endDate, btn_endTime;
    Button btn_save;
    EditText et_eventName, et_eventTarget, et_eventLocation;
    String strEventName = null, strEventTarget = null, strEventLocation = null;
    DatePickerDialog startDatePickerDialog, endDatePickerDialog;
    TimePickerDialog startTimePickerDialog, endTimePickerDialog;
    long TIME_INTERVAL = 600000; //10 mins
    Calendar selectedStartTime = Calendar.getInstance();
    Calendar selectedEndTime = Calendar.getInstance();
    long clickedTimeMillis;
    long selectedStartTimeMillis = 0, selectedEndTimeMillis = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        findAllViews();
        setDisplayStartAndEndTime();
        defineAllDialogs();
        listenForTimeButtonClicks();

        btn_save.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("jia", "day of selectedStartTime: "+ String.valueOf(selectedStartTime));
                if(selectedStartTimeMillis == clickedTimeMillis) { //default time
                    selectedStartTime.setTimeInMillis(selectedStartTimeMillis);
                    selectedEndTimeMillis = selectedStartTimeMillis + TIME_INTERVAL;
                    selectedEndTime.setTimeInMillis(selectedEndTimeMillis);
                }

                saveEventData(selectedStartTime, selectedEndTime);
                finish();
            }
        });

    }

    public void findAllViews(){
        //Find all views.
        btn_startDate = (Button) findViewById(R.id.activity_edit_event_btn_startDate);
        btn_startTime = (Button) findViewById(R.id.activity_edit_event_btn_startTime);
        btn_endDate = (Button) findViewById(R.id.activity_edit_event_btn_endDate);
        btn_endTime = (Button) findViewById(R.id.activity_edit_event_btn_endTime);

        et_eventName = (EditText) findViewById(R.id.activity_edit_event_et_eventName);
        et_eventLocation = (EditText) findViewById(R.id.activity_edit_event_et_eventLocation);
        et_eventTarget = (EditText) findViewById(R.id.activity_edit_event_et_eventTarget);

        btn_save = (Button) findViewById(R.id.activity_edit_event_btn_save);
    }

    public void setDisplayStartAndEndTime(){
        Intent it = this.getIntent();
        clickedTimeMillis = it.getLongExtra("timeMillis", 0);

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy 年 MM 月 dd 日, EEE");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");

        setStartDate(dateFormatter, clickedTimeMillis);
        setStartTime(timeFormatter, clickedTimeMillis);

        setEndDate(dateFormatter, clickedTimeMillis);
        setEndTime(timeFormatter, clickedTimeMillis);

    }

    public void setStartDate(SimpleDateFormat dateFormatter, long timeMillis){//start date
        Date startDate = new Date(timeMillis);
        String strDate = dateFormatter.format(startDate);
        btn_startDate.setText(strDate);
    }
    public void setStartTime(SimpleDateFormat timeFormatter, long timeMillis) {//start time
        Date startTime = new Date(timeMillis);
        String strStartTime = timeFormatter.format(startTime);
        btn_startTime.setText(strStartTime);
    }
    public void setEndDate(SimpleDateFormat dateFormatter, long timeMillis){//end date
        Date endDate = new Date(timeMillis + TIME_INTERVAL);
        String strEndDate = dateFormatter.format(endDate);
        btn_endDate.setText(strEndDate);
    }
    public void setEndTime(SimpleDateFormat timeFormatter, long timeMillis){//end time
        Date endTime = new Date(timeMillis + TIME_INTERVAL);
        String strEndTime = timeFormatter.format(endTime);
        btn_endTime.setText(strEndTime);

    }

    public void defineAllDialogs(){
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(clickedTimeMillis);

        selectedStartTimeMillis = clickedTimeMillis;

        Calendar addTime = Calendar.getInstance();
        addTime.setTimeInMillis(TIME_INTERVAL);

        startDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            //將設定的日期顯示出來
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectedStartTimeMillis = selectedStartTime.getTimeInMillis();
                selectedStartTime.set(year, monthOfYear, dayOfMonth);

                String dayOfWeek = getDayOfWeek(year, monthOfYear, dayOfMonth - 1);
                btn_startDate.setText(year + " 年 " + (monthOfYear + 1) + " 月 " + dayOfMonth + " 日 " + dayOfWeek); //monthOfYear starts from 0
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));


        startTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            //將時間轉為12小時製顯示出來
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                selectedStartTimeMillis = selectedStartTime.getTimeInMillis();
                selectedStartTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selectedStartTime.set(Calendar.MINUTE, minute);

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
                selectedEndTime.set(year, monthOfYear, dayOfMonth);

                String dayOfWeek = getDayOfWeek(year, monthOfYear, dayOfMonth - 1);
                btn_endDate.setText(year + " 年 " + (monthOfYear + 1) + " 月 " + dayOfMonth + " 日 " + dayOfWeek); //monthOfYear starts from 0
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        endTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            //將時間轉為12小時製顯示出來
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                selectedEndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selectedEndTime.set(Calendar.MINUTE, minute);

                btn_endTime.setText((hourOfDay > 12 ? hourOfDay - 12 : hourOfDay)
                        + ":" + minute + " " + (hourOfDay > 12 ? "下午" : "上午"));
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(calendar.MINUTE) + addTime.get(Calendar.MINUTE),
                false);
    }

    public void listenForTimeButtonClicks(){
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
                startTimePickerDialog.show();
            }
        });
        btn_endDate.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                endDatePickerDialog.show();
            }
        });
        btn_endTime.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                endTimePickerDialog.show();
            }
        });
    }

    public String getDayOfWeek(int year, int monthOfYear, int dayOfMonth){
        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
        Date date = new Date(year, monthOfYear, dayOfMonth);
        return simpledateformat.format(date);
    }

    public void saveEventData(Calendar startTime, Calendar endTime){
        Log.d("jia", String.valueOf(startTime));
        Log.d("jia", String.valueOf(endTime));
        getInputData();

        long startTimeMillis = startTime.getTimeInMillis();
        long endTimeMillis = endTime.getTimeInMillis();

        DB.saveData(getApplicationContext(), startTimeMillis, endTimeMillis, strEventName, strEventTarget, strEventLocation);
    }

    public void getInputData(){
        strEventName = et_eventName.getText().toString().trim();
        strEventTarget = et_eventTarget.getText().toString().trim();
        strEventLocation = et_eventLocation.getText().toString().trim();

        if(strEventName.length() == 0)
            strEventName = null;
        if(strEventTarget.length() == 0)
            strEventTarget = null;
        if(strEventLocation.length() == 0)
            strEventLocation = null;

        Log.d("jia", "strEventName: "+String.valueOf(strEventName));
//        Log.d("jia", "strEventTarget: "+String.valueOf(strEventTarget));
//        Log.d("jia", "strEventLocation: "+String.valueOf(strEventLocation));

    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        return null;
    }
}