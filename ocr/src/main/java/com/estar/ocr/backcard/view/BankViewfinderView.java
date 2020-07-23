package com.estar.ocr.backcard.view;



import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.estar.ocr.R;

public final class BankViewfinderView extends View {

	// private static final int[] SCANNER_ALPHA = { 0, 64, 128, 192, 255, 192,
	// 128, 64 };
	/**
	 * 鍒锋柊鐣岄潰鐨勬椂锟�
	 */
	private static final long ANIMATION_DELAY = 10L;
	// private static final int OPAQUE = 0xFF;
	/**
	 * 鍒ゆ柇灞忓箷鐨勬棆杞殑搴︽暟瀵瑰簲鐨勬柟鍚戯拷?锟藉锟�0,1,2,3
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
	 * 涓棿婊戝姩绾跨殑锟�椤剁浣嶇疆
	 */
	// private int slideTop;
	// private int slideTop1;

	/**
	 * 涓棿婊戝姩绾跨殑锟�搴曠浣嶇疆
	 */
	// private int slideBottom;
	/**
	 * 涓棿閭ｆ潯绾挎瘡娆″埛鏂扮Щ鍔ㄧ殑璺濈
	 */
	// private static final int SPEEN_DISTANCE = 10;
	/**
	 * 鎵弿妗嗕腑鐨勭嚎鐨勫锟�
	 */
	// private static final int MIDDLE_LINE_WIDTH = 0;
	// private boolean isFirst = false;
	/**
	 * 鍥涘懆杈规鐨勫锟�
	 */
	// private static final int FRAME_LINE_WIDTH = 0;
	private Rect frame;

	int w, h;
	boolean boo = false;
	private Paint mTextPaint;  
    private String mText;  

	public BankViewfinderView(Context context, int w, int h) {
		super(context);
		this.w = w;
		this.h = h;
		paint = new Paint();
		paintLine = new Paint();
		Resources resources = getResources();
		maskColor = resources.getColor(R.color.viewfinder_mask);
		resultColor = resources.getColor(R.color.result_view);
		frameColor = resources.getColor(R.color.viewfinder_frame);// 缁胯壊
		laserColor = resources.getColor(R.color.viewfinder_laser);// 绾㈣壊
		scannerAlpha = 0;
	}

	public BankViewfinderView(Context context, int w, int h, boolean boo) {
		super(context);
		this.w = w;
		this.h = h;
		this.boo = boo;
		paint = new Paint();
		paintLine = new Paint();
		Resources resources = getResources();
		maskColor = resources.getColor(R.color.viewfinder_mask);
		resultColor = resources.getColor(R.color.result_view);
		frameColor = resources.getColor(R.color.viewfinder_frame);// 缁胯壊
		laserColor = resources.getColor(R.color.viewfinder_laser);// 绾㈣壊
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
			 * 杩欎釜鐭╁舰灏辨槸涓棿鏄剧ず鐨勯偅涓锟�
			 */
			int $t = h / 10;
            t = $t;
            b = h - t;
            int $l = (int) ((b - t) * 1.585);
            l = (w - $l) / 2;
            r = w - l;

            l = l + 30;
            t = t + 19;
            r = r - 30;
            b = b - 19;
            frame = new Rect(l, t, r, b);
        } else {
            int $t = h / 5;
            t = $t;
            b = h - t;
            int $l = (int) ((b - t) * 1.585);
            l = (w - $l) / 2;
            r = w - l;

            l = l + 30;
            t = t + 19;
            r = r - 30;
            b = b - 19;
            frame = new Rect(l, t, r, b);
		}

		// 鐢诲嚭鎵弿妗嗗闈㈢殑闃村奖閮ㄥ垎锛屽叡鍥涗釜閮ㄥ垎锛屾壂鎻忔鐨勪笂闈㈠埌灞忓箷涓婇潰锛屾壂鎻忔鐨勪笅闈㈠埌灞忓箷涓嬮潰
		// 鎵弿妗嗙殑宸﹁竟闈㈠埌灞忓箷宸﹁竟锛屾壂鎻忔鐨勫彸杈瑰埌灞忓箷鍙宠竟
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
		   mText = "银行卡";
		     mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);  
		     mTextPaint.setStrokeWidth(3);  
		     mTextPaint.setTextSize(50);  
		     mTextPaint.setColor(frameColor);  
		//     Rect targetRect = new Rect(w/2-50, h/2-50,w/2+50, h/2+50); 
		     //canvas.drawRect(targetRect, mTextPaint);
		    // FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();  
		    
		   // int baseline = (h- fontMetrics.bottom - fontMetrics.top) / 2;  
		    mTextPaint.setTextAlign(Paint.Align.CENTER);  
		     canvas.drawText(mText,w/2,h/2, mTextPaint); 
		if (frame == null) {
			return;
		}

		/**
		 * 褰撴垜浠幏寰楃粨鏋滅殑鏃讹拷?锟斤紝鎴戜滑鏇存柊鏁翠釜灞忓箷鐨勫唴锟�
		 */
		postInvalidateDelayed(ANIMATION_DELAY);
	}
}
