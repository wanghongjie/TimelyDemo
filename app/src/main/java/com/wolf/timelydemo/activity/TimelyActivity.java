package com.wolf.timelydemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wolf.timelydemo.R;
import com.wolf.timelydemo.widget.TimelyLeftView;

public class TimelyActivity extends AppCompatActivity {

    private TimelyLeftView timelyLeftView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timely);

        timelyLeftView = (TimelyLeftView) findViewById(R.id.time_view);
    }
}
