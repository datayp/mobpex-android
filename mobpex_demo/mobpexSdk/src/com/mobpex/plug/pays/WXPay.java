package com.mobpex.plug.pays;

import com.tencent.mm.sdk.modelpay.PayReq;

import org.json.JSONException;
import org.json.JSONObject;

public class WXPay {
	/**
	 * @param jsonData
	 * @throws JSONException
	 */
	public static PayReq toRequesWeixin(String jsonData) throws JSONException {
		PayReq req = null;
		JSONObject json = new JSONObject(jsonData);
		req = new PayReq();
		req.appId = json.getString("appid");
		req.partnerId = json.getString("partnerid");
		req.prepayId = json.getString("prepayid");
		req.nonceStr = json.getString("noncestr");
		req.timeStamp = json.getString("timestamp");
		req.packageValue = json.getString("package");
		req.sign = json.getString("sign");
		return req;
	}
}
