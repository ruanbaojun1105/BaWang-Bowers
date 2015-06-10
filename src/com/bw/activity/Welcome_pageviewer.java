package com.bw.activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import example.webbowers.R;

import com.ab.util.AbFileUtil;
import com.ab.util.AbImageUtil;
import com.rbj.bawang.util.AboutDialog;
import com.rbj.bawang.util.ImageUtil;
import com.rbj.bawang.util.ImageUtilEX;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint({ "ResourceAsColor", "NewApi" })
public class Welcome_pageviewer extends Activity implements OnPageChangeListener,OnTouchListener{
	private ViewPager mViewPager;
	private List<View> list;
	public boolean flag = false;
	private LinearLayout pointLLayout;
	private ImageView[] imgs;
	private int count;
	private int currentItem;
	private int lastX = 0;// 获得当前X坐标
	private boolean isChangePath = false;
	private SharedPreferences sharedPreferences;
	private Context context;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager);
		context = this;
		sharedPreferences = this.getSharedPreferences(// 得到
				"share", MODE_PRIVATE);

		boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
		if (isFirstRun) {
			/*Drawable drawable=getResources().getDrawable(R.drawable.test1);
			try {
				ImageUtilEX.saveImage("bg", ImageUtilEX.drawableToBitmap(drawable));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			pointLLayout = (LinearLayout) findViewById(R.id.llayout);
			count = pointLLayout.getChildCount();
			imgs = new ImageView[count];
			for (int i = 0; i < count; i++) {
				imgs[i] = (ImageView) pointLLayout.getChildAt(i);
				imgs[i].setEnabled(true);
				imgs[i].setTag(i);
			}
			imgs[0].setEnabled(false);
			mViewPager = (ViewPager) findViewById(R.id.viewPager);
			mViewPager.setOnPageChangeListener(this);
			mViewPager.setOnTouchListener(this);
			//final LayoutInflater inflater = LayoutInflater
			//		.from(Welcome_pageviewer.this);
			
			initpage();
			
			String directory = Environment.getExternalStorageDirectory() + "/";
			String savePath = directory + "BWBowersFile";
			SharedPreferences spb = getSharedPreferences("down_path", 0);
			isChangePath = spb.getBoolean("isChangePath", false);
			if (!isChangePath) {
				SharedPreferences.Editor et = spb.edit();
				et.putString("down_path", savePath);
				et.commit();
			}
		}
		else {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					
					//intent.setData(Uri.parse("xiazdong://www.xiazdong.com/xia"));
					Intent intent = new Intent(Welcome_pageviewer.this,
							Start.class);
					//intent.setAction("com.bw.action");
					//intent.addCategory("com.bw.category");
					startActivity(intent);
					Welcome_pageviewer.this.finish();
				}
			}, 0);
		}
	}

	@SuppressLint({ "ResourceAsColor", "NewApi" })
	public void initpage() {
		//View view1 = flater.inflate(R.layout.loading, null);
		//View view2 = flater.inflate(R.layout.loading, null);
		//View view3 = flater.inflate(R.layout.loading, null);
		list = new ArrayList<View>();
		TextView t1 = new TextView(context);
		TextView t2 = new TextView(context);
		TextView t3 = new TextView(context);
		t1.setGravity(Gravity.CENTER);
		t1.setTextSize(30);
		t1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		t1.setTextColor(getResources().getColor(R.color.blue));
		t1.setBackgroundColor(getResources().getColor(R.color.green));
		//t2.setGravity(Gravity.CENTER);
		t2.setScrollContainer(true);
		t2.setTextSize(18);
		t2.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		t2.setTextColor(getResources().getColor(R.color.black));
		t2.setBackgroundColor(getResources().getColor(R.color.blue));
		t3.setGravity(Gravity.CENTER);
		t3.setTextSize(40);
		t3.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		t3.setTextColor(getResources().getColor(R.color.littlegreen));
		t3.setBackgroundColor(getResources().getColor(R.color.yellow));
		t1.setText(getString(R.string.app_name)+"\t新版本："+AboutDialog.getVersion(context)+"\n\n\n新的起点\n扬帆起航"
				);
		t2.setText("功能看点如下:"
				+ "\n1.全新界面，在上版本基础上增加了很多亮点·"
				+ "\n2.现在浏览网页时可下拉浏览器来刷新啦~"
				+ "\n3.背景功能全新改版，加入毛玻璃效果"
				+ "\n4.修复清空历史BUG"
				+ "\n5.长按BW小图标可弹出菜单，长按刷新可以复制网址，同时在[书签]、[历史]同样可以长按单项复制网址"
				+ "\n6.更换广告栏位效果，模块重新引用"
				+ "\n7.现在的下载也可以管理了，在菜单中的【下载管理】查看"
				+ "\n8.修复调用图库大图片引起的OOM"
				+ "\n9.重新调整页面跳转的逻辑"
				+ "\n10.去掉了推送和广告，做这个不为啥，做这浏览器初衷是满足用户日常浏览需求，所以一切为了用户，不好的一定要去掉的对吧\nT.T"
				+ "\n11.新增的广告栏位招租~~~你们看着办吧\n");
		
		t3.setText("浏览\n无限大\n用心去触发\n.......");
		list.add(t1);
		list.add(t2);
		list.add(t3);
		mViewPager.setAdapter(pager);
		//iv1.setBackground(getDrawable(R.drawable.lod1));
		//iv2.setBackground(getDrawable(R.drawable.lod2));
		//iv3.setBackground(getDrawable(R.drawable.lod3));
		/*InputStream is1 = this.getResources()
				.openRawResource(R.drawable.lod1);
		InputStream is2 = this.getResources()
				.openRawResource(R.drawable.lod2);
		InputStream is3 = this.getResources()
				.openRawResource(R.drawable.lod3);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inSampleSize = 2; // width，hight设为原来的十分一
		Bitmap btp1 = BitmapFactory.decodeStream(is1, null, options);
		Bitmap btp2 = BitmapFactory.decodeStream(is2, null, options);
		Bitmap btp3 = BitmapFactory.decodeStream(is3, null, options);
		Drawable drawable1 = new BitmapDrawable(btp1);
		Drawable drawable2 = new BitmapDrawable(btp2);
		Drawable drawable3 = new BitmapDrawable(btp3);
		// iv1.setImageBitmap(btp);
		iv1.setBackgroundDrawable(drawable1);
		iv2.setBackgroundDrawable(drawable2);
		iv3.setBackgroundDrawable(drawable3);*/
		/*list.add(iv1);
		list.add(iv2);
		list.add(iv3);
		mViewPager.setAdapter(pager);*/
		/*
		 * if(!btp.isRecycled() ){ btp.recycle(); //回收图片所占的内存
		 * System.gc();//提醒系统及时回收 }
		 */
	}

	private void setcurrentPoint(int position) {
		if (position < 0 || position > count - 1 || currentItem == position) {
			return;
		}
		imgs[currentItem].setEnabled(true);
		imgs[position].setEnabled(false);
		currentItem = position;
	}

	PagerAdapter pager = new PagerAdapter() {

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView(list.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			container.addView(list.get(position));
			return list.get(position);
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.ViewPager.OnPageChangeListener#
	 * onPageScrollStateChanged(int)
	 */
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled
	 * (int, float, int)
	 */
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected
	 * (int)
	 */
	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		setcurrentPoint(arg0);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View,
	 * android.view.MotionEvent)
	 */
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		switch (arg1.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastX = (int) arg1.getX();
			break;

		case MotionEvent.ACTION_UP:
			if (!flag) {
				if ((lastX - arg1.getX() > 100)
						&& (mViewPager.getCurrentItem() == mViewPager
								.getAdapter().getCount() - 1)) {// 从最后一页向右滑动
					new Handler().postDelayed(new Runnable() {
						public void run() {
							Editor editor = sharedPreferences.edit();
							editor.putBoolean("isFirstRun", false);
							editor.commit();
							Intent intent = new Intent(Welcome_pageviewer.this,
									Start.class);
							startActivity(intent);
							overridePendingTransition(R.anim.anim_alpha,
									R.anim.anim_alpha);
							finish();
						};
					}, 0);
				}
			}
		}
		return false;
	}
	

}
