package com.mobpex.plug;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.mobpex.plug.pays.PayAlipay;
import com.mobpex.plug.pays.PayMobpex;
import com.mobpex.plug.pays.UPPay;
import com.mobpex.plug.pays.WXPay;
import com.mobpex.plug.pays.WebViewMobpex;
import com.mobpex.plug.utils.MobpexLog;
import com.mobpex.plug.utils.PayChannel;
import com.mobpex.plug.utils.PayConfig;
import com.mobpex.plug.utils.PayJsonUtls;
import com.mobpex.plug.utils.PayResultCallback;
import com.mobpex.plug.utils.Response;
import com.mobpex.plug.utils.Utils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 
 * @author simon.xin
 *
 */
public class MobpexPaymentActivity extends Activity implements
		PayResultCallback, IWXAPIEventHandler {

	private ProgressDialog progressDialog;
	private IWXAPI api;
	public static final String EXTRA_CHARGE = "com.mobpex.sdk.PaymentActivity.CHARGE";
	private UPPay uupa;
	public int STATUS = 0;
	private Response response;

	@Override
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		init();
	}


	private void init() {
		STATUS = 0;
		Intent localIntent = getIntent();
		String charge =  localIntent.getStringExtra(EXTRA_CHARGE);
		if (!Utils.isNull(charge)) {
			onChanagePay(charge);
		} else {
			payResult(PayConfig.fail, "PaymentRequest_null_fail");
		}
	}

	private boolean checkCharge(Response response){
		if(Utils.isNull(response.charge) || !response.isSuceed){
			return false;
		}
		return true;
	}
	/**
	 * @param charge
	 */
	private void onChanagePay(String charge) {
		if (Utils.isNull(charge)) {
			onPayFail();
			return;
		}
		try {
			response = PayJsonUtls.getPay(charge);
			if(Utils.isNull(response)){
				onPayFail();
				return;
			}
			if(!response.liveMode && !Utils.isNull(response.testPayUrl)){
				new WebViewMobpex(this, this).load(response.testPayUrl);
				return;
			}
			if(!checkCharge(response)){
				onPayFail();
				return;
			}

			if (response.payChannel.equalsIgnoreCase(PayChannel.alipay)) {
				PayAlipay pay = new PayAlipay(this, this, response.charge);
				pay.pay();
			} else if (response.payChannel.equalsIgnoreCase(PayChannel.wx)) {
				PayReq payReq = WXPay.toRequesWeixin(response.charge);
				if (!Utils.isNull(payReq)) {
					initWX(payReq);
					if (api.isWXAppInstalled()) {
						api.sendReq(payReq);
					} else {
						payResult(PayConfig.fail, "Not installed WX...");
					}
				} else {
					payResult(PayConfig.fail,"PayReq parameter is empty...");
				}
			} else if (response.payChannel.equalsIgnoreCase(PayChannel.upacp)) {
				uupa = new UPPay();
				uupa.startPay(MobpexPaymentActivity.this, response.charge);
			} else if (response.payChannel.equalsIgnoreCase(PayChannel.mobpex)) {
				new PayMobpex(this, this).load(response.charge);
			}
		} catch (Exception e) {
			onPayFail();
		}
	}

	@Override
	public void onPayResult(String channel, String result, String msg) {
		payResult(channel, result, msg);
	}

	private void initWX(PayReq p) {
		if (Utils.isNull(p.appId)) {
			payResult(PayConfig.fail, "wx_appId_null");
			return;
		}
		api = WXAPIFactory.createWXAPI(this, p.appId, false);
		api.registerApp(p.appId);
		api.handleIntent(this.getIntent(), this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		}
		String str = data.getExtras().getString("pay_result");
		if (!Utils.isNull(str) && str.equalsIgnoreCase("success")) {
			onPaySuccess();
		} else if (str.equalsIgnoreCase("fail")) {
			onPayFail();
		} else if (str.equalsIgnoreCase("cancel")) {
			onPayFail();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		MobpexLog.error(getClass(), "onNewIntent...");
		if (!Utils.isNull(api)) {
			setIntent(intent);
			api.handleIntent(intent, this);
		}
	}
 

	
	
	public void setStatus(int status) {
		STATUS = status;
	}

	@Override

	public void onReq(BaseReq baseReq) {
		MobpexLog.error(getClass(), "onReq...");
	}

	@Override
	protected void onResume () {
		super.onResume();
		MobpexLog.error(getClass(), "onResume...:"+STATUS);
		if(!Utils.isNull(response) && response.payChannel.equalsIgnoreCase(PayChannel.wx)){
			if (STATUS == 0) {
		    	STATUS = 1;
		    } else {
                onPayFail();
			}
		}
	}
	
	@Override
	public void onResp(BaseResp resp) {
		MobpexLog.error(getClass(), "onResp...:"+resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			setStatus(resp.errCode);
			if (resp.errCode == BaseResp.ErrCode.ERR_OK) { 
				onPaySuccess();
			} else {
				onPayFail();
			}
		}else{
			onPayFail();
		}
	}

	public void onPaySuccess() {
		payResult(PayConfig.success, PayConfig.msg_success);
	}

	public void onPayFail() {
		payResult(PayConfig.fail, PayConfig.msg_fail);
	}

	private void onFail(String msg) {
		payResult(PayConfig.fail, msg);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			onPayFail();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void payResult(String channel, String result, String msg) {
		Intent var4 = new Intent();
		var4.putExtra(PayConfig.pay_channel, channel);
		var4.putExtra(PayConfig.pay_result, result);
		var4.putExtra(PayConfig.pay_msg, msg);
		this.setResult(-1, var4);
		this.finish();
	}

	private void payResult(String result, String msg) {
		Intent var4 = new Intent();
		var4.putExtra(PayConfig.pay_channel, response.payChannel);
		var4.putExtra(PayConfig.pay_result, result);
		var4.putExtra(PayConfig.pay_msg, msg);
		this.setResult(-1, var4);
		this.finish();
	}
}
