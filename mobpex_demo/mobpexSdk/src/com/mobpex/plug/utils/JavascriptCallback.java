package com.mobpex.plug.utils;

public interface JavascriptCallback {
	/**
	 * @param status
	 *            0：成功 1：失败
	 * @param msg
	 */
	public void resultCallBack(String status, String msg);
}
