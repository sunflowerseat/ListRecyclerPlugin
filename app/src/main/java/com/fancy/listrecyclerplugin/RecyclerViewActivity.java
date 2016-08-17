package com.fancy.listrecyclerplugin;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fancy.listrecyclerplugin.helper.Divider;
import com.fancy.recycler_plugin.adapter.LoadMoreAdapter;
import com.fancy.recycler_plugin.plugin.RecyclerPlugin;
import com.fancy.recycler_plugin.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity implements LoadMoreAdapter.OnLoadMoreListener{
    RecyclerView recycler;
    RecyclerView.Adapter mAdapter;
    RecyclerPlugin plugin;
    List<String> mDatas = new ArrayList<>();
    ArrayList<Integer> localImages = new ArrayList<Integer>();
    RelativeLayout rl_progressbar;

    int recylerTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        initData();
        recycler = (RecyclerView) findViewById(R.id.recycler);
        rl_progressbar = (RelativeLayout) findViewById(R.id.rl_progressbar);
        recycler.setLayoutManager(new GridLayoutManager(this, 1));
        recycler.addItemDecoration(new Divider(this));
        mAdapter = new MyAdapter();



        /** 添加代码 创建一个RecylerPlugin*/
        plugin = new RecyclerPlugin(getLayoutInflater(),this,recycler, mAdapter);
        /** 添加代码 创建Header*/
        plugin.createHeader(R.layout.headview);
        /** 添加代码 创建加载更多视图*/
        plugin.createAddMore(false,null);
        plugin.setNoMoreView(R.layout.nomore_loading);
        /**设置加载更多视图不可见 当数据不足一屏时，可调用该方法*/
//        plugin.setAddMoreVisible(false);

        recycler.setAdapter(plugin.getLastAdapter());

    }

    void initData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int dataLength = 2;
                for (int i = 0; i < dataLength; i++) {
                    mDatas.add("position:" + i);
                }

                localImages.add(R.drawable.i1);
                localImages.add(R.drawable.i2);
                localImages.add(R.drawable.i3);
                rl_progressbar.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();
                if (dataLength < 10) {
                    //初始化数据小于10条
                    plugin.setAddMoreVisible(true,null, R.layout.default_loading);
                    plugin.setHasMoreData(false);
                } else {
                    plugin.setAddMoreVisible(true,RecyclerViewActivity.this, R.layout.default_loading);
                }
            }
        }, 1000);

    }

    int addData() {
        if (++recylerTime < 2) {
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
    public void onLoadMoreRequested() {
        if(plugin.getHasFooter())
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    int addDataLength = addData();
                    if (addDataLength < 10) {
                        mAdapter.notifyDataSetChanged();
                        plugin.setNowRequest(false);
                        plugin.loadMoreAdapter.setOnLoadMoreListener(null);
                        plugin.setHasMoreData(false);
                    } else {
                        mAdapter.notifyDataSetChanged();
                        plugin.setNowRequest(false);
                    }

                }
            }, 1000);
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>
    {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    RecyclerViewActivity.this).inflate(R.layout.item_recycler, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position)
        {
            holder.tv.setText(mDatas.get(position) + ":" +position);
            holder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), mDatas.get(position), Toast.LENGTH_SHORT).show();
                }
            });
            /** 添加代码 */
            plugin.addSwipe(holder.swipeLayout);
            holder.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatas.remove(position);
                    mAdapter.notifyDataSetChanged();
                    /** 添加代码 */
                    plugin.deleteSwipe(holder.swipeLayout);
                }
            });

        }

        @Override
        public int getItemCount()
        {
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {

            TextView tv;
            TextView menu;
            SwipeLayout swipeLayout;

            public MyViewHolder(View view)
            {
                super(view);
                tv = (TextView) view.findViewById(R.id.id_item_list_title);
                swipeLayout = (SwipeLayout) view.findViewById(R.id.swipe);
                menu = (TextView) view.findViewById(R.id.swipemenu);
            }
        }
    }
}
