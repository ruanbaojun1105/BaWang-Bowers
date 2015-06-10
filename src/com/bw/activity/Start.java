package com.bw.activity;

import example.webbowers.R;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Start extends Activity {

	private boolean addHistory = true;
	TextView alpha_star, alpha_star2;
	private Intent intentServer = new Intent("ResorceServer.servers");
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
		startService(intentServer);// 启动服务

		
		overridePendingTransition(R.anim.base_slide_right_in, R.anim.anim_alpha);
		//RelativeLayout layout = (RelativeLayout) findViewById(R.id.linear);
		alpha_star = (TextView) findViewById(R.id.alph_star1);
		alpha_star2 = (TextView) findViewById(R.id.alph_star2);
		alpha_star.setVisibility(View.GONE);
		alpha_star2.setVisibility(View.GONE);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// Message m=new Message();
				try {
					Thread.sleep(1300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// m.what=0x111;
				// mHandler.sendMessage(m);
				alpha_star.post(new Runnable() {

					@Override
					public void run() {
						alpha_star.setVisibility(View.VISIBLE);
					}
				});

			}

		}).start();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// Message m=new Message();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// m.what=0x111;
				// mHandler.sendMessage(m);
				alpha_star2.post(new Runnable() {

					@Override
					public void run() {
						alpha_star2.setVisibility(View.VISIBLE);
					}
				});

			}

		}).start();
//		Random random = new Random();
//		Animation alpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
////		int a[] = { R.drawable.center, R.drawable.title, R.drawable.center2 };
////		InputStream is1 = this.getResources().openRawResource(
////				a[random.nextInt(3)]);
//		InputStream is1 = this.getResources().openRawResource(R.drawable.center2);
//		BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inJustDecodeBounds = false;
//		options.inSampleSize = 1; // width，hight设为原来的十分一
//		Bitmap btp1 = BitmapFactory.decodeStream(is1, null, options);
//		@SuppressWarnings("deprecation")
//		Drawable drawable1 = new BitmapDrawable(btp1);
		//layout.setBackground(drawable1);
		//layout.startAnimation(alpha);

		final Animation rotate = AnimationUtils.loadAnimation(this,
				R.anim.rotate);
		ImageView imageView = (ImageView) findViewById(R.id.imageView1);
		imageView.startAnimation(rotate);
		SharedPreferences sp = Start.this.getSharedPreferences("sava_weburl",
				MODE_WORLD_READABLE);
		SharedPreferences.Editor et = sp.edit();
		et.putString("sava_weburl", "http://m.hao123.com");
		et.commit();
		SharedPreferences sp1 = Start.this.getSharedPreferences("addHistory",
				MODE_PRIVATE);
		SharedPreferences.Editor et1 = sp1.edit();
		et1.putBoolean("addHistory", addHistory);

		String aaa = " 新年快乐, 祝福短信,happy new year,一生所爱,www.qq.com,www.taobao.com";
		SharedPreferences.Editor re = sp1.edit();
		for (char c = '!'; c <= '|'; c++) {
			char b = c;
			String cc = b + ",";
			aaa += cc;
			System.out.print(cc + " ");
		}
		re.putString("hotWord", aaa);
		re.commit();
		init();

	}

	protected void init() {
		// 延迟3200毫秒发送消息到消息队列
		handler1.sendEmptyMessageDelayed(0, 3200);
	}

	public void MyBinders() {
		// TODO Auto-generated method stub

	}

	@SuppressLint("HandlerLeak")
	Handler handler1 = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 跳转到登录界面,并默认方式关闭当前界面
			Intent intent = new Intent(Start.this, Home.class);
			// intent.setAction("android.intent.action.VIEW");
			// intent.addCategory("android.intent.category.DEFAULT");
			startActivity(intent);
			Start.this.finish();
			// overridePendingTransition(R.anim.base_slide_right_in,
			// R.anim.anim_alpha);

		}
	};

}
