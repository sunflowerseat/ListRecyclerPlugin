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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        listview = (ListView) findViewById(R.id.listview);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        initData();

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
        plugin = new ListPlugin(this,listview, mAdapter);
        /** 添加代码 创建Header*/
        plugin.createHeader(getLayoutInflater(), R.layout.headview);
        /** 添加代码 创建加载更多视图*/
        plugin.createAddMore(getLayoutInflater() ,this);

        listview.setAdapter(mAdapter);

        String ss = null;
        if (ss != null) {

        }
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
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    addData();
                    mAdapter.notifyDataSetChanged();
                    plugin.nowRequest = false;
                }
            }, 3000);
    }
}
