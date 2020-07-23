package com.estar.hh.survey.utils;

import org.xutils.http.RequestParams;

/**
 * Created by Administrator on 2017/11/17 0017.
 * 网络请求封装工具类
 */

public class HttpUtils {


    /**
     * 构建请求参数
     * @param url
     * @param msg
     */
    public static RequestParams buildRequestParam(String url, String msg){
        RequestParams params = new RequestParams(url);
        params.setAsJsonContent(true);
        params.setBodyContent(msg);

        return params;
    }

}
