package com.polant.projectsport.theme;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;

import com.polant.projectsport.R;
import com.polant.projectsport.activity.ArticlesActivity;
import com.polant.projectsport.model.UserParametersInfo;
import com.polant.projectsport.preferences.PreferencesNewActivity;

/**
 * Created by Антон on 16.10.2015.
 */
public class ThemeSettings {


    //Вызывается после применения новой темы в настройках приложения, просто меняя фон старой темы.
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

    //Применение темы из настроек. Вызывается в OnCreate() in Activity.
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


    //Заполняю ностройки данными о пользователе.
    public static void setUserParametersInfo(UserParametersInfo user, SharedPreferences sp){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PreferencesNewActivity.PREF_USER_SEX, user.getSex());
        editor.putString(PreferencesNewActivity.PREF_USER_AGE, String.valueOf(user.getAge()));
        editor.putString(PreferencesNewActivity.PREF_USER_WEIGHT, String.valueOf(user.getWeight()));
        editor.putString(PreferencesNewActivity.PREF_USER_HEIGHT, String.valueOf(user.getHeight()));
        editor.apply();
    }
}
