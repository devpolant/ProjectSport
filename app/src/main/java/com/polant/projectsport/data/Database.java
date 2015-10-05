package com.polant.projectsport.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

    //Категории пищи. Работаетв верно.
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
    ////+ в таблице есть FOOD_NAME как внешний ключ.
    public static final String DAY = "DAY";
    public static final String MONTH = "MONTH";
    public static final String YEAR = "YEAR";

//----------------------------------------------------------//


    private static class SportOpenHelper extends SQLiteOpenHelper {

        private static final String LOG = SportOpenHelper.class.getName();

        private static final int DATABASE_VERSION = 7;

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
                        DAY + " TEXT, " +
                        MONTH + " TEXT, " +
                        YEAR + " TEXT);";

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
