package com.estar.utils;

import android.app.Dialog;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.estarutils.R;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.List;


public class KeyboardDialog extends Dialog {

	private KeyboardView keyboardView;
	private Keyboard rackQwe;// 字母键盘 车架号
	private Keyboard double_;// 数字带小数点键盘
	private Keyboard rackNum;// 数字键盘 ABC字母切换
	private Keyboard idNum;// 数字键盘 身份证
	private Keyboard onlyNum;//只有 数字键盘
	private Keyboard carArea;
	private Keyboard carNum;
	public boolean isnun = false;// 是否数据键盘
	public boolean isupper = false;// 是否大写
	private boolean isArea = false;
	private EditText ed;
	private TextView tv2;
	private ImageButton deleteBtn;
	private TextView tv;
	private String area = "京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼港澳";

	private String jjCarStr = "军海空北沈兰济南广成挂学使";//军警车牌

	private INPUT_TYPE type;
	public enum INPUT_TYPE {//车架号、身份证号、车牌号、纯数字、小数
		RACKNO, IDNUMBER, CARNO,NUMBER,DOUBLE,NUMBER_ONLY
	}

	public KeyboardDialog(Context context) {
		super(context, R.style.dialog);
		setContentView(R.layout.rackno_keyboard);
		getWindow().setGravity(Gravity.BOTTOM);
		getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		init();
		showKeyboard();
	}

	public void show(TextView tv, String str, INPUT_TYPE type) {
		super.show();
		this.tv = tv;
		this.type = type;
		if (type == INPUT_TYPE.RACKNO) {
			keyboardView.setKeyboard(rackQwe);
		} else if (type == INPUT_TYPE.DOUBLE) {
			keyboardView.setKeyboard(double_);
		} else if (type == INPUT_TYPE.NUMBER_ONLY) {
			keyboardView.setKeyboard(onlyNum);
		} else if (type == INPUT_TYPE.NUMBER) {
			keyboardView.setKeyboard(rackNum);
		} else if (type == INPUT_TYPE.IDNUMBER) {
			keyboardView.setKeyboard(idNum);
		} else if (type == INPUT_TYPE.CARNO) {
			keyboardView.setKeyboard(carArea);
			isArea = true;
		}
		tv2.setText(str);
		ed.setText("");
		ed.setText(tv.getText());
		ed.setSelection(tv.getText().length());
	}

	@Override
	public void dismiss() {

		super.dismiss();
	}

	private void submit() {
		if (tv != null) {
			if (type == INPUT_TYPE.RACKNO) {
				if (ed.getText().toString().trim().length() < 17) {
					showShortToast("车架号不能小于17位");
					return;
				}
			} else if (type == INPUT_TYPE.IDNUMBER) {
				String result = CheckIDCard.chekIdCard(ed.getText().toString()
						.trim());
				if (!"".equals(result)&&!"证件号码为必填".equals(result)) {
					showShortToast("证件输入错误，请重新输入");
					return;
				}
			} else if (type == INPUT_TYPE.DOUBLE) {
				String result = ed.getText().toString().trim();
				if (!"".equals(result)) {
					DecimalFormat df = new DecimalFormat("#.00");
//					String d= df.format(Double.parseDouble(result));
					tv.setText(df.format(Double.parseDouble(result)));
				}else {
					tv.setText(ed.getText());

				}
				dismiss();
				return;
			} else if (type == INPUT_TYPE.CARNO) {
				String carNo = ed.getText().toString();
				if (!"".equals(carNo)) {
					if (!"*".equals(carNo)
							&& !CheckIDCard.isCheckCarNo(ed.getText()
							.toString())) {
						showShortToast("车牌号输入错误，请重新输入");
						return;
					}
				}
			}

				tv.setText(ed.getText());
		}
		dismiss();
	}



