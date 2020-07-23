package com.estar.hh.survey.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class MD5Util
{
    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7",
        "8", "9", "a", "b", "c", "d", "e", "f"};
    /**
     * MD5加密算法
     * @param source
     * @return
     */
     public static String getMD5(byte[] source) {
          String s = null;
          char[] hexDigits = {       // 用来将字节转换成 16 进制表示的字符
             '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',  'e', 'f'}; 
           try
           {
            MessageDigest md = MessageDigest.getInstance( "MD5" );
            md.update( source );
            byte[] tmp = md.digest();          // MD5 的计算结果是一个 128 位的长整数，
                                                        // 用字节表示就是 16 个字节
            char[] str = new char[16 * 2];   // 每个字节用 16 进制表示的话，使用两个字符，
                                                         // 所以表示成 16 进制需要 32 个字符
            int k = 0;                                // 表示转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) {          // 从第一个字节开始，对 MD5 的每一个字节
                                                         // 转换成 16 进制字符的转换
             byte byte0 = tmp[i];                 // 取第 i 个字节
             str[k++] = hexDigits[byte0 >>> 4 & 0xf];  // 取字节中高 4 位的数字转换, 
                                                                     // >>> 为逻辑右移，将符号位一起右移
             str[k++] = hexDigits[byte0 & 0xf];            // 取字节中低 4 位的数字转换
            } 
            s = new String(str);                                 // 换后的结果转换为字符串

           }catch( Exception e )
           {
            e.printStackTrace();
           }
           return s;
         }
     /**
      * 转换字节数组为16进制字串
      * @param b 字节数组
      * @return 16进制字串
      */
     public static String byteArrayToHexString(byte[] b) {
         StringBuilder resultSb = new StringBuilder();
         for (byte aB : b) {
             resultSb.append(byteToHexString(aB));
         }
         return resultSb.toString();
     }

     /**
      * 转换byte到16进制
      * @param b 要转换的byte
      * @return 16进制格式
      */
     private static String byteToHexString(byte b) {
         int n = b;
         if (n < 0) {
             n = 256 + n;
         }
         int d1 = n / 16;
         int d2 = n % 16;
         return hexDigits[d1] + hexDigits[d2];
     }

     /**
      * MD5编码
      * @param origin 原始字符串
      * @return 经过MD5加密之后的结果
      */
     public static String MD5Encode(String origin) {
         String resultString = null;
         try {
             resultString = origin;
             MessageDigest md = MessageDigest.getInstance("MD5");
             md.update(resultString.getBytes(StandardCharsets.UTF_8));
             resultString = byteArrayToHexString(md.digest());
         } catch (Exception e) {
             e.printStackTrace();
         }
         return resultString;
     }
     
     
//	  public static void main(String[] args) {
//		  String orderId="1";
//		  String str=MD5Util.MD5Encode(orderId);
//		  System.out.println("加密后:"+str.toUpperCase());
////		  System.out.println("返元:"+EncryptUtils.decryptData2(str));
//	  }
}
