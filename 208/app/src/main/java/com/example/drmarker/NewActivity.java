package com.example.drmarker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.drmarker.RealmModule.FoodModule;
import com.example.drmarker.RealmModule.UserInfoModule;
import com.example.drmarker.Recommend.Food;
import com.example.drmarker.Recommend.FoodRecommender;
import com.example.drmarker.userModel.UserInformation;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class NewActivity extends AppCompatActivity {
    private String gender;
    private double height,weight,waist,hip;
    private int age,activeType;
    private ArrayList<Food> foods;
    private Realm foodDB;
    private UserInformation userInfo;
    private double BMI,BMR,BF,dailyCal,SM;
    private String uid;
    String analysisOfBMI, analysisOfSM, analysisOfBF,totalAnalysis;
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        String TAG = "ANAL";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        //Get the intent from the input activity
        Intent intent=getIntent();
        uid = intent.getStringExtra("uid");
        initUserInfo();
//        height = Double.parseDouble(intent.getStringExtra("height")) ;
//        weight = Double.parseDouble(intent.getStringExtra("weight"));
//        gender = intent.getStringExtra("gender");
//        waist = Double.parseDouble(intent.getStringExtra("waist"));
//        hip = Double.parseDouble(intent.getStringExtra("hip"));
//        age = 2019-Integer.parseInt(intent.getStringExtra("yob"));
//        activeType = intent.getIntExtra("activeType",0);

        toolbar = (Toolbar) findViewById(R.id.toolbar_new);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewActivity.this,InputActivity.class);
                intent.putExtra("uid",getIntent().getStringExtra("uid"));
                startActivity(intent);
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        height = Double.parseDouble(userInfo.getHeight());
        weight = Double.parseDouble(userInfo.getWeight());
        gender = userInfo.getGender();
        waist = Double.parseDouble(userInfo.getWaist());
        hip = Double.parseDouble(userInfo.getHip());
        age = 2019-userInfo.getYOB();
        activeType = userInfo.getCategory();
                Log.d(TAG, height+"HE");
        Log.d(TAG, weight+"WE");
        Log.d(TAG, gender);
        Log.d(TAG, waist+"WA");
        Log.d(TAG, hip+"HI");
        Log.d(TAG, age+"YOB");
        Log.d(TAG, activeType+"AT");

        foodDB = Realm.getInstance(new RealmConfiguration.Builder()
                .name("food_db").modules(new FoodModule()).deleteRealmIfMigrationNeeded()
                .build());
        RealmResults<Food> mFoods = foodDB.where(Food.class).findAll();
        foods = (ArrayList<Food>) foodDB.copyFromRealm(mFoods);

        BMR = FoodRecommender.standardBMR(gender,weight,height,age);
        Log.d(TAG, BMR+"BMR");
        dailyCal = FoodRecommender.dailyCalories(activeType,BMR);
        Log.d(TAG, dailyCal+"daC");


        BMI = FoodRecommender.BMI(weight,height);
        Log.d(TAG, BMI+"BMI");

        analysisOfBMI = FoodRecommender.analysisBMI(BMI);
        Log.d(TAG, analysisOfBMI+"analysisOfBMI");


        BF = FoodRecommender.bodyFat(BMI,age,gender);
        Log.d(TAG, BF+"BF");
        analysisOfBF = FoodRecommender.analysisBF(BF,gender,age);
        Log.d(TAG, analysisOfBF+"aBF");

        SM = FoodRecommender.SM(gender,weight,height,waist,hip,age);
        Log.d(TAG, SM+"SM");
        analysisOfSM = FoodRecommender.analysisSM(SM,gender,age);
        Log.d(TAG, analysisOfSM+"ASM");

        totalAnalysis = FoodRecommender.totalAnalysis(analysisOfBMI,analysisOfBF,analysisOfSM);
        Log.d(TAG, totalAnalysis+"tAna");


        ArrayList<Food> breakfast = FoodRecommender.getRecommendBreakfast(totalAnalysis,foods);
        ArrayList<Food> lunch = FoodRecommender.getRecommendLunch(totalAnalysis,foods);
        ArrayList<Food> dinner = FoodRecommender.getRecommendDinner(totalAnalysis,foods);
        TextView tv = findViewById(R.id.image_comment1);
        tv.setText(breakfast.get(0).getName());
        ImageView ig = findViewById(R.id.image1);
        




    }

    private void initUserInfo(){
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("userInfo_db").modules(new UserInfoModule()).deleteRealmIfMigrationNeeded()
                .build();
        Realm userInfoDB = Realm.getInstance(realmConfig);
        userInfo = userInfoDB.where(UserInformation.class).equalTo("uid",uid).findFirst();
    }


}

