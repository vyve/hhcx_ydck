package com.estar.hh.survey.entity.vo.video;

import android.os.Parcel;
import android.os.Parcelable;

import com.estar.hh.survey.entity.vo.HeadVO;

/**
 * Created by ding on 2015/11/2.
 */
public class ReponseResVideoLineVO implements Parcelable {
    private HeadVO head = new HeadVO();
    private ResVideoLineVO data = new ResVideoLineVO();
    private ResultVO result = new ResultVO();

    public HeadVO getHead() {
        return head;
    }

    public void setHead(HeadVO head) {
        this.head = head;
    }

    public ResVideoLineVO getData() {
        return data;
    }

    public void setData(ResVideoLineVO data) {
        this.data = data;
    }

    public ResultVO getResult() {
        return result;
    }

    public void setResult(ResultVO result) {
        this.result = result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.head, flags);
        dest.writeParcelable(this.data, flags);
        dest.writeParcelable(this.result, flags);
    }

    public ReponseResVideoLineVO() {
    }

    protected ReponseResVideoLineVO(Parcel in) {
        this.head = in.readParcelable(HeadVO.class.getClassLoader());
        this.data = in.readParcelable(ResVideoLineVO.class.getClassLoader());
        this.result = in.readParcelable(ResultVO.class.getClassLoader());
    }

    public static final Parcelable.Creator<ReponseResVideoLineVO> CREATOR = new Parcelable.Creator<ReponseResVideoLineVO>() {
        @Override
        public ReponseResVideoLineVO createFromParcel(Parcel source) {
            return new ReponseResVideoLineVO(source);
        }

        @Override
        public ReponseResVideoLineVO[] newArray(int size) {
            return new ReponseResVideoLineVO[size];
        }
    };
}
