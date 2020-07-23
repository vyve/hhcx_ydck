package com.estar.hh.survey.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.text.TextPaint;

import com.estar.hh.survey.R;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * 加水印工具
 */
public class ImageCanvas {

		private int width;
		private int height;
		private Context context;
		private ExifInterface exifInfo;
		public ImageCanvas(Context context)
		{
			this.context = context;
		}
		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}







		/**
		 * 给图片添加水印和文字
		 * @param path 图片路径
		 * 查勘员名字
		 * @param textSize 文字大小
		 * @return
		 */
	    public  boolean watermarkBitmap(String path, String address, int textSize, String unameStr, String reportNo, String time) {
			int ww=0;
			int wh = 0;
			if(time.contains("1970")){//防止时间为1970年
				time="获取时间失败";
			}

			Map<String,String> map = new HashMap<String,String>();
			map = readExifInfo(path);
//	    	BitmapFactory.Options options = new BitmapFactory.Options();
//	        //这一个设置使 BitmapFactory.decodeFile获得的图片是空的,但是会将图片信息写到options中
//	        options.inJustDecodeBounds = true;
			Bitmap oldBitmap = BitmapFactory.decodeFile(path);

			BitmapFactory.Options options2 = new BitmapFactory.Options();
			options2.inSampleSize=4;//图片高宽度都为原来的二分之一，即图片大小为原来的大小的四分之一
//			Bitmap markImg = BitmapFactory.decodeFile(context.getResources(R.mipmap.icon1).toString(), options2);

			Bitmap markImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_circle,options2);
			int w = oldBitmap.getWidth();
			int h = oldBitmap.getHeight();

			//需要处理图片太大造成的内存超过的问题,这里我的图片很小所以不写相应代码了
			Bitmap newb= Bitmap.createBitmap(w, h, Config.ARGB_4444);// 创建一个新的和SRC长度宽度一样的位图
			Canvas cv = new Canvas(newb);
			cv.drawBitmap(oldBitmap, 0, 0, null);// 在 0，0坐标开始画入src
			Paint paint=new Paint();
			//加入图片
			if (markImg != null) {
				ww = markImg.getWidth();
				wh = markImg.getHeight();
				paint.setAlpha(50);//取值范围为0~255，数值越小越透明
				cv.drawBitmap(markImg, 10, h - wh - 30, paint);// 在src的左下角画入水印
//	            cv.drawBitmap(markImg, 20, 10, paint);// 在src的左下角画入水印
//				cv.drawBitmap(markImg, w-wh-550, h - wh - 20, paint);
			}
			//加入文字

			String familyName ="宋体";
			Typeface font = Typeface.create(familyName, Typeface.NORMAL);//Typeface.BOLD //粗体
			//Typeface.NORMAL //常规
			TextPaint textPaint=new TextPaint();
			textPaint.setColor(context.getResources().getColor(R.color.white));
//	        textPaint.setColor(Color.rgb(255, 128, 0));
			textPaint.setTypeface(font);
			textPaint.setTextSize(textSize);
			int x = 320;//偏移量x
			int y = 28;//偏移量y
			//这里是自动换行的
			//StaticLayout layout = new StaticLayout(getSysDate(),textPaint,w,Alignment.ALIGN_NORMAL,1.0F,0.0F,true);
			//layout.draw(cv);
			//文字加右下角
//	        cv.drawText(time,w-textSize-x,h-textSize-y , textPaint);
//	        if(empCde.equals("")){
//	        	empCde="UM:"+uid;
//	        }else{
//	        	empCde="UM:"+uid+"  "+empCde;
//	        }
//	        	cv.drawText(empCde,w-textSize-x,h-textSize-y+20,textPaint);
//	        	cv.drawText(time,w-textSize-x,h-textSize-y+36,textPaint);
//	        	 cv.drawText(address,w-textSize-x,h-textSize-y+18 , textPaint);
//	        	 cv.drawText(simSerialNumber,w-textSize-x,h-textSize-y , textPaint);
			if(address==null||address.equals("")){
				address="未获取到位置";
			}

			if(!"".equals(address)){
				address="地址: "+address;
			}
//			cv.drawText(address,20,30 , textPaint);
//			cv.drawText(reportNo,w-190,30 , textPaint);
			cv.drawText(time,ww+20,h - wh-15 , textPaint);
			cv.drawText(address, ww+20,h - wh+10 , textPaint);
			cv.drawText(unameStr,ww+20,h - wh+30 , textPaint);
			cv.drawText(reportNo,ww+20,h - wh+50 , textPaint);
