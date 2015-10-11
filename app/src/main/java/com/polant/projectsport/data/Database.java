package com.polant.projectsport.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.polant.projectsport.data.model.SpecificFood;
import com.polant.projectsport.data.model.UserParametersInfo;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Антон on 05.10.2015.
 */
public class Database {

    Context context;

    SportOpenHelper openHelper;
    SQLiteDatabase sqLiteDatabase;

    public Database(Context _context) {
        context = _context;
    }

    public void open(){
        openHelper = new SportOpenHelper(context);
        sqLiteDatabase = openHelper.getWritableDatabase();
    }

    public void close(){
        if (openHelper != null) openHelper.close();
    }

    public SQLiteDatabase getSqLiteDatabase(){
        return sqLiteDatabase;
    }

    //Категории пищи. Работает верно.
    public Cursor getFoodData(){
        String[] projection = new String[] {ID_FOOD + " AS " + "_id", FOOD_CATEGORY};
        return sqLiteDatabase.query(TABLE_FOOD, projection, null, null, null, null, null);
    }

    //Конкретная еда выбранной категории. Работает верно.
    public Cursor getSpecificFoodData(String category){
        String query = "SELECT " + ID_SPECIFIC_FOOD + ", " + FOOD_NAME + ", " +
                FOOD_CATEGORY + ", " + CAL_COUNT +
                " FROM " + TABLE_FOOD + ", " + TABLE_SPECIFIC_FOOD + " " +
                "WHERE " + TABLE_FOOD + "." + ID_FOOD + "="
                         + TABLE_SPECIFIC_FOOD + "." + ID_FOOD +
                " AND " + FOOD_CATEGORY + "='" + category + "';";
        return sqLiteDatabase.rawQuery(query, null);
    }

    public Cursor getTodayFoodStatistics(){

        Calendar calendar = Calendar.getInstance();
        //Но если надо будет использовать это в CursorAdapter-е, то надо для поля ID_STATISTICS обозначить псевдоним _id.
        String query = "SELECT " +
                    ID_STATISTICS + " AS _id, " +
                    //TABLE_SPECIFIC_FOOD + "." + ID_SPECIFIC_FOOD + ", " +
                    FOOD_CATEGORY + ", " +
                    FOOD_NAME + ", " +
                    CAL_COUNT + ", " +
                    DELTA + ", " +
                    DAY + ", " +
                    MONTH + ", " +
                    YEAR +
                " FROM " + TABLE_FOOD + ", " + TABLE_SPECIFIC_FOOD + ", " + TABLE_STATISTICS +
                " WHERE " + TABLE_FOOD + "." + ID_FOOD + "="
                         + TABLE_SPECIFIC_FOOD + "." + ID_FOOD +
                    " AND " + TABLE_STATISTICS + "." + ID_SPECIFIC_FOOD + "="
                         + TABLE_SPECIFIC_FOOD + "." + ID_SPECIFIC_FOOD +
                    " AND " + DAY + "=" + calendar.get(Calendar.DATE) +
                    " AND " + MONTH + "=" + calendar.get(Calendar.MONTH) +
                    " AND " + YEAR + "=" + calendar.get(Calendar.YEAR) + ";";
        return sqLiteDatabase.rawQuery(query, null);
    }

    //добавляю пищу в базу.
    public void addSpecificFood(SpecificFood food, float delta){
        ContentValues cv = new ContentValues();

        //Использую для получения сегодняшней даты.
        Calendar calendar = Calendar.getInstance();

        cv.put(ID_SPECIFIC_FOOD, food.getIdSpecificFood());
        cv.put(DELTA, delta);
        cv.put(DAY, calendar.get(Calendar.DATE));
        cv.put(MONTH, calendar.get(Calendar.MONTH));
        cv.put(YEAR, calendar.get(Calendar.YEAR));

        sqLiteDatabase.insert(TABLE_STATISTICS, null, cv);
    }


    //Рассчитано на одного пользователя.
    public UserParametersInfo getUserParametersInfo(){
        String[] projection = new String[] {
                USER_NAME, USER_WEIGHT, USER_HEIGHT, USER_SEX
        };
        //Получаю данные одного единственного на данный момент юзера.
        Cursor c = sqLiteDatabase.query(TABLE_USER, projection, null, null, null, null, null);
        c.moveToFirst();

        float weight = c.getFloat(c.getColumnIndex(USER_WEIGHT));
        float height = c.getFloat(c.getColumnIndex(USER_HEIGHT));
        String sex = c.getString(c.getColumnIndex(USER_SEX));
        String name = c.getString(c.getColumnIndex(USER_NAME));

        c.close();

        return new UserParametersInfo(name, weight, height, sex);
    }

