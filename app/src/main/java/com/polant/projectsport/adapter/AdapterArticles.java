package com.polant.projectsport.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.polant.projectsport.R;
import com.polant.projectsport.data.Database;

/**
 * Created by Антон on 24.10.2015.
 */
public class AdapterArticles extends CursorAdapter {

    private static final int LAYOUT = R.layout.list_adapter_article;

    private LayoutInflater layoutInflater;
    private Context context;

    public AdapterArticles(Context _context, Cursor c, int flags) {
        super(_context, c, flags);
        context = _context;
        layoutInflater = LayoutInflater.from(_context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return layoutInflater.inflate(LAYOUT, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String title = cursor.getString(cursor.getColumnIndex(Database.ARTICLE_TITLE));
        TextView titleTextView = (TextView) view.findViewById(R.id.textViewArticleName);

        String date = cursor.getString(cursor.getColumnIndex(Database.ARTICLE_DATE));
        TextView dateTextView = (TextView) view.findViewById(R.id.textViewArticleDate);

        titleTextView.setText(title);
        dateTextView.setText(date);
    }

}
