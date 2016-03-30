package com.mobpex.plug.utils;

import android.util.Log;

import java.util.Calendar;

 
public class MobpexLog {
    public static boolean DEBUG = false;
    public static String LOG_TAG = "mobpex";
	public static final void info(Class<?> clazz, String info) {
		if (DEBUG) {
			Log.i(LOG_TAG, ">>>>>>>>>>>" + clazz.getSimpleName() + " "
					+ info);
		}
	}

	public static final void error(Class<?> clazz, String error, Throwable t) {
		if (DEBUG) {
			Log.e( LOG_TAG, ">>>>>>>>>>>" + clazz.getSimpleName() + " "
					+ error, t);
		}
	}

	public static final void error(Class<?> clazz, String error) {
		if (DEBUG) {
			Log.e( LOG_TAG, ">>>>>>>>>>>" + clazz.getSimpleName() + " "
					+ error);
		}
	}

	public static final void debug(Class<?> clazz, String info) {
		if (DEBUG) {
			Log.d(LOG_TAG, ">>>>>>>>>>>" + clazz.getSimpleName() + " "
					+ info);
		}
	}
}
