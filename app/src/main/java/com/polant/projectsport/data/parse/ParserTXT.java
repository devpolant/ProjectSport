package com.polant.projectsport.data.parse;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.polant.projectsport.data.database.Database;
import com.polant.projectsport.model.Article;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by ����� on 05.10.2015.
 */
public class ParserTXT {

    //��������� ��� ���������� ������ � ��.
    Database DB;

    //������� ������ �� ����, � �� ������ �� �� �������� Database, �.�. ���������
    //���������� ��� ��� ������� ������� �����.
    SQLiteDatabase database;

    public ParserTXT(Database db) {
        DB = db;
        database = db.getSqLiteDatabase();
    }


    public void parseArticle(InputStream inputStream){
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try{
            String s = reader.readLine();
            int allCountIndex = s.lastIndexOf(" ");
            s = s.substring(allCountIndex + 1);
            int count = Integer.valueOf(s);

            String category = "";
            String date = "";
            String articleTitle = "";
            StringBuilder articleText = new StringBuilder();

            //��������� ����������.
            String temp = "";
            String endCategoryKey = "=====";
            String endArticleKey = "-----";
            String nameArticleKey = "--";
            String nameCategoryKey = "==";

            for (int i = 0; i < count; i++) {
                //������� �� 2 �������, �.�. ������ ��������� ������ ���������� � "==".
                category = reader.readLine();
                if (category.startsWith(nameCategoryKey))
                    category = category.substring(2);

                while (true){
                    temp = reader.readLine();
                    if (temp.equals(endCategoryKey))
                        break;
                    if (temp.startsWith(nameArticleKey))
                        articleTitle = temp.substring(2);

                    //����� �������� ���� ���������� ������.
                    date = reader.readLine();

                    //����� ������ ������� ������ StringBuilder.
                    articleText.delete(0, articleText.length());

                    while (true){
                        //�������� ������ ������ ������.
                        temp = reader.readLine();

                        if (temp.equals(endArticleKey)) {
                            putArticle(articleTitle, category, articleText.toString(), date);
                            break;
                        }

                        //��� �������� ������ ������ ������.
                        articleText.append(temp);
                    }
                }

            }
        }
        catch (IOException ex){
            Log.d("ERROR_IN_PARSER_TXT_ART", ex.getMessage());
            ex.printStackTrace();
        }
    }

    //���������� ������ � ����.
    private void putArticle(String articleTitle, String category, String articleText, String date) {
        Article article = new Article(articleTitle, category, articleText, date);
        DB.addArticle(article);
    }


    public void parseFood(InputStream inputStream){
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String s = reader.readLine();
            int allCountIndex = s.lastIndexOf(" ");
            s = s.substring(allCountIndex + 1);
            int count = Integer.valueOf(s);
            String check = "";
            String category = "";
            for (int i = 0; i < count; i++) {

                category = reader.readLine();//��� ��� ����.
                ContentValues categoryValues = new ContentValues();
                categoryValues.put(Database.FOOD_CATEGORY, category);
                database.insert(Database.TABLE_FOOD, null, categoryValues);

                Cursor c = database.query(Database.TABLE_FOOD, new String[]{Database.ID_FOOD, Database.FOOD_CATEGORY},
                        Database.FOOD_CATEGORY + "=?", new String[]{ category }, null, null, null);
                c.moveToFirst();
                //int id = c.getInt(c.getColumnIndex(Database.FOOD_CATEGORY));
                int id = c.getInt(c.getColumnIndex(Database.ID_FOOD));
                c.close();

                while(true) {
                    check = reader.readLine();
                    if (check.equals("-----")){
                        break;
                    }

                    int index = check.lastIndexOf(" ");
                    String name = check.substring(0, index);
                    int calories = Integer.valueOf(check.substring(index + 1));

                    ContentValues cv = new ContentValues();
                    cv.put(Database.FOOD_NAME, name);
                    cv.put(Database.ID_FOOD, id);
                    cv.put(Database.CAL_COUNT, calories);

                    database.insert(Database.TABLE_SPECIFIC_FOOD, null, cv);
                }
            }
            try {
                reader.close();
                //inputStream.close();
            }catch (Exception e){
                Log.d("PARSER_TXT_METHOD_CLOSE", e.getMessage());
                e.printStackTrace();
            }
        } catch (IOException e) {
            Log.d("ERROR_IN_PARSER_TXT", e.getMessage());
            e.printStackTrace();
        }
    }

}
