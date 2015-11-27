package com.polant.projectsport.adapter.dialog;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.polant.projectsport.R;
import com.polant.projectsport.data.database.Database;

/**
 * Created by Антон on 11.10.2015.
 */
public class AdapterTodayFoodDialog extends CursorAdapter {

    private static final int LAYOUT = R.layout.list_dialog_adapter_today_food;

    private LayoutInflater layoutInflater;
    private Context mContext;

    public AdapterTodayFoodDialog(Context _context, Cursor c, int flags) {
        super(_context, c, flags);
        mContext = _context;
        layoutInflater = LayoutInflater.from(_context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return layoutInflater.inflate(LAYOUT, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndex(Database.FOOD_NAME));
        String category = cursor.getString(cursor.getColumnIndex(Database.FOOD_CATEGORY));
        int ccalCount = cursor.getInt(cursor.getColumnIndex(Database.CAL_COUNT));
        Float ccalDelta = cursor.getFloat(cursor.getColumnIndex(Database.DELTA));
        int day = cursor.getInt(cursor.getColumnIndex(Database.DAY));
        int month = cursor.getInt(cursor.getColumnIndex(Database.MONTH));
        int year = cursor.getInt(cursor.getColumnIndex(Database.YEAR));

        TextView nameTV = (TextView) view.findViewById(R.id.textViewDialogTodayNameFood);
        TextView categoryTV = (TextView) view.findViewById(R.id.textViewDialogTodayCategoryFood);
        TextView ccalDeltaTV = (TextView) view.findViewById(R.id.textViewDialogTodayDeltaCalories);
        TextView dayTV = (TextView) view.findViewById(R.id.textViewDialogTodayDay);
        TextView monthTV = (TextView) view.findViewById(R.id.textViewDialogTodayMonth);
        TextView yearTV = (TextView) view.findViewById(R.id.textViewDialogTodayYear);

        nameTV.setText(name);
        categoryTV.setText(category);
        String resultCalories = ccalDelta + " (" + ccalCount + ")";
        ccalDeltaTV.setText(resultCalories);
        dayTV.setText(String.valueOf(day).concat(" /"));
        monthTV.setText(String.valueOf(month).concat(" /"));
        yearTV.setText(String.valueOf(year));
    }

}
