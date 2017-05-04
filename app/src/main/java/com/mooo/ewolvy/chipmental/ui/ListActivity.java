package com.mooo.ewolvy.chipmental.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mooo.ewolvy.chipmental.R;
import com.mooo.ewolvy.chipmental.adapter.ChipAdapter;
import com.mooo.ewolvy.chipmental.model.ChipData;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recView;
    private ChipAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recView = (RecyclerView) findViewById(R.id.rec_list);
        recView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ChipAdapter(ChipData.getListData(), this);
        recView.setAdapter(adapter);
    }
}
