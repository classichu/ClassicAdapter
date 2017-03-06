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

public abstract class ClassicRecyclerViewAdapter<D> extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
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
        if (holder instanceof ClassicRecyclerViewHolder){
            //抽象方法
            this.findBindView(position, (ClassicRecyclerViewHolder) holder);
            holder.itemView.setOnClickListener(new OnNotFastClickListener() {
                @Override
                protected void onNotFastClick(View v) {
                    if (onItemClickListener!=null){
                        onItemClickListener.onItemClick(v,position);
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
    public abstract void findBindView(int position,ClassicRecyclerViewHolder classicRecyclerViewHolder);


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public abstract static class  OnItemClickListener {
        public  void onItemClick(View itemView, int position){

        }
        public  boolean onItemLongClick(View itemView, int position){
            return false;
        }
    }
    private OnItemClickListener onItemClickListener;

}
