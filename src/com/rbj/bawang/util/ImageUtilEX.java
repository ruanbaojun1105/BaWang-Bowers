package com.rbj.bawang.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import com.lidroid.xutils.util.LogUtils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 *
 * @author jiuhua.song
 */
public class ImageUtilEX {
	
	/**
	 * 图片的缩放
	 * @param bitmap
	 * @param w
	 * @param h
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidht = ((float) w / width);
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidht, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		return newbmp;
	}

	
	/**
	 * 把drawable转成BITMAP
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
//		Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
//				.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
//				: Bitmap.Config.RGB_565);
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * ID转化成BITMAP
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap drawableToBitmap(Context context,int resId){
		Drawable drawable = context.getResources().getDrawable(resId);
		return drawableToBitmap(drawable);
	}
	
	
	/**
	 * 图片本地保存</br> 1、图片存在不再进行存放 2、图片不存在进行存放
	 * 
	 * @param bitmap
	 * @throws IOException
	 */

	@SuppressLint("SdCardPath")
	public static void saveImage(String imageName, Bitmap bmp) throws IOException {
		String path = "/sdcard/BWfile/" + imageName + ".png";
		File f = new File(path);
		if (!f.isFile())// 要存放的图片不存在
		{
			String dir = path.substring(0, path.lastIndexOf("/"));
			File dirFile = new File(dir);
			if (!dirFile.exists()) {// 目录不存在时，兴建
				boolean isMakeDir = dirFile.mkdirs();
				if (!isMakeDir) {// 兴建目录失败
					return;
				}
			}

			f.createNewFile();

			FileOutputStream fOut = null;
			try {
				if (bmp!=null) {
					fOut = new FileOutputStream(f);
				bmp.compress(Bitmap.CompressFormat.PNG, 100, fOut);
				LogUtils.e("文件保存成功");
				}else {
					LogUtils.e("文件保存失败");
				}
				
			} catch (FileNotFoundException e) {
			//	e.printStackTrace();
			} finally {
				if (fOut != null) {
					fOut.flush();
					fOut.close();
				}
			}
		}

	}
	public static Bitmap convertDrawable2BitmapSimple(Drawable drawable){
		BitmapDrawable bd = (BitmapDrawable) drawable;
		return bd.getBitmap();
	}
	

	public static Drawable convertBitmap2Drawable(Bitmap bitmap) {
		BitmapDrawable bd = new BitmapDrawable(bitmap);
		// 因为BtimapDrawable是Drawable的子类，最终直接使用bd对象即可。
		return bd;
	} 
	
	/**
	 * 获取对应名称的图片
	 * 
	 * @param path
	 *            缓存的路径
	 * @return
	 */
	public static Bitmap getImage(String imageName) throws Exception {
		Bitmap bit = null;
		try {
			String path = "/sdcard/oneData/images/" + imageName + ".png";
			File imageFile = new File(path);
			if (imageFile.exists()) {// 当前图片存在
				bit = BitmapFactory.decodeFile(path);
				if (bit!=null) {
					LogUtils.e("读取文件成功");
				}else {
					LogUtils.e("读取文件失败");
					bit=null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		//	throw new Exception();
		} 
		return bit;
	}
	/**
	 * 获取对应名称的图片
	 * 
	 * @param path
	 *            缓存的路径
	 * @return
	 */
	public static Bitmap getRealImage(String imageUrl) throws Exception {
		Bitmap bit = null;
		try {
			String path = imageUrl;
			File imageFile = new File(path);
			if (imageFile.exists()) {// 当前图片存在
				bit = BitmapFactory.decodeFile(path);
				if (bit!=null) {
					LogUtils.e("读取文件成功");
				}else {
					LogUtils.e("读取文件失败");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		//	throw new Exception();
		} 
		return bit;
	}
	/**
	 * 从安装包中读取图片
	 * 
	 * @param context
	 * @param imagePath
	 * @return
	 */
	public static Bitmap getImageFromLocal(Context context, String imageName)throws Exception {
		String imagePath="content/images/"+imageName;
		Bitmap bit = null;
		try {
			AssetManager assets = context.getAssets();
			InputStream is = assets.open(imagePath);
			bit = BitmapFactory.decodeStream(is);

		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return bit;
	}

	// 获取SD卡上所有图片
	public List<Object> getImgFromSDcard() {
		List<Object> fileList = new ArrayList<Object>(); 
		String PATH = "/sdcard/";
		File file = new File("");
		File[] files = file.listFiles();

		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				String filename = files[i].getName();
				// 获取bmp,jpg,png格式的图片
				if (filename.endsWith(".jpg") || filename.endsWith(".png")
						|| filename.endsWith(".bmp")) {
					String filePath = files[i].getAbsolutePath();
					fileList.add(filePath);
				}
			} else if (files[i].isDirectory()) {
				PATH = files[i].getAbsolutePath();
				getImgFromSDcard();
			}
		}
		return fileList;
	}
	
	
	public static Bitmap getRBitmap(int x ,int y ,int w, int h, Bitmap image, float outerRadiusRat){
		Drawable imageDrawable = new BitmapDrawable(image); 
		Bitmap output = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888); 
		Canvas canvas = new Canvas(output); 
		RectF outerRect = new RectF(x, y, w, h); 
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG); 
		canvas.drawRoundRect(outerRect, outerRadiusRat, outerRadiusRat, paint); 
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN)); 
		
		imageDrawable.setBounds(x, y, w, h); 
		canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG); 
		imageDrawable.draw(canvas); 
		canvas.restore(); 
        return output; 
	}
}
