package com.fancy.plugin_lib.plugin;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.fancy.plugin_lib.R;
import com.fancy.plugin_lib.listener.LoadMoreListener;
import com.fancy.plugin_lib.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by sunflowerseat on 2016/7/19.
 */
public class ListPlugin {
    private Context mContext;
    public SwipeRefreshLayout refresh;
    public View footer;
    public View header;
    public ListView listView;
    public BaseAdapter adapter;
    boolean hasFooter = true;
    public boolean nowRequest = false;

    public ListPlugin(Context context, ListView listView, BaseAdapter adapter) {
        mContext = context;
        this.listView = listView;
        this.adapter = adapter;
    }

    public ListPlugin createRefresh(SwipeRefreshLayout v) {
        refresh = v;
        return this;
    }


    public ListPlugin createViewpagerHeader(FragmentManager manager, final Fragment... fragments) {

        return this;
    }

    public ListPlugin createHeader(LayoutInflater inflater, int resid) {
        header = inflater.inflate(resid, null);
        listView.addHeaderView(header);
        return this;
    }

    public ListPlugin createBannerHeader(LayoutInflater inflater, List<Integer> localImages) {

        header = inflater.inflate(R.layout.header_banner, null);
        BGABanner banner = (BGABanner) header.findViewById(R.id.banner);
        banner.setAdapter(new BGABanner.Adapter() {
            @Override
            public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
//                Glide.with(banner.getContext()).load(model).dontAnimate().thumbnail(0.1f).into((ImageView) view);
                ImageView imageView = (ImageView) view;
                imageView.setImageResource((Integer) model);
            }
        });
        List<String> texts = new ArrayList<>();
        for (Integer localImage : localImages) {
            texts.add("提示文案");
        }
        banner.setData(localImages, texts);
        header = banner;
        listView.addHeaderView(header);
        return this;
    }

    public ListPlugin addSwipe(SwipeLayout swipeLayout) {
        SwipeLayout.addSwipeView(swipeLayout);
        if (refresh != null) {
            swipeLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_MOVE:
                            refresh.setEnabled(false);
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            refresh.setEnabled(true);
                            break;
                    }
                    return false;
                }
            });

        }

        return this;
    }

    public ListPlugin deleteSwipe(SwipeLayout swipeLayout) {
        SwipeLayout.removeSwipeView(swipeLayout);
        return this;
    }


    public ListPlugin createAddMore(LayoutInflater inflater, final LoadMoreListener listener) {
        footer = inflater.inflate(R.layout.default_loading, null);
        listView.addFooterView(footer);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (view.getLastVisiblePosition() == view.getCount()-1) {
                    if (!nowRequest) {
                        nowRequest = true;
                        listener.onLoadMore();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


        return this;
    }

    public View getAddMoreView() {
        return footer;
    }

    public void setAddMoreVisible(boolean visible) {
        if (footer != null) {
            if (visible) {
                listView.addFooterView(footer);
            } else {
                listView.removeFooterView(footer);
            }
        }
    }

    public boolean getHasFooter() {
        return hasFooter;
    }

}
