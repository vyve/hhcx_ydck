package com.estar.hh.survey.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;


import com.estar.utils.DateUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * 文件类 所有文件操作
 */
public class FileUtils {

	public static FileUtils instance=null;
	private Context context;
	/** 最外层目录 **/
	public static String SYS_TEMMD = "hhydck";
	/** APK名称 **/
	public static String APK_NAME = SYS_TEMMD+".apk";
	/** APK目录 **/
	public static String VERSION = "version";
	/** 图片信息 **/
	public static String IMAGE = "image";
	/** 缓存目录 **/
	public static String CACHE = "cache";
	/** 日志文件目录 **/
	public static String LOG = "log";
	/** APK路径 **/
	public static String APK_LOCATION = "/" + SYS_TEMMD + "/"+ CACHE + "/"+VERSION;

	private FileUtils(Context context) {
		this.context=context;
	}

	public FileUtils(){

	}

	public static FileUtils getInstance(Context context){
		if (null==instance){
			synchronized (FileUtils.class) {
				//未初始化，则初始instance变量
				if (instance == null) {
					instance = new FileUtils(context);
				}
			}
		}
		return instance;
	}

	public static FileUtils getInstance(){
		return instance;
	}
	/**
	 * 根据任务号
	 * 得到图片图片目录
	 *
	 * @param
	 * @return
	 */
	public  String getImagePath() {
		return getCachePath()+"/" +"zsbx.jpg";
	}
	/**
	 * 得到Apk缓存目录
	 *
	 * @param
	 * @return
	 */
	public String getApkMD() {

		File file = new File(getCachePath());
		if (!file.exists())
			file.mkdir();
		File file2 = new File(getCachePath() + "/" + CACHE);
		if (!file2.exists())
			file2.mkdir();
		File file3 = new File(getCachePath()+ "/" + CACHE+"/"+VERSION);
		if (!file3.exists())
			file3.mkdir();
		return getCachePath() + "/"+ CACHE + "/"+VERSION;
	}
	/**
	 * 得到缓存目录
	 * @param
	 * @return
	 */
	public String getCacheMD() {
		File file = new File(getCachePath());
		if (!file.exists())
			file.mkdir();
		File file2 = new File(getCachePath()+ "/" + CACHE);
		if (!file2.exists())
			file2.mkdir();
		return getCachePath()+ "/" + CACHE + "/";
	}



	/**
	 * 根据任务号
	 * 得到图片图片目录
	 *
	 * @param
	 * @return
	 */
	public  String getImageMd(String reportNo) {

		File file = new File(getCachePath()+ "/" + CACHE+ "/" + IMAGE + "/"+ reportNo);
		if (!file.exists()){
			file.mkdirs();
		}
		return file.getPath();
	}





	/**
	 * 获取案件备注信息路径
	 * @param
	 * @return
	 */
	public  String carateSubmitCaseRemarkSearchInfoPath(String proNO) {

		File file = new File(getCachePath());
		if (!file.exists())
			file.mkdir();
		File file2 = new File(getCachePath() + "/" + CACHE);
		if (!file2.exists())
			file2.mkdir();
		File file3 = new File(getCachePath()+ "/" + CACHE+"/"+"submitCaseRemarkSearchinfo");
		if (!file3.exists())
			file3.mkdir();
		return getCachePath()+ "/" + CACHE + "/" +"submitCaseRemarkSearchinfo"+"/"+proNO + ".txt";
	}
	/**
	 * 获取查勘提交信息路径
	 * @param
	 * @return
	 */
	public  String carateSubmitSurvMainInfoPath(String proNO) {

		File file = new File(getCachePath());
		if (!file.exists())
			file.mkdir();
		File file2 = new File(getCachePath() + "/" + CACHE);
		if (!file2.exists())
			file2.mkdir();
		File file3 = new File(getCachePath()+ "/" + CACHE+"/"+"submitsurvmaininfo");
		if (!file3.exists())
			file3.mkdir();
		return getCachePath()+ "/" + CACHE + "/" +"submitsurvmaininfo"+"/"+proNO + ".txt";
	}


	/**
	 * 获取草图信息路径
	 * @param
	 * @return
	 */
	public  String carateSubmitSubSurveyVOPath(String proNO) {

		File file = new File(getCachePath());
		if (!file.exists())
			file.mkdir();
		File file2 = new File(getCachePath() + "/" + CACHE);
		if (!file2.exists())
			file2.mkdir();
		File file3 = new File(getCachePath()+ "/" + CACHE+"/"+"subsurveyvo");
		if (!file3.exists())
			file3.mkdir();
		return getCachePath()+ "/" + CACHE + "/" +"subsurveyvo"+"/"+proNO + ".txt";
	}

	/**
	 * 获取精友信息路径
	 * @param
	 * @return
	 */
	public  String carateSubmitJYInfoPath(String proNO) {

		File file = new File(getCachePath());
		if (!file.exists())
			file.mkdir();
		File file2 = new File(getCachePath() + "/" + CACHE);
		if (!file2.exists())
			file2.mkdir();
		File file3 = new File(getCachePath()+ "/" + CACHE+"/"+"JYinfo");
		if (!file3.exists())
			file3.mkdir();
		return getCachePath()+ "/" + CACHE + "/" +"JYinfo"+"/"+proNO + ".txt";
	}

	/**
	 * 获取记事本路径
	 *
	 * @param
	 * @return
	 */
	public  String carateNotesPath(String time) {
		File file = new File(getCachePath());
		if (!file.exists())
			file.mkdir();
		File file2 = new File(getCachePath()+ "/" + CACHE);
		if (!file2.exists())
			file2.mkdir();
		File file3 = new File(getCachePath()+ "/" + CACHE+"/"+"notes");
		if (!file3.exists())
			file3.mkdir();
		return getCachePath()+ "/" + CACHE + "/" +"notes"+"/"+time + ".pa";
	}






	private String carateTaskMd(String str) {
		File file = new File(getCachePath());
		if (!file.exists())
			file.mkdir();
		File file2 = new File(getCachePath()+ "/" + CACHE);
		if (!file2.exists())
			file2.mkdir();
		return getCachePath()+ "/" + CACHE + "/" + str + ".pa";
	}

	/**
	 *  根据日期 获取日志路径
	 *
	 * @param
	 * @return
	 */
	public  String carateLogPath(String log) {

		File file = new File(getCachePath());
		if (!file.exists())
			file.mkdir();
		File file2 = new File(getCachePath()+ "/" + CACHE);
		if (!file2.exists())
			file2.mkdir();
		File file3 = new File(getCachePath()+ "/" + CACHE+ "/" +LOG);
		if (!file3.exists())
			file3.mkdir();
		return getCachePath()+ "/" + CACHE+ "/" + LOG+"/"+log + ".txt";
	}


