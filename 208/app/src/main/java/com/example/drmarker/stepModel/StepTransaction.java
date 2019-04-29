package com.example.drmarker.stepModel;

import android.util.Log;

import java.util.Date;

import io.realm.Realm;


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

        if (stepModel == null)
            stepModel = realm.createObject(StepModel.class);
        stepModel.setDate(date);
        stepModel.setNumSteps(num);
        stepModel.setUid(uid);
    }
}
