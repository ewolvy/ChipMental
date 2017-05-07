package com.mooo.ewolvy.chipmental.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mooo.ewolvy.chipmental.R;
import com.mooo.ewolvy.chipmental.adapter.ChipAdapter;
import com.mooo.ewolvy.chipmental.model.ChipData;
import com.mooo.ewolvy.chipmental.model.ListItem;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements ChipAdapter.ItemClickCallback{

    private static final String LIMITING = "LIMITING";
    private static final String GROWING = "GROWING";
    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private RecyclerView recView;
    private ChipAdapter adapter;
    private ArrayList listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listData = (ArrayList) ChipData.getListData();

        recView = (RecyclerView) findViewById(R.id.rec_list);
        recView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ChipAdapter(ChipData.getListData(), this);
        recView.setAdapter(adapter);

        adapter.setItemClickCallback(this);
    }

    @Override
    public void onItemClick(int p) {
        ListItem item = (ListItem) listData.get(p);

        Intent i = new Intent(this, EditActivity.class);

        Bundle extras = new Bundle();
        extras.putString(LIMITING, item.getLimiting());
        extras.putString(GROWING, item.getGrowing());

        i.putExtra(BUNDLE_EXTRAS, extras);
        startActivity(i);
    }
}
