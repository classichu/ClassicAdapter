package com.classichu.adapter.helper;

import android.content.Context;
import android.view.ViewGroup;

import com.classichu.adapter.widget.ClassicEmptyView;

/**
 * Created by louisgeek on 2017/3/10.
 */

public class ClassicEmptyViewHelper {
    public static ClassicEmptyView getClassicEmptyView(Context context, ClassicEmptyView.OnEmptyViewClickListener onEmptyViewClickListener) {
        ClassicEmptyView classicEmptyView = new ClassicEmptyView(context);
        classicEmptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        classicEmptyView.setOnEmptyViewClickListener(onEmptyViewClickListener);
        return classicEmptyView;
    }
}
