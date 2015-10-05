package com.polant.projectsport.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Антон on 05.10.2015.
 */
public class MyContentProvider extends ContentProvider {

    public static final Uri CONTENT_URI = Uri.parse("content://com.polant.projectsport.data/information");


    public static final int ALL_ROWS = 1;
    public static final int SINGLE_ROW = 2;

    public static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.polant.projectsport.data", "information", ALL_ROWS);
        uriMatcher.addURI("com.polant.projectsport.data", "information/#", SINGLE_ROW);
    }


    @Override
    public boolean onCreate() {
        return false;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri))
        {
            case ALL_ROWS:
                return "vnd.android.cursor.dir/vnd.polant.information";
            case SINGLE_ROW:
                return "vnd.android.cursor.item/vnd.polant.information";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
