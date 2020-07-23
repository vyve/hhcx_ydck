package com.estar.hh.survey.adapter.video;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.estar.hh.survey.R;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
//import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
//import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class MainPage2GridViewAdapter extends BaseAdapter {

//	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private Context mContext;
	private List<String> list = null;
	private LayoutInflater layoutInflater = null;
	int width = 0;
	private OnPraiseClickListener onPraiseClickListener;
//	private ImageLoader imageLoader;
	public interface OnPraiseClickListener {
		void praise();
	}

	public void setOnPraiseClickListener(
			OnPraiseClickListener onPraiseClickListener) {
		this.onPraiseClickListener = onPraiseClickListener;
	}

	public void notifyDataSetChanged(List<String> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	public MainPage2GridViewAdapter(Context context, List<String> list, int width) {
		mContext = context;
		this.list = list;
		layoutInflater = LayoutInflater.from(mContext);
		this.width = width;
		this.width = this.width / 3 - 10;
	}

//	public MainPage2GridViewAdapter(Context context, List<String> list,
//                                    int width, ImageLoader imageLoader) {
//		mContext = context;
//		this.list = list;
//		layoutInflater = LayoutInflater.from(mContext);
//		this.width = width;
//		this.width = this.width / 3 - 10;
//		this.imageLoader=imageLoader;
//	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;


		if (convertView == null) {

			convertView = layoutInflater.inflate(R.layout.video_page2_griddata,
					null);

			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}


		vh.image.setLayoutParams(new LinearLayout.LayoutParams(width,
				this.width));

		try {
			Glide.with(mContext).load("file:///"+ list.get(position)).into(vh.image);
//			imageLoader.displayImage("file:///"+ list.get(position), vh.image); // 纯粹为了加载默认配置的一个图片的
		} catch (Exception e) {
			e.printStackTrace();

		}
		return convertView;
	}


	class ViewHolder {
		TextView title;
		ImageView image;
		LinearLayout girdLL;

		public ViewHolder(View view) {
			title = view.findViewById(R.id.title);
			girdLL = view.findViewById(R.id.girdLL);
			image = view.findViewById(R.id.image);

		}
	}

//	private static class AnimateFirstDisplayListener extends
//			SimpleImageLoadingListener {
//
//		static final List<String> displayedImages = Collections
//				.synchronizedList(new LinkedList<String>());
//
//		public void onLoadingComplete(String imageUri, View view,
//                                      Bitmap loadedImage) {
//			if (loadedImage != null) {
//				ImageView imageView = (ImageView) view;
//				boolean firstDisplay = !displayedImages.contains(imageUri);
//				if (firstDisplay) {
//					FadeInBitmapDisplayer.animate(imageView, 500);
//					displayedImages.add(imageUri);
//				}
//			}
//		}
//	}

	private void clearSetOnClickListener() {

	}
}
