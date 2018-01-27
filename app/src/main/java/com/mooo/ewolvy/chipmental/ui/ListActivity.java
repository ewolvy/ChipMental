package com.mooo.ewolvy.chipmental.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mooo.ewolvy.chipmental.R;
import com.mooo.ewolvy.chipmental.adapter.ChipAdapter;
import com.mooo.ewolvy.chipmental.model.ChipData;
import com.mooo.ewolvy.chipmental.model.ListItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements ChipAdapter.ItemClickCallback{

    private static final String LIMITING = "LIMITING";
    private static final String GROWING = "GROWING";
    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private static final String POSITION = "POSITION";
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;

    public static ChipAdapter adapter;
    public static ArrayList<ListItem> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listData = (ArrayList<ListItem>) ChipData.getListData(this);

        RecyclerView recView = findViewById(R.id.rec_list);
        recView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ChipAdapter(listData, this);
        recView.setAdapter(adapter);

        adapter.setItemClickCallback(this);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(recView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.list_new:
                Intent intent = new Intent(ListActivity.this, EditActivity.class);
                Bundle extras = new Bundle();
                extras.putString(LIMITING, "");
                extras.putString(GROWING, "");
                extras.putInt(POSITION, 0);

                intent.putExtra(BUNDLE_EXTRAS, extras);

                startActivity(intent);
                return true;
            case R.id.list_backup:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }else {
                    doBackup();
                }
                return true;
            case R.id.list_restore:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }else {
                    doRestore();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ChipData.saveListData(listData, this);
    }

    private ItemTouchHelper.Callback createHelperCallback(){
        return(
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
                });
    }

    private void moveItem (int oldPos, int newPos){
        ListItem itemMoved = listData.get(oldPos);
        ListItem itemSwitched = listData.get(newPos);
        itemMoved.setPosition(newPos);
        itemSwitched.setPosition(oldPos);
        listData.remove(oldPos);
        listData.add(newPos, itemMoved);
        adapter.notifyItemMoved(oldPos, newPos);
    }

    private void deleteItem (final int pos){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.delete_confirmation_title))
                .setMessage(getString(R.string.delete_confirmation_message))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listData.remove(pos);
                        adapter.notifyItemRemoved(pos);
                    }

                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        adapter.notifyDataSetChanged();
                    }
                })
                .show();
    }

    @Override
    public void onItemClick(View v, int p) {
        ListItem item = listData.get(p);

        Intent i = new Intent(this, EditActivity.class);

        Bundle extras = new Bundle();
        extras.putString(LIMITING, item.getLimiting());
        extras.putString(GROWING, item.getGrowing());
        extras.putInt(POSITION, p);

        i.putExtra(BUNDLE_EXTRAS, extras);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Fade(Fade.IN));
            getWindow().setExitTransition(new Fade(Fade.OUT));
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    new Pair<>(v.findViewById(R.id.txt_limiting),
                            getString(R.string.transition_limiting)),
                    new Pair<>(v.findViewById(R.id.txt_growing),
                            getString(R.string.transition_growing))
            );

            ActivityCompat.startActivity(this, i, options.toBundle());
        } else {
            startActivity(i);
        }
    }

    private void doBackup(){
        final String PACKAGE_NAME = "com.mooo.ewolvy.chipmental";
        final String DATABASE_NAME = "Thoughts.db";

        File DATABASE_DIRECTORY = new File(Environment.getExternalStorageDirectory(),"ChipMental");
        final File DATA_DIRECTORY_DATABASE =
                new File(Environment.getDataDirectory() +
                        "/data/" + PACKAGE_NAME +
                        "/databases/" + DATABASE_NAME );
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)){
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.no_media_message), Toast.LENGTH_LONG);
            toast.show();
        }else{
            File file = new File(DATABASE_DIRECTORY, DATABASE_NAME);

            if (!DATABASE_DIRECTORY.exists()) {
                if (!DATABASE_DIRECTORY.mkdirs()){
                    Toast toast = Toast.makeText(this, getString(R.string.no_dir_message), Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
            }

            try {
                if (file.exists()){
                    file.delete();
                }
                if (!file.createNewFile()){
                    Toast toast = Toast.makeText(this, getString(R.string.no_file_message), Toast.LENGTH_LONG);
                    toast.show();
                }else {
                    copyFile(DATA_DIRECTORY_DATABASE, file);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void doRestore(){
        final String PACKAGE_NAME = "com.mooo.ewolvy.chipmental";
        final String DATABASE_NAME = "Thoughts.db";

        File DATABASE_DIRECTORY = new File(Environment.getExternalStorageDirectory(),"ChipMental");

        File exportFile = new File(Environment.getDataDirectory() +
                "/data/" + PACKAGE_NAME +
                "/databases/" + DATABASE_NAME );;

        File importFile = new File(DATABASE_DIRECTORY,"Thoughts.db");;

        if (!importFile.exists()) {
            return;
        }

        try {
            if (exportFile.delete()) {
                if (!exportFile.createNewFile()){
                    Toast toast = Toast.makeText(this, getString(R.string.no_restore_message), Toast.LENGTH_LONG);
                    toast.show();
                }
            }
            copyFile(importFile, exportFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        listData.clear();
        listData = (ArrayList<ListItem>) ChipData.getListData(this);
        RecyclerView recView = findViewById(R.id.rec_list);
        recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = null;
        adapter = new ChipAdapter(listData, this);
        recView.setAdapter(adapter);

        adapter.setItemClickCallback(this);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(recView);
    }

    private static void copyFile(File src, File dst) throws IOException {
        try (FileChannel inChannel = new FileInputStream(src).getChannel(); FileChannel outChannel = new FileOutputStream(dst).getChannel()) {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doBackup();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                }
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doRestore();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                }
        }
    }
}
