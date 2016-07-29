package com.fancy.recycler_plugin.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.fancy.recycler_plugin.R;
import com.fancy.recycler_plugin.adapter.HeaderAndFooterAdapter;
import com.fancy.recycler_plugin.adapter.LoadMoreAdapter;
import com.fancy.recycler_plugin.view.HeaderFragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.transform.Transformer;

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

    public RecyclerPlugin(Context context,RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        mContext = context;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
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

    public RecyclerPlugin createBannerHeader(LayoutInflater inflater, List localImages) {
        headerAndFooterAdapter = new HeaderAndFooterAdapter(adapter);

        header = inflater.inflate(R.layout.header_banner, null);
        ConvenientBanner banner = (ConvenientBanner) header.findViewById(R.id.convenientBanner);
        banner.setPages(
                new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, localImages)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .setScrollDuration(2000);

        headerAndFooterAdapter.addHeaderView(header);
        lastAdapter = headerAndFooterAdapter;
        return this;
    }

    public RecyclerPlugin createHeader(View v) {
        headerAndFooterAdapter = new HeaderAndFooterAdapter(adapter);
        header = v;
        headerAndFooterAdapter.addHeaderView(header);
        lastAdapter = headerAndFooterAdapter;

        return this;
    }

    //TODO 需添加弱引用
    public RecyclerPlugin createHeader(LayoutInflater inflater, int resid) {
        headerAndFooterAdapter = new HeaderAndFooterAdapter(adapter);
        header = inflater.inflate(resid, null);
        headerAndFooterAdapter.addHeaderView(header);
        lastAdapter = headerAndFooterAdapter;

        return this;
    }

    public RecyclerPlugin createAddMore(LayoutInflater inflater, final LoadMoreAdapter.OnLoadMoreListener listener) {
        if (headerAndFooterAdapter != null) {
            loadMoreAdapter = new LoadMoreAdapter(headerAndFooterAdapter);
            footer = inflater.inflate(R.layout.default_loading, null);
//            loadMoreAdapter.setLoadMoreView(R.layout.default_loading);
            loadMoreAdapter.setLoadMoreView(footer);
            loadMoreAdapter.setOnLoadMoreListener(listener);

//            recyclerView.setAdapter(loadMoreAdapter);
            lastAdapter = loadMoreAdapter;
        } else {
            loadMoreAdapter = new LoadMoreAdapter(adapter);
            footer = inflater.inflate(R.layout.default_loading, null);
            View spaceLine = footer.findViewById(R.id.footer_divider);
            loadMoreAdapter.setLoadMoreView(R.layout.default_loading);
            loadMoreAdapter.setOnLoadMoreListener(listener);

            recyclerView.setAdapter(loadMoreAdapter);
            lastAdapter = loadMoreAdapter;
        }
        return this;
    }

    public RecyclerPlugin createAddMore(LayoutInflater inflater, boolean initVisible, final LoadMoreAdapter.OnLoadMoreListener listener) {
        if (headerAndFooterAdapter != null) {
            loadMoreAdapter = new LoadMoreAdapter(headerAndFooterAdapter);
            footer = inflater.inflate(R.layout.default_loading, null);
//            loadMoreAdapter.setLoadMoreView(R.layout.default_loading);
            if (initVisible)
                loadMoreAdapter.setLoadMoreView(footer);
            loadMoreAdapter.setOnLoadMoreListener(listener);

//            recyclerView.setAdapter(loadMoreAdapter);
            lastAdapter = loadMoreAdapter;
        } else {
            loadMoreAdapter = new LoadMoreAdapter(adapter);
            footer = inflater.inflate(R.layout.default_loading, null);
            View spaceLine = footer.findViewById(R.id.footer_divider);
            loadMoreAdapter.setLoadMoreView(R.layout.default_loading);
            loadMoreAdapter.setOnLoadMoreListener(listener);
            recyclerView.setAdapter(loadMoreAdapter);
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
                if (footer != null) {
                    loadMoreAdapter.setLoadMoreVisible(true);
                } else {
                    loadMoreAdapter.setLoadMoreView(footer);
                    loadMoreAdapter.notifyDataSetChanged();
                }
                loadMoreAdapter.addData();

            } else {
                hasFooter = false;
                loadMoreAdapter.removeLoadMoreView();
            }
        }
    }

    public boolean getHasFooter() {
        return hasFooter;
    }


    class LocalImageHolderView implements Holder<Integer> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, Integer data) {

            imageView.setImageResource(data);

        }
    }

}
