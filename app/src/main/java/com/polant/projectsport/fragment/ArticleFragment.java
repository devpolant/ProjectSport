package com.polant.projectsport.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.polant.projectsport.IToolbarFragment;
import com.polant.projectsport.MainActivity;
import com.polant.projectsport.R;
import com.polant.projectsport.adapter.TabsPagerFragmentAdapter;

/**
 * Created by Антон on 04.10.2015.
 */
public class ArticleFragment extends Fragment implements IToolbarFragment{

    private static final int LAYOUT = R.layout.fragment_article;

    private View view;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private MainActivity mainActivity;

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
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        //ссылки на toolbar, tabLayout и viewPager получаю здесь (и передаю их методам активити
        //для доп. инициализации), а на NavigationView в Активити.
        mainActivity.initToolbar(toolbar);
        mainActivity.initNavigationView(toolbar);
        mainActivity.initTabLayout(viewPager, tabLayout);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mainActivity = (MainActivity) activity;
    }

    //----------------------------------//

    public View getToolbar(){
        return view.findViewById(R.id.toolbar);
    }

    public View getViewPager(){
        return view.findViewById(R.id.viewPager);
    }

    public View getTabLayout(){
        return view.findViewById(R.id.tabLayout);
    }
}
