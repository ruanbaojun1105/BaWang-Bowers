package com.rbj.bawang.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Http请求工具类，如文件上传下载工具处理等
 * 
 * @author asus1
 *
 */
public class HttpUtilbj {
	
//	public static String SERVICE_PATH =  "http://120.25.200.62/user/EPATH.do";
	public static String SERVICE_PATH =  "http://app.baoyitech.com.cn/car_app/user/EPATH.do";
	
	/**
	 * 接口类型
	 */
	public static final String EPATH = "";
	public static String getServerPath(String EPATH){
		Log.i("解析地址：", SERVICE_PATH.replace("EPATH", EPATH));
		return SERVICE_PATH.replace("EPATH", EPATH);
		
	}
	public static final String indexData="indexData";//首页滚动信息接口名
	public static final String getSmsCode="getSmsCode";//用于提交竞价时获取手机验证码
	public static final String submitBidding="submitBidding";//竞价提交
	public static final String checkSuccess="checkSuccess";//检测竞价是否成交
	public static final String quoteList="quoteList";//获取有效的报价列表
	public static final String quoteDetail="quoteDetail";//报价详情
	public static final String quoteSuccess="quoteSuccess";//报价成交
	public static final String successList="successList";//获取车牌竞价成交记录列表
	public static final String successDetail="successDetail";//成交记录详情
	public static final String submitHandle="submitHandle";//提交代办信息
	public static final String handleList="handleList";//获取有效的代办列表
	public static final String handleDetail="handleDetail";//代办记录详情
	public static final String getUserInfo="getUserInfo";//根据IMEI码获取用户信息
	public static final String biddingHistoryList="biddingHistoryList";//获取历史竞价记录列表数据
	public static final String submitComplain="submitComplain";//提交申述
	public static final String submitAdvice="submitAdvice";//意见反馈
	public static final String checkVersion="checkVersion";//版本控制
	
	/**
	 * 终端类型
	 */
	public static final String TTYPE = "app";
	
	/**
	 * 当前的版本ID
	 */
	public static final String 	TVER = "1.0";
	/**
	 * http的post类型
	 */
	public static final String POST = "POST";
	/**
	 * http的get类型
	 */
	public static final String GET = "GET";
	
	
	/**
	 * 发送http请求
	 * @param urlPath 请求路径
	 * @param requestType 请求类型
	 * @param request 请求参数，如果没有参数，则为null
	 * 
	 * @return
	 */
	
	public static String sendRequest(String urlPath, String requestType, String request,String tokenid) {
		
		URL url = null;
		HttpURLConnection conn = null;
		OutputStream os = null;
		InputStream is = null;
		String result = null;
		
		try {
			url = new URL(urlPath);
			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod(requestType);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setReadTimeout(15 * 1000);
			conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			conn.setRequestProperty("Connection", "keep-alive");
			conn.setRequestProperty("Content-Length", Integer.toString(request.trim().getBytes().length));
			if (!"".equals(tokenid)) {
				conn.setRequestProperty("tokenid", tokenid);
			}

			if (request != null && !"".equals(request)) {
				os = conn.getOutputStream();
				os.write(request.getBytes());
				os.flush();
			}
			
			if (200 == conn.getResponseCode()) {
				is = conn.getInputStream();
				byte[] temp = readStream(is);
				result = new String(temp);
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (result != null) {
			return result;
		} else {
			return null;
		}
	}
	
	public static byte[] readStream(InputStream is) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] buffer = new byte[2048];
		int len = 0;
		try {
			while((len = is.read(buffer)) != -1){
				os.write(buffer,0,len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return os.toByteArray();
	}
	
	/**
	 * 发送GET请求
	 * @param url 请求地址
	 * @return String
	 */
	public static String getHttpClient(String url) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 15*1000);
		try {
			HttpGet httpget = new HttpGet(url);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = httpclient.execute(httpget, responseHandler);
			
			return responseBody;

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return "";
	}
	

	/**
	 * 发送GET请求
	 * @param url 请求地址
	 * @param params 请求参数
	 * @return JSONObject
	 */
	public static JSONObject getHttpClient(String url,String params) throws JSONException {
		return new JSONObject(getHttpClient(url+params));
	}
	
	/**
	 * 判断是否存在网络问题
	 * @param activity
	 * @return
	 */
	public static boolean isConnectInternet(Activity activity) {
        ConnectivityManager conManager=(ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        if(networkInfo != null){  
        	return networkInfo.isAvailable();
        }
        return false;
	 }
}
