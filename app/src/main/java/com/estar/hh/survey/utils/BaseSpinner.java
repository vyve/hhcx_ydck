package com.estar.hh.survey.utils;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.estar.hh.survey.R;
import com.estarview.CitemSpinner;
import com.rey.material.widget.Spinner;

import java.util.ArrayList;
import java.util.List;


public class BaseSpinner {
	private Context context;

	public BaseSpinner(Context c) {
		context = c;
	}

	/*
	 * 参数：控件，数组，默认值
	 */
	public void initSpinner(Spinner spinnerSpn, String[] spinnerTextArray,
                            String str) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, spinnerTextArray);
//		adapter
//				.setDropDownViewResource(R.layout.new_spinner_dropdown_item);
		spinnerSpn.setAdapter(adapter);
		if (null!=str  || !"".equals(str)) {
			spinnerSpn.setSelection(adapter.getPosition(str));

		}

	}

	/*
	 * 参数：控件，数组ID，默认值
	 */
	public void initSpinner(Spinner spinnerSpn, int array, String str) {
		String[] spinnerTextArray = context.getResources()
				.getStringArray(array);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, spinnerTextArray);
//		adapter
//				.setDropDownViewResource(R.layout.new_spinner_dropdown_item);
		spinnerSpn.setAdapter(adapter);
		if (str != null || !"".equals(str)) {
			spinnerSpn.setSelection(adapter.getPosition(str));

		}

	}

	public String getSpinner(Spinner spinnerSpn) {
		TextView textView = (TextView) spinnerSpn.getSelectedView();
		String spinnerText = textView.getText().toString().trim();
		return spinnerText;
	}

	/**
	 * 根据value键值对应的下拉框初始化
	 *
	 * @param spinnerSpn
	 *            下拉框
	 * @param valueId
	 *            XML中定义显示字符串数组value的ID
	 * @param keyId
	 *            XML中定义显示字符串数组所对应的key值字符串数组的ID
//	 * @param 传入默认选中的value值
	 *            ，
	 */
	public void initSprinnerByValue(Spinner spinnerSpn, int valueId, int keyId,
                                    String value) {
		CitemSpinner citemSpinner = null;
		List<CitemSpinner> list = new ArrayList<CitemSpinner>();
		String[] values = context.getResources().getStringArray(valueId);
		String[] keys = context.getResources().getStringArray(keyId);
		int count = values.length < keys.length ? values.length : keys.length;
		for (int i = 0; i < count; i++) {
			if (value != null || !"".equals(value)) {
				if (values[i].equals(value)) {
					citemSpinner = new CitemSpinner(keys[i], values[i]);
					list.add(citemSpinner);
				} else {
					CitemSpinner ct = new CitemSpinner(keys[i], values[i]);
					list.add(ct);
				}

			} else {
				CitemSpinner ct = new CitemSpinner(keys[i], values[i]);
				list.add(ct);
			}
		}
		ArrayAdapter<CitemSpinner> adapter = new ArrayAdapter<CitemSpinner>(context, R.layout.row_spn, list);
		adapter.setDropDownViewResource(R.layout.row_spn_dropdown);
		spinnerSpn.setAdapter(adapter);
		if (citemSpinner != null) {
			spinnerSpn.setSelection(adapter.getPosition(citemSpinner));
		}
	}




	/**
	 *
	 * @author MaBo  Data>>Dec 23, 2010
	 * <b>方法描述</b>：根据code找到value <p> 
	 * <b>方法流程</b>： <p> 
	 * @param
	 * @param valueId
	 * @param keyId
//	 * @param value
	 */
	public String getSpinnerVlaueByCode(int valueId, int keyId,
										String key) {
		String value="";
		String[] values = context.getResources().getStringArray(valueId);
		String[] keys = context.getResources().getStringArray(keyId);
		int count = values.length < keys.length ? values.length : keys.length;
		for (int i = 0; i < count; i++) {
			if(key!=null&&!key.equals("")){
				if(key.equals(keys[i])){
					value=values[i];
				}
			}
		}
		return value;

	}

	/**
	 *
	 * @author MaBo  Data>>Dec 23, 2010
	 * <b>方法描述</b>：根据code找到value <p> 
	 * <b>方法流程</b>： <p> 
	 * @param
//	 * @param valueId
//	 * @param keyId
//	 * @param value
	 */
	public String getSpinnerVlaueByCode(String[] values, String[] keys,
										String key) {
		String value="";
		int count = values.length < keys.length ? values.length : keys.length;
		for (int i = 0; i < count; i++) {
			if(key!=null&&!key.equals("")){
				if(key.equals(keys[i])){
					value=values[i];
				}
			}
		}
		return value;

	}
	/**
	 *
	 * @author MaBo  Data>>Dec 23, 2010
	 * <b>方法描述</b>：根据value找到code <p> 
	 * <b>方法流程</b>： <p> 
	 * @param
	 * @param valueId
	 * @param keyId
	 * @param value
	 */
	public String getSpinnerCodeByvalue(int valueId, int keyId,
										String value) {
		String code="";
		String[] values = context.getResources().getStringArray(valueId);
		String[] keys = context.getResources().getStringArray(keyId);
		int count = values.length < keys.length ? values.length : keys.length;
		for (int i = 0; i < count; i++) {
			if(null!=values){
				if(value.equals(values[i])){
					code=keys[i];
				}
			}
		}
		return code;

	}
	/**
	 *
	 * @author MaBo  Data>>Dec 23, 2010
	 * <b>方法描述</b>：根据value找到code <p> 
	 * <b>方法流程</b>： <p> 
	 * @param
//	 * @param valueId
//	 * @param keyId
//	 * @param value
	 */
	public String getSpinnerCodeByvalue(String[] values, String[] keys,
										String value) {
		String code="";
		int count = values.length < keys.length ? values.length : keys.length;
		for (int i = 0; i < count; i++) {
			if(null!=values){
				if(value.equals(values[i])){
					code=keys[i];
				}
			}
		}
		return code;

	}

	/**
	 *
	 * 根据key值键值对应的下拉框初始化
	 *
	 * @param spinnerSpn
	 *            下拉框
//	 * @param valueId
//	 *            定义显示字符串数组value
//	 * @param keyId
//	 *            定义显示字符串数组所对应的key值字符串数组
	 * @param key
	 *            传入默认选中的key值，可以为空
	 */
	public void initSprinnerByKey(Spinner spinnerSpn, String[] values,
                                  String[] keys, String key) {
		CitemSpinner citemSpinner = null;
		List<CitemSpinner> list = new ArrayList<CitemSpinner>();
		int count = values.length < keys.length ? values.length : keys.length;
		for (int i = 0; i < count; i++) {
			if (key != null || !"".equals(key)) {
				if (keys[i].equals(key)) {
					citemSpinner = new CitemSpinner(keys[i], values[i]);
					list.add(citemSpinner);
				} else {
					CitemSpinner ct = new CitemSpinner(keys[i], values[i]);
					list.add(ct);
				}

			} else {
				CitemSpinner ct = new CitemSpinner(keys[i], values[i]);
				list.add(ct);
			}
		}

		ArrayAdapter<CitemSpinner> adapter = new ArrayAdapter<CitemSpinner>(context, R.layout.row_spn, list);
		adapter.setDropDownViewResource(R.layout.row_spn_dropdown);

		spinnerSpn.setAdapter(adapter);
		if (citemSpinner != null) {
			spinnerSpn.setSelection(adapter.getPosition(citemSpinner));
		}


	}

	/**
	 * 根据value键值对应的下拉框初始化
	 *
	 * @param spinnerSpn
	 *            下拉框
//	 * @param valueId
//	 *            字符串数组value
//	 * @param keyId
//	 *            显示字符串数组所对应的key值字符串数组
//	 * @param str
//	 *            传入默认选中的value值，
	 */
	public void initSprinnerByValue(Spinner spinnerSpn, String[] values,
                                    String[] keys, String value) {
		CitemSpinner citemSpinner = null;
		List<CitemSpinner> list = new ArrayList<CitemSpinner>();
		int count = values.length < keys.length ? values.length : keys.length;
		for (int i = 0; i < count; i++) {
			if (value != null || !"".equals(value)) {
				if (values[i].equals(value)) {
					citemSpinner = new CitemSpinner(keys[i], values[i]);
					list.add(citemSpinner);
				} else {
					CitemSpinner ct = new CitemSpinner(keys[i], values[i]);
					list.add(ct);
				}

			} else {
				CitemSpinner ct = new CitemSpinner(keys[i], values[i]);
				list.add(ct);
			}
		}
		ArrayAdapter<CitemSpinner> adapter = new ArrayAdapter<CitemSpinner>(context, R.layout.row_spn, list);
		adapter.setDropDownViewResource(R.layout.row_spn_dropdown);
		spinnerSpn.setAdapter(adapter);
		if (citemSpinner != null) {
			spinnerSpn.setSelection(adapter.getPosition(citemSpinner));
		}
	}

	/**
	 * 根据key值键值对应的下拉框初始化
	 *
	 * @param spinnerSpn
	 *            下拉框
	 * @param valueId
	 *            XML中定义显示字符串数组value的ID
	 * @param keyId
	 *            XML中定义显示字符串数组所对应的key值字符串数组的ID
	 * @param key
	 *            传入默认选中的key值，可以为空
	 */
	public void initSprinnerByKey(Spinner spinnerSpn, int valueId, int keyId,
                                  String key) {
		CitemSpinner citemSpinner = null;
		List<CitemSpinner> list = new ArrayList<CitemSpinner>();
		String[] values = context.getResources().getStringArray(valueId);
		String[] keys = context.getResources().getStringArray(keyId);
		int count = values.length < keys.length ? values.length : keys.length;
		for (int i = 0; i < count; i++) {
			if (key != null || !"".equals(key)) {
				if (keys[i].equals(key)) {
					citemSpinner = new CitemSpinner(keys[i], values[i]);
					list.add(citemSpinner);
				} else {
					CitemSpinner ct = new CitemSpinner(keys[i], values[i]);
					list.add(ct);
				}

			} else {
				CitemSpinner ct = new CitemSpinner(keys[i], values[i]);
				list.add(ct);
			}
		}

		ArrayAdapter<CitemSpinner> adapter = new ArrayAdapter<CitemSpinner>(context, R.layout.row_spn, list);
		adapter.setDropDownViewResource(R.layout.row_spn_dropdown_camera_new);

		spinnerSpn.setAdapter(adapter);
		if (citemSpinner != null) {
			spinnerSpn.setSelection(adapter.getPosition(citemSpinner));
		}
	}

	/**
	 * 根据key值键值对应的下拉框初始化
	 *
	 * @param spinnerSpn
	 *            下拉框
	 * @param valueId
	 *            XML中定义显示字符串数组value的ID
	 * @param keyId
	 *            XML中定义显示字符串数组所对应的key值字符串数组的ID
	 * @param key
	 *            传入默认选中的key值，可以为空
	 */
	public void initSprinnerByKey2(Spinner spinnerSpn, int valueId, int keyId,
                                   String key) {
		CitemSpinner citemSpinner = null;
		List<CitemSpinner> list = new ArrayList<CitemSpinner>();
		String[] values = context.getResources().getStringArray(valueId);
		String[] keys = context.getResources().getStringArray(keyId);
		int count = values.length < keys.length ? values.length : keys.length;
		for (int i = 0; i < count; i++) {
			if (key != null || !"".equals(key)) {
				if (keys[i].equals(key)) {
					citemSpinner = new CitemSpinner(keys[i], values[i]);
					list.add(citemSpinner);
				} else {
					CitemSpinner ct = new CitemSpinner(keys[i], values[i]);
					list.add(ct);
				}

			} else {
				CitemSpinner ct = new CitemSpinner(keys[i], values[i]);
				list.add(ct);
			}
		}

		ArrayAdapter<CitemSpinner> adapter = new ArrayAdapter<CitemSpinner>(context, R.layout.row_spn2, list);
		adapter.setDropDownViewResource(R.layout.row_spn_dropdown);

		spinnerSpn.setAdapter(adapter);
		if (citemSpinner != null) {
			spinnerSpn.setSelection(adapter.getPosition(citemSpinner));
		}
	}

	/**
	 * 根据key值键值对应的下拉框初始化
	 *
	 * @param spinnerSpn
	 *            下拉框
	 * @param values
	 *            XML中定义显示字符串数组value的ID
	 * @param keys
	 *            XML中定义显示字符串数组所对应的key值字符串数组的ID
	 * @param key
	 *            传入默认选中的key值，可以为空
	 */
	public void initSprinnerByKey2(Spinner spinnerSpn, String[] values, String[] keys,
                                   String key) {
		CitemSpinner citemSpinner = null;
		List<CitemSpinner> list = new ArrayList<CitemSpinner>();
		int count = values.length < keys.length ? values.length : keys.length;
		for (int i = 0; i < count; i++) {
			if (key != null || !"".equals(key)) {
				if (keys[i].equals(key)) {
					citemSpinner = new CitemSpinner(keys[i], values[i]);
					list.add(citemSpinner);
				} else {
					CitemSpinner ct = new CitemSpinner(keys[i], values[i]);
					list.add(ct);
				}

			} else {
				CitemSpinner ct = new CitemSpinner(keys[i], values[i]);
				list.add(ct);
			}
		}

		ArrayAdapter<CitemSpinner> adapter = new ArrayAdapter<CitemSpinner>(context, R.layout.row_spn2, list);
		adapter.setDropDownViewResource(R.layout.row_spn_dropdown);

		spinnerSpn.setAdapter(adapter);
		if (citemSpinner != null) {
			spinnerSpn.setSelection(adapter.getPosition(citemSpinner));
		}
	}

	/**
	 * 获取选中的key值
	 *
	 * @param spinnerSpn
	 *            所要获取的下拉框
	 * @return 返回key值
	 */
	public String getSpinnerKey(Spinner spinnerSpn) {
		String spinnerId = ((CitemSpinner) spinnerSpn.getSelectedItem()).getId();
		return spinnerId;
	}

	/**
	 * 获取选中的value值
	 *
	 * @param spinnerSpn
	 *            所要获取的下拉框
	 * @return 返回value值
	 */
	public String getSpinnerValue(Spinner spinnerSpn) {
		String spinnerValue = ((CitemSpinner) spinnerSpn.getSelectedItem()).getValue();
		return spinnerValue;
	}

	/**
	 * 根据xml文件中的key获取对应value值 --add by dingding
	 */
	public String getVlaueByCode(int valueId, int keyId,
								 String key) {
		String value="";
		String[] values = context.getResources().getStringArray(valueId);
		String[] keys = context.getResources().getStringArray(keyId);
		int count = values.length < keys.length ? values.length : keys.length;
		for (int i = 0; i < count; i++) {
			if(key!=null&&!key.equals("")){
				if(key.equals(keys[i])){
					value=values[i];
				}
			}
		}
		return value;
	}

}