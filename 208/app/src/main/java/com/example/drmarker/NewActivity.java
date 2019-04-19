package com.example.drmarker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class NewActivity extends AppCompatActivity {
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        //Get the intent from the input activity
        Intent intent=getIntent();

        TextView tv_height=findViewById(R.id.tv_heightvalue);
        TextView tv_weight=findViewById(R.id.tv_weightvalue);
        //Show the text
        tv_height.setText(intent.getStringExtra("height"));
        tv_weight.setText(intent.getStringExtra("weight"));
    }
}

