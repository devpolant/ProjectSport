package com.polant.projectsport.adapter.tab;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.polant.projectsport.R;
import com.polant.projectsport.fragment.help.HelpFragment;

/**
 * Created by Антон on 19.11.2015.
 */
public class TabHelpFragmentAdapter extends FragmentStatePagerAdapter {

    private String[] tabs;

    public TabHelpFragmentAdapter(Activity activity, FragmentManager fm) {
        super(fm);
        //Получаю Активити в конструкторе только для получения доступа к ресурсам проекта.
        tabs = new String[] {
                activity.getString(R.string.tab_help_description),
                activity.getString(R.string.tab_help_procedure)
        };
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return HelpFragment.getInstance(HelpFragment.HELP_DESCRIPTION);
            case 1:
                return HelpFragment.getInstance(HelpFragment.HELP_PROCEDURE);
            default:
                return null;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        HelpFragment fragment = (HelpFragment) object;
        String helpType = fragment.getArguments().getString(HelpFragment.HELP_TYPE);

        int position = -1;
        switch (helpType){
            case HelpFragment.HELP_DESCRIPTION:
                position = 0;
                break;
            case HelpFragment.HELP_PROCEDURE:
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
