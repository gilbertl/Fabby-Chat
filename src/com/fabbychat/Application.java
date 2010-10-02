package com.fabbychat;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class Application extends android.app.Application {

	private FbChatService.LocalBinder mService;

	@Override
	public void onCreate() {
		super.onCreate();
		bindService(new Intent(this, FbChatService.class), 
			new ServiceConnection() {
				@Override
				public void onServiceDisconnected(ComponentName name) {
					// TODO Auto-generated method stub
				}
				
				@Override
				public void onServiceConnected(ComponentName name,
					IBinder service) {
					Application.this.mService = 
						(FbChatService.LocalBinder) service;
				}
			}, BIND_AUTO_CREATE);
	}
	
	public FbChatService.LocalBinder getService() {
		return mService;
	}
}
