package com.polant.projectsport.data.model;

/**
 * Created by Антон on 06.10.2015.
 */
public class UserParametersInfo {

    private float weight;
    private float height;
    private String sex;
    private String name;

    public UserParametersInfo(String name, float weight, float height, String sex) {
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
