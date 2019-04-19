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
public class Food extends RealmObject {
    private String name;
    private int calories;
    private String type;
    private boolean forBreakFast, forLunch, forDinner;
    
    public Food() {
        // Null Constructor.
    }
    
    public Food(String name, int calories, String type, boolean forBreakFast, boolean forLunch, boolean ForDinner) {
        this.name = name;
        this.calories = calories;
        this.type = type;
        this.forBreakFast = forBreakFast;
        this.forLunch = forLunch;
        this.forDinner = forDinner;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getCalories() {
        return this.calories;
    }
    
    public String getType() {
        return this.type;
    }
    
    public boolean suitOnBreakFast() {
        return this.forBreakFast;
    }
    
    public boolean suitOnLunch() {
        return this.forLunch;
    }
    
    public boolean suitOnDinner() {
        return this.forDinner;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setCalories(int calories) {
        this.calories = calories;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public void setForBreakFast(boolean forBreakFast) {
        this.forBreakFast = forBreakFast;
    }
    
    public void setForLunch(boolean forLunch) {
        this.forLunch = forLunch;
    }
    
    public void setForDinner(boolean forDinner) {
        this.forDinner = forDinner;
    }
    
    @Override
    public String toString() {
        return "Food Name: " + this.name + "; Calories per 100 grams: " + this.calories + ".";
    }
}
