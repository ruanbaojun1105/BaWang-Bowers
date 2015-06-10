package com.bw.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rbj.bawang.util.SyncSet;
import example.webbowers.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.LauncherActivity.ListItem;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
/**
 * 暂不实现
 * @author baojun
 *
 */
public class ShowPicture extends Activity {
	private GridView showPic;
	Handler handler;
	LinearLayout bglLayout;
	List<Map<String, Object>> listItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showpic);
		SyncSet syncLight = new SyncSet();
		// syncLight.syncLight(getApplicationContext());

		showPic = (GridView) findViewById(R.id.show_morepics);
		bglLayout = (LinearLayout) findViewById(R.id.show_morepics_bg);
		//int imageId = R.drawable.test2;
		String[] title = new String[] { "1", "2", "3", "4", "5" };
		listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < title.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			//map.put("image", imageId);
			map.put("title", title[i]);
			listItems.add(map);

		}
		SimpleAdapter adapter = new SimpleAdapter(this, listItems,
				R.layout.gridview_picture_style, new String[] { "title",
						"image" }, new int[] { R.id.ItemText, R.id.ItemImage });
		showPic.setAdapter(adapter);
		showPic.setOnItemClickListener(new GetPicToChange());

	}

	public class GetPicToChange implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Dialog alertDialog = new AlertDialog.Builder(
					getApplicationContext())
					// .setTitle("文件下载提示：")
					.setMessage("確定將此張圖片設為軟件所有界面的背景嗎？")
					.setIcon(R.drawable.about)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

									Message msg = handler.obtainMessage();
									Bundle bundle = new Bundle();
									bundle.putString("add", (String) listItems
											.get(arg2).get("image"));
									msg.setData(bundle);
									handler = new responseChanggePic();
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
								}
							}).create();
			alertDialog.show();
		}
	}

	private class responseChanggePic extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
		}
	}
}
