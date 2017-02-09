package com.yudan.face;

import android.util.Log;

public class LogUtils {
	private final static int LEVEL = 5;

	private final static String DEFAULT_TAG = "yudan";

	private LogUtils() {
		throw new UnsupportedOperationException("mLog cannot be instantiated");
	}

	public static void v(String msg) {
		if(LEVEL >= 5){
			Log.v(DEFAULT_TAG, msg);
		}
	}

	public static void d(String msg) {
		if(LEVEL >= 4){
			Log.d(DEFAULT_TAG, msg);
		}
	}

	public static void i(String msg) {
		if(LEVEL >= 3){
			Log.i(DEFAULT_TAG, msg);
		}
	}

	public static void w(String msg) {
		if(LEVEL >= 2){
			Log.w(DEFAULT_TAG, msg);
		}
	}

	public static void e(String msg) {
		if(LEVEL >= 1){
			Log.e(DEFAULT_TAG, msg);
		}
	}
}
