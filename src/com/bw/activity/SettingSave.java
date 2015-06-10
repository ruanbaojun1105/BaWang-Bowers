package com.bw.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.ab.global.AbConstant;
import com.ab.util.AbFileUtil;
import com.ab.util.AbImageUtil;
import com.ab.util.AbStrUtil;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapCommonUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnCompoundButtonCheckedChange;
import com.rbj.bawang.util.AboutDialog;
import com.rbj.bawang.util.ImageUtil;
import com.rbj.bawang.util.SyncSet;

import example.webbowers.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@ContentView(R.layout.settings)
public class SettingSave extends Activity implements OnClickListener,OnCheckedChangeListener {

	public static String PREFERENCES_NAME = "PRE_NAME";
	public static String KEY_support_JS = "VALUE_JS";
	public static String KEY_support_PIC = "VALUE_PIC";
	public static String KEY_support_ZOOM = "VALUE_ZOOM";
	public static String KEY_SUPPORT_CACHE = "VALUE_CACHE";
	public static String KEY_SUPPORT_HISTORY = "VALUE_HISTORY";
	public static String KEY_support_blur = "VALUE_BLUR";
	private SharedPreferences preferences;
	private SharedPreferences.Editor prefer_editor;
	@ViewInject(R.id.settings_set_javascript_toggle_btn)
	private CheckBox toggleBtn_setJS = null;
	@ViewInject(R.id.settings_set_zoom_toggle_btn)
	private CheckBox toggleBtn_setZoom = null;
	@ViewInject(R.id.settings_set_picture_toggle_btn)
	private CheckBox toggleBtn_setPIC = null;
	@ViewInject(R.id.change_directory)
	private FrameLayout change_save_directory_Btn = null;
	@ViewInject(R.id.clear_data)
	private FrameLayout clear_data_Btn = null;
	private View dialogView = null;
	public TextView dialogText = null;
	public EditText dialogEdittext = null;
	@ViewInject(R.id.openblur)
	private CheckBox openBlur;
	@ViewInject(R.id.openPic)
	private FrameLayout openPicture;
	@ViewInject(R.id.backtohome)
	private ImageButton backHome;
	@ViewInject(R.id.imagebg_look)
	private ImageView imageViewbg;
	@ViewInject(R.id.setting_bg)
	private RelativeLayout settingBg;
	@ViewInject(R.id.set_pic)
	private Button setPicButton;
	@ViewInject(R.id.lightset)
	private FrameLayout setLight;
	private File file;
	private boolean change = false;
	public static SettingSave settingSave = null;
	public static final String search_engine_baidu = "http://www.baidu.com/s?wd=";
	public static final String search_engine_soso = "http://www.soso.com/q?w=";
	public static final String search_engine_360 = "http://www.so.com/s?q=";
	public boolean clear_flags[] = { false, false };
	public ArrayAdapter<String> adapter;
	private ByteArrayOutputStream oStreamTest;
	private Context context;
	private String currentFilePath="";

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.gc();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		ViewUtils.inject(this);
		
