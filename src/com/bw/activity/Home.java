package com.bw.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import example.webbowers.R;

import com.bw.adapter.MyFragmentPagerAdapter;
import com.bw.adapter.MyOnPageChangeListener;
import com.bw.fragment.Tab1;
import com.bw.fragment.Tab2;
import com.bw.fragment.Tab3;
import com.gc.flashview.FlashView;
import com.gc.flashview.constants.EffectConstants;
import com.gc.flashview.listener.FlashViewListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rbj.bawang.util.AboutDialog;
import com.rbj.bawang.util.ConnUrlHandler;
import com.rbj.bawang.util.HttpUtilbj;
import com.rbj.bawang.util.ImageUtil;
import com.rbj.bawang.util.ImageUtilEX;
import com.rbj.bawang.util.ScreenUtils;
import com.rbj.bawang.util.SyncSet;
import com.slidingmenu.lib.SlidingMenu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

@ContentView(R.layout.home_index)
public class Home extends FragmentActivity implements OnClickListener,
		OnKeyListener, OnItemClickListener {
	private static Boolean isExit = false;
	private static Boolean isExit1 = false;
	@ViewInject(R.id.autoCompleteTextView)
	private AutoCompleteTextView autoCompleteTextView;
	@ViewInject(R.id.rela_homebg)
	private RelativeLayout homebglLayout;
	@ViewInject(R.id.menuopen)
	private ImageButton openmenu;
	public static Home home = null;
	private Context context;
	private RelativeLayout slidiBg;//sliding背景

	private SlidingMenu slidingMenu;// slidingmenu
	private TextView searchText;
	private TextView goSearchText;
	private Button exitBtn;
	@ViewInject(R.id.flashviews)
	private FlashView flashView;
	@ViewInject(R.id.viewPager)
	private ViewPager viewPage;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		init();
		initSlidingmenu();

	}

	private void initSlidingmenu() {
		// TODO Auto-generated method stub
		slidingMenu = new SlidingMenu(context);
		slidingMenu.setMode(SlidingMenu.LEFT);
		//slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);// 解决滑动冲突问题
		slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		slidingMenu.setShadowDrawable(R.drawable.shadow);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		slidingMenu.setBehindWidth(ScreenUtils.getScreenWidth(context)
				- (ScreenUtils.getScreenWidth(context) / 5));
		slidingMenu.setFadeDegree(0.5f);
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		slidingMenu.setMenu(R.layout.slidingmenu);

		searchText = (TextView) slidingMenu.findViewById(R.id.searchText);// 搜索编辑框
		searchText.setOnKeyListener(this);
		goSearchText = (TextView) slidingMenu.findViewById(R.id.goSearchText);// 开始搜索
		goSearchText.setOnClickListener(this);
		exitBtn = (Button) slidingMenu.findViewById(R.id.exit);
		exitBtn.setOnClickListener(this);

		ListView listView = (ListView) slidingMenu.findViewById(R.id.menulist);

		List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		String[] d = new String[] { "文件","下载管理", "历史", "书签", "设置", "关于" };
		/*int[] e = new int[] { android.R.drawable.sym_contact_card,R.drawable.history_title,
				android.R.drawable.ic_menu_recent_history, R.drawable.bookmark,
				R.drawable.tab_settings, android.R.drawable.sym_action_chat };*/
		int[] e = new int[] {R.drawable.touming};
		for (int i = 0; i < 5; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", e[0]);
			map.put("text", d[i]);
			listData.add(map);
		}
		SimpleAdapter simpleAdapter = new SimpleAdapter(context, listData,
				R.layout.slidingmenu_listitem,
				new String[] {  "text" }, new int[] {
						R.id.text });
		listView.setAdapter(simpleAdapter);
		listView.setOnItemClickListener(this);

	}

	private void init() {
		// TODO Auto-generated method stub
		home = this;
		context = Home.this;
		/**
		 * 广告栏位初始化
		 */
		ArrayList<String> imageUrls = new ArrayList<String>();
		/*if (HttpUtilbj.isConnectInternet(Home.this)) {
			
		}*/
		imageUrls
				.add("http://img.my.csdn.net/uploads/201309/01/1378037235_3453.jpg");
		imageUrls
				.add("http://img.my.csdn.net/uploads/201309/01/1378037235_7476.jpg");
		imageUrls
				.add("http://img.my.csdn.net/uploads/201309/01/1378037235_9280.jpg");
		imageUrls
				.add("http://img.my.csdn.net/uploads/201309/01/1378037193_1286.jpg");
		flashView.setImageUris(imageUrls);
		flashView.setEffect(EffectConstants.CUBE_EFFECT);// 更改图片切换的动画效果
		flashView.setOnPageClickListener(new FlashViewListener() {
			@Override
			public void onClick(int position) {
				Intent intent = new Intent(context, MainView.class);
				switch (position + 1) {
				case 1:
					intent.putExtra("urlA", "http://401763159.qzone.qq.com/");
					context.startActivity(intent);
					break;
				case 2:
					intent.putExtra("urlA", "http://wufazhuce.com/");
					context.startActivity(intent);
					break;
				case 3:
					intent.putExtra("urlA", "http://www.douban.com/");
					context.startActivity(intent);
					break;
				case 4:
					intent.putExtra("urlA",
							"http://apk.91.com/Soft/Android/example.webbowers-88-5.0.html");
					context.startActivity(intent);
					break;
				}
			}
		});
		/*
		 * flashView.setOnPageClickListener(new FlashViewListener() {
		 * 
		 * @Override public void onClick(int position) { // TODO Auto-generated
		 * method stub Toast.makeText(getApplicationContext(), "你的点击的是第" +
		 * (position + 1) + "张图片！", 1000).show(); } });
		 */
		// ======================================================================
		new SyncSet().setBgForUI(context, SyncSet.handler, homebglLayout);
		// ======================================================================
		SharedPreferences hotPreferences = getSharedPreferences("addHistory",
				MODE_WORLD_WRITEABLE);
		String hotWord = hotPreferences.getString("hotWord", "");
		String[] hotWords = hotWord.split(",");
		autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
		ArrayAdapter<String> AutoAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, hotWords); // 创建一个ArrayAdapter适配器
		autoCompleteTextView.setAdapter(AutoAdapter);
		autoCompleteTextView.setOnKeyListener(this);
		// ======================================================================
		viewPage = (ViewPager) findViewById(R.id.pager);
		viewPage.setOffscreenPageLimit(3);// 预告加载的页面数量
		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(new Tab1());
		fragments.add(new Tab2());
		fragments.add(new Tab3());
		viewPage.setAdapter(new MyFragmentPagerAdapter(
				getSupportFragmentManager(), fragments));
		viewPage.setOnPageChangeListener(new MyOnPageChangeListener());
	}
