package com.mooo.ewolvy.chipmental.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ChipData {

    public static List<ListItem> getListData(Context context){
        List<ListItem> data = new ArrayList<>();

        ThoughtsDBHelper dbHelper = new ThoughtsDBHelper(context);
        Cursor dbData = getDatabaseData(dbHelper);

        while (dbData.moveToNext()){
            ListItem item = new ListItem();
            item.setLimiting(dbData.getString(dbData.getColumnIndex(ThoughtsDBContract.Thoughts.COLUMN_LIMITING)));
            item.setGrowing(dbData.getString(dbData.getColumnIndex(ThoughtsDBContract.Thoughts.COLUMN_GROWING)));
            item.setPosition(dbData.getInt(dbData.getColumnIndex(ThoughtsDBContract.Thoughts.COLUMN_POSITION)));
            data.add(item);
        }
        dbData.close();

        return data;
    }

    public static void saveListData (List<ListItem> data, Context context){
        ThoughtsDBHelper dbHelper = new ThoughtsDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(ThoughtsDBContract.Thoughts.TABLE_NAME, null, null);
        for(int i = 0; i < data.size(); i++){
            ContentValues values = new ContentValues();
            values.put(ThoughtsDBContract.Thoughts.COLUMN_LIMITING, data.get(i).getLimiting());
            values.put(ThoughtsDBContract.Thoughts.COLUMN_GROWING, data.get(i).getGrowing());
            values.put(ThoughtsDBContract.Thoughts.COLUMN_POSITION, data.get(i).getPosition());
            db.insert(ThoughtsDBContract.Thoughts.TABLE_NAME, null, values);
        }
    }

    private static Cursor getDatabaseData(ThoughtsDBHelper dbHelper){
        String[] projection = {
                ThoughtsDBContract.Thoughts.COLUMN_LIMITING,
                ThoughtsDBContract.Thoughts.COLUMN_GROWING,
                ThoughtsDBContract.Thoughts.COLUMN_POSITION
        };

        String orderBy = ThoughtsDBContract.Thoughts.COLUMN_POSITION;

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        return db.query(
                ThoughtsDBContract.Thoughts.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                orderBy
        );
    }
}
