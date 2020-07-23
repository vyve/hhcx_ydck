package com.estar.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 常用工具类
 * Created by ding on 2016/5/23.
 */
public class CommonUtils {

    public static final String SYS_EMUI = "sys_emui";
    public static final String SYS_MIUI = "sys_miui";
    public static final String SYS_FLYME = "sys_flyme";
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
    private static final String KEY_EMUI_API_LEVEL = "ro.build.hw_emui_api_level";
    private static final String KEY_EMUI_VERSION = "ro.build.version.emui";
    private static final String KEY_EMUI_CONFIG_HW_SYS_VERSION = "ro.confg.hw_systemversion";

    /**
     * 获取手机厂商 华为、小米、魅族
     * @return
     */
    public static String getSystem(){
        String SYS = "";
        try {
            Properties prop= new Properties();
            prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            if(prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null){
                SYS = SYS_MIUI;//小米
            }else if(prop.getProperty(KEY_EMUI_API_LEVEL, null) != null
                    ||prop.getProperty(KEY_EMUI_VERSION, null) != null
                    ||prop.getProperty(KEY_EMUI_CONFIG_HW_SYS_VERSION, null) != null){
                SYS = SYS_EMUI;//华为
            }else if(getMeizuFlymeOSFlag().toLowerCase().contains("flyme")){
                SYS = SYS_FLYME;//魅族
            }
        } catch (IOException e){
            e.printStackTrace();
            return SYS;
        }
        return SYS;
    }

    public static String getMeizuFlymeOSFlag() {
        return getSystemProperty("ro.build.display.id", "");
    }

