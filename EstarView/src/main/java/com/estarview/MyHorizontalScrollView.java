package com.estarview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * url www.johdan.com
 * @author johdan
 *
 */
public class MyHorizontalScrollView extends HorizontalScrollView {
    public MyHorizontalScrollView(Context context) {
        super(context);
    }
    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }
    public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        View view = getChildAt(getChildCount()-1);
//	        int diff = (view.getLeft()-getScrollX());// 如果为0，证明滑动到最左边
//	        int diff = (view.getRight()-(getWidth()+getScrollX()));// 如果为0证明滑动到最右边
        if(view.getLeft()-getScrollX()==0){// 如果为0，证明滑动到最左边
            if(onScrollListener!=null)
                onScrollListener.onLeft();
        }else if((view.getRight()-(getWidth()+getScrollX()))==0){//如果为0证明滑动到最右边
            if(onScrollListener!=null)
                onScrollListener.onRight();
        }else{//说明在中间
            if(onScrollListener!=null)
                onScrollListener.onScroll();
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    private OnScrollListener onScrollListener;
    public void setOnScrollListener(OnScrollListener onScrollListener){
        this.onScrollListener=onScrollListener;
    }
}
