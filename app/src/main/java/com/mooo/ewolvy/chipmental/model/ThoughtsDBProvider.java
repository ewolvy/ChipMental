package com.mooo.ewolvy.chipmental.model;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import com.mooo.ewolvy.chipmental.model.ThoughtsDBContract.Thoughts;

public class ThoughtsDBProvider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = ThoughtsDBProvider.class.getSimpleName();

    /** URI matcher code for the content URI for the pets table */
    private static final int THOUGHTS = 100;

    /** URI matcher code for the content URI for a single pet in the pets table */
    private static final int THOUGHT_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // The content URI of the form "content://com.example.android.pets/pets" will map to the
        // integer code {@link #PETS}. This URI is used to provide access to MULTIPLE rows
        // of the pets table.
        sUriMatcher.addURI(ThoughtsDBContract.CONTENT_AUTHORITY,
                ThoughtsDBContract.PATH_THOUGHTS,
                THOUGHTS);

        // The content URI of the form "content://com.mooo.ewolvy.chipmental/thoughts/#" will map to
        // the integer code {@link #THOUGHT_ID}. This URI is used to provide access to ONE single
        // row of the thoughts table.
        //
        // In this case, the "#" wildcard is used where "#" can be substituted for an integer.
        // For example, "content://com.mooo.ewolvy.chipmental/thoughts/3" matches, but
        // "content://com.mooo.ewolvy.chipmental/thoughts" (without a number at the end) doesn't
        // match.
        sUriMatcher.addURI(ThoughtsDBContract.CONTENT_AUTHORITY,
                ThoughtsDBContract.PATH_THOUGHTS + "/#",
                THOUGHT_ID);
    }

    /** Database helper object */
    private ThoughtsDBHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new ThoughtsDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case THOUGHTS:
                // For the THOUGHTS code, query the thoughts table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the thoughts table.
                cursor = database.query(Thoughts.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case THOUGHT_ID:
                // For the THOUGHT_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.mooo.ewolvy.chipmental/thoughts/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = Thoughts._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the thoughts table where the _id equals 3 to return
                // a Cursor containing that row of the table.
                cursor = database.query(Thoughts.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor
        return cursor;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case THOUGHTS:
                return insertThought(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a thought into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertThought(Uri uri, ContentValues values) {

        // Check that the limiting thought is not null
        String limiting = values.getAsString(Thoughts.COLUMN_GROWING);
        if (limiting == null) {
            throw new IllegalArgumentException("limiting thought required");
        }

        // Check that the growing thought is not null
        String growing = values.getAsString(Thoughts.COLUMN_GROWING);
        if (growing == null) {
            throw new IllegalArgumentException("Growing thought required");
        }

        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new thought with the given values
        long id = database.insert(Thoughts.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the data has changed for the pet content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case THOUGHTS:
                return updateThought(uri, contentValues, selection, selectionArgs);
            case THOUGHT_ID:
                // For the THOUGHT_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = Thoughts._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateThought(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update thoughts in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more thoughts).
     * Return the number of rows that were successfully updated.
     */
    private int updateThought(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link Thoughts#COLUMN_LIMITING} key is present,
        // check that the limiting thought value is not null.
        if (values.containsKey(Thoughts.COLUMN_LIMITING)) {
            String limiting = values.getAsString(Thoughts.COLUMN_LIMITING);
            if (limiting == null) {
                throw new IllegalArgumentException("Limiting thought required");
            }
        }

        // If the {@link Thoughts#COLUMN_GROWING} key is present,
        // check that the growing thought value is valid.
        if (values.containsKey(Thoughts.COLUMN_GROWING)) {
            String growing = values.getAsString(Thoughts.COLUMN_GROWING);
            if (growing == null) {
                throw new IllegalArgumentException("Growing thought required");
            }
        }

        // If the {@link Thoughts#COLUMN_POSITION} key is present,
        // check that the weight value is valid.
        if (values.containsKey(Thoughts.COLUMN_POSITION)) {
            // Check that the position is greater than or equal to 0
            Integer position = values.getAsInteger(Thoughts.COLUMN_POSITION);
            if (position != null && position < 0) {
                throw new IllegalArgumentException("Position required");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(Thoughts.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case THOUGHTS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(Thoughts.TABLE_NAME, selection, selectionArgs);
                break;
            case THOUGHT_ID:
                // Delete a single row given by the ID in the URI
                selection = Thoughts._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(Thoughts.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case THOUGHTS:
                return Thoughts.CONTENT_LIST_TYPE;
            case THOUGHT_ID:
                return Thoughts.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
