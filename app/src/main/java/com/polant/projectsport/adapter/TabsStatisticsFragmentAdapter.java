package com.polant.projectsport.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.polant.projectsport.R;
import com.polant.projectsport.fragment.chart.ChartStatisticsFragment;

/**
 * Created by Антон on 25.10.2015.
 */
public class TabsStatisticsFragmentAdapter extends FragmentStatePagerAdapter {

    private String[] tabs;

    public TabsStatisticsFragmentAdapter(Activity activity, FragmentManager fm) {
        super(fm);
        //Получаю Активити в конструкторе только для получения доступа к ресурсам проекта.
        tabs = new String[] {
                activity.getString(R.string.tab_statistics_week),
                activity.getString(R.string.tab_statistics_month)
        };
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return ChartStatisticsFragment.getInstance(ChartStatisticsFragment.STATISTICS_WEEK);
            case 1:
                return ChartStatisticsFragment.getInstance(ChartStatisticsFragment.STATISTICS_MONTH);
            default:
                return null;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        ChartStatisticsFragment fragment = (ChartStatisticsFragment) object;
        int interval = fragment.getArguments().getInt(ChartStatisticsFragment.STATISTICS_INTERVAL);

        int position = -1;
        switch (interval){
            case ChartStatisticsFragment.STATISTICS_WEEK:
                position = 0;
                break;
            case ChartStatisticsFragment.STATISTICS_MONTH:
                position = 1;
                break;
        }

        if (position >= 0) {
            return position;
        } else {
            return POSITION_NONE;
        }
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
