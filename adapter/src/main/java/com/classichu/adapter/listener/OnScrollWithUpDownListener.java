package com.classichu.adapter.listener;

import android.support.v7.widget.RecyclerView;

/**
 * Created by louisgeek on 2016/12/27.
 */

public abstract class OnScrollWithUpDownListener extends RecyclerView.OnScrollListener {

    private final int mScrollThreshold = 30;

    public abstract void onScrollUp();

    public abstract void onScrollDown();

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (Math.abs(dy) > mScrollThreshold) {
            if (dy > 0) {
                //往上滑动
                onScrollUp();
            } else if (dy < 0) {
                //往下滑动
                onScrollDown();
            }
        }
    }
}