//			cv.drawText(address,20,40 , textPaint);
//			cv.drawText(time,w-500,h - wh-70 , textPaint);
//			cv.drawText(reportNo, w-500,h - wh-30 , textPaint);
//			cv.drawText(unameStr,w-500,h - wh+10 , textPaint);

			cv.save(Canvas.ALL_SAVE_FLAG);// 保存
			cv.restore();// 存储
			try {
				FileOutputStream out = new FileOutputStream(path);
				newb.compress(Bitmap.CompressFormat.JPEG, 85, out);
				width=newb.getWidth();
				height=newb.getHeight();
				writeExifInfo(path, map, "");
				oldBitmap.recycle();
				if (null!=markImg)
					markImg.recycle();
				newb.recycle();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				oldBitmap.recycle();
				if (null!=markImg)
					markImg.recycle();
				newb.recycle();
				return false;
			}
	        
	    }


	/**
	 * PC截屏
	 * 给图片添加水印和文字
	 * @param path 图片路径
	 * 查勘员名字
	 * @param textSize 文字大小
	 * @return
	 */
	public  boolean watermarkBitmap2(String path, String address, int textSize, String unameStr, String reportNo, String time) {
		int ww;
		int wh = 0;

		Map<String,String> map = new HashMap<String,String>();
		map = readExifInfo(path);
		BitmapFactory.Options options = new BitmapFactory.Options();
		//这一个设置使 BitmapFactory.decodeFile获得的图片是空的,但是会将图片信息写到options中
		options.inJustDecodeBounds = true;
		Bitmap oldBitmap = BitmapFactory.decodeFile(path);

//			BitmapFactory.Options options2 = new BitmapFactory.Options();
//			options2.inSampleSize=2;//图片高宽度都为原来的二分之一，即图片大小为原来的大小的四分之一
//			Bitmap markImg = BitmapFactory.decodeFile(context.getResources(R.mipmap.icon1).toString(), options2);

		Bitmap markImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_circle);
		int w = oldBitmap.getWidth();
		int h = oldBitmap.getHeight();

		//需要处理图片太大造成的内存超过的问题,这里我的图片很小所以不写相应代码了
		Bitmap newb= Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas cv = new Canvas(newb);
		cv.drawBitmap(oldBitmap, 0, 0, null);// 在 0，0坐标开始画入src
		Paint paint=new Paint();
		//加入图片
//		if (markImg != null) {
//			ww = markImg.getWidth();
//			wh = markImg.getHeight();
//			paint.setAlpha(50);//取值范围为0~255，数值越小越透明
////	            cv.drawBitmap(markImg, 10, h - wh - 5, paint);// 在src的左下角画入水印
////	            cv.drawBitmap(markImg, 20, 10, paint);// 在src的左下角画入水印
//			cv.drawBitmap(markImg, w-wh-180, h - wh - 30, paint);
//		}
		//加入文字

		String familyName ="黑体";
		Typeface font = Typeface.create(familyName, Typeface.BOLD);//Typeface.BOLD //粗体
		//Typeface.NORMAL //常规
		TextPaint textPaint=new TextPaint();
		textPaint.setColor(context.getResources().getColor(R.color.white));
//	        textPaint.setColor(Color.rgb(255, 128, 0));
		textPaint.setTypeface(font);
		textPaint.setTextSize(textSize);
//		int x = 320;//偏移量x
//		int y = 28;//偏移量y
		//这里是自动换行的
		//StaticLayout layout = new StaticLayout(getSysDate(),textPaint,w,Alignment.ALIGN_NORMAL,1.0F,0.0F,true);
		//layout.draw(cv);
		//文字加右下角
//	        cv.drawText(time,w-textSize-x,h-textSize-y , textPaint);
//	        if(empCde.equals("")){
//	        	empCde="UM:"+uid;
//	        }else{
//	        	empCde="UM:"+uid+"  "+empCde;
//	        }
//	        	cv.drawText(empCde,w-textSize-x,h-textSize-y+20,textPaint);
//	        	cv.drawText(time,w-textSize-x,h-textSize-y+36,textPaint);
//	        	 cv.drawText(address,w-textSize-x,h-textSize-y+18 , textPaint);
//	        	 cv.drawText(simSerialNumber,w-textSize-x,h-textSize-y , textPaint);
		if(!"".equals(address)){
			address="地址: "+address;
		}
