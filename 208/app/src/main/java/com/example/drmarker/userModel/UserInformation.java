package com.example.drmarker.userModel;

import javax.annotation.Nullable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserInformation extends RealmObject {
    @PrimaryKey
    private String uid;
    private String height,weight,hip,waist;
    private int category;
    private String gender;
    private int YOB;

    public UserInformation(String uid,String height,String weight,String waist,String hip,String gender,int category,int YOB){
        this.uid = uid;
        this.height = height;
        this.waist = waist;
        this.weight = weight;
        this.category = category;
        this.YOB = YOB;
        this.setGender(gender);
        this.setHip(hip);
    }
    public UserInformation(){

    }

    public int getYOB() {
        return YOB;
    }

    public void setYOB(int YOB) {
        this.YOB = YOB;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHeight() {
        return height;
    }

    public String getHip() {
        return hip;
    }

    public String getWaist() {
        return waist;
    }

    public String getWeight() {
        return weight;
    }

    public String getUid() {
        return uid;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setHip(String hip) {
        this.hip = hip;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setWaist(String waist) {
        this.waist = waist;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