    private static String getSystemProperty(String key, String defaultValue) {
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method get = clz.getMethod("get", String.class, String.class);
            return (String)get.invoke(clz, key, defaultValue);
        } catch (Exception e) {
        }
        return defaultValue;
    }

    /**
    px转换dp
     */
    public static int dp2px(Context context,int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }
    /**
     * 用来判断服务是否运行.
     * @param
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRunning(Context mContext,String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)
                mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList
                = activityManager.getRunningServices(30);
        if (!(serviceList.size()>0)) {
            return false;
        }
        for (int i=0; i<serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
    /**
     * 生成二维码
     * @param url  二维码链接
     * @param width  图片宽
     * @param height
     * @return
     */
    public static Bitmap createImage(String url,int width,int height) {
        try {
            // 需要引入core包
            QRCodeWriter writer = new QRCodeWriter();



            if (url == null || "".equals(url) || url.length() < 1) {
                return null;
            }

            // 把输入的文本转为二维码
            BitMatrix martix = writer.encode(url, BarcodeFormat.QR_CODE,
                    width, height);


            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(url,
                    BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    } else {
                        pixels[y * width + x] = 0xffffffff;
                    }

                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);

            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 去掉字符串空格
     * @param str
     * @return
     */
    public static String deleteSpace(String str){
        return replaceNULL(str).replaceAll(" ", "");
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8、7，其他位置的可以为0-9
    */
//        String telRegex = "[1][3578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "[1]\\d{10}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }
    //判断email格式是否正确
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    /**
     * 比较date1-date2差几天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int dateDiff(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        long ldate1 = date1.getTime() + cal1.get(Calendar.ZONE_OFFSET) + cal1.get(Calendar.DST_OFFSET);
        long ldate2 = date2.getTime() + cal2.get(Calendar.ZONE_OFFSET) + cal2.get(Calendar.DST_OFFSET);
        // Use integer calculation, truncate the decimals
        int hr1 = (int) (ldate1 / 3600000); // 60*60*1000
        int hr2 = (int) (ldate2 / 3600000);

        int days1 = hr1 / 24;
        int days2 = hr2 / 24;

        int dateDiff = days1 - days2;
        return dateDiff;
    }

    /**
     *
     * 得到两个日期之间的相差的自然月数
     * @param nowDate
     * @param oldDate
     * @return
     * @throws ParseException
     */
    public static int getYearMounthDays(String nowDate, String oldDate){
        int result = 0;
        int result_M = 0;
        int result_Y = 0;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        try {
            c1.setTime(sdf.parse(nowDate));
            c2.setTime(sdf.parse(oldDate));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        result_M = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
        result_Y = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
        result=result_M+result_Y*12;
//        Log.e("CarQuoteActivity","  nowDate="+nowDate+"  oldDate="+oldDate+
//                "  result_M="+result_M+"  result_Y="+result_Y+"  result="+result);
        return Math.abs(result);
    }
    /**
     * 计算两个时间相差天数
     * @param str1
     * @param str2
     * @return
     */
    public static int getIntervalDays(String str1, String str2) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = formatter.parse(str1);
            date2 = formatter.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int dif = dateDiff(date1,date2);

        return dif;


    }

    /**
     * 计算两个时间相差天数
     * @param fDate
     * @param oDate
     * @return
     */
    public static int daysOfTwo(Date fDate, Date oDate) {

        if (null == fDate || null == oDate) {

            return -1;

        }

        long intervalMilli = oDate.getTime() - fDate.getTime();
        int re=(int) (intervalMilli / (24 * 60 * 60 * 1000));
        return Math.abs(re);

    }

    /**
     * 判断查询时间端 合法
     * @param date1
     * @param date2
     * @return  0 date1 相等 date2   date1 小于 date2  date1 大于 date2
     */
    public static Integer compareTime(String date1,String date2){
        java.text.DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        java.util.Calendar c1=java.util.Calendar.getInstance();
        java.util.Calendar c2=java.util.Calendar.getInstance();
        try
        {
            c1.setTime(df.parse(date1));
            c2.setTime(df.parse(date2));
        }catch(java.text.ParseException e){
//            System.err.println("格式不正确");
        }
        int result=c1.compareTo(c2);
        if(result==0){
//            System.out.println("c1相等c2");
        }else if(result<0) {
//            System.out.println("c1小于c2");
        }else {
//            System.out.println("c1大于c2");

        }
        return result;
    }

    /**
     * 将null替换为""
     *
     * @param str
     * @return
     */
    public static String replaceNULL(String str) {
        return null == str || "null".equals(str) || "".equals(str.trim()) ? "" : str.trim();
    }

    /**
     * EditText 控件输入 数字、字母提示
     *
     * @param view
     */
    public static void addTextChangedListener(Activity activity,EditText view) {
        view.addTextChangedListener(new EditTextWatcher(activity, view, "只能输入数字或字母", new EditTextWatcher.EditTextWatcherInterface() {
            public boolean checkInput(Editable editTextString) {
                return checkfilename(editTextString);
            }
        }));
    }


    //校验只能输入数字和字母
    public static boolean checkfilename(Editable s) {
        String str = s.toString();

        if (str.length() > s.toString().trim().length()) {
            return true;
        } else {
            str = s.toString().trim();
        }
        String pattern = "^[A-Za-z0-9*]+$";
        Pattern p = Pattern.compile(pattern);
        Matcher result = p.matcher(str);
        return !result.find();
    }

    /**
     * str 转 double  四舍五入  保留两位小数
     * @return
     */
    public static double getDoubleForString(String str){
        if(replaceNULL(str).equals("")){
            str ="0";
        }
        if (isDouble(str)){
            double db=Double.parseDouble(str);
            BigDecimal b=new BigDecimal(db);
            double d=b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            return d;
        }
       return 0;
    }

    /**
     * 四舍五入  保留两位小数
     * @param db
     * @return
     */
    public static float getForFloat(float db){
        BigDecimal b=new BigDecimal(db);
        float d=b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        return d;
    }
    /**
     * 四舍五入  保留两位小数
     * @param db
     * @return
     */
    public static double getForDouble(double db){
        BigDecimal b=new BigDecimal(db);
        double d=b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return d;
    }
    /**
     * 判断字符是否double类型
     * @param str
     * @return
     */
    public static boolean isDouble(String str)
    {
        try
        {
            Double.parseDouble(str);
            return true;
        }
        catch(NumberFormatException ex){
        }
        return false;
    }
    /**
     * 初始化标题
     *
     * @param str
     * @return
     */
    public static Spannable getTitle(String str) {
        Spannable wordtoSpan = new SpannableString(str);

        wordtoSpan = new SpannableString(str);
        wordtoSpan.setSpan(new RelativeSizeSpan(1f), 0, str.indexOf("-"),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new RelativeSizeSpan(0.8f), str.lastIndexOf("-"),
                str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return wordtoSpan;
    }

    /**
     * EditText设置错误提示信息
     * @param editTextt
     * @param msg
     */
    public static void setEditEorr(EditText editTextt,String msg){
            editTextt.setError(Html.fromHtml("<font color=#C7000A>"+msg+"</font>"));
            editTextt.requestFocus();
    }

    /**
     * EditText非空判断
     * @param editTextt
     * @param msg
     * @return
     */
    public static boolean setEditTextEorr(EditText editTextt,String msg){

        if ("".equals(editTextt.getText().toString().trim())) {
            editTextt.setError(Html.fromHtml("<font color=#C7000A>"+msg+"</font>"));
            editTextt.requestFocus();
            return true;
        }
        return false;
    }

    /**
     * TextView非空判断
     * @param textView
     * @param msg
     * @return
     */
    public static boolean setTextViewEorr(TextView textView, String msg){
        if ("".equals(textView.getText().toString().trim())) {
//                    actv_carNme.setError("车型名称不能为空!");
            textView.setError(Html.fromHtml("<font color=#C7000A>"+msg+"</font>"));
            textView.requestFocus();
            return true;
        }
        return false;
    }

    /**
     * 根据key值键值对应的下拉框初始化
     *
     * @param valueId XML中定义显示字符串数组value的ID
     * @param keyId   XML中定义显示字符串数组所对应的key值字符串数组的ID
     * @param key     传入默认选中的key值，可以为空
     */
    public static String initViewByKey(Context context,  int valueId, int keyId, String key) {
        String[] values = context.getResources().getStringArray(valueId);
        String[] keys = context.getResources().getStringArray(keyId);
        int count = values.length < keys.length ? values.length : keys.length;
        for (int i = 0; i < count; i++) {
            if (key != null && !"".equals(key)) {
                if (keys[i].equals(key)) {
                    return values[i];
                }

            }
        }
        return "";
    }
    /**
     * 根据key值键值对应的下拉框初始化
     *
     * @param key     传入默认选中的key值，可以为空
     */
    public static String initViewByKey(Context context,  String[] values, String[] keys, String key) {
        int count = values.length < keys.length ? values.length : keys.length;
        for (int i = 0; i < count; i++) {
            if (key != null && !"".equals(key)) {
                if (keys[i].equals(key)) {
                    return values[i];
                }

            }
        }
        return "";
    }

    /**
     * 根据key值键值对应的下拉框初始化
     *
     * @param valueId XML中定义显示字符串数组value的ID
     * @param keyId   XML中定义显示字符串数组所对应的key值字符串数组的ID
     * @param key     传入默认选中的key值，可以为空
     */
    public static void initTextViewByKey(Activity activity,    TextView view, int valueId, int keyId, String key) {
        String[] values = activity.getResources().getStringArray(valueId);
        String[] keys = activity.getResources().getStringArray(keyId);
        int count = values.length < keys.length ? values.length : keys.length;
        for (int i = 0; i < count; i++) {
            if (key != null && !"".equals(key)) {
                if (keys[i].equals(key)) {
                    view.setText(values[i]);
                    return ;
                }

            }else{
                view.setText("");
            }
        }
        return ;
    }
    /**
     * 关闭输入法
     *
     * @param
     */
    protected static void closeInput(Activity activity) {

        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 调用自定义键盘
     * @param tv
     * @param type
     */
    public static void showKeyboardDialog(Context context,TextView tv,String str, KeyboardDialog.INPUT_TYPE type){

        KeyboardDialog  keyboardDialog = new KeyboardDialog(context);
        keyboardDialog.show(tv,str, type);
//        keyboardDialog.show(loginBtn, KeyboardDialog.INPUT_TYPE.CARNO);//车牌号
//        keyboardDialog.show(loginBtn, KeyboardDialog.INPUT_TYPE.IDNUMBER);//身份证
//        keyboardDialog.show(loginBtn, KeyboardDialog.INPUT_TYPE.RACKNO);//车架号
    }
    /**
     * 判断是否是银行卡号
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId
                .substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;

    }

    private static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null
                || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            // 如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 验证邮箱地址是否正确
     * @param email
     * @return
     */
    public static boolean checkEmail(String email){
        boolean flag = false;
        try{
            String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        }catch(Exception e){
            flag = false;
        }

        return flag;
    }
}
