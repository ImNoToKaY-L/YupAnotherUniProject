/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.drmarker.Recommend;

import io.realm.RealmObject;

/**
 *
 * @author 12242
 */
public class Sport extends RealmObject {
    
    private String name;
    private double calories;
    
    public Sport() {
        // Null constructor.
    }
    
    public Sport(String name, double calories) {
        this.name = name;
        this.calories = calories;
    }
    
    public String getName() {
        return this.name;
    }
    
    public double getCalories() {
        return this.calories;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setCalories(double calories) {
        this.calories = calories;
    }
    
    @Override
    public String toString() {
        return "Sport Name: " + this.name + "; Calories per kg: " + this.calories + ".";
    }
}
