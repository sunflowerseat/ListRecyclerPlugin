package com.fancy.plugin_lib.plugin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.fancy.plugin_lib.R;
import com.fancy.plugin_lib.listener.LoadMoreListener;
import com.fancy.plugin_lib.swipe.SwipeLayout;

/**
 * Created by sunflowerseat on 2016/7/19.
 */
public class ListPlugin {
    private Context mContext;
    public View refresh;
    public View footer;
    public View header;
    public ListView listView;
    public BaseAdapter adapter;
    boolean hasFooter = true;
    public boolean nowRequest = false;
    public LayoutInflater inflater;
    LoadMoreListener listener;

    public ListPlugin(LayoutInflater inflater, Context context, ListView listView, BaseAdapter adapter) {
        mContext = context;
        this.listView = listView;
        this.adapter = adapter;
        this.inflater = inflater;
    }

    public ListPlugin createRefresh(View v) {
        refresh = v;
        return this;
    }


    public ListPlugin createHeader(int resid) {
        header = inflater.inflate(resid, null);
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


    public ListPlugin createAddMore(final LoadMoreListener l) {
        listener = l;
        footer = inflater.inflate(R.layout.default_loading, null);
        listView.addFooterView(footer);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (view.getLastVisiblePosition() == view.getCount() - 1) {
                    if (!nowRequest) {
                        nowRequest = true;
                        if(listener != null)
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

    public ListPlugin createAddMore(boolean initVisible, int resid,final LoadMoreListener l) {
        listener = l;
        footer = inflater.inflate(resid, null);
        if (initVisible) {
            listView.addFooterView(footer);
        }
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (view.getLastVisiblePosition() == view.getCount() - 1) {
                    if (!nowRequest) {
                        nowRequest = true;
                        if(listener != null)
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

    public ListPlugin createAddMore(boolean initVisible, final LoadMoreListener l) {
        listener = l;
        footer = inflater.inflate(R.layout.default_loading, null);
        if (initVisible) {
            listView.addFooterView(footer);
        }
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (view.getLastVisiblePosition() == view.getCount() - 1) {
                    if (!nowRequest) {
                        nowRequest = true;
                        if(listener != null)
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

    public void changeAddMore(int resId) {
        listView.removeFooterView(footer);
        footer = inflater.inflate(resId, null);
        listView.addFooterView(footer);
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

    public void setOnLoadMoreListener(LoadMoreListener l) {
        this.listener = l;
    }

}
