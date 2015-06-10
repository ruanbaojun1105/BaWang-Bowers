package com.bw.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import example.webbowers.R;
import com.bw.activity.MainView;
import com.rbj.bawang.util.ConnUrlHandler;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;

@SuppressLint("NewApi")
public class Tab1 extends Fragment {
	private View tab1;
	private GridView gridView;
	private ArrayList<HashMap<String, Object>> itemList;
	private SimpleAdapter adapter;
	private String[] a = new String[] { "google", "百度", "腾讯", "淘宝", "雅虎",
			"豌豆荚", "人人", "搜狐", "RSS", "书哈哈", "导航" };
	private String[] c = new String[] { "http://www.google.com.hk",
			"http://www.baidu.com", "http://info.3g.qq.com",
			"http://www.taobao.com", "http://www.yahoo.com",
			"http://www.wandoujia.com", "http://www.renren.com",
			"http://www.sohu.com", "http://www.163.com/rss",
			"http://www.shuhaha.com/Book/ShowBookList.aspx",
			"file:///android_asset/testpage.htm" };
	private int[] b = new int[] { R.drawable.google, R.drawable.baidu,
			R.drawable.qq, R.drawable.taobao, R.drawable.yahoo,
			R.drawable.wandoujia, R.drawable.renren, R.drawable.sohu,
			R.drawable.rss, R.drawable.shuhaha, R.drawable.daohang };
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		tab1= inflater.inflate(R.layout.tab1, container,false);
		init();
		return tab1;
		
	}
	/**
	 * 初始化gridview
	 */
	private void init() {
		// TODO Auto-generated method stub
		gridView=(GridView) tab1.findViewById(R.id.index_girdview);
		itemList = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < a.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", a[i]);
			map.put("image", b[i]);
			map.put("url", c[i]);
			itemList.add(map);
		}
		adapter = new SimpleAdapter(getActivity(), itemList,
				R.layout.index_item_style, new String[] { "image", "name" },
				new int[] { R.id.ItemImage, R.id.ItemText });
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.e("toast:", "touched");
				startActivity(new Intent(getActivity(), MainView.class)
						.putExtra("urlA", itemList.get(position).get("url").toString()));
				// 打开连接
				//getActivity().finish();
				getActivity().overridePendingTransition(R.anim.base_slide_right_in,
						R.anim.anim_alpha);
			}
		});
	}

}
