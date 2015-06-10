package com.bw.Downloader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rbj.bawang.util.AboutDialog;

public class DownLoaderTask extends AsyncTask<String, Integer, String> {

	Context context;
	Handler handler;
	ProgressBar downProgressBar;
	TextView downloadProgressPercentage;
	
	public DownLoaderTask(Context context,Handler handler,ProgressBar downProgressBar,TextView downloadProgressPercentage) {
		this.context=context;
		this.handler=handler;
		this.downProgressBar=downProgressBar;
		this.downloadProgressPercentage=downloadProgressPercentage;
	}

	@SuppressLint("SdCardPath")
	@Override
	protected String doInBackground(String... params) {
		String url = params[0];
		// default fileName is the after the last '\'
		String fileName = url.substring(url.lastIndexOf("/"));
		// String fileName = null;
		try {
			// convert the fileName into UTF-8 format
			fileName = URLDecoder.decode(fileName, "UTF-8");
			Log.e("file", fileName);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// save-path ,will self-define later
		File filete;
		if (!Environment.getExternalStorageDirectory().exists()) {
			filete = new File(Environment.getExternalStorageDirectory()
					+ "/BWfile/Download/");

		} else {
			filete = new File("/sdcard/BWfile/Download/");
		}
		SharedPreferences sp = context.getSharedPreferences("down_path",
				context.MODE_WORLD_READABLE);
		if (sp.getBoolean("isChangePath", false)) {
			String[] str;
			str = filete.getPath().split("/");
			String s2 = "";
			for (int i = 0; i < str.length - 2; i++) {
				s2 += (str[i] + "/");
			}
			filete = new File(s2 + sp.getString("down_path", ""));
			System.out.println(s2 + str.length);
			filete.mkdir();
			DownloadFile(filete.getPath().toString(), url, fileName);
			return fileName;
		} else {
			filete.mkdir();
			DownloadFile(filete.getPath().toString(), url, fileName);
			return fileName;
		}

	}

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		Log.e("result", result);
		if (result == null) {
			new AboutDialog().getToast(context, "下载异常结束！",
					Gravity.CENTER, 1);
		} else {
			new AboutDialog().getToast(context, "下载完成!",
					Gravity.BOTTOM, 0);
			downProgressBar.setVisibility(View.GONE);
			downloadProgressPercentage.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(final Integer... progresses) {
		downProgressBar.setVisibility(View.VISIBLE);
		downloadProgressPercentage.setVisibility(View.VISIBLE);
		downProgressBar.setProgress(progresses[0]);
		downloadProgressPercentage.setText(progresses[0] + "%");
		// through handler to send progress
		Message msg = handler.obtainMessage();
		msg.arg1 = progresses[0];
		handler.sendMessage(msg);
		super.onProgressUpdate(progresses);
	}

	// functions
	public void DownloadFile(String path, String url, String fileName) {
		long total_length = 0;
		int downloadedSize = 0;
		try {
			URL load_url = new URL(url);
			URLConnection connection = load_url.openConnection();
			connection.connect();
			// create the stream
			InputStream inStream = connection.getInputStream();
			total_length = connection.getContentLength();
			Log.e("legnth:", total_length + "");
			// Toast.makeText(getApplicationContext(),
			// "start downloading...", Toast.LENGTH_SHORT).show();
			if (total_length <= 0) {
				throw new RuntimeException(
						"文件大小无法获取can't get the file_length!");
			}
			if (inStream == null) {
				throw new RuntimeException(
						"无法获取文件流can't get the file_stream!");
			}
			// create the output file
			FileOutputStream outputStream = new FileOutputStream(path
					+ fileName);
			byte[] buffer = new byte[2048];
			int temp_length = 0;
			while ((temp_length = inStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, temp_length);
				downloadedSize += temp_length;
				int cur_progress = (int) ((downloadedSize / (float) total_length) * 100);
				Log.e("progress",
						(int) ((downloadedSize / (float) total_length) * 100)
								+ "%");
				// send the progress
				publishProgress(cur_progress);
			}

			outputStream.flush();
			outputStream.close();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
