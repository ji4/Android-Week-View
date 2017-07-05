package com.alamkanak.weekview.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.List;

public class MonthView extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_view);
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        return null;
    }
}
