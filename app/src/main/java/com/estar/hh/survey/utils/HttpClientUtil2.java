/**
 *
 */
package com.estar.hh.survey.utils;

import android.util.Log;

import com.estar.hh.survey.constants.Constants;
import com.estar.hh.survey.entity.entity.User;
import com.estar.hh.survey.entity.request.LoginRequest;
import com.google.gson.Gson;

import org.litepal.crud.DataSupport;
import org.xutils.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;

/**
 * @Description:〈HttpClient工具类〉<br/>
 * @Provider:广州易星科技有限公司 <br/>
 * @Author: wuhan@estar-info.com<br/>
 * @Date:2018年3月5日
 */
public class HttpClientUtil2 {

    public static final String APPID = "1";//1.移动销售; 2.移动查勘;


    /**
     * @param
     * @param appId        APP标识
     * @param appSecret    秘钥
     * @param timestamp    时间戳
     * @param
     * @return
     * @Description：生成鉴权码 <br/>
     * @Author：wanghx@estar-info.com <br/>
     * @Date：2017/09/04 <br/>
     */
    public static String getSign(String appId, String timestamp, String appSecret, String userName, String msgBody) {
//        String content = appId + timestamp + randomNumber + appSecret;
        String content = appId + timestamp + appSecret + userName + msgBody;
        String token = md5(content);
        return token;
    }


    /**
     * MD5加密
     *
     * @param content
     * @return
     */
    public static String md5(String content) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(content.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }


    public static RequestParams getHttpRequestParam(String interfaceName, String requestMsg) {

        // 添加http头信息
        RequestParams params = new RequestParams(interfaceName);
        params.setAsJsonContent(true);
        params.setBodyContent(requestMsg);

        String randomNumber = "cba6fbed-4363-40b4-b844-4324fe1e8e1b";
        String timestamp = String.valueOf(new Date().getTime());
        String version = "1";
        String appSecret = "65537";
        //添加头部
        if (!interfaceName.equals(Constants.LOGIN)) {
            User user = DataSupport.findFirst(User.class);
            params.addHeader("userToken", user.getUsertoken());
            LogUtils.i("======Heander-userToken", user.getUsertoken());
        }
        LoginRequest request = new LoginRequest();
        Gson gson = new Gson();
        request = gson.fromJson(requestMsg,LoginRequest.class);
//        User user = DataSupport.findFirst(User.class);
        String[] a = interfaceName.split("esType=");
        params.addHeader("token", getSign(a[1], timestamp, appSecret, request.getData().getTelNo(), ASCIISort(requestMsg)).toUpperCase());
//        params.addHeader("appId", APPID);
//        params.addHeader("version", version);
//        params.addHeader("randomNumber", randomNumber);
        params.addHeader("timestamp", timestamp);
        params.addHeader("userName", request.getData().getTelNo());
        return params;
    }

    public static String ASCIISort(String str) {
        char[] test = new char[str.length()];
        StringBuilder sb = new StringBuilder();
        while (true) {
            String a = str;//直接读取这行当中的字符串。
            for (int i = 0; i < str.length(); i++) {
                //字符串处理每次读取一位。
                test[i] = a.charAt(i);
            }
            Arrays.sort(test);
            for (int i = 0; i < test.length; i++) {
                sb.append(test[i]);
            }
            String trim = sb.toString().trim();
            return trim;
        }
    }

}
