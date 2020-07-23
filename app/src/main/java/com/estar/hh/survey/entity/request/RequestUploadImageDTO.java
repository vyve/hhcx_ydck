package com.estar.hh.survey.entity.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.estar.hh.survey.entity.vo.HeadVO;
import com.estar.hh.survey.entity.vo.UploadImageDTO;


/**
 *案件列表查询
 * Created by Administrator on 2015/10/13.
 */
public class RequestUploadImageDTO implements Parcelable {
    private HeadVO head = new HeadVO();
    private UploadImageDTO data = new UploadImageDTO();

    public HeadVO getHead() {
        return head;
    }

    public void setHead(HeadVO head) {
        this.head = head;
    }

    public UploadImageDTO getData() {
        return data;
    }

    public void setData(UploadImageDTO data) {
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

    public RequestUploadImageDTO() {
    }

    protected RequestUploadImageDTO(Parcel in) {
        this.head = in.readParcelable(HeadVO.class.getClassLoader());
        this.data = in.readParcelable(UploadImageDTO.class.getClassLoader());
    }

    public static final Creator<RequestUploadImageDTO> CREATOR = new Creator<RequestUploadImageDTO>() {
        public RequestUploadImageDTO createFromParcel(Parcel source) {
            return new RequestUploadImageDTO(source);
        }

        public RequestUploadImageDTO[] newArray(int size) {
            return new RequestUploadImageDTO[size];
        }
    };
}
