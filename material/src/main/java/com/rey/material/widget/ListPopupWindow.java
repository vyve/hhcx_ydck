/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rey.material.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.text.TextUtilsCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v4.widget.PopupWindowCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.rey.material.R;

import java.lang.reflect.Method;
import java.util.Locale;

/**
 * This is a copy of android.support.v7.widget.ListPopupWindow.
 * Just change DropDownListView's parent class to com.rey.material.widget.ListView to support
 * RippleEffect in child view.
 *
 * @see android.widget.ListPopupWindow
 */
public class ListPopupWindow {
    private static final String TAG = "ListPopupWindow";
    private static final boolean DEBUG = false;

    /**
     * This value controls the length of time that the user
     * must leave a pointer down without scrolling to expand
     * the autocomplete dropdown list to cover the IME.
     */
    private static final int EXPAND_LIST_TIMEOUT = 250;

    private static Method sClipToWindowEnabledMethod;

    static {
        try {
            sClipToWindowEnabledMethod = android.widget.PopupWindow.class.getDeclaredMethod(
                    "setClipToScreenEnabled", boolean.class);
        } catch (NoSuchMethodException e) {
        }
    }

    private Context mContext;
    private PopupWindow mPopup;
    private ListAdapter mAdapter;
    private DropDownListView mDropDownList;
    
    private int mDropDownHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
    private int mDropDownWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
    private int mDropDownHorizontalOffset;
    private int mDropDownVerticalOffset;
    private boolean mDropDownVerticalOffsetSet;
    
    private int mItemAnimationId;
    private int mItemAnimationOffset;    

    private int mDropDownGravity = Gravity.NO_GRAVITY;

    private boolean mDropDownAlwaysVisible = false;
    private boolean mForceIgnoreOutsideTouch = false;
    int mListItemExpandMaximum = Integer.MAX_VALUE;

    private View mPromptView;
    private int mPromptPosition = POSITION_PROMPT_ABOVE;

    private DataSetObserver mObserver;

    private View mDropDownAnchorView;

    private Drawable mDropDownListHighlight;

    private AdapterView.OnItemClickListener mItemClickListener;
    private AdapterView.OnItemSelectedListener mItemSelectedListener;

    private final ResizePopupRunnable mResizePopupRunnable = new ResizePopupRunnable();
    private final PopupTouchInterceptor mTouchInterceptor = new PopupTouchInterceptor();
    private final PopupScrollListener mScrollListener = new PopupScrollListener();
    private final ListSelectorHider mHideSelector = new ListSelectorHider();
    private Runnable mShowDropDownRunnable;

    private Handler mHandler = new Handler();

    private Rect mTempRect = new Rect();

    private boolean mModal;

    private int mLayoutDirection;

    /**
     * The provided prompt view should appear above list content.
     *
     * @see #setPromptPosition(int)
     * @see #getPromptPosition()
     * @see #setPromptView(View)
     */
    public static final int POSITION_PROMPT_ABOVE = 0;

    /**
     * The provided prompt view should appear below list content.
     *
     * @see #setPromptPosition(int)
     * @see #getPromptPosition()
     * @see #setPromptView(View)
     */
    public static final int POSITION_PROMPT_BELOW = 1;

    /**
     * Alias for {@link ViewGroup.LayoutParams#MATCH_PARENT}.
     * If used to specify a popup width, the popup will match the width of the anchor view.
     * If used to specify a popup height, the popup will fill available space.
     */
    public static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;

    /**
     * Alias for {@link ViewGroup.LayoutParams#WRAP_CONTENT}.
     * If used to specify a popup width, the popup will use the width of its content.
     */
    public static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;

    /**
     * Mode for {@link #setInputMethodMode(int)}: the requirements for the
     * input method should be based on the focusability of the popup.  That is
     * if it is focusable than it needs to work with the input method, else
     * it doesn't.
     */
    public static final int INPUT_METHOD_FROM_FOCUSABLE = PopupWindow.INPUT_METHOD_FROM_FOCUSABLE;

    /**
     * Mode for {@link #setInputMethodMode(int)}: this popup always needs to
     * work with an input method, regardless of whether it is focusable.  This
     * means that it will always be displayed so that the user can also operate
     * the input method while it is shown.
     */
    public static final int INPUT_METHOD_NEEDED = PopupWindow.INPUT_METHOD_NEEDED;

    /**
     * Mode for {@link #setInputMethodMode(int)}: this popup never needs to
     * work with an input method, regardless of whether it is focusable.  This
     * means that it will always be displayed to use as much space on the
     * screen as needed, regardless of whether this covers the input method.
     */
    public static final int INPUT_METHOD_NOT_NEEDED = PopupWindow.INPUT_METHOD_NOT_NEEDED;

    /**
     * Create a new, empty popup window capable of displaying items from a ListAdapter.
     * Backgrounds should be set using {@link #setBackgroundDrawable(Drawable)}.
     *
     * @param context Context used for contained views.
     */
    public ListPopupWindow(Context context) {
        this(context, null, R.attr.listPopupWindowStyle, 0);
    }

