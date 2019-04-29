package com.example.drmarker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    public static String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_me);
//        setContentView(R.layout.about_me);
        //Get the intent from the input activity
        Intent fromAbove = getIntent();
        uid= fromAbove.getStringExtra("uid");
        Button button_back = findViewById(R.id.bt_back);
        button_back.setOnClickListener(this);
    }

    public void onClick(View view){
        switch(view.getId()){
            case R.id.bt_back:
                Intent intent_back=new Intent(AboutActivity.this, MeActivity.class);
                intent_back.putExtra("uid",getIntent().getStringExtra("uid"));
                startActivity(intent_back);
                finish();
                break;
        }
    }

}
