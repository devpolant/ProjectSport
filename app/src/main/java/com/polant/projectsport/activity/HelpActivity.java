package com.polant.projectsport.activity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.polant.projectsport.R;
import com.polant.projectsport.theme.ThemeSettings;
import com.polant.projectsport.adapter.tab.TabHelpFragmentAdapter;

/**
 * Активити помощи пользователю. Использует фрагмент HelpFragment.
 */
public class HelpActivity extends AppCompatActivity{

    private static final int LAYOUT = R.layout.activity_help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeSettings.setCurrentTheme(this, PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        initToolbar();
        initTabLayout();
    }

    private void initTabLayout() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

        TabHelpFragmentAdapter adapter = new TabHelpFragmentAdapter(
                this,
                getSupportFragmentManager()
        );
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.help_activity_title);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        //Кнопка назад на toolbar-е.
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

}
