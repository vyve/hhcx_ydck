package com.estar.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.estarutils.R;

import java.io.File;
import java.util.List;

public class ShowImagePopWindow extends PopupWindow {
	List<String> imageList;
	View view;
	TextView reghtText, leftText, centerText;
	ViewPager viewPager1;
	RelativeLayout bodyLL;
	TextView delTV;
	RelativeLayout downLL;
	Activity context;
	ImageAdapter imageAdapter;
	int index = 1;
	int width;
	DelImageOnClickListener delImage;

	public interface DelImageOnClickListener {
		void delImage(String path);
	}

	public void setDelImageOnClickListener(DelImageOnClickListener delImage) {
		delTV.setVisibility(View.VISIBLE);
		this.delImage = delImage;
	}

	public ShowImagePopWindow(Activity context, List<String> imageList, int position) {
		super(context);
		this.imageList = imageList;
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.show_image_view, null);
		viewPager1 = view.findViewById(R.id.viewPager1);
		bodyLL = view.findViewById(R.id.bodyLL);
		downLL = view.findViewById(R.id.downLL);
		reghtText = view.findViewById(R.id.reghtText);
		leftText = view.findViewById(R.id.leftText);
		centerText = view.findViewById(R.id.centerText);
		delTV = view.findViewById(R.id.del);

		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;

		LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		ll.topMargin = ((dm.heightPixels - width) / 2);
		// ll.bottomMargin=(dm.heightPixels-width)/2-30;
//		viewPager1.setLayoutParams(ll);

		LinearLayout.LayoutParams downll = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		downll.topMargin = -(((dm.heightPixels - width) / 2) + (int) (width / 4.45));

//		downLL.setLayoutParams(downll);

		if (null != imageList && imageList.size() <= 1) {
			reghtText.setVisibility(View.INVISIBLE);
			centerText.setVisibility(View.INVISIBLE);
			leftText.setVisibility(View.INVISIBLE);

		}
		setAnimationStyle(R.style.PopupAnimation);
		this.setContentView(view);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置viewPager高度

		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x00000000);
		this.setBackgroundDrawable(dw);
		this.update();

		bodyClick();
		// 初始化
		viewPager1.setAdapter(null);
		imageAdapter = new ImageAdapter();
		viewPager1.setAdapter(imageAdapter);
		viewPager1.setCurrentItem(position);
		// 设置初始化位置
		try {
			reghtText.setText(this.imageList.size() + "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		index = position+1;
		leftText.setText(index+ "");
		viewPager1.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageSelected(int arg0) {
				arg0++;
				if (arg0 > index) {
					// 右边
					leftText.setText((++index) + "");
				} else {
					// 左边
					leftText.setText((--index) + "");
				}

				index = arg0;
			}

		});

	}

	private void bodyClick() {
		bodyLL.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {

				ShowImagePopWindow.this.dismiss();

			}

		});
		delTV.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {

				new MessageDialog(context, new MessageDialog.SubmitOnClick() {
					@Override
					public void onSubmitOnClickSure() {
						if (null != delImage)
							delImage.delImage(imageList.get(
									viewPager1.getCurrentItem()));
						ShowImagePopWindow.this.dismiss();
					}

					@Override
					public void onSubmitOnClickCancel() {
					}
				},  "友情提示", "是否确定删除！", "确定",
						"取消",false);
			}

		});
	}

	@Override
	public void dismiss() {

		super.dismiss();

	}

	private class ImageAdapter extends PagerAdapter {

		private LayoutInflater inflater;

		public ImageAdapter() {
			inflater = LayoutInflater.from(context);
		}

		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		public int getCount() {
			return imageList.size();
		}

		public Object instantiateItem(ViewGroup view, int position) {
			View imageLayout = inflater.inflate(R.layout.item_pager_image,
					view, false);
			assert imageLayout != null;
			ImageView imageView = imageLayout
					.findViewById(R.id.image);

//			imageView.setLayoutParams(new FrameLayout.LayoutParams(width, width));
//			FrameLayout body = (FrameLayout) imageLayout
//					.findViewById(R.id.imageFBody);
//			body.setLayoutParams(new FrameLayout.LayoutParams(width, width));

//			RelativeLayout chiBG = (RelativeLayout) imageLayout
//					.findViewById(R.id.chiBG);

			Glide.with(context)
					.load(new File(imageList.get(position)))
//					.placeholder(com.yongchun.library.R.mipmap.ic_placeholder)//设置加载时的照片
//					.error(com.yongchun.library.R.mipmap.ic_placeholder)//设置加载错误时的照片
//                    .centerCrop()
					.into(imageView);

			view.addView(imageLayout, 0);

//			chiBG.setOnClickListener(new OnClickListener() {
//
//				public void onClick(View arg0) {
//
//					ShowImagePopWindow.this.dismiss();
//
//				}
//
//			});

			imageView.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {

				}

			});
			return imageLayout;
		}

		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		public Parcelable saveState() {
			return null;
		}
	}
}
