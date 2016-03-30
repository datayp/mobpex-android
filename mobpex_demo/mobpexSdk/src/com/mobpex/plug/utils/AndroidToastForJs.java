package com.mobpex.plug.utils;

import android.webkit.JavascriptInterface;

public class AndroidToastForJs implements JavascriptCallback {

	public JavascriptCallback mJavaScriptCallback;

	public AndroidToastForJs(JavascriptCallback loadJavascriptCallback) {
		// TODO Auto-generated constructor stub
		this.mJavaScriptCallback = loadJavascriptCallback;
	}

	@JavascriptInterface
	public void resultCallBack(String status, String msg) {
		MobpexLog.debug(getClass(), "resultCallBack..." + status + msg);
		mJavaScriptCallback.resultCallBack(status, msg);
	}

	@JavascriptInterface
	public void payResult(String status) {
		MobpexLog.debug(getClass(), "payResult..." + status);
		mJavaScriptCallback.resultCallBack(status, "");
	}
}
	
