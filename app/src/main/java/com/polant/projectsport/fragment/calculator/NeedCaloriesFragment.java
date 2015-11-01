package com.polant.projectsport.fragment.calculator;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.polant.projectsport.Constants;
import com.polant.projectsport.R;
import com.polant.projectsport.activity.MainActivity;
import com.polant.projectsport.data.Database;
import com.polant.projectsport.data.model.UserParametersInfo;

/**
 * Created by Антон on 17.10.2015.
 */
public class NeedCaloriesFragment extends Fragment {

    private static final int LAYOUT = R.layout.fragment_need_calories;

    //Список констант-коефициентов для подсчета нормы количества калорий, в зависимости от образа жизни пользователя.
    private static final float MIN = 1.2f;
    private static final float THREE_TIMES_A_WEEK = 1.375f;
    private static final float FIVE_TIMES_PER_WEEK = 1.4625f;
    private static final float FIVE_TIMES_PER_WEEK_INTENSIVE = 1.550f;
    private static final float EVERY_DAY = 1.6375f;
    private static final float TWICE_A_DAY = 1.725f;
    private static final float EVERY_DAY_PLUS_WORKING = 1.9f;

    private View view;
    private Spinner spinner;
    private Database DB;


    //Переменные, которые служат ключами для сохранения настроек Bundle.
    private static final String KEY_NORMAL_CCAL = "KEY_NORMAL_CCAL";
    private static final String KEY_LOSS_WEIGHT_CCAL = "KEY_LOSS_WEIGHT_CCAL";
    private static final String KEY_FAST_LOSS_WEIGHT_CCAL = "KEY_FAST_LOSS_WEIGHT_CCAL";
    private static final String KEY_SPINNER_SELECTED_POSITION = "KEY_SPINNER_SELECTED_POSITION";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);

        initSpinner();
        if (savedInstanceState != null){
            initViewsOnConfigurationChanged(savedInstanceState);
        }
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        TextView normal = (TextView) view.findViewById(R.id.textViewNeedCaloriesValue);
        TextView lossWeight = (TextView) view.findViewById(R.id.textViewNeedCaloriesLossWeightValue);
        TextView fastLossWeight = (TextView) view.findViewById(R.id.textViewNeedCaloriesFastLossWeightValue);
        int spinnerSelectedIndex = spinner.getSelectedItemPosition();

        outState.putString(KEY_NORMAL_CCAL, normal.getText().toString());
        outState.putString(KEY_LOSS_WEIGHT_CCAL, lossWeight.getText().toString());
        outState.putString(KEY_FAST_LOSS_WEIGHT_CCAL, fastLossWeight.getText().toString());
        outState.putInt(KEY_SPINNER_SELECTED_POSITION, spinnerSelectedIndex);
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Получаю объект БД, который инициализирован в Активити.
        DB = ((MainActivity) getActivity()).getDatabase();

        initButtonCalculate();
        //initSpinner(); // Инициалихирую в onCreateView().
    }


    private void initButtonCalculate() {
        Button btIndex = (Button) view.findViewById(R.id.buttonCalculateNeedCalories);
        btIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildAlertDialogChoiceUserInfo();
            }
        });
    }

    private void initSpinner() {
        spinner = (Spinner) view.findViewById(R.id.spinnerLifeStyle);

        //Настраиваю адаптер.
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.array_life_style,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setPrompt(getString(R.string.text_spinner_life_style_title));
        spinner.setAdapter(adapter);
    }

    //Построение AlertDialog для изменения параметров пола, роста, веса и возраста юзера.
    private void buildAlertDialogChoiceUserInfo(){

        //Построение диалога, в котором пользователь введет количество съеденной еды.
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //Передаю не id лайаута, а ссылку View, чтобы потом получить доступ к нему.
        final View alertView = getActivity().getLayoutInflater().inflate(R.layout.alert_user_info_need_calories, null);

        RadioButton radioFromDB = (RadioButton) alertView.findViewById(R.id.radioFromDB);
        //Вынес в отдельный метод обработку радио кнопок.
        setOnRadioBtCheckedChange(radioFromDB, alertView);

        builder.setTitle(R.string.alertChangeUserInfoTitle)
                .setMessage(R.string.alertChoiceUserInfoMessage)
                .setCancelable(true)
                .setIcon(R.drawable.alert_change_user_parameters_info_icon)
                .setView(alertView)
                .setPositiveButton(getString(R.string.alertChangeUserInfoButtonPositive), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Переношу обработку в отдельный метод.
                        setOnPositiveBtAlertClick(alertView);
                    }
                })
                .setNegativeButton(getString(R.string.alertChangeUserInfoButtonNegative), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //Построение взаимодействия радио кнопок с другими представлениями диалога.
    private void setOnRadioBtCheckedChange(RadioButton radioButton, final View alertView) {
        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                EditText wEdit = (EditText) alertView.findViewById(R.id.editTextAlertUserWeight);
                EditText hEdit = (EditText) alertView.findViewById(R.id.editTextAlertUserHeight);
                EditText aEdit = (EditText) alertView.findViewById(R.id.editTextAlertUserAge);
                RadioButton radioMale = (RadioButton) alertView.findViewById(R.id.radioMale);
                RadioButton radioFemale = (RadioButton) alertView.findViewById(R.id.radioFemale);
                if (isChecked) {
                    wEdit.setEnabled(false);
                    hEdit.setEnabled(false);
                    aEdit.setEnabled(false);
                    radioMale.setEnabled(false);
                    radioFemale.setEnabled(false);
                } else {
                    wEdit.setEnabled(true);
                    hEdit.setEnabled(true);
                    aEdit.setEnabled(true);
                    radioMale.setEnabled(true);
                    radioFemale.setEnabled(true);
                }
            }
        });
    }

    //Обработчик positive button в диалоге.
    private void setOnPositiveBtAlertClick(View alertView) {
        EditText wText = (EditText) alertView.findViewById(R.id.editTextAlertUserWeight);
        EditText hText = (EditText) alertView.findViewById(R.id.editTextAlertUserHeight);
        EditText aText = (EditText) alertView.findViewById(R.id.editTextAlertUserAge);
        RadioGroup groupSex = (RadioGroup) alertView.findViewById(R.id.radioSexGroup);
        RadioGroup groupMain = (RadioGroup) alertView.findViewById(R.id.radioNeedCaloriesGroup);

        UserParametersInfo user = new UserParametersInfo();

        int idSelectedRadio = groupMain.getCheckedRadioButtonId();
        if(idSelectedRadio == R.id.radioFromDB){
            //Выбираю параметры из БД.
            user = DB.getUserParametersInfo();
        }
        else if (!TextUtils.isEmpty(wText.getText()) && !TextUtils.isEmpty(hText.getText())
                && !TextUtils.isEmpty(aText.getText())){
            //Выбираю параметры из полей ввода.
            int idSelectedSex = groupSex.getCheckedRadioButtonId();
            RadioButton radioBtSex = (RadioButton) alertView.findViewById(idSelectedSex);

            user.setSex(radioBtSex.getText().toString());

            //Проверка на корректные данные ввода.
            int age = Integer.valueOf(aText.getText().toString());
            float weight = Float.valueOf(wText.getText().toString());
            float height = Float.valueOf(hText.getText().toString());
            if (age < Constants.MIN_AGE_VALUE
                    || weight < Constants.MIN_WEIGHT_VALUE
                    || height < Constants.MIN_HEIGHT_VALUE)  {
                Toast.makeText(getActivity(), getString(R.string.toastMistakeMinValuesUserInfo),
                        Toast.LENGTH_LONG)
                        .show();
                return;
            }

            user.setWeight(Float.valueOf(wText.getText().toString()));
            user.setHeight(Float.valueOf(hText.getText().toString()));
            user.setAge(Integer.valueOf(aText.getText().toString()));
        }
        else{
            Toast.makeText(getActivity(), R.string.text_err_input_user_info_need_calories, Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        //Получаю норму количества калорий.
        setCountCalories(user.normalCaloriesCount(getActivity()));
    }

    //Устанавливаю значение калорий, исходя из образа жизни пользователя.
    private void setCountCalories(int count){
        switch (spinner.getSelectedItemPosition()){
            //Начинаю с 0, а не с 1, потому что при 0 елементе коеффициент умножения параметра (count) == 1.
            case 1:
                count *= MIN;
                break;
            case 2:
                count *= THREE_TIMES_A_WEEK;
                break;
            case 3:
                count *= FIVE_TIMES_PER_WEEK;
                break;
            case 4:
                count *= FIVE_TIMES_PER_WEEK_INTENSIVE;
                break;
            case 5:
                count *= EVERY_DAY;
                break;
            case 6:
                count *= TWICE_A_DAY;
                break;
            case 7:
                count *= EVERY_DAY_PLUS_WORKING;
                break;
        }

        TextView normal = (TextView) view.findViewById(R.id.textViewNeedCaloriesValue);
        TextView lossWeight = (TextView) view.findViewById(R.id.textViewNeedCaloriesLossWeightValue);
        TextView fastLossWeight = (TextView) view.findViewById(R.id.textViewNeedCaloriesFastLossWeightValue);

        String normalTextValue = String.valueOf(count) + " " + getString(R.string.text_ccal);
        String lossTextValue = String.valueOf((int)(count * 0.8)) + " " + getString(R.string.text_ccal);
        String fastLossTextValue = String.valueOf((int)(count * 0.6)) + " " + getString(R.string.text_ccal);

        normal.setText(normalTextValue);
        lossWeight.setText(lossTextValue);
        fastLossWeight.setText(fastLossTextValue);
    }

    //Инициализирую сохраненные значения после поворота экрана.
    private void initViewsOnConfigurationChanged(Bundle savedInstanceState) {
        TextView normal = (TextView) view.findViewById(R.id.textViewNeedCaloriesValue);
        TextView lossWeight = (TextView) view.findViewById(R.id.textViewNeedCaloriesLossWeightValue);
        TextView fastLossWeight = (TextView) view.findViewById(R.id.textViewNeedCaloriesFastLossWeightValue);

        normal.setText(savedInstanceState.getString(KEY_NORMAL_CCAL));
        lossWeight.setText(savedInstanceState.getString(KEY_LOSS_WEIGHT_CCAL));
        fastLossWeight.setText(savedInstanceState.getString(KEY_FAST_LOSS_WEIGHT_CCAL));

        spinner.setSelection(savedInstanceState.getInt(KEY_SPINNER_SELECTED_POSITION));
    }
}
