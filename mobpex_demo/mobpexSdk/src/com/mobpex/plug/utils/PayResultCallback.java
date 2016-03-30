package com.mobpex.plug.utils;

/**
 * @author: xin.wu
 * @create time: 2016/1/11 09:43
 * @TODO: 支付结果
 */
public interface PayResultCallback {
    /**
     * @param channel
     * @param result
     * @param msg
     */
    public void onPayResult (String channel,String result, String msg);
}
