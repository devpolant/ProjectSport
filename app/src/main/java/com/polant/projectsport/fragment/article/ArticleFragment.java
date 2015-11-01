package com.polant.projectsport.fragment.article;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.polant.projectsport.Constants;
import com.polant.projectsport.R;
import com.polant.projectsport.activity.MainActivity;
import com.polant.projectsport.activity.ArticleInfoActivity;
import com.polant.projectsport.adapter.AdapterArticles;
import com.polant.projectsport.data.Database;
import com.polant.projectsport.data.parse.ParserTXT;

/**
 * Created by Антон on 24.10.2015.
 */
public class ArticleFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String ARTICLE_CATEGORY = "ARTICLE_CATEGORY";

    private Handler handler = new Handler();

    private Activity activity;
    private Database DB;
    private AdapterArticles adapter;

    private String categoryArticle;

    @Override
    public void onAttach(Activity _activity) {
        super.onAttach(_activity);
        activity = _activity;
    }

    public static ArticleFragment getInstance(String categoryArticle){
        Bundle args = new Bundle();
        args.putString(ArticleFragment.ARTICLE_CATEGORY, categoryArticle);

        ArticleFragment fragment = new ArticleFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DB = new Database(activity);
        DB.open();
        if (getArguments() != null){
            categoryArticle = getArguments().getString(ARTICLE_CATEGORY);
        }
        adapter = new AdapterArticles(activity, null, 0);
        setListAdapter(adapter);

        getLoaderManager().initLoader(0, null, this);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                parse();
            }
        });
        thread.start();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent intent = new Intent(activity, ArticleInfoActivity.class);
        intent.putExtra(ArticleInfoActivity.ARTICLE_ID, id);
        startActivityForResult(intent, Constants.SHOW_ACTIVITY_ARTICLE_INFO);
    }

    //Парсинг raw ресурса текстового файла со статьями.
    private void parse() {
        //получаем сохраненные настройки.
        Context context = getActivity().getApplicationContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (preferences.getInt(MainActivity.DB_ARTICLES_VERSION_KEY, -1) != MainActivity.DBVersion) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(MainActivity.DB_ARTICLES_VERSION_KEY, MainActivity.DBVersion);
            editor.apply();

            ParserTXT parserTXT = new ParserTXT(DB);
            parserTXT.parseArticle(getResources().openRawResource(R.raw.articles));
            handler.post(new Runnable() {
                @Override
                public void run() {
                    getLoaderManager().restartLoader(0, null, ArticleFragment.this);
                }
            });
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), null, null, null, null, null){
            @Override
            public Cursor loadInBackground() {
                String[] projection = new String[] {
                        Database.ID_ARTICLE,
                        Database.ARTICLE_TITLE,
                        Database.ARTICLE_CATEGORY,
                        Database.ARTICLE_DATE
                };
                return DB.getArticles(projection, categoryArticle);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
