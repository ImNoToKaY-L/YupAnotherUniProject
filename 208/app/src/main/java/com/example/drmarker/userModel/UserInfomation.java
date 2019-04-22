package com.example.drmarker.userModel;

import io.realm.RealmObject;

public class UserInfomation extends RealmObject {
    private String uid;
    private int height,weight,hip,waist;
    private String category;

    public int getHeight() {
        return height;
    }

    public int getHip() {
        return hip;
    }

    public int getWaist() {
        return waist;
    }

    public int getWeight() {
        return weight;
    }

    public String getUid() {
        return uid;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setHip(int hip) {
        this.hip = hip;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setWaist(int waist) {
        this.waist = waist;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
