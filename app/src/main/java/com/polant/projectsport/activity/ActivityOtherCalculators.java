package com.polant.projectsport.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.polant.projectsport.R;
import com.polant.projectsport.ThemeSettings;
import com.polant.projectsport.data.Database;
import com.polant.projectsport.fragment.calculator.IndexBodyFragment;
import com.polant.projectsport.preferences.PreferencesNewActivity;
import com.polant.projectsport.preferences.PreferencesOldActivity;

public class ActivityOtherCalculators extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_calculators;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    private Database DB;

    public static final String ACTION_INDEX_BODY = "ACTION_INDEX_BODY";
    public static final String ACTION_NEED_CALORIES = "ACTION_NEED_CALORIES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeSettings.setCurrentTheme(this, PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(LAYOUT);

        //Открываю БД здесь, чтобы не делать это в дочерних фрагментах.
        //Во фрагментах получаю базу через метод getDatabase().
        DB = new Database(this);
        DB.open();

        initToolbar();
        initNavigationView();
        //TODO : В обработчиках initNavigationView() других Активити сделать переход на данную Активити
        //TODO : по клику на пункт меню

        //Выбираю необходимый фрагмент, в зависимости от вызывающего Активити действия Action.
        replaceFragment(savedInstanceState);
    }

    private void replaceFragment(Bundle savedInstanceState){
        String action = getIntent().getAction();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (action){
            case ACTION_INDEX_BODY:
                IndexBodyFragment indexBodyFragment;

                //Использую при повороте экрана.
                if (savedInstanceState != null){
                    indexBodyFragment = (IndexBodyFragment) getSupportFragmentManager().
                            findFragmentByTag(getString(R.string.tag_fragment_index_body));
                }
                else{
                    indexBodyFragment = new IndexBodyFragment();
                }

                transaction.replace(
                        R.id.containerCalculators,
                        indexBodyFragment,
                        getString(R.string.tag_fragment_index_body)
                );
                transaction.commit();
                break;
        }
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

    //Получение БД во фрагментах.
    public Database getDatabase(){
        return DB;
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
