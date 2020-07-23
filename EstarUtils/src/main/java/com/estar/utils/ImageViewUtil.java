package com.estar.utils;

public class ImageViewUtil {

//	/**
//	 * ��ĳ��ͼƬ��ʾ��ImageView������
//	 * @param activity -- ���ڶ���
//	 * @param res -- ��Դ
//	 * @param id -- ��ԴID
//	 * @return
//	 */
//	public Bitmap bitmap2ImageView(Activity activity, Resources res, int id){
//
//		//����һ��ImageView
//		Bitmap resultbitmap=null;
//		try{
//			Bitmap bitmap = BitmapFactory.decodeResource(res,id);
//			//��ȡͼƬ�߶�
//			int width = bitmap.getWidth(); //��
//			int height = bitmap.getHeight(); //��
//
//			int widthMap = activity.getWindowManager().getDefaultDisplay().getWidth();
//			int heightMap = activity.getWindowManager().getDefaultDisplay().getHeight();
//
//			float scaleWidth = ((float)widthMap)/width;
//			float scaleHeight = ((float)heightMap)/height;
//
//			Matrix matrix = new Matrix();
//			matrix.postScale(scaleWidth, scaleHeight);
//			// �����µ�ͼƬ
//			Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
//					width, height, matrix, true);
//			bitmap.recycle();
//			//�����洴����Bitmapת����Drawable����ʹ�������ʹ����ImageView, ImageButton��
//			BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);
//			// ����ImageView��ͼƬΪ����ת����ͼƬ
//			resizedBitmap.recycle();
//			return resultbitmap;
//
//
//		}catch(Exception ex){
//			return null;
//		}
//
//	}
//	  public Bitmap getLoacalBitmap(String url) {
//			if (Environment.getExternalStorageState().equals(
//					Environment.MEDIA_MOUNTED)) {
//	         try {
//	              FileInputStream fis = new FileInputStream(url);
//	              return BitmapFactory.decodeStream(fis);  ///����ת��ΪBitmapͼƬ
//
//	           } catch (FileNotFoundException e) {
//	              e.printStackTrace();
//	              return null;
//	         }
//			}else{
//				return null;
//			}
//	    }
//	/**
//	 * ��ĳ��ͼƬ��ʾ��ImageView������
//	 * @param activity -- ���ڶ���
//	 * @param  -- ��Դ
//	 * @param -- ��ԴID
//	 * @return
//	 */
//	public Bitmap bitmap2ImageView(Activity activity, String url){
//
//		//����һ��ImageView
//		Bitmap resultbitmap=null;
//		try{
//			Bitmap bitmap = getLoacalBitmap(url);
//			if(bitmap==null)return null;
//			//��ȡͼƬ�߶�
//			int width = bitmap.getWidth(); //��
//			int height = bitmap.getHeight(); //��
//
//			int widthMap = activity.getWindowManager().getDefaultDisplay().getWidth();
//			int heightMap = activity.getWindowManager().getDefaultDisplay().getHeight();
//
//			float scaleWidth = ((float)widthMap)/width;
//			float scaleHeight = ((float)heightMap)/height;
//
//			Matrix matrix = new Matrix();
//			matrix.postScale(scaleWidth, scaleHeight);
//			// �����µ�ͼƬ
//			Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
//					width, height, matrix, true);
//			//�����洴����Bitmapת����Drawable����ʹ�������ʹ����ImageView, ImageButton��
//
//			// ����ImageView��ͼƬΪ����ת����ͼƬ
//			return resizedBitmap;
//			//��ͼƬ������ʾ
//			//imageView.setScaleType(ScaleType.CENTER);
//		}catch(Exception ex){
//			return null;
//		}
//
//	}
//
//	/**
//	 * ��ĳ��ͼƬ��ʾ��ImageView������
//	 * @param activity -- ���ڶ���
//	 * @param  -- ��Դ
//	 * @param -- ��ԴID
//	 * @return
//	 */
//	public void bitmap2ImageView(ImageView imageView, Activity activity, String url, int widthMap, int heightMap){
//
//		//����һ��ImageView
//
//		try{
//			Bitmap bitmap = getLoacalBitmap(url);
//			if(bitmap==null)return ;
//			//��ȡͼƬ�߶�
//			int width = bitmap.getWidth(); //��
//			int height = bitmap.getHeight(); //��
//			if(widthMap>width)widthMap=width;
//			if(heightMap>height)heightMap=height;
//			float scaleWidth = ((float)widthMap)/width;
//			float scaleHeight = ((float)heightMap)/height;
//			Matrix matrix = new Matrix();
//			matrix.postScale(scaleWidth, scaleHeight);
//			// �����µ�ͼƬ
//			Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
//					width, height, matrix, true);
//
//			//�����洴����Bitmapת����Drawable����ʹ�������ʹ����ImageView, ImageButton��
//			BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);
//			// ����ImageView��ͼƬΪ����ת����ͼƬ
//			imageView.setImageDrawable(bmd);
//			//��ͼƬ������ʾ
//			//imageView.setScaleType(ScaleType.CENTER);
//		}catch(Exception ex){
//			Log.d("bitmap2ImageView",ex.getMessage());
//		}
//
//	}
//
//
//
//	/**
//	 * ��ĳ��ͼƬ��ʾ��ImageView������,��ݱ��и߶��Զ�����
//	 * @param activity -- ���ڶ���
//	 * @param -- ��Դ
//	 * @param  -- ��ԴID
//	 * @return
//	 */
//	public void bitmap2ImageView(ImageView imageView, Activity activity, String url, int widthMap ){
//
//		//����һ��ImageView
//
//		try{
//
//			Bitmap bitmap = getLoacalBitmap(url);
//			if(bitmap==null){
//				//�ļ�����,ɾ���ļ�
//				new File(url).delete();
//				return;
//			}
//
//			//��ȡͼƬ�߶�
//			int width = bitmap.getWidth(); //��
//			int height = bitmap.getHeight(); //��
//			float heightMap=widthMap/(width/ Float.parseFloat(height+".0"));
//
//
//
//			float scaleWidth = ((float)widthMap)/width;
//			float scaleHeight = ((float)heightMap)/height;
//			Matrix matrix = new Matrix();
//			matrix.postScale(scaleWidth, scaleHeight);
//			// �����µ�ͼƬ
//			Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
//					width, height, matrix, true);
//
//			//�����洴����Bitmapת����Drawable����ʹ�������ʹ����ImageView, ImageButton��
//			BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);
//			// ����ImageView��ͼƬΪ����ת����ͼƬ
//			imageView.setImageDrawable(bmd);
//			//���� imageView ��Ⱥ͸߶�
//			RelativeLayout.LayoutParams ll=	new RelativeLayout.LayoutParams(widthMap,(int)heightMap+1);
//			// ll.gravity=Gravity.CENTER;
//			 imageView.setLayoutParams(ll);
//
//			//��ͼƬ������ʾ
//			//imageView.setScaleType(ScaleType.CENTER);
//		}catch(Exception ex){
//			ex.printStackTrace();
//
//		}
//
//	}
//
//
//	/**
//	 *
//	 * @param -- ���ڶ���
//	 * @param  -- ��Դ
//	 * @param  -- ��ԴID
//	 * @return
//	 */
//	public Bitmap getBitmapByWidth(String url, int widthMap ){
//
//		//����һ��ImageView
//
//		try{
//			Bitmap resizedBitmap =null;
//			Bitmap bitmap = getLoacalBitmap(url);
//			if(bitmap==null){
//				//�ļ�����,ɾ���ļ�
//				new File(url).delete();
//				return null;
//			}
//
//			//��ȡͼƬ�߶�
//			int width = bitmap.getWidth(); //��
//			int height = bitmap.getHeight(); //��
//			float heightMap=widthMap/(width/ Float.parseFloat(height+".0"));
//
//			float scaleWidth = ((float)widthMap)/width;
//			float scaleHeight = ((float)heightMap)/height;
//			Matrix matrix = new Matrix();
//			matrix.postScale(scaleWidth, scaleHeight);
//			// �����µ�ͼƬ
//			 resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
//					width, height, matrix, true);
//			 return resizedBitmap;
//		}catch(Exception ex){
//			ex.printStackTrace();
//			return null;
//		}
//
//	}
//
//
//	//���ͼƬ��}������
//	public static int computeSampleSize(BitmapFactory.Options options,
//	        int minSideLength, int maxNumOfPixels) {
//	    int initialSize = computeInitialSampleSize(options, minSideLength,maxNumOfPixels);
//
//	    int roundedSize;
//	    if (initialSize <= 8 ) {
//	        roundedSize = 1;
//	        while (roundedSize < initialSize) {
//	            roundedSize <<= 1;
//	        }
//	    } else {
//	        roundedSize = (initialSize + 7) / 8 * 8;
//	    }
//	    return roundedSize;
//	}
//
//	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
//	    double w = options.outWidth;
//	    double h = options.outHeight;
//
//	    int lowerBound = (maxNumOfPixels == -1) ? 1 :
//	            (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
//	    int upperBound = (minSideLength == -1) ? 128 :
//	            (int) Math.min(Math.floor(w / minSideLength),
//	            Math.floor(h / minSideLength));
//
//	    if (upperBound < lowerBound) {
//	        // return the larger one when there is no overlapping zone.
//	        return lowerBound;
//	    }
//
//	    if ((maxNumOfPixels == -1) &&
//	            (minSideLength == -1)) {
//	        return 1;
//	    } else if (minSideLength == -1) {
//	        return lowerBound;
//	    } else {
//	        return upperBound;
//	    }
//	}
//
//
//	 public static int readPictureDegree(String path) {
//	        int degree  = 0;
//	        try {
//	                ExifInterface exifInterface = new ExifInterface(path);
//	                int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//	                switch (orientation) {
//	                case ExifInterface.ORIENTATION_ROTATE_90:
//	                        degree = 90;
//	                        break;
//	                case ExifInterface.ORIENTATION_ROTATE_180:
//	                        degree = 180;
//	                        break;
//	                case ExifInterface.ORIENTATION_ROTATE_270:
//	                        degree = 270;
//	                        break;
//	                }
//
//	        } catch (IOException e) {
//	                e.printStackTrace();
//	        }
//	        return degree;
//	    }
}