//		cv.drawText(address,20,30 , textPaint);
//		cv.drawText(reportNo,w-190,30 , textPaint);
//		cv.drawText(time, w-180,h - wh-15 , textPaint);
//		cv.drawText(unameStr,w-180,h - wh+10 , textPaint);
		cv.drawText("PC截屏:"+time+unameStr,20,h - wh-15 , textPaint);
		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		cv.restore();// 存储
		try {
			FileOutputStream out = new FileOutputStream(path);
			newb.compress(Bitmap.CompressFormat.JPEG, 85, out);
			width=newb.getWidth();
			height=newb.getHeight();
			writeExifInfo(path, map, "");
			oldBitmap.recycle();
			markImg.recycle();
			newb.recycle();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			oldBitmap.recycle();
			markImg.recycle();
			newb.recycle();
			return false;
		}

	}
	    

	 
	    
		/**
		 * 
		* 目前Android SDK定义的Tag有:
			TAG_DATETIME 时间日期
			TAG_FLASH 闪光灯
			TAG_GPS_LATITUDE 纬度
			TAG_GPS_LATITUDE_REF 纬度参考 
			TAG_GPS_LONGITUDE 经度
			TAG_GPS_LONGITUDE_REF 经度参考 
			TAG_IMAGE_LENGTH 图片长
			TAG_IMAGE_WIDTH 图片宽
			TAG_MAKE 设备制造商
			TAG_MODEL 设备型号
			TAG_ORIENTATION 方向
			TAG_WHITE_BALANCE 白平衡
		 * @param path
		 * @return
		 */
		public Map<String,String> readExifInfo(String path)
		{
			Map<String, String> map = new HashMap<String,String>();
			ExifInterface exif;
			try {
				exif = new ExifInterface(path);
				exifInfo = exif;
				/*String TAG_DATETIME = exif.getAttribute(ExifInterface.TAG_DATETIME);
				map.put("TAG_DATETIME", TAG_DATETIME);
				String TAG_FLASH = exif.getAttribute(ExifInterface.TAG_FLASH);
				map.put("TAG_FLASH", TAG_FLASH);
				String TAG_FOCAL_LENGTH = exif.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
				map.put("TAG_FOCAL_LENGTH", TAG_FOCAL_LENGTH);
				String TAG_GPS_ALTITUDE = exif.getAttribute(ExifInterface.TAG_GPS_ALTITUDE);
				map.put("TAG_GPS_ALTITUDE", TAG_GPS_ALTITUDE);
				String TAG_GPS_ALTITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_ALTITUDE_REF);
				map.put("TAG_GPS_ALTITUDE_REF", TAG_GPS_ALTITUDE_REF);
				String TAG_GPS_DATESTAMP = exif.getAttribute(ExifInterface.TAG_GPS_DATESTAMP);
				map.put("TAG_GPS_DATESTAMP", TAG_GPS_DATESTAMP);
				String TAG_GPS_LATITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
				map.put("TAG_GPS_LATITUDE", TAG_GPS_LATITUDE);
				String TAG_GPS_LATITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
				map.put("TAG_GPS_LATITUDE_REF", TAG_GPS_LATITUDE_REF);
				String TAG_GPS_LONGITUDE = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
				map.put("TAG_GPS_LONGITUDE", TAG_GPS_LONGITUDE);
				String TAG_GPS_LONGITUDE_REF = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
				map.put("TAG_GPS_LONGITUDE_REF", TAG_GPS_LONGITUDE_REF);
				String TAG_GPS_PROCESSING_METHOD = exif.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD);
				map.put("TAG_GPS_PROCESSING_METHOD", TAG_GPS_PROCESSING_METHOD);
				String TAG_GPS_TIMESTAMP = exif.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP);
				map.put("TAG_GPS_TIMESTAMP", TAG_GPS_TIMESTAMP);
				String TAG_IMAGE_LENGTH = exif.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
				map.put("TAG_IMAGE_LENGTH", TAG_IMAGE_LENGTH);
				String TAG_IMAGE_WIDTH = exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
				map.put("TAG_IMAGE_WIDTH", TAG_IMAGE_WIDTH);
				String TAG_MAKE = exif.getAttribute(ExifInterface.TAG_MAKE);
				map.put("TAG_MAKE", TAG_MAKE);
				String TAG_MODEL = exif.getAttribute(ExifInterface.TAG_MODEL);
				map.put("TAG_MODEL", TAG_MODEL);
				String TAG_ORIENTATION = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
				map.put("TAG_ORIENTATION", TAG_ORIENTATION);
				//String TAG_WHITE_BALANCE = exif.getAttribute(ExifInterface.TAG_WHITE_BALANCE);
				map.put("TAG_WHITE_BALANCE", "estar");
				*/
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return map;
		}
		
		public void writeExifInfo(String path, Map<String, String> map, String empCde)
		{
			ExifInterface exif;
			try {
				exif = new ExifInterface(path);
				exif = exifInfo ;
				
				exif.setAttribute(ExifInterface.TAG_MAKE,"estar");
				/*exif.setAttribute(ExifInterface.TAG_DATETIME,map.get("TAG_DATETIME"));
				exif.setAttribute(ExifInterface.TAG_FLASH,map.get("TAG_FLASH"));
				exif.setAttribute(ExifInterface.TAG_FOCAL_LENGTH,map.get("TAG_FOCAL_LENGTH"));
				
				exif.setAttribute(ExifInterface.TAG_IMAGE_LENGTH,map.get("TAG_IMAGE_LENGTH"));
				exif.setAttribute(ExifInterface.TAG_IMAGE_WIDTH,map.get("TAG_IMAGE_WIDTH"));
				
				exif.setAttribute(ExifInterface.TAG_MODEL,map.get("TAG_MODEL"));
				exif.setAttribute(ExifInterface.TAG_ORIENTATION,map.get("TAG_ORIENTATION"));
				exif.setAttribute(ExifInterface.TAG_WHITE_BALANCE,map.get("TAG_WHITE_BALANCE"));
				*/
				if(exif !=null)
				{
					exif.saveAttributes();
				}
				//Toast.makeText(this, exif.getAttribute(ExifInterface.TAG_MODEL), Toast.LENGTH_LONG).show();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}
