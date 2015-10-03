package com.polant.projectsport;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.polant.projectsport.adapter.TabsPagerFragmentAdapter;
import com.polant.projectsport.preferences.PreferencesNewActivity;
import com.polant.projectsport.preferences.PreferencesOldActivity;

/**
 * Created by Антон on 02.10.2015.
 */
public class MainActivity extends AppCompatActivity {

    public static final int LAYOUT = R.layout.activity_main;
    public static final int SHOW_PREFERENCES = 1;

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.AppDefault);
        setCurrentTheme(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        initToolbar();
        initNavigationView();
        initTabLayout();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });

        toolbar.inflateMenu(R.menu.menu);
    }

    private void initTabLayout() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        TabsPagerFragmentAdapter adapter = new TabsPagerFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_view_open,
                R.string.navigation_view_close);

        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                drawerLayout.closeDrawers();

                switch (item.getItemId()) {
                    case R.id.actionArticleItem:
                        showNotificationTab();
                        break;
                    case R.id.settingsItem:
                        //добавим совместимость со старыми версиями платформы.
                        Class c = Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ?
                                PreferencesOldActivity.class : PreferencesNewActivity.class;

                        Intent intent = new Intent(MainActivity.this, c);
                        Log.d("Class in intent", c.getName());
                        startActivityForResult(intent, SHOW_PREFERENCES);
                        break;
                }

                return true;
            }
        });
    }


    //Открытие таба "Статьи" из NavigationView.
    private void showNotificationTab() {
        viewPager.setCurrentItem(Constants.TAB_TWO);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SHOW_PREFERENCES){
            updateFromPreferences();
        }

    }

    //Применение настроек приложения.
    private void updateFromPreferences(){
        //TODO: сделать обработчик применения выбранных настроек.

        //Применяю тему.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        setUpdatedTheme(sp);
    }

    private void setUpdatedTheme(SharedPreferences sp) {
        String theme = sp.getString(PreferencesNewActivity.PREF_APP_THEME, "Light");
        switch (theme){
            case "Light":
                toolbar.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));
                tabLayout.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));
                break;
            case "Dark":
                toolbar.setBackgroundColor(getResources().getColor(R.color.DarkColorPrimary));
                tabLayout.setBackgroundColor(getResources().getColor(R.color.DarkColorPrimary));
                break;
        }
    }

    //Применение темы из настроек. Вызывается в OnCreate().
    private void setCurrentTheme(SharedPreferences sp){
        String theme = sp.getString(PreferencesNewActivity.PREF_APP_THEME, "Light");
        switch (theme){
            case "Light":
                setTheme(R.style.AppDefault);
                break;
            case "Dark":
                setTheme(R.style.AppDark);
                break;
        }
    }
}
