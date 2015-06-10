package com.rbj.bawang.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bw.activity.MainView;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

/**
 * 集连接的检测和打开
 * @author baojun
 *
 */
public class ConnUrlHandler {

	/**
	 * 打开连接
	 * @param context
	 * @param handler
	 * @param urlString
	 */
	public static Context context;
	public static void openUrl(Context context,Handler handler,String urlString) {
		ConnUrlHandler.context=context;
		if (!TextUtils.isEmpty(urlString)) {
			if (handler != null) {
				Message msg = handler.obtainMessage();
				//msg.obj = ImageUtils.BoxBlurFilter(a);
				msg.obj=urlString;
				handler.sendMessage(msg);
			}
		}else {
			Toast.makeText(context, "没有内容！", 0).show();
		}
	}
	public static  Handler handler =new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (MainView.webView!=null) {
				MainView.webView.loadUrl((String) msg.obj);
			}else {
				Intent intent = new Intent(context, MainView.class);
				context.startActivity(intent);
				// 打开连接
				ConnUrlHandler.openUrl(context, ConnUrlHandler.handler, (String) msg.obj);
			}
			
		};
	};
	
	public static String checkURL(String url) {
		// TODO Auto-generated method stub
		String standard = "^[http://][\\S]+\\.(com|org|net|mil|edu|COM|ORG|NET|MIL|EDU)$";
		//String standard = "^[http://www.|www.][\\S]+\\.(com|org|net|mil|edu|COM|ORG|NET|MIL|EDU)$";
		Pattern pattern = Pattern.compile(standard);
		Matcher match = pattern.matcher(url);
		if (match.find()) {
			url = "http://" + url;
			Log.e("isurl", "yes");
		} else {
			Log.e("isurl", "no");
			url = "http://www.baidu.com/s?wd=" + url;
		}
		return url;
	}
}