@OnClick(R.id.menuopen)
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.menuhome:
			openOptionsMenu();
			break;
		case R.id.goSearchText:
			if (searchText.getText().toString().equals("")) {
				AboutDialog.getToast(context, "内容空白~", Gravity.CENTER, 0);
			}else {
				Intent intent = new Intent(context, MainView.class);
			intent.putExtra("url", searchText.getText().toString());
			startActivity(intent);
			}
			overridePendingTransition(R.anim.base_slide_right_in,
					R.anim.anim_alpha);
			break;
		case R.id.exit:
			home.finish();
			overridePendingTransition(R.anim.feature_scale_in, R.anim.translate);
			break;
		case R.id.menuopen:
			slidingMenu.toggle();
			break;
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.searchText:
			Timer tExit1 = null;
			if (isExit1 == false) {
				isExit1 = true; // 准备退出第二次的相应
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					if (searchText.getText().toString().equals("")) {
						new AboutDialog().getToast(context, "内容空白~", Gravity.CENTER, 0);
					}else {
						Intent intent = new Intent(context, MainView.class);
					intent.putExtra("url", searchText.getText().toString());
					startActivity(intent);
					}
					
				}
				tExit1 = new Timer();
				tExit1.schedule(new TimerTask() {
					@Override
					public void run() {
						isExit1 = false; // 取消退出
					}
				}, 1000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

			}

			break;
		case R.id.autoCompleteTextView:
			Timer tExit = null;
			if (!isExit1) {
				isExit1 = true; // 准备退出第二次的相应
				if (keyCode == KeyEvent.KEYCODE_ENTER) { // 如果为回车键
					String url = autoCompleteTextView.getText().toString()
							.trim();
					if (!url.isEmpty()) {
						LogUtils.e(url);
						SharedPreferences sp = Home.this.getSharedPreferences(
								"addHistory", MODE_WORLD_WRITEABLE);
						String aString = sp.getString("hotWord", "");
						if (aString.contains(url)) {
							Log.e("热词已有无需添加！", "bad:_" + url);
						} else {
							SharedPreferences.Editor et = sp.edit();
							et.putString("hotWord", aString + "," + url);
							et.commit();
							Log.e("热词已经添加！", "ok:_" + url);
						}
						Intent intent = new Intent(this, MainView.class)
								.putExtra("url", url);
						startActivity(intent);
						overridePendingTransition(R.anim.base_slide_right_in,
								R.anim.anim_alpha);
					} else
						new AboutDialog().getToast(getApplicationContext(),
								"内容空白~", Gravity.TOP, 0);
				}
				tExit = new Timer();
				tExit.schedule(new TimerTask() {
					@Override
					public void run() {
						isExit1 = false; // 取消退出
					}
				}, 1000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

			}
			break;
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		switch (parent.getId()) {

		case R.id.menulist:
			switch (position ) {
			case 0:
				startActivity(new Intent(getApplicationContext(),
						FileTest.class).putExtra("intent", "home"));
				break;
			case 1:
				Intent intent = new Intent(this, DownloadListActivity.class).putExtra("intent", "home");
		        startActivity(intent);
				break;
			case 2:
				startActivity(new Intent(getApplicationContext(), History.class)
						.putExtra("intent", "home"));
				break;
			case 3:
				startActivity(new Intent(getApplicationContext(),
						Bookmark.class).putExtra("intent", "home"));
				break;
			case 4:
				startActivity(new Intent(getApplicationContext(),
						SettingSave.class).putExtra("intent", "home"));
				break;
			case 5:
				new AboutDialog().setDialogView(Home.this);
				break;

			}
			slidingMenu.toggle();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.gc();
		LogUtils.e("home页面结束，已提醒系统回收垃圾");
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK)
			exitBy2Click();
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			slidingMenu.toggle();
		}
		return false;
	}

	// 双击退出
	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			slidingMenu.toggle();
			new AboutDialog().getToast(getApplicationContext(), "再按一次回到桌面",
					Gravity.CENTER, 0);
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 1000); // 如果1秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			/*
			 * Intent home = new Intent(Intent.ACTION_MAIN);
			 * home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 * home.addCategory(Intent.CATEGORY_HOME); startActivity(home);
			 * overridePendingTransition(R.anim.feature_scale_in,
			 * R.anim.translate);
			 */
			// finish();
			System.exit(0);
		}
	}
}