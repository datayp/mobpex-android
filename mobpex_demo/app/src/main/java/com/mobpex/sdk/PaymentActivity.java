package com.mobpex.sdk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mobpex.plug.MobpexPaymentActivity;
import com.mobpex.plug.utils.MobpexLog;
import com.mobpex.plug.utils.PayChannel;

import java.util.HashMap;
import java.util.Random;



/**
 * @author xin.wu
 *
 * Mobpex SDK 示例程序，仅供开发者参考。
 * 【注意】运行该示例，需要用户填写一个URL。
 * Mobpex SDK 使用流程如下：
 * 1）客户端请求服务端获得charge。服务端生成charge的方式参考Mobpex 官方文档;
 * 2）收到服务端的charge，调用Mobpex SDK;
 * 3）onActivityResult 中获得支付结果;
 * 4）如果支付成功。服务端会收到Mobpex 异步通知，支付成功依据服务端异步通知为准。
 */
public class PaymentActivity extends Activity implements View.OnClickListener {

	private static final int REQUEST_CODE_PAYMENT = 1;
	private EditText amountEditText;
	private String currentAmount = "";
	public String channel = "";
	public ProgressDialog progressDialog;

	/**
	 *开发者需要填一个服务端URL 该URL是用来请求支付需要的charge。务必确保，URL能返回json格式的charge对象。
	 *服务端生成charge 的方式可以参考Mobpex官方文档，
	 *【https://www.mobpex.com/yop-center/demo 】是 Mobpex 为了方便开发者体验 sdk 而提供的一个临时 url 。
	 * 该 url 仅能调用【模拟支付控件】，开发者需要改为自己服务端的 url 。
	 */
	/**
	 * test
	 */

	public static String URL = "https://220.181.25.235/mashup-demo/demo/submitOrder";
//	String appId = "16031706093671048936";
//	String appId = "1603290925342397104893671";//1603290925342397104893671 15122404366710489367

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		MobpexLog.DEBUG = true;
		amountEditText = (EditText) findViewById(R.id.amountEditText);
		findViewById(R.id.upmpButton).setOnClickListener(this);
		findViewById(R.id.alipayButton).setOnClickListener(this);
		findViewById(R.id.wechatButton).setOnClickListener(this);
		findViewById(R.id.yeepayButton).setOnClickListener(this);
	}
	private void dismiss() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	String amount;

	@Override
	public void onClick(View view) {
		amount = amountEditText.getText().toString();
		if (amount.equals("")) {
			Toast.makeText(getApplication(), "请输入金额", Toast.LENGTH_LONG).show();
			return;
		}
		if (view.getId() == R.id.upmpButton) {
			onPay(PayChannel.upacp);
		} else if (view.getId() == R.id.alipayButton) {
			onPay(PayChannel.alipay);
		} else if (view.getId() == R.id.wechatButton) {
			onPay(PayChannel.wx);
		} else if (view.getId() == R.id.yeepayButton) {
			onPay(PayChannel.mobpex);
		}
	}

	public static String getRandomString() {
		String base = "123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 12; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	String charge = "";
	@SuppressLint("NewApi")
	public class HttpTask extends AsyncTask<String, String, String> {
		private HashMap<String, String> paramMap;

		public HttpTask(HashMap<String, String> map) {
			this.paramMap = map;
		}

		@Override
		protected String doInBackground(String[] params) {
			try {
				charge = WebUtils.doPost(URL, paramMap);

			} catch (Exception e) {
			}
			return charge;
		}

		@Override
		protected void onPostExecute(String charge) {
			MobpexLog.debug(PaymentActivity.class,"response:"+charge);
			dismiss();
			if (!charge.isEmpty()) {
				onExecute(charge);
			}
		}
	}

	public  class  Res{
		String string;
	}

	public void onPay(String channel) {
		this.channel = channel;
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.show();
		String tradeNo = getRandomString(); // 订单号
		HashMap<String, String> params = ParametersUtils.getMapParams(channel,tradeNo, amount);
		new HttpTask(params).execute();
	}


	private void onExecute(String charge) {
		Intent intent = new Intent();
		String orderId = getRandomString();
		String packageName = getPackageName();
		if (charge != null) {
			ComponentName componentName = new ComponentName(packageName,packageName + ".wxapi.WXPayEntryActivity");
			intent.setComponent(componentName);
			intent.putExtra(MobpexPaymentActivity.EXTRA_CHARGE, charge);
			startActivityForResult(intent, REQUEST_CODE_PAYMENT);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 支付页面返回处理
		if (requestCode == REQUEST_CODE_PAYMENT) {
			if (resultCode == Activity.RESULT_OK) {
				/*
				 * 处理返回值 "success" - 成功 ,"fail" - 支付失败
				 */
				String result = data.getExtras().getString("mobpex_result");
				/**
				 * 错误消息
				 */
				String msg = data.getExtras().getString("mobpex_msg");
				/**
				 * 当前渠道
				 */
				String channel = data.getExtras().getString("mobpex_channel");
				showMsg(channel, result, msg);
			}
		}
	}

	public void showMsg(String channel, String result, String msg) {
		StringBuffer sb = new StringBuffer();
		sb.append("channel:" + channel + "\n");
		sb.append("result:" + result + "\n");
		if (null != msg && msg.length() != 0) {
			sb.append("\n" + msg);
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(sb.toString());
		builder.setTitle("提示");
		builder.setPositiveButton("OK", null);
		builder.create().show();
	}
}
