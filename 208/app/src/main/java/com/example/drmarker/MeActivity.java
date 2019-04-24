package com.example.drmarker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drmarker.RealmModule.UserModule;
import com.example.drmarker.userModel.User;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MeActivity extends AppCompatActivity implements View.OnClickListener {

    public static String uid;
    private RealmConfiguration userDB;
    private TextView userInfo;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        //Get the intent from the input activity
        Intent fromAbove = getIntent();
        uid= fromAbove.getStringExtra("uid");
        userDB = new RealmConfiguration.Builder()
                .name("user_db").schemaVersion(2).modules(new UserModule())
                .build();
        Realm mRealm=Realm.getInstance(userDB);
        RealmResults<User> userlist = mRealm.where(User.class).equalTo("uid", uid).findAll();
        User loginUser = userlist.get(0);


        userInfo = (TextView) findViewById(R.id.me_profile);
        userInfo.setText("Name:" +loginUser.getName());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.navigation_me);

        Button button_editName = findViewById(R.id.me_editName);
        Button me_editPic = findViewById(R.id.me_editPic);
        Button me_changePassword = findViewById(R.id.me_editPassword);
        Button me_logout = findViewById(R.id.bt_me_logout);
        Button me_setting = findViewById(R.id.me_setting);

        button_editName.setOnClickListener(this);
        me_editPic.setOnClickListener(this);
        me_changePassword.setOnClickListener(this);
        me_logout.setOnClickListener(this);
        me_setting.setOnClickListener(this);
    }


    public void onClick(View view){
        switch (view.getId()){
            case R.id.me_editName:
                Intent intent_editName=new Intent(MeActivity.this, EditNameActivity.class);
                intent_editName.putExtra("uid",getIntent().getStringExtra("uid"));
                startActivity(intent_editName);
                finish();
                break;
            case R.id.me_editPic:

                break;
            case R.id.me_editPassword:
                Intent intent_editPassword=new Intent(MeActivity.this, EditPasswordActivity.class);
                intent_editPassword.putExtra("uid",getIntent().getStringExtra("uid"));
                startActivity(intent_editPassword);
                finish();
                break;
            case R.id.bt_me_logout:
                Intent intent = new Intent(MeActivity.this,loginActivity.class);
                Toast.makeText(this, "Log out success", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
                break;
            case R.id.me_setting:

                break;
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //Do something
                    Intent intent = new Intent(MeActivity.this, MainActivity.class);
                    intent.putExtra("uid",getIntent().getStringExtra("uid"));
                    startActivity(intent);
                    finish();
                    return true;

                case R.id.navigation_monitor:
                    //Do something
                    Intent intent_monitor=new Intent(MeActivity.this, InputActivity.class);
                    intent_monitor.putExtra("uid",getIntent().getStringExtra("uid"));
                    startActivity(intent_monitor);
                    finish();
                    return true;

                case R.id.navigation_forum:
                    //Do something
                    Intent intent_forum=new Intent(MeActivity.this, ForumActivity.class);
                    intent_forum.putExtra("uid",getIntent().getStringExtra("uid"));
                    startActivity(intent_forum);
                    finish();
                    return true;

                case R.id.navigation_me:
                    //Do something
                    return true;
            }
            return false;
        }
    };
}

