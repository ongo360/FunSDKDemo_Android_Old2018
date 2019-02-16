package com.example.common;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.funsdkdemo.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Modify by niutong at 2017-11-25
 *
 * @author niutong
 * @date 2017-11-25
 * Discription:
 */

public class FileManagerActivity extends ListActivity implements View.OnClickListener {

    private ImageButton buttonBack;
    private TextView textTitle;
    private SimpleAdapter mFileAdapter;
    private List<Map<String, Object>> mapList = new ArrayList<>();
    private File fileShowNow;
    private boolean setResult = false;
    private Intent resultIntent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filemanager);
        buttonBack = (ImageButton) findViewById(R.id.backBtnInTopLayout);
        textTitle = (TextView) findViewById(R.id.textViewInTopLayout);

        buttonBack.setOnClickListener(this);

        initData();
    }

    private void initData() {
        fileShowNow = Environment.getExternalStorageDirectory();
        textTitle.setText(fileShowNow.getPath());
        mFileAdapter = new SimpleAdapter(this, getPathList(fileShowNow), R.layout.item_filemanageradapter,
                new String[]{"name", "image"}, new int[]{R.id.item_file_manager_text, R.id.item_file_manager_image});

        this.setListAdapter(mFileAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        final File nearlier = getFileClicked(position);
        if (nearlier == null) {
            return;
        }
        if (nearlier.isDirectory()) {
            getPathList(nearlier);
            mFileAdapter.notifyDataSetInvalidated();
        }else {
            new AlertDialog.Builder(this)
                    .setTitle("ensure to update ?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setResult = true;
                            resultIntent.putExtra("path", nearlier.getPath());
                            finish();
                        }
                    })
                    .show();
        }
    }

    private File getFileClicked(int position){
        for (File file : fileShowNow.listFiles()) {
            if (file.getName().equals(mapList.get(position).get("name"))) {
                return file;
            }
        }
        return null;
    }

    @Override
    public void finish() {
        if (setResult) {
            this.setResult(RESULT_OK, resultIntent);
        }else {
            this.setResult(RESULT_CANCELED);
        }
        super.finish();
    }

    private List getPathList(File file) {
        fileShowNow = file;
        textTitle.setText(file.getPath());
        mapList.clear();
        File[] files = file.listFiles();
        if (files.length <= 0) {
            return mapList;
        }
        for (File file1 : files) {
            Map<String, Object> map = new HashMap<String, Object>();
            String name = file1.getName();
            if (file1.isDirectory()) {
                map.put("name", name);
                map.put("image", R.drawable.xm_ui_lib_folder);
                mapList.add(map);
            }else if (name.endsWith(".bin")){
                map.put("name", name);
                map.put("image", R.drawable.xm_ui_lib_file);
                mapList.add(map);
            }
        }
        return mapList;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtnInTopLayout:
                File[] mfile = fileShowNow.getParentFile().listFiles();
                if (mfile != null && mfile.length > 0) {
                    getPathList(fileShowNow.getParentFile());
                    mFileAdapter.notifyDataSetInvalidated();
                }else {
                    finish();
                }
                break;
            default:
                break;
        }
    }
}
