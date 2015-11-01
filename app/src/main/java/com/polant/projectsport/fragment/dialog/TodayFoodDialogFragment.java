package com.polant.projectsport.fragment.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.polant.projectsport.R;
import com.polant.projectsport.activity.ActivityCalculateFood;
import com.polant.projectsport.adapter.dialog.AdapterTodayFoodDialog;
import com.polant.projectsport.data.Database;

/**
 * Created by Антон on 11.10.2015.
 */
public class TodayFoodDialogFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int LAYOUT = R.layout.alert_food_today_list;
    
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
        view = inflater.inflate(LAYOUT, container, false);
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

        //Назначаю обработчик нажатия на список для удаления записи из базы.
        setListItemLongClickListener(listView);
    }


    private void setListItemLongClickListener(ListView listView){
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final long idListItem = id;

                //Построение диалога, в котором пользователь введет количество съеденной еды.
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.alertChangeTodayListDeleteTitle)
                        .setMessage(R.string.alertChangeTodayListDeleteMessage)
                        .setCancelable(true)
                        .setIcon(R.drawable.icon_delete)
                        .setPositiveButton(getString(R.string.alertChangeTodayListDeleteButtonText), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DB.deleteStatisticsRecord((int) idListItem);
                                getLoaderManager().restartLoader(0, null, TodayFoodDialogFragment.this);
                                mListener.changeTodayListFood();
                            }
                        })
                        .setNegativeButton(getString(R.string.alertAddFoodNegativeBt), new DialogInterface.OnClickListener() {
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

                return false;
            }
        });
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
