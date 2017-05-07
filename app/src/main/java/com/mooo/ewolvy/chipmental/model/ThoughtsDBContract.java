package com.mooo.ewolvy.chipmental.model;


import android.provider.BaseColumns;

public final class ThoughtsDBContract {

    // To avoid instantiating this class we create an empty constructor
    private ThoughtsDBContract(){}

    // Inner class that defines the table used
    public static final class Thoughts implements BaseColumns{
        public static final String TABLE_NAME = "Thoughts";
        public static final String COLUMN_LIMITING = "Limiting";
        public static final String COLUMN_GROWING = "Growing";
        public static final String COLUMN_POSITION = "Position";
    }
}
