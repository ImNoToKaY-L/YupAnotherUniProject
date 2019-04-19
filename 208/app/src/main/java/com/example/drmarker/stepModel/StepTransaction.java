package com.example.drmarker.stepModel;

import android.util.Log;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * Created by gojuukaze on 16/8/19.
 * Email: i@ikaze.uu.me
 */
public class StepTransaction implements Realm.Transaction {

    private Date date;
    private long num;
    private String uid;

    public StepTransaction(Date date, long num,String uid) {
        this.date = date;
        this.num = num;
        this.uid = uid;
    }

    @Override
    public void execute(Realm realm) {
        Log.d("realm", "now insert [" + date + " ," + num + uid+"]");

        StepModel stepModel =realm.where(StepModel.class).equalTo("date",date).equalTo("uid",uid).findFirst();
        RealmResults<StepModel> people = realm.where(StepModel.class).findAll();
        for (StepModel s:people){
            Log.d("DEBUG", s.getUid()+" "+s.getNumSteps()+" "+s.getDate());
        }

        if (stepModel == null)
            stepModel = realm.createObject(StepModel.class);
        stepModel.setDate(date);
        stepModel.setNumSteps(num);
        stepModel.setUid(uid);
    }
}
