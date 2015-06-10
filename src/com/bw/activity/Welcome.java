package com.bw.activity;

import com.rbj.bawang.util.FeatureAnimationListener;
import example.webbowers.R;
import com.rbj.bawang.util.ObservableScrollView;
import com.rbj.bawang.util.OnScrollChangedListener;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class Welcome extends Activity implements OnGlobalLayoutListener,
		OnScrollChangedListener {
	private ObservableScrollView mScrollView;
	private View mAnimView;
	private int mScrollViewHeight;// 滚动条的高度
	private int mStartAnimateTop;// 动画顶部
	private TextView imageView14;// 上拉到最低端，点击开始旅程进入软件首页

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		SharedPreferences sharedPreferences = this.getSharedPreferences(// 得到
				"share", MODE_PRIVATE);
		boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
		Editor editor = sharedPreferences.edit();
		if (isFirstRun) {
			mScrollView = (ObservableScrollView) this
					.findViewById(R.id.scrollView1);
			mScrollView.getViewTreeObserver().addOnGlobalLayoutListener(this);
			mScrollView.setOnScrollChangedListener(this);

			mAnimView = this.findViewById(R.id.anim1);
			mAnimView.setVisibility(View.INVISIBLE);
			init();
			editor.putBoolean("isFirstRun", false);
			editor.commit();
		} else {
			Welcome.this.finish();
			startActivity(new Intent(Welcome.this, Start.class));

		}

	}

	public void init() {
		imageView14 = (TextView) findViewById(R.id.imageView14);
		imageView14.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), Start.class);
				startActivity(intent);
				Welcome.this.finish();
			}
		});
	}

	@Override
	public void onGlobalLayout() {
		mScrollViewHeight = mScrollView.getHeight();
		mStartAnimateTop = mScrollViewHeight / 3 * 2;
	}

	boolean hasStart = false;

	/**
	 * 滚动改变的处理类
	 */
	@Override
	public void onScrollChanged(int top, int oldTop) {
		int animTop = mAnimView.getTop() - top;

		if (top > oldTop) {
			if (animTop < mStartAnimateTop && !hasStart) {
				Animation anim1 = AnimationUtils.loadAnimation(this,
						R.anim.feature_anim2scale_in);
				anim1.setAnimationListener(new FeatureAnimationListener(
						mAnimView, true));

				mAnimView.startAnimation(anim1);
				hasStart = true;
			}
		} else {
			if (animTop > mStartAnimateTop && hasStart) {
				Animation anim1 = AnimationUtils.loadAnimation(this,
						R.anim.feature_alpha_out);
				anim1.setAnimationListener(new FeatureAnimationListener(
						mAnimView, false));

				mAnimView.startAnimation(anim1);
				hasStart = false;
			}
		}
	}

}
