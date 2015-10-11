package com.polant.projectsport.fragment.dialog;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.polant.projectsport.R;
import com.polant.projectsport.activity.ActivityCalculateFood;
import com.polant.projectsport.adapter.dialog.AdapterTodayFoodDialog;
import com.polant.projectsport.data.Database;

/**
 * Created by Антон on 11.10.2015.
 */
public class TodayFoodDialogFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor>{


    public interface TodayListFoodChangeListener{
        void changeTodayListFood();
    }
    TodayListFoodChangeListener mListener;

    private Database DB;
    private AdapterTodayFoodDialog adapter;
    private View view;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (ActivityCalculateFood) activity;
    }

    public static TodayFoodDialogFragment newInstance(){
        return new TodayFoodDialogFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.alert_food_today_list, container, false);
        return view;
    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getDialog().setTitle(R.string.alertChangeTodayList);

        DB = new Database(getActivity());
        DB.open();

        adapter = new AdapterTodayFoodDialog(getActivity(), null, 0);

        ListView listView = (ListView) view.findViewById(R.id.listViewTodayFood);
        listView.setAdapter(adapter);

        getLoaderManager().initLoader(0, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), null, null, null, null, null){
            @Override
            public Cursor loadInBackground() {
                return DB.getTodayFoodStatistics();
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