		initView();

	}
	/**
	 * 初始化数据
	 */
	private void initView() {
		// TODO Auto-generated method stub
		settingSave = this;
		// Toast.makeText(getApplicationContext(), getIntentName, 0).show();
		settingBg = (RelativeLayout) findViewById(R.id.setting_bg);
		SyncSet syncLight = new SyncSet();
		// syncLight.syncLight(getApplicationContext(),settingBg);
		syncLight.setBgForUI(getApplicationContext(),syncLight.handler, settingBg);
		preferences = getSharedPreferences(PREFERENCES_NAME,
				MODE_WORLD_WRITEABLE);
		prefer_editor = preferences.edit();
		toggleBtn_setJS.setChecked(preferences
				.getBoolean(KEY_support_JS, true));// default case:doesn't
		//////////////////////////////////////////
		toggleBtn_setZoom.setChecked(preferences.getBoolean(KEY_support_ZOOM,
				true));
		//////////////////////////////////////////
		toggleBtn_setPIC.setChecked(preferences.getBoolean(KEY_support_PIC,
				false));
		//////////////////////////////////////////
		openBlur.setChecked(preferences.getBoolean(KEY_support_blur,
				true));
		dialogView = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.clean_history, null);
	}
	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			Uri uri = data.getData();
			currentFilePath = getPath(uri);
			LogUtils.e("该图片地址为："+currentFilePath);
			if(!TextUtils.isEmpty(currentFilePath)){
				 //相册中原来的图片
		        File mFile = new File(currentFilePath);
		        Bitmap mBitmap;
		        try{
		        	mBitmap = AbFileUtil.getBitmapFromSD(mFile,AbConstant.SCALEIMG,500,500);
		        	//mBitmap=ImageUtil.getimage(mFile.getPath());
		        	
		        	/*ContentResolver cr = this.getContentResolver();
		        	mBitmap=readBitMap(context, cr, uri);	*/					
		        	Drawable drawable1 = new BitmapDrawable(mBitmap);
						settingBg.setBackground(drawable1);
						imageViewbg.setBackgroundDrawable(drawable1);
						//加入存储
						change=true;
						ByteArrayOutputStream oStream = new ByteArrayOutputStream();// 声明并创建一个输出字节流对象
						mBitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);// 将BITMAP压缩为png格式，图像质量为50，第三个参数为输出流
						oStreamTest = oStream;
						AboutDialog.getToast(context, "此为效果预览,点击OK确认保存~", Gravity.TOP, 1);
		            
		        }catch (Exception e) {
		        	//Toast.makeText(this, "没有找到图片", 0).show();
				}
			}else{
	        	LogUtils.e("未在存储卡中找到这个文件");
	        }
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	     * 以最省内存的方式读取本地资源的图片
	     * @param context
	     * @param resId
	     * @return
	 * @throws FileNotFoundException 
	     */  
	    public static Bitmap readBitMap(Context context,ContentResolver cr, Uri src) throws FileNotFoundException{  
	        BitmapFactory.Options opt = new BitmapFactory.Options();  
	        opt.inPreferredConfig = Bitmap.Config.RGB_565;   
	       opt.inPurgeable = true;  
	       opt.inInputShareable = true;  
	         //获取资源图片  
	       //InputStream is = context.getResources().openRawResource(resId);  
	           return BitmapFactory.decodeStream(cr.openInputStream(src),null,opt);  
	   }
	/**
	 * 从相册得到的url转换为SD卡中图片路径
	 */
	public String getPath(Uri uri) {
		if(TextUtils.isEmpty(uri.getAuthority())){
			return null;
		}
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(column_index);
		return path;
	}

	public void setDialogView() {

		final Dialog dialog = new Dialog(SettingSave.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialogsetfilepath);
		dialog.getWindow().setBackgroundDrawableResource(R.drawable.btn_shadows2);
		Display d = getWindowManager().getDefaultDisplay(); // 为获取屏幕宽、高
		LayoutParams p = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
		p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.95
		final Button tureSet = (Button) dialog.findViewById(R.id.tureSet);
		final Button falseSet = (Button) dialog.findViewById(R.id.falseSet);
		tureSet.setOnClickListener(new View.OnClickListener() {

			@SuppressLint("SdCardPath")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText inputText = (EditText) dialog
						.findViewById(R.id.inputText);
				if (inputText.getText().toString().trim().equals("")) {
					new AboutDialog().getToast(getApplicationContext(), "输入为空",
							Gravity.TOP, 0);
				} else {
					LogUtils.e(inputText.getText().toString());
					String savePath = inputText.getText().toString().trim();
					SharedPreferences sp = getSharedPreferences("down_path",
							getApplicationContext().MODE_WORLD_READABLE);
					SharedPreferences.Editor et = sp.edit();
					et.putString("down_path", savePath);
					et.putBoolean("isChangePath", true);
					et.commit();
					new AboutDialog().getToast(getApplicationContext(),
							"下载地址更改成功！", Gravity.TOP, 0);
					dialog.dismiss();
					// savePath = sp.getBoolean("isChangePath", false);
				}

			}
		});
		falseSet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	public void cleanHistory() {

		final Dialog dialog = new Dialog(SettingSave.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.clean_history);
		dialog.getWindow().setBackgroundDrawableResource(R.drawable.btn_shadows2);
		Display d = getWindowManager().getDefaultDisplay(); // 为获取屏幕宽、高
		LayoutParams p = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
		p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.95
		final Button tureSet, falseSet;
		tureSet = (Button) dialog.findViewById(R.id.button1);
		falseSet = (Button) dialog.findViewById(R.id.button2);
		tureSet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					try {
						File file, file1;
						if (!Environment.getExternalStorageDirectory().exists()) {
							file = new File(Environment
									.getExternalStorageDirectory()
									+ "/BWfile/bg.png");
							file1 = new File(Environment
									.getExternalStorageDirectory()
									+ "/BWfile/bg1.png");
						} else {
							file = new File("/sdcard/BWfile/bg.png");
							file1 = new File("/sdcard/BWfile/bg1.png");
						}
						file.delete();// 清空背景图片的设定
						file1.delete();

						// 2
						prefer_editor.clear();
						prefer_editor.commit();
						// 3
						SharedPreferences sp = getSharedPreferences(
								"down_path",
								getApplicationContext().MODE_WORLD_READABLE);
						SharedPreferences.Editor et = sp.edit();
						et.clear();
						et.commit();

						if (getIntent().getExtras().get("intent").equals("home")) {
							Home.home.finish();
						} else {
							MainView.mainView.finish();
						}
						Intent intent = new Intent();
						intent.setClass(SettingSave.this, Start.class);
						startActivity(intent);
						SettingSave.this.finish();
						Log.e("clear_settings", "cleared");
					} catch (Exception e) {
						Log.e("clear_settings", "failed");
					}
					AboutDialog.getToast(getApplicationContext(),
							"已还原设置！", Gravity.CENTER, 0);
				}

				// savePath = sp.getBoolean("isChangePath", false);

		});
		falseSet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	// 设置夜间模式,需要用线程实现才能实时调节屏幕的亮度
	public void lightMenu() {

		final String[] items = new String[] { "夜间", "日间", "正常" };
		Builder builder = new AlertDialog.Builder(SettingSave.this);
		// builder.setIcon(R.drawable.about);
		// builder.setTitle("Please choose list:");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				new AboutDialog().getToast(getApplicationContext(), "已切换"
						+ items[which] + "模式", Gravity.CENTER, 0);
				SharedPreferences sp1 = getApplicationContext()
						.getSharedPreferences("LIGHTSET", MODE_WORLD_READABLE);
				switch (which) {
				case 0:
					// 设置完毕后，保证每个页面的亮度都一致
					SharedPreferences.Editor et1 = sp1.edit();
					et1.putString("light", "0");
					et1.commit();

					break;
				case 1:
					SharedPreferences.Editor et2 = sp1.edit();
					et2.putString("light", "1");
					et2.commit();
					break;
				case 2:
					WindowManager.LayoutParams p = getWindow().getAttributes();
					Log.e("亮度为", p.screenBrightness + "");
					SharedPreferences.Editor et3 = sp1.edit();
					et3.putString("light", p.screenBrightness + "");
					et3.commit();
					break;
				default:
					break;

				}
				new AboutDialog().getToast(getApplicationContext(),
						"文件浏览和设置页面将不会同步亮度的设置", Gravity.BOTTOM, 0);
			}
		});
		builder.create().show();

	}
