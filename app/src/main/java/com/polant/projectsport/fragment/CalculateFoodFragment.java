package com.polant.projectsport.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.polant.projectsport.MainActivity;
import com.polant.projectsport.R;

/**
 * Created by Антон on 04.10.2015.
 */
public class CalculateFoodFragment extends Fragment {

    private static final int LAYOUT = R.layout.fragment_calories_counter;

    private View view;

    private MainActivity mainActivity;
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        //ссылку на toolbar получаю здесь (и передаю методам активити
        //для доп. инициализации), а на NavigationView в Активити.
        mainActivity.initToolbar(toolbar);
        mainActivity.initNavigationView(toolbar);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mainActivity = (MainActivity) activity;
    }

}
