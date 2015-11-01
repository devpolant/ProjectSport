package com.polant.projectsport.data.model;

/**
 * Created by Антон on 24.10.2015.
 */
public class Article {

    private String title;
    private String category;
    private String text;
    private String date;

    public Article() {
    }

    public Article(String title, String category, String text, String date) {
        this.title = title;
        this.category = category;
        this.text = text;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public Article setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public Article setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getText() {
        return text;
    }

    public Article setText(String text) {
        this.text = text;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Article setDate(String date) {
        this.date = date;
        return this;
    }
}
