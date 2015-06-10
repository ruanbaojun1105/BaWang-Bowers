package com.rbj.bawang.util;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

/**
 * 同步UI
 * 
 * @author baojun
 *
 */
public class SyncSet {
	public void syncLight(final Context context, final View view) {
		// 设置完毕后，保证每个页面的亮度都一致

		new Thread(new Runnable() {

			@Override
			public void run() {
				// Message m=new Message();
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// m.what=0x111;
				// mHandler.sendMessage(m);
				SharedPreferences spset = context.getSharedPreferences(
						"LIGHTSET", -1);
				String lightSet = spset.getString("light", "");
				if (lightSet != null) {
					if (lightSet.equals("0")) {
						WindowManager.LayoutParams p = ((Activity) context)
								.getWindow().getAttributes();
						Log.e("亮度为", p.screenBrightness + "");
						p.screenBrightness = 0.01f;
						((Activity) context).getWindow().setAttributes(p);
						Log.e("亮度为", p.screenBrightness + "");
					} else if (lightSet.equals("1")) {
						WindowManager.LayoutParams p1 = ((Activity) context)
								.getWindow().getAttributes();
						p1.screenBrightness = 1.0f;
						((Activity) context).getWindow().setAttributes(p1);
						Log.e("亮度为", p1.screenBrightness + "");
					} else {
						WindowManager.LayoutParams p2 = ((Activity) context)
								.getWindow().getAttributes();
						p2.screenBrightness = -1.0f;
						((Activity) context).getWindow().setAttributes(p2);
						Log.e("亮度为", p2.screenBrightness + "");
						// int value = 0;
						// ContentResolver cr = context.getContentResolver();
						// try {
						// value = Settings.System.getInt(cr,
						// Settings.System.SCREEN_BRIGHTNESS);
						// } catch (SettingNotFoundException e) {
						// value=255;
						// }
						// WindowManager.LayoutParams p1 = ((Activity)
						// context).getWindow()
						// .getAttributes();
						// float f=value / 255.0f;
						// p1.screenBrightness = f;
						// ((Activity) context).getWindow().setAttributes(p1);
						// Log.e("亮度为", p1.screenBrightness+"");

					}

				}

			}

		}).start();

	}

	static View view;
	static boolean change = false;

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static void setBgForUI(Context context, Handler handler, View view) {
		// add save pic_path
		SyncSet.view = view;
		File file;
		if (!Environment.getExternalStorageDirectory().exists()) {
			file = new File(Environment.getExternalStorageDirectory()
					+ "/BWfile/bg.png");
		} else {
			file = new File("/sdcard/BWfile/bg.png");
		}
		if (!file.exists()) {
			Log.e("是否已更改过背景图片", "否，不处理此方法");
		} else {
			change = true;
			Log.e("是否已更改过背景图片", change + "是，正在修改背景");
			Drawable d;
			SharedPreferences preferences = context.getSharedPreferences(
					"PRE_NAME", context.MODE_WORLD_WRITEABLE);
			Bitmap btp = new BitmapDrawable(BitmapFactory.decodeFile(file
					.getPath())).getBitmap();
			if (preferences.getBoolean("VALUE_BLUR", true)) {

				d = ImageUtilEX.convertBitmap2Drawable(Blur.fastblur(context,
						btp, 12));
			} else {
				d = ImageUtilEX.convertBitmap2Drawable(btp);
			}
			if (!btp.isRecycled()) {
				btp.recycle(); // 回收图片所占的内存
				System.gc();// 提醒系统及时回收
			}
			if (handler != null) {
				Message msg = handler.obtainMessage();
				// msg.obj = ImageUtils.BoxBlurFilter(a);
				msg.obj = d;
				handler.sendMessage(msg);
			}
		}

	}

	public static void justSetbg(Context context, Handler handler, View view,
			Bitmap bitmap) {
		SyncSet.view = view;
		if (handler != null) {
			Message msg = handler.obtainMessage();
			// msg.obj = ImageUtils.BoxBlurFilter(a);
			msg.obj = ImageUtilEX.convertBitmap2Drawable(Blur.fastblur(context,
					bitmap, 12));
			;
			handler.sendMessage(msg);
		}
	}

	public static Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (change) {
				if (msg.obj != null) {
					view.setBackgroundDrawable((Drawable) msg.obj);
				}
			}
		};
	};

}
