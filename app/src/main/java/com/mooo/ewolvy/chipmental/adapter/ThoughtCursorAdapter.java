package com.mooo.ewolvy.chipmental.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.mooo.ewolvy.chipmental.R;
import com.mooo.ewolvy.chipmental.model.ThoughtsDBContract.Thoughts;

public class ThoughtCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link ThoughtCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public ThoughtCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the thought data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the limiting word for the current thought can be set on the
     * limiting TextView in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView limitingTextView = (TextView) view.findViewById(R.id.txt_limitating);
        TextView growingTextView = (TextView) view.findViewById(R.id.txt_growing);

        // Find the columns of thought attributes that we're interested in
        int limitingColumnIndex = cursor.getColumnIndex(Thoughts.COLUMN_LIMITING);
        int growingColumnIndex = cursor.getColumnIndex(Thoughts.COLUMN_GROWING);

        // Read the thought attributes from the Cursor for the current thought
        String thoughtLimiting = cursor.getString(limitingColumnIndex);
        String thoughtGrowing = cursor.getString(growingColumnIndex);

        // Update the TextViews with the attributes for the current pet
        limitingTextView.setText(thoughtLimiting);
        growingTextView.setText(thoughtGrowing);
    }
}
