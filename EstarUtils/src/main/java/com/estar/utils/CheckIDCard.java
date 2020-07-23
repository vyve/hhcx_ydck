package com.estar.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 身份证校验
 */
public class CheckIDCard {
	public static final String ACCEPT = ""; // 检查通过是返回的的成功标识字符串  
    
    public static final int EIGHTEEN_IDCARD = 18;   //标识18位身份证号码  
    public static final int FIFTEEN_IDCARD = 15;    //标识15位身份证号码  
      
    public static final int MAX_MAINLAND_AREACODE = 659004; //大陆地区地域编码最大值  
    public static final int MIN_MAINLAND_AREACODE = 110000; //大陆地区地域编码最小值  
    public static final int HONGKONG_AREACODE = 810000; //香港地域编码值  
    public static final int TAIWAN_AREACODE = 710000;   //台湾地域编码值  
    public static final int MACAO_AREACODE = 820000;    //澳门地域编码值  
      
    private static final int MAN_SEX = 1;   //标识男性  
//    private static final int WOMAN_SEX = 2; //标识女性  
      
    //储存18位身份证校验码  
    private static final String[] SORTCODES = new String[]{"1","0","X","9","8","7","6","5","4","3","2"};  
    /** 
     * @param args 
     */  
  /*  public static void main(String[] args) {  
        // TODO Auto-generated method stub  
        String idCard = "440825199011150316";  
        String result = chekIdCard(idCard);  
        if( "".equals(result) )  
            System.out.println("身份证合法");  
        else   
            System.out.println(result);  
    }  */

    /**
     * 正则表达式判断车牌号是否合格
     *
     * @param carNos
     * @return
     */
	/*
	 * protected boolean checkCarNo(String carNos) { // String vehicleNoStyle =
	 * "^[\u4e00-\u9fa5]{1}[A-Z0-9]{6}$"; String re1 =
	 * "(^[\u4e00-\u9fa5]{1}|^WJ)(.{5,11}$)"; String re2 =
	 * "(^WJ([A-Z0-9\u4E00-\u9FA5])+([A-Z0-9\u4E00-\u9FA5]$))|(^.([A-Z0-9\u4E00-\u9FA5])+([A-Z0-9\u4E00-\u9FA5]$))"
	 * ;
	 *
	 * Pattern pattern1 = Pattern.compile(re1); Matcher matcher1 =
	 * pattern1.matcher(carNos);
	 *
	 * Pattern pattern2 = Pattern.compile(re2); Matcher matcher2 =
	 * pattern2.matcher(carNos); if (!matcher1.matches() || !matcher2.matches())
	 * {
	 *
	 * return false;
	 *
	 * } return true;
	 *
	 * }
	 */

    public static boolean isCheckCarNo(String carNos) {
        String vehicleNoStyle = "^[\u4e00-\u9fa5]{1}[A-Z0-9]{6,10}$";
        if(carNos.contains("学")||carNos.contains("挂")){
            vehicleNoStyle ="^[\u4e00-\u9fa5]{1}[A-Z0-9]{5,9}[\u4e00-\u9fa5]{1}$";
        }
        Pattern pattern = Pattern.compile(vehicleNoStyle);
        Matcher matcher = pattern.matcher(carNos);

        // Toast.makeText(CarNoActivity.this,
        // "车牌号格式不对！",Toast.LENGTH_LONG).show();
        return "*".equals(carNos) || matcher.matches();
    }
      
    /** 
     * 验证身份证主方法 
     */  
    public static String chekIdCard( String idCardInput ){  
        if( idCardInput == null || "".equals(idCardInput))  
            return "身份证号码为必填";  
        if( idCardInput.length() != 18 && idCardInput.length() !=15 )  
            return "身份证号码位数不符";  
        if( idCardInput.length() == 15)  
            return checkIdCard15(idCardInput);  
        else  
            return checkIdCard18(idCardInput);  
    }  
      
    /** 
     * 验证15位身份证号码 
     */  
    public static String checkIdCard15( String idCardInput ){  
        String numberResult = checkNumber(FIFTEEN_IDCARD,idCardInput);  
        if( !ACCEPT.equals(numberResult))  
            return numberResult;  
          
        String areaResult = checkArea(idCardInput); //地域编码
        if( !ACCEPT.equals(areaResult))  
            return areaResult;  
          
        String birthResult = checkBirthDate( FIFTEEN_IDCARD, idCardInput);  //出生日期
        if( !ACCEPT.equals(birthResult))  
            return birthResult;  
       /*   
        String sortCodeResult = checkSortCode(FIFTEEN_IDCARD,idCardInput);  
        if( !ACCEPT.equals(sortCodeResult))  
            return sortCodeResult;  */
          
        String checkCodeResult = checkCheckCode(FIFTEEN_IDCARD,idCardInput);  //
        if( !ACCEPT.equals(checkCodeResult))  
            return checkCodeResult;  
          
        return ACCEPT;  
    }  
      
    /** 
     * 验证18位身份证号码 
     */  
    public static String checkIdCard18(String idCardInput ){  
          
        String numberResult = checkNumber(EIGHTEEN_IDCARD,idCardInput);  
        if( !ACCEPT.equals(numberResult))  
            return numberResult;  
          
        String areaResult = checkArea(idCardInput);  
        if( !ACCEPT.equals(areaResult))  
            return areaResult;  
          
        String birthResult = checkBirthDate( EIGHTEEN_IDCARD, idCardInput);  
        if( !ACCEPT.equals(birthResult))  
            return birthResult;  
  
       /* String sortCodeResult = checkSortCode(EIGHTEEN_IDCARD,sex,idCardInput);  
        if( !ACCEPT.equals(sortCodeResult))  
            return sortCodeResult;  */
          
        String checkCodeResult = checkCheckCode(EIGHTEEN_IDCARD,idCardInput);  
        if( !ACCEPT.equals(checkCodeResult))  
            return checkCodeResult;  
          
        return ACCEPT;  
    }  
      
    /** 
     * 验证身份证的地域编码是符合规则 
     */  
    public static String checkArea( String idCardInput ){  
        String subStr = idCardInput.substring(0, 6);  
        int areaCode = Integer.parseInt(subStr);  
        if( areaCode != HONGKONG_AREACODE && areaCode != TAIWAN_AREACODE  
                && areaCode != MACAO_AREACODE   
                && ( areaCode > MAX_MAINLAND_AREACODE || areaCode < MIN_MAINLAND_AREACODE) )  
            return "输入的身份证号码地域编码不符合大陆和港澳台规则";  
        return ACCEPT;  
    }  
      
    /** 
     * 验证身份证号码数字字母组成是否符合规则 
     */  
    private static String checkNumber( int idCardType ,String idCard ){  
        char[] chars = idCard.toCharArray();  
        if( idCardType == FIFTEEN_IDCARD ){  
            for( int i = 0; i<chars.length;i++){  
                if( chars[i] > '9' )  
                    return idCardType+"位身份证号码中不能出现字母";  
            }  
        } else {  
            for( int i = 0; i < chars.length; i++ ) {  
                if( i < chars.length-1 ){  
                    if( chars[i] > '9' )  
                        return EIGHTEEN_IDCARD+"位身份证号码中前"+(EIGHTEEN_IDCARD-1)+"不能出现字母";  
                } else {  
                    if( chars[i] > '9' && chars[i] != 'X')  
                        return idCardType+"位身份证号码中最后一位只能是数字0~9或字母X";  
                }  
            }  
              
        }  
              
        return ACCEPT;  
    }  
      
    /** 
     * 验证身份证号码出生日期是否符合规则 
     */  
    private static String checkBirthDate(int idCardType, String idCardInput ){  
        String yearResult = checkBirthYear(idCardType,idCardInput);  
        if( !ACCEPT.equals(yearResult))  
            return yearResult;  
          
        String monthResult = checkBirthMonth(idCardType,idCardInput);  
        if( !ACCEPT.equals(monthResult))  
            return monthResult;  
          
        String dayResult = checkBirthDay(idCardType,idCardInput);  
        if( !ACCEPT.equals(dayResult))  
            return dayResult;  
  
        return ACCEPT;  
    }  
      
    /** 
     * 验证身份证号码出生日期年份是否符合规则 
     */  
    private static String checkBirthYear(int idCardType, String idCardInput){  
        if( idCardType == FIFTEEN_IDCARD){  
            int year = Integer.parseInt(idCardInput.substring(6, 8));  
            if( year < 0 || year > 99 )  
                return idCardType+"位的身份证号码年份须在00~99内";  
        } else {  
            int year = Integer.parseInt(idCardInput.substring(6, 10));  
            int yearNow = getYear();  
            if( year < 1900 || year > yearNow )  
                return idCardType+"位的身份证号码年份须在1900~"+yearNow+"内";  
        }  
        return ACCEPT;  
    }  
  
    /** 
     * 验证身份证号码出生日期月份是否符合规则 
     */  
    public static String checkBirthMonth(int idCardType, String idCardInput){  
        int month = 0;  
        if( idCardType == FIFTEEN_IDCARD)  
            month = Integer.parseInt(idCardInput.substring(8, 10));   
        else   
            month = Integer.parseInt(idCardInput.substring(10, 12));  
          
        if( month < 1 || month > 12)  
            return "身份证号码月份须在01~12内";  
              
        return ACCEPT;  
    }  
      
    /** 
     * 验证身份证号码出生日期天数是否符合规则 
     */  
    public static String checkBirthDay(int idCardType, String idCardInput){  
        boolean bissextile = false;   
        int year,month,day;  
        if( idCardType == FIFTEEN_IDCARD){  
            year = Integer.parseInt("19"+idCardInput.substring(6, 8));  
            month = Integer.parseInt(idCardInput.substring(8, 10));   
            day = Integer.parseInt(idCardInput.substring(10, 12));  
        } else {  
            year = Integer.parseInt(idCardInput.substring(6, 10));  
            month = Integer.parseInt(idCardInput.substring(10, 12));  
            day = Integer.parseInt(idCardInput.substring(12, 14));  
        }  
        if( year%4 == 0 && year%100 != 0 || year%400 ==0 )    
            bissextile = true;  
          
        switch( month ){  
        case 1:  
        case 3:  
        case 5:  
        case 7:  
        case 8:  
        case 10:  
        case 12:  
            if( day < 1 || day > 31 )  
                return "身份证号码大月日期须在1~31之间";  
                break;  
        case 4:  
        case 6:  
        case 9:  
        case 11:  
            if( day < 1 || day > 30 )  
                return "身份证号码小月日期须在1~30之间";  
                break;  
        case 2:  
            if(bissextile){  
                if( day < 1 || day > 29 )  
                    return "身份证号码闰年2月日期须在1~29之间";  
            }else {  
                if( day < 1 || day > 28 )  
                    return "身份证号码非闰年2月日期年份须在1~28之间";  
            }  
            break;  
        }  
        return ACCEPT;  
    }  
      
    /** 
     * 验证身份证号码顺序码是否符合规则,男性为偶数,女性为奇数 
     */  
    public static String checkSortCode(int idCardType ,int sex,String idCardInput){  
        int sortCode = 0;  
        if( idCardType == FIFTEEN_IDCARD ){  
            sortCode = Integer.parseInt(idCardInput.substring(12, 15));  
        } else {  
            sortCode = Integer.parseInt(idCardInput.substring(14, 17));  
        }  
          
        if( sex == MAN_SEX ){  
            if( sortCode%2 == 0)  
                return "男性的身份证顺序码须为奇数";  
        } else {  
            if( sortCode%2 != 0)  
                return "女性的身份证顺序码须为偶数";       
        }  
          
        return ACCEPT;  
    }  
      
    /** 
     * 验证18位身份证号码校验码是否符合规则 
     */  
    public static String checkCheckCode( int idCardType , String idCard ){  
        if( idCardType == EIGHTEEN_IDCARD ){  
            int sum = 0;  
            char[] chars = idCard.toCharArray();  
            for( int i=0; i<chars.length; i++ ){
                if( i==0 ) sum = sum+(Integer.valueOf(String.valueOf(chars[i]))*7);
                if( i==1 ) sum = sum+(Integer.valueOf(String.valueOf(chars[i]))*9);
                if( i==2 ) sum = sum+(Integer.valueOf(String.valueOf(chars[i]))*10);
                if( i==3 ) sum = sum+(Integer.valueOf(String.valueOf(chars[i]))*5);  
                if( i==4 ) sum = sum+(Integer.valueOf(String.valueOf(chars[i]))*8);  
                if( i==5 ) sum = sum+(Integer.valueOf(String.valueOf(chars[i]))*4);  
                if( i==6 ) sum = sum+(Integer.valueOf(String.valueOf(chars[i]))*2);  
                if( i==7 ) sum = sum+(Integer.valueOf(String.valueOf(chars[i]))*1);  
                if( i==8 ) sum = sum+(Integer.valueOf(String.valueOf(chars[i]))*6);  
                if( i==9 ) sum = sum+(Integer.valueOf(String.valueOf(chars[i]))*3);  
                if( i==10 ) sum = sum+(Integer.valueOf(String.valueOf(chars[i]))*7);  
                if( i==11 ) sum = sum+(Integer.valueOf(String.valueOf(chars[i]))*9);  
                if( i==12 ) sum = sum+(Integer.valueOf(String.valueOf(chars[i]))*10);  
                if( i==13 ) sum = sum+(Integer.valueOf(String.valueOf(chars[i]))*5);  
                if( i==14 ) sum = sum+(Integer.valueOf(String.valueOf(chars[i]))*8);  
                if( i==15 ) sum = sum+(Integer.valueOf(String.valueOf(chars[i]))*4);  
                if( i==16 ) sum = sum+(Integer.valueOf(String.valueOf(chars[i]))*2);  
               // System.out.println("--sum--"+sum);
            }  
            int checkCode = sum%11;  
            String sortCode = SORTCODES[checkCode];  
              
            if(!sortCode.equals(String.valueOf(chars[chars.length-1])))  
                return "身份证的校验码不正确";  
        }  
        return ACCEPT;  
    }  
      
    /** 
     * 返回当前年份 
     */  
    public static int getYear(){  
        Date now = new Date();  
        SimpleDateFormat format = new SimpleDateFormat("yyyymmdd");   
        String nowStr = format.format(now);  
        return Integer.parseInt(nowStr.substring(0, 4));  
    }  
}
