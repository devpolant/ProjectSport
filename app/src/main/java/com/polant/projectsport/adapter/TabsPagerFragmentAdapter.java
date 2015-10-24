package com.polant.projectsport.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.polant.projectsport.R;
import com.polant.projectsport.data.model.Article;
import com.polant.projectsport.fragment.ExampleFragment;
import com.polant.projectsport.fragment.article.ArticleFragment;

/**
 * Created by Антон on 02.10.2015.
 */
public class TabsPagerFragmentAdapter extends FragmentPagerAdapter {

    private String[] tabs;

    public TabsPagerFragmentAdapter(Activity activity, FragmentManager fm) {
        super(fm);
        //Получаю Активити в конструкторе только для получения доступа к ресурсам проекта.
        tabs = new String[] {
                activity.getString(R.string.tab_sport_article),
                activity.getString(R.string.tab_food_article),
                activity.getString(R.string.tab_fitness_article)
        };
    }

    @Override
    public Fragment getItem(int position) {
        return ArticleFragment.getInstance(tabs[position]);
    }

    @Override
    public int getCount() {
        return tabs.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
}
