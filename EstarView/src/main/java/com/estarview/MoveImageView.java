package com.estarview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * 自由移动的ImageView
 * Created by ding on 2015/12/13.
 */
public class MoveImageView extends ImageView {

    public MoveImageView(Context context) {
        super(context);
    }

    public MoveImageView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public MoveImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // 定义一个方法，该方法用于控制MyView的显示位置
    public void setLocation(int x, int y) {
//        this.setFrame(x, y - this.getHeight(), x + this.getWidth(), y);
//        this.setFrame(x, y , x + this.getWidth(), y- this.getHeight());
        this.setFrame(x-this.getWidth(), y-this.getHeight() , x , y);
    }

//    // 定义一个方法，该方法用于控制MyView的显示位置
//    public void setLocation(int top, int left)
//    {
//        this.setFrame(left, top, left + 40, top + 40);
//    }

    // 移动
    public boolean autoMouse(MotionEvent event) {
        boolean rb = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                this.setLocation((int) event.getX(), (int) event.getY());
                rb = true;
                break;
        }
        return rb;
    }

}




