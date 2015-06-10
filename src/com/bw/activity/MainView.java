package com.bw.activity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.impl.cookie.BasicClientCookie;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.util.PreferencesCookieStore;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnCompoundButtonCheckedChange;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.lidroid.xutils.view.annotation.event.OnLongClick;
import com.rbj.bawang.util.AboutDialog;
import com.rbj.bawang.util.ConnUrlHandler;
import com.rbj.bawang.util.FileListUtil;
import com.rbj.bawang.util.ImageUtil;
import com.rbj.bawang.util.ImageUtilEX;
import com.rbj.bawang.util.ScreenUtils;
import com.rbj.bawang.util.SyncSet;

import example.webbowers.R;

import com.bw.Downloader.DownLoaderTask;
import com.bw.Downloader.DownloadManager;
import com.bw.Downloader.DownloadService;
import com.bw.bean.BW_bookmark;
import com.bw.bean.BW_history;
import com.bw.database.BookmarkDAO;
import com.bw.database.HistoryDAO;
import com.slidingmenu.lib.SlidingMenu;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.util.Log;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

@ContentView(R.layout.main_view)
public class MainView extends Activity implements OnClickListener,
		OnItemClickListener, OnCheckedChangeListener, OnKeyListener,
		OnLongClickListener {
	public static WebView webView;
	public static String cur_url = "";
	private SharedPreferences preferences;
	@ViewInject(R.id.progressWeb)
	private ProgressBar progressWeb = null;
	@ViewInject(R.id.menu_imagebtn_forward)
	private ImageButton menu_ImageBtn_forward = null;
	@ViewInject(R.id.menu_imagebtn_back)
	private ImageButton menu_ImageBtn_back = null;
	@ViewInject(R.id.menu_imagebtn_refresh)
	private ImageButton menu_ImageBtn_refresh = null;
	@ViewInject(R.id.menu_imagebtn_home)
	private ImageButton menu_ImageBtn_menu = null;
	private boolean flag_loading = false;
	@ViewInject(R.id.change_big)
	private LinearLayout changeBigLayout;
	private int count = 1;// 记录当前页面为本次开启浏览器的第几个页面
	public static MainView mainView = null;
	private String urls;
	private Context context;
	private SlidingMenu slidingMenu;
	private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
	private SimpleAdapter simpleAdapter;
	@ViewInject(R.id.checkBox1)
	private CheckBox checkBox;
	@ViewInject(R.id.searchText)
	private TextView searchText;
	@ViewInject(R.id.goSearchText)
	private TextView goSearchText;
	@ViewInject(R.id.exit)
	private Button exitBtn;
	private RelativeLayout slidiBg;
	private int backHomeNum = 2;
	private boolean isExit1 = false;// 防止重复响应回车事件标签
	private PullToRefreshWebView mPullRefreshWebView;
	private DownloadManager downloadManager;
	private PreferencesCookieStore preferencesCookieStore;
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		ViewUtils.inject(this);
		mainView = this;
		context = MainView.this;
		
		mPullRefreshWebView = (PullToRefreshWebView) findViewById(R.id.webview);
		webView = mPullRefreshWebView.getRefreshableView();
		mPullRefreshWebView.setOnRefreshListener(new OnRefreshListener<WebView>() {
			@Override
			public void onRefresh(PullToRefreshBase<WebView> refreshView) {
				// TODO Auto-generated method stub
				webView.reload();
				mPullRefreshWebView.onRefreshComplete();
			}
		});
		init();// id初始化
		initSlidingmenu();// slidingmenu初始化
		initWebview();// 浏览器属性初始化
		readWebketPreferen();// 读取保存浏览器的属性
		try {
			if (getIntent().getIntExtra("visi", 0) == 1) {
				visiAds();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.gc();
	}

	/**
	 * 初始化slidingmenu
	 */
	private void initSlidingmenu() {
		// TODO Auto-generated method stub
		slidingMenu = new SlidingMenu(context);
		slidingMenu.setMode(SlidingMenu.LEFT);
		// slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);// 解决滑动冲突问题
		slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		slidingMenu.setShadowDrawable(R.drawable.shadow);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		slidingMenu.setBehindWidth(ScreenUtils.getScreenWidth(context)
				- (ScreenUtils.getScreenWidth(context) / 3));
		slidingMenu.setFadeDegree(0.5f);
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		slidingMenu.setMenu(R.layout.slidingmenu);

		searchText = (TextView) findViewById(R.id.searchText);// 搜索编辑框
		searchText.setOnKeyListener(this);
		goSearchText = (TextView) findViewById(R.id.goSearchText);// 开始搜索
		goSearchText.setOnClickListener(this);
		exitBtn = (Button) findViewById(R.id.exit);// 退出
		exitBtn.setText("回到桌面");
		exitBtn.setOnClickListener(this);
		ListView listView = (ListView) slidingMenu.findViewById(R.id.menulist);
		listData = new ArrayList<Map<String, Object>>();
		String[] d = new String[] { "文件","下载管理", "历史", "书签", "设置", "存书签" };
		/*int[] e = new int[] { android.R.drawable.sym_contact_card,R.drawable.history_title,
				android.R.drawable.ic_menu_recent_history, R.drawable.bookmark,
				R.drawable.tab_settings, android.R.drawable.ic_menu_add };*/
		int[] e = new int[] {R.drawable.touming};
		for (int i = 0; i < 6; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", e[0]);
			map.put("text", d[i]);
			listData.add(map);
		}
		simpleAdapter = new SimpleAdapter(context, listData,
				R.layout.slidingmenu_listitem,
				new String[] {  "text" }, new int[] { 
						R.id.text });
		listView.setAdapter(simpleAdapter);
		listView.setOnItemClickListener(this);
		
	}

	// 数据初始化
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public void init() {

		preferences = getSharedPreferences(SettingSave.PREFERENCES_NAME,
				MODE_WORLD_WRITEABLE);

		//下载服务的初始化
		downloadManager = DownloadService.getDownloadManager(context);
        preferencesCookieStore = new PreferencesCookieStore(context);
        BasicClientCookie cookie = new BasicClientCookie("test", "hello");
        cookie.setDomain("192.168.1.5");
        cookie.setPath("/");
        preferencesCookieStore.addCookie(cookie);
	}

	/**
	 * 初始化浏览器属性
	 */
	private void initWebview() {
		// TODO Auto-generated method stub
		webView.requestFocus();
		webView.getSettings().setDefaultTextEncodingName("utf-8");
		webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null); // 不开启硬件加速函数
		String dir = this.getApplicationContext()
				.getDir("bawang", Context.MODE_WORLD_READABLE).getPath();
		webView.getSettings().setAppCachePath(dir);
		webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		webView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
		webView.getSettings().setSaveFormData(true);
		webView.getSettings().setSavePassword(true);
		webView.setDownloadListener(new myDownloaderListener());
		webView.setInitialScale(80);// 浏览器的缩放比例
		webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
		webView.getSettings().setUseWideViewPort(true);
		try {
			urls=getIntent().getExtras().get("urlA").toString();
			webView.loadUrl(urls);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			urls=getIntent().getExtras().get("url").toString();
			if (!urls.isEmpty()) {
				webView.loadUrl(ConnUrlHandler.checkURL(urls));
			}else {
				onBackPressed();
			}
		}
		
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				count = count + 1;
				// view.loadUrl(url);
				// cur_url = url;
				SharedPreferences sp = MainView.this.getSharedPreferences(
						"addHistory", MODE_PRIVATE);
				if (sp.getBoolean("addHistory", true)) {
					try {
						if (view.getTitle().equals("")) {
							Log.i("标签为空，不添加记录", view.getUrl());
						} else {
							Date date = new Date(System.currentTimeMillis());
							HistoryDAO flagDAO = new HistoryDAO(
									getApplicationContext());// 创建FlagDAO对象
							BW_history tb_flag = new BW_history();
							String flag = view.getTitle() + "--"
									+ date.toLocaleString().toString();
							tb_flag.setFlag(flag);
							tb_flag.setWeburl(view.getUrl());
							Log.i("网址添加成功::", flag + url);
							Log.i("时间:", date.toLocaleString());
							flagDAO.add(tb_flag);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.gc();
				return super.shouldOverrideUrlLoading(view, url);
			}

		});

		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				progressWeb.setProgress(0);
				menu_ImageBtn_refresh.setImageDrawable(getResources()
						.getDrawable(R.drawable.cancel));
				flag_loading = true;
				if (newProgress > 95) {
					menu_ImageBtn_refresh.setImageDrawable(getResources()
							.getDrawable(R.drawable.refresh));
				}
				if (newProgress >= 100) {
					flag_loading = false;
					progressWeb.setVisibility(View.GONE);
				} else {
					progressWeb.setVisibility(View.VISIBLE);
				}
				Handler handler = new progressWeb();
				if (handler != null) {
					Message msg = handler.obtainMessage();
					msg.what = newProgress;
					handler.sendMessage(msg);
				}

				super.onProgressChanged(webView, newProgress);
			}
		});
	}

	/**
	 * 读取浏览器开启属性的记录
	 */
	private void readWebketPreferen() {
		preferences = getSharedPreferences(SettingSave.PREFERENCES_NAME,
				MODE_WORLD_WRITEABLE);
		boolean isopen = true;
		isopen = preferences.getBoolean(SettingSave.KEY_support_JS, isopen);
		if (isopen) {
			webView.getSettings().setJavaScriptEnabled(true); // 开启JavaScript
		} else {
			webView.getSettings().setJavaScriptEnabled(false); // 关闭JavaScript
		}
		boolean isopen2 = true;
		isopen2 = preferences.getBoolean(SettingSave.KEY_support_ZOOM, isopen2);
		if (isopen2) {
			webView.getSettings().setSupportZoom(true);// 开启缩放
			webView.getSettings().setBuiltInZoomControls(true);// 显示缩放控件图标
		} else {
			webView.getSettings().setSupportZoom(true);
			webView.getSettings().setBuiltInZoomControls(false);
		}
		boolean isopen3 = false;
		isopen3 = preferences.getBoolean(SettingSave.KEY_support_PIC, isopen3);
		if (isopen3) {
			webView.getSettings().setBlockNetworkImage(true); // 显示网络图片
		} else {
			webView.getSettings().setBlockNetworkImage(false); // 不显示网络图片
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.gc();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.e("sliding是否打开", "Focused:" + slidingMenu.isFocused()
					+ "--isShown" + slidingMenu.isShown() + "--sSelected"
					+ slidingMenu.isSelected() + "");
			if (webView.canGoBack()) {
				count = count - 1;
				webView.goBack();
				backHomeNum = 2;
			} else {
				slidingMenu.toggle();
				backHomeNum--;
				if (backHomeNum < 1) {
					onBackPressed();
					overridePendingTransition(R.anim.feature_scale_in,
							R.anim.translate);
				}
			}
		}
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			slidingMenu.toggle();
		}
		return false;
	}

	// settings
	public void setJavascript(boolean flag) {
		Log.e("my_set_js", ":" + flag);
		webView.getSettings().setJavaScriptEnabled(flag);
	}

	public void setZoom(boolean flag) {
		Log.e("my_set_zoom", ":" + flag);
		webView.getSettings().setSupportZoom(flag);
		webView.getSettings().setBuiltInZoomControls(flag);
	}

	public void setBlockPicture(boolean flag) {
		Log.e("my_set_pic", ":" + flag);
		webView.getSettings().setBlockNetworkImage(flag);
	}

	public void setCache(boolean flag) {
		Log.e("my_set_cache", ":" + flag);
		if (flag) {
			webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		} else {
			webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		}
	}

	// inner class for WebViewCLient
	/**
	 * 下载功能
	 * 
	 * @author baojun
	 *
	 */
	// inner class for download
	private class myDownloaderListener implements DownloadListener {

		@Override
		public void onDownloadStart(final String url, String userAgent,
				String arg2, String arg3, long arg4) {
			// if the SD card can't be written or read,then toast
			
				Dialog alertDialog = new AlertDialog.Builder(MainView.this)
						// .setTitle("文件下载提示：")
						.setMessage("文件下载提示：\n此操作会消耗流量\n" + "确定下载吗？")
						.setIcon(R.drawable.about)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										//String target = "/sdcard/xUtils/" + System.currentTimeMillis() + "lzfile.apk";
										try {
											File filePath;
											if (!Environment.getExternalStorageDirectory().exists()) {
												filePath = new File(Environment.getExternalStorageDirectory()
														+ "/BWfile/Download/");

											} else {
												filePath = new File("/sdcard/BWfile/Download/");
											}
											//String fileName = FileListUtil.getReallyFileName(url);
											LogUtils.e(url);
											String fileName = "BW-"+url.substring(url.lastIndexOf("/"));
											//fileName = URLDecoder.decode(fileName, "UTF-8");
								            downloadManager.addNewDownload(url,
								            		fileName,
								                    filePath.getPath().toString()+fileName,
								                    true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
								                    true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
								                    null);
								            AboutDialog.getToast(context, "已加入下载,请在[下载管理]中查看", Gravity.CENTER, 1);
								            LogUtils.e("开始下载了");
								        } catch (DbException e) {
								            LogUtils.e(e.getMessage(), e);
								        }
										/*DownLoaderTask download_task = new DownLoaderTask(
										context, new PopWin(),downProgressBar, downFileName);
										download_task.execute(url);*/
										count -= 1;
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).create();
				alertDialog.show();
			}
	}



	@SuppressLint("HandlerLeak")
	private class progressWeb extends Handler {

		@Override
		public void handleMessage(Message msg) {
			LogUtils.e("网页加载进度"+msg.what);
			progressWeb.setProgress(msg.what);
			if (msg.what == 100)
				progressWeb.setProgress(100);
			super.handleMessage(msg);
		}

	}

	@SuppressLint("HandlerLeak")
	private class ChanggeBig extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// 0:全屏
			// 1：退出全屏
			Animation leftAni = AnimationUtils.loadAnimation(context,
					R.anim.ttt);
			if (msg.what == 0) {
				changeBigLayout.setVisibility(View.GONE);
			}
			if (msg.what == 1) {
				changeBigLayout.setVisibility(View.VISIBLE);
				changeBigLayout.startAnimation(leftAni);
			}
			Log.e("状态", "0为全屏1为退出全屏，当前状态为:" + msg.what);
			super.handleMessage(msg);
		}

	}

	@OnItemClick({ R.id.menulist })
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		switch (parent.getId()) {
		case R.id.menulist:
			switch (position ) {

			case 0:
				Intent intent_filetest = new Intent();
				intent_filetest.putExtra("intent", "mainview");
				intent_filetest.setClass(getApplicationContext(),
						FileTest.class);
				startActivity(intent_filetest);
				break;
			case 1:
				Intent intent = new Intent(this, DownloadListActivity.class).putExtra("intent", "mainview");
		        startActivity(intent);
				break;
			case 2:
				Intent intent_history = new Intent();
				intent_history.putExtra("intent", "mainview");
				intent_history.setClass(getApplicationContext(), History.class);
				startActivity(intent_history);
				break;
			case 3:
				Intent intent_bookmark = new Intent();
				intent_bookmark.putExtra("intent", "mainview");
				intent_bookmark.setClass(getApplicationContext(),
						Bookmark.class);
				startActivity(intent_bookmark);
				break;
			case 4:
				Intent intent_settings = new Intent();
				intent_settings.putExtra("intent", "mainview");
				intent_settings.setClass(getApplicationContext(),
						SettingSave.class);
				startActivity(intent_settings);
				break;
			case 5:
				Date date = new Date(System.currentTimeMillis());
				String name = webView.getTitle();
				String url = webView.getUrl();
				BookmarkDAO bookmarkDAO = new BookmarkDAO(context);
				BW_bookmark bw_bookmark = new BW_bookmark();
				bw_bookmark.setFlag(name);
				bw_bookmark.setWeburl(url);
				bw_bookmark.setTime(date.toLocaleString().toString());
				bookmarkDAO.add(bw_bookmark);
				AboutDialog.getToast(getApplicationContext(), "书签添加成功！",
						Gravity.CENTER, 0);
				// MainView.this.finish();
				break;

			}
			slidingMenu.toggle();
		}
	}

	@OnClick({ R.id.menu_imagebtn_forward, R.id.menu_imagebtn_back,
			R.id.menu_imagebtn_refresh, R.id.menu_imagebtn_home, 
			R.id.goSearchText, R.id.exit })
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.menu_imagebtn_forward:
			webView.goForward();
			break;
		case R.id.menu_imagebtn_back:
			webView.goBack();
			count -= 1;

			break;
		case R.id.menu_imagebtn_refresh:
			if (flag_loading == false)
				webView.reload();
			else
				webView.stopLoading();
			break;
		case R.id.menu_imagebtn_home:
			onBackPressed();
			overridePendingTransition(R.anim.feature_scale_in, R.anim.translate);
			break;
		case R.id.goSearchText:
			slidingMenu.toggle();
			// 打开连接
			ConnUrlHandler.openUrl(context, ConnUrlHandler.handler, searchText
					.getText().toString());
			// 强制隐藏输入法
			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(MainView.this.getCurrentFocus()
							.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
			break;
		case R.id.exit:
			/*
			 * stopService(intentServer);// 停止服务 MainView.this.finish();
			 */
			/**
			 * 回退HOME桌面
			 */
			slidingMenu.toggle();
			Intent home = new Intent(Intent.ACTION_MAIN);
			home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			home.addCategory(Intent.CATEGORY_HOME);
			startActivity(home);
			overridePendingTransition(R.anim.feature_scale_in, R.anim.translate);
			break;
		}
	}

	private void visiAds() {
		// TODO Auto-generated method stub
		Message msg = new ChanggeBig().obtainMessage();
		msg.what = 0;
		AboutDialog.getToast(getApplicationContext(), "已开启全屏浏览",
				Gravity.TOP, 0);
		checkBox.setChecked(true);
		checkBox.setText("退出全屏");
		// 全屏
		// ((Activity) context).requestWindowFeature(Window.FEATURE_NO_TITLE);
		((Activity) context).getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		new ChanggeBig().sendMessage(msg);
	}

	@OnCompoundButtonCheckedChange(R.id.checkBox1)
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		switch (buttonView.getId()) {
		case R.id.checkBox1:

			if (isChecked) {
				visiAds();
			} else {
				Message msg = new ChanggeBig().obtainMessage();
				msg.what = 1;
				AboutDialog.getToast(getApplicationContext(), "已退出全屏浏览",
						Gravity.TOP, 0);
				buttonView.setText("全屏");

				// 退出全屏
				final WindowManager.LayoutParams attrs = getWindow()
						.getAttributes();
				attrs.flags = (WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
				getWindow().setAttributes(attrs);
				getWindow().clearFlags(
						WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
				new ChanggeBig().sendMessage(msg);
			}
			overridePendingTransition(R.anim.feature_scale_in, R.anim.translate);
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

					// 强制隐藏输入法
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
							.hideSoftInputFromWindow(MainView.this
									.getCurrentFocus().getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);
					slidingMenu.toggle();
					// 打开连接
					ConnUrlHandler.openUrl(context, ConnUrlHandler.handler,
							searchText.getText().toString());
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
		}
		return false;
	}

	@OnLongClick({R.id.menu_imagebtn_refresh,R.id.menu_imagebtn_home})
	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.menu_imagebtn_refresh:
			// 粘贴板
			ClipboardManager cmb = (ClipboardManager) context
					.getSystemService(context.CLIPBOARD_SERVICE);
			cmb.setText(webView.getUrl());
			AboutDialog.getToast(context, "网址已复制到剪贴板", Gravity.CENTER, 0);
			// 分享
			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(Intent.EXTRA_TEXT, webView.getUrl());

			shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(Intent.createChooser(shareIntent, "请选择分享"));
			break;
		case R.id.menu_imagebtn_home:
			slidingMenu.toggle();
			break;
		}
		return false;
	}
}
