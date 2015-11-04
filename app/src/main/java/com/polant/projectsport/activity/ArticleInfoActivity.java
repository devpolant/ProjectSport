package com.polant.projectsport.activity;


import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.polant.projectsport.R;
import com.polant.projectsport.ThemeSettings;
import com.polant.projectsport.data.Database;
import com.polant.projectsport.data.model.Article;

/**
 * Данная Активити используется для отображения одной, выбранной пользователем из списка, статьи.
 */
public class ArticleInfoActivity extends AppCompatActivity {

    public static final String ARTICLE_ID = "ARTICLE_ID";

    private static final int LAYOUT = R.layout.activity_article_info;
//    private static final int LAYOUT = R.layout.tmp;

    private Database DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeSettings.setCurrentTheme(this, PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        DB = new Database(this);
        DB.open();

        initToolbar();
        initInfoViews();
    }

    //Сама информация выбранной статьи.
    private void initInfoViews() {
        long idArticle = getIntent().getLongExtra(ARTICLE_ID, 0);
        Article article = DB.getArticle((int) idArticle);
        if (article != null){
            setArticleTitle(article.getTitle());

            TextView text = (TextView) findViewById(R.id.textViewArticleText);
            text.setText(article.getText());

            //Категорию статьи отображаю в toolbar.
            CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
            toolBarLayout.setTitle(article.getCategory());
        }
    }

    private void setArticleTitle(String title){
        TextView titleTextView = (TextView) findViewById(R.id.textViewArticleTitle);
        titleTextView.setText(title);
    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
