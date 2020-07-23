package com.estar.hh.survey.view.activity.video;

import android.hardware.Camera;

import java.util.Comparator;

/**
 * 计算拍照时分辨率
 * Created by ding on 2015/12/15.
 */
public class ComparatorSize implements Comparator<Object>
{

    public int compare(Object arg0, Object arg1)
    {
        Camera.Size size0=(Camera.Size)arg0;
        Camera.Size size1=(Camera.Size)arg1;

        if(size0.width != size1.width)
        {
            return size0.width - size1.width;
        }
        else
        {
            return size0.height - size1.height;
        }
    }
}
