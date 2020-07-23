package com.estar.hh.survey.entity.vo.video;

import android.os.Parcel;
import android.os.Parcelable;


public class ResultVO implements Parcelable {
	/** 返回代码 **/
	private String code="";
	/** 返回提示信息 **/
	private String message="";

	public ResultVO() {
	}
	protected ResultVO(Parcel in) {
		code = in.readString();
		message = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(code);
		dest.writeString(message);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<ResultVO> CREATOR = new Creator<ResultVO>() {
		@Override
		public ResultVO createFromParcel(Parcel in) {
			return new ResultVO(in);
		}

		@Override
		public ResultVO[] newArray(int size) {
			return new ResultVO[size];
		}
	};

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
