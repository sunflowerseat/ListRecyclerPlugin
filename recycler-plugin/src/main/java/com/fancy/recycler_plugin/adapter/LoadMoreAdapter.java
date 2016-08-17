package com.fancy.recycler_plugin.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by sunflowerseat on 16/7/19.
 * 注：基本参照hyman的LoadMoreWapper 添加了adapter数据同步更新功能
 */
public class LoadMoreAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public static final int ITEM_TYPE_LOAD_MORE = Integer.MAX_VALUE - 2;
    public static final int ITEM_TYPE_NO_MORE = Integer.MAX_VALUE - 3;

    private RecyclerView.Adapter mInnerAdapter;
    private View mLoadMoreView;
    private int mLoadMoreLayoutId;
    private View mNoMoreView;
    public boolean nowRequest = false;
    public boolean hasMoreData = true;

    public LoadMoreAdapter(RecyclerView.Adapter adapter)
    {
        mInnerAdapter = adapter;
    }

    private boolean hasLoadMore()
    {
        return mLoadMoreView != null || mLoadMoreLayoutId != 0;
    }


    private boolean isShowLoadMore(int position)
    {
        return hasLoadMore() && (position >= mInnerAdapter.getItemCount());
    }

    @Override
    public int getItemViewType(int position)
    {
        if (isShowLoadMore(position))
        {
            if (hasMoreData) {
                return ITEM_TYPE_LOAD_MORE;
            } else {
                return ITEM_TYPE_NO_MORE;
            }

        }

        return mInnerAdapter.getItemViewType(position);
    }

    public void setHasMoreData(boolean flag) {
        hasMoreData = flag;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (viewType == ITEM_TYPE_LOAD_MORE)
        {
            Log.d("LoadMoreAdapter", "onCreateViewHolder");
            ViewHolder holder;
            if (mLoadMoreView != null)
            {
                holder = ViewHolder.createViewHolder(parent.getContext(), mLoadMoreView);
            } else
            {
                holder = ViewHolder.createViewHolder(parent.getContext(), parent, mLoadMoreLayoutId);
            }
            return holder;
        }

        if (viewType == ITEM_TYPE_NO_MORE) {
            ViewHolder holder;
            if (mLoadMoreView != null)
            {
                holder = ViewHolder.createViewHolder(parent.getContext(), mNoMoreView);
                return holder;
            }
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if (isShowLoadMore(position))
        {
            if (mOnLoadMoreListener != null)
            {
                addData();
            }
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback()
        {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position)
            {
                if (isShowLoadMore(position))
                {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null)
                {
                    return oldLookup.getSpanSize(position);
                }
                return 1;
            }
        });
    }


    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder)
    {
        mInnerAdapter.onViewAttachedToWindow(holder);

        if (isShowLoadMore(holder.getLayoutPosition()))
        {
            setFullSpan(holder);
        }
    }

    private void setFullSpan(RecyclerView.ViewHolder holder)
    {
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams)
        {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;

            p.setFullSpan(true);
        }
    }

    @Override
    public int getItemCount()
    {
        return mInnerAdapter.getItemCount() + (hasLoadMore() ? 1 : 0);
    }


    public interface OnLoadMoreListener
    {
        void onLoadMoreRequested();
    }

    private OnLoadMoreListener mOnLoadMoreListener;

    public LoadMoreAdapter setOnLoadMoreListener(OnLoadMoreListener loadMoreListener)
    {
        mOnLoadMoreListener = loadMoreListener;
        return this;
    }

    public void addData() {
        if (!nowRequest) {
            nowRequest = true;
            mOnLoadMoreListener.onLoadMoreRequested();
        }
    }

    public LoadMoreAdapter setLoadMoreView(View loadMoreView)
    {
        mLoadMoreView = loadMoreView;
        return this;
    }

    public LoadMoreAdapter setLoadMoreView(int layoutId)
    {
        mLoadMoreLayoutId = layoutId;
        return this;
    }

    public LoadMoreAdapter setLoadMoreView(LayoutInflater inflater,int layoutId)
    {
//        mLoadMoreLayoutId = layoutId;
        Log.d("LoadMoreAdapter", "setLoadMoreView");
        mLoadMoreView = inflater.inflate(layoutId, null);
        return this;
    }

    public LoadMoreAdapter setNoMoreView(LayoutInflater inflater,int layoutId)
    {
//        mLoadMoreLayoutId = layoutId;
        Log.d("LoadMoreAdapter", "setLoadMoreView");
        mNoMoreView = inflater.inflate(layoutId, null);
        return this;
    }

    public void removeLoadMoreView() {
        if (mLoadMoreView != null) {
            mLoadMoreView.setVisibility(View.GONE);
            mLoadMoreLayoutId = 0;
            mLoadMoreView = null;
        }
        notifyDataSetChanged();
    }

    public void setLoadMoreVisible(boolean flag) {
        if(mLoadMoreView != null)
            mLoadMoreView.setVisibility((flag) ? View.VISIBLE : View.GONE);
    }

    public View getmLoadMoreView() {
        return mLoadMoreView;
    }


    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        mInnerAdapter.registerAdapterDataObserver(observer);
    }
}
