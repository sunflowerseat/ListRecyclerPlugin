package com.fancy.listrecyclerplugin;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.fancy.listrecyclerplugin.lv_common.CommonAdapter;
import com.fancy.listrecyclerplugin.lv_common.ViewHolder;
import com.fancy.plugin_lib.listener.LoadMoreListener;
import com.fancy.plugin_lib.plugin.ListPlugin;
import com.fancy.plugin_lib.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.List;

public class ListViewActivity extends AppCompatActivity implements LoadMoreListener{

    ListView listview;
    CommonAdapter<String> mAdapter;
    SwipeRefreshLayout refresh;

    List<String> mDatas = new ArrayList<>();
    ArrayList<Integer> localImages = new ArrayList<Integer>();

    ListPlugin plugin;

    int listTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        listview = (ListView) findViewById(R.id.listview);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);

        mAdapter = new CommonAdapter<String>(ListViewActivity.this,mDatas,R.layout.item_list) {
            @Override
            public void convert(ViewHolder holder, final String s) {
                holder.setText(R.id.id_item_list_title, s);
                final SwipeLayout swipeLayout = holder.getView(R.id.swipe);
                plugin.addSwipe(swipeLayout);
                holder.setOnClickListener(R.id.swipemenu, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDatas.remove(s);
                        mAdapter.notifyDataSetChanged();
                        plugin.deleteSwipe(swipeLayout);
                    }
                });
            }

        };

        /** 添加代码 创建一个listPlugin*/
        plugin = new ListPlugin(getLayoutInflater(),this,listview, mAdapter);
        /** 添加代码 创建Header*/
        plugin.createHeader(R.layout.headview);
        /** 添加代码 创建加载更多视图*/
        plugin.createAddMore(false,this);
        initData();
        listview.setAdapter(mAdapter);

    }


    void initData() {
        int dataLength = 10;
        for (int i = 0; i < dataLength; i++) {
            mDatas.add("position:"+i);
        }
        mAdapter.notifyDataSetChanged();
        if (dataLength < 10) {
            //初始化数据小于10条
            plugin.setAddMoreVisible(true);
            plugin.setOnLoadMoreListener(null);
            plugin.changeAddMore(R.layout.nomore_loading);
        } else {
            plugin.setAddMoreVisible(true);
        }

        localImages.add(R.drawable.i1);
        localImages.add(R.drawable.i2);
        localImages.add(R.drawable.i3);
    }

    int addData() {
        if (++listTime < 2) {
            for (int i = 0; i < 10; i++) {
                mDatas.add("Add-position:" + i);
            }
            return 10;
        } else {
            for (int i = 0; i < 4; i++) {
                mDatas.add("Last-position:" + i);
            }
            return 4;
        }
    }


    @Override
    public void onLoadMore() {
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    int addDataLength = addData();
                    mAdapter.notifyDataSetChanged();
                    if (addDataLength < 10) {
                        plugin.nowRequest = false;
                        plugin.setOnLoadMoreListener(null);
                        plugin.changeAddMore(R.layout.nomore_loading);
                    } else {
                        plugin.nowRequest = false;
                    }
                }
            }, 3000);
    }
}
