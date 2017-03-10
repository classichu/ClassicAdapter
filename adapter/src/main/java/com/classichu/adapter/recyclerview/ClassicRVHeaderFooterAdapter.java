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

public abstract class ClassicRVHeaderFooterAdapter<D> extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "ClassicRVHeaderFooterAd";


    private static final int VIEW_TYPE_HEADER_OFFSET = 10000;
    private static final int VIEW_TYPE_FOOTER_OFFSET = 20000;
    private static final int VIEW_TYPE_EMPTY = 1992;

    private SparseArray<View> mHeaderViews = new SparseArray<>();
    private SparseArray<View> mFooterViews = new SparseArray<>();
    private View mEmptyView;

    protected List<D> mDataList = new ArrayList<>();
    private int mItemLayoutId;

    public ClassicRVHeaderFooterAdapter(List<D> mDataList, int mItemLayoutId) {
        this.mDataList = mDataList;
        this.mItemLayoutId = mItemLayoutId;
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
            mEmptyView.setVisibility(View.VISIBLE);
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
        int viewType=0;
        if (setupDelegate() != null) {
            viewType=setupDelegate().getItemViewType(getRealPosition(position));
        }
        return viewType;
    }

    /**
     * ===========================start=========================================
     */
    public List<D> getDataList() {
        return mDataList;
    }

    public D getData(int position) {
        return mDataList.get(position);
    }


    public void refreshDataList(List<D> dataList) {
        mDataList.clear();
        if (dataList != null) {
            mDataList.addAll(dataList);
        }
        notifyDataSetChanged();
    }

    public void clearDataList() {
        mDataList.clear();
        notifyDataSetChanged();
    }


    public void addDataListAtStart(List<D> dataList) {
        if (dataList != null) {
            mDataList.addAll(0, dataList);
          /*  if (mHeaderView != null) {
                notifyItemRangeInserted(0 + 1, dataList.size());
            } else*/
            {
                notifyItemRangeInserted(0, dataList.size());
            }
        }
    }

    public void addDataListAtEnd(List<D> dataList) {
        if (dataList != null) {
            int positionStart = mDataList.size();
            mDataList.addAll(dataList);
          /*  if (mHeaderView != null) {
                notifyItemRangeInserted(positionStart + 1, dataList.size());
            } else */
            {
                notifyItemRangeInserted(positionStart, dataList.size());
            }
        }
    }

    public void addData(int position, D data) {
        mDataList.add(position, data);
       /* if (mHeaderView != null) {
            notifyItemInserted(position + 1);
        } else*/
        {
            notifyItemInserted(position);
        }
    }

    public void addDataAtStart(D data) {
        mDataList.add(0, data);
      /*  if (mHeaderView != null) {
            notifyItemInserted(0 + 1);
        } else */
        {
            notifyItemInserted(0);
        }
    }

    public void addDataAtEnd(D data) {
        int positionStart = mDataList.size();
        mDataList.add(data);
       /* if (mHeaderView != null) {
            notifyItemInserted(positionStart + 1);
        } else */
        {
            notifyItemInserted(positionStart);
        }
    }

    public void removeData(int position) {
        int positionStart = mDataList.size();
        mDataList.remove(position);
       /* if (mHeaderView != null) {
            notifyItemRemoved(positionStart + 1);
        } else */
        {
            notifyItemRemoved(positionStart);
        }
    }

    public void removeData(D data) {
        this.removeData(mDataList.indexOf(data));
    }


    public void replaceData(int locationPos, D data) {
        mDataList.set(locationPos, data);
        /*if (mHeaderView != null) {
            notifyItemChanged(locationPos + 1);
        } else */
        {
            notifyItemChanged(locationPos);
        }
    }

    public void replaceData(D oldData, D data) {
        this.replaceData(mDataList.indexOf(oldData), data);
    }

    /**
     * ===========================end========================================
     */
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
