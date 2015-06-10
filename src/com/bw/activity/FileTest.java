package com.bw.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.Destroyable;

import example.webbowers.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.lidroid.xutils.view.annotation.event.OnItemLongClick;
import com.rbj.bawang.util.AboutDialog;
import com.rbj.bawang.util.DelectFiles;
import com.rbj.bawang.util.SyncSet;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;
@ContentView(R.layout.filetest)
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint({ "WorldReadableFiles", "NewApi" })
public class FileTest extends Activity implements OnClickListener ,OnItemClickListener,OnItemLongClickListener{

	private ArrayList<Map<String, String>> listData;
	private SimpleAdapter adapter;
	@ViewInject(R.id.listView)
	private ListView listView;
	
	boolean flag = false;
	boolean isAudioList = true;
	// boolean isDirectory=false;
	@SuppressLint("SdCardPath")
	private String rootPath = "/";
	@ViewInject(R.id.backtohome)
	private ImageButton tohomeButton;
	/*@ViewInject(R.id.popuplist)
	private ImageButton popButton;*/
	@ViewInject(R.id.file_bg)
	private RelativeLayout fileBg;
	@ViewInject(R.id.backlistfile)
	private TextView backList;
	
	public static FileTest fileTest = null;
	Context context;

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.gc();
		System.out.println("filetest页面结束，已提醒系统回收垃圾");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 设置界面没有titlebar
		ViewUtils.inject(this);
		fileTest = this;
		context = FileTest.this;
		init();

		try {
			AudioList(listView, rootPath);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@SuppressLint("CutPasteId")
	public void init() {
		//popButton.setVisibility(View.GONE);
		SyncSet.setBgForUI(getApplicationContext(),SyncSet.handler, fileBg);
	}

	@OnClick({R.id.backtohome,R.id.backlistfile})
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.backtohome:
			onBackPressed();
			overridePendingTransition(R.anim.feature_scale_in, R.anim.translate);
			break;
		case R.id.backlistfile:
			if (backList.getText().length() != 0) {
				try {
					getFiles(backList.getText().toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				adapter.notifyDataSetInvalidated();

			} else {
				new AboutDialog().getToast(getApplicationContext(), "空白",
						Gravity.CENTER, 0);
			}
			String a = backList.getText().toString();
			String[] str;
			str = a.split("/");
			String s2 = "";
			for (int i = 0; i < str.length - 1; i++) {
				s2 += (str[i] + "/");
			}
			LogUtils.e(s2 + str.length);
			backList.setText(s2);
			break;
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (backList.getText().toString().equals("")) {
				onBackPressed();
				overridePendingTransition(R.anim.feature_scale_in,
						R.anim.translate);
			} else {
				try {
					getFiles(backList.getText().toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String a = backList.getText().toString();
				String[] str;
				str = a.split("/");
				String s2 = "";
				for (int i = 0; i < str.length - 1; i++) {
					s2 += (str[i] + "/");
				}
				System.out.println(s2 + str.length);
				backList.setText(s2);
			}
			// Intent intent = new Intent(FileTest.this, Home.class);
			// startActivity(intent);
		}

		return false;
	}

	private Handler handler=new Handler(){
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			if (msg.what==1) {
				String[] str;
				str = backList.getText().toString().split("/");
				String s2 = "";
				for (int i = 0; i < str.length ; i++) {
					s2 += (str[i] + "/");
				}
				try {
					getFiles(s2);
					adapter.notifyDataSetChanged();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				String[] str;
				str = backList.getText().toString().split("/");
				String s3 = "";
				for (int i = 0; i < str.length-1 ; i++) {
					s3 += (str[i] + "/");
				}
				try {
					getFiles(s3);
					adapter.notifyDataSetChanged();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		};
	};
	public void AudioList(final ListView listView, String filePathEX)
			throws Exception {
		if (getFiles(filePathEX)!=null) {
			listData = (ArrayList<Map<String, String>>) getFiles(filePathEX);
		}else {
			String[] str = null;
			str = backList.getText().toString().split("/");
			String s2 = "";
			for (int i = 0; i < str.length-1 ; i++) {
				s2 += (str[i] + "/");
			}
			try {
				getFiles(s2);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public static String[] ImageFormatSet = new String[] { "apk" };

	/**
	 * @param判断文件格式函数、可以自定义丰富的功能
	 */
	public static boolean isAudioFile(String path) {
		for (String format : ImageFormatSet) {
			if (path.contains(format)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取该地址的全部列表
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> getFiles(String url) throws Exception {
		File files = new File(url);
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
			adapter = new SimpleAdapter(context, listData,
					R.layout.history_display_style, new String[] { "name",
							"url" }, new int[] { R.id.website_names,
							R.id.website_url });
			listView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			System.gc();// 提醒系统及时回收

		} else {
			new AboutDialog().getToast(context,"空文件夹，如果不需要可长按删除！", Gravity.CENTER, 0);
			//return listData=null;
		}
		return listData;
		
	}
	@OnItemLongClick(R.id.listView)
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		new DelectFiles().delefileDialog(context, listData.get(position).get("url"),handler);
		return false;
	}

	@OnItemClick(R.id.listView)
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		String a = listData.get(position).get("url");
		String b=backList.getText().toString();
		Log.e("点击位置：", position + a + "");
		File file = new File(a);
		if (!file.isDirectory()) {
			Log.e("report", "点击的为文件");
			new DelectFiles().delefileDialog(context, a,handler);

		} else {
			
			try {
				getFiles(listData.get(position).get("url"));
				backList.setText(a);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				new AboutDialog().getToast(context,
						"no permission read this file!",
						Gravity.CENTER, 0);
				
			}
		}
	}


}