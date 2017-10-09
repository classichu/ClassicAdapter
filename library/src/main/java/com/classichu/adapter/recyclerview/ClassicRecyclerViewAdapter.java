package com.classichu.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.classichu.adapter.listener.OnNotFastClickListener;

import java.util.List;

/**
 * Created by louisgeek on 2017/3/5.
 */

public abstract class ClassicRecyclerViewAdapter<D> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected List<D> mDataList;
    private int mItemLayoutId;

    public ClassicRecyclerViewAdapter(List<D> mDataList, int mItemLayoutId) {
        this.mDataList = mDataList;
        this.mItemLayoutId = mItemLayoutId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mItemLayoutId, parent, false);
        return new ClassicRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ClassicRecyclerViewHolder) {
            //抽象方法
            this.findBindView(position, (ClassicRecyclerViewHolder) holder);
            holder.itemView.setOnClickListener(new OnNotFastClickListener() {
                @Override
                protected void onNotFastClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, position);
                    }
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemClickListener != null) {
                        return onItemClickListener.onItemLongClick(v, position);
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
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
    public abstract void findBindView(int position, ClassicRecyclerViewHolder classicRecyclerViewHolder);


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