	/**
	 * Get PDF file Intent
	 */
	public Intent getPdfFileIntent(String path) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.addCategory(Intent.CATEGORY_DEFAULT);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(path));
		i.setDataAndType(uri, "application/pdf");
		return i;
	}

	// android获取一个用于打开Word文件的intent
	public Intent getWordFileIntent(String path) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(path));
		intent.setDataAndType(uri, "application/msword");
		return intent;
	}

	/**
	 * 根据路径打开文件
	 * @param filePath
	 * @return
	 */
	public Intent openFile(String filePath) {

		File file = new File(filePath);

		if ((file == null) || !file.exists() || file.isDirectory())
			return null;

		/* 取得扩展名 */
		String end = file
				.getName()
				.substring(file.getName().lastIndexOf(".") + 1
                ).toLowerCase();
		/* 依扩展名的类型决定MimeType */
		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			return getAudioFileIntent(filePath);
		} else if (end.equals("3gp") || end.equals("mp4")) {
			return getAudioFileIntent(filePath);
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			return getImageFileIntent(filePath);
		} else if (end.equals("apk")) {
			return getApkFileIntent(filePath);
		} else if (end.equals("ppt")) {
			return getPptFileIntent(filePath);
		} else if (end.equals("xls")) {
			return getExcelFileIntent(filePath);
		} else if (end.equals("doc")||end.equals("docx")) {
			return getWordFileIntent(filePath);
		} else if (end.equals("pdf")) {
			return getPdfFileIntent(filePath);
		} else if (end.equals("chm")) {
			return getChmFileIntent(filePath);
		} else if (end.equals("txt")) {
			return getTextFileIntent(filePath, false);
		} else {
			return getAllIntent(filePath);
		}
	}

	// Android获取一个用于打开APK文件的intent
	public Intent getAllIntent(String param) {

		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "*/*");
		return intent;
	}

	// Android获取一个用于打开APK文件的intent
	public Intent getApkFileIntent(String param) {

		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		return intent;
	}

	// Android获取一个用于打开VIDEO文件的intent
	public Intent getVideoFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "video/*");
		return intent;
	}

	// Android获取一个用于打开AUDIO文件的intent
	public Intent getAudioFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "audio/*");
		return intent;
	}

	// Android获取一个用于打开Html文件的intent
	public Intent getHtmlFileIntent(String param) {

		Uri uri = Uri.parse(param).buildUpon()
				.encodedAuthority("com.android.htmlfileprovider")
				.scheme("content").encodedPath(param).build();
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.setDataAndType(uri, "text/html");
		return intent;
	}

	// Android获取一个用于打开图片文件的intent
	public Intent getImageFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "image/*");
		return intent;
	}

	// Android获取一个用于打开PPT文件的intent
	public Intent getPptFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
		return intent;
	}

	// Android获取一个用于打开Excel文件的intent
	public Intent getExcelFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/vnd.ms-excel");
		return intent;
	}

	// Android获取一个用于打开CHM文件的intent
	public Intent getChmFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/x-chm");
		return intent;
	}

	// Android获取一个用于打开文本文件的intent
	public Intent getTextFileIntent(String param, boolean paramBoolean) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (paramBoolean) {
			Uri uri1 = Uri.parse(param);
			intent.setDataAndType(uri1, "text/plain");
		} else {
			Uri uri2 = Uri.fromFile(new File(param));
			intent.setDataAndType(uri2, "text/plain");
		}
		return intent;
	}



	/**
	 * 保存信息
	 * @param jsonData
	 *            (保存的数据)
	 */
	public  boolean saveFile(String jsonData, String filePath) {

		try {
			java.io.FileOutputStream out = new java.io.FileOutputStream(
					new File(filePath));
			out.write(jsonData.getBytes());
			out.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 根据路径获取文件
	 *
	 * @param filePath
	 * @return
	 */
	public  String getFile(String filePath) {

		FileInputStream is = null;
		if (filePath != null && !"".equals(filePath)) {
			try {
				is = new FileInputStream(new File(filePath));
				return readFile(is);
			} catch (IOException e) {
				return null;
			} finally {
				if (is != null)
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}

		return null;
	}

	private  String readFile(FileInputStream inputStream) {
		String readedStr = "";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
			String tmp;
			while ((tmp = br.readLine()) != null) {
				readedStr += tmp;
			}

		} catch (UnsupportedEncodingException e) {
			return null;
		} catch (IOException e) {
			return null;
		} finally {
			try {
				if (br != null)
					br.close();
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return readedStr;
	}

	/**
	 *  删除文件路径下所有文件
	 * @param
	 * @return
	 */
	public  void deletePath(String path){
		try {
			File fileTemp = new File(path);
			DeleteFile(fileTemp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	/**
	 * 递归删除文件和文件夹
	 *
	 * @param file 要删除的目录
	 */
	public  void DeleteFile(File file) {
		if (file.exists() == false) {
			return;
		} else {
			if (file.isFile()) {
				file.delete();
				return;
			}
			if (file.isDirectory()) {
				File[] childFile = file.listFiles();
				if (childFile == null || childFile.length == 0) {
					file.delete();
					return;
				}
				for (File f : childFile) {
					DeleteFile(f);
				}
				file.delete();
			}
		}
	}
	/**
	 * 判断是否存在SD卡
	 *
	 * @return
	 */
	public boolean hasSDCard() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	public String getCachePath() {
		if (hasSDCard()) {
			//sd卡文件路径 /storage/emulated/0/hhydck
			return Environment.getExternalStorageDirectory().toString() + File.separator + SYS_TEMMD;
		} else {
			// 内置路径/data/data/com.estar.qhcxydck/cache/qhcxydck
			return context.getCacheDir() + File.separator + SYS_TEMMD;
		}
	}

	/**
	 * 将定位信息写入文件
	 * @return
	 */
	public boolean writeException(Exception ex){
		boolean result = false;
		try{
			//组装异常内容
			String today = DateUtil.formatDate();//当前日期----yyyyMMdd
			String fileName = getLogFilePath() + "/" + today + "ex.txt";
			String curTime = DateUtil.getSysDate();//当前时间----yyyy-MM-dd HH:mm:ss
			String exInfo = "**********************************\n" ;
			exInfo += curTime + "系统出现异常：\n" ;
			exInfo += ex.getLocalizedMessage() + "\n";
			StackTraceElement[] exs = ex.getStackTrace();
			for(int i=0;i<exs.length;i++){
				exInfo +=  exs[i].toString() + "\n";
			}

			result = writeFile(fileName, exInfo);

		}catch (Exception e){
			saveExceptionFile("FileUtils\n异常信息:" + DateUtil.getSysDate() + "\n" + e);//打印异常信息
		}
		return result;
	}

	/*
 * 获取日志文件路径
 */
	public String getLogFilePath(){
		String filepath = "";
		try{
			String SDCARD = Environment.getExternalStorageDirectory().toString();
			filepath = SDCARD + "/estar/netspeed";
			File file = new File(filepath);
			if (!file.exists())
				file.mkdir();
		}catch (Exception ex){
			ex.printStackTrace();
			return "";
		}

		return filepath;

	}

	/**
	 * 将内容写入到SDCARD文件中
	 * @param fileName
	 * @param inputName
	 * @return
	 */
	public boolean writeFile(String fileName, String inputName){
		boolean result = false;
		try{
			FileOutputStream fout =new FileOutputStream(fileName,true);
			byte [] bytes = inputName.getBytes();
			fout.write(bytes);
			fout.close();
			result = true;
		}
		catch(Exception e){
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	/**
	 * 保存文件
	 *
	 * @param jsonData
	 *            (保存的数据)
	 */
	public static boolean saveExceptionFile(String jsonData) {

		String sdcard = Environment.getExternalStorageDirectory().toString();
		File file = new File(sdcard + "/" + SYS_TEMMD);
		if (!file.exists())
			file.mkdir();
		File file2 = new File(sdcard + "/" + SYS_TEMMD + "/" +CACHE);
		if (!file2.exists())
			file2.mkdir();
		File file3 = new File(sdcard + "/" + SYS_TEMMD + "/" +CACHE+"/" +"exception");
		if (!file3.exists())
			file3.mkdir();
		String filePath=sdcard + "/" + SYS_TEMMD + "/" +CACHE+"/" +"exception"+"/"+DateUtil.getLongData()+".pa";
		try {
			java.io.FileOutputStream out = new java.io.FileOutputStream(
					new File(filePath));
			out.write(jsonData.getBytes());
			out.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将网络信息写入日志文件
	 * @return
	 */
	public boolean writeLog(String inputText){
		String today = DateUtil.formatDate();//当前日期----yyyyMMdd
		String curTime = DateUtil.getSysDate();//当前时间----yyyy-MM-dd HH:mm:ss
		String fileName = getLogFilePath() + "/" + today + "netdata.txt";
		inputText = "\n##########(" + curTime + ")输出：\n" + inputText;
		inputText = inputText + "\n##########\n";

		boolean result = writeFile(fileName, inputText);

		return result;
	}

}
