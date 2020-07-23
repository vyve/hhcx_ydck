package com.estar.hh.survey.utils;

import android.os.Environment;
import android.util.Log;

import com.estar.hh.survey.constants.Constants;
import com.estar.utils.DateUtil;

import java.io.File;
import java.io.FileOutputStream;


/**
 * 日志输出工具
 * @author Administrator
 *
 */
public class LogTools {


	/**
	 * 将文本内容分日期的方式写到存储卡中
	 */
	public static void writeText(String content){
		try{
			//是否开启日志
			if(Constants.LOGWITCH){
				//组装异常内容
				String today = DateUtil.formatDate();//当前日期----yyyyMMdd
				String curTime = DateUtil.getSysDate();//当前时间----yyyy-MM-dd HH:mm:ss
				String info = "**********************************\n" ;
				info += curTime + "日志输出：\n" ;
				info += content + "\n";

				//写日志到存储卡
				String logName = "log-" + today + ".log";//日志文件名称
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					File sdCardDir = Environment.getExternalStorageDirectory();//获取SDCard目录

					createDir(sdCardDir+ "/hhydck/");
					createDir(sdCardDir+ "/hhydck/"+ "/log/");

					File saveFile = new File(sdCardDir+ "/hhydck/"+ "/log/", logName);
					FileOutputStream outStream = new FileOutputStream(saveFile,true);
					outStream.write(info.getBytes());
					outStream.close();
				}
			}

		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 特殊日志
	 * @param content
	 */
	public static void writeTextTs(String content){
		try{
			//是否开启日志
			if(Constants.LOGWITCHTS){
				//组装异常内容
				String today = DateUtil.getDate();//当前日期yyyy-MM-dd
				String curTime = DateUtil.getSysDate();//当前时间----yyyy-MM-dd HH:mm:ss
				String info = "**********************************\n" ;
				info += curTime + "日志输出：\n" ;
				info += content + "\n";

				//写日志到存储卡
				String logName = "crash-" + today + ".log";//日志文件名称
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					File sdCardDir = Environment.getExternalStorageDirectory();//获取SDCard目录

					createDir(sdCardDir+ "/hhydck/");
					createDir(sdCardDir+ "/hhydck/"+ "/log/");

					File saveFile = new File(sdCardDir+ "/hhydck/"+ "/log/", logName);
					FileOutputStream outStream = new FileOutputStream(saveFile,true);
					outStream.write(info.getBytes());
					outStream.close();
				}
			}

		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 拍照日志
	 * @param content
	 */
	public static void writeTextTss(String content){
		try{
			//是否开启日志
			if(Constants.LOGWITCHTSS){
				//组装异常内容
				String today = DateUtil.getDate();//当前日期yyyy-MM-dd
				String curTime = DateUtil.getSysDate();//当前时间----yyyy-MM-dd HH:mm:ss
				String info = "**********************************\n" ;
				info += curTime + "日志输出：\n" ;
				info += content + "\n";

				//写日志到存储卡
				String logName = "camera-" + today + ".log";//日志文件名称
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					File sdCardDir = Environment.getExternalStorageDirectory();//获取SDCard目录

					createDir(sdCardDir+ "/hhydck/");
					createDir(sdCardDir+ "/hhydck/"+ "/log/");

					File saveFile = new File(sdCardDir+ "/hhydck/"+ "/log/", logName);
					FileOutputStream outStream = new FileOutputStream(saveFile,true);
					outStream.write(info.getBytes());
					outStream.close();
				}
			}

		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 将异常分日期的方式写到存储卡中
	 * @param ex
	 */
	public static void writeException(Exception ex){
		try{
			//组装异常内容
			String today = DateUtil.formatDate();//当前日期----yyyyMMdd
			String curTime = DateUtil.getSysDate();//当前时间----yyyy-MM-dd HH:mm:ss
			String exInfo = "**********************************\n" ;
			exInfo += curTime + "系统出现异常：\n" ;
			exInfo += ex.getLocalizedMessage() + "\n";
			StackTraceElement[] exs = ex.getStackTrace();
			for(int i=0;i<exs.length;i++){
				exInfo +=  exs[i].toString() + "\n";
			}

			//写日志到存储卡
			String logName = "log-" + today + ".log";//日志文件名称
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				File sdCardDir = Environment.getExternalStorageDirectory();//获取SDCard目录

				createDir(sdCardDir+ "/hhydck/");
				createDir(sdCardDir+ "/hhydck/"+ "/log/");

				File saveFile = new File(sdCardDir+ "/hhydck/"+ "/log/", logName);
				FileOutputStream outStream = new FileOutputStream(saveFile,true);
				outStream.write(exInfo.getBytes());
				outStream.close();
			}
		}catch (Exception e){
			e.printStackTrace();
		}

	}
	/**
	 * 将异常分日期的方式写到存储卡中
	 * @param ex
	 */
	public static void writeExceptionTs(Exception ex){
		try{
			//组装异常内容
			String today = DateUtil.formatDate();//当前日期----yyyyMMdd
			String curTime = DateUtil.getSysDate();//当前时间----yyyy-MM-dd HH:mm:ss
			String exInfo = "**********************************\n" ;
			exInfo += curTime + "系统出现异常：\n" ;
			exInfo += ex.getLocalizedMessage() + "\n";
			StackTraceElement[] exs = ex.getStackTrace();
			for(int i=0;i<exs.length;i++){
				exInfo +=  exs[i].toString() + "\n";
			}

			//写日志到存储卡
			String logName = "crash-" + today + ".log";//日志文件名称
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				File sdCardDir = Environment.getExternalStorageDirectory();//获取SDCard目录

				createDir(sdCardDir+ "/hhydck/");
				createDir(sdCardDir+ "/hhydck/"+ "/log/");

				File saveFile = new File(sdCardDir+ "/hhydck/"+ "/log/", logName);
				FileOutputStream outStream = new FileOutputStream(saveFile,true);
				outStream.write(exInfo.getBytes());
				outStream.close();
			}
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	/**
	 * 将异常分日期的方式写到存储卡中
	 * @param ex
	 */
	public static void writeException(Throwable ex){
		try{
			//组装异常内容
			String today = DateUtil.formatDate();//当前日期----yyyyMMdd
			String curTime = DateUtil.getSysDate();//当前时间----yyyy-MM-dd HH:mm:ss
			String exInfo = "**********************************\n" ;
			exInfo += curTime + "系统出现异常：\n" ;
			exInfo += ex.getLocalizedMessage() + "\n";
			StackTraceElement[] exs = ex.getStackTrace();
			for(int i=0;i<exs.length;i++){
				exInfo +=  exs[i].toString() + "\n";
			}

			//写日志到存储卡
			String logName = "log-" + today + ".log";//日志文件名称
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				File sdCardDir = Environment.getExternalStorageDirectory();//获取SDCard目录

				createDir(sdCardDir+ "/hhydck/");
				createDir(sdCardDir+ "/hhydck/"+ "/log/");

				File saveFile = new File(sdCardDir+ "/hhydck/"+ "/log/", logName);
				FileOutputStream outStream = new FileOutputStream(saveFile,true);
				outStream.write(exInfo.getBytes());
				outStream.close();
			}
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	/**
	 * 将异常输出到LogCat中
	 * @param
	 */
	public static void outException(String title, Exception ex){
		try{
			//组装异常内容
			String exInfo = "**********************************\n" ;
			exInfo += ex.getLocalizedMessage() + "\n";
			StackTraceElement[] exs = ex.getStackTrace();
			for(int i=0;i<exs.length;i++){
				exInfo +=  exs[i].toString() + "\n";
			}
			LogUtils.d(title,exInfo);
		}catch (Exception e){

		}

	}

	/**
	 * 将异常输出到LogCat中
	 * @param
	 */
	public static void outException(String title, Throwable ex){
		try{
			//组装异常内容
			String exInfo = "**********************************\n" ;
			exInfo += ex.getLocalizedMessage() + "\n";
			StackTraceElement[] exs = ex.getStackTrace();
			for(int i=0;i<exs.length;i++){
				exInfo +=  exs[i].toString() + "\n";
			}
			LogUtils.d(title,exInfo);
		}catch (Exception e){

		}

	}

	/**
	 * 将输出到LogCat中，重新组装Log.d，避免参数为null值的情况
	 * @param
	 */
	public static void d(String title, String info){
		if (title == null)
			title = "null";

		if (info == null)
			info = "null";

		LogUtils.d(title,info);

	}
	/**
	 *
	 * @author chenyb Data>>方法描述</b>：创建目录
	 *         <p>
	 *         <b>方法流程</b>：
	 *         <p>
	 * @param
	 * @param filepath
	 */
	public static void createDir(String filepath) {
		File file = new File(filepath);
		if (!file.exists())
			file.mkdir();
	}
}
