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

import com.polant.projectsport.activity.ActivityCalculateFood;
import com.polant.projectsport.adapter.TabsPagerFragmentAdapter;
import com.polant.projectsport.data.Database;
import com.polant.projectsport.fragment.ArticleFragment;
import com.polant.projectsport.preferences.PreferencesNewActivity;
import com.polant.projectsport.preferences.PreferencesOldActivity;

/**
 * Created by Антон on 02.10.2015.
 */
public class MainActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_main;

    public static final int DBVersion = Database.getDatabaseVersion();
    public static final String DB_VERSION_KEY = "DB_VERSION_KEY";

    public static final int SHOW_ACTIVITY_CALCULATE_FOOD = 4;

    //находится в разметке самой активити.
    DrawerLayout drawerLayout;

    //находятся в разметке фрагментов, которые содержит MainActivity.
    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;

    IToolbarFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.AppDefault);
        setCurrentTheme(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        fragment = new ArticleFragment();

        //добавляю Fragment динамически, чтобы я имел возможность его потом заменить методом replace()
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(
                R.id.container,
                (ArticleFragment) fragment,
                getResources().getString(R.string.tag_fragment_article));
        transaction.commit();

//        initToolbar();
//        initNavigationView();
//        initTabLayout();
    }

    //используется во фрагментах.
    public void initToolbar(Toolbar tool) {
        //Получаю ссылку на инициализированный во фрагменте Toolbar.
        toolbar = tool;

        toolbar.setTitle(R.string.app_name);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });

        //toolbar.inflateMenu(R.menu.menu);
    }

    //используется во фрагментах.
    public void initTabLayout(ViewPager pager, TabLayout tab) {
        //Получаю ссылку на инициализированные во фрагменте ViewPager и TabLayout.
        viewPager = pager;
        tabLayout = tab;

        TabsPagerFragmentAdapter adapter = new TabsPagerFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) ((ArticleFragment) fragment).getTabLayout();
        tabLayout.setupWithViewPager(viewPager);
    }

    public void initNavigationView(Toolbar tool) {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, tool, R.string.navigation_view_open,
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
//                        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                        fragment = null;
//                        transaction.replace(
//                                R.id.container,
//                                new ArticleFragment(),
//                                getResources().getString(R.string.tag_fragment_article)
//                        );
//                        transaction.commit();
                        break;
                    case R.id.ActionCalculateFood:
                        Intent foodCaloriesCouter = new Intent(MainActivity.this, ActivityCalculateFood.class);
                        startActivityForResult(foodCaloriesCouter, SHOW_ACTIVITY_CALCULATE_FOOD);
                        break;
                    case R.id.actionSettingsItem:
                        //добавим совместимость со старыми версиями платформы.
                        Class c = Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ?
                                PreferencesOldActivity.class : PreferencesNewActivity.class;

                        Intent intent = new Intent(MainActivity.this, c);
                        Log.d("Class in intent", c.getName());
                        startActivityForResult(intent, PreferencesNewActivity.SHOW_PREFERENCES);
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

        updateFromPreferences();
//        if (requestCode == PreferencesNewActivity.SHOW_PREFERENCES){
//            updateFromPreferences();
//        }

    }

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

        if (fragment == null || fragment instanceof ArticleFragment == false || fragment instanceof IToolbarFragment == false){
            return;
        }

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