@OnClick({R.id.backtohome,R.id.change_directory,R.id.clear_data,R.id.openPic,R.id.set_pic,R.id.lightset})
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backtohome:
			onBackPressed();
			overridePendingTransition(R.anim.feature_scale_in, R.anim.translate);
			break;
		case R.id.change_directory:
			setDialogView();
			break;
		case R.id.clear_data:
			cleanHistory();
			break;
		case R.id.openPic:
			Intent intent = new Intent();
			/* 开启Pictures画面Type设定为image */
			intent.setType("image/*");
			/* 使用Intent.ACTION_GET_CONTENT这个Action */
			intent.setAction(Intent.ACTION_GET_CONTENT);
			/* 取得相片后返回本画面 */
			startActivityForResult(intent, 1);
			overridePendingTransition(R.anim.feature_scale_in,
					R.anim.translate);
			break;
		case R.id.set_pic:
			if (change) {
				try {
					if (!Environment.getExternalStorageDirectory().exists()) {
						file = new File(
								Environment.getExternalStorageDirectory()
										+ "/BWfile");
					} else {
						file = new File("/sdcard/BWfile");
					}
					if (!file.exists()) {
						file.mkdir();
						Log.i("设定路径为空", "已自动创建该地址的文件夹");
					} 
					FileOutputStream fileOutputStream = new FileOutputStream(
							file + "/bg.png");
					fileOutputStream.write(oStreamTest.toByteArray());
					fileOutputStream.close();
					Log.e("存储的背景图片写入状态", "已完成写入");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				AboutDialog.getToast(getApplicationContext(),
						"自定义背景进度：成功！", Gravity.BOTTOM, 0);
				if (getIntent().getExtras().get("intent").equals("home")) {
					onBackPressed();
					Home.home.finish();
				} else {
					MainView.mainView.finish();
					Home.home.finish();
					onBackPressed();
				}
				Intent re = new Intent(SettingSave.this, Home.class);
				startActivity(re);
				overridePendingTransition(R.anim.feature_scale_in,
						R.anim.translate);
			} else {
				new AboutDialog().getToast(getApplicationContext(),
						"自定义背景进度：失败！\n你还未选择背景当作更换对象。", Gravity.BOTTOM, 0);
			}

			break;
		case R.id.lightset:
			// lightMenu();
			AboutDialog.getToast(getApplicationContext(),
					"此功能有异常，后续版本奉上~", Gravity.CENTER, 0);
			
			break;
		}
	}
	@OnCompoundButtonCheckedChange({R.id.settings_set_javascript_toggle_btn,R.id.settings_set_zoom_toggle_btn,R.id.openblur})
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		switch (buttonView.getId()) {
		case R.id.settings_set_javascript_toggle_btn:
			if (isChecked) {
				prefer_editor.putBoolean(KEY_support_JS, true);
			} else {
				prefer_editor.putBoolean(KEY_support_JS, false);

			}
			prefer_editor.commit();
			new AboutDialog().getToast(getApplicationContext(), "ok",
					Gravity.CENTER, 0);
			break;
		case R.id.settings_set_zoom_toggle_btn:
			if (isChecked) {
				prefer_editor.putBoolean(KEY_support_ZOOM, true);

			} else {
				prefer_editor.putBoolean(KEY_support_ZOOM, false);
			}
			prefer_editor.commit();
			new AboutDialog().getToast(getApplicationContext(), "ok",
					Gravity.CENTER, 0);
			break;
		case R.id.openblur:
			if (isChecked) {
				prefer_editor.putBoolean(KEY_support_blur, true);
				new AboutDialog().getToast(getApplicationContext(), "duang~duang~duang,已打开毛玻璃效果",
						Gravity.CENTER, 0);
			} else {
				prefer_editor.putBoolean(KEY_support_blur, false);
			}
			prefer_editor.commit();
			break;
		}
	}
}
