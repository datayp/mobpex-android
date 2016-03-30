package com.mobpex.plug.pays;

import com.mobpex.plug.utils.MobpexLog;
import com.unionpay.UPPayAssistEx;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;

public class UPPay {

	private String mMode = "00";	//01  00
	private Activity mActivity;

	public String getMode() {
		return mMode;
	}

	/**
	 * 
	 * @param activity
	 * @param orderNo
	 *            商户订单号
	 * @param isTest
	 *            true:
	 */
	public void startPay(Activity activity, String orderNo) {
		this.mActivity = activity;
		Message msg = mHandler.obtainMessage();
		msg.obj = orderNo;
		mHandler.sendMessage(msg);
	}

	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int ret = UPPayAssistEx.startPay(mActivity, null, null,
					msg.obj.toString(), mMode);
			if (ret == UPPayAssistEx.PLUGIN_NOT_FOUND) {
				UPPayAssistEx.installUPPayPlugin(mActivity);
			}
		};
	};
}
