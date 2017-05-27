package com.classichu.classicadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.classichu.adapter.recyclerview.ClassicRVHeaderFooterAdapter;
import com.classichu.adapter.recyclerview.ClassicRVHeaderFooterViewHolder;

import java.util.List;

/**
 * Created by louisgeek on 2017/3/6.
 */

public class RecyclerViewRVHFAdapter extends ClassicRVHeaderFooterAdapter<String> {


    public RecyclerViewRVHFAdapter(Context mContext, List<String> mDataList, int mItemLayoutId) {
        super(mContext, mDataList, mItemLayoutId);
    }

    @Override
    public RVHeaderFooterAdapterDelegate setupDelegate() {
        return new RVHeaderFooterAdapterDelegate() {
            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int realPosition) {

            }

            @Override
            public int getItemViewType(int realPosition) {
                return 0;
            }
        };
    }

    @Override
    public void findBindView(int realPosition, ClassicRVHeaderFooterViewHolder classicRVHeaderFooterViewHolder) {
        TextView id_tv_title = classicRVHeaderFooterViewHolder.findBindItemView(R.id.id_tv_title);
        id_tv_title.setText(mDataList.get(realPosition) + "");
    }


}
