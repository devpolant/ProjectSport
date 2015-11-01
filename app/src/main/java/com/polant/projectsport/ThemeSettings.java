package com.polant.projectsport;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;

import com.polant.projectsport.activity.ArticlesActivity;
import com.polant.projectsport.data.model.UserParametersInfo;
import com.polant.projectsport.preferences.PreferencesNewActivity;

/**
 * Created by ����� on 16.10.2015.
 */
public class ThemeSettings {


    //���������� ����� ���������� ����� ���� � ���������� ����������, ������ ����� ��� ������ ����.
    public static void setUpdatedTheme(Activity activity, SharedPreferences sp) {
        String theme = sp.getString(PreferencesNewActivity.PREF_APP_THEME, "Light");
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        switch (theme){
            case "Light":
                toolbar.setBackgroundColor(activity.getResources().getColor(R.color.ColorPrimary));
                break;
            case "Dark":
                toolbar.setBackgroundColor(activity.getResources().getColor(R.color.DarkColorPrimary));
                break;
        }
        setTabLayoutColor(activity, theme);
    }

    //���������� ���� �� ��������. ���������� � OnCreate() in Activity.
    public static void setCurrentTheme(Activity activity, SharedPreferences sp){
        String theme = sp.getString(PreferencesNewActivity.PREF_APP_THEME, "Light");
        switch (theme){
            case "Light":
                activity.setTheme(R.style.AppDefault);
                break;
            case "Dark":
                activity.setTheme(R.style.AppDark);
                break;
        }
    }

    private static void setTabLayoutColor(Activity activity, String theme){
        if (activity instanceof ArticlesActivity){
            TabLayout tabLayout = (TabLayout) activity.findViewById(R.id.tabLayout);
            switch (theme) {
                case "Light":
                    tabLayout.setBackgroundColor(activity.getResources().getColor(R.color.ColorPrimary));
                    break;
                case "Dark":
                    tabLayout.setBackgroundColor(activity.getResources().getColor(R.color.DarkColorPrimary));
                    break;
            }
        }
    }


    //�������� ��������� ������� � ������������.
    public static void setUserParametersInfo(UserParametersInfo user, SharedPreferences sp){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PreferencesNewActivity.PREF_USER_SEX, user.getSex());
        editor.putString(PreferencesNewActivity.PREF_USER_AGE, String.valueOf(user.getAge()));
        editor.putString(PreferencesNewActivity.PREF_USER_WEIGHT, String.valueOf(user.getWeight()));
        editor.putString(PreferencesNewActivity.PREF_USER_HEIGHT, String.valueOf(user.getHeight()));
        editor.apply();
    }
}
