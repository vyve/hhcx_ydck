package com.estar.hh.survey.view.component;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.viewpagerindicator.UnderlinePageIndicator;

/**
 * Created by Administrator on 2017/9/28 0028.
 */

public class UnderlinePageIndicatorEx extends UnderlinePageIndicator {

    public UnderlinePageIndicatorEx(Context context) {
        super(context);
    }

    public UnderlinePageIndicatorEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UnderlinePageIndicatorEx(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setViewPager(ViewPager view) {
        super.setViewPager(view);
    }
}
