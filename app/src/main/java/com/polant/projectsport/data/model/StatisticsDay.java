package com.polant.projectsport.data.model;

/**
 * Created by Антон on 25.10.2015.
 */
public class StatisticsDay {
    
    private int day;
    private int month;
    private int year;
    private int delta;

    public StatisticsDay() {
    }

    public StatisticsDay(int day, int month, int year, int delta) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.delta = delta;
    }

    public int getDay() {
        return day;
    }

    public StatisticsDay setDay(int day) {
        this.day = day;
        return this;
    }

    public int getMonth() {
        return month;
    }

    public StatisticsDay setMonth(int month) {
        this.month = month;
        return this;
    }

    public int getYear() {
        return year;
    }

    public StatisticsDay setYear(int year) {
        this.year = year;
        return this;
    }

    public int getDelta() {
        return delta;
    }

    public StatisticsDay setDelta(int delta) {
        this.delta = delta;
        return this;
    }
}
