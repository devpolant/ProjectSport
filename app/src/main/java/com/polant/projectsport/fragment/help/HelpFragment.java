package com.polant.projectsport.fragment.help;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import com.polant.projectsport.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Используется в HelpActivity.
 */
public class HelpFragment extends Fragment{

    private static final int LAYOUT = R.layout.fragment_help_temp;

    //Константа, которая определяет интервал статистики.
    public static final String HELP_TYPE = "HELP_TYPE";
    public static final String HELP_DESCRIPTION = "HELP_DESCRIPTION";
    public static final String HELP_PROCEDURE = "HELP_PROCEDURE";

    private View view;
    private String helpType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        return view;
    }

    //Метод-фабрика.
    public static HelpFragment getInstance(String helpType){
        Bundle args = new Bundle();
        args.putString(HELP_TYPE, helpType);

        HelpFragment fragment = new HelpFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null){
            helpType = getArguments().getString(HELP_TYPE);
        }
        initExpandableListView();
    }

    private void initExpandableListView() {
        String[] groups = new String[] {
                "Калькуляторы", "Шагомер", "Статьи", "Статистика", "Настройки"
        };

        Map<String, String> map;
        ArrayList<Map<String, String>> groupDataList = new ArrayList<>();

        for (String group: groups){
            map = new HashMap<>();
            map.put("groupName", group);
            groupDataList.add(map);
        }

        String[] groupFrom = new String[]{ "groupName" };
        int[] groupTo = new int[] { android.R.id.text1 };

        //Общая коллекция для коллекций элементов.
        ArrayList<ArrayList<Map<String, String>>> childDataList = new ArrayList<>();

        ArrayList<Map<String, String>> childDataItemList;
        for (int i = 0; i < groups.length; i++){
            childDataItemList = new ArrayList<>();
            map = new HashMap<>();
            String info = null;
            switch (i){
                case 0:
                    info = helpType.equals(HELP_DESCRIPTION) ? getString(R.string.help_text_calculators):
                                        getString(R.string.help_text_calculators_proc);
                    break;
                case 1:
                    info = helpType.equals(HELP_DESCRIPTION) ? getString(R.string.help_text_step_counter):
                            getString(R.string.help_text_step_counter_proc);
                    break;
                case 2:
                    info = helpType.equals(HELP_DESCRIPTION) ? getString(R.string.help_text_articles):
                            getString(R.string.help_text_articles_proc);
                    break;
                case 3:
                    info = helpType.equals(HELP_DESCRIPTION) ? getString(R.string.help_text_statistics):
                            getString(R.string.help_text_statistics_proc);
                    break;
                case 4:
                    info = helpType.equals(HELP_DESCRIPTION) ? getString(R.string.help_text_settings):
                            getString(R.string.help_text_settings_proc);
                    break;
            }
            map.put("itemName", info);
            childDataItemList.add(map);
            childDataList.add(childDataItemList);
        }

        String[] childFrom = new String[]{ "itemName" };
        int[] childTo = new int[] { R.id.textViewHelpItem };

        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                getActivity(), groupDataList,
                android.R.layout.simple_expandable_list_item_1, groupFrom, groupTo, childDataList,
                R.layout.list_adapter_help_item, childFrom, childTo
        );

        ExpandableListView listView = (ExpandableListView) view.findViewById(R.id.expandableListViewHelp);
        listView.setAdapter(adapter);
    }


}
