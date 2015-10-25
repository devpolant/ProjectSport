package com.polant.projectsport.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.polant.projectsport.R;
import com.polant.projectsport.data.model.Article;
import com.polant.projectsport.fragment.ExampleFragment;
import com.polant.projectsport.fragment.article.ArticleFragment;
import com.polant.projectsport.fragment.chart.ChartStatisticsFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Антон on 02.10.2015.
 */
public class TabsPagerFragmentAdapter extends FragmentStatePagerAdapter {

    private ArrayList<String> tabs;

    public TabsPagerFragmentAdapter(Activity activity, FragmentManager fm) {
        super(fm);
        //Получаю Активити в конструкторе только для получения доступа к ресурсам проекта.
        tabs = new ArrayList<String>();
        tabs.add(activity.getString(R.string.tab_sport_article));
        tabs.add(activity.getString(R.string.tab_food_article));
        tabs.add(activity.getString(R.string.tab_fitness_article));
    }

    @Override
    public Fragment getItem(int position) {
        return ArticleFragment.getInstance(tabs.get(position));
    }

    @Override
    public int getItemPosition(Object object) {
        ArticleFragment fragment = (ArticleFragment) object;
        String title = fragment.getArguments().getString(ArticleFragment.ARTICLE_CATEGORY);

        int position = tabs.indexOf(title);

        if (position >= 0) {
            return position;
        } else {
            return POSITION_NONE;
        }
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position);
    }
}
