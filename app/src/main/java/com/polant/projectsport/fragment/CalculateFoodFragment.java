package com.polant.projectsport.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.polant.projectsport.R;

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
        String[] arr = new String[] { fruits, "Овощи", "Мясо", "Грибы", "Молочные продукты"};

        //----------------заглушка----------------//
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,
                arr);
        setListAdapter(adapter);

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
        transaction.commit();
    }
}
