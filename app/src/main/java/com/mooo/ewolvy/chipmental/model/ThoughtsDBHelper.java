package com.mooo.ewolvy.chipmental.model;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ThoughtsDBHelper extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Thoughts.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ThoughtsDBContract.Thoughts.TABLE_NAME + " (" +
                    ThoughtsDBContract.Thoughts._ID + " INTEGER PRIMARY KEY," +
                    ThoughtsDBContract.Thoughts.COLUMN_LIMITING + " TEXT," +
                    ThoughtsDBContract.Thoughts.COLUMN_GROWING + " TEXT," +
                    ThoughtsDBContract.Thoughts.COLUMN_POSITION + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ThoughtsDBContract.Thoughts.TABLE_NAME;


    public ThoughtsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This is the first versino, so there is nothing to upgrade
    }
}
