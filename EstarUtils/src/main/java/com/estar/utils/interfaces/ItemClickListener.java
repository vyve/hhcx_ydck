package com.estar.utils.interfaces;

import android.view.View;

/**
 * item点击回调接口
 * 
 * @author wen_er
 * 
 */
public interface ItemClickListener {

	/**
	 * Item 普通点击
	 */

    void onItemClick(View view, int postion);

	/**
	 * Item 长按
	 */

    void onItemLongClick(View view, int postion);

	/**
	 * Item 内部View点击
	 */

    void onItemSubViewClick(View view, int postion, boolean bl);
}
