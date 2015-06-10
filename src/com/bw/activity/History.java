package com.bw.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.lidroid.xutils.view.annotation.event.OnItemLongClick;
import com.rbj.bawang.util.AboutDialog;
import com.rbj.bawang.util.ConnUrlHandler;
import com.rbj.bawang.util.SyncSet;
import example.webbowers.R;
import com.bw.activity.Home;
import com.bw.activity.MainView;
import com.bw.bean.BW_history;
import com.bw.database.HistoryDAO;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;

@ContentView(R.layout.page_history)
public class History extends Activity implements OnItemClickListener,
OnItemLongClickListener,OnMenuItemClickListener,OnClickListener{
	// 获取所有收入信息，并存储到List泛型集合中
	private List<BW_history> listinfos;
	private HistoryDAO flagDAO = new HistoryDAO(History.this);// 创建FlagDAO对象
	// private BW_history tb_flag = new BW_history();
	@ViewInject(R.id.history_list)
	private ListView listView_history;
	private SimpleAdapter simpleAdapter;
	private String[] str_flag = null;// 定义字符串数组，用来存储收入信息
	private String[] str_url = null;// 定义字符串数组，用来存储收入信息
	private List<Map<String, String>> listData = new ArrayList<Map<String, String>>();
	@ViewInject(R.id.history_bg)
	private LinearLayout historyBg;
	public static History history = null;
	private String getIntentName;
	private Context context;
	@ViewInject(R.id.backtohome)
	private ImageButton backHome;
	@ViewInject(R.id.more)
	private ImageButton more;
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.gc();
		System.out.println("history页面结束，已提醒系统回收垃圾");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		History.history = this;
		context=History.this;
		
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		
		SyncSet.setBgForUI(getApplicationContext(),SyncSet.handler, historyBg);
		/**
		 * 从数据库得到数据
		 */

		if (flagDAO.getCount() > 60) {
			listinfos = flagDAO.getScrollData(flagDAO.getCount() - 50,
					flagDAO.getCount());

		} else {
			listinfos = flagDAO.getScrollData(0, flagDAO.getCount());
		}

		String[] strInfos = null;// 定义字符串数组，用来存储收入信息
		strInfos = new String[listinfos.size()];// 设置字符串数组的长度
		str_flag = new String[listinfos.size()];// 设置字符串数组的长度
		str_url = new String[listinfos.size()];// 设置字符串数组的长度
		int m = 0;// 定义一个开始标识
		if (flagDAO.getMaxId() == flagDAO.getCount() + 50) {
			new AboutDialog().getToast(getApplicationContext(),
					"为方便浏览，超过60条记录自动只显示50条", Gravity.CENTER, 0);
		}
		if (flagDAO.getCount() == 0) {
			new AboutDialog().getToast(getApplicationContext(), "没有浏览历史",
					Gravity.CENTER, 0);
			str_flag = null;
			str_url = null;
		} else {
			listData = new ArrayList<Map<String, String>>();
			for (BW_history tb_flag : listinfos) {// 遍历List泛型集合
				// 将收入相关信息组合成一个字符串，存储到字符串数组的相应位置
				strInfos[m] = tb_flag.getid() + "|" + tb_flag.getFlag() + "|"
						+ tb_flag.getWeburl();
				str_flag[m] = tb_flag.getFlag();
				str_url[m] = tb_flag.getWeburl();
				
				String urls = tb_flag.getFlag();
				String[] str;
				str = urls.split("--");
				String s2 = "";
				s2 = str[0];
				Log.e("s2+str.length", s2 + str.length + " ");
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", s2);
				map.put("url", tb_flag.getWeburl());
				listData.add(map);
				m++;
			}
			}
			simpleAdapter = new SimpleAdapter(context, listData,
					R.layout.history_display_style, new String[] { "name",
							"url" }, new int[] { R.id.website_names,
							R.id.website_url });
			listView_history.setAdapter(simpleAdapter);
		}


	@SuppressWarnings("deprecation")
	public String forDate() {
		String dateTest = "";
		Date date = new Date(System.currentTimeMillis());
		String time = date.toLocaleString().toString();
		for (int i = 0; i < 50; i++) {
			dateTest += "时间是:" + time;
		}
		return dateTest;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		openOptionsMenu();
		return true;
	}

	@OnItemLongClick(R.id.history_list)
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		String urls = str_flag[position].toString();
		String[] str;
		str = urls.split("--");
		String s2 = "";
		s2 = str[1];
		ClipboardManager cmb = (ClipboardManager) context
				.getSystemService(context.CLIPBOARD_SERVICE);
		cmb.setText(listData.get(position).get("url"));
		new AboutDialog().getToast(getApplicationContext(),
				"记录时间：" + s2+"\n此地址已复制到粘贴板", Gravity.CENTER, 0);
		return false;
	}

	@OnItemClick(R.id.history_list)
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		String urls = str_url[position].toString();
		Log.i("得到的链接::", urls);
		String intentsString=getIntent().getExtras().get("intent").toString();
		if (intentsString.equals("home")) {
			
			// 打开连接
			Intent intent=new Intent(this,MainView.class).putExtra("urlA", urls);
			startActivity(intent);
		}else {
			ConnUrlHandler.openUrl(context, ConnUrlHandler.handler,urls);
		}
		onBackPressed();
		overridePendingTransition(R.anim.feature_scale_in, R.anim.translate);
	}

	@OnClick({R.id.backtohome,R.id.more})
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backtohome:
			onBackPressed();
			overridePendingTransition(R.anim.feature_scale_in, R.anim.translate);
			break;
		case R.id.more:
			PopupMenu popupMenu = new PopupMenu(this, v);
			MenuInflater inflater = popupMenu.getMenuInflater();
			inflater.inflate(R.menu.historymenu, popupMenu.getMenu());
			popupMenu.show();
			popupMenu.setOnMenuItemClickListener(this);
			break;
		}
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.history_clean:
			for (int i = 1; i <= flagDAO.getMaxId(); i++) {
				Log.e("删除了：", flagDAO.find(i) + "");
				flagDAO.detele(i);
			}
			new AboutDialog().getToast(getApplicationContext(),
					"已清空浏览历史，继续看新闻吧！", Gravity.TOP, 0);
			listData.clear();
			simpleAdapter.notifyDataSetChanged();
			break;
		case R.id.history_shutdown:
			PopupMenu popupMenu = new PopupMenu(this, more);
			MenuInflater inflater = popupMenu.getMenuInflater();
			inflater.inflate(R.menu.popupmenu, popupMenu.getMenu());
			popupMenu.show();
			popupMenu.setOnMenuItemClickListener(this);
			break;
		case R.id.open:
				SharedPreferences sp = History.this.getSharedPreferences(
						"addHistory", 0);
				SharedPreferences.Editor et = sp.edit();
				et.putBoolean("addHistory", true);
				et.commit();
				new AboutDialog().getToast(getApplicationContext(),
						"已开启无痕浏览\n浏览器将记录下你浏览的网页！", Gravity.CENTER, 0);
			break;
		case R.id.close:
			SharedPreferences sp1 = History.this.getSharedPreferences(
					"addHistory", 0);
			SharedPreferences.Editor et1 = sp1.edit();
			et1.putBoolean("addHistory", false);
			et1.commit();
			new AboutDialog().getToast(getApplicationContext(),
					"已关闭无痕浏览\n浏览器将不会保存浏览的网页历史！", Gravity.CENTER, 0);
			break;
		}
		return false;
	}

}