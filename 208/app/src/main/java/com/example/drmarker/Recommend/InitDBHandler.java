package com.example.drmarker.Recommend;

import android.util.Log;

import com.example.drmarker.RealmModule.FoodModule;
import com.example.drmarker.RealmModule.SportModule;

import java.io.InputStream;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class InitDBHandler {
    private FoodRecommender food;
    private SportRecommender sport;
    private RealmConfiguration foodDB;
    private RealmConfiguration sportDB;

    private void init(InputStream foodStream,InputStream sportStream){
        foodDB =  new RealmConfiguration.Builder()
                .name("food_db").modules(new FoodModule())
                .build();
        sportDB =  new RealmConfiguration.Builder()
                .name("sport_db").schemaVersion(2).modules(new SportModule())
                .build();
        food = new FoodRecommender(foodStream);
        sport = new SportRecommender(sportStream);

        Realm foodRealm = Realm.getInstance(foodDB);
        Food firstFoodObject = foodRealm.where(Food.class).findFirst();
        if (firstFoodObject==null){
            Log.d("sport", "init: db is null");
            for (Food f:food.getFoods()){
                final Food food = f;
                foodRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealm(food);
                    }
                });
            }
        }
        Realm sportRealm = Realm.getInstance(sportDB);
        Sport firstSportObject = sportRealm.where(Sport.class).findFirst();
        if (firstSportObject==null){
            Log.d("sport", "init: db is null");
            for (Sport s:sport.getSports()){
                final Sport sport = s;
                sportRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealm(sport);
                    }
                });
            }
        }
    }

    public InitDBHandler(InputStream foodStream,InputStream sportStream){
        init(foodStream,sportStream);
    }

    public RealmConfiguration getFoodDB() {
        return foodDB;
    }

    public RealmConfiguration getSportDB() {
        return sportDB;
    }
}
