package com.bw.fragment;

import example.webbowers.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


@SuppressLint("NewApi")
public class Tab3 extends Fragment {
	@ViewInject(R.id.call)
	private ImageView call;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View tab3= inflater.inflate(R.layout.tab3, container,false);
		ViewUtils.inject(this, tab3);
		return tab3;
	}
	
	@OnClick(R.id.call)
	public void OnClick(View view){
		Intent intent = new Intent();
		intent.setAction("android.intent.action.CALL");
		intent.addCategory("android.intent.category.DEFAULT");
		// 指定要拨打的电话号码
		intent.setData(Uri.parse("tel:18046714505"));
		getActivity().startActivity(intent);
	}
}
