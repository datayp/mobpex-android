package com.mobpex.plug.pays;

import com.mobpex.plug.utils.AndroidToastForJs;
import com.mobpex.plug.utils.JavascriptCallback;
import com.mobpex.plug.utils.MobpexLog;
import com.mobpex.plug.utils.PayChannel;
import com.mobpex.plug.utils.PayConfig;
import com.mobpex.plug.utils.PayResultCallback;
import com.mobpex.plug.utils.Utils; 

import android.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.StateListDrawable;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * 
 * @author xin.wu
 *
 */
public class PayMobpex implements JavascriptCallback {

	public Activity mContext;
	public WebView webview = null;
	private ProgressBar mProgressBar;
	private int ACTION_RESUT = 51;
	public boolean isBack;

	public PayResultCallback callback;

	public PayMobpex(Activity context, PayResultCallback payResultCallback) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.callback = payResultCallback;
		initView();
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.what == ACTION_RESUT) {
				if (Utils.isNull(msg.obj)) {
					callback.onPayResult(PayChannel.mobpex, PayConfig.fail,
							PayConfig.msg_fail);
					return;
				}
				toResult((String[]) msg.obj);
			}
		};
	};

	public void toResult(String[] re) {
		if (!Utils.isNull(re) && re[0].equals("1")) { // 1: suceed 0:error
			callback.onPayResult(PayChannel.mobpex, PayConfig.success,
					(!Utils.isNull(re[1]) ? re[1] : PayConfig.msg_success));
		} else {
			callback.onPayResult(PayChannel.mobpex, PayConfig.fail,
					(!Utils.isNull(re[1]) ? re[1] : PayConfig.msg_fail));
		}
	}

	public Resources getResources() {
		return mContext.getResources();
	}

	private View.OnClickListener backClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			
			if (webview.canGoBack()) {
				if (!isBack) {
					webview.goBack();
				} else {
					callback.onPayResult(PayChannel.mobpex, PayConfig.fail,PayConfig.msg_fail);
				}
			}else {
				callback.onPayResult(PayChannel.mobpex, PayConfig.fail,PayConfig.msg_fail);
			}
		}
	};

	private void initView() {
		RelativeLayout rlk = new RelativeLayout(mContext);
		RelativeLayout rl = new RelativeLayout(mContext);
		rl.setBackgroundColor(Color.parseColor("#40B1F9"));
		RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		lp1.addRule(RelativeLayout.ALIGN_PARENT_TOP);

		RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(150,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		lp2.addRule(RelativeLayout.ALIGN_LEFT);
		TextView btn1 = new TextView(mContext);
		btn1.setLayoutParams(lp2);
		btn1.setText("<");
		btn1.setPadding(25, 10, 10, 10);
		btn1.setTextColor(Color.parseColor("#FFFFFF"));
		btn1.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
		btn1.setTextSize(24);
		btn1.setId(122123);

		rlk.setLayoutParams(lp2);
		rlk.addView(btn1);
		rl.addView(rlk);
		rlk.setOnClickListener(backClickListener);

		TextView tv_title = new TextView(mContext);
		tv_title.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
		tv_title.setTextSize(18);
		tv_title.setText("魔派支付");
		tv_title.setTextColor(Color.parseColor("#FFFFFF"));
		RelativeLayout.LayoutParams rlt = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		rlt.addRule(RelativeLayout.CENTER_IN_PARENT);
		tv_title.setLayoutParams(rlt);
		rl.addView(tv_title);

		LinearLayout rootlayout = new LinearLayout(mContext);
		rootlayout.setOrientation(LinearLayout.VERTICAL);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		webview = new WebView(mContext);

		mProgressBar = new ProgressBar(mContext, null,
				android.R.attr.progressBarStyleHorizontal);
		mProgressBar.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, 4));
		mProgressBar.setMax(100);
		mProgressBar.setProgress(0);
		webview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		rootlayout.setLayoutParams(params);
		rootlayout.addView(rl);
		rootlayout.addView(mProgressBar);
		rootlayout.addView(webview);
		mContext.setContentView(rootlayout);
		webview.requestFocus();
		webview.setWebViewClient(new MyWebViewClient());
		webview.setWebChromeClient(new PayWebViewClient());
		webview.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
					if (isBack) {
						return false;
					} else {
						webview.goBack();
						return true;
					}
				}
				return false;
			}
		});

		webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);// 把所有内容放到WebView组件等宽的一列中
		webview.getSettings().setDefaultTextEncodingName("utf-8");
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setUseWideViewPort(true);
		webview.getSettings().setSupportZoom(true);
		webview.getSettings().setBuiltInZoomControls(true);
		webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
	}

	public void load(String url) {
		webview.addJavascriptInterface(new AndroidToastForJs(this), "jsObj");
		webview.loadUrl(url);
	}



	private class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			MobpexLog.error(getClass(), "url:" + url);
			if (isBack) {
				return false;
			}
			if (!Utils.isNull(url) && url.contains("systemerror")) {
				isBack = true;
				return false;
			}
			load(url);
			return true;

		}

		@Override
		public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) {
			handler.proceed();  // 接受所有网站的证书
		}

	}

	private class PayWebViewClient extends WebChromeClient {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress < 100) {
				mProgressBar.setVisibility(View.VISIBLE);
				mProgressBar.setProgress(newProgress);
			} else {
				mProgressBar.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void resultCallBack(String status, String msg) {
		// TODO Auto-generated method stub
		Message message = mHandler.obtainMessage();
		message.what = ACTION_RESUT;
		message.obj = new String[] { status, msg };
		mHandler.sendMessage(message);

	}
}
