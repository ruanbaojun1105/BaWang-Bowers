package com.rbj.bawang.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bw.activity.FileTest;

import android.R.integer;
import android.R.string;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DelectFiles {

	@SuppressWarnings("deprecation")
	public boolean delefileDialog(final Context context, final String fileUrl, final Handler handler) {
		// TODO Auto-generated method stub
		final ProgressDialog mDialog;
		mDialog = new ProgressDialog(context);
		mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置风格为圆形进度条
		mDialog.setTitle("确定删除吗？");
		mDialog.setMessage("如果选择【确定】则SD卡内对应文件将会消失，注：不能恢复\n\n如果选择【取消】将不会继续操作！");
		mDialog.setIndeterminate(true);// 设置进度条是否为不明确
		mDialog.setCancelable(true);// 设置进度条是否可以按退回键取消
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setButton("确定", new DialogInterface.OnClickListener() {

			@SuppressLint("SdCardPath")
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				try {
					if (deletefile(fileUrl)) {
						Toast.makeText(context, "OK!", 0).show();
						if (handler!= null) {
							Message message=handler.obtainMessage();
							message.what=1;
							handler.sendMessage(message);
						}
					} else {
						Toast.makeText(context, "no done!", 0).show();
						Message message=handler.obtainMessage();
						message.what=2;
						handler.sendMessage(message);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		mDialog.setButton2("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
			}
		});

		mDialog.show();
		return true;
		
	}

	public static boolean deletefile(String delpath) throws Exception {
		boolean a = false;
		try {

			File file = new File(delpath);
			// 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true
			

				if (!file.isDirectory()) {
					file.delete();
				} else if (file.isDirectory()) {
					String[] filelist = file.list();
					for (int i = 0; i < filelist.length; i++) {
						File delfile = new File(delpath + "/" + filelist[i]);
						if (!delfile.isDirectory()) {
							delfile.delete();
							Log.e("done", "该文件夹的子文件删除成功");
						} else if (delfile.isDirectory()) {
							deletefile(delpath + "/" + filelist[i]);
							Log.e("done", "该文件夹的子文件夹删除成功");
						}
					}
					System.out.println(file.getAbsolutePath() + "删除成功");
					file.delete();

				}
				
				String[] str;
				str = file.getAbsolutePath().toString().split("/");
				String s2 = "";
				for (int i = 0; i < str.length-1 ; i++) {
					s2 += (str[i] + "/");
				}
				File file2=new File(s2);
				if (file2.list().length>0) {
					a = true;
				}else {
					a=false;
				}
		} catch (FileNotFoundException e) {
			System.out.println("deletefile() Exception:" + e.getMessage());
		}
		return a;
	}

}