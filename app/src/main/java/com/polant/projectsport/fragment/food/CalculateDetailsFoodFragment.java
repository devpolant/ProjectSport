package com.polant.projectsport.fragment.food;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.polant.projectsport.adapter.food.AdapterCalculateDetailsFood;
import com.polant.projectsport.data.database.Database;
import com.polant.projectsport.model.SpecificFood;


/**
 * Created by Антон on 05.10.2015.
 */
public class CalculateDetailsFoodFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public interface FoodCheckListener{
        void changeSelectedCaloriesCount(SpecificFood specificFood, boolean isInserting);
    }

    private Context context;
    //Категория пищи.
    private String categoryFood;
    private AdapterCalculateDetailsFood adapter;
    private Database DB;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("MY_DB_LOGS", "OnAttachSF");
        context = activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DB = new Database(context);
        DB.open();

        if (getArguments() != null){
            categoryFood = getArguments().getString(Database.FOOD_CATEGORY);
        }
        adapter = new AdapterCalculateDetailsFood(context, null, 0);
        setListAdapter(adapter);

        getLoaderManager().initLoader(0, null, this);

        Log.d("MY_DB_LOGS", "OnActivityCreatedSF");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("MY_DB_LOGS", "OnDestroyViewSF");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MY_DB_LOGS", "OnDestroySF");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("MY_DB_LOGS", "OnDetachSF");
    }




    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), null, null, null, null, null){
            @Override
            public Cursor loadInBackground() {
                return DB.getSpecificFoodData(categoryFood);
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
