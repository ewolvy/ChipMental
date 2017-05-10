package com.mooo.ewolvy.chipmental.model;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

final class ThoughtsDBContract {

    // To avoid instantiating this class we create an empty constructor
    private ThoughtsDBContract(){}

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.mooo.ewolvy.chipmental/thoughts/ is a valid path for
     * looking at pet data. content://com.mooo.ewolvy.chipmental/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_THOUGHTS = "thoughts";

    // Inner class that defines the table used
    static final class Thoughts implements BaseColumns{

        /** Name of database table for thoughts */
        static final String TABLE_NAME = "Thoughts";

        /**
         * Unique ID number for the thought (only for use in the database table).
         *
         * Type: INTEGER
         */
        final static String _ID = BaseColumns._ID;

        /**
         * Limiting thought
         *
         * Type: TEXT
         */
        static final String COLUMN_LIMITING = "Limiting";

        /**
         * Growing thought
         *
         * Type: TEXT
         */
        static final String COLUMN_GROWING = "Growing";

        /**
         * Position on the list
         *
         * Type: INTEGER
         */
        static final String COLUMN_POSITION = "Position";
    }
}
