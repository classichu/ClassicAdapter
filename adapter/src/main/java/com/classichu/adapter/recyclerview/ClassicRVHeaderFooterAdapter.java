package com.classichu.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.classichu.adapter.listener.OnNotFastClickListener;
import com.classichu.adapter.widget.ClassicEmptyView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louisgeek on 2017/3/6.
 */

public abstract class ClassicRVHeaderFooterAdapter<D> extends ClassicRecyclerViewAdapter<D> {

    private static final String TAG = "ClassicRVHeaderFooterAd";

    private int mItemLayoutId;

    private static final int VIEW_TYPE_HEADER_OFFSET = 10000;
    private static final int VIEW_TYPE_FOOTER_OFFSET = 20000;
    private static final int VIEW_TYPE_EMPTY = 1992;

    private SparseArray<View> mHeaderViews = new SparseArray<>();
    private SparseArray<View> mFooterViews = new SparseArray<>();
    private View mEmptyView;

    protected List<D> mDataList = new ArrayList<>();

    public ClassicRVHeaderFooterAdapter(List<D> mDataList, int mItemLayoutId) {
        super(mDataList, mItemLayoutId);
        this.mItemLayoutId=mItemLayoutId;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: viewType:" + viewType);
        //headerView和footerView的viewType不采用常规的方式判断，采用是否
        // header 类型
        View headerView = mHeaderViews.get(viewType);
        // footer 类型
        View footerView = mFooterViews.get(viewType);
        if (headerView != null) {
            return new ClassicRVHeaderFooterViewHolder(headerView);
        } else if (footerView != null) {
            return new ClassicRVHeaderFooterViewHolder(footerView);
        } else if (viewType == VIEW_TYPE_EMPTY) {
            // empty 类型
            return new ClassicRVHeaderFooterViewHolder(mEmptyView);
        } else {
            // normal item 类型
            View view = LayoutInflater.from(parent.getContext()).inflate(mItemLayoutId, parent, false);
            return new ClassicRVHeaderFooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: position:" + position);
        int itemType = this.getItemViewType(position);
        // header 类型
        View headerView = mHeaderViews.get(itemType);
        // footer 类型
        View footerView = mFooterViews.get(itemType);
        if (headerView != null || footerView != null) {
            return;
        }
        if (itemType == VIEW_TYPE_EMPTY) {
            return;
        }
        final int realPosition = getRealPosition(position);
        //### final T data = mDataList.get(realPosition);
        if (setupDelegate() != null) {
            setupDelegate().onBindViewHolder(holder, realPosition);
        }

        if (holder instanceof ClassicRVHeaderFooterViewHolder) {
            //抽象方法
            this.findBindView(realPosition, (ClassicRVHeaderFooterViewHolder) holder);

            holder.itemView.setOnClickListener(new OnNotFastClickListener() {
                @Override
                protected void onNotFastClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, realPosition);
                    }
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemClickListener != null) {
                        return onItemClickListener.onItemLongClick(v, realPosition);
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int count = mDataList.size();
        if (count == 0) {
            count = 1;//empty view
        }
        if (mHeaderViews.size() > 0) {
            count = count + mHeaderViews.size();
        }
        if (mFooterViews.size() > 0) {
            count = count + mFooterViews.size();
        }
        Log.d(TAG, "getItemCount: count:" + count);
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        // return super.getItemViewType(position);
        if (mHeaderViews.size() > 0 && position < mHeaderViews.size()) {
            return mHeaderViews.keyAt(position);//返回存入的key作为view type
        } else if (mFooterViews.size() > 0 && position >= getFooterFirstPosition()) {
            return mFooterViews.keyAt(position - getFooterFirstPosition());//返回存入的key作为view type
        } else if (mEmptyView != null && mDataList.size() == 0) {
            return VIEW_TYPE_EMPTY;
        }
        return setupDelegate().getItemViewType(getRealPosition(position));
    }

    /**
     * ========================private==========================
     */
    private int getRealPosition(int position) {
        int realPos = position;
        if (mHeaderViews.size() > 0) {
            realPos = position - mHeaderViews.size();
        }
        Log.d(TAG, "getRealPosition: realPos:" + realPos);
        return realPos;
    }

    private int getFooterFirstPosition() {
        int footerFirstPos = mDataList.size();
        if (mHeaderViews.size() > 0) {
            footerFirstPos = footerFirstPos + mHeaderViews.size();
        }
        if (mEmptyView != null && mDataList.size() == 0) {
            footerFirstPos = footerFirstPos +1;//add empty view
        }
        Log.d(TAG, "getFooterFirstPosition: footerFirstPos:" + footerFirstPos);
        return footerFirstPos;
    }

    /**
     * ========================public==========================
     */
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
        notifyDataSetChanged();
    }

    public void setEmptyView(ClassicEmptyView classicEmptyView) {
        mEmptyView = classicEmptyView;
        notifyDataSetChanged();
    }

    public void addHeaderView(View view) {
        int index4headerkey = VIEW_TYPE_HEADER_OFFSET + mHeaderViews.size();//每次add mHeaderViews.size()会加1 所以可以充当index
        mHeaderViews.put(index4headerkey, view);
        notifyDataSetChanged();
    }

    public void addFooterView(View view) {
        int index4footerkey = VIEW_TYPE_FOOTER_OFFSET + mFooterViews.size();//每次add mFooterViews.size()会加1 所以可以充当index
        mFooterViews.put(index4footerkey, view);
        notifyDataSetChanged();
    }

    /**
     * =====================public abstract=============================
     */
    public abstract RVHeaderFooterAdapterDelegate setupDelegate();

    public abstract void findBindView(int realPosition, ClassicRVHeaderFooterViewHolder classicRVHeaderFooterViewHolder);

    //public abstract class RVHeaderFooterAdapterDelegate mRVHeaderFooterAdapterDelegate;
    public interface RVHeaderFooterAdapterDelegate {
        void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int realPosition);

        int getItemViewType(int realPosition);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public abstract static class OnItemClickListener {
        public void onItemClick(View itemView, int position) {

        }

        public boolean onItemLongClick(View itemView, int position) {
            return false;
        }
    }

    private OnItemClickListener onItemClickListener;
}
