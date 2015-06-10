package com.bw.activity;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
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
import com.bw.bean.BW_bookmark;
import com.bw.database.BookmarkDAO;

@ContentView(R.layout.page_bookmark)
public class Bookmark extends Activity implements OnItemClickListener,
		OnClickListener, OnItemLongClickListener {
	@ViewInject(R.id.bookmark_list)
	private ListView bookmarklist;
	@ViewInject(R.id.backtohome)
	private ImageButton backHome;
	@ViewInject(R.id.bookmark_bg)
	private LinearLayout bookmarkBg;

	private SimpleAdapter adapter;
	private String time = "";
	private List<String> list1 = new ArrayList<String>();
	private List<String> list2 = new ArrayList<String>();
	private List<Map<String, String>> listData = new ArrayList<Map<String, String>>();
	private BookmarkDAO bookmarkDAO;
	private Context context;
	public Bookmark bookmark;

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.gc();
		System.out.println("bookmark页面结束，已提醒系统回收垃圾");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		ViewUtils.inject(this);
		bookmark = Bookmark.this;
		context = Bookmark.this;
		// 初始化
		init();
	}

	public void init() {
		SyncSet syncLight = new SyncSet();
		syncLight.syncLight(Bookmark.this, bookmarkBg);
		syncLight.setBgForUI(getApplicationContext(),SyncSet.handler, bookmarkBg);

		bookmarkDAO = new BookmarkDAO(getApplicationContext());// 创建InaccountDAO对象
		List<BW_bookmark> listinfos = bookmarkDAO.getScrollData(0,
				bookmarkDAO.getCount());
		int m = 0;// 定义一个开始标识
		listData = new ArrayList<Map<String, String>>();
		for (BW_bookmark bw_bookmark : listinfos) {// 遍历List泛型集合
			// 将收入相关信息组合成一个字符串，存储到字符串数组的相应位置
			list1.add(bw_bookmark.getFlag());
			list2.add(bw_bookmark.getWeburl());
			Log.e(bw_bookmark.getFlag(), bw_bookmark.getWeburl());
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", bw_bookmark.getFlag());
			map.put("url", bw_bookmark.getWeburl());
			listData.add(map);
			m++;
		}
		adapter = new SimpleAdapter(context, listData,
				R.layout.history_display_style, new String[] { "name", "url" },
				new int[] { R.id.website_names, R.id.website_url });
		;
		bookmarklist.setAdapter(adapter);

	}

	@OnItemLongClick({ R.id.bookmark_list })
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		bookmarkDAO = new BookmarkDAO(context);
		try {
			time = bookmarkDAO.find(position + 1).getTime().toString();
			ClipboardManager cmb = (ClipboardManager) context
					.getSystemService(context.CLIPBOARD_SERVICE);
			cmb.setText(time);
			new AboutDialog().getToast(getApplicationContext(), "记录时间：" + time
					+ "\n此地址已复制到粘贴板", Gravity.CENTER, 0);
		} catch (Exception e) {
			// TODO: handle exception
			time = "";
		}
		return false;
	}

	@OnItemClick({ R.id.bookmark_list })
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		String intentsString=getIntent().getExtras().get("intent").toString();
		if (intentsString.equals("home")) {
			
			// 打开连接
			Intent intent=new Intent(this,MainView.class).putExtra("urlA", listData.get(position).get("url"));
			startActivity(intent);
		}else {
			ConnUrlHandler.openUrl(context, ConnUrlHandler.handler,listData.get(position).get("url"));
		}
		onBackPressed();
		overridePendingTransition(R.anim.feature_scale_in, R.anim.translate);
	}

	@OnClick({ R.id.backtohome })
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backtohome:
			onBackPressed();
			overridePendingTransition(R.anim.feature_scale_in, R.anim.translate);
			break;

		}
	}

}