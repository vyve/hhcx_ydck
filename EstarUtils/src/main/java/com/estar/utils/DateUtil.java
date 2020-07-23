package com.estar.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static String getTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return format.format(date);
	}
	public static String getTime_YMDM(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return format.format(date);
	}
	public static String getTime_YMDMS(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}

	public static Date getDate_YMDM(String strdate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date=new Date();
		try {
			date=format.parse(strdate);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
		return date;
	}
	public static Date getDate_YMDMS(String strdate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date=new Date();
		try {
			date=format.parse(strdate);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
		return date;
	}
	public static Date getDate(String strdate) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟
		Date date=new Date();
		try {
			date=sdf.parse(strdate);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
		return date;
	}

	//2010-08-15
	public static String getTimeNow(){
		 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 String str = dateFormat.format(new Date());
		return str;
	}


	/**
	 * 
	 * @author MaBo  Data>>Nov 22, 2010
	 * <b>方法描述</b>：获得时间 <p> 
	 * <b>方法流程</b>： 返回格式<p> 
	 * @param  
	 * @return
	 */
   public static String getLongData(){
	 DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	 String str = dateFormat.format(new Date());
		return str;
	}
   
   /**
    * 
    * @author MaBo  Data>>Jan 4, 2011
    * <b>方法描述</b>：将标准时区，转换为北京标准时区 <p> 
    * <b>方法流程</b>： <p> 
    * @param  
    * @return
    */
   public static  String getYMDHM(){
	   DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	   String str = dateFormat.format(new Date());
	   return str;
   }
   /**
    *
    * @author MaBo  Data>>Jan 4, 2011
    * <b>方法描述</b>：将标准时区，转换为北京标准时区 <p>
    * <b>方法流程</b>： <p>
    * @param
    * @return
    */
   public static  String getYMDHMS(){
	   DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   String str = dateFormat.format(new Date());
	   return str;
   }

	/**
	 *
	 * @author MaBo  Data>>Jan 4, 2011
	 * <b>方法描述</b>：将标准时区，转换为北京标准时区 <p>
	 * <b>方法流程</b>： <p>
	 * @param
	 * @return
	 */
	public static String getYMD(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String str = format.format(new Date());
		return str;
	}

	/**
	 * 根据时间返回Calendar
	 *
	 * @param dateStr
	 * @return
	 */
	public static Calendar calendarConver(String dateStr) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		try {
			Date date = dateFormat.parse(dateStr);
			calendar.setTime(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return calendar;
	}
   
   /**
    * 
    * @author 丁丁  Data>>2011-11-14
    * <b>方法描述</b>：统计字符串长度，汉字为2个字符，其他为一个字符 <p> 
    * <b>方法流程</b>： <p> 
    * @param  
    * @return
   */
   public int length(String value) {
		int valueLength = 0;
		
		try {
			String english = "[a-zA-Z0-9]";
			for (int i = 0; i < value.length(); i++) {
				String temp = value.substring(i, i + 1);
				if (temp.matches(english)) {
					valueLength += 1;
				} else {
					valueLength += 2;
				}
			}
			return valueLength;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return valueLength;
	}
	/**
	 *
	 * @param day 天数
	 * @return
	 */
	public static String add(int day){
		Calendar cal =Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.DATE,cal.get(Calendar.DATE)-day);
		return 	new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
	}


	/**
	 *
	 * @param day 天数
	 * @return
	 */
	public static String add(int day,Date date){
		Calendar cal =Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE,cal.get(Calendar.DATE)-day);
		return 	new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
	}



	public static Date StringToDate(String s) {
		Date d = new Date();

		Date date;

		if (s == null)
			return null;

		d = new Date(s);

		date = new Date(d.getTime());

		return date;
	}
	public static Date StringToDate(Object o) {
		Date d = new Date();

		Date date;

		if (o == null)
			return null;

		//		d = new java.util.Date("2004-01-01");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			d = sdf.parse(o.toString());
		} catch (ParseException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}

		date = new Date(d.getTime());

		return date;
	}

	public static int getSysYear() {
		Date d = new Date();

		Date date = new Date(d.getTime());

		return date.getYear();
	}

	public static int getSysMonth() {
		Date d = new Date();

		Date date = new Date(d.getTime());

		return date.getMonth();
	}

	public static int getSysDay() {
		Date d = new Date();

		Date date = new Date(d.getTime());

		return date.getDay();
	}

	public static String getSysDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}
	public static String getSmallSysDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(new Date());
	}

	public static String getTimeLong(String endTime){
		try{
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			Date now;

			now = df.parse(getSysDate());

			Date date=df.parse(endTime);

			long l=now.getTime()-date.getTime();
			long day=l/(24*60*60*1000);
			long hour=(l/(60*60*1000)-day*24);
			long min=((l/(60*1000))-day*24*60-hour*60);

			String str="";
			double result=  ((((day*24)+hour)*60)+min);
			if(result<60){
				str=result+"分钟";
			}else{
				DecimalFormat df2  = new DecimalFormat("###.0");


				str=df2.format(result/60.0)+"小时";
			}
			return  str;
		}catch(ParseException e){
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 取得当前时间的日期字符串
	 * @returnt
	 */
	public static String getDateNow()
	{
		Calendar cal = Calendar.getInstance();
		String retStr = "";
		retStr = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) +1) + "-" + cal.get(Calendar.DAY_OF_MONTH);
		return retStr;
	}

	public static String getDateSerial() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSSSSS");
		return sdf.format(new Date());
	}

	public static String getDateShortSerial() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSSS");
		return sdf.format(new Date());
	}
	public static String getDateLongSerial() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		return sdf.format(new Date());
	}
	public static String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());
	}

	public static String getDateTOString(String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());
	}

	public static String getDateYear(String ds) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d = new Date();
		try {
			d = sdf.parse(ds);
			calendar.setTime(d);
		} catch (ParseException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return String.valueOf(calendar.get(Calendar.YEAR));
	}

	public static String getDateMonth(String ds) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d = new Date();
		try {
			d = sdf.parse(ds);
			calendar.setTime(d);
		} catch (ParseException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return String.valueOf(calendar.get(Calendar.MONTH)+1);
	}

	public static String getDateDay(String ds) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d = new Date();
		try {
			d = sdf.parse(ds);
			calendar.setTime(d);
		} catch (ParseException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
	}

	public static String getDateHour(String ds) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
		Date d = new Date();
		try {
			d = sdf.parse(ds);
			calendar.setTime(d);
		} catch (ParseException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
	}

	public static String formatDateTime(String ds) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String df = null;
		try {
			d = sdf2.parse(ds);
			df = sdf.format(d);
		} catch (ParseException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return df;
	}

	public static String formatDate(String ds) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date d = new Date();
		String df = null;

		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		try {
			d = sdf2.parse(ds);
			df = sdf.format(d);
		} catch (ParseException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return df;
	}

	public static String formatDate() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date d = new Date();
		String df = null;

		df = sdf.format(d);

		return df;
	}

	public static String formatDateYM() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		Date d = new Date();
		String df = null;

		df = sdf.format(d);

		return df;
	}

	public static String formatShortDateTime(String ds) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String df = null;
		try {
			d = sdf2.parse(ds);
			df = sdf.format(d);
		} catch (ParseException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		return df;
	}

	/**
	 * 添加(或减小)时间
	 * @param date
	 * @param field 要添加(或减小)的字段(年或月或日或...)
	 * @param amount 要添加(或减小)的数量，amount为正数时，是添加，为负数时是减小
	 * @return 日期
	 */
	public static Date add(Date date,int field,int amount){
		Date uDate = convert2JavaDate((java.sql.Date) date);
		Calendar ca = Calendar.getInstance();
		ca.setTime(uDate);
		ca.add(field, amount);
		Date uNewDate = ca.getTime();
		Date newDate = convert2SqlDate(uNewDate);
		return newDate;
	}

	/**
	 * 添加(或减小)时间
	 * @param date
	 * @param field 要添加(或减小)的字段(年或月或日或...)
	 * @param amount 要添加(或减小)的数量，amount为正数时，是添加，为负数时是减小
	 * @param pattern 格式化模式
	 * @return 格式化后的日期字符串
	 */
	public static String add(Date date,int field,int amount,String pattern){

		Date uDate = convert2JavaDate((java.sql.Date) date);
		Calendar ca = Calendar.getInstance();
		ca.setTime(uDate);
		ca.add(field, amount);

		return format(ca.getTime(),pattern);
	}

	/**
	 * 添加(或减小)时间
	 * @param date
	 * @param field 要添加(或减小)的字段(年或月或日或...)
	 * @param amount 要添加(或减小)的数量，amount为正数时，是添加，为负数时是减小
	 * @param pattern 格式化模式
	 * @return 格式化后的日期字符串
	 * @throws ParseException
	 */
	public static String add(String date,int field,int amount,String pattern) throws ParseException{
		Calendar ca = Calendar.getInstance();
		ca.setTime(parse(date, pattern));
		ca.add(field, amount);

		return format(ca.getTime(),pattern);
	}

	/**
	 * 根据字符串生成日期
	 * @param dateStr
	 * @param pattern
	 * @return Date
	 * @throws ParseException
	 */

	public static Date parse(String dateStr,String pattern) throws ParseException {
		SimpleDateFormat format =  new SimpleDateFormat(pattern);
		return format.parse(dateStr);
	}
	/**
	 * 格式化日期
	 * @param date
	 * @param pattern
	 * @return String
	 */
	public static String format(Date date,String pattern){
		SimpleDateFormat format =  new SimpleDateFormat(pattern);
		return format.format(date);
	}

	/**
	 * 将java.util.Date转换为java.sql.Date
	 * @param javaDate
	 * @return
	 */
	public static Date convert2SqlDate(Date javaDate){
		java.sql.Date sd;

		sd = new java.sql.Date(javaDate.getTime());
		return sd;

	}

	/**
	 * 将java.sql.Date转换为java.util.Date
	 * @param sqlDate
	 * @return
	 */
	public static Date convert2JavaDate(java.sql.Date sqlDate){
		return new java.sql.Date(sqlDate.getTime());
	}



	/*
	 * 得到当前日期的后一天
	 *
	 * @param specifiedDay
	 *
	 * @return
	 */
	public static String getSpecifiedDayBefore(String specifiedDay) {
		if("".equals(specifiedDay)){//当传入时间为空时默认为当前时间
			specifiedDay=getDate();
		}
		// SimpleDateFormat simpleDateFormat = new
		// SimpleDateFormat("yyyy-MM-dd");
		if("".equals(specifiedDay)){//当传入时间为空时默认为当前时间
			specifiedDay=getDate();
		}
		Calendar c = Calendar.getInstance();
		java.util.Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day +1);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c
				.getTime());
		return dayBefore;
	}
	/*
	 * 得到后一年当前日期的前一天
	 *
	 * @param specifiedDay
	 *
	 * @return
	 */
	public static String getSpecifiedDayBefore2(String specifiedDay) {
		// SimpleDateFormat simpleDateFormat = new
		// SimpleDateFormat("yyyy-MM-dd");
		if("".equals(specifiedDay)){//当传入时间为空时默认为当前时间
			specifiedDay=getDate();
		}
		Calendar c = Calendar.getInstance();
		java.util.Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		c.set(Calendar.YEAR, year+1);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day-1);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c
				.getTime());
		return dayBefore;
	}

	/**
	 * 得到date时间的下一个小时
	 * @param date
	 * @return
	 */
	public static String nextHours(java.util.Date date){

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR)+1);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());

	}


	/**
	 * @author 朱雪亮
	 * 拼接日期跟时间
	 * @param yMd  时间字符串
	 * @param hms  日期字符串
	 * @return  拼接后的时间
	 */
	public static java.util.Date concatDate(String yMd,String hms){

		if (yMd == null) {
			yMd = format(new java.util.Date(),"yyyy-MM-dd");

		}
		if (hms==null) {
			hms= format(new java.util.Date(), "HH:mm:ss");
		}
		java.util.Date d = null;
		try {
			d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(yMd.concat(" "+hms));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return d;
	}
	/**
	 * 得到后一年当前日期的前一天,包含小时
	 * @param specifiedDay
	 * @return
	 */
	public static String getSpecifiedDayOrHourBefore(String specifiedDay) {

		Calendar c = Calendar.getInstance();
		java.util.Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		c.set(Calendar.YEAR, year+1);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day);
//		c.set(Calendar.HOUR, c.get(Calendar.HOUR)-1);
//		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, c.get(Calendar.SECOND)-1);
		String dayBefore = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(c
				.getTime());
		return dayBefore;
	}
}
