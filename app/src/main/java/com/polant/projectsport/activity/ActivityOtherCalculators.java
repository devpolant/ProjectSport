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
import com.polant.projectsport.data.model.UserParametersInfo;
import com.polant.projectsport.data.parse.ParserTXT;
import com.polant.projectsport.fragment.calculator.IndexBodyFragment;
import com.polant.projectsport.fragment.calculator.NeedCaloriesFragment;
import com.polant.projectsport.fragment.step.StepCounterFragment;
import com.polant.projectsport.preferences.PreferencesNewActivity;
import com.polant.projectsport.preferences.PreferencesOldActivity;

public class ActivityOtherCalculators extends AppCompatActivity implements SensorEventListener,
                                                    StepCounterFragment.StepCounterManagerListener{

    private static final int LAYOUT = R.layout.activity_calculators;

    public static final int DBVersion = Database.getDatabaseVersion();
    public static final String DB_ARTICLES_VERSION_KEY = "DB_ARTICLES_VERSION_KEY";
    public static final String DB_FOOD_VERSION_KEY = "DB_FOOD_VERSION_KEY";


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
    //Текущее значение шагомера.
    private int currentStepValue;
    private int resetStepValue;
    //Специальная переменная, которая служит лишь при первом собитии датчика шагомера.
    //Ее цель - проверить, не равно ли значениее кол-ва шагов 0.
    //Если оно равно, то обнуляю SharedPreferences.
    private int testStepSensorValue;


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

        //Тестовая переменная.
        testStepSensorValue = 0;
        //Текущие значения шагомера.
        currentStepValue = prefs.getInt(PreferencesNewActivity.PREF_CURRENT_STEP_COUNT, 0);
        resetStepValue = prefs.getInt(PreferencesNewActivity.PREF_RESET_STEP_COUNT, 0);
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
                        showStepCounterFragment();
                        break;
                    case R.id.ActionIndexBodyWeight:
                        showIndexBodyFragment();
                        break;
                    case R.id.ActionDayNeedCalories:
                        showNeedCaloriesFragment();
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


    //Методы для вызова транзакций фрагментов, которые используются в данной Активити.
    private void showStepCounterFragment(){
        if (getCurrentAction().equals(ACTION_NEED_CALORIES) ||
                getCurrentAction().equals(ACTION_INDEX_BODY)) {
            setCurrentAction(ACTION_STEP_COUNTER);
            replaceFragment(null);
        }
    }
    private void showIndexBodyFragment(){
        if (getCurrentAction().equals(ACTION_NEED_CALORIES) ||
                getCurrentAction().equals(ACTION_STEP_COUNTER)) {
            setCurrentAction(ACTION_INDEX_BODY);
            replaceFragment(null);
        }
    }
    private void showNeedCaloriesFragment(){
        if (getCurrentAction().equals(ACTION_INDEX_BODY) ||
                getCurrentAction().equals(ACTION_STEP_COUNTER)) {
            setCurrentAction(ACTION_NEED_CALORIES);
            replaceFragment(null);
        }
    }


    //------------------Жизненный цикл---------------------//

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MY_LOGS", "Destroy ActivityOtherCalculators");
        DB.close();
        Log.d("MY_LOGS", "DB.close() ActivityOtherCalculators");
        unRegisterSensors();
        Log.d("MY_LOGS", "unRegisterSensors() ActivityOtherCalculators");
        //Сохраняю значение шагомера.
        setStepCount(currentStepValue);
        Log.d("MY_LOGS", "setStepCount() ActivityOtherCalculators");
        deleteCurrentAction();
        Log.d("MY_LOGS", "deleteCurrentAction() ActivityOtherCalculators");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MY_LOGS", "Stop ActivityOtherCalculators");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MY_LOGS", "Pause ActivityOtherCalculators");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        updateFromPreferences();
        if (requestCode == Constants.SHOW_ACTIVITY_ARTICLES ||
                requestCode == Constants.SHOW_ACTIVITY_CALCULATE_FOOD){

            if (resultCode == RESULT_OK) {

                switch (data.getStringExtra(CURRENT_ACTION_STRING)) {
                    case ACTION_STEP_COUNTER:
                        showStepCounterFragment();
                        break;
                    case ACTION_INDEX_BODY:
                        showIndexBodyFragment();
                        break;
                    case ACTION_NEED_CALORIES:
                        showNeedCaloriesFragment();
                        break;
                }
            }
        }
    }


    //Вызывается в onDestroy() данной Активити.
    @Override
    public void setStepCount(int stepCount) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PreferencesNewActivity.PREF_CURRENT_STEP_COUNT, stepCount);
        editor.apply();
        //Или правильнее сделать так: (раньше устанавливал в 0)
        currentStepValue = stepCount;
    }

    //Вызывается обработчиком кнопки reset фрагмента StepCounterFragment.
    @Override
    public void setBeforeResetCount(int resetCount) {
        resetStepValue += resetCount;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PreferencesNewActivity.PREF_RESET_STEP_COUNT, resetStepValue);
        editor.apply();
    }

    //Получение текущего состояния кол-ва шагов.
    public int getStepCountValue(){
        return currentStepValue;
    }


    //---------------Настройки---------------------//
    //Применение настроек приложения.
    private void updateFromPreferences(){
        //Применяю тему.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ThemeSettings.setUpdatedTheme(this, sp);

        //Обновляю информация о пользователе.
        DB.updateUserParametersInfo(sp);
    }


    //------------------Сенсоры---------------------//

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
        //Если пользователь нажал стоп, до того как нажал старт возникает NullPointerException.
        try {
            sensorManager.unregisterListener(this, stepCounterSensor);
            sensorManager.unregisterListener(this, stepDetectorSensor);
        }
        catch (NullPointerException ex) { }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        float[] values = event.values;
        int value = -1;

        if (values.length > 0) {
            value = (int) values[0];
            if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {

                if (testStepSensorValue == 0){
                    if (value == 0){
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt(PreferencesNewActivity.PREF_CURRENT_STEP_COUNT, 0);
                        editor.putInt(PreferencesNewActivity.PREF_RESET_STEP_COUNT, 0);
                        editor.apply();

                        currentStepValue = 0;
                        resetStepValue = 0;

                        //И инкрементирую переменную, чтобы данная проверка проходила лишь 1 раз
                        //при создании Активити.
                        testStepSensorValue++;
                    }
                }
                //
                currentStepValue = value - resetStepValue;

                if (stepCounterFragment != null) {
                    stepCounterFragment.stepDetected(currentStepValue);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
