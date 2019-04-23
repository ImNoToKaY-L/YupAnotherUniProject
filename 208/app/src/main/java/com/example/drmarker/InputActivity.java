package com.example.drmarker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.drmarker.RealmModule.UserInfoModule;
import com.example.drmarker.userModel.UserInformation;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class InputActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edit_height;
    private EditText edit_weight;
    private EditText edit_waist;
    private EditText edit_hip;
    private Realm userInfoDB;
    public static final int SEDENTARY = 1;
    public static final int LIGHTLY_ACTIVE = 2;
    public static final int MODERATELY_ACTIVE = 3;
    public static final int VERY_ACTIVE = 4;
    public static final int EXTREMELY_ACTIVE = 5;
    private int activeType;
    private UserInformation userInfo;
    private ArrayAdapter<String> adapter, adapter_gender, adapter_age;
    private Spinner categorySpinner;
    private Spinner ageSpinner;
    private Spinner genderSpinner;
    private boolean categorySelected = false;
    private boolean genderSelected = false;
    private boolean ageSelected = false;
    private boolean HWInput = false;
    private boolean advanceView = false;
    private String category, gender, YOB, uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        getInfo();
        uid = getIntent().getStringExtra("uid");
        categorySpinner = (Spinner) findViewById(R.id.input_spinner);
        genderSpinner = (Spinner) findViewById(R.id.spinner_gender);
        ageSpinner = (Spinner) findViewById(R.id.spinner_age);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, initDataList());
        adapter_gender = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, initGenderList());
        adapter_age = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, initAgeList());

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_age.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpinner.setAdapter(adapter);
        genderSpinner.setAdapter(adapter_gender);
        ageSpinner.setAdapter(adapter_age);


        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = adapter.getItem(position);
                Log.d("position", position + "");
                if (category.equals(getString(R.string.sport_None))) {
                    categorySelected = false;
                } else {
                    categorySelected = true;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                categorySelected = false;
            }
        });


        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = adapter_gender.getItem(position);
                Log.d("gender", "onItemSelected: " + gender);
                if (gender.equals(getString(R.string.gender_None))) {
                    genderSelected = false;
                } else {
                    genderSelected = true;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                genderSelected = false;
            }
        });


        ageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                YOB = adapter_age.getItem(position);
                Log.d("position", "Agep: " + position);
                if (YOB.equals(getString(R.string.age_none))) {
                    ageSelected = false;
                } else {
                    ageSelected = true;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ageSelected = false;
            }
        });


        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.navigation_monitor);
        initExistUserInfo();
        for (UserInformation u : userInfoDB.where(UserInformation.class).findAll()) {
            Log.d(u.getUid(), u.getGender() + " " + u.getHeight() + " " + u.getYOB() + " " + u.getCategory());
        }
        if (userInfo != null) selectedItemsChange();

    }

    private void initExistUserInfo() {
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("userInfo_db").modules(new UserInfoModule()).deleteRealmIfMigrationNeeded()
                .build();
        userInfoDB = Realm.getInstance(realmConfig);
        UserInformation tempUserInfo = userInfoDB.where(UserInformation.class).equalTo("uid", uid).findFirst();
        if (tempUserInfo != null) {
            userInfo = tempUserInfo;
            Log.d("uinfo", "Found");
        } else {
            userInfo = null;
            Log.d("uinfo", "didnt find");
        }
    }

    private void selectedItemsChange() {
        edit_height.setText(userInfo.getHeight());
        edit_weight.setText(userInfo.getWeight());
        if (userInfo.getGender().equals("male")) genderSpinner.setSelection(1);
        if (userInfo.getGender().equals("female")) genderSpinner.setSelection(2);
        categorySpinner.setSelection(userInfo.getCategory());
        int agePosition = 2019 - userInfo.getYOB() + 1;
        ageSpinner.setSelection(agePosition);
        if (userInfo.getWaist().length() != 0) {
            edit_waist.setText(userInfo.getWaist());
        }
        if (userInfo.getHip().length() != 0) {
            edit_hip.setText(userInfo.getHip());
        }

    }

    private List<String> initDataList() {
        List<String> dataList = new ArrayList<>();
        dataList.add(getString(R.string.sport_None));
        dataList.add(getString(R.string.sport_Sedentary));
        dataList.add(getString(R.string.sport_LightlyActive));
        dataList.add(getString(R.string.sport_ModeratelyActive));
        dataList.add(getString(R.string.sport_VeryActive));
        dataList.add(getString(R.string.sport_ExtremelyActive));

        return dataList;

    }

    private int choiceToType(String choice) {
        if (choice.equals(getString(R.string.sport_Sedentary))) return SEDENTARY;
        else if (choice.equals(getString(R.string.sport_LightlyActive))) return LIGHTLY_ACTIVE;
        else if (choice.equals(getString(R.string.sport_VeryActive))) return VERY_ACTIVE;
        else if (choice.equals(getString(R.string.sport_ModeratelyActive)))
            return MODERATELY_ACTIVE;
        else if (choice.equals(getString(R.string.sport_ExtremelyActive))) return EXTREMELY_ACTIVE;
        return 0;

    }

    private List<String> initGenderList() {
        List<String> dataList = new ArrayList<>();
        dataList.add(getString(R.string.gender_None));
        dataList.add("male");
        dataList.add("female");
        return dataList;
    }


    private List<String> initAgeList() {
        List<String> dataList = new ArrayList<>();
        dataList.add(getString(R.string.age_none));
        for (int i = 2019; i > 1925; i--) {
            dataList.add(String.valueOf(i));
        }
        return dataList;
    }


    private void getInfo() {
        //Get the height and weight
        edit_height = findViewById(R.id.edit_height);
        edit_weight = findViewById(R.id.edit_weight);
        edit_waist = findViewById(R.id.edit_waist);
        edit_hip = findViewById(R.id.edit_hip);

        //Initialize the button
        Button button_send = findViewById(R.id.bt_edit);
        button_send.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_edit:
                //Initialize an intent to the receive class
                if (edit_height.getText().toString().trim().length() != 0
                        && edit_weight.getText().toString().trim().length() != 0) HWInput = true;
                else HWInput = false;
                Log.d("HAM", "{" + edit_height.getText().toString().trim() + "}");

                if (categorySelected && genderSelected && ageSelected && HWInput) {
                    Intent intent = new Intent(InputActivity.this, NewActivity.class);
                    //Get the input from text view
                    final String height = edit_height.getText().toString().trim();
                    final String weight = edit_weight.getText().toString().trim();
                    final String waist = edit_waist.getText().toString().trim();
                    final String hip = edit_hip.getText().toString().trim();

                    //put the info into the intent and send it
                    intent.putExtra("height", height);
                    intent.putExtra("weight", weight);
                    intent.putExtra("gender", gender);
                    intent.putExtra("yob", YOB);
                    Log.d("ANAL", YOB + " from above");
                    intent.putExtra("uid", getIntent().getStringExtra("uid"));
                    intent.putExtra("activeType", choiceToType(category));
                    intent.putExtra("waist", waist);
                    intent.putExtra("hip", hip);
                    if (userInfo != null) {
                        userInfoDB.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                UserInformation user = realm.where(UserInformation.class)
                                        .equalTo("uid", uid)
                                        .findFirst();
                                user.setCategory(choiceToType(category));
                                user.setGender(gender);
                                user.setHeight(height);
                                user.setWeight(weight);
                                user.setHip(hip);
                                user.setYOB(Integer.parseInt(YOB));
                                user.setWaist(waist);
                            }
                        });
                    } else {
                        final UserInformation user = new UserInformation(uid, height, weight,
                                waist, hip, gender, choiceToType(category), Integer.parseInt(YOB));
                        userInfoDB.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealm(user);
                                Log.d("uinfo", "inserted user " + user.getUid());

                                for (UserInformation u : realm.where(UserInformation.class).findAll()) {
                                    Log.d("uinfo", u.getUid());
                                }


                            }
                        });
                    }


                    startActivity(intent);
                    finish();
                } else
                    Toast.makeText(this, "Compulsory fields are blank", Toast.LENGTH_SHORT).show();

        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //Do something
                    Intent intent_home = new Intent(InputActivity.this, MainActivity.class);
                    intent_home.putExtra("uid", getIntent().getStringExtra("uid"));
                    startActivity(intent_home);
                    finish();
                    return true;

                case R.id.navigation_monitor:
                    //Do something
                    return true;

                case R.id.navigation_forum:
                    //Do something
                    Intent intent_forum = new Intent(InputActivity.this, ForumActivity.class);
                    intent_forum.putExtra("uid", getIntent().getStringExtra("uid"));
                    startActivity(intent_forum);
                    finish();
                    return true;

                case R.id.navigation_me:
                    //Do something
                    Intent intent_me = new Intent(InputActivity.this, MeActivity.class);
                    intent_me.putExtra("uid", getIntent().getStringExtra("uid"));
                    startActivity(intent_me);
                    finish();
                    return true;
            }
            return false;
        }
    };

}
