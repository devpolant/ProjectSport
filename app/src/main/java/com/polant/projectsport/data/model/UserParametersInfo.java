package com.polant.projectsport.data.model;

import android.content.Context;

import com.polant.projectsport.R;

import java.util.Formatter;

/**
 * Created by Антон on 06.10.2015.
 */
public class UserParametersInfo {

    private float weight;
    private float height;
    private String sex;
    private String name;
    private int age;

    public UserParametersInfo() {
    }

    public UserParametersInfo(String name, int age, float weight, float height, String sex) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.sex = sex;
    }

    //Передаю контекст, чтобы получить доступ к строковым ресурсам.
    public int normalCaloriesCount(Context context){
        int k = 0;
        if (this.getSex().equals(context.getString(R.string.text_your_sex_M))){
            k = 5;
        }
        else{
            k = -161;
        }
        return  (int) (10 * weight + 6.25 * height + 5 * age + k);
    }

    //Считаю индекс массы тела.
    public static double calculateIndexBody(float w, float h){
        double result = w / Math.pow(h / 100, 2);
        try(Formatter formatter = new Formatter()) {
            formatter.format("%.2f", result);
            String resultStr = formatter.toString().replaceAll(",", ".");
            result = Double.valueOf(resultStr);
        }
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
