package com.example.drmarker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class ForumActivity extends AppCompatActivity {

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        //Get the intent from the input activity

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.navigation_forum);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //Do something
                    Intent intent = new Intent(ForumActivity.this, MainActivity.class);
                    intent.putExtra("uid",getIntent().getStringExtra("uid"));
                    startActivity(intent);
                    finish();
                    return true;

                case R.id.navigation_monitor:
                    //Do something
                    Intent intent_monitor=new Intent(ForumActivity.this, InputActivity.class);
                    intent_monitor.putExtra("uid",getIntent().getStringExtra("uid"));
                    startActivity(intent_monitor);
                    finish();
                    return true;

                case R.id.navigation_forum:
                    //Do nothing
                    return true;

                case R.id.navigation_me:
                    //Do something
                    Intent intent_me=new Intent(ForumActivity.this, MeActivity.class);
                    intent_me.putExtra("uid",getIntent().getStringExtra("uid"));
                    startActivity(intent_me);
                    finish();
                    return true;
            }
            return false;
        }
    };

}
