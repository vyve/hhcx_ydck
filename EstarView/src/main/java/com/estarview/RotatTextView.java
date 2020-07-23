package com.estarview;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsoluteLayout;
import android.widget.TextView;

public class RotatTextView extends TextView {


	public RotatTextView(Context context, AttributeSet attrs) {
		super(context, attrs);


	}

	public RotatTextView(Context context) {
		super(context, null);


	}

	float down_x;
	float down_y;

	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()& MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN: {


				down_x = event.getX();
				down_y = event.getY();

				break;

			}
			case MotionEvent.ACTION_MOVE: {

				AbsoluteLayout.LayoutParams p=(AbsoluteLayout.LayoutParams)this.getLayoutParams();
				p.x+=(event.getX()-down_x);
				p.y+=(event.getY()-down_y);
				this.setLayoutParams(p);

				break;
			}
			case MotionEvent.ACTION_POINTER_DOWN:

				break;
			case MotionEvent.ACTION_POINTER_UP:

				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP: {
				break;
			}
		}
		return true;
	}

}
