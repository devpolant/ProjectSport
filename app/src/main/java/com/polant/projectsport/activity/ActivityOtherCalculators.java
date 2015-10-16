package com.polant.projectsport.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.polant.projectsport.R;
import com.polant.projectsport.ThemeSettings;
import com.polant.projectsport.preferences.PreferencesNewActivity;
import com.polant.projectsport.preferences.PreferencesOldActivity;

public class ActivityOtherCalculators extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_calculators;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeSettings.setCurrentTheme(this, PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(LAYOUT);

        initToolbar();
        initNavigationView();
        //TODO : В обработчиках initNavigationView() других Активити сделать переход на данную Активити
        //TODO : по клику на пункт меню
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle(getResources().getString(R.string.title_activity_activity_other_calculators));
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
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
                        //Возможно так делать неправильно.
//                        setResult(RESULT_OK);
//                        finish();
                        break;
                    case R.id.actionCaloriesCounterItem:
                        break;
                    case R.id.actionSettingsItem:
                        //добавим совместимость со старыми версиями платформы.
                        Class c = Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ?
                                PreferencesOldActivity.class : PreferencesNewActivity.class;

                        Intent intentSettings = new Intent(ActivityOtherCalculators.this, c);
                        Log.d("Class in intent", c.getName());
                        startActivityForResult(intentSettings, PreferencesNewActivity.SHOW_PREFERENCES);
                        break;
                }

                return true;
            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PreferencesNewActivity.SHOW_PREFERENCES){
            updateFromPreferences();
        }
    }


    //---------------Настройки---------------------//
    //Применение настроек приложения.
    private void updateFromPreferences(){
        //TODO: сделать обработчик применения выбранных настроек.
        //Применяю тему.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ThemeSettings.setUpdatedTheme(this, sp);
    }


}
