package com.alamkanak.weekview.sample;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.alamkanak.weekview.WeekViewEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import com.alamkanak.weekview.sample.FeedReaderContract.FeedEntry;


public class EditEvent extends BaseActivity {
    Button btn_startDate, btn_startTime, btn_endDate, btn_endTime;
    Button btn_save;
    EditText et_eventName, et_eventTarget, et_eventLocation;
    String strEventName = null, strEventTarget = null, strEventLocation = null;
    long TIME_INTERVAL = 600000; //10 mins
    long clickedTimeMillis; //calendar's colum's time
    long eventId = -1;
    //TimePickers
    DatePickerDialog startDatePickerDialog, endDatePickerDialog;
    TimePickerDialog startTimePickerDialog, endTimePickerDialog;
    Calendar selectedStartTime = Calendar.getInstance();
    Calendar selectedEndTime = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        findAllViews();
        receiveIntents();
        setDisplayStartAndEndTime();
        defineAllTimeDialogs();
        listenForTimeButtonsClick();

        btn_save.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInputData();

                long startTimeMillis = selectedStartTime.getTimeInMillis();
                long endTimeMillis = selectedEndTime.getTimeInMillis();

                if(eventId == -1) {//save data
                    DB.saveData(getApplicationContext(), startTimeMillis, endTimeMillis, strEventName, strEventTarget, strEventLocation);

                }
                else {//update data
                    DB.updateData(getApplicationContext(), eventId, startTimeMillis, endTimeMillis, strEventName, strEventTarget, strEventLocation);
                }
                finish();
            }
        });

    }

    public void findAllViews(){
        btn_startDate = (Button) findViewById(R.id.activity_edit_event_btn_startDate);
        btn_startTime = (Button) findViewById(R.id.activity_edit_event_btn_startTime);
        btn_endDate = (Button) findViewById(R.id.activity_edit_event_btn_endDate);
        btn_endTime = (Button) findViewById(R.id.activity_edit_event_btn_endTime);

        et_eventName = (EditText) findViewById(R.id.activity_edit_event_et_eventName);
        et_eventLocation = (EditText) findViewById(R.id.activity_edit_event_et_eventLocation);
        et_eventTarget = (EditText) findViewById(R.id.activity_edit_event_et_eventTarget);

        btn_save = (Button) findViewById(R.id.activity_edit_event_btn_save);
    }
    public void receiveIntents(){
        //Receive Clicked Calendar Empty Colum's Time
        Intent it = this.getIntent();
        clickedTimeMillis = it.getLongExtra("timeMillis", 0);

        //Receive Clicked Calendar Existent Event
        eventId = it.getLongExtra("eventId", -1);
        if(eventId != -1) { //Edit existent event
            ArrayList<EventData> eventDataArrayList = DB.getData(getApplicationContext(), FeedEntry._ID, String.valueOf(eventId));
            assert eventDataArrayList != null;
            EventData eventData = eventDataArrayList.get(0);
            et_eventName.setText(eventData.eventName);
            et_eventTarget.setText(eventData.eventTarget);
            et_eventLocation.setText(eventData.eventLocation);
        }
    }


    public void setDisplayStartAndEndTime(){
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy 年 MM 月 dd 日, EEE");
//        SimpleDateFormat dateFormatter = new SimpleDateFormat(String.format("yyyy %s MM %s dd %s, EEE", R.string.editevent_year, R.string.editevent_month, R.string.editevent_day));
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

    public void defineAllTimeDialogs(){
        //Set Received Time for TimePicker Dialogs
        selectedStartTime.setTimeInMillis(clickedTimeMillis);
        selectedEndTime.setTimeInMillis(clickedTimeMillis + TIME_INTERVAL);

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(clickedTimeMillis);

        Calendar addTime = Calendar.getInstance();
        addTime.setTimeInMillis(TIME_INTERVAL);

        startDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            //將設定的日期顯示出來
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                setDate(selectedStartTime, btn_startDate, year, monthOfYear, dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)); //clickedTimeMillis's calendar


        startTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            //將時間轉為12小時製顯示出來
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                setTime(selectedEndTime, btn_endTime, hourOfDay, minute);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(calendar.MINUTE) + addTime.get(Calendar.MINUTE),
                false);

        endDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() { //not completely set yet
            //將設定的日期顯示出來
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                setDate(selectedEndTime, btn_endDate, year, monthOfYear, dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        endTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            //將時間轉為12小時製顯示出來
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                setTime(selectedEndTime, btn_endTime, hourOfDay, minute);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(calendar.MINUTE) + addTime.get(Calendar.MINUTE),
                false);
    }

    public void setDate(Calendar selectedDate, Button btn_Date, int year, int monthOfYear, int dayOfMonth){
        selectedDate.set(year, monthOfYear, dayOfMonth);

        String dayOfWeek = getDayOfWeek(year, monthOfYear, dayOfMonth - 1);
        btn_Date.setText(year + R.string.editevent_year + (monthOfYear + 1) + R.string.editevent_month + dayOfMonth + R.string.editevent_day + dayOfWeek); //monthOfYear starts from 0
    }

    public void setTime(Calendar selectedTime, Button btn_time, int hourOfDay, int minute){
        selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        selectedTime.set(Calendar.MINUTE, minute);

        btn_time.setText((hourOfDay > 12 ? hourOfDay - 12 : hourOfDay)
                + ":" + minute + " " + (hourOfDay > 12 ? R.string.editevent_pm : R.string.editevent_am));
    }

    public void listenForTimeButtonsClick(){
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
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        return null;
    }
}