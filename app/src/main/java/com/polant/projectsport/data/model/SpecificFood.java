package com.polant.projectsport.data.model;

/**
 * Created by Антон on 05.10.2015.
 */
public class SpecificFood {

    private int idSpecificFood;
    private String foodCategory;
    private String foodName;
    private int caloriesCount;


    public SpecificFood(int idSpecificFood, String foodCategory, String foodName, int caloriesCount) {
        this.idSpecificFood = idSpecificFood;
        this.foodCategory = foodCategory;
        this.foodName = foodName;
        this.caloriesCount = caloriesCount;
    }

    public int getIdSpecificFood() {
        return idSpecificFood;
    }

    public void setIdSpecificFood(int idSpecificFood) {
        this.idSpecificFood = idSpecificFood;
    }

    public String getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(String foodCategory) {
        this.foodCategory = foodCategory;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getCaloriesCount() {
        return caloriesCount;
    }

    public void setCaloriesCount(int caloriesCount) {
        this.caloriesCount = caloriesCount;
    }
}
