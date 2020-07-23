package com.estar.utils;

import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * 手机号  每3、4个空一个空格
 * @author Administrator
 *
 */

public class PhoneWatcher implements TextWatcher {

	private EditText et;
	private String pn;
	public PhoneWatcher(EditText et) {
		this.et = et;
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		if (count == 1){
			int length = s.toString().length();
			if (length == 3 || length == 8){
				et.setText(s + " ");
				et.setSelection(et.getText().toString().length());
			}
		}
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
	}

	public void afterTextChanged(Editable s) {
		if(et.getText().toString().length()==13){
			pn=deleteSpace(et.getText().toString());
//			LogUtil.w("pn="+pn);
			if(!isMobileNO(pn)){
				et.setError(Html.fromHtml("<font color=#C7000A>"+"请填写正确的手机号码"+"</font>"));
				et.requestFocus();
			}
		}

	}

	/**
	 * 验证手机格式
	 */
	public boolean isMobileNO(String mobiles){

		  /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8、7，其他位置的可以为0-9
    */
//		String telRegex = "[1][3578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		String telRegex = "[1]\\d{10}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobiles)) return false;
		else return mobiles.matches(telRegex);
		}

	/**
	 * 去掉字符串空格
	 * @param str
	 * @return
	 */
	public String deleteSpace(String str){
		return replaceNULL(str).replaceAll(" ", "");
	}
	/**
	 * 将null替换为""
	 *
	 * @param str
	 * @return
	 */
	public String replaceNULL(String str) {
		return null == str || "null".equals(str) || "".equals(str.trim()) ? "" : str.trim();
	}
}
