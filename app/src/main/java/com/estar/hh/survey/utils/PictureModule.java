package com.estar.hh.survey.utils;


import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.estar.hh.survey.R;


/**
 * 2012-03-14
 * 
 * @author mabo
 * 
 */
public class PictureModule {
	private ImageViewUtil ImageViewUtil = new ImageViewUtil();
	private Context mContext;
	private static PictureModule mTakePictureModule = null;
	private LayoutInflater mInflater = null;
	private View mContentView = null;
	private PopupWindow mPopWindow;
	private Animation bottomOut = null;
	private Animation topIn = null;
	private ImageView mImageView;
	private ImageButton mCloseView;
	private String paramString = "";

	private PictureModule() {

	}

	// 单列模式
	public static PictureModule getInstance() {
		if (mTakePictureModule == null)
			mTakePictureModule = new PictureModule();
		return mTakePictureModule;
	}

	// 初始化mPopWindow
	public boolean initialize(final Context paramContext,
			final String videoModel) {
		this.mContext = paramContext;
		if (this.mContext != null) {
			this.mInflater = LayoutInflater.from(paramContext);
			this.mContentView = this.mInflater.inflate(
					R.layout.take_picture_view, null);
			// 初始化右下角删除按钮
			findView();
			mImageView.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					if (paramString.equals(""))
						return;

				}

			});

			this.mPopWindow = new PopupWindow(this.mContentView, -2, -2);
			if (null != this.mPopWindow) {
				this.topIn = AnimationUtils.loadAnimation(this.mContext,
						R.anim.picture_from_left_in);
				this.bottomOut = AnimationUtils.loadAnimation(this.mContext,
						R.anim.picture_from_left_out);

				return true;
			} else {

				return false;
			}

		} else {
			return false;
		}
	}

	public ImageView getPictureView() {
		return this.mImageView;
	}

	// 获得控件
	public void findView() {

		this.mImageView = this.mContentView
				.findViewById(R.id.pictureView);
		this.mCloseView = this.mContentView
				.findViewById(R.id.closepictureView);
		closePicture();

	}

	// 关闭popWindow
	private void closePicture() {
		mCloseView.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				dismissPicture();

			}

		});
	}

	// 展示PopWindow
	public void showPicture(View paramView, String paramString, int x, int y) {
		try {
			this.paramString = paramString;
			if (null != this.mPopWindow) {
				// this.mImageView.setImageDrawable(Drawable
				// .createFromPath(paramString));
				ImageViewUtil.bitmap2ImageView(this.mImageView,
						((Activity) this.mContext), paramString, 195, 165);
				this.mPopWindow.setAnimationStyle(R.style.PictureAnim);
				this.mPopWindow.update();
				this.mPopWindow.showAtLocation(paramView, 51, x, y);
			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void showPicture(View paramView, String paramString, Boolean isbool) {
		if (null != this.mPopWindow) {
			this.paramString = paramString;

			ImageViewUtil.bitmap2ImageView(this.mImageView,
					((Activity) this.mContext), paramString, 195, 165);
			this.mPopWindow.setAnimationStyle(R.style.PictureAnim);
			this.mPopWindow.update();
			if(isbool){
				this.mPopWindow.showAsDropDown(paramView);
			}else{
				this.mPopWindow.showAtLocation(paramView, Gravity.LEFT| Gravity.TOP,
						100, 0);
			}
			
			
		} else {

		}
	}

	// 关闭
	public void dismissPicture() {
		if (null != this.mPopWindow) {
			this.mPopWindow.setAnimationStyle(R.anim.scale_from_bottom_out);
			this.mPopWindow.dismiss();
		} else {

		}
	}

}
