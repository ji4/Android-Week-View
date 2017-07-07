package com.alamkanak.weekview.sample;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.alamkanak.weekview.WeekViewEvent;
import com.alamkanak.weekview.WeekViewLoader;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class EditEvent extends BaseActivity {
    Button btn_startDate, btn_startTime, btn_endDate, btn_endTime;
    Button btn_save;
    DatePickerDialog startDatePickerDialog, endDatePickerDialog;
    TimePickerDialog startTimePickerDialog, endTimePickerDialog;
    long TIME_INTERVAL = 600000; //10 mins

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        findAllViews();

        setStartAndEndTime();

        GregorianCalendar calendar = new GregorianCalendar();
        final Calendar selectedStartTime = Calendar.getInstance();
        final Calendar selectedEndTime = Calendar.getInstance();
        Calendar addTime = Calendar.getInstance();
        addTime.setTimeInMillis(TIME_INTERVAL);

        startDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            //將設定的日期顯示出來
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectedStartTime.set(year, monthOfYear, dayOfMonth);

                String dayOfWeek = getDayOfWeek(year, monthOfYear, dayOfMonth - 1);
                monthOfYear += 1; //monthOfYear starts from 0
                btn_startDate.setText(year + " 年 " + monthOfYear + " 月 " + dayOfMonth + " 日 " + dayOfWeek);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));


        startTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            //將時間轉為12小時製顯示出來
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
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
                monthOfYear += 1; //monthOfYear starts from 0
                btn_endDate.setText(year + " 年 " + monthOfYear + " 月 " + dayOfMonth + " 日 " + dayOfWeek);
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

        btn_save.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEventData(selectedStartTime, selectedEndTime);
                finish();
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
        btn_save = (Button) findViewById(R.id.activity_edit_event_btn_save);
    }

    public void setStartAndEndTime(){
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

    public void saveEventData(Calendar startTime, Calendar endTime){
        Log.d("jia", String.valueOf(startTime));
        Log.d("jia", String.valueOf(endTime));
        long startTimeMillis = startTime.getTimeInMillis();
        long endTimeMillis = endTime.getTimeInMillis();

        DB.saveData(getApplicationContext(), startTimeMillis, endTimeMillis);



    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        return null;
    }
}