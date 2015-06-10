package com.rbj.bawang.util;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class FileListUtil {
	/**
	 * 获取该地址的全部列表
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> getFiles(String url) throws Exception {
		File files = new File(url);
		ArrayList<Map<String, String>>listData=null;
		// str_url = new String[listinfos.size()];// 设置字符串数组的长度
		File[] file = files.listFiles();
		if (files.listFiles().length != 0 && url.endsWith("/")) {
			listData = new ArrayList<Map<String, String>>();

			for (int j = 0; j < file.length; j++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", file[j].getName());
				map.put("url", file[j].getAbsolutePath() + "/");
				listData.add(map);
			}
			System.gc();// 提醒系统及时回收
			return listData;
		} else {
			return null;
		}
	}
	/**
	 * 调用系统自带程序打开文件
	 * @param context
	 * @param f
	 */
	public static void openFile(Context context,File f) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		String type = getMIMEType(f);
		intent.setDataAndType(Uri.fromFile(f), type);
		context.startActivity(intent);
	}
	public static String getMIMEType(File f) {
		String type = "";
		String fName = f.getName();
		String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();
		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			type = "audio";
		} else if (end.equals("3gp") || end.equals("mp4")) {
			type = "video";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			type = "image";
		} else if (end.equals("apk")) {
			type = "application/vnd.android.package-archive";
		} else {
			type = "*";
		}
		if (end.equals("apk")) {
		} else {
			type += "/*";
		}
		return type;
	}
	public static String getReallyFileName(String url) {
        String filename = "";
        URL myURL;
        HttpURLConnection conn = null;
        if (url == null || url.length() < 1) {
            return null;
        }

        try {
            myURL = new URL(url);
            conn = (HttpURLConnection) myURL.openConnection();
            conn.connect();
            conn.getResponseCode();
            URL absUrl = conn.getURL();// 获得真实Url
            Log.e("H3c", "x:" + absUrl);
            // 打印输出服务器Header信息
            // Map<String, List<String>> map = conn.getHeaderFields();
            // for (String str : map.keySet()) {
            // if (str != null) {
            // Log.e("H3c", str + map.get(str));
            // }
            // }
            filename = conn.getHeaderField("Content-Disposition");// 通过Content-Disposition获取文件名，这点跟服务器有关，需要灵活变通
            if (filename == null || filename.length() < 1) {
                filename = absUrl.getFile();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }

        return filename;
    }
}
