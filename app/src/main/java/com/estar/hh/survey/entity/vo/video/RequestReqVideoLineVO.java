package com.estar.hh.survey.entity.vo.video;

import android.os.Parcel;
import android.os.Parcelable;

import com.estar.hh.survey.entity.vo.HeadVO;


/**
 * Created by sth on 20157/7/9
 */
public class RequestReqVideoLineVO implements Parcelable {
    private HeadVO head = new HeadVO();
    private ReqVideoLineVO data = new ReqVideoLineVO();

    public HeadVO getHead() {
        return head;
    }

    public void setHead(HeadVO head) {
        this.head = head;
    }

    public ReqVideoLineVO getData() {
        return data;
    }

    public void setData(ReqVideoLineVO data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.head, 0);
        dest.writeParcelable(this.data, 0);
    }

    public RequestReqVideoLineVO() {
    }

    protected RequestReqVideoLineVO(Parcel in) {
        this.head = in.readParcelable(HeadVO.class.getClassLoader());
        this.data = in.readParcelable(ReqVideoLineVO.class.getClassLoader());
    }

    public static final Creator<RequestReqVideoLineVO> CREATOR = new Creator<RequestReqVideoLineVO>() {
        public RequestReqVideoLineVO createFromParcel(Parcel source) {
            return new RequestReqVideoLineVO(source);
        }

        public RequestReqVideoLineVO[] newArray(int size) {
            return new RequestReqVideoLineVO[size];
        }
    };
}
