package com.classichu.adapter.listener;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Classichu on 2017/10/8.
 */

public class SimpleOnRVItemTouchListener extends OnRVItemTouchListener {

    public SimpleOnRVItemTouchListener(RecyclerView mRecyclerView) {
        super(mRecyclerView);
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}
