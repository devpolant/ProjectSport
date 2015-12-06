package com.polant.projectsport.preferences;

import android.preference.PreferenceActivity;

import com.polant.projectsport.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Антон on 02.10.2015.
 */
public class PreferencesNewActivity extends PreferenceActivity {

    public static final int SHOW_PREFERENCES = 1;

    public static final String PREF_APP_THEME = "PREF_APP_THEME";
    //public static final String PREF_AUTO_UPDATE = "PREF_AUTO_UPDATE";
    public static final String PREF_USER_NAME = "PREF_USER_NAME";
    public static final String PREF_USER_SEX = "PREF_USER_SEX";
    public static final String PREF_USER_WEIGHT = "PREF_USER_WEIGHT";
    public static final String PREF_USER_HEIGHT = "PREF_USER_HEIGHT";
    public static final String PREF_USER_AGE = "PREF_USER_AGE";

    public static final String PREF_TARGET_STEP_COUNT = "PREF_TARGET_STEP_COUNT";
    public static final String PREF_CURRENT_STEP_COUNT = "PREF_CURRENT_STEP_COUNT";
    public static final String PREF_RESET_STEP_COUNT = "PREF_RESET_STEP_COUNT";

    public static final String PREF_IS_FIRST_CALL = "PREF_IS_FIRST_CALL";


    private ArrayList<String> fragmentsNames = new ArrayList<>();

    //нужно просто переопределить данный метод.
    @Override
    public void onBuildHeaders(List<Header> target) {

        loadHeadersFromResource(R.xml.preferences_headers, target);

        fragmentsNames.clear();
        for (PreferenceActivity.Header header: target)
            fragmentsNames.add(header.fragment);

    }

    //и также переопределить данный метод.
    @Override
    protected boolean isValidFragment(String fragmentName) {
        return fragmentName.contains(fragmentName);
    }

}