    public void setUserParametersInfo(UserParametersInfo user){
        ContentValues cv = new ContentValues();
        cv.put(USER_WEIGHT, user.getWeight());
        cv.put(USER_HEIGHT, user.getHeight());
        cv.put(USER_SEX, user.getSex());

        String where = USER_NAME + "=?";
        String[] whereArgs = new String[] { user.getName() };

        sqLiteDatabase.update(TABLE_USER, cv, where, whereArgs);
    }


    public static int getDatabaseVersion() {
        return SportOpenHelper.DATABASE_VERSION;
    }

    //----------------------------------------------------------//

    //все таблицы.
    public static final String TABLE_USER = "USER_TABLE";
    public static final String TABLE_STATISTICS = "STATISTICS_TABLE";
    public static final String TABLE_SPECIFIC_FOOD = "SPECIFIC_FOOD_TABLE";
    public static final String TABLE_FOOD = "FOOD_TABLE";


    //все столбцы всех таблиц.
    //USER
    public static final String ID_USER = "ID_USER";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_WEIGHT = "USER_WEIGHT";
    public static final String USER_HEIGHT = "USER_HEIGHT";
    public static final String USER_SEX = "USER_SEX";

    //FOOD
    public static final String ID_FOOD = "food_id";
    public static final String FOOD_CATEGORY = "FOOD_CATEGORY";

    //SPECIFIC_FOOD
    public static final String ID_SPECIFIC_FOOD = "_id";
    public static final String FOOD_NAME = "FOOD_NAME";
    //+ в таблице есть ID_FOOD как внешний ключ.
    public static final String CAL_COUNT = "CAL_COUNT";

    //STATISTICS
    public static final String ID_STATISTICS = "ID_STATISTICS";
    ////+ в таблице есть ID_SPECIFIC_FOOD как внешний ключ.
    public static final String DELTA = "DELTA";//или вместо разницы ("DELTA") хранить вес пищи?
    public static final String DAY = "DAY";
    public static final String MONTH = "MONTH";
    public static final String YEAR = "YEAR";

//----------------------------------------------------------//


    private static class SportOpenHelper extends SQLiteOpenHelper {

        private static final String LOG = SportOpenHelper.class.getName();

        private static final int DATABASE_VERSION = 12;

        private static final String DATABASE_NAME = "sport.db";


        //В данных момент я не связываю таблицу TABLE_USER с другими таблицами.
        private static final String CREATE_TABLE_USER = "Create table " + TABLE_USER + " (" +
                ID_USER + " integer primary key autoincrement, " +
                USER_NAME + " TEXT, " +
                USER_HEIGHT + " FLOAT, " +
                USER_WEIGHT + " FLOAT, " +
                USER_SEX + " TEXT);";

        private static final String CREATE_TABLE_FOOD =
                "Create table " + TABLE_FOOD + " (" +
                        ID_FOOD + " integer primary key autoincrement, " +
                        FOOD_CATEGORY + " TEXT);";

        private static final String CREATE_TABLE_SPECIFIC_FOOD =
                "Create table " + TABLE_SPECIFIC_FOOD + " (" +
                        ID_SPECIFIC_FOOD + " integer primary key autoincrement, " +
                        FOOD_NAME + " TEXT, " +
                        ID_FOOD + " INTEGER, " +
                        CAL_COUNT + " INTEGER);";

        private static final String CREATE_TABLE_STATISTICS =
                "Create table " + TABLE_STATISTICS + " (" +
                        ID_STATISTICS + " integer primary key autoincrement, " +
                        //ID_USER + " INTEGER, " +
                        ID_SPECIFIC_FOOD + " INTEGER, " +
                        DELTA + " FLOAT, " + //или вместо разницы ("DELTA") хранить вес пищи?
                        DAY + " INTEGER, " +
                        MONTH + " INTEGER, " +
                        YEAR + " INTEGER);";

        //конструктор.
        public SportOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG, "----- Create database  -----");

            db.execSQL(CREATE_TABLE_USER);
            db.execSQL(CREATE_TABLE_FOOD);
            db.execSQL(CREATE_TABLE_SPECIFIC_FOOD);
            db.execSQL(CREATE_TABLE_STATISTICS);

            ContentValues cv = new ContentValues();
            cv.put(USER_NAME, "Антон");
            cv.put(USER_HEIGHT, 184);
            cv.put(USER_WEIGHT, 75);
            cv.put(USER_SEX, "M");
            db.insert(TABLE_USER, null, cv);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(LOG, "Update database from " + oldVersion + " to " + newVersion + ", which will destroy all old data");

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER + ";");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD + ";");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPECIFIC_FOOD + ";");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATISTICS + ";");

            onCreate(db);
        }
    }


}
