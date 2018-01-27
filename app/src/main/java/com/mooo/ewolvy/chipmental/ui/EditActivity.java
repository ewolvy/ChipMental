package com.mooo.ewolvy.chipmental.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.mooo.ewolvy.chipmental.R;
import com.mooo.ewolvy.chipmental.model.ListItem;

import java.util.Objects;

public class EditActivity extends AppCompatActivity {

    private static final String LIMITING = "LIMITING";
    private static final String GROWING = "GROWING";
    private static final String POSITION = "POSITION";
    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";

    private boolean mThoughtHasChanged = false;
    private boolean mNewThought;
    private int mModifiedItem;

    private EditText limitingTextView;
    private EditText growingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        limitingTextView = (EditText) findViewById(R.id.edit_limiting);
        growingTextView = (EditText) findViewById(R.id.edit_growing);

        Bundle extras = getIntent().getBundleExtra(BUNDLE_EXTRAS);

        if (!Objects.equals(extras.getString(LIMITING), "") &&
                !Objects.equals(extras.getString(GROWING), "")) {
            limitingTextView.setText(extras.getString(LIMITING));
            growingTextView.setText(extras.getString(GROWING));
            mModifiedItem = extras.getInt(POSITION);
            mNewThought = false;
        }else{
            mNewThought = true;
        }

        limitingTextView.setOnTouchListener(mTouchListener);
        growingTextView.setOnTouchListener(mTouchListener);
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                view.performClick();
            }
            mThoughtHasChanged = true;
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.edit_save:
                saveThought();
                finish();
                return true;
            case android.R.id.home:
                if (!mThoughtHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed(){
        if (!mThoughtHasChanged){
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the thought.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void saveThought(){
        ListItem newItem = new ListItem();
        newItem.setLimiting(limitingTextView.getText().toString());
        newItem.setGrowing(growingTextView.getText().toString());
        if (mNewThought){
            newItem.setPosition(ListActivity.listData.size() + 1);
            ListActivity.listData.add(newItem);
        }else{
            ListItem oldItem = ListActivity.listData.get(mModifiedItem);
            newItem.setPosition(oldItem.getPosition());
            ListActivity.listData.set(mModifiedItem, newItem);
        }
        ListActivity.adapter.notifyDataSetChanged();
    }
}