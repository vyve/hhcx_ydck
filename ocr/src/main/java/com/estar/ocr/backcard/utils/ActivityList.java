package com.estar.ocr.backcard.utils;

import java.util.ArrayList;

import android.app.Activity;

public class ActivityList {
	private static ArrayList<Activity> list;

	public static void addActivity(Activity context) {
		if (list == null) {
			list = new ArrayList<Activity>();
		}
		list.add(context);
	}

	public static void closeActivity() {
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				Activity con=list.get(i);
				con.finish();
			}
		}
	}
}
