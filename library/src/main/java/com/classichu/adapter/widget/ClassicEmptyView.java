package com.classichu.adapter.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.classichu.adapter.R;
import com.classichu.adapter.helper.ImageOrVectorResHelper;
import com.classichu.adapter.listener.OnNotFastClickListener;


/**
 * Created by louisgeek on 2017/3/6.
 */

public class ClassicEmptyView extends LinearLayout {

    private View mEmptyView;
    private LinearLayout id_ll_empty_view;
    private ImageView id_iv_empty_view;
    private TextView id_tv_empty_view;

    public ClassicEmptyView(@NonNull Context context) {
        this(context, null);
    }

    public ClassicEmptyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClassicEmptyView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //
        init(context);
        //
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClassicEmptyView);
        initAttrs(typedArray);
        typedArray.recycle();
    }

    private void initAttrs(TypedArray typedArray) {
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int index = typedArray.getIndex(i);
            //
            if (index == R.styleable.ClassicEmptyView_classic_text) {
                setEmptyText(typedArray.getText(index));
            } else if (index == R.styleable.ClassicEmptyView_classic_drawable) {
                //Deprecated
                // Drawable drawable= typedArray.getDrawable(index);
                int resourceId = typedArray.getResourceId(index, R.drawable.ic_info_black_24dp);
                Drawable drawable = ImageOrVectorResHelper.getDrawable(getContext(), resourceId);
                setEmptyImage(drawable);
            }
        }
    }

    private void init(Context context) {
        mEmptyView = LayoutInflater.from(context).inflate(R.layout.layout_classic_empty_view, this);
        id_ll_empty_view = (LinearLayout) mEmptyView.findViewById(R.id.id_ll_empty_view);
        id_iv_empty_view = (ImageView) mEmptyView.findViewById(R.id.id_iv_empty_view);
        id_tv_empty_view = (TextView) mEmptyView.findViewById(R.id.id_tv_empty_view);

        id_ll_empty_view.setOnClickListener(new OnNotFastClickListener() {
            @Override
            protected void onNotFastClick(View v) {
                if (mOnEmptyViewClickListener != null) {
                    mOnEmptyViewClickListener.onClickEmptyView(v);
                }
            }
        });


        id_iv_empty_view.setOnClickListener(new OnNotFastClickListener() {
            @Override
            protected void onNotFastClick(View v) {
                if (mOnEmptyViewClickListener != null) {
                    mOnEmptyViewClickListener.onClickImageView(v);
                }
            }
        });


        id_tv_empty_view.setOnClickListener(new OnNotFastClickListener() {
            @Override
            protected void onNotFastClick(View v) {
                if (mOnEmptyViewClickListener != null) {
                    mOnEmptyViewClickListener.onClickTextView(v);
                }
            }
        });

    }

    public void setContentView(View emptyView) {
        if (mEmptyView != null) {
            if (emptyView == null) {
                mEmptyView.setVisibility(GONE);
            } else {
                mEmptyView = emptyView;
                mEmptyView.setVisibility(VISIBLE);
            }
        }
    }

    public void setContentView(int layoutResId) {
        if (layoutResId == 0) {
            mEmptyView.setVisibility(GONE);
        } else {
            View view = LayoutInflater.from(this.getContext()).inflate(layoutResId, null);
            //
            setContentView(view);
        }
    }


    public ClassicEmptyView setEmptyText(int resId) {
        if (id_tv_empty_view != null) {
            if (resId == 0) {
                id_tv_empty_view.setVisibility(GONE);
            } else {
                id_tv_empty_view.setText(resId);
                id_tv_empty_view.setVisibility(VISIBLE);
            }
        }
        return this;
    }

    public ClassicEmptyView setEmptyText(CharSequence text) {
        if (id_tv_empty_view != null) {
            if (text == null) {
                id_tv_empty_view.setVisibility(GONE);
            } else {
                id_tv_empty_view.setText(text);
                id_tv_empty_view.setVisibility(VISIBLE);
            }
        }
        return this;
    }

    public ClassicEmptyView setEmptyImage(int resId) {
        if (id_iv_empty_view != null) {
            if (resId == 0) {
                id_iv_empty_view.setVisibility(GONE);
            } else {
                id_iv_empty_view.setImageResource(resId);
                id_iv_empty_view.setVisibility(VISIBLE);
            }
        }
        return this;
    }

    public ClassicEmptyView setEmptyImage(Drawable drawable) {
        if (id_iv_empty_view != null) {
            if (drawable == null) {
                id_iv_empty_view.setVisibility(GONE);
            } else {
                id_iv_empty_view.setImageDrawable(drawable);
                id_iv_empty_view.setVisibility(VISIBLE);
            }
        }
        return this;
    }

    private OnEmptyViewClickListener mOnEmptyViewClickListener;

    public void setOnEmptyViewClickListener(OnEmptyViewClickListener onEmptyViewClickListener) {
        mOnEmptyViewClickListener = onEmptyViewClickListener;
    }

    public static abstract class OnEmptyViewClickListener {

        public void onClickTextView(View view) {
        }

        public void onClickImageView(View view) {
        }

        public void onClickEmptyView(View view) {
        }
    }

}
