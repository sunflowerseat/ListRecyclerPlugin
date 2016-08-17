package com.fancy.listrecyclerplugin;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fancy.listrecyclerplugin.helper.Divider;
import com.fancy.recycler_plugin.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 肖芳 on 2016/8/5.
 */
public class RecyclerFragment extends Fragment{
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    List<String> mDatas = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_recycler_view, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.addItemDecoration(new Divider(getContext()));

        mAdapter = new MyAdapter();
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();
    }

    void initData() {
        Log.d("RecyclerFragment", "initData");
        for (int i = 0; i < 10; i++) {
            mDatas.add("position:"+i);
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>
    {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    RecyclerFragment.this.getContext()).inflate(R.layout.item_recycler, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position)
        {
            holder.tv.setText(mDatas.get(position) + ":" +position);
            Log.d("RecyclerFragment", "onBindViewHolder");
//            if(position == 9) plugin.setAddMoreVisible(true);
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
