package com.estar.ocr.vin.vincode;

import com.estar.ocr.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public final class VinViewfinderView extends View {

	// private static final int[] SCANNER_ALPHA = { 0, 64, 128, 192, 255, 192,
	// 128, 64 };
	/**
	 * 刷新界面的时�?
	 */
	private static final long ANIMATION_DELAY = 10L;
	// private static final int OPAQUE = 0xFF;
	/**
	 * 判断屏幕的旋转的度数对应的方向�?�如�?0,1,2,3
	 */
	// private static int directtion = 1;

	// public int getDirecttion() {
	// return directtion;
	// }

	// public void setDirecttion(int directtion) {
	// this.directtion = directtion;
	// }

	private final Paint paint;
	private final Paint paintLine;
	// private Bitmap resultBitmap;
	private final int maskColor;
	private final int resultColor;
	private final int frameColor;
	private final int laserColor;
	private int scannerAlpha;
	private int leftLine = 0;
	private int topLine = 0;
	private int rightLine = 0;
	private int bottomLine = 0;
	/**
	 * 中间滑动线的�?顶端位置
	 */
	// private int slideTop;
	// private int slideTop1;

	/**
	 * 中间滑动线的�?底端位置
	 */
	// private int slideBottom;
	/**
	 * 中间那条线每次刷新移动的距离
	 */
	// private static final int SPEEN_DISTANCE = 10;
	/**
	 * 扫描框中的线的宽�?
	 */
	// private static final int MIDDLE_LINE_WIDTH = 0;
	// private boolean isFirst = false;
	/**
	 * 四周边框的宽�?
	 */
	// private static final int FRAME_LINE_WIDTH = 0;
	private Rect frame;

	int w, h;
	boolean boo = false;
	private Paint mTextPaint;  
    private String mText;  

	public VinViewfinderView(Context context, int w, int h) {
		super(context);
		this.w = w;
		this.h = h;
		paint = new Paint();
		paintLine = new Paint();
		Resources resources = getResources();
		maskColor = resources.getColor(R.color.viewfinder_mask);
		resultColor = resources.getColor(R.color.result_view);
		frameColor = resources.getColor(R.color.viewfinder_frame);// 绿色
		laserColor = resources.getColor(R.color.viewfinder_laser);// 红色
		scannerAlpha = 0;
	}

	public VinViewfinderView(Context context, int w, int h, boolean boo) {
		super(context);
		this.w = w;
		this.h = h;
		this.boo = boo;
		paint = new Paint();
		paintLine = new Paint();
		Resources resources = getResources();
		maskColor = resources.getColor(R.color.viewfinder_mask);
		resultColor = resources.getColor(R.color.result_view);
		frameColor = resources.getColor(R.color.viewfinder_frame);// 绿色
		laserColor = resources.getColor(R.color.viewfinder_laser);// 红色
		scannerAlpha = 0;
	}

	public void setLeftLine(int leftLine) {
		this.leftLine = leftLine;
	}

	public void setTopLine(int topLine) {
		this.topLine = topLine;
	}

	public void setRightLine(int rightLine) {
		this.rightLine = rightLine;
	}

	public void setBottomLine(int bottomLine) {
		this.bottomLine = bottomLine;
	}

	@Override
	public void onDraw(Canvas canvas) {
		int width = canvas.getWidth();
		int height = canvas.getHeight();

		int t;
		int b;
		int l;
		int r;

		if (width > height && !boo) {
			/**
			 * 这个矩形就是中间显示的那个框�?
			 */
			int $t = h / 10;
			int ntmp = h*3 / 10;
			t = ntmp;
			b = h - ntmp;
			int $l = (int) ((h-$t-$t) * 1.585);
			l = (w - $l) / 2;
			r = w - l;
			l = l + 30;
			t = t + 19;
			r = r - 30;
			b = b - 19;
			frame = new Rect(l, t, r, b);

		} else {
			int $t = h / 5;
			int ntmp = h*2 / 5;
			t = ntmp;
			b = h - t;
			int $l = (int) ((h - $t-$t) * 1.585);
			l = (w - $l) / 2;
			r = w - l;

			
			l = l + 30;
			t = t + 19;
			r = r - 30;
			b = b - 19;
			frame = new Rect(l, t, r, b);
		}

		// 画出扫描框外面的阴影部分，共四个部分，扫描框的上面到屏幕上面，扫描框的下面到屏幕下面
		// 扫描框的左边面到屏幕左边，扫描框的右边到屏幕右边
		paint.setColor(maskColor);
		canvas.drawRect(0, 0, width, frame.top, paint);
		canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
		canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1,
				paint);
		canvas.drawRect(0, frame.bottom + 1, width, height, paint);

		if (width > height && !boo) {

			paintLine.setColor(frameColor);
			paintLine.setStrokeWidth(8);
			paintLine.setAntiAlias(true);
			int num = 40;
			canvas.drawLine(l - 4, t, l + num, t, paintLine);
			canvas.drawLine(l, t, l, t + num, paintLine);

			canvas.drawLine(r, t, r - num, t, paintLine);
			canvas.drawLine(r, t - 4, r, t + num, paintLine);

			canvas.drawLine(l - 4, b, l + num, b, paintLine);
			canvas.drawLine(l, b, l, b - num, paintLine);

			canvas.drawLine(r, b, r - num, b, paintLine);
			canvas.drawLine(r, b + 4, r, b - num, paintLine);

			if (leftLine == 1) {
				canvas.drawLine(l, t, l, b, paintLine);
			}
			if (rightLine == 1) {
				canvas.drawLine(r, t, r, b, paintLine);
			}
			if (topLine == 1) {
				canvas.drawLine(l, t, r, t, paintLine);
			}
			if (bottomLine == 1) {
				canvas.drawLine(l, b, r, b, paintLine);
			}

		} else {
			paintLine.setColor(frameColor);
			paintLine.setStrokeWidth(8);
			paintLine.setAntiAlias(true);
			canvas.drawLine(l, t, l + 100, t, paintLine);
			canvas.drawLine(l, t, l, t + 100, paintLine);
			canvas.drawLine(r, t, r - 100, t, paintLine);
			canvas.drawLine(r, t, r, t + 100, paintLine);
			canvas.drawLine(l, b, l + 100, b, paintLine);
			canvas.drawLine(l, b, l, b - 100, paintLine);
			canvas.drawLine(r, b, r - 100, b, paintLine);
			canvas.drawLine(r, b, r, b - 100, paintLine);
		}

		if (frame == null) {
			return;
		}
//		mText = "如果无法识别，请点击拍照按钮";
		mText = "车架号识别";
	     mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	     mTextPaint.setStrokeWidth(3);  
	     mTextPaint.setTextSize(30);  
	     mTextPaint.setColor(frameColor);  
	//     Rect targetRect = new Rect(w/2-50, h/2-50,w/2+50, h/2+50); 
	     //canvas.drawRect(targetRect, mTextPaint);
	    // FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();  
	    
	   // int baseline = (h- fontMetrics.bottom - fontMetrics.top) / 2;  
	    mTextPaint.setTextAlign(Paint.Align.CENTER);  
	     canvas.drawText(mText,w/2,b+70, mTextPaint); 
		/**
		 * 当我们获得结果的时�?�，我们更新整个屏幕的内�?
		 */
		postInvalidateDelayed(ANIMATION_DELAY);
	}
}
