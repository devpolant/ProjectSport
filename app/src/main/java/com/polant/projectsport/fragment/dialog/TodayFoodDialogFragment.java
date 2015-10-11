package com.polant.projectsport.fragment.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.polant.projectsport.R;
import com.polant.projectsport.activity.ActivityCalculateFood;

import java.util.ArrayList;

/**
 * Created by Антон on 11.10.2015.
 */
public class TodayFoodDialogFragment extends DialogFragment {

    public interface TodayListFoodChangeListener{
        void changeTodayListFood();
    }
    TodayListFoodChangeListener mListener;

    View view;

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().setTitle(R.string.alertChangeTodayList);
        ListView listView = (ListView) view.findViewById(R.id.listViewTodayFood);

        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            arrayList.add("First");
            arrayList.add("Second");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList);

        listView.setAdapter(adapter);
    }
}
