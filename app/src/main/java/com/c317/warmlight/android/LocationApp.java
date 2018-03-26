package com.c317.warmlight.android;

import com.baidu.mapapi.SDKInitializer;

import android.app.Application;

public class LocationApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		SDKInitializer.initialize(getApplicationContext());
	}
	
}
