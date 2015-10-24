package com.polant.projectsport.preferences;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.polant.projectsport.R;

/**
 * Created by Антон on 24.10.2015.
 */
public class UserParametersInfoFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.user_parameters_info);
    }

}
