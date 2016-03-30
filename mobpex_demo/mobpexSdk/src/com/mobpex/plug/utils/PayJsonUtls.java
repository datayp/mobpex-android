package com.mobpex.plug.utils;

import com.tencent.mm.sdk.modelpay.PayReq;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author: xin.wu
 * @create time: 2016/1/13 10:05
 * @TODO: 解析服务端验签数据
 */
public class PayJsonUtls {

	public static Response getPay(String charge){
		Response response = toJson(charge);
		String payChannel = response.payChannel;
		if(!Utils.isNull(payChannel)){
			if(payChannel.equalsIgnoreCase(PayChannel.alipay)){
				response = toJsonAlipay(response);
			}else if(payChannel.equalsIgnoreCase(PayChannel.wx)){
				response = toJsonWeixin(response);
			}else if(payChannel.equalsIgnoreCase(PayChannel.upacp)){
				response = toJsonUpacp(response);
			}else if(payChannel.equalsIgnoreCase(PayChannel.mobpex)){
				response = toJsonMobpex(response);
			}
			if(!Utils.isNull(response)){
				response.payChannel = payChannel;
			}
		}

		return response;

	}

	public static Response toJson(String jsonData) {
		Response res = new Response();
		if (!Utils.isEmpty(jsonData)) {
			try {
				JSONObject jsonObj = new JSONObject(jsonData);

				if (jsonObj.has("result")) {
					String result = jsonObj.getString("result");
					JSONObject resultJson = new JSONObject(result);
					if (resultJson.has("paymentParams")) {
						res.paymentParams = resultJson.getString("paymentParams");
					}
					if (resultJson.has("liveMode")) {
						res.liveMode = resultJson.getBoolean("liveMode");
					}
				}

				if (jsonObj.has("ext")) {
					String ext = jsonObj.getString("ext");
					JSONObject extJson = new JSONObject(ext);
					if (extJson.has("payChannel")) {
						res.payChannel = extJson.getString("payChannel");
					}
					if (!res.liveMode && extJson.has("testPayUrl")) {
						res.testPayUrl = extJson.getString("testPayUrl");
					}

				}
				if (!res.isSuceed || Utils.isNull(res.charge)) {
					res.msg = toErrorMsg(jsonObj);
				}
			} catch (Exception e) {
				// TODO: handle exception
				res.isSuceed = false;
				res.msg = "The server is being processed, please try again later!";
			}
		}
		return res;
	}

	/**
	 * @TODO Alipay
	 * @return Response
	 */
	public static Response toJsonAlipay(Response res) {
		if(!Utils.isNull(res.paymentParams)){
			try {
				JSONObject paramObj = new JSONObject(res.paymentParams);
				if (paramObj.has("orderInfo")) {
					res.charge = paramObj.getString("orderInfo");
					res.isSuceed = true;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return res;
	}
	/**
	 * @TODO WeiXin
	 * @return Response res
	 */
	public static Response toJsonWeixin(Response res){
		if(!Utils.isNull(res.paymentParams)){
			res.charge = res.paymentParams;
			res.isSuceed = true;
		}
		return res;
	}

	/**
	 * @TODO UPACP
	 * @return Response res
	 */
	public static Response toJsonUpacp(Response res){
		if(!Utils.isNull(res.paymentParams)){
			try {
				JSONObject paramObj = new JSONObject(res.paymentParams);
				if (paramObj.has("tn")) {
					res.charge = paramObj.getString("tn");
					res.isSuceed = true;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return res;
	}

	/**
	 * @TODO MobPex
	 * @return Response res
	 */
	public static Response toJsonMobpex(Response res){
		if(!Utils.isNull(res.paymentParams)){
			try {
				JSONObject paramObj = new JSONObject(res.paymentParams);
				if (paramObj.has("transUrl")) {
					res.charge = paramObj.getString("transUrl");
					res.isSuceed = true;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return res;
	}
	
	public static String toErrorMsg(JSONObject jsonObj) throws JSONException {
		String str = null;
		if (jsonObj.has("error")) {
			JSONObject resObj = new JSONObject(jsonObj.getString("error"));
			if (resObj.has("message")) {
				str = resObj.getString("message");
			}
		}
		return str;
	}
	
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
