package com.adapter.listener;

import android.view.View;

/**
 * Created by lizhangqu on 2015/6/3.
 */
public interface OnItemViewClickListener<T> {
    void onClick(View view, T item, int position,boolean b);
}
