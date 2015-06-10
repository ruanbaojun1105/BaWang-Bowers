package com.bw.adapter;

import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class MyOnPageChangeListener implements OnPageChangeListener {

	private int offset = 0;// ����ͼƬƫ����
	private int currIndex = 0;// 
	private int bmpW;// 
	int one = offset * 2 + bmpW;// ҳ��1 -> ҳ��2 ƫ����
	int two = one * 2;// ҳ��1 -> ҳ��3 ƫ����

	@Override
	public void onPageSelected(int arg0) {
		Animation animation = null;
		switch (arg0) {
		case 0:
			if (currIndex == 1) {
				animation = new TranslateAnimation(one, 0, 0, 0);
			} else if (currIndex == 2) {
				animation = new TranslateAnimation(two, 0, 0, 0);
			}
			break;
		case 1:
			if (currIndex == 0) {
				animation = new TranslateAnimation(offset, one, 0, 0);
			} else if (currIndex == 2) {
				animation = new TranslateAnimation(two, one, 0, 0);
			}
			break;
		case 2:
			if (currIndex == 0) {
				animation = new TranslateAnimation(offset, two, 0, 0);
			} else if (currIndex == 1) {
				animation = new TranslateAnimation(one, two, 0, 0);
			}
			break;
		}
		currIndex = arg0;
		// animation.setFillAfter(true);// True:ͼƬͣ�ڶ�������λ��
		// animation.setDuration(300);
		// cursor.startAnimation(animation);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}
	
}
