package com.estar.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * 圆形图片工具
 */
public class ImageCircleUtil {
	 private static final int STROKE_WIDTH = 4;
	//从assets资源中获取图片
		public static Bitmap getBitmap(Context context,String filename){
			
		
				Bitmap image = null;
//				AssetManager am = context.getResources().getAssets();
				try {
					
//					InputStream is = am.open(filename);
//					image = BitmapFactory.decodeStream(is);
//					is.close();
					image=BitmapFactory.decodeFile(filename);//从内存卡中加载图片
				} catch (Exception e) {
					e.printStackTrace();
				}
				return image;
			}
	
	public static  Bitmap toRoundBitmap(Context context,String filename) {
		 Bitmap bitmap= getBitmap(context, filename);
		 int width = bitmap.getWidth();
		 int height = bitmap.getHeight();
		 float roundPx;
	     float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
		 if (width <= height) {
		      roundPx = width / 2;
		      top = 0;
		      left = 0;
		      bottom = width;
	          right = width;
	          height = width;
		      dst_left = 0;
		      dst_top = 0;
		      dst_right = width;
		      dst_bottom = width;
		  } else {
		      roundPx = height / 2;
	          float clip = (width - height) / 2;
		      left = clip;
		      right = width - clip;
		      top = 0;
		      bottom = height;
			  width = height;
			  dst_left = 0;
			  dst_top = 0;
			  dst_right = height;
			  dst_bottom = height;
	     }

	        Bitmap output = Bitmap.createBitmap(width,
			height, Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			
		    final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom);
		    final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
			final RectF rectF = new RectF(dst);

			paint.setAntiAlias(true);

	        canvas.drawARGB(0, 0, 0, 0);
		    paint.setColor(Color.WHITE);
		    paint.setStrokeWidth(4); 
	        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		    canvas.drawBitmap(bitmap, src, dst, paint);
		    
		    //画白色圆圈
		    paint.reset();
		    paint.setColor(Color.WHITE);
		    paint.setStyle(Paint.Style.STROKE);
		    paint.setStrokeWidth(STROKE_WIDTH);
		    paint.setAntiAlias(true);
	        canvas.drawCircle(width / 2, width / 2, width / 2 - STROKE_WIDTH / 2, paint);
	        return output ; 
		}
	
	

}