    /**
     * Create a new, empty popup window capable of displaying items from a ListAdapter.
     * Backgrounds should be set using {@link #setBackgroundDrawable(Drawable)}.
     *
     * @param context Context used for contained views.
     * @param attrs   Attributes from inflating parent views used to style the popup.
     */
    public ListPopupWindow(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.listPopupWindowStyle, 0);
    }

    /**
     * Create a new, empty popup window capable of displaying items from a ListAdapter.
     * Backgrounds should be set using {@link #setBackgroundDrawable(Drawable)}.
     *
     * @param context Context used for contained views.
     * @param attrs Attributes from inflating parent views used to style the popup.
     * @param defStyleAttr Default style attribute to use for popup content.
     */
    public ListPopupWindow(Context context, AttributeSet attrs, int defStyleAttr){
        this(context, attrs, defStyleAttr, 0);
    }

    /**
     * Create a new, empty popup window capable of displaying items from a ListAdapter.
     * Backgrounds should be set using {@link #setBackgroundDrawable(Drawable)}.
     *
     * @param context Context used for contained views.
     * @param attrs Attributes from inflating parent views used to style the popup.
     * @param defStyleAttr Default style attribute to use for popup content.
     * @param defStyleRes Default style to use for popup content.
     */
    public ListPopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        mContext = context;

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ListPopupWindow,
                defStyleAttr, defStyleRes);
        mDropDownHorizontalOffset = a.getDimensionPixelOffset(
                R.styleable.ListPopupWindow_android_dropDownHorizontalOffset, 0);
        mDropDownVerticalOffset = a.getDimensionPixelOffset(
                R.styleable.ListPopupWindow_android_dropDownVerticalOffset, 0);
        if (mDropDownVerticalOffset != 0) {
            mDropDownVerticalOffsetSet = true;
        }
        a.recycle();

        mPopup = new PopupWindow(context, attrs, defStyleAttr);
        mPopup.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

        // Set the default layout direction to match the default locale one
        final Locale locale = mContext.getResources().getConfiguration().locale;
        mLayoutDirection = TextUtilsCompat.getLayoutDirectionFromLocale(locale);
    }

    public void setItemAnimation(int id){
    	mItemAnimationId = id;
    }

    public void setItemAnimationOffset(int offset){
    	mItemAnimationOffset = offset;
    }

    public void setBackgroundDrawable(Drawable background){
    	mPopup.setBackgroundDrawable(background);
    }

    public Drawable getBackground(){
    	return mPopup.getBackground();
    }

    /**
     * Sets the adapter that provides the data and the views to represent the data
     * in this popup window.
     *
     * @param adapter The adapter to use to create this window's content.
     */
    public void setAdapter(ListAdapter adapter) {
        if (mObserver == null) {
            mObserver = new PopupDataSetObserver();
        } else if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mObserver);
        }
        mAdapter = adapter;
        if (mAdapter != null) {
            adapter.registerDataSetObserver(mObserver);
        }

        if (mDropDownList != null) {
            mDropDownList.setAdapter(mAdapter);
        }
    }

    /**
     * Set where the optional prompt view should appear. The default is
     * {@link #POSITION_PROMPT_ABOVE}.
     *
     * @param position A position constant declaring where the prompt should be displayed.
     *
     * @see #POSITION_PROMPT_ABOVE
     * @see #POSITION_PROMPT_BELOW
     */
    public void setPromptPosition(int position) {
        mPromptPosition = position;
    }

    /**
     * @return Where the optional prompt view should appear.
     *
     * @see #POSITION_PROMPT_ABOVE
     * @see #POSITION_PROMPT_BELOW
     */
    public int getPromptPosition() {
        return mPromptPosition;
    }

    /**
     * Set whether this window should be modal when shown.
     *
     * <p>If a popup window is modal, it will receive all touch and key input.
     * If the user touches outside the popup window's content area the popup window
     * will be dismissed.
     *
     * @param modal {@code true} if the popup window should be modal, {@code false} otherwise.
     */
    public void setModal(boolean modal) {
        mModal = modal;
        mPopup.setFocusable(modal);
    }

    /**
     * Returns whether the popup window will be modal when shown.
     *
     * @return {@code true} if the popup window will be modal, {@code false} otherwise.
     */
    public boolean isModal() {
        return mModal;
    }

    /**
     * Forces outside touches to be ignored. Normally if {@link #isDropDownAlwaysVisible()} is
     * false, we allow outside touch to dismiss the dropdown. If this is set to true, then we
     * ignore outside touch even when the drop down is not set to always visible.
     *
     * @hide Used only by AutoCompleteTextView to handle some internal special cases.
     */
    public void setForceIgnoreOutsideTouch(boolean forceIgnoreOutsideTouch) {
        mForceIgnoreOutsideTouch = forceIgnoreOutsideTouch;
    }

    /**
     * Sets whether the drop-down should remain visible under certain conditions.
     *
     * The drop-down will occupy the entire screen below {@link #getAnchorView} regardless
     * of the size or content of the list.  {@link #getBackground()} will fill any space
     * that is not used by the list.
     *
     * @param dropDownAlwaysVisible Whether to keep the drop-down visible.
     *
     * @hide Only used by AutoCompleteTextView under special conditions.
     */
    public void setDropDownAlwaysVisible(boolean dropDownAlwaysVisible) {
        mDropDownAlwaysVisible = dropDownAlwaysVisible;
    }

    /**
     * @return Whether the drop-down is visible under special conditions.
     *
     * @hide Only used by AutoCompleteTextView under special conditions.
     */
    public boolean isDropDownAlwaysVisible() {
        return mDropDownAlwaysVisible;
    }

    /**
     * Sets the operating mode for the soft input area.
     *
     * @param mode The desired mode, see
     *        {@link android.view.WindowManager.LayoutParams#softInputMode}
     *        for the full list
     *
     * @see android.view.WindowManager.LayoutParams#softInputMode
     * @see #getSoftInputMode()
     */
    public void setSoftInputMode(int mode) {
        mPopup.setSoftInputMode(mode);
    }

    /**
     * Returns the current value in {@link #setSoftInputMode(int)}.
     *
     * @see #setSoftInputMode(int)
     * @see android.view.WindowManager.LayoutParams#softInputMode
     */
    public int getSoftInputMode() {
        return mPopup.getSoftInputMode();
    }

    /**
     * Sets a drawable to use as the list item selector.
     *
     * @param selector List selector drawable to use in the popup.
     */
    public void setListSelector(Drawable selector) {
        mDropDownListHighlight = selector;
    }

    /**
     * Set an animation style to use when the popup window is shown or dismissed.
     *
     * @param animationStyle Animation style to use.
     */
    public void setAnimationStyle(int animationStyle) {
        mPopup.setAnimationStyle(animationStyle);
    }

    /**
     * Returns the animation style that will be used when the popup window is shown or dismissed.
     *
     * @return Animation style that will be used.
     */
    public int getAnimationStyle() {
        return mPopup.getAnimationStyle();
    }

    /**
     * Returns the view that will be used to anchor this popup.
     *
     * @return The popup's anchor view
     */
    public View getAnchorView() {
        return mDropDownAnchorView;
    }

    /**
     * Sets the popup's anchor view. This popup will always be positioned relative to the anchor
     * view when shown.
     *
     * @param anchor The view to use as an anchor.
     */
    public void setAnchorView(View anchor) {
        mDropDownAnchorView = anchor;
    }

    /**
     * @return The horizontal offset of the popup from its anchor in pixels.
     */
    public int getHorizontalOffset() {
        return mDropDownHorizontalOffset;
    }

    /**
     * Set the horizontal offset of this popup from its anchor view in pixels.
     *
     * @param offset The horizontal offset of the popup from its anchor.
     */
    public void setHorizontalOffset(int offset) {
        mDropDownHorizontalOffset = offset;
    }

    /**
     * @return The vertical offset of the popup from its anchor in pixels.
     */
    public int getVerticalOffset() {
        if (!mDropDownVerticalOffsetSet) {
            return 0;
        }
        return mDropDownVerticalOffset;
    }

    /**
     * Set the vertical offset of this popup from its anchor view in pixels.
     *
     * @param offset The vertical offset of the popup from its anchor.
     */
    public void setVerticalOffset(int offset) {
        mDropDownVerticalOffset = offset;
        mDropDownVerticalOffsetSet = true;
    }

    /**
     * Set the gravity of the dropdown list. This is commonly used to
     * set gravity to START or END for alignment with the anchor.
     *
     * @param gravity Gravity value to use
     */
    public void setDropDownGravity(int gravity) {
        mDropDownGravity = gravity;
    }

    /**
     * @return The width of the popup window in pixels.
     */
    public int getWidth() {
        return mDropDownWidth;
    }

    /**
     * Sets the width of the popup window in pixels. Can also be {@link #MATCH_PARENT}
     * or {@link #WRAP_CONTENT}.
     *
     * @param width Width of the popup window.
     */
    public void setWidth(int width) {
        mDropDownWidth = width;
    }

    /**
     * Sets the width of the popup window by the size of its content. The final width may be
     * larger to accommodate styled window dressing.
     *
     * @param width Desired width of content in pixels.
     */
    public void setContentWidth(int width) {
        Drawable popupBackground = mPopup.getBackground();
        if (popupBackground != null) {
            popupBackground.getPadding(mTempRect);
            mDropDownWidth = mTempRect.left + mTempRect.right + width;
        } else {
            setWidth(width);
        }
    }

    /**
     * @return The height of the popup window in pixels.
     */
    public int getHeight() {
        return mDropDownHeight;
    }

    /**
     * Sets the height of the popup window in pixels. Can also be {@link #MATCH_PARENT}.
     *
     * @param height Height of the popup window.
     */
    public void setHeight(int height) {
        mDropDownHeight = height;
    }

    /**
     * Sets a listener to receive events when a list item is clicked.
     *
     * @param clickListener Listener to register
     *
     * @see ListView#setOnItemClickListener(AdapterView.OnItemClickListener)
     */
    public void setOnItemClickListener(AdapterView.OnItemClickListener clickListener) {
        mItemClickListener = clickListener;
    }

    /**
     * Sets a listener to receive events when a list item is selected.
     *
     * @param selectedListener Listener to register.
     *
     * @see ListView#setOnItemSelectedListener(AdapterView.OnItemSelectedListener)
     */
    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener selectedListener) {
        mItemSelectedListener = selectedListener;
    }

    /**
     * Set a view to act as a user prompt for this popup window. Where the prompt view will appear
     * is controlled by {@link #setPromptPosition(int)}.
     *
     * @param prompt View to use as an informational prompt.
     */
    public void setPromptView(View prompt) {
        boolean showing = isShowing();
        if (showing) {
            removePromptView();
        }
        mPromptView = prompt;
        if (showing) {
            show();
        }
    }

    /**
     * Post a {@link #show()} call to the UI thread.
     */
    public void postShow() {
        mHandler.post(mShowDropDownRunnable);
    }

    /**
     * Show the popup list. If the list is already showing, this method
     * will recalculate the popup's size and position.
     */
    public void show() {
        int height = buildDropDown();

        int widthSpec = 0;
        int heightSpec = 0;

        boolean noInputMethod = isInputMethodNotNeeded();

        if (mPopup.isShowing()) {
            if (mDropDownWidth == ViewGroup.LayoutParams.MATCH_PARENT) {
                // The call to PopupWindow's update method below can accept -1 for any
                // value you do not want to update.
                widthSpec = -1;
            } else if (mDropDownWidth == ViewGroup.LayoutParams.WRAP_CONTENT) {
                widthSpec = getAnchorView().getWidth();
            } else {
                widthSpec = mDropDownWidth;
            }

            if (mDropDownHeight == ViewGroup.LayoutParams.MATCH_PARENT) {
                // The call to PopupWindow's update method below can accept -1 for any
                // value you do not want to update.
                heightSpec = noInputMethod ? height : ViewGroup.LayoutParams.MATCH_PARENT;
                if (noInputMethod) {
                    mPopup.setWindowLayoutMode(
                            mDropDownWidth == ViewGroup.LayoutParams.MATCH_PARENT ?
                                    ViewGroup.LayoutParams.MATCH_PARENT : 0, 0);
                } else {
                    mPopup.setWindowLayoutMode(
                            mDropDownWidth == ViewGroup.LayoutParams.MATCH_PARENT ?
                                    ViewGroup.LayoutParams.MATCH_PARENT : 0,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                }
            } else if (mDropDownHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
                heightSpec = height;
            } else {
                heightSpec = mDropDownHeight;
            }

            mPopup.setOutsideTouchable(!mForceIgnoreOutsideTouch && !mDropDownAlwaysVisible);

            mPopup.update(getAnchorView(), mDropDownHorizontalOffset,
                    mDropDownVerticalOffset, widthSpec, heightSpec);
        } else {
            if (mDropDownWidth == ViewGroup.LayoutParams.MATCH_PARENT) {
                widthSpec = ViewGroup.LayoutParams.MATCH_PARENT;
            } else {
                if (mDropDownWidth == ViewGroup.LayoutParams.WRAP_CONTENT) {
                    mPopup.setWidth(getAnchorView().getWidth());
                } else {
                    mPopup.setWidth(mDropDownWidth);
                }
            }

            if (mDropDownHeight == ViewGroup.LayoutParams.MATCH_PARENT) {
                heightSpec = ViewGroup.LayoutParams.MATCH_PARENT;
            } else {
                if (mDropDownHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
                    mPopup.setHeight(height);
                } else {
                    mPopup.setHeight(mDropDownHeight);
                }
            }

            mPopup.setWindowLayoutMode(widthSpec, heightSpec);
            setPopupClipToScreenEnabled(true);

            // use outside touchable to dismiss drop down when touching outside of it, so
            // only set this if the dropdown is not always visible
            mPopup.setOutsideTouchable(!mForceIgnoreOutsideTouch && !mDropDownAlwaysVisible);
            mPopup.setTouchInterceptor(mTouchInterceptor);
            PopupWindowCompat.showAsDropDown(mPopup, getAnchorView(), mDropDownHorizontalOffset,
                    mDropDownVerticalOffset, mDropDownGravity);
            mDropDownList.setSelection(ListView.INVALID_POSITION);

            if (!mModal || mDropDownList.isInTouchMode()) {
                clearListSelection();
            }
            if (!mModal) {
                mHandler.post(mHideSelector);
            }

            // show item animation
            if(mItemAnimationId != 0)
	            mPopup.getContentView().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

					@Override
					public boolean onPreDraw() {
						mPopup.getContentView().getViewTreeObserver().removeOnPreDrawListener(this);
						for(int i = 0, count = mDropDownList.getChildCount(); i < count; i ++){
							View v = mDropDownList.getChildAt(i);

							Animation anim = AnimationUtils.loadAnimation(mContext, mItemAnimationId);
							anim.setStartOffset(mItemAnimationOffset * i);
							v.startAnimation(anim);
						}
						return false;
					}

				});
        }
    }

    /**
     * Dismiss the popup window.
     */
    public void dismiss() {
        mPopup.dismiss();
        removePromptView();
        mPopup.setContentView(null);
        mDropDownList = null;
        mHandler.removeCallbacks(mResizePopupRunnable);
    }

    /**
     * Set a listener to receive a callback when the popup is dismissed.
     *
     * @param listener Listener that will be notified when the popup is dismissed.
     */
    public void setOnDismissListener(PopupWindow.OnDismissListener listener) {
        mPopup.setOnDismissListener(listener);
    }

    private void removePromptView() {
        if (mPromptView != null) {
            final ViewParent parent = mPromptView.getParent();
            if (parent instanceof ViewGroup) {
                final ViewGroup group = (ViewGroup) parent;
                group.removeView(mPromptView);
            }
        }
    }

    /**
     * Control how the popup operates with an input method: one of
     * {@link #INPUT_METHOD_FROM_FOCUSABLE}, {@link #INPUT_METHOD_NEEDED},
     * or {@link #INPUT_METHOD_NOT_NEEDED}.
     *
     * <p>If the popup is showing, calling this method will take effect only
     * the next time the popup is shown or through a manual call to the {@link #show()}
     * method.</p>
     *
     * @see #getInputMethodMode()
     * @see #show()
     */
    public void setInputMethodMode(int mode) {
        mPopup.setInputMethodMode(mode);
    }

    /**
     * Return the current value in {@link #setInputMethodMode(int)}.
     *
     * @see #setInputMethodMode(int)
     */
    public int getInputMethodMode() {
        return mPopup.getInputMethodMode();
    }

    /**
     * Set the selected position of the list.
     * Only valid when {@link #isShowing()} == {@code true}.
     *
     * @param position List position to set as selected.
     */
    public void setSelection(int position) {
        DropDownListView list = mDropDownList;
        if (isShowing() && list != null) {
            list.mListSelectionHidden = false;
            list.setSelection(position);

            if (Build.VERSION.SDK_INT >= 11) {
                if (list.getChoiceMode() != ListView.CHOICE_MODE_NONE) {
                    list.setItemChecked(position, true);
                }
            }
        }
    }

    /**
     * Clear any current list selection.
     * Only valid when {@link #isShowing()} == {@code true}.
     */
    public void clearListSelection() {
        final DropDownListView list = mDropDownList;
        if (list != null) {
            // WARNING: Please read the comment where mListSelectionHidden is declared
            list.mListSelectionHidden = true;
            //list.hideSelector();
            list.requestLayout();
        }
    }

    /**
     * @return {@code true} if the popup is currently showing, {@code false} otherwise.
     */
    public boolean isShowing() {
        return mPopup.isShowing();
    }

    /**
     * @return {@code true} if this popup is configured to assume the user does not need
     * to interact with the IME while it is showing, {@code false} otherwise.
     */
    public boolean isInputMethodNotNeeded() {
        return mPopup.getInputMethodMode() == INPUT_METHOD_NOT_NEEDED;
    }

    /**
     * Perform an item click operation on the specified list adapter position.
     *
     * @param position Adapter position for performing the click
     * @return true if the click action could be performed, false if not.
     *         (e.g. if the popup was not showing, this method would return false.)
     */
    public boolean performItemClick(int position) {
        if (isShowing()) {
            if (mItemClickListener != null) {
                final DropDownListView list = mDropDownList;
                final View child = list.getChildAt(position - list.getFirstVisiblePosition());
                final ListAdapter adapter = list.getAdapter();
                mItemClickListener.onItemClick(list, child, position, adapter.getItemId(position));
            }
            return true;
        }
        return false;
    }

    /**
     * @return The currently selected item or null if the popup is not showing.
     */
    public Object getSelectedItem() {
        if (!isShowing()) {
            return null;
        }
        return mDropDownList.getSelectedItem();
    }

    /**
     * @return The position of the currently selected item or {@link ListView#INVALID_POSITION}
     * if {@link #isShowing()} == {@code false}.
     *
     * @see ListView#getSelectedItemPosition()
     */
    public int getSelectedItemPosition() {
        if (!isShowing()) {
            return ListView.INVALID_POSITION;
        }
        return mDropDownList.getSelectedItemPosition();
    }

    /**
     * @return The ID of the currently selected item or {@link ListView#INVALID_ROW_ID}
     * if {@link #isShowing()} == {@code false}.
     *
     * @see ListView#getSelectedItemId()
     */
    public long getSelectedItemId() {
        if (!isShowing()) {
            return ListView.INVALID_ROW_ID;
        }
        return mDropDownList.getSelectedItemId();
    }

    /**
     * @return The View for the currently selected item or null if
     * {@link #isShowing()} == {@code false}.
     *
     * @see ListView#getSelectedView()
     */
    public View getSelectedView() {
        if (!isShowing()) {
            return null;
        }
        return mDropDownList.getSelectedView();
    }

    /**
     * @return The {@link ListView} displayed within the popup window.
     * Only valid when {@link #isShowing()} == {@code true}.
     */
    public ListView getListView() {
        return mDropDownList;
    }

    public PopupWindow getPopup(){
        return mPopup;
    }

    /**
     * The maximum number of list items that can be visible and still have
     * the list expand when touched.
     *
     * @param max Max number of items that can be visible and still allow the list to expand.
     */
    void setListItemExpandMax(int max) {
        mListItemExpandMaximum = max;
    }

    /**
     * Filter key down events. By forwarding key down events to this function,
     * views using non-modal ListPopupWindow can have it handle key selection of items.
     *
     * @param keyCode keyCode param passed to the host view's onKeyDown
     * @param event event param passed to the host view's onKeyDown
     * @return true if the event was handled, false if it was ignored.
     *
     * @see #setModal(boolean)
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // when the drop down is shown, we drive it directly
        if (isShowing()) {
            // the key events are forwarded to the list in the drop down view
            // note that ListView handles space but we don't want that to happen
            // also if selection is not currently in the drop down, then don't
            // let center or enter presses go there since that would cause it
            // to select one of its items
            if (keyCode != KeyEvent.KEYCODE_SPACE
                    && (mDropDownList.getSelectedItemPosition() >= 0
                    || !isConfirmKey(keyCode))) {
                int curIndex = mDropDownList.getSelectedItemPosition();
                boolean consumed;

                final boolean below = !mPopup.isAboveAnchor();

                final ListAdapter adapter = mAdapter;

                boolean allEnabled;
                int firstItem = Integer.MAX_VALUE;
                int lastItem = Integer.MIN_VALUE;

                if (adapter != null) {
                    allEnabled = adapter.areAllItemsEnabled();
                    firstItem = allEnabled ? 0 :
                            mDropDownList.lookForSelectablePosition(0, true);
                    lastItem = allEnabled ? adapter.getCount() - 1 :
                            mDropDownList.lookForSelectablePosition(adapter.getCount() - 1, false);
                }

                if ((below && keyCode == KeyEvent.KEYCODE_DPAD_UP && curIndex <= firstItem) ||
                        (!below && keyCode == KeyEvent.KEYCODE_DPAD_DOWN && curIndex >= lastItem)) {
                    // When the selection is at the top, we block the key
                    // event to prevent focus from moving.
                    clearListSelection();
                    mPopup.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
                    show();
                    return true;
                } else {
                    // WARNING: Please read the comment where mListSelectionHidden
                    //          is declared
                    mDropDownList.mListSelectionHidden = false;
                }

                consumed = mDropDownList.onKeyDown(keyCode, event);
                if (DEBUG) Log.v(TAG, "Key down: code=" + keyCode + " list consumed=" + consumed);

                if (consumed) {
                    // If it handled the key event, then the user is
                    // navigating in the list, so we should put it in front.
                    mPopup.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
                    // Here's a little trick we need to do to make sure that
                    // the list view is actually showing its focus indicator,
                    // by ensuring it has focus and getting its window out
                    // of touch mode.
                    mDropDownList.requestFocusFromTouch();
                    show();

                    switch (keyCode) {
                        // avoid passing the focus from the text view to the
                        // next component
                        case KeyEvent.KEYCODE_ENTER:
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_DPAD_DOWN:
                        case KeyEvent.KEYCODE_DPAD_UP:
                            return true;
                    }
                } else {
                    if (below && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        // when the selection is at the bottom, we block the
                        // event to avoid going to the next focusable widget
                        return curIndex == lastItem;
                    } else return !below && keyCode == KeyEvent.KEYCODE_DPAD_UP &&
                            curIndex == firstItem;
                }
            }
        }

        return false;
    }

    /**
     * Filter key down events. By forwarding key up events to this function,
     * views using non-modal ListPopupWindow can have it handle key selection of items.
     *
     * @param keyCode keyCode param passed to the host view's onKeyUp
     * @param event event param passed to the host view's onKeyUp
     * @return true if the event was handled, false if it was ignored.
     *
     * @see #setModal(boolean)
     */
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (isShowing() && mDropDownList.getSelectedItemPosition() >= 0) {
            boolean consumed = mDropDownList.onKeyUp(keyCode, event);
            if (consumed && isConfirmKey(keyCode)) {
                // if the list accepts the key events and the key event was a click, the text view
                // gets the selected item from the drop down as its content
                dismiss();
            }
            return consumed;
        }
        return false;
    }

    /**
     * Filter pre-IME key events. By forwarding {@link View#onKeyPreIme(int, KeyEvent)}
     * events to this function, views using ListPopupWindow can have it dismiss the popup
     * when the back key is pressed.
     *
     * @param keyCode keyCode param passed to the host view's onKeyPreIme
     * @param event event param passed to the host view's onKeyPreIme
     * @return true if the event was handled, false if it was ignored.
     *
     * @see #setModal(boolean)
     */
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isShowing()) {
            // special case for the back key, we do not even try to send it
            // to the drop down list but instead, consume it immediately
            final View anchorView = mDropDownAnchorView;
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                KeyEvent.DispatcherState state = anchorView.getKeyDispatcherState();
                if (state != null) {
                    state.startTracking(event, this);
                }
                return true;
            } else if (event.getAction() == KeyEvent.ACTION_UP) {
                KeyEvent.DispatcherState state = anchorView.getKeyDispatcherState();
                if (state != null) {
                    state.handleUpEvent(event);
                }
                if (event.isTracking() && !event.isCanceled()) {
                    dismiss();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns an {@link OnTouchListener} that can be added to the source view
     * to implement drag-to-open behavior. Generally, the source view should be
     * the same view that was passed to {@link #setAnchorView}.
     * <p>
     * When the listener is set on a view, touching that view and dragging
     * outside of its bounds will open the popup window. Lifting will select the
     * currently touched list item.
     * <p>
     * Example usage:
     * <pre>
     * ListPopupWindow myPopup = new ListPopupWindow(context);
     * myPopup.setAnchor(myAnchor);
     * OnTouchListener dragListener = myPopup.createDragToOpenListener(myAnchor);
     * myAnchor.setOnTouchListener(dragListener);
     * </pre>
     *
     * @param src the view on which the resulting listener will be set
     * @return a touch listener that controls drag-to-open behavior
     */
    public OnTouchListener createDragToOpenListener(View src) {
        return new ForwardingListener(src) {
            @Override
            public ListPopupWindow getPopup() {
                return ListPopupWindow.this;
            }
        };
    }

    private int getSystemBarHeight(String resourceName) {
        int height = 0;
        int resourceId = mContext.getResources().getIdentifier(resourceName, "dimen", "android");
        if (resourceId > 0) {
            height = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }

    /**
     * <p>Builds the popup window's content and returns the height the popup
     * should have. Returns -1 when the content already exists.</p>
     *
     * @return the content's height or -1 if content already exists
     */
    private int buildDropDown() {        
        int otherHeights = 0;

        if (mDropDownList == null) {
        	ViewGroup dropDownView;
            Context context = mContext;

            /**
             * This Runnable exists for the sole purpose of checking if the view layout has got
             * completed and if so call showDropDown to display the drop down. This is used to show
             * the drop down as soon as possible after user opens up the search dialog, without
             * waiting for the normal UI pipeline to do it's job which is slower than this method.
             */
            mShowDropDownRunnable = new Runnable() {
                public void run() {
                    // View layout should be all done before displaying the drop down.
                    View view = getAnchorView();
                    if (view != null && view.getWindowToken() != null) {
                        show();
                    }
                }
            };

            mDropDownList = new DropDownListView(context, !mModal);
            if (mDropDownListHighlight != null) {
                mDropDownList.setSelector(mDropDownListHighlight);
            }
            mDropDownList.setAdapter(mAdapter);
            mDropDownList.setOnItemClickListener(mItemClickListener);
            mDropDownList.setFocusable(true);
            mDropDownList.setFocusableInTouchMode(true);
            mDropDownList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view,
                        int position, long id) {

                    if (position != -1) {
                        DropDownListView dropDownList = mDropDownList;

                        if (dropDownList != null) {
                            dropDownList.mListSelectionHidden = false;
                        }
                    }
                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            mDropDownList.setOnScrollListener(mScrollListener);

            if (mItemSelectedListener != null) {
                mDropDownList.setOnItemSelectedListener(mItemSelectedListener);
            }

            dropDownView = mDropDownList;

            View hintView = mPromptView;
            if (hintView != null) {
                // if a hint has been specified, we accomodate more space for it and
                // add a text view in the drop down menu, at the bottom of the list
                LinearLayout hintContainer = new LinearLayout(context);
                hintContainer.setOrientation(LinearLayout.VERTICAL);

                LinearLayout.LayoutParams hintParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.0f
                );

                switch (mPromptPosition) {
                    case POSITION_PROMPT_BELOW:
                        hintContainer.addView(dropDownView, hintParams);
                        hintContainer.addView(hintView);
                        break;

                    case POSITION_PROMPT_ABOVE:
                        hintContainer.addView(hintView);
                        hintContainer.addView(dropDownView, hintParams);
                        break;

                    default:
                        break;
                }

                // measure the hint's height to find how much more vertical space
                // we need to add to the drop down's height
                int widthSpec = MeasureSpec.makeMeasureSpec(mDropDownWidth, MeasureSpec.AT_MOST);
                int heightSpec = MeasureSpec.UNSPECIFIED;
                hintView.measure(widthSpec, heightSpec);

                hintParams = (LinearLayout.LayoutParams) hintView.getLayoutParams();
                otherHeights = hintView.getMeasuredHeight() + hintParams.topMargin
                        + hintParams.bottomMargin;

                dropDownView = hintContainer;
            }
            
            mPopup.setContentView(dropDownView);            
            
        } else {
            final View view = mPromptView;
            if (view != null) {
                LinearLayout.LayoutParams hintParams =
                        (LinearLayout.LayoutParams) view.getLayoutParams();
                otherHeights = view.getMeasuredHeight() + hintParams.topMargin
                        + hintParams.bottomMargin;
            }
        }

        // getMaxAvailableHeight() subtracts the padding, so we put it back
        // to get the available height for the whole window
        int padding = 0;
        Drawable background = mPopup.getBackground();
        if (background != null) {
            background.getPadding(mTempRect);
            padding = mTempRect.top + mTempRect.bottom;

            // If we don't have an explicit vertical offset, determine one from the window
            // background so that content will line up.
            if (!mDropDownVerticalOffsetSet) {
                mDropDownVerticalOffset = -mTempRect.top;
            }
        } else {
            mTempRect.setEmpty();
        }

        int systemBarsReservedSpace = 0;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //  getMaxAvailableHeight() on Lollipop seems to ignore the system bars.
            systemBarsReservedSpace = Math.max(
                    getSystemBarHeight("status_bar_height"),
                    getSystemBarHeight("navigation_bar_height")
            );
        }

        // Max height available on the screen for a popup.
        boolean ignoreBottomDecorations =
                mPopup.getInputMethodMode() == PopupWindow.INPUT_METHOD_NOT_NEEDED;
        final int maxHeight = mPopup.getMaxAvailableHeight(
                getAnchorView(), mDropDownVerticalOffset /*, ignoreBottomDecorations*/)
                - systemBarsReservedSpace;
        
        if (mDropDownAlwaysVisible || mDropDownHeight == ViewGroup.LayoutParams.MATCH_PARENT) {
            return maxHeight + padding;
        }

        final int childWidthSpec;
        switch (mDropDownWidth) {
            case ViewGroup.LayoutParams.WRAP_CONTENT:
                childWidthSpec = MeasureSpec.makeMeasureSpec(
                        mContext.getResources().getDisplayMetrics().widthPixels -
                                (mTempRect.left + mTempRect.right),
                        MeasureSpec.AT_MOST);
                break;
            case ViewGroup.LayoutParams.MATCH_PARENT:
                childWidthSpec = MeasureSpec.makeMeasureSpec(
                        mContext.getResources().getDisplayMetrics().widthPixels -
                                (mTempRect.left + mTempRect.right),
                        MeasureSpec.EXACTLY);
                break;
            default:
                childWidthSpec = MeasureSpec.makeMeasureSpec(mDropDownWidth, MeasureSpec.EXACTLY);
                break;
        }

        final int listContent = mDropDownList.measureHeightOfChildrenCompat(childWidthSpec,
                0, DropDownListView.NO_POSITION, maxHeight - otherHeights, -1);
        // add padding only if the list has items in it, that way we don't show
        // the popup if it is not needed
        if (listContent > 0) otherHeights += padding;

        return listContent + otherHeights;
    }

    /**
     * Abstract class that forwards touch events to a {@link ListPopupWindow}.
     *
     * @hide
     */
    public static abstract class ForwardingListener implements OnTouchListener {
        /** Scaled touch slop, used for detecting movement outside bounds. */
        private final float mScaledTouchSlop;

        /** Timeout before disallowing intercept on the source's parent. */
        private final int mTapTimeout;
        /** Timeout before accepting a long-press to start forwarding. */
        private final int mLongPressTimeout;

        /** Source view from which events are forwarded. */
        private final View mSrc;

        /** Runnable used to prevent conflicts with scrolling parents. */
        private Runnable mDisallowIntercept;
        /** Runnable used to trigger forwarding on long-press. */
        private Runnable mTriggerLongPress;

        /** Whether this listener is currently forwarding touch events. */
        private boolean mForwarding;
        /**
         * Whether forwarding was initiated by a long-press. If so, we won't
         * force the window to dismiss when the touch stream ends.
         */
        private boolean mWasLongPress;

        /** The id of the first pointer down in the current event stream. */
        private int mActivePointerId;

        /**
         * Temporary Matrix instance
         */
        private final int[] mTmpLocation = new int[2];

        public ForwardingListener(View src) {
            mSrc = src;
            mScaledTouchSlop = ViewConfiguration.get(src.getContext()).getScaledTouchSlop();
            mTapTimeout = ViewConfiguration.getTapTimeout();
            // Use a medium-press timeout. Halfway between tap and long-press.
            mLongPressTimeout = (mTapTimeout + ViewConfiguration.getLongPressTimeout()) / 2;
        }

        /**
         * Returns the popup to which this listener is forwarding events.
         * <p>
         * Override this to return the correct popup. If the popup is displayed
         * asynchronously, you may also need to override
         * {@link #onForwardingStopped} to prevent premature cancelation of
         * forwarding.
         *
         * @return the popup to which this listener is forwarding events
         */
        public abstract ListPopupWindow getPopup();

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final boolean wasForwarding = mForwarding;
            final boolean forwarding;
            if (wasForwarding) {
                if (mWasLongPress) {
                    // If we started forwarding as a result of a long-press,
                    // just silently stop forwarding events so that the window
                    // stays open.
                    forwarding = onTouchForwarded(event);
                } else {
                    forwarding = onTouchForwarded(event) || !onForwardingStopped();
                }
            } else {
                forwarding = onTouchObserved(event) && onForwardingStarted();

                if (forwarding) {
                    // Make sure we cancel any ongoing source event stream.
                    final long now = SystemClock.uptimeMillis();
                    final MotionEvent e = MotionEvent.obtain(now, now, MotionEvent.ACTION_CANCEL,
                            0.0f, 0.0f, 0);
                    mSrc.onTouchEvent(e);
                    e.recycle();
                }
            }

            mForwarding = forwarding;
            return forwarding || wasForwarding;
        }

        /**
         * Called when forwarding would like to start. <p> By default, this will show the popup
         * returned by {@link #getPopup()}. It may be overridden to perform another action, like
         * clicking the source view or preparing the popup before showing it.
         *
         * @return true to start forwarding, false otherwise
         */
        protected boolean onForwardingStarted() {
            final ListPopupWindow popup = getPopup();
            if (popup != null && !popup.isShowing()) {
                popup.show();
            }
            return true;
        }

        /**
         * Called when forwarding would like to stop. <p> By default, this will dismiss the popup
         * returned by {@link #getPopup()}. It may be overridden to perform some other action.
         *
         * @return true to stop forwarding, false otherwise
         */
        protected boolean onForwardingStopped() {
            final ListPopupWindow popup = getPopup();
            if (popup != null && popup.isShowing()) {
                popup.dismiss();
            }
            return true;
        }

        /**
         * Observes motion events and determines when to start forwarding.
         *
         * @param srcEvent motion event in source view coordinates
         * @return true to start forwarding motion events, false otherwise
         */
        private boolean onTouchObserved(MotionEvent srcEvent) {
            final View src = mSrc;
            if (!src.isEnabled()) {
                return false;
            }

            final int actionMasked = MotionEventCompat.getActionMasked(srcEvent);
            switch (actionMasked) {
                case MotionEvent.ACTION_DOWN:
                    mActivePointerId = srcEvent.getPointerId(0);
                    mWasLongPress = false;

                    if (mDisallowIntercept == null) {
                        mDisallowIntercept = new DisallowIntercept();
                    }
                    src.postDelayed(mDisallowIntercept, mTapTimeout);
                    if (mTriggerLongPress == null) {
                        mTriggerLongPress = new TriggerLongPress();
                    }
                    src.postDelayed(mTriggerLongPress, mLongPressTimeout);
                    break;
                case MotionEvent.ACTION_MOVE:
                    final int activePointerIndex = srcEvent.findPointerIndex(mActivePointerId);
                    if (activePointerIndex >= 0) {
                        final float x = srcEvent.getX(activePointerIndex);
                        final float y = srcEvent.getY(activePointerIndex);
                        if (!pointInView(src, x, y, mScaledTouchSlop)) {
                            clearCallbacks();

                            // Don't let the parent intercept our events.
                            src.getParent().requestDisallowInterceptTouchEvent(true);
                            return true;
                        }
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    clearCallbacks();
                    break;
            }

            return false;
        }

        private void clearCallbacks() {
            if (mTriggerLongPress != null) {
                mSrc.removeCallbacks(mTriggerLongPress);
            }

            if (mDisallowIntercept != null) {
                mSrc.removeCallbacks(mDisallowIntercept);
            }
        }

        private void onLongPress() {
            clearCallbacks();

            final View src = mSrc;
            if (!src.isEnabled()) {
                return;
            }

            if (!onForwardingStarted()) {
                return;
            }

            // Don't let the parent intercept our events.
            mSrc.getParent().requestDisallowInterceptTouchEvent(true);

            // Make sure we cancel any ongoing source event stream.
            final long now = SystemClock.uptimeMillis();
            final MotionEvent e = MotionEvent.obtain(now, now, MotionEvent.ACTION_CANCEL, 0, 0, 0);
            mSrc.onTouchEvent(e);
            e.recycle();

            mForwarding = true;
            mWasLongPress = true;
        }

        /**
         * Handled forwarded motion events and determines when to stop forwarding.
         *
         * @param srcEvent motion event in source view coordinates
         * @return true to continue forwarding motion events, false to cancel
         */
        private boolean onTouchForwarded(MotionEvent srcEvent) {
            final View src = mSrc;
            final ListPopupWindow popup = getPopup();
            if (popup == null || !popup.isShowing()) {
                return false;
            }

            final DropDownListView dst = popup.mDropDownList;
            if (dst == null || !dst.isShown()) {
                return false;
            }

            // Convert event to destination-local coordinates.
            final MotionEvent dstEvent = MotionEvent.obtainNoHistory(srcEvent);
            toGlobalMotionEvent(src, dstEvent);
            toLocalMotionEvent(dst, dstEvent);

            // Forward converted event to destination view, then recycle it.
            final boolean handled = dst.onForwardedEvent(dstEvent, mActivePointerId);
            dstEvent.recycle();

            // Always cancel forwarding when the touch stream ends.
            final int action = MotionEventCompat.getActionMasked(srcEvent);
            final boolean keepForwarding = action != MotionEvent.ACTION_UP
                    && action != MotionEvent.ACTION_CANCEL;

            return handled && keepForwarding;
        }

        private static boolean pointInView(View view, float localX, float localY, float slop) {
            return localX >= -slop && localY >= -slop &&
                    localX < ((view.getRight() - view.getLeft()) + slop) &&
                    localY < ((view.getBottom() - view.getTop()) + slop);
        }

        /**
         * Emulates View.toLocalMotionEvent(). This implementation does not handle transformations
         * (scaleX, scaleY, etc).
         */
        private boolean toLocalMotionEvent(View view, MotionEvent event) {
            final int[] loc = mTmpLocation;
            view.getLocationOnScreen(loc);
            event.offsetLocation(-loc[0], -loc[1]);
            return true;
        }

        /**
         * Emulates View.toGlobalMotionEvent(). This implementation does not handle transformations
         * (scaleX, scaleY, etc).
         */
        private boolean toGlobalMotionEvent(View view, MotionEvent event) {
            final int[] loc = mTmpLocation;
            view.getLocationOnScreen(loc);
            event.offsetLocation(loc[0], loc[1]);
            return true;
        }

        private class DisallowIntercept implements Runnable {
            @Override
            public void run() {
                final ViewParent parent = mSrc.getParent();
                parent.requestDisallowInterceptTouchEvent(true);
            }
        }

        private class TriggerLongPress implements Runnable {
            @Override
            public void run() {
                onLongPress();
            }
        }
    }

    /**
     * <p>Wrapper class for a ListView. This wrapper can hijack the focus to
     * make sure the list uses the appropriate drawables and states when
     * displayed on screen within a drop down. The focus is never actually
     * passed to the drop down in this mode; the list only looks focused.</p>
     */
    private static class DropDownListView extends ListView {

        /*
        * WARNING: This is a workaround for a touch mode issue.
        *
        * Touch mode is propagated lazily to windows. This causes problems in
        * the following scenario:
        * - Type something in the AutoCompleteTextView and get some results
        * - Move down with the d-pad to select an item in the list
        * - Move up with the d-pad until the selection disappears
        * - Type more text in the AutoCompleteTextView *using the soft keyboard*
        *   and get new results; you are now in touch mode
        * - The selection comes back on the first item in the list, even though
        *   the list is supposed to be in touch mode
        *
        * Using the soft keyboard triggers the touch mode change but that change
        * is propagated to our window only after the first list layout, therefore
        * after the list attempts to resurrect the selection.
        *
        * The trick to work around this issue is to pretend the list is in touch
        * mode when we know that the selection should not appear, that is when
        * we know the user moved the selection away from the list.
        *
        * This boolean is set to true whenever we explicitly hide the list's
        * selection and reset to false whenever we know the user moved the
        * selection back to the list.
        *
        * When this boolean is true, isInTouchMode() returns true, otherwise it
        * returns super.isInTouchMode().
        */
        private boolean mListSelectionHidden;

        /**
         * True if this wrapper should fake focus.
         */
        private boolean mHijackFocus;

        /** Whether to force drawing of the pressed state selector. */
        private boolean mDrawsInPressedState;

        /** Current drag-to-open click animation, if any. */
        private ViewPropertyAnimatorCompat mClickAnimation;

        /** Helper for drag-to-open auto scrolling. */
        private ListViewAutoScrollHelper mScrollHelper;

        /**
         * <p>Creates a new list view wrapper.</p>
         *
         * @param context this view's context
         */
        public DropDownListView(Context context, boolean hijackFocus) {
            super(context, null, R.attr.dropDownListViewStyle);
            mHijackFocus = hijackFocus;
            setCacheColorHint(0); // Transparent, since the background drawable could be anything.            
        }
        
        /**
         * Handles forwarded events.
         *
         * @param activePointerId id of the pointer that activated forwarding
         * @return whether the event was handled
         */
        public boolean onForwardedEvent(MotionEvent event, int activePointerId) {
            boolean handledEvent = true;
            boolean clearPressedItem = false;

            final int actionMasked = MotionEventCompat.getActionMasked(event);
            switch (actionMasked) {
                case MotionEvent.ACTION_CANCEL:
                    handledEvent = false;
                    break;
                case MotionEvent.ACTION_UP:
                    handledEvent = false;
                    // $FALL-THROUGH$
                case MotionEvent.ACTION_MOVE:
                    final int activeIndex = event.findPointerIndex(activePointerId);
                    if (activeIndex < 0) {
                        handledEvent = false;
                        break;
                    }

                    final int x = (int) event.getX(activeIndex);
                    final int y = (int) event.getY(activeIndex);
                    final int position = pointToPosition(x, y);
                    if (position == INVALID_POSITION) {
                        clearPressedItem = true;
                        break;
                    }

                    final View child = getChildAt(position - getFirstVisiblePosition());
                    setPressedItem(child, position, x, y);
                    handledEvent = true;

                    if (actionMasked == MotionEvent.ACTION_UP) {
                        clickPressedItem(child, position);
                    }
                    break;
            }

            // Failure to handle the event cancels forwarding.
            if (!handledEvent || clearPressedItem) {
                clearPressedItem();
            }

            // Manage automatic scrolling.
            if (handledEvent) {
                if (mScrollHelper == null) {
                    mScrollHelper = new ListViewAutoScrollHelper(this);
                }
                mScrollHelper.setEnabled(true);
                mScrollHelper.onTouch(this, event);
            } else if (mScrollHelper != null) {
                mScrollHelper.setEnabled(false);
            }

            return handledEvent;
        }

        /**
         * Starts an alpha animation on the selector. When the animation ends,
         * the list performs a click on the item.
         */
        private void clickPressedItem(final View child, final int position) {
            final long id = getItemIdAtPosition(position);
            performItemClick(child, position, id);
        }

        private void clearPressedItem() {
            mDrawsInPressedState = false;
            setPressed(false);
            // This will call through to updateSelectorState()
            drawableStateChanged();

            if (mClickAnimation != null) {
                mClickAnimation.cancel();
                mClickAnimation = null;
            }
        }

        private void setPressedItem(View child, int position, float x, float y) {
            mDrawsInPressedState = true;

            // Ordering is essential. First update the pressed state and layout
            // the children. This will ensure the selector actually gets drawn.
            setPressed(true);
            layoutChildren();

            // Ensure that keyboard focus starts from the last touched position.
            setSelection(position);
            positionSelectorLikeTouchCompat(position, child, x, y);

            // This needs some explanation. We need to disable the selector for this next call
            // due to the way that ListViewCompat works. Otherwise both ListView and ListViewCompat
            // will draw the selector and bad things happen.
            setSelectorEnabled(false);

            // Refresh the drawable state to reflect the new pressed state,
            // which will also update the selector state.
            refreshDrawableState();
        }

        @Override
        protected boolean touchModeDrawsInPressedStateCompat() {
            return mDrawsInPressedState || super.touchModeDrawsInPressedStateCompat();
        }

        @Override
        public boolean isInTouchMode() {
            // WARNING: Please read the comment where mListSelectionHidden is declared
            return (mHijackFocus && mListSelectionHidden) || super.isInTouchMode();
        }

        /**
         * <p>Returns the focus state in the drop down.</p>
         *
         * @return true always if hijacking focus
         */
        @Override
        public boolean hasWindowFocus() {
            return mHijackFocus || super.hasWindowFocus();
        }

        /**
         * <p>Returns the focus state in the drop down.</p>
         *
         * @return true always if hijacking focus
         */
        @Override
        public boolean isFocused() {
            return mHijackFocus || super.isFocused();
        }

        /**
         * <p>Returns the focus state in the drop down.</p>
         *
         * @return true always if hijacking focus
         */
        @Override
        public boolean hasFocus() {
            return mHijackFocus || super.hasFocus();
        }

    }

    private class PopupDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            if (isShowing()) {
                // Resize the popup to fit new content
                show();
            }
        }

        @Override
        public void onInvalidated() {
            dismiss();
        }
    }

    private class ListSelectorHider implements Runnable {
        public void run() {
            clearListSelection();
        }
    }

    private class ResizePopupRunnable implements Runnable {
        public void run() {
            if (mDropDownList != null && mDropDownList.getCount() > mDropDownList.getChildCount() &&
                    mDropDownList.getChildCount() <= mListItemExpandMaximum) {
                mPopup.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
                show();
            }
        }
    }

    private class PopupTouchInterceptor implements OnTouchListener {
        public boolean onTouch(View v, MotionEvent event) {
            final int action = event.getAction();
            final int x = (int) event.getX();
            final int y = (int) event.getY();

            if (action == MotionEvent.ACTION_DOWN &&
                    mPopup != null && mPopup.isShowing() &&
                    (x >= 0 && x < mPopup.getWidth() && y >= 0 && y < mPopup.getHeight())) {
                mHandler.postDelayed(mResizePopupRunnable, EXPAND_LIST_TIMEOUT);
            } else if (action == MotionEvent.ACTION_UP) {
                mHandler.removeCallbacks(mResizePopupRunnable);
            }
            return false;
        }
    }

    private class PopupScrollListener implements ListView.OnScrollListener {
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                int totalItemCount) {

        }

        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == SCROLL_STATE_TOUCH_SCROLL &&
                    !isInputMethodNotNeeded() && mPopup.getContentView() != null) {
                mHandler.removeCallbacks(mResizePopupRunnable);
                mResizePopupRunnable.run();
            }
        }
    }

    private static boolean isConfirmKey(int keyCode) {
        return keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER;
    }

    private void setPopupClipToScreenEnabled(boolean clip) {
        if (sClipToWindowEnabledMethod != null) {
            try {
                sClipToWindowEnabledMethod.invoke(mPopup, clip);
            } catch (Exception e) {
            }
        } else if(clip && Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            mPopup.setClippingEnabled(false);
        }
    }

}