package com.polant.projectsport.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.polant.projectsport.R;
import com.polant.projectsport.data.Database;

/**
 * Created by Антон on 05.10.2015.
 */
public class AdapterCalculateDetailsFood extends CursorAdapter{

    private LayoutInflater layoutInflater;
    private Context context;

    public AdapterCalculateDetailsFood(Context _context, Cursor c, int flags) {
        super(_context, c, flags);
        context = _context;
        layoutInflater = LayoutInflater.from(_context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return layoutInflater.inflate(R.layout.list_adapter_details_food, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String nameFood = cursor.getString(cursor.getColumnIndex(Database.FOOD_NAME));
        //столбец имеет INTEGER значение, но можно получить и через метод cursor.getString(). Я пробовал - работает и так.
        String caloriesCount = String.valueOf(cursor.getInt(cursor.getColumnIndex(Database.CAL_COUNT)));

        TextView nameTextView = (TextView) view.findViewById(R.id.textViewDetailFood);
        TextView caloriesTextView = (TextView) view.findViewById(R.id.textViewCalInFood);

        nameTextView.setText(nameFood);
        caloriesTextView.setText(caloriesCount);
    }

}
