package com.polant.projectsport.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.widget.SimpleAdapter;

import com.polant.projectsport.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Антон on 05.10.2015.
 */
public class CalculateDetailsFoodFragment extends ListFragment {

    public interface FoodCheckListener{
        void changeSelectedCaloriesCount(int delta);
    }

    private Context context;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("MY_DB_LOGS", "OnAttachSF");
        context = activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //------------------------------------//
        //заглушка чисто для пробы.
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();

        String[] mas = new String[] {"Абрикос", "Авокадо", "Апельсин"};
        String[] cal = new String[] {"47", "100", "30"};

        //HashMap<String, String> temp = new HashMap<String, String>() {"Иванов И. И" : "Директор"};

        for(int i = 0; i < mas.length; i++)
        {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("Food", mas[i]);
            map.put("Img", String.valueOf(R.drawable.fab_small));
            map.put("Cals", cal[i]);
            arrayList.add(map);
        }

        //создаем адаптер.
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), arrayList, R.layout.list_adapter_details_food,
                new String[] {"Food", "Img","Cals"},
                new int[] {R.id.textViewDetailFood, R.id.imageButtonDetailFood, R.id.textViewCalInFood});
        //подписали ListView на адаптер.
        setListAdapter(adapter);
        //------------------------------------//

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
}
