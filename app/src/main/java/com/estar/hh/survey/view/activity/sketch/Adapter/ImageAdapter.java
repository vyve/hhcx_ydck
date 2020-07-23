package com.estar.hh.survey.view.activity.sketch.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.estar.hh.survey.BuildConfig;
import com.estar.hh.survey.R;

import java.util.List;

/**
 * Created by ding on 2016/4/11.
 */
public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private List<Integer> list=null;
    private int length = 145;
    private OnLongCkickListener onLongCkickListener;


    public void setOnLongCkickListener(OnLongCkickListener onLongCkickListener) {
        this.onLongCkickListener = onLongCkickListener;
    }

    public ImageAdapter(Context context, List<Integer> list ) {
        mContext = context;
        this.list=list;
        length =(((Activity)mContext).getWindowManager().getDefaultDisplay().getWidth()-9)/3;
    }

    public interface OnLongCkickListener {

        void onLongClick(int index);
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public BitmapDrawable yasuo(int id){
        Bitmap bmpOrg = BitmapFactory.decodeResource(mContext.getResources(),id);
        int width = bmpOrg.getWidth();
        int height = bmpOrg.getHeight();
        float sw = ((float) length) / width;
        float sh = ((float) length) / height;

        if(width<=length){

            return  new BitmapDrawable(bmpOrg);
        }


        Matrix matrix = new Matrix();
        matrix.postScale(sw, sh);
        Bitmap resizedBitmap = Bitmap.createBitmap(bmpOrg, 0, 0, width, height,
                matrix, true);
        bmpOrg.recycle();
        BitmapDrawable bmp = new BitmapDrawable(resizedBitmap);
        return bmp;
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(length-2,length-2));
            imageView.setAdjustViewBounds(false);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(4, 4, 4, 4);
        } else {
            imageView = (ImageView) convertView;

        }
        int photoURL =list.get(position);
        BitmapDrawable bmp=null;
        try{
            bmp= yasuo(photoURL);
            imageView.setImageDrawable(bmp);
        }catch(Exception e){
            e.printStackTrace();
            Resources res=mContext.getResources();
            Bitmap bitmap= BitmapFactory.decodeResource(res, R.drawable.error);
            imageView.setImageBitmap(bitmap);
            imageView.setBackgroundColor(android.graphics.Color.GREEN);

        }
        return imageView;
    }


}




