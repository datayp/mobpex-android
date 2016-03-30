package com.mobpex.plug.utils;

public class Response implements java.io.Serializable {
	public boolean isSuceed;
	public String charge;	//最终渠道签名信息（唤醒支付SDK）
	public String msg;
	public String paymentParams;
	public boolean liveMode = false;
	public String url;
	public String payChannel;
	public String testPayUrl;
}
