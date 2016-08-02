package com.fancy.listrecyclerplugin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.fancy.listrecyclerplugin.common.CommonAdapter;
import com.fancy.listrecyclerplugin.common.ViewHolder;
import com.fancy.plugin_lib.listener.LoadMoreListener;
import com.fancy.plugin_lib.plugin.ListPlugin;

import java.util.ArrayList;
import java.util.List;

public class ListViewActivity extends AppCompatActivity implements LoadMoreListener{

    ListView listview;
    CommonAdapter<String> mAdapter;

    List<String> mDatas = new ArrayList<>();
    ArrayList<Integer> localImages = new ArrayList<Integer>();

    ListPlugin plugin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        listview = (ListView) findViewById(R.id.listview);
        initData();

        mAdapter = new CommonAdapter<String>(ListViewActivity.this,mDatas,R.layout.item_list) {
            @Override
            public void convert(ViewHolder holder, String s) {
                holder.setText(R.id.id_item_list_title, s);
            }

        };

        listview.setAdapter(mAdapter);
        plugin = new ListPlugin(this,listview, mAdapter);

        plugin.createHeader(getLayoutInflater(), R.layout.headview);
        plugin.createAddMore(getLayoutInflater() ,this);

    }


    void initData() {
        for (int i = 0; i < 10; i++) {
            mDatas.add("position:"+i);
        }

        localImages.add(R.drawable.i1);
        localImages.add(R.drawable.i2);
        localImages.add(R.drawable.i3);
    }

    void addData() {
        for (int i = 0; i < 10; i++) {
            mDatas.add("Add-position:"+i);
        }
    }


    @Override
    public void onLoadMore() {
        addData();
        mAdapter.notifyDataSetChanged();
    }
}
