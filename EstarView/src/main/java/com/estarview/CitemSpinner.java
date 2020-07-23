package com.estarview;

public class CitemSpinner {
	private String id;
	private String value;

	public CitemSpinner() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CitemSpinner(String id, String value) {
		super();
		this.id = id;
		this.value = value;
	}

	public String toString() { // 为什么要重写toString()呢？因为适配器在显示数据的时候，如果传入适配器的对象不是字符串的情况下，直接就使用对象.toString()
		// TODO Auto-generated method stub
		return value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
