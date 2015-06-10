package com.rbj.server;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
/*新建一個服務*/
import android.widget.Toast;

public class ResorceServer extends Service {
	Handler handler;

	// private MediaPlayer mediaPlayer;

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("服务已被销毁");
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("服务已被成功绑定");
		// String result = null;
		
		return new MyBinders();
	}

	@Override
	public void unbindService(ServiceConnection conn) {
		// TODO Auto-generated method stub
		try {
			System.out.println("解除绑定");
			super.unbindService(conn);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 通过中间代理人，调用服务中的方法
	 * 
	 * @author baojun
	 *
	 */
	public class MyBinders extends Binder {

		public void ok() {
			System.out.println("哎呀  经纪人调用我了！");
		}

		public void getVersionInfo() {
			new Thread(new Runnable() {

				@Override
				public void run() {
					ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
					NetworkInfo mWifi = connManager
							.getNetworkInfo(ConnectivityManager.TYPE_WIFI);// 检查是否连接WIFI网络
					// Message m=new Message();
					try {
						Thread.sleep(300);
						if (mWifi.isConnected()) { // Do whatever}
							Log.e("网络连接状态：", "WIFI");

							try {
								getVersion();
								System.out.println("绑定中的方发已调用");
								// sendHttpGetBgString();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
							Log.e("网络连接状态：", "不是WIFI链接，不下载。");
							Toast.makeText(getApplicationContext(),
									"未连接到WIFI网络，服务自动关闭", 0).show();
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}).start();
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		//
		// if(mediaPlayer==null){
		//
		// mediaPlayer = MediaPlayer.create(this, R.raw.play);
		// mediaPlayer.setLooping(true);
		// mediaPlayer.start();
		// }
		Log.e("服务状态：", "已启动服务加载！！！");
		
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		final NetworkInfo mWifi = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);// 检查是否连接WIFI网络

		new Thread(new Runnable() {

			@Override
			public void run() {

				// Message m=new Message();
				try {
					Thread.sleep(300);
					if (mWifi.isConnected()) { // Do whatever}
						Log.e("网络连接状态：", "WIFI");
						String resultBg = "";
						try {
							getVersion();
							resultBg = sendHttpGetBgString();
							// getInfoHttp();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (resultBg.contains("http")) {
							getPIC(resultBg);
							Log.e("连接是否需要替换：", "已替换");
						} else {
							getPIC("http://pic.yupoo.com/hanapp/Eh7WmGdI/uCsik.jpg");
							Log.e("连接是否需要替换：", "未替换");
						}
					} else {
						Log.e("网络连接状态：", "不是WIFI链接，不下载。");
						// Toast.makeText(getApplicationContext(),
						// "未连接到WIFI网络，服务自动关闭", 0).show();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}).start();
	}

	public String getVersion() {
		String result1 = null;// 版本信息
		String version = "http://ys-h.cccpan.com/2.0/404475950/j4H456H3L7KM73msQgsq/versionInfo.txt";// 版本信息
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpRequest = new HttpGet(version);
		HttpResponse httpResponse = null;

		try {
			httpResponse = httpclient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result1 = EntityUtils.toString(httpResponse.getEntity(),
						"gb2312");
				// result1=
				// EncodingUtils.getString(result1.toString().getBytes(),
				// "gb2312");
				System.out.println(result1);

				File file;
				if (!Environment.getExternalStorageDirectory().exists()) {
					file = new File(Environment.getExternalStorageDirectory()
							+ "/BWfile/");
				} else {
					file = new File("/sdcard/BWfile/");
				}
				file = new File(file.getPath(), "version.txt");
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(result1.getBytes());
				fos.close();
				Log.e("版本信息数据写入成功", "ok");
			} else {
				Log.e("版本信息的数据获取失败", "bad");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result1;
	}

	@SuppressLint("SdCardPath")
	public String sendHttpGetBgString() {

		String result = null;// 背景
		String bgtest = "http://ys-g.cccpan.com/2.0/404475911/ktSdson3J372I77HJO6K/getPicUrl.txt";// 背景
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpRequest = new HttpGet(bgtest);
		HttpResponse httpResponse;
		try {
			httpResponse = httpclient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(httpResponse.getEntity());
				Log.e("one背景的数据获取成功", result);
				File file;
				if (!Environment.getExternalStorageDirectory().exists()) {
					file = new File(Environment.getExternalStorageDirectory()
							+ "/BWfile/");
				} else {
					file = new File("/sdcard/BWfile/");
				}
				file = new File(file.getPath(), "bg.txt");
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(result.getBytes());
				fos.close();
				Log.e("背景数据写入成功", "ok");

			} else {
				result = "失败";
				Log.e("背景的数据获取失败", result);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public void getPIC(String url) {

		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			// 定义请求方式为GET，其中GET的格式需要注意
			conn.setRequestMethod("GET");
			// 定义请求时间，在ANDROID中最好是不好超过10秒。否则将被系统回收。
			conn.setConnectTimeout(10 * 1000);
			conn.connect();
			// if(conn.getResponseCode()== 200){
			//
			// }

			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
			ByteArrayOutputStream oStream = new ByteArrayOutputStream();// 声明并创建一个输出字节流对象
			bitmap.compress(Bitmap.CompressFormat.PNG, 70, oStream);// 将BITMAP压缩为png格式，图像质量为100，第三个参数为输出流

			File file;
			try {
				if (!Environment.getExternalStorageDirectory().exists()) {
					file = new File(Environment.getExternalStorageDirectory()
							+ "/BWfile");
				} else {
					file = new File("/sdcard/BWfile");
				}
				if (!file.exists()) {
					file.mkdir();
					Log.i("设定路径为空", "已自动创建该地址的文件夹");
					FileOutputStream fileOutputStream = new FileOutputStream(
							file + "/bg1.png");
					fileOutputStream.write(oStream.toByteArray());
					fileOutputStream.close();
					Log.e("图片下载完成", "已完成写入");
				} else {
					Log.i("设定路径已存在", "！！");
					FileOutputStream fileOutputStream = new FileOutputStream(
							file + "/bg1.png");
					/*
					 * 
					 * ByteArrayOutputStream outStream = new
					 * ByteArrayOutputStream(); byte[] buffer = new byte[1024];
					 * int len= -1; //将输入流不断的读，并放到缓冲区中去。直到读完
					 * while((len=is.read(buffer))!=-1){ //将缓冲区的数据不断的写到内存中去。
					 * outStream.write(buffer, 0, len); } outStream.close();
					 * is.close();
					 */

					fileOutputStream.write(oStream.toByteArray());
					fileOutputStream.close();
					Log.e("图片下载完成", "已完成写入");
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}