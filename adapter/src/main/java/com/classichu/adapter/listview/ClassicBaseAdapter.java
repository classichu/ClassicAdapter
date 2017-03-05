package com.classichu.adapter.listview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.classichu.adapter.R;

import java.util.List;

/**
 * Created by louisgeek on 2017/3/5.
 */

public abstract class ClassicBaseAdapter<D> extends BaseAdapter {
    protected List<D> mDataList;
    private int mItemLayoutId;

    public ClassicBaseAdapter(List<D> dataList, int itemLayoutId) {
        mDataList = dataList;
        mItemLayoutId = itemLayoutId;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClassicBaseViewHolder classicBaseViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(mItemLayoutId, parent, false);
            classicBaseViewHolder = new ClassicBaseViewHolder(convertView);
            convertView.setTag(R.id.hold_classic_base_view_holder, classicBaseViewHolder);
        } else {
            classicBaseViewHolder = (ClassicBaseViewHolder) convertView.getTag(R.id.hold_classic_base_view_holder);
        }
        //抽象方法
        this.findBindView(position, classicBaseViewHolder);
        return convertView;
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
            notifyDataSetChanged();
        }
    }

    public void addDataListAtEnd(List<D> dataList) {
        if (dataList != null) {
            int positionStart = mDataList.size();
            mDataList.addAll(dataList);
           notifyDataSetChanged();
        }
    }

    public void addData(int position, D data) {
        mDataList.add(position, data);
        notifyDataSetChanged();
    }

    public void addDataAtStart(D data) {
        mDataList.add(0, data);
        notifyDataSetChanged();
    }

    public void addDataAtEnd(D data) {
        int positionStart = mDataList.size();
        mDataList.add(data);
        notifyDataSetChanged();
    }

    public void removeData(int position) {
        int positionStart = mDataList.size();
        mDataList.remove(position);
        notifyDataSetChanged();
    }

    public void removeData(D data) {
        this.removeData(mDataList.indexOf(data));
    }


    public void replaceData(int locationPos, D data) {
        mDataList.set(locationPos, data);
        notifyDataSetChanged();
    }

    public void replaceData(D oldData, D data) {
        this.replaceData(mDataList.indexOf(oldData), data);
    }

    /**
     * ===========================end========================================
     */

    public abstract void findBindView(int position,ClassicBaseViewHolder classicBaseViewHolder);


}
