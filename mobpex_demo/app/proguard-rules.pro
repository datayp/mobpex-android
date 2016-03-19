-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
  
#支付宝
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}


-libraryjars libs/mobpexSdk.jar
-libraryjars libs/UPPayAssistEx.jar
-libraryjars libs/UPPayPluginExPro.jar
-libraryjars libs/alipaySdk-20151215.jar 
-libraryjars libs/android-support-v4.jar 
-dontwarn android.support.v4.** 
-keep class android.support.v4.** { *; } 
-keep public class * extends android.support.v4.** 
-keep public class * extends android.app.Fragment 


-dontwarn com.alipay.**
-keep class com.alipay.** {*;}

-dontwarn  com.tencent.**
-keep class com.tencent.** {*;}

-dontwarn  com.unionpay.**
-keep class com.unionpay.** {*;}
    
-dontwarn com.mobpex.plug.**
-keep class com.mobpex.plug.** {*;}

-keepclassmembers class * {
   @android.webkit.JavascriptInterface <methods>;
}

#银联
-keep class com.unionpay.tsmservice.** {*;}
-keep class com.unionpay.** {*;}

-keep  public class com.unionpay.uppay.net.HttpConnection {
	public <methods>;
}
-keep  public class com.unionpay.uppay.net.HttpParameters {
	public <methods>;
}
-keep  public class com.unionpay.uppay.model.BankCardInfo {
	public <methods>;
}
-keep  public class com.unionpay.uppay.model.PAAInfo {
	public <methods>;
}
-keep  public class com.unionpay.uppay.model.ResponseInfo {
	public <methods>;
}
-keep  public class com.unionpay.uppay.model.PurchaseInfo {
	public <methods>;
}
-keep  public class com.unionpay.uppay.util.DeviceInfo {
	public <methods>;
}
-keep  public class java.util.HashMap {
	public <methods>;
}
-keep  public class java.lang.String {
	public <methods>;
}
-keep  public class java.util.List {
	public <methods>;
}


-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep  public class com.unionpay.uppay.util.PayEngine {
	public <methods>;
	native <methods>;
}
-keep  public class com.unionpay.utils.UPUtils {
	native <methods>;
}
-keepclassmembers class **.R$* {
    public static <fields>;
}
