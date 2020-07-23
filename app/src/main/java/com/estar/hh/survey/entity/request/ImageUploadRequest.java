package com.estar.hh.survey.entity.request;

import com.estar.hh.survey.entity.entity.ImageUploadDto;

/**
 * Created by Administrator on 2017/12/18 0018.
 */

public class ImageUploadRequest {

    private ImageUploadDto data = new ImageUploadDto();

    public void setData(ImageUploadDto data) {
        this.data = data;
    }

    public ImageUploadDto getData() {
        return data;
    }
}
