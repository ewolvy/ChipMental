package com.mooo.ewolvy.chipmental.ui;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.Fade;
import android.view.View;

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

        adapter = new ChipAdapter(listData, this);
        recView.setAdapter(adapter);

        adapter.setItemClickCallback(this);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(recView);
    }

    private ItemTouchHelper.Callback createHelperCallback(){
        ItemTouchHelper.SimpleCallback simpleCallback =
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                        return true;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        deleteItem(viewHolder.getAdapterPosition());
                    }
                };
        return simpleCallback;
    }

    private void moveItem (int oldPos, int newPos){
        ListItem item = (ListItem) listData.get(oldPos);
        listData.remove(oldPos);
        listData.add(newPos, item);
        adapter.notifyItemMoved(oldPos, newPos);
    }

    private void deleteItem (final int pos){
        listData.remove(pos);
        adapter.notifyItemRemoved(pos);
    }

    @Override
    public void onItemClick(View v, int p) {
        ListItem item = (ListItem) listData.get(p);

        Intent i = new Intent(this, EditActivity.class);

        Bundle extras = new Bundle();
        extras.putString(LIMITING, item.getLimiting());
        extras.putString(GROWING, item.getGrowing());

        i.putExtra(BUNDLE_EXTRAS, extras);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Fade(Fade.IN));
            getWindow().setExitTransition(new Fade(Fade.OUT));
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    new Pair<View, String>(v.findViewById(R.id.txt_limitating),
                            getString(R.string.transition_limiting)),
                    new Pair <View, String>(v.findViewById(R.id.txt_growing),
                            getString(R.string.transition_growing))
            );

            ActivityCompat.startActivity(this, i, options.toBundle());
        } else {
            startActivity(i);
        }
    }
}
