package com.fancy.adapterutil.lv_common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class LvCommonAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    private int layoutId;

    public LvCommonAdapter(Context context, List<T> datas, int layoutId) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mDatas = datas;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        if (mDatas == null) {
            return 0;
        }
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        if (mDatas == null) {
            return null;
        }
        if (mDatas.size() == 0) {
            return null;
        }
        if(position < mDatas.size())
        return mDatas.get(position);
        else return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public void setData(List<T> datas) {
        if(mDatas == null) mDatas = new ArrayList<>();
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public void addData(List<T> datas) {
        if(mDatas == null) mDatas = new ArrayList<>();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(mContext, convertView, parent,
                layoutId, position);
        convert(holder, getItem(position),position);

        return holder.getConvertView();
    }

    public abstract void convert(ViewHolder holder, T t,int position);


}