	public void init() {
		rackQwe = new Keyboard(getContext(), R.xml.rack_qwerty);
		onlyNum = new Keyboard(getContext(), R.xml.only_number);
		rackNum = new Keyboard(getContext(), R.xml.rack_number);
		double_ = new Keyboard(getContext(), R.xml.double_);
		idNum = new Keyboard(getContext(), R.xml.idnumber);
		carArea = new Keyboard(getContext(), R.xml.carnno_area);
		carNum = new Keyboard(getContext(), R.xml.carnno_123abc);

		this.ed = findViewById(R.id.edit);
		this.tv2 = findViewById(R.id.tv2);
		deleteBtn = findViewById(R.id.delBtn);
		keyboardView = findViewById(R.id.keyboard_view);
		keyboardView.setEnabled(true);
		keyboardView.setPreviewEnabled(false);
		keyboardView.setOnKeyboardActionListener(listener);

		deleteBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onKey(Keyboard.KEYCODE_DELETE, null);
			}
		});
		deleteBtn.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				ed.getEditableText().clear();
				return true;
			}
		});
		disableShowSoftInput();
	}

	/**
	 * 禁止Edittext弹出软件盘，光标依然正常显示。
	 */
	public void disableShowSoftInput() {
		if (android.os.Build.VERSION.SDK_INT <= 10) {
			ed.setInputType(InputType.TYPE_NULL);
		} else {
			Class<EditText> cls = EditText.class;
			Method method;
			try {
				method = cls
						.getMethod("setShowSoftInputOnFocus", boolean.class);
				method.setAccessible(true);
				method.invoke(ed, false);
			} catch (Exception e) {
				// TODO: handle exception
			}

			try {
				method = cls.getMethod("setSoftInputShownOnFocus",
						boolean.class);
				method.setAccessible(true);
				method.invoke(ed, false);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
		@Override
		public void swipeUp() {
		}

		@Override
		public void swipeRight() {
		}

		@Override
		public void swipeLeft() {
		}

		@Override
		public void swipeDown() {
		}

		@Override
		public void onText(CharSequence text) {
		}

		@Override
		public void onRelease(int primaryCode) {
		}

		@Override
		public void onPress(int primaryCode) {
		}

		@Override
		public void onKey(int primaryCode, int[] keyCodes) {
			Editable editable = ed.getText();
			int start = ed.getSelectionStart();
			if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
				submit();
			} else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
				if (editable != null && editable.length() > 0) {
					if (start > 0) {
						editable.delete(start - 1, start);
					}
				}
			} else if (primaryCode == Keyboard.KEYCODE_DONE) {// 小数点
				if (editable != null && !ed.getText().toString().contains(".") ) {
					editable.append(".");
				}else if (editable.length()<1){
					editable.append("0.");
				}
			} else if (primaryCode == Keyboard.KEYCODE_ALT) {// 清空
				if (editable != null && editable.length() > 0) {
					ed.setText("");
				}
			} else if (primaryCode == Keyboard.KEYCODE_SHIFT) {// 大小写切换
				changeKey();
				keyboardView.setKeyboard(rackQwe);

			} else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {// 数字键盘切换
				if (isnun) {
					isnun = false;
					keyboardView.setKeyboard(rackQwe);
				} else {
					isnun = true;
					keyboardView.setKeyboard(rackNum);
				}
			} else if (primaryCode == -8) {
				if (isArea) {
					keyboardView.setKeyboard(carNum);
					isArea = false;
				} else {
					keyboardView.setKeyboard(carArea);
					isArea = true;
				}

			} else if (primaryCode == 57419) { // go left
				if (start > 0) {
					ed.setSelection(start - 1);
				}
			} else if (primaryCode == 57421) { // go right
				if (start < ed.length()) {
					ed.setSelection(start + 1);
				}
			} else if (primaryCode == -10000) {

			} else if (primaryCode >= -132 && primaryCode <= -100) {
				editable.clear();
				start = 0;
				editable.insert(start, Character.toString(area.charAt(Math
						.abs(primaryCode) % 100)));

			}else if(primaryCode>=-2012 && primaryCode<=-2000){
				if(primaryCode !=-2010 && primaryCode!=-2011){
					editable.clear();
					start = 0;
				}
				editable.insert(start, Character.toString(jjCarStr.charAt(Math
						.abs(primaryCode) % 2000)));
			} else {
				if (type == INPUT_TYPE.RACKNO) {
					if (ed.getText().length() >= 17) {
						// showShortToast("车架号不能大于17位");
						ed.getText().toString().subSequence(0, 17);
						return;
					}
				}
				if (type == INPUT_TYPE.CARNO) {
					if (ed.getText().length() >= 8) {
						ed.getText().toString().subSequence(0, 8);
						return;
					}
				}
				editable.insert(start, Character.toString((char) primaryCode));
			}
		}
	};

	/**
	 * 键盘大小写切换
	 */
	private void changeKey() {
		List<Key> keylist = rackQwe.getKeys();
		if (isupper) {// 大写切换小写
			isupper = false;
			for (Key key : keylist) {
				if (key.label != null && isword(key.label.toString())) {
					key.label = key.label.toString().toLowerCase();
					key.codes[0] = key.codes[0] + 32;
				}
			}
		} else {// 小写切换大写
			isupper = true;
			for (Key key : keylist) {
				if (key.label != null && isword(key.label.toString())) {
					key.label = key.label.toString().toUpperCase();
					key.codes[0] = key.codes[0] - 32;
				}
			}
		}
	}

	public void showKeyboard() {
		int visibility = keyboardView.getVisibility();
		if (visibility == View.GONE || visibility == View.INVISIBLE) {
			keyboardView.setVisibility(View.VISIBLE);
		}
	}

	private boolean isword(String str) {
		String wordstr = "abcdefghijklmnopqrstuvwxyz";
		return wordstr.indexOf(str.toLowerCase()) > -1;
	}

	protected void showShortToast(String text) {
		Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
	}
}
