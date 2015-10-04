package com.polant.projectsport.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;

/**
 * Created by Антон on 04.10.2015.
 */
public class CalculateFoodFragment extends ListFragment {

    private Context context;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        context = activity;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String fruits = "фрукты";

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,
                new String[] { fruits, "Овощи", "Мясо", "Грибы", "Молочные продукты"});
        setListAdapter(adapter);

    }



}
