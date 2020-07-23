package com.estar.hh.survey.entity.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class MsgVO implements Parcelable {

	private String name="";
	private String msg="";
	private String optionType="";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getOptionType() {
		return optionType;
	}

	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.name);
		dest.writeString(this.msg);
		dest.writeString(this.optionType);
	}

	public MsgVO() {
	}

	protected MsgVO(Parcel in) {
		this.name = in.readString();
		this.msg = in.readString();
		this.optionType = in.readString();
	}

	public static final Creator<MsgVO> CREATOR = new Creator<MsgVO>() {
		public MsgVO createFromParcel(Parcel source) {
			return new MsgVO(source);
		}

		public MsgVO[] newArray(int size) {
			return new MsgVO[size];
		}
	};
}
