package com.estar.hh.survey.utils;

import android.util.Log;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ReadFile {

	public static byte[] getFileFromUrl(String url, NetWorkSpeedInfo netWorkSpeedInfo) {
		int currentByte = 0;
		int fileLength = 0;
		long startTime = 0;
		long intervalTime = 0;

		byte[] b = null;

		int bytecount = 0;
		URL urlx = null;
		URLConnection con = null;
		InputStream stream = null;
		try {
			LogUtils.d("URL:", url);
			urlx = new URL(url);
			con = urlx.openConnection();
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
			fileLength = con.getContentLength();
			stream = con.getInputStream();
			netWorkSpeedInfo.totalBytes = fileLength;
			b = new byte[fileLength];
			startTime = System.currentTimeMillis();
			while ((currentByte = stream.read()) != -1) {
				netWorkSpeedInfo.hadFinishedBytes++;
				intervalTime = System.currentTimeMillis() - startTime;
				if (intervalTime == 0) {
					netWorkSpeedInfo.speed = 1000;
				} else {
					netWorkSpeedInfo.speed = (netWorkSpeedInfo.hadFinishedBytes / intervalTime) * 1000;
				}
				if (bytecount < fileLength) {
					b[bytecount++] = (byte) currentByte;
				}
			}
		} catch (Exception e) {
			LogUtils.e("exception : ", e.getMessage() + "");
		} finally {
			try {
				if (stream != null) {
					stream.close();
				}
			} catch (Exception e) {
				LogUtils.e("exception : ", e.getMessage());
			}

		}
		return b;
	}

}
