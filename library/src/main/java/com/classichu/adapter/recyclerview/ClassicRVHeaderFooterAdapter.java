package com.classichu.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.classichu.adapter.listener.OnNotFastClickListener;
import com.classichu.adapter.widget.ClassicEmptyView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louisgeek on 2017/3/6.
 */

public abstract class ClassicRVHeaderFooterAdapter<D> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "ClassicRVHeaderFooterAd";
    //分页第一页
    public static final int PAGE_NUM_DEFAULT = 1;
    //默认分页数量
    public static final int PAGE_SIZE_DEFAULT = 10;

    private static final int VIEW_TYPE_HEADER_OFFSET = 10000;
    private static final int VIEW_TYPE_FOOTER_OFFSET = 20000;
    private static final int VIEW_TYPE_EMPTY = 1992;

    private SparseArray<View> mHeaderViews = new SparseArray<>();
    private SparseArray<View> mFooterViews = new SparseArray<>();
    private View mEmptyView;

    protected List<D> mDataList = new ArrayList<>();
    private int mItemLayoutId;
    private Context mContext;

    public ClassicRVHeaderFooterAdapter(Context mContext, List<D> mDataList, int mItemLayoutId) {
        this.mDataList = mDataList;
        this.mItemLayoutId = mItemLayoutId;
        this.mContext = mContext;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        //为GridLayoutManager 合并头布局的跨度
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                /**
                 * 抽象方法  返回当前index位置的item所占用的跨度的数量
                 * ##单元格合并  就是相当于占据了设定列spanCount的数量
                 * ##不合并     就是相当于占据了原来1个跨度
                 *
                 * @param position
                 * @return
                 */
                @Override
                public int getSpanSize(int position) {
                    int spanSize=1;
                    if (mHeaderViews.size() > 0 && position < mHeaderViews.size()) {
                        spanSize=gridLayoutManager.getSpanCount();
                    } else if (mFooterViews.size() > 0 && position >= getFooterFirstPosition()) {
                        spanSize=gridLayoutManager.getSpanCount();
                    }
                    return spanSize;
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.d(TAG, "onCreateViewHolder: viewType:" + viewType);
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
        //Log.d(TAG, "onBindViewHolder: position:" + position);
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
        //Log.d(TAG, "getItemCount: count:" + count);
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
        int viewType = 0;
        if (setupDelegate() != null) {
            viewType = setupDelegate().getItemViewType(getRealPosition(position));
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
        //###Log.d(TAG, "getRealPosition: realPos:" + realPos);
        return realPos;
    }

    private int getFooterFirstPosition() {
        int footerFirstPos = mDataList.size();
        if (mHeaderViews.size() > 0) {
            footerFirstPos = footerFirstPos + mHeaderViews.size();
        }
        if (mEmptyView != null && mDataList.size() == 0) {
            footerFirstPos = footerFirstPos + 1;//add empty view
        }
        //Log.d(TAG, "getFooterFirstPosition: footerFirstPos:" + footerFirstPos);
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

    public void removeHeaderView(View view) {
        int index4header = mHeaderViews.indexOfValue(view);
        if (index4header >= 0) {
            mHeaderViews.removeAt(index4header);
            notifyDataSetChanged();
        }
    }

    public void removeFooterView(View view) {
        int index4footer = mFooterViews.indexOfValue(view);
        if (index4footer >= 0) {
            mFooterViews.removeAt(index4footer);
            notifyDataSetChanged();
        }
    }

    private TextView textView;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;

    private View setupLoadingView(Context context, String showText) {
        if (linearLayout == null || textView == null) {
            linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.setPadding(dp2px(context, 10), dp2px(context, 15)
                    , dp2px(context, 10), dp2px(context, 15));

            progressBar = new ProgressBar(context);

            linearLayout.addView(progressBar);

            textView = new TextView(context);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            // textView.setText(showText);
            textView.setPadding(dp2px(context, 10), dp2px(context, 10)
                    , dp2px(context, 10), dp2px(context, 10));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            linearLayout.addView(textView);
        }
        textView.setText(showText);

        if ("数据加载中...".equals(showText)) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
        return linearLayout;
    }

    private int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    //当前页码
    private int mPageNum = PAGE_NUM_DEFAULT;
    //是否在加载ing
    private boolean mDataLoading;
    //是否所有数据加载完毕（没有下一页）
    private boolean mLoadComplete;

    /**
     * 下一页
     *
     * @return
     */
    public int getNextPageNum() {
        return mPageNum + 1;
    }

    /**
     * 翻页
     *
     * @return
     */
    public void turnNextPageNum() {
        mPageNum = mPageNum + 1;
        //return mPageNum;
    }

    public int getPageNum() {
        return mPageNum;
    }

    //返回已加载的数量
    public int getNowPageCount() {
        return mPageNum * PAGE_SIZE_DEFAULT;
    }

    public void setDataLoading(boolean dataLoading) {
        mDataLoading = dataLoading;
    }

    public boolean isDataLoading() {
        return mDataLoading;
    }

    public void setLoadComplete(boolean loadComplete) {
        mLoadComplete = loadComplete;
    }

    public boolean isLoadComplete() {
        return mLoadComplete;
    }

    /**
     * 加载中
     */
    private View mLoadingView;

    public void showFooterViewDataLoading() {
        //
        setDataLoading(true);
        setLoadComplete(false);
        //
        removeFooterView(mLoadingView);
        mLoadingView = setupLoadingView(mContext, "数据加载中...");
        addFooterView(mLoadingView);
        // CLog.d("showFooterViewDataLoading");
        Log.d(TAG, "showFooter showFooterViewDataLoading: ");
    }


    /**
     * 上拉加载更多数据  （一页加载完成)
     */
    public void showFooterViewNormal() {
        //
        setLoadComplete(false);
        setDataLoading(false);
        //
        removeFooterView(mLoadingView);
        mLoadingView = setupLoadingView(mContext, "上拉加载更多数据");
        addFooterView(mLoadingView);
        Log.d(TAG, "showFooter showFooterViewNormal: ");
    }

    /**
     * 所有数据加载完成
     */
    public void showFooterViewLoadComplete() {
        //
        setLoadComplete(true);
        setDataLoading(false);
        //
        removeFooterView(mLoadingView);
        mLoadingView = setupLoadingView(mContext, "数据加载完成");
        addFooterView(mLoadingView);
        Log.d(TAG, "showFooter showFooterViewLoadComplete: ");
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
