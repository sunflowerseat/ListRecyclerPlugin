package com.fancy.recycler_plugin.plugin;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.fancy.recycler_plugin.R;
import com.fancy.recycler_plugin.adapter.HeaderAndFooterAdapter;
import com.fancy.recycler_plugin.adapter.LoadMoreAdapter;
import com.fancy.recycler_plugin.swipe.SwipeLayout;

/**
 * Created by sunflowerseat on 2016/7/19.
 */
public class RecyclerPlugin {
    private Context mContext;
    public SwipeRefreshLayout refresh;
    public View footer;
    public View header;
    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    public HeaderAndFooterAdapter headerAndFooterAdapter;
    public LoadMoreAdapter loadMoreAdapter;
    public RecyclerView.Adapter lastAdapter;
    boolean hasFooter = true;

    public LayoutInflater inflater;

    public RecyclerPlugin(LayoutInflater inflater,Context context, RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        mContext = context;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.inflater = inflater;
    }

    public RecyclerPlugin createRefresh(SwipeRefreshLayout v) {
        refresh = v;
        return this;
    }


    public RecyclerPlugin createViewpagerHeader(FragmentManager manager, final Fragment... fragments) {
        headerAndFooterAdapter = new HeaderAndFooterAdapter(adapter);

        header = LayoutInflater.from(mContext).inflate(R.layout.header_viewpager, null);
        ViewPager viewPager = (ViewPager) header.findViewById(R.id.vp);
        viewPager.setAdapter(new FragmentPagerAdapter(manager) {
            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }
        });

        headerAndFooterAdapter.addHeaderView(header);
        lastAdapter = headerAndFooterAdapter;

