package com.mooo.ewolvy.chipmental.model;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class ThoughtsDBContract {

    // To avoid instantiating this class we create an empty constructor
    private ThoughtsDBContract(){}

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.mooo.ewolvy.chipmental";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.mooo.ewolvy.chipmental/thoughts/ is a valid path for
     * looking at pet data. content://com.mooo.ewolvy.chipmental/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_THOUGHTS = "thoughts";

    // Inner class that defines the table used
    public static final class Thoughts implements BaseColumns{

        /** The content URI to access the pet data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_THOUGHTS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of thoughts.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_THOUGHTS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single thought.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_THOUGHTS;

        /** Name of database table for thoughts */
        public static final String TABLE_NAME = "Thoughts";

        /**
         * Unique ID number for the thought (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Limiting thought
         *
         * Type: TEXT
         */
        public static final String COLUMN_LIMITING = "Limiting";

        /**
         * Growing thought
         *
         * Type: TEXT
         */
        public static final String COLUMN_GROWING = "Growing";

        /**
         * Position on the list
         *
         * Type: INTEGER
         */
        public static final String COLUMN_POSITION = "Position";
    }
}
