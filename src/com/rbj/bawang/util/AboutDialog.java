package com.rbj.bawang.util;

import java.sql.Date;

import example.webbowers.R;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AboutDialog {
	private AutoScrollView scrollView;
	private TextView aboutnews;
	@SuppressWarnings("unused")
	private Handler handler;
	private String result = "";

	@SuppressWarnings("deprecation")
	public void setDialogView(final Context context) {

		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.about_dialog);
		dialog.getWindow().setBackgroundDrawableResource(R.drawable.btn_shadows2);
		aboutnews = (TextView) dialog.findViewById(R.id.aboutnews);
		Date date = new Date(System.currentTimeMillis());
		result = "\n最后编写时间：20150520---20150605\n" + "现在时间："
				+ date.toLocaleString() + "\n"
				+ "如有意见或建议，请联系QQ邮箱：  1018795480@qq.com/401763159@qq.com\n" + "TEL：18046714505\n"
				+ "软件名：" + context.getResources().getString(R.string.app_name)
				+ "\n版本号：" + getVersion(context)
				+ "\n______________ by baojun\n" + "最新版本更新看点：\n1：全新的ui\n"
				+ "2：新增推送\n" ;
				/*+ "3：文件管理优化，再没异常，也没卡顿，但是不加新功能，没空O(∩_∩)O~\n" 
				+ "4：长按链接会复制到粘贴板，贴心么？！\n"
				+ "5：fix:相同的链接重复添加到历史记录，暂无法修复\n"
				+ "6：增加复制网页地址"
				+ "7：修复全屏功能，全新触发"
				+ "8：没空···了";*/
		// }
		aboutnews.setText(result);

		scrollView = (AutoScrollView) dialog.findViewById(R.id.auto_scrollview);
		scrollView.setScrolled(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
		SharedPreferences pre = context.getSharedPreferences("PRE_NAME", 0);
		SharedPreferences.Editor et = pre.edit();
		et.putString("VERSION", getVersion(context));
		et.commit();
	}
/**
 * 自定义toast
 * @param context
 * @param message
 * @param postivi
 * @param time
 */
	@SuppressLint("ResourceAsColor")
	public static void getToast(Context context, String message, int postivi, int time) {
		Toast t = new Toast(context);
		t.setDuration(time);
		t.setGravity(postivi, 0, 0);
		LinearLayout I = new LinearLayout(context);
		I.setBackgroundResource(R.color.alp_g33);
		ImageView imageView = new ImageView(context);
		imageView.setImageResource(R.drawable.history_title);
		imageView.setPadding(10, 16, 15, 8);
		I.addView(imageView);
		TextView textView = new TextView(context);
		textView.setTextColor(context.getResources().getColor(R.color.blue));
		
		TextPaint paint1 = textView.getPaint();
		paint1.setFakeBoldText(true); // 设置字体为粗体
		textView.setPadding(10, 10, 20, 10);
		textView.setTextSize(12);
		textView.setText(message);
		I.setGravity(Gravity.CENTER);
		I.addView(textView);
		t.setView(I);
		t.show();
	}
/**获取版本信息
 * @param context
 * @return
 */
	public static String getVersion(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "can_not_find_version";
		}
	}
}