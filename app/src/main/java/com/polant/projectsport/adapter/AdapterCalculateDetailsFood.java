package com.polant.projectsport.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.polant.projectsport.R;
import com.polant.projectsport.activity.ActivityCalculateFood;
import com.polant.projectsport.data.Database;
import com.polant.projectsport.data.model.SpecificFood;

/**
 * Created by Антон on 05.10.2015.
 */
public class AdapterCalculateDetailsFood extends CursorAdapter{

    private LayoutInflater layoutInflater;
    private final Context mContext;

    public AdapterCalculateDetailsFood(Context _context, Cursor c, int flags) {
        super(_context, c, flags);
        mContext = _context;
        layoutInflater = LayoutInflater.from(_context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return layoutInflater.inflate(R.layout.list_adapter_details_food, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final String nameFood = cursor.getString(cursor.getColumnIndex(Database.FOOD_NAME));
        //столбец имеет INTEGER значение, но можно получить и через метод cursor.getString(). Я пробовал - работает и так.
        final String caloriesCount = String.valueOf(cursor.getInt(cursor.getColumnIndex(Database.CAL_COUNT)));

        //Проинициализирую визуальные значения в списке.
        TextView nameTextView = (TextView) view.findViewById(R.id.textViewDetailFood);
        TextView caloriesTextView = (TextView) view.findViewById(R.id.textViewCalInFood);
        nameTextView.setText(nameFood);
        caloriesTextView.setText(caloriesCount);


        //далее все, что нужно для обработчика клика по ImageView для добавления.
        final int idSpecificFood = cursor.getInt(cursor.getColumnIndex(Database.ID_SPECIFIC_FOOD));
        final String foodCategory = cursor.getString(cursor.getColumnIndex(Database.FOOD_CATEGORY));

        ImageView imageClickable = (ImageView) view.findViewById(R.id.imageViewDetailFood);
        imageClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpecificFood sf = new SpecificFood(idSpecificFood, foodCategory, nameFood, Integer.valueOf(caloriesCount));
                //Передаю параметры, которые будут обрабатываться уже в самой Активити.
                ((ActivityCalculateFood) mContext).changeSelectedCaloriesCount(sf, true);
            }
        });
    }

}
