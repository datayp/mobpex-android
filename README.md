Android-Mobpex-Client-SDK����ָ��

 һ����������
    mobpex SDK ������֧�������������ױ���΢���ĸ�������
    mobpex SDK Ϊ�������ṩ�� demo ���򣬿��Կ������� client-sdk �������̡����� clent-sdk ֮�� demo Ŀ¼���뵽���� eclipse ���� AndroidStudio ֮�С�
 

�������ټ���

     ���������
     1. alipaySDK-xxxxxxxx.jar
     2. libammsdk.jar
     3. mobpexsdk.jar
     4. UPPayAssistEx.jar
     5. UPPayPluginExStd.jar
    
     ���������ļ�λ��������Ŀ¼�� libs ��������������ļ���ӵ� Android ���̵� libsĿ¼���档
     ������� AndroidStudio IDE ��Ո�ڵ�ǰbuild.gradle�ļ��м���һ�����ã��� 
     sourceSets {
        main{
            jniLibs.srcDirs = ['libs']
        }
    }

    
    AndroidManifest.xml ��ע�� activity
 
	 <!-- ֧���� sdk -->
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

       <!-- mobpex SDK ע�� -->
        <activity
            android:name="com.mobpex.plug.MobpexPaymentActivity"
            android:configChanges="orientation|screenSize"
            android:launchMode="singleTop"
            android:theme="@android:style/Theme.Translucent.NoTitleBar" />

        <!-- ΢��֧�� sdk ��Ҳ�� mobpex sdk ������� -->
        <activity-alias
            android:name=".wxapi.WXPayEntryActivity"
            android:exported="true"
            android:targetActivity="com.mobpex.plug.MobpexPaymentActivity" />

	<!-- ����sdk  -->
        <activity
            android:name="com.unionpay.uppay.PayActivity"
            android:configChanges="orientation|keyboardHidden|keyboard"
            android:screenOrientation="portrait" > 
        </activity>

        <uses-library
            android:name="org.simalliance.openmobileapi"
            android:required="false" /> 

	               
����Ȩ������

    1��΢��֧��������ͨ����΢�š��ͻ��˷����������֧���ģ�Ҫ���ֻ����밲װ΢�š����û�а�װ΢�ţ�mobpex sdk ����֧������и���֪ͨ������Ҫ����Ȩ�ޡ�
    2������֧��������ͨ���������ֻ�֧�����񡰽���֧���ģ�Ҫ���ֻ����밲װ�������ֻ�֧�����񡱡����û�а�װ��mobpex sdk ����֧������и�����ʾ�����������Ȩ�ޡ�
    3��֧������΢�ŵ���������Ҫ��Ȩ��Ϊ
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" /> 
    <!-- ����  -->
    <uses-permission android:name="org.simalliance.openmobileapi.SMARTCARD" />
    <uses-permission android:name="android.permission.NFC" />
    <uses-feature android:name="android.hardware.nfc.hce" />

������� Charge ����(֧��ƾ֤)
	�� Server ʹ�� Mobpex Server SDK ��ȡ Charge �����Server �Ὣ�ö��󴫸� Client ���ͻ�����Ҫ���ո� Charge ���󣬲����˴��� Client SDK �Ե���֧���ؼ������� Server ��λ�ȡ Charge ������� Server ����ָ�ϡ�

�ġ�����֧��
    String charge;
    Intent intent = new Intent();
    String packageName = getPackageName();
    ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
    intent.setComponent(componentName);
    intent.putExtra(MobpexPaymentActivity.EXTRA_CHARGE, charge);
    startActivityForResult(intent, REQUEST_CODE_PAYMENT);

    ˵��: ��������ʽ�� mobpex client-sdk Ψһ�������÷�ʽ�� ��.wxapi.WXPayEntryActivity�� ����������֧������ڣ�����ֻ��΢��֧����ڡ�
    ���demo���̡�
    
�塢��ȡ֧��״̬

  �� Activity �� onActivityResult �����л��֧�����(����֧���ɹ�����ݷ�����첽֪ͨΪ׼��)��

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// ֧��ҳ�淵�ش���
		if (requestCode == REQUEST_CODE_PAYMENT) {
			if (resultCode == Activity.RESULT_OK) {
				/*
				 * ������ֵ "success" - �ɹ� ,"fail" - ֧��ʧ��
				 */
				String result = data.getExtras().getString("mobpex_result");
				/**
				 * ������Ϣ
				 */
				String msg = data.getExtras().getString("mobpex_msg");
				/**
				 * ��ǰ����
				 */
				String channel = data.getExtras().getString("mobpex_channel");
				showMsg(channel, result, msg);
			}
		}
	}
 
     
    
������������

�û����� apk ���������ʱ��Ϊ�˲�Ӱ�� mobpex SDK �Լ����� SDK ��ʹ�ã�������ο�demo������ proguard-cfg�ļ��� 
        
��־����

    sdk �ṩ����־���ܣ�Ĭ����־Ϊ�ر�״̬��
    �����߿���ͨ���������ô���־���ء�ͨ�� "mobpex" ������־����ɸѡ��
    
    MobpexLog.DEBUG = true;
     
��ע����헡�

1��mobpex SDK ������֧�������������ױ���΢���ĸ�������
2��wx ������ͨ����΢�ſͻ��˷����������֧���ģ�Ҫ��
 (1)���ֻ����밲װ΢�š�
 (2)��Ӧ�ð�����ǩ����������д��΢��ƽ̨�ϵ�һ�£�΢��ƽ̨�ϵ�ǩ������ MD5 �Ҳ���ð�ŵĸ�ʽ��
 (3)�����Ե�ʱ��������������ԣ������޷�����΢��֧���ؼ���
    
 
            

    
