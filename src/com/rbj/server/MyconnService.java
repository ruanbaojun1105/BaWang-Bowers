package com.rbj.server;

import com.rbj.server.ResorceServer.MyBinders;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * 链接服务的中间人，间接调用服务中的方法
 * 
 * @author baojun
 *
 */
public class MyconnService implements ServiceConnection {

	private ResorceServer.MyBinders myBinders;

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		// TODO Auto-generated method stub
		System.out.println("从服务返回代理人已调用完成的方法");
		myBinders = (MyBinders) service;
		myBinders.ok();
		myBinders.getVersionInfo();
	}

	@Override
	public void onServiceDisconnected(ComponentName arg0) {
		// TODO Auto-generated method stub
		System.out.println("出现异常时才会调用");
	}

}