        return this;
    }



    public RecyclerPlugin addHeader(View view) {
        if (headerAndFooterAdapter == null)
            headerAndFooterAdapter = new HeaderAndFooterAdapter(adapter);
        headerAndFooterAdapter.addHeaderView(view);
        lastAdapter = headerAndFooterAdapter;
        return this;
    }

    public void setNowRequest(boolean nowRequest) {
        loadMoreAdapter.nowRequest = nowRequest;
    }

    public RecyclerPlugin createHeader(View v) {
        headerAndFooterAdapter = new HeaderAndFooterAdapter(adapter);
        header = v;
        headerAndFooterAdapter.addHeaderView(header);
        lastAdapter = headerAndFooterAdapter;

        return this;
    }

    public void setHasMoreData(boolean flag) {
        if (loadMoreAdapter != null) {
            loadMoreAdapter.setHasMoreData(flag);
        }
    }

    public RecyclerPlugin createHeader(int resid) {
        headerAndFooterAdapter = new HeaderAndFooterAdapter(adapter);
        header = inflater.inflate(resid, null);
        headerAndFooterAdapter.addHeaderView(header);
        lastAdapter = headerAndFooterAdapter;

        return this;
    }

    public RecyclerPlugin addSwipe(SwipeLayout swipeLayout) {
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

    public RecyclerPlugin deleteSwipe(SwipeLayout swipeLayout) {
        SwipeLayout.removeSwipeView(swipeLayout);
        return this;
    }

    public RecyclerPlugin createAddMore(final LoadMoreAdapter.OnLoadMoreListener listener) {
        if (headerAndFooterAdapter != null) {
            loadMoreAdapter = new LoadMoreAdapter(headerAndFooterAdapter);
            footer = inflater.inflate(R.layout.default_loading, null);
            loadMoreAdapter.setLoadMoreView(footer);
            loadMoreAdapter.setOnLoadMoreListener(listener);

            lastAdapter = loadMoreAdapter;
        } else {
            loadMoreAdapter = new LoadMoreAdapter(adapter);
            footer = inflater.inflate(R.layout.default_loading, null);
            View spaceLine = footer.findViewById(R.id.footer_divider);
            loadMoreAdapter.setLoadMoreView(R.layout.default_loading);
            loadMoreAdapter.setOnLoadMoreListener(listener);
            lastAdapter = loadMoreAdapter;
        }
        return this;
    }

    public RecyclerPlugin createAddMore(final LoadMoreAdapter.OnLoadMoreListener listener,@LayoutRes int resId) {
        if (headerAndFooterAdapter != null) {
            loadMoreAdapter = new LoadMoreAdapter(headerAndFooterAdapter);
            footer = inflater.inflate(resId, null);
            loadMoreAdapter.setLoadMoreView(footer);
            loadMoreAdapter.setOnLoadMoreListener(listener);

            lastAdapter = loadMoreAdapter;
        } else {
            loadMoreAdapter = new LoadMoreAdapter(adapter);
            footer = inflater.inflate(resId, null);
            loadMoreAdapter.setLoadMoreView(footer);
            loadMoreAdapter.setOnLoadMoreListener(listener);
            lastAdapter = loadMoreAdapter;
        }
        return this;
    }


    //initVisible表示 是否显示addMore视图
    public RecyclerPlugin createAddMore(boolean initVisible, final LoadMoreAdapter.OnLoadMoreListener listener) {
        if (headerAndFooterAdapter != null) {
            loadMoreAdapter = new LoadMoreAdapter(headerAndFooterAdapter);
            if (initVisible) {
                footer = inflater.inflate(R.layout.default_loading, null);
                loadMoreAdapter.setLoadMoreView(footer);
            }

            loadMoreAdapter.setOnLoadMoreListener(listener);
            lastAdapter = loadMoreAdapter;
        } else {
            loadMoreAdapter = new LoadMoreAdapter(adapter);
            footer = inflater.inflate(R.layout.default_loading, null);
            if (initVisible) {
                loadMoreAdapter.setLoadMoreView(R.layout.default_loading);
                loadMoreAdapter.setOnLoadMoreListener(listener);
            }

            lastAdapter = loadMoreAdapter;
        }
        return this;
    }

    public HeaderAndFooterAdapter getHeaderAndFooterAdapter() {
        return headerAndFooterAdapter;
    }


    public LoadMoreAdapter getLoadMoreAdapter() {
        return loadMoreAdapter;
    }

    public RecyclerView.Adapter getLastAdapter() {
        return lastAdapter;
    }

    public View getAddMoreView() {
        return footer;
    }

    public void setAddMoreVisible(boolean visible) {
        if (loadMoreAdapter != null) {
            if (visible) {
                hasFooter = true;
                loadMoreAdapter.setLoadMoreView(footer);
                loadMoreAdapter.setLoadMoreVisible(true);
                loadMoreAdapter.notifyDataSetChanged();
            } else {
                hasFooter = false;
                Log.d("RecyclerPlugin", "haha"+loadMoreAdapter.getmLoadMoreView());
                loadMoreAdapter.setLoadMoreVisible(false);
                loadMoreAdapter.setLoadMoreView(null);
                loadMoreAdapter.removeLoadMoreView();
                loadMoreAdapter.notifyDataSetChanged();
            }
        }
    }

    public void setAddMoreVisible(boolean visible,final LoadMoreAdapter.OnLoadMoreListener listener,int resId) {
        if (loadMoreAdapter != null) {
            if (visible) {
                Log.d("RecyclerPlugin", "setAddMoreVisible");
                hasFooter = true;
                footer = inflater.inflate(resId, null);
                loadMoreAdapter.setLoadMoreView(footer);
                loadMoreAdapter.setLoadMoreVisible(true);
                loadMoreAdapter.setOnLoadMoreListener(listener);
            } else {
                hasFooter = false;
                loadMoreAdapter.setLoadMoreVisible(false);
                loadMoreAdapter.setLoadMoreView(null);
                loadMoreAdapter.setOnLoadMoreListener(listener);
                loadMoreAdapter.removeLoadMoreView();
            }
            loadMoreAdapter.notifyDataSetChanged();
        }
    }

    public boolean getHasFooter() {
        return hasFooter;
    }

    public void setNoMoreView(@LayoutRes int resId) {
        if (loadMoreAdapter != null) {
            loadMoreAdapter.setNoMoreView(inflater, resId);
        }
    }


}
