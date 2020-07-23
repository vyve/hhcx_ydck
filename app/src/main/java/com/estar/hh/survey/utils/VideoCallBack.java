package com.estar.hh.survey.utils;

/**
 */
public interface VideoCallBack {
    /**
     * @fn onPictureData
     * @brief 图片数据回调
     * @param data
     *            - 图片数据(OUT)
     * @param dataType
     *            - 图片类型(OUT)
     * @return 无
     */
    void onPictureData(byte[] data, int dataType);

    /**
     * @fn onMsg
     * @brief 消息回调
     * @param msg
     *            - 消息类型(OUT)
     * @param msgText
     *            - 消息文字(OUT)
     * @return 无
     */
    void onMsg(int msg, String msgText);
}
