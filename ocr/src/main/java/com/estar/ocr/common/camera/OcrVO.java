package com.estar.ocr.common.camera;

import com.estar.ocr.common.ValueForKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/1.
 */

public class OcrVO implements Serializable{

    private List<ValueForKey> list = new ArrayList<>();

    public void setList(List<ValueForKey> list) {
        this.list = list;
    }

    public List<ValueForKey> getList() {
        return list;
    }
}
