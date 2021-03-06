package com.classichu.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
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

    private static final int VIEW_TYPE_HEADER_OFFSET = 100000;
    private static final int VIEW_TYPE_FOOTER_OFFSET = 200000;
    private static final int VIEW_TYPE_EMPTY = -1992;

    private SparseArray<View> mHeaderViews = new SparseArray<>();
    private SparseArray<View> mFooterViews = new SparseArray<>();
    private View mEmptyView;

    protected List<D> mDataList = new ArrayList<>();
    private int mItemLayoutId;
    private Context mContext;

    public ClassicRVHeaderFooterAdapter(Context mContext, int mItemLayoutId) {
        this.mItemLayoutId = mItemLayoutId;
        this.mContext = mContext;
    }

    private void configGridLayoutManagerSpanInfo(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int spanSize = 1;
                    if (mHeaderViews.size() > 0 && position < mHeaderViews.size()) {
                        spanSize = gridLayoutManager.getSpanCount();
                    } else if (mFooterViews.size() > 0 && position >= getFooterFirstPosition()) {
                        spanSize = gridLayoutManager.getSpanCount();
                    } else if (getItemViewType(position) == VIEW_TYPE_EMPTY) {
                        // empty 类型
                        spanSize = gridLayoutManager.getSpanCount();
                    }
                    return spanSize;
                }
            });
        }
    }

    private void configStaggeredGridLayoutManagerSpanInfo(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder != null && viewHolder.itemView != null && viewHolder.itemView.getLayoutParams() != null) {
            int position = viewHolder.getLayoutPosition();
            ViewGroup.LayoutParams vglp = viewHolder.itemView.getLayoutParams();
            if (vglp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams sglm_lp1 = (StaggeredGridLayoutManager.LayoutParams) vglp;
                if (mHeaderViews.size() > 0 && position < mHeaderViews.size()) {
                    sglm_lp1.setFullSpan(true);
                } else if (mFooterViews.size() > 0 && position >= getFooterFirstPosition()) {
                    sglm_lp1.setFullSpan(true);
                } else if (getItemViewType(position) == VIEW_TYPE_EMPTY) {
                    // empty 类型
                    sglm_lp1.setFullSpan(true);
                }
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        Log.i(TAG, "onAttachedToRecyclerView: ");
        configGridLayoutManagerSpanInfo(recyclerView);
    }

    /**
     * may  call  after  set  Grid  LayoutManager
     *
     * @param recyclerView
     */
    public void callAfterChangeGridLayoutManager(RecyclerView recyclerView) {
        configGridLayoutManagerSpanInfo(recyclerView);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        Log.i(TAG, "onViewAttachedToWindow: ");
        configStaggeredGridLayoutManagerSpanInfo(holder);
        configFooterViews4EmptyView();
    }

    /**
     * 如果有EmptyView不显示FooterView
     */
    private void configFooterViews4EmptyView() {
        if (mEmptyView == null) {
            return;
        }
        if (mDataList.isEmpty()) {
            hideFooterViews();
        } else {
            showFooterViews();
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
            view.setClickable(true);//让RecyclerView.SimpleOnItemTouchListener实现点击时的selector生效
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
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
        if (count == 0 && mEmptyView != null) {
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
        } else if (mEmptyView != null && mDataList.isEmpty()) {
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
    public List<D> hideHeader() {
        return mDataList;
    }

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


    public boolean addDataListAtStart(List<D> dataList) {
        boolean result = false;
        if (dataList != null) {
            result = mDataList.addAll(0, dataList);
            if (result) {
                notifyItemRangeInserted(0, dataList.size());
            }
        }
        return result;
    }

    public boolean addDataListAtEnd(List<D> dataList) {
        boolean result = false;
        if (dataList != null) {
            int positionStart = mDataList.size();
            result = mDataList.addAll(dataList);
            if (result) {
                notifyItemRangeInserted(positionStart, dataList.size());
            }
        }
        return result;
    }

    public boolean addData(int position, D data) {
        boolean result = mDataList.add(data);
        if (result) {
            notifyItemInserted(position);
        }
        return result;
    }

    public boolean addDataAtStart(D data) {
        boolean result = false;
        if (data != null) {
            result = true;
            mDataList.add(0, data);
            notifyItemInserted(0);
        }
        return result;
    }

    public boolean addDataAtEnd(D data) {
        boolean result = false;
        if (data != null) {
            result = true;
            int positionStart = mDataList.size();
            mDataList.add(data);
            notifyItemInserted(positionStart);
        }
        return result;
    }

    public boolean removeData(int position) {
        boolean result = false;
        D d = mDataList.remove(position);
        if (d != null) {
            result = true;
            notifyItemRemoved(position);
        }
        return result;
    }

    public boolean removeData(D data) {
        boolean result = false;
        int index = mDataList.indexOf(data);
        if (index > 0) {
            result = this.removeData(index);
        }
        return result;
    }


    public boolean replaceData(int locationPos, D data) {
        boolean result = false;
        if (data != null) {
            result = true;
            mDataList.set(locationPos, data);
            notifyItemChanged(locationPos);
        }
        return result;
    }

    public boolean replaceData(D oldData, D data) {
        boolean result = false;
        int index = mDataList.indexOf(oldData);
        if (index > 0) {
            result = this.replaceData(index, data);
        }
        return result;
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
        mEmptyView.setVisibility(View.GONE);
        notifyDataSetChanged();
    }

    public void setEmptyView(ClassicEmptyView classicEmptyView) {
        mEmptyView = classicEmptyView;
        mEmptyView.setVisibility(View.GONE);
        notifyDataSetChanged();
    }

    public void setEmptyViewVisibility() {
        setEmptyViewVisibility(View.VISIBLE);
    }

    public void setEmptyViewVisibility(int visibility) {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(visibility);
        }
    }

    public void hideHeaderViews() {
        if (mHeaderViews != null && mHeaderViews.size() > 0) {
            for (int i = 0; i < mHeaderViews.size(); i++) {
                mHeaderViews.valueAt(i).setVisibility(View.GONE);
            }
        }
    }

    public void hideFooterViews() {
        if (mFooterViews != null && mFooterViews.size() > 0) {
            for (int i = 0; i < mFooterViews.size(); i++) {
                mFooterViews.valueAt(i).setVisibility(View.GONE);
            }
        }
    }

    public void showHeaderViews() {
        if (mHeaderViews != null && mHeaderViews.size() > 0) {
            for (int i = 0; i < mHeaderViews.size(); i++) {
                mHeaderViews.valueAt(i).setVisibility(View.VISIBLE);
            }
        }
    }

    public void showFooterViews() {
        if (mFooterViews != null && mFooterViews.size() > 0) {
            for (int i = 0; i < mFooterViews.size(); i++) {
                mFooterViews.valueAt(i).setVisibility(View.VISIBLE);
            }
        }
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

    private View setupLoadingView(Context context, String showText, boolean showProgressBar) {
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

        if (showProgressBar) {
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

    public void showFooterViewDataLoading(String showText) {
        //
        setDataLoading(true);
        setLoadComplete(false);
        //
        removeFooterView(mLoadingView);
        mLoadingView = setupLoadingView(mContext, TextUtils.isEmpty(showText) ? "数据加载中..." : showText, true);
        addFooterView(mLoadingView);
        // CLog.d("showFooterViewDataLoading");
        Log.d(TAG, "showFooter showFooterViewDataLoading: ");
    }


    /**
     * 上拉加载更多数据  （一页加载完成)
     */
    public void showFooterViewNormal(String showText) {
        //
        setLoadComplete(false);
        setDataLoading(false);
        //
        removeFooterView(mLoadingView);
        mLoadingView = setupLoadingView(mContext, TextUtils.isEmpty(showText) ? "上拉加载更多数据" : showText, false);
        addFooterView(mLoadingView);
        Log.d(TAG, "showFooter showFooterViewNormal: ");
    }

    /**
     * 所有数据加载完成
     */
    public void showFooterViewLoadComplete(String showText) {
        //
        setLoadComplete(true);
        setDataLoading(false);
        //
        removeFooterView(mLoadingView);
        mLoadingView = setupLoadingView(mContext, TextUtils.isEmpty(showText) ? "数据加载完成" : showText, false);
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

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public static abstract class OnItemClickListener {

        public void onItemClick(View view, int position) {
        }

        public boolean onItemLongClick(View view, int position) {
            return false;
        }
    }

}
