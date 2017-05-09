package com.mooo.ewolvy.chipmental.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class ChipData {
    // private static final String[] limiting_thoughts = {"limit 1", "limit 2", "limit 3"};
    // private static final String[] growing_thoughts = {"grow 1", "grow 2", "grow 3"};

    public static List<ListItem> getListData(Context context){
        List<ListItem> data = new ArrayList<>();

        ThoughtsDBHelper dbHelper = new ThoughtsDBHelper(context);
        Cursor dbData = getDatabaseData(dbHelper);

        while (dbData.moveToNext()){
            ListItem item = new ListItem();
            item.setLimiting(dbData.getString(dbData.getColumnIndex(ThoughtsDBContract.Thoughts.COLUMN_LIMITING)));
            item.setGrowing(dbData.getString(dbData.getColumnIndex(ThoughtsDBContract.Thoughts.COLUMN_GROWING)));
            item.setPosition(dbData.getInt(dbData.getColumnIndex(ThoughtsDBContract.Thoughts.COLUMN_POSITION)));
        }
        dbData.close();

        /*
        // Dummy data for testing purposes
        for (int x = 0; x < 4; x++){
            for (int i = 0; i < limiting_thoughts.length && i < growing_thoughts.length; i++){
                ListItem item = new ListItem();
                item.setLimiting(limiting_thoughts[i]);
                item.setGrowing(growing_thoughts[i]);
                data.add(item);
            }
        }*/

        return data;
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
