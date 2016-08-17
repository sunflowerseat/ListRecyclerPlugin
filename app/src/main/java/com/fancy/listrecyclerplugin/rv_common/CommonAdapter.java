package com.fancy.listrecyclerplugin.rv_common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunflowerseat on 16/7/19.
 * 重新对RecyclerView.ViewHolder进行封装，但用法与原先几乎一致
 */
public abstract class CommonAdapter<T> extends RecyclerView.Adapter<ViewHolder>
{
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    public CommonAdapter(Context mContext, int mLayoutId, List<T> mDatas) {
        this.mContext = mContext;
        this.mLayoutId = mLayoutId;
        this.mDatas = mDatas;
    }


    protected abstract void convert(ViewHolder holder, T t, int position);

    public void removeData(int position) {
        if(mDatas == null) mDatas = new ArrayList<>();
        if (position < mDatas.size()) {
            mDatas.remove(position);
            notifyDataSetChanged();
        }
    }

    public void addData(List<T> datas) {
        if(mDatas == null) mDatas = new ArrayList<>();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public void refreshData(List<T> datas) {
        if(mDatas == null) mDatas = new ArrayList<>();
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        ViewHolder holder = ViewHolder.createViewHolder(mContext, parent, mLayoutId);
        return holder;
    }

    @Override
    public int getItemCount()
    {
        int itemCount = mDatas.size();
        return itemCount;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        convert(holder,mDatas.get(position),position);
    }

}
