package com.polant.projectsport.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.polant.projectsport.R;
import com.polant.projectsport.ThemeSettings;
import com.polant.projectsport.data.Database;
import com.polant.projectsport.data.model.Article;
import com.polant.projectsport.preferences.PreferencesNewActivity;
import com.polant.projectsport.preferences.PreferencesOldActivity;

/**
 * Created by Антон on 24.10.2015.
 */
public class ArticleInfoActivity extends AppCompatActivity {

    public static final String ARTICLE_ID = "ARTICLE_ID";

    private static final int LAYOUT = R.layout.activity_article_info;


    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    private Database DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeSettings.setCurrentTheme(this, PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        DB = new Database(this);
        DB.open();

        initToolbar();
        initNavigationView();
        initInfoViews();
    }

    //Сама информация выбранной статьи.
    private void initInfoViews() {
        long idArticle = getIntent().getLongExtra(ARTICLE_ID, 0);
        Article article = DB.getArticle((int) idArticle);
        if (article != null){
            TextView title = (TextView) findViewById(R.id.textViewArticleTitle);
            title.setText(article.getTitle());

            TextView text = (TextView) findViewById(R.id.textViewArticleText);
            text.setText(article.getText());

            //Категорию статьи отображаю в toolbar.
            toolbar.setTitle(article.getCategory());
        }
    }


    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        //toolbar.setTitle(R.string.app_name);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
    }

    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_view_open,
                R.string.navigation_view_close);

        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                drawerLayout.closeDrawers();

                switch (item.getItemId()) {
                    case R.id.actionArticleItem:
                        //Возвращаемся назад на вызвавшую Активити.
                        Intent articles = new Intent();
                        setResult(RESULT_OK, articles);
                        finish();
                        break;
                    case R.id.actionSettingsItem:
                        //добавим совместимость со старыми версиями платформы.
                        Class c = Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ?
                                PreferencesOldActivity.class : PreferencesNewActivity.class;

                        Intent intent = new Intent(ArticleInfoActivity.this, c);
                        Log.d("Class in intent", c.getName());
                        startActivityForResult(intent, PreferencesNewActivity.SHOW_PREFERENCES);
                        break;
                }

                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        updateFromPreferences(sp);
    }

    //Применение настроек приложения.
    private void updateFromPreferences(SharedPreferences sp){
        //Применяю тему.
        ThemeSettings.setUpdatedTheme(this, sp);
    }
}
