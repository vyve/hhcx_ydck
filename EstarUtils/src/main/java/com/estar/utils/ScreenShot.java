package com.estar.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ScreenShot {

	// ���浽sdcard
	private  void savePic(Bitmap b, String strFileName) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(strFileName);
			if (null != fos) {
				b.compress(Bitmap.CompressFormat.JPEG, 80, fos);
				fos.flush();
				fos.close();

			}
			if(!b.isRecycled()){//先判断图片是否已释放了
				b.recycle();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public  void shoot(Activity a,String path,View view) {
		savePic(takeScreenShot(a,view), path);
	}

	private  Bitmap takeScreenShot(Activity activity,View view){ 
		//View������Ҫ��ͼ��
		
		view.setDrawingCacheEnabled(true); 
		view.buildDrawingCache();
		Bitmap b1 = view.getDrawingCache(); 
//		//��ȡ״̬���߶� 
//		Rect frame = new Rect(); 
//		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
//		int statusBarHeight = frame.top; System.out.println(statusBarHeight); 
//		//��ȡ��Ļ���͸�
//		int width = activity.getWindowManager().getDefaultDisplay().getWidth();
//		int height = activity.getWindowManager().getDefaultDisplay().getHeight(); 
//		//ȥ�������� 
//		//Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
//		
//		Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight); 
//		view.destroyDrawingCache(); 

		return b1; 
		
	}

}
