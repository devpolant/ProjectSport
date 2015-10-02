package com.polant.projectsport.preferences;

import android.preference.PreferenceActivity;

import com.polant.projectsport.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Антон on 02.10.2015.
 */
public class PreferencesNewActivity extends PreferenceActivity {

    public static final String APP_THEME = "APP_THEME";

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
