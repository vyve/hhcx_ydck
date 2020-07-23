package com.estar.hh.survey.utils;


import android.text.TextUtils;
import android.util.Base64;


import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class AesUtil {
    /**
     * @author ngh
     * AES128 算法
     * <p>
     * CBC 模式
     * <p>
     * PKCS7Padding 填充模式
     * <p>
     * CBC模式需要添加偏移量参数iv，必须16位
     * 密钥 sessionKey，必须16位
     * <p>
     * 介于java 不支持PKCS7Padding，只支持PKCS5Padding 但是PKCS7Padding 和 PKCS5Padding 没有什么区别
     * 要实现在java端用PKCS7Padding填充，需要用到bouncycastle组件来实现
     */
    private final String sessionKey = "0102030405060708";
    // 偏移量 16位
    private final String iv = "0102030405060708";

    // 算法名称
    final String KEY_ALGORITHM = "PKCS7Padding";
    // 加解密算法/模式/填充方式
    final String algorithmStr = "AES/CBC/PKCS7Padding";
    // 加解密 密钥 16位

    byte[] ivByte;
    byte[] keybytes;
    private Key key;
    private Cipher cipher;
    boolean isInited = false;

    public void init() {
        // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
        keybytes = sessionKey.getBytes();
        ivByte = iv.getBytes();
        // 初始化
        Security.addProvider(new BouncyCastleProvider());
        // 转化成JAVA的密钥格式
        key = new SecretKeySpec(keybytes, KEY_ALGORITHM);
        try {
            // 初始化cipher
            cipher = Cipher.getInstance(algorithmStr, "BC");
        } catch (NoSuchAlgorithmException e) {
//            Klog.Companion.d(e.toString());
        } catch (NoSuchPaddingException e) {
//            Klog.Companion.d(e.toString());
        } catch (NoSuchProviderException e) {
//            Klog.Companion.d(e.toString());
        }
    }

    /**
     * 加密方法
     *
     * @param content  要加密的字符串
     * @return
     */
    public String encrypt(String content) {
        if(TextUtils.isEmpty(content)){
            return "";
        }
        byte[] encryptedText = null;
        byte[] contentByte = content.getBytes();

        init();
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(ivByte));
            encryptedText = cipher.doFinal(contentByte);

            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < encryptedText.length; i++) {
                stringBuilder.append(encryptedText[i]);
            }
        } catch (Exception e) {
//            Klog.Companion.d(e.toString());
        }
        return new String(Base64.encode(encryptedText,Base64.NO_WRAP));
    }

    /**
     * 解密方法
     *
     * @param encryptedData 要解密的字符串
     * @return
     */
    public String decrypt(String encryptedData) {
        if(TextUtils.isEmpty(encryptedData)){
            return "";
        }
        byte[] encryptedText = null;
        byte[] encryptedDataByte = Base64.decode(encryptedData,Base64.NO_WRAP);
        init();
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivByte));
            encryptedText = cipher.doFinal(encryptedDataByte);
        } catch (Exception e) {
            return "";
        }
        return new String(encryptedText);
    }


}