package com.example.drmarker;


import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.drmarker.Recommend.Food;
import com.example.drmarker.Recommend.InitDBHandler;
import com.example.drmarker.Recommend.Sport;

import java.io.InputStream;

import io.realm.Realm;


public class StepActivity extends AppCompatActivity {

    SensorManager mSensorManager;
    Sensor Counter;
    Sensor Detector;
    int stepSensor=-1;
    int steps = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);



    }
}




