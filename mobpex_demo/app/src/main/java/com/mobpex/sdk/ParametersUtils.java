package com.mobpex.sdk;

import java.util.HashMap;


public class ParametersUtils {
	
	/**
	 * @param channel	渠道名
	 * @param tradeNo	订单编号
	 * @param amount	订单金额
	 * @param appId     用户ID
	 */
	public static HashMap<String,String> getMapParams(String channel,String tradeNo,String amount,String appId){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("payChannel",channel);	//必传参数
		map.put("tradeNo",tradeNo);		//必传参数
		map.put("appId",appId);			//必传参数
		map.put("amount",amount);
//		map.put("secretKey",secretKey);	//该参数由服务端SDK封装
//		map.put("liveMode","false");	//true：正式環境,	false:測試環境環境(则跳转webView)
		return map;
	}
}
