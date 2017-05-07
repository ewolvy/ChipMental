package com.mooo.ewolvy.chipmental.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.mooo.ewolvy.chipmental.R;

public class EditActivity extends AppCompatActivity {

    private static final String LIMITING = "LIMITING";
    private static final String GROWING = "GROWING";
    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Bundle extras = getIntent().getBundleExtra(BUNDLE_EXTRAS);

        ((TextView)findViewById(R.id.edit_limiting)).setText(extras.getString(LIMITING));
        ((TextView)findViewById(R.id.edit_growing)).setText(extras.getString(GROWING));
    }
}
