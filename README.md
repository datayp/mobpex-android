Android Mobpex-SDK接入指南
========================

###一、快速体验
------
mobpex SDK 适用于支付宝、银联、易宝、微信四个渠道。
mobpex SDK 为开发者提供了 demo 程序，可以快速体验 client-sdk 接入流程。下载 clent-sdk 之后将 demo 目录导入到您的 AndroidStudio 之中。

###二、快速集成 
     添加依赖包
     1. alipaySDK-xxxxxxxx.jar
     2. libammsdk.jar
     3. mobpexsdk.jar
     4. UPPayAssistEx.jar
     5. UPPayPluginExStd.jar
    
     以上所有文件位置在下载目录的 libs ，请把上面所有文件添加到 Android 工程的 libs目录下面。
     （如果是 AndroidStudio IDE ，請在当前build.gradle文件中加入一下配置）。 
     sourceSets {
        main{
            jniLibs.srcDirs = ['libs']
        }
    }
    AndroidManifest.xml 中注册 activity
    <code>
	 <!-- 支付宝 sdk -->
        <activity
            android:name="com.alipay.sdk.app.H5PayActivity"
            android:configChanges="orientation|keyboardHidden|navigation"
            android:exported="false"
            android:screenOrientation="behind" >
        </activity>
        <activity
            android:name="com.alipay.sdk.auth.AuthActivity"
            android:configChanges="orientation|keyboardHidden|navigation"
            android:exported="false"
            android:screenOrientation="behind" >
        </activity>
       <!-- mobpex SDK 注册 -->
        <activity
            android:name="com.mobpex.plug.MobpexPaymentActivity"
            android:configChanges="orientation|screenSize"
            android:launchMode="singleTop"
            android:theme="@android:style/Theme.Translucent.NoTitleBar" />

        <!-- 微信支付 sdk ，也是 mobpex sdk 调用入口 -->
        <activity-alias
            android:name=".wxapi.WXPayEntryActivity"
            android:exported="true"
            android:targetActivity="com.mobpex.plug.MobpexPaymentActivity" />

	<!-- 银联sdk  -->
        <activity
            android:name="com.unionpay.uppay.PayActivity"
            android:configChanges="orientation|keyboardHidden|keyboard"
            android:screenOrientation="portrait" > 
        </activity>

        <uses-library
            android:name="org.simalliance.openmobileapi"
            android:required="false" /> 
	               
###二、权限声明
    1、微信支付渠道是通过向“微信“客户端发起请求进行支付的，要求手机必须安装微信。如果没有安装微信，mobpex sdk 会在支付结果中给予通知。不需要额外权限。
    2、银联支付渠道是通过“银联手机支付服务“进行支付的，要求手机必须安装“银联手机支付服务”。如果没有安装，mobpex sdk 会在支付结果中给予提示。不需求额外权限。
    3、支付宝、微信等渠道，需要的权限为
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" /> 
    <!-- 银联  -->
    <uses-permission android:name="org.simalliance.openmobileapi.SMARTCARD" />
    <uses-permission android:name="android.permission.NFC" />
    <uses-feature android:name="android.hardware.nfc.hce" />

###三、获得 Charge 对象(支付凭证)
   在 Server 使用 Mobpex Server SDK 获取 Charge 对象后，Server 会将该对象传给 Client 。客户端需要接收该 Charge 对象，并将此传给 Client SDK 以调起支付控件。关于 Server 如何获取 Charge 对象，详见 Server 接入指南。

###四、发起支付
------
<pre><code>
    String charge;
    Intent intent = new Intent();
    String packageName = getPackageName();
    ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
    intent.setComponent(componentName);
    intent.putExtra(MobpexPaymentActivity.EXTRA_CHARGE, charge);
    startActivityForResult(intent, REQUEST_CODE_PAYMENT);
</pre></code>
    说明: 上述发起方式是 mobpex client-sdk 唯一公开调用方式， “.wxapi.WXPayEntryActivity“ 是所有渠道支付的入口，并非只是微信支付入口。
    详见demo工程。
    
###五、获取支付状态
  从 Activity 的 onActivityResult 方法中获得支付结果(最终支付成功请根据服务端异步通知为准！)。
<pre><code>
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
 </pre></code>
     
###六、混淆设置
用户进行 apk 混淆打包的时候，为了不影响 mobpex SDK 以及渠道 SDK 的使用，具体请参考demo工程中 proguard-cfg文件。 
        
日志开关

    sdk 提供了日志功能，默认日志为关闭状态。
    开发者可以通过下面设置打开日志开关。通过 "mobpex" 来对日志进行筛选。
    
    MobpexLog.DEBUG = true;
     
###【注意事項】

1、mobpex SDK 适用于支付宝、银联、易宝、微信四个渠道;<br>
2、wx 渠道是通过向微信客户端发起请求进行支付的 
要求：<br>
 (1)、手机必须安装微信。<br>
 (2)、应用包名和签名必须与填写在微信平台上的一致，微信平台上的签名需是 MD5 且不带冒号的格式。<br>
 (3)、调试的时候必须打包出来测试，否则无法调用微信支付控件。<br>
    
 
            

    
