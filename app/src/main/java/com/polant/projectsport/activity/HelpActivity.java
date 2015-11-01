package com.polant.projectsport.activity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import com.polant.projectsport.R;
import com.polant.projectsport.ThemeSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Активити помощи пользователю.
 */
public class HelpActivity extends AppCompatActivity{

    private static final int LAYOUT = R.layout.activity_help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeSettings.setCurrentTheme(this, PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        initToolbar();
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
                    info = getString(R.string.help_text_calculators);
                    break;
                case 1:
                    info = getString(R.string.help_text_step_counter);
                    break;
                case 2:
                    info = getString(R.string.help_text_articles);
                    break;
                case 3:
                    info = getString(R.string.help_text_statistics);
                    break;
                case 4:
                    info = getString(R.string.help_text_settings);
                    break;
            }
            map.put("itemName", info);
            childDataItemList.add(map);
            childDataList.add(childDataItemList);
        }

        String[] childFrom = new String[]{ "itemName" };
        int[] childTo = new int[] { R.id.textViewHelpItem };

        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                this, groupDataList,
                android.R.layout.simple_expandable_list_item_1, groupFrom, groupTo, childDataList,
                R.layout.list_adapter_help_item, childFrom, childTo
        );

        ExpandableListView listView = (ExpandableListView) findViewById(R.id.expandableListViewHelp);
        listView.setAdapter(adapter);
    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.help_activity_title);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        //Кнопка назад на toolbar-е.
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

}
