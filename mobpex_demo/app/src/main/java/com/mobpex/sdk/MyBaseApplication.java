package com.mobpex.sdk;

import android.app.Application;

import java.io.IOException;
import java.io.InputStream;

public class MyBaseApplication extends Application {

	private static MyBaseApplication mInstance;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mInstance = this;

	}
	
	public synchronized static MyBaseApplication getInstance() {
		if (Utils.isNull(mInstance)) {
			mInstance = new MyBaseApplication();
		}
		return mInstance;
	}

	public InputStream getAssets(String fileName){
		try {
			return getResources().getAssets().open(fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
