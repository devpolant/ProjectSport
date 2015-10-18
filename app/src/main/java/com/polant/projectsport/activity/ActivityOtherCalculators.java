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
import com.polant.projectsport.fragment.calculator.NeedCaloriesFragment;
import com.polant.projectsport.fragment.step.StepCounterFragment;
import com.polant.projectsport.preferences.PreferencesNewActivity;
import com.polant.projectsport.preferences.PreferencesOldActivity;

public class ActivityOtherCalculators extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_calculators;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    private Database DB;

    public static final String ACTION_INDEX_BODY = "ACTION_INDEX_BODY";
    public static final String ACTION_NEED_CALORIES = "ACTION_NEED_CALORIES";
    public static final String ACTION_STEP_COUNTER = "ACTION_STEP_COUNTER";


    public static final String CURRENT_ACTION = "CURRENT_ACTION";
    public static final String FIRST_ACTION = "FIRST_ACTION";


    private String firstCallAction;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ThemeSettings.setCurrentTheme(this, prefs);
        setContentView(LAYOUT);

        //�������� �� �����, ����� �� ������ ��� � �������� ����������.
        //�� ���������� ������� ���� ����� ����� getDatabase().
        DB = new Database(this);
        DB.open();

        initToolbar();
        initNavigationView();
        //TODO : � ������������ initNavigationView() ������ �������� ������� ������� �� ������ ��������
        //TODO : �� ����� �� ����� ����

        firstCallAction = getIntent().getAction();

        //�������� ������� �������� � ��������� ����������.
        initSharedPreferences();

        //������� ����������� ��������, � ����������� �� ����������� �������� �������� Action.
        replaceFragment(savedInstanceState);
    }

    //������ 3 ������ ����� ��� ����, ����� ���������� ���������� ��������� ���������, ���� ����
    //� ����� �� ��������� ��������� ������� ������.
    private void initSharedPreferences(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(FIRST_ACTION, firstCallAction);
        String current = prefs.getString(CURRENT_ACTION, "");
        if (current.equals("")){
            editor.putString(CURRENT_ACTION, String.valueOf(firstCallAction));
        }
        editor.apply();
    }
    private void setCurrentAction(String action){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(CURRENT_ACTION, action);
        editor.apply();
    }
    private String getCurrentAction(){
        return prefs.getString(CURRENT_ACTION, "");
    }

    //������ ���������, ������������ � onCreate() � ��� ���������� � navigationView.
    private void replaceFragment(Bundle savedInstanceState){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (getCurrentAction()){
            case ACTION_STEP_COUNTER:
                StepCounterFragment stepCounterFragment = new StepCounterFragment();
                transaction.replace(
                        R.id.containerCalculators,
                        stepCounterFragment,
                        getString(R.string.tag_fragment_step_counter)
                );
                transaction.commit();
                break;
            case ACTION_INDEX_BODY:
                IndexBodyFragment indexBodyFragment;

                //��������� ��� �������� ������.
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
            case ACTION_NEED_CALORIES:
                NeedCaloriesFragment needCaloriesFragment;

                //��������� ��� �������� ������.
                if (savedInstanceState != null){
                    needCaloriesFragment = (NeedCaloriesFragment) getSupportFragmentManager().
                            findFragmentByTag(getString(R.string.tag_fragment_need_calories));
                }
                else{
                    needCaloriesFragment = new NeedCaloriesFragment();
                }

                transaction.replace(
                        R.id.containerCalculators,
                        needCaloriesFragment,
                        getString(R.string.tag_fragment_need_calories)
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

                        break;
                    case R.id.ActionIndexBodyWeight:
                        if (getCurrentAction().equals(ACTION_NEED_CALORIES)){
                            setCurrentAction(ACTION_INDEX_BODY);
                            replaceFragment(null);
                        }
                        break;
                    case R.id.ActionDayNeedCalories:
                        if (getCurrentAction().equals(ACTION_INDEX_BODY)){
                            setCurrentAction(ACTION_NEED_CALORIES);
                            replaceFragment(null);
                        }
                        break;
                    case R.id.actionSettingsItem:
                        //������� ������������� �� ������� �������� ���������.
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

    //��������� �� �� ����������.
    public Database getDatabase(){
        return DB;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        updateFromPreferences();
    }


    //---------------���������---------------------//
    //���������� �������� ����������.
    private void updateFromPreferences(){
        //TODO: ������� ���������� ���������� ��������� ��������.
        //�������� ����.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ThemeSettings.setUpdatedTheme(this, sp);
    }


}
