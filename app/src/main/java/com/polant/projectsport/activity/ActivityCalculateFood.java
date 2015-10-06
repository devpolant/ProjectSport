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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.polant.projectsport.MainActivity;
import com.polant.projectsport.R;
import com.polant.projectsport.data.Database;
import com.polant.projectsport.data.model.UserParametersInfo;
import com.polant.projectsport.fragment.CalculateFoodFragment;
import com.polant.projectsport.preferences.PreferencesNewActivity;
import com.polant.projectsport.preferences.PreferencesOldActivity;

/**
 * Created by Антон on 04.10.2015.
 */
public class ActivityCalculateFood extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_calculate_food;

    DrawerLayout drawerLayout;
    Toolbar toolbar;

    Database DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCurrentTheme(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        DB = new Database(this);
        DB.open();

        CalculateFoodFragment fragment = new CalculateFoodFragment();
        //добавляю Fragment динамически, чтобы я имел возможность его потом заменить методом replace()
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(
                R.id.containerKindsFood,
                fragment,
                getResources().getString(R.string.tag_fragment_calculate_food));
        transaction.commit();

        initToolbar();
        initNavigationView();
        initButtonChangeYourInfo();

        Log.d("MY_DB_LOGS", "OnCreate");
    }

    private void initButtonChangeYourInfo() {
        Button bt = (Button) findViewById(R.id.buttonChangeYourHW);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MY_DB_LOGS", "OnDestroy");
        DB.close();
    }

    public void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle(getResources().getString(R.string.nav_menu_item_calc_food));
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });

        toolbar.inflateMenu(R.menu.menu);
    }

    public void initNavigationView() {
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
//                        Intent intent = new Intent(ActivityCalculateFood.this, MainActivity.class);
//                        startActivity(intent);
                        setResult(RESULT_OK);
                        finish();
                        break;
                    case R.id.actionCaloriesCounterItem:
//                        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                        transaction.replace(
//                                R.id.container,
//                                new CalculateFoodFragment(),
//                                getResources().getString(R.string.tag_fragment_calculate_food)
//                        );
//                        transaction.commit();
                        break;
                    case R.id.actionSettingsItem:
                        //добавим совместимость со старыми версиями платформы.
                        Class c = Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ?
                                PreferencesOldActivity.class : PreferencesNewActivity.class;

                        Intent intentSettings = new Intent(ActivityCalculateFood.this, c);
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
        setUpdatedTheme(sp);
    }

    //Вызывается после применения новой темы в настройках приложения, просто меняя фон старой темы.
    private void setUpdatedTheme(SharedPreferences sp) {
        String theme = sp.getString(PreferencesNewActivity.PREF_APP_THEME, "Light");
        switch (theme){
            case "Light":
                toolbar.setBackgroundColor(getResources().getColor(R.color.ColorPrimary));
                break;
            case "Dark":
                toolbar.setBackgroundColor(getResources().getColor(R.color.DarkColorPrimary));
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
