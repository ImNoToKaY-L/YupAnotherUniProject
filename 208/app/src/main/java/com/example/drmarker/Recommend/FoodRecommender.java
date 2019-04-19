/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.drmarker.Recommend;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author 12242
 */
public class FoodRecommender {
    
    ArrayList<Food> foods = new ArrayList<>(); // arraylist for store the food.
    
    public FoodRecommender() {
        try {
            // read.
            FileInputStream fileInputStream = new FileInputStream("src/calories.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
			
            String str = new String();
            while((str = bufferedReader.readLine()) != null) {
                // each line: name, calories, type, breakfast, lunch, dinner.
                String[] foodInfo = str.split(",");
                String name = foodInfo[0];
                int calories = Integer.parseInt(foodInfo[1]);
                String type = foodInfo[2];
                boolean forBreakFast = Boolean.parseBoolean(foodInfo[3]);
                boolean forLunch = Boolean.parseBoolean(foodInfo[4]);
                boolean forDinner = Boolean.parseBoolean(foodInfo[5]);
                
                Food food = new Food(name, calories, type, forBreakFast, forLunch, forDinner);
                foods.add(food); // add into arraylist.
            }

            // close.
            fileInputStream.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.getStackTrace();
            Log.d("food", "FoodRecommender: IOException");
        } catch (NumberFormatException e) {
            e.getStackTrace();
            Log.d("food", "FoodRecommender: NumberFormatException");
        }
    }
    
}
