package com.example.drmarker.stepModel;


import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Index;


public class StepModel extends RealmObject {
    @Index
    private Date date;
    private long numSteps;
    private String uid;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getNumSteps() {
        return numSteps;
    }

    public void setNumSteps(long numSteps) {
        this.numSteps = numSteps;
    }

    public String getUid(){
        return uid;
    }
    public void setUid(String uid){this.uid = uid;}
}
