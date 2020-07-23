package com.estar.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;
/**
 * EditText 控件输入 数字、字母提示
 *
 */
public class EditTextWatcher implements TextWatcher {
	private int editStart;
	private int editEnd;
	private EditText editText;// 输入
	private String mes = "";// 错误提示
	private EditTextWatcherInterface estarWatcherInterface;// 接口，回调验证数据用，暂时没用
	private Context context;// 上下文

	/**
	 *
	 * @param context
	 *            上下文
	 * @param et
	 *            EditText
	 * @param mes
	 *            错误消息
//	 * @param pattern
//	 *            正则表达式
	 */
	public EditTextWatcher(Context context, EditText et, String mes,
						   EditTextWatcherInterface estarWatcherInterface) {
		this.editText = et;
		this.mes = mes;
		this.estarWatcherInterface = estarWatcherInterface;
		this.context = context;
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	public void beforeTextChanged(CharSequence s, int start, int count,
								  int after) {
	}

	public void afterTextChanged(Editable s) {
		editStart = editText.getSelectionStart();
		editEnd = editText.getSelectionEnd();

		// 验证正则
		if (estarWatcherInterface.checkInput(s)) {
			// 错误提示
			Toast.makeText(context, mes, Toast.LENGTH_SHORT).show();
			int _editStart = editStart - 1;
			if (_editStart < 0)
				_editStart = 0;
			// 删除掉了错误提示，更新数据
			s.delete(_editStart, editEnd);
			if(_editStart!=0)
				editText.setText(s);
			editText.setSelection(s.length());

		}

	}

	/**
	 * 回调借口
	 *
	 * @author estar
	 *
	 */
	public	interface EditTextWatcherInterface {
		boolean checkInput(Editable s);
	}

}
