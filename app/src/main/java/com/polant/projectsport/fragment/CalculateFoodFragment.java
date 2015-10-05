package com.polant.projectsport.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.polant.projectsport.MainActivity;
import com.polant.projectsport.R;
import com.polant.projectsport.data.Database;
import com.polant.projectsport.data.parse.ParserTXT;

/**
 * Created by Антон on 04.10.2015.
 */
public class CalculateFoodFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private Context context;
    private Handler handler = new Handler();
    private Database DB;

    private SimpleCursorAdapter adapter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("MY_DB_LOGS", "OnAttachF");
        context = activity;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        String fruits = "фрукты";
//        String[] arr = new String[] { fruits, "Овощи", "Мясо", "Грибы", "Молочные продукты"};
//
//        //----------------заглушка----------------//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,
//                arr);
//        setListAdapter(adapter);

        Log.d("MY_DB_LOGS", "OnActivityCreatedF");

        DB = new Database(getActivity());
        DB.open();

        //формируем столбцы сопоставления
        String[] from = new String[] { Database.FOOD_CATEGORY };
        int[] to = new int[] { android.R.id.text1 };
        adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, null, from, to);
        // создааем адаптер и настраиваем список
        //scAdapter = new SimpleCursorAdapter(this, R.layout.item, cursor, from, to);
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

    private void parse() {
        //получаем сохраненные настройки.
        Context context = getActivity().getApplicationContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (preferences.getInt(MainActivity.DB_VERSION_KEY, -1) != MainActivity.DBVersion) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(MainActivity.DB_VERSION_KEY, MainActivity.DBVersion);
            editor.apply();

            ParserTXT parserTXT = new ParserTXT(DB);
            parserTXT.parse(getResources().openRawResource(R.raw.calories));
            handler.post(new Runnable() {
                @Override
                public void run() {
                    getLoaderManager().restartLoader(0, null, CalculateFoodFragment.this);
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("MY_DB_LOGS", "OnDestroyViewF");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MY_DB_LOGS", "OnDestroyF");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("MY_DB_LOGS", "OnDetachF");
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        //TODO : сделать выбор, учитывая выбранный элемент.

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.containerKindsFood,
                new CalculateDetailsFoodFragment(),
                getResources().getString(R.string.tag_fragment_calculate_details_food)
        );
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), null, null, null, null, null){
            @Override
            public Cursor loadInBackground() {
                return DB.getFoodData();
            }
        };
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
