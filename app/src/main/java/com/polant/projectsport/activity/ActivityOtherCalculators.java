package com.polant.projectsport.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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

import com.polant.projectsport.Constants;
import com.polant.projectsport.MainActivity;
import com.polant.projectsport.R;
import com.polant.projectsport.ThemeSettings;
import com.polant.projectsport.data.Database;
import com.polant.projectsport.fragment.calculator.IndexBodyFragment;
import com.polant.projectsport.fragment.calculator.NeedCaloriesFragment;
import com.polant.projectsport.fragment.step.StepCounterFragment;
import com.polant.projectsport.preferences.PreferencesNewActivity;
import com.polant.projectsport.preferences.PreferencesOldActivity;

public class ActivityOtherCalculators extends AppCompatActivity implements SensorEventListener,
                                                    StepCounterFragment.StepCounterManagerListener{

    private static final int LAYOUT = R.layout.activity_calculators;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    private Database DB;

    public static final String ACTION_INDEX_BODY = "ACTION_INDEX_BODY";
    public static final String ACTION_NEED_CALORIES = "ACTION_NEED_CALORIES";
    public static final String ACTION_STEP_COUNTER = "ACTION_STEP_COUNTER";


    public static final String CURRENT_ACTION_STRING = "CURRENT_ACTION_STRING";
    public static final String FIRST_ACTION = "FIRST_ACTION";


    private String firstCallAction;
    private SharedPreferences prefs;

    //Сенсоры.
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private Sensor stepDetectorSensor;

    private StepCounterFragment stepCounterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ThemeSettings.setCurrentTheme(this, prefs);
        setContentView(LAYOUT);

        Log.d("MY_LOGS", "onCreate()");

        //Открываю БД здесь, чтобы не делать это в дочерних фрагментах.
        //Во фрагментах получаю базу через метод getDatabase().
        DB = new Database(this);
        DB.open();

        initToolbar();
        initNavigationView();

        firstCallAction = ACTION_STEP_COUNTER;

        //Сохнаняю текущее действие в настройки приложения.
        initSharedPreferences();
        //Выбираю необходимый фрагмент, в зависимости от (вызывающего Активити) действия Action.
        replaceFragment(savedInstanceState);
    }

    //Замена фрагмента, используется в onCreate() и для транзакций в navigationView.
    private void replaceFragment(Bundle savedInstanceState){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (getCurrentAction()){
            case ACTION_STEP_COUNTER:
                stepCounterFragment = new StepCounterFragment();
                transaction.replace(
                        R.id.containerCalculators,
                        stepCounterFragment,
                        getString(R.string.tag_fragment_step_counter)
                );
                transaction.commit();
                break;
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
                stepCounterFragment = null;
                break;
            case ACTION_NEED_CALORIES:
                NeedCaloriesFragment needCaloriesFragment;

                //Использую при повороте экрана.
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
                stepCounterFragment = null;
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
                        Intent articleIntent = new Intent(ActivityOtherCalculators.this, MainActivity.class);
                        startActivityForResult(articleIntent, Constants.SHOW_ACTIVITY_ARTICLES);
                        break;
                    case R.id.actionStepCounterItem:
                        if (getCurrentAction().equals(ACTION_NEED_CALORIES) ||
                                getCurrentAction().equals(ACTION_INDEX_BODY)) {
                            setCurrentAction(ACTION_STEP_COUNTER);
                            replaceFragment(null);
                        }
                        break;
                    case R.id.ActionIndexBodyWeight:
                        if (getCurrentAction().equals(ACTION_NEED_CALORIES) ||
                                getCurrentAction().equals(ACTION_STEP_COUNTER)) {
                            setCurrentAction(ACTION_INDEX_BODY);
                            replaceFragment(null);
                        }
                        break;
                    case R.id.ActionDayNeedCalories:
                        if (getCurrentAction().equals(ACTION_INDEX_BODY) ||
                                getCurrentAction().equals(ACTION_STEP_COUNTER)) {
                            setCurrentAction(ACTION_NEED_CALORIES);
                            replaceFragment(null);
                        }
                        break;
                    case R.id.ActionCalculateFood:
                        Intent foodCaloriesCounter = new Intent(ActivityOtherCalculators.this, ActivityCalculateFood.class);
                        startActivityForResult(foodCaloriesCounter, Constants.SHOW_ACTIVITY_CALCULATE_FOOD);
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

    //Данные 3 метода нужны для того, чтобы транзакции фрагментов проходили корректно, даже если
    //в каком то фрагменте произошел поворот экрана.
    private void initSharedPreferences(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(FIRST_ACTION, firstCallAction);
        String current = prefs.getString(CURRENT_ACTION_STRING, "");
        if (current.equals("")){
            editor.putString(CURRENT_ACTION_STRING, String.valueOf(firstCallAction));
        }
        editor.apply();
    }
    private void setCurrentAction(String action){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(CURRENT_ACTION_STRING, action);
        editor.apply();
    }
    private String getCurrentAction(){
        return prefs.getString(CURRENT_ACTION_STRING, "");
    }
    private void deleteCurrentAction(){
        //Удаляю значение настройки текущего действия, которое используется в ActivityOtherCalculators.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(ActivityOtherCalculators.CURRENT_ACTION_STRING);
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MY_LOGS", "Destroy ActivityOtherCalculators");
        deleteCurrentAction();
        DB.close();
        unRegisterSensors();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //outState.putString(CURRENT_ACTION_STRING, getCurrentAction());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        updateFromPreferences();
    }


    //---------------Настройки---------------------//
    //Применение настроек приложения.
    private void updateFromPreferences(){
        //TODO: сделать обработчик применения выбранных настроек.
        //Применяю тему.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ThemeSettings.setUpdatedTheme(this, sp);
    }



    @Override
    public void registerCounter() {
        initSensors();
    }

    @Override
    public void unregisterCounter() {
        unRegisterSensors();
    }

    //Инициализирую сенсоры, которые использую для подсчета шагов.
    private void initSensors(){
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void unRegisterSensors(){
        sensorManager.unregisterListener(this, stepCounterSensor);
        sensorManager.unregisterListener(this, stepDetectorSensor);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        float[] values = event.values;
        int value = -1;

        if (values.length > 0) {
            value = (int) values[0];
            if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
                if (stepCounterFragment != null) {
                    stepCounterFragment.stepDetected(value);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
