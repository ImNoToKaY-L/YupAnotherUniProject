package com.example.drmarker;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class StepActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager mSensorManager;
    Sensor Counter;
    Sensor Detector;
    int stepSensor=-1;
    int steps = 0;
    TextView tv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        tv = (TextView)findViewById(R.id.tv_step_steps);
        Counter = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Detector = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if(Counter!=null){
            stepSensor = 0;
            mSensorManager.registerListener(this,Counter,SensorManager.SENSOR_DELAY_NORMAL);
        }else if (Detector!=null){
            stepSensor = 1;
            mSensorManager.registerListener(this,Detector,SensorManager.SENSOR_DELAY_NORMAL);

        }else {
            Log.e("FK","NOSENSOR");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (stepSensor==0){
            int tempstep = (int) event.values[0];
            tv.setText(tempstep);
        }
        if(stepSensor == 1){
            steps++;
            tv.setText(steps);
        }
        if (stepSensor==-1){
            tv.setText("你妈的没有sensor");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
