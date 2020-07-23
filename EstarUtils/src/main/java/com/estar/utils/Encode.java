package com.estar.utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 加密解密算法，使用异或运算
 * Created by ding on 2016/7/19.
 */
public class Encode {

    private static final String key0 = "<ZSBX@DING>";
    private static final Charset charset = StandardCharsets.UTF_8;
    private static byte[] keyBytes = key0.getBytes(charset);

    /**
     * 加密
     * @param enc
     * @return
     */
    public static String encode(String enc){
        byte[] b = enc.getBytes(charset);
        for(int i=0,size=b.length;i<size;i++){
            for(byte keyBytes0:keyBytes){
                b[i] = (byte) (b[i]^keyBytes0);
            }
        }
        return new String(b);
    }

    /**
     * 解密
     * @param dec
     * @return
     */
    public static String decode(String dec){
        byte[] e = dec.getBytes(charset);
        byte[] dee = e;
        for(int i=0,size=e.length;i<size;i++){
            for(byte keyBytes0:keyBytes){
                e[i] = (byte) (dee[i]^keyBytes0);
            }
        }
        return new String(e);
    }
}
