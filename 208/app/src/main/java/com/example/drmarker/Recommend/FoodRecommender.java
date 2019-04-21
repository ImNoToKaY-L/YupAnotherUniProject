/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.drmarker.Recommend;

import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author 12242
 */
public class FoodRecommender {
    
    private ArrayList<Food> foods = new ArrayList<>(); // arraylist for store the food.
    
    public FoodRecommender(InputStream inputStream) {
        try {
            // read.
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			
            String str = new String();
            while((str = bufferedReader.readLine()) != null) {
                // each line: name, calories, type, breakfast, lunch, dinner.
                String[] foodInfo = str.split(",");
                String name = foodInfo[0];
                int unit = Integer.parseInt(foodInfo[1]);
                double calories = Double.parseDouble(foodInfo[2]);
                double protein = Double.parseDouble(foodInfo[3]);
                double carbohydrate = Double.parseDouble(foodInfo[4]);
                double fat = Double.parseDouble(foodInfo[5]);
                String type = foodInfo[6];
                boolean forBreakfast = Boolean.parseBoolean(foodInfo[7]);
                
                Food food = new Food(name, unit, calories, protein, carbohydrate, fat, type, forBreakfast);
                foods.add(food); // add into arraylist.
            }

            // close.
            inputStream.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.getStackTrace();
            System.out.println("Error for checking file's exist.");
        } catch (NumberFormatException e) {
            e.getStackTrace();
            System.out.println("Error for raw data format.");
        }
    }
    
    public ArrayList<Food> getFoods() {
        return foods;
    }
    
    public double standardBMR(String gender, double weight, double height, int age) {
        if (gender.equalsIgnoreCase("male")) {
            return 13.8 * weight + 5 * height - 6.8 * age + 66;
        } else {
            return 9.6 * weight + 1.8 * height -4.7 * age + 655;
        }
    }
    
    public double BMR(String gender, double waist, double neck, double hip, double height, double weight) {
        return 370 + 21.6 * (weight - bodyFat(gender, waist, neck, hip, height));
    }
    
    public double SM(String gender, double weight, double height, double waist, double hip, int age) {
        if (gender.equalsIgnoreCase("male")) {
            return 39.5 + 0.665 * weight - 0.185 * waist - 0.418 * hip - 0.08 * age;
        } else {
            return 2.89 + 0.255 * weight - 0.175 * hip - 0.038 * age + 0.118 * height;
        }
    }
    
    public double BMI(double weight, double height) {
        return weight / (height * height);
    }
    
    public double bodyFat(String gender, double waist, double neck, double hip, double height) {
        if (gender.equalsIgnoreCase("male")) {
            return 495.0 / (1.0324 - 0.19077 * (waist - neck)) + 0.15456 * height - 450;
        } else {
            return 495.0 / (1.29579 - 0.35004 * (waist + hip - neck)) + 0.22100 * height - 450;
        }
    }
    
}
