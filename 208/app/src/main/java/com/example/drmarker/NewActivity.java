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
import com.example.drmarker.Recommend.InitDBHandler;
import com.example.drmarker.Step.DateTimeHelper;
import com.example.drmarker.userModel.UserInformation;

import java.io.InputStream;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class NewActivity extends AppCompatActivity {
    private String gender;
    private double height,weight,waist,hip;
    private int age,activeType;
    private ArrayList<Food> foods,recommend;
    private String period;
    private Realm foodDB;
    private UserInformation userInfo;
    private double BMI,BMR,BF,dailyCal,SM;
    private String uid;
    String analysisOfBMI, analysisOfSM, analysisOfBF,totalAnalysis;
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        String TAG = "analise";
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
        toolbar.setTitle("");
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
        InputStream foodStream = getResources().openRawResource(R.raw.newfoods);
        InputStream sportStream = getResources().openRawResource(R.raw.sports);
        InitDBHandler init = new InitDBHandler(foodStream,sportStream);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        height = Double.parseDouble(userInfo.getHeight());
        weight = Double.parseDouble(userInfo.getWeight());
        gender = userInfo.getGender();
        age = 2019-userInfo.getYOB();
        activeType = userInfo.getCategory();
                Log.d(TAG, height+"HE");
        Log.d(TAG, weight+"WE");
        Log.d(TAG, gender);
//        Log.d(TAG, waist+"WA");
//        Log.d(TAG, hip+"HI");
        Log.d(TAG, age+"YOB");
        Log.d(TAG, activeType+"AT");
        String outputMessage;
        foodDB = Realm.getInstance(init.getFoodDB());
        RealmResults<Food> mFoods = foodDB.where(Food.class).findAll();
        foods = (ArrayList<Food>) foodDB.copyFromRealm(mFoods);

        BMR = FoodRecommender.standardBMR(gender,weight,height,age);
        Log.d(TAG, BMR+"BMR");
        TextView bmrView = findViewById(R.id.tv_new_BMR);
        bmrView.setText(BMR+"");
        dailyCal = FoodRecommender.dailyCalories(activeType,BMR);
        Log.d(TAG, dailyCal+"daC");

        BMI = FoodRecommender.BMI(weight,height);
        Log.d(TAG, BMI+"BMI");
        TextView bmiView = findViewById(R.id.tv_new_BMI);
        bmiView.setText(BMI+"");
        TextView commonTV;
        analysisOfBMI = FoodRecommender.analysisBMI(BMI);
        outputMessage = "According to your BMI level, your body is "+analysisOfBMI+" now";
        commonTV = findViewById(R.id.tv_result1);
        commonTV.setText(outputMessage);

        Log.d(TAG, analysisOfBMI+"analysisOfBMI");


        BF = FoodRecommender.bodyFat(BMI,age,gender);
        Log.d(TAG, BF+"BF");
        analysisOfBF = FoodRecommender.analysisBF(BF,gender,age);
        Log.d(TAG, analysisOfBF+"aBF");
        outputMessage = "And according to your body fat level, your might be suffering from "
                +analysisOfBF+ " at this moment";
        commonTV = findViewById(R.id.tv_result2);
        commonTV.setText(outputMessage);

//        skeletal muscle
        commonTV = findViewById(R.id.tv_result3);
        if (userInfo.getWaist().length()!=0&&userInfo.getHip().length()!=0){
            waist = Double.parseDouble(userInfo.getWaist());
            hip = Double.parseDouble(userInfo.getHip());

            SM = FoodRecommender.SM(gender,weight,height,waist,hip,age);
            Log.d(TAG, SM+"SM");
            analysisOfSM = FoodRecommender.analysisSM(SM,gender,age);
            outputMessage = "Additionally, according to your data, you have a "
            +analysisOfSM+" level of Skeletal Muscle";
            commonTV.setText(outputMessage);
            Log.d(TAG, analysisOfSM+"ASM");

        }else {
            outputMessage = "To know more about your Skeletal muscle information, please measure your"+
                    "waist and hip and edit them in the previous monitor page";
            analysisOfSM = "";
            commonTV.setText(outputMessage);
        }


        totalAnalysis = FoodRecommender.totalAnalysis(analysisOfBMI,analysisOfBF,analysisOfSM);
        Log.d(TAG, totalAnalysis+"tAna");
        commonTV = findViewById(R.id.tv_result4);
        outputMessage = "In total, your exercising goal is "+totalAnalysis;
        commonTV.setText(outputMessage);

        Log.d(TAG, DateTimeHelper.getHour()+"time");
        int timeNow = DateTimeHelper.getHour();
        if (timeNow>=8&&timeNow<=11) {
            recommend = FoodRecommender.getRecommendLunch(totalAnalysis,foods);
            period = "For your next Lunch, the recommended foods are:";
        }
        else if (timeNow>11&&timeNow<=16) {
            recommend = FoodRecommender.getRecommendDinner(totalAnalysis,foods);
            period = "For your next Dinner, the recommended foods are:";
        }
        else  {
            recommend =FoodRecommender.getRecommendBreakfast(totalAnalysis,foods);
            period = "For your next Breakfast, the recommended foods are:";
            }
        TextView foodRecommend = findViewById(R.id.tv_new_food_recommend);
        foodRecommend.setText(period);
        initFoodRecommend();



    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NewActivity.this,InputActivity.class);
        intent.putExtra("uid",getIntent().getStringExtra("uid"));
        startActivity(intent);
        finish();
    }

    private void initUserInfo(){
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("userInfo_db").modules(new UserInfoModule()).deleteRealmIfMigrationNeeded()
                .build();
        Realm userInfoDB = Realm.getInstance(realmConfig);
        userInfo = userInfoDB.where(UserInformation.class).equalTo("uid",uid).findFirst();
    }
    private void initFoodRecommend(){
        TextView tv1 = findViewById(R.id.food1_name);
        tv1.setText(recommend.get(0).getReadableName());
        ImageView ig1 = findViewById(R.id.image1);
        int image1ID = getResources().getIdentifier(recommend.get(0).getName(),"raw",getPackageName());
        ig1.setImageResource(image1ID);
        tv1 = findViewById(R.id.food1_description);
        tv1.setText(recommend.get(0).toString());

        TextView tv2 = findViewById(R.id.food2_name);
        tv2.setText(recommend.get(1).getReadableName());
        ImageView ig2 = findViewById(R.id.image2);
        int image2ID = getResources().getIdentifier(recommend.get(1).getName(),"raw",getPackageName());
        ig2.setImageResource(image2ID);
        tv2 = findViewById(R.id.food2_description);
        tv2.setText(recommend.get(1).toString());


        TextView tv3 = findViewById(R.id.food3_name);
        ImageView ig3 = findViewById(R.id.image3);
        if (recommend.size()==3){
            tv3.setText(recommend.get(2).getReadableName());
            int image3ID = getResources().getIdentifier(recommend.get(2).getName(),"raw",getPackageName());
            ig3.setImageResource(image3ID);
            tv3 = findViewById(R.id.food3_description);
            tv3.setText(recommend.get(2).toString());
        }else {
            tv3.setText("");
            ig3.setScaleX(0);
            ig3.setScaleY(0);
        }
    }


}

