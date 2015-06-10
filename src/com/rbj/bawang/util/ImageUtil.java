package com.rbj.bawang.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

public class ImageUtil {

	// 图片转码

	// 判断文件类型
	public static  int getWith(String path) {
		if (path.endsWith(".jpg")) {
			return 1;
		}
		if (path.endsWith(".png")) {
			return 2;
		}
		if (path.endsWith(".gif")) {
			return 3;
		}
		return -1;
	}
	
	
	// 路径转位图
		public static Bitmap getBitmap(String path) {
			if (path != null) {
				Bitmap b = BitmapFactory.decodeFile(path);
				return b;
			}
			return null;
		}
	
	
	//缩放图片
	public  static Bitmap getzoom(Bitmap b,ImageView iv) {
		int width = b.getWidth();
		int height = b.getHeight();
		int bi;
		int h = iv.getHeight() - 10;
		int w;
		// 宽高比以
		if (b.getHeight() > b.getWidth()) {
			bi = b.getHeight() / b.getWidth();
			w = h / bi;
		} else {
			bi = b.getWidth() / b.getHeight();
			w = h * bi;
		}

		return zoomBitmap(b, w, h);
	}
	
	/**
	 * 图片缩放
	 * */
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

	/** 图片压缩 */

	public  static Bitmap getimage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	public static Bitmap getimage(byte[] bytes) {
//      BitmapFactory.Options newOpts = new BitmapFactory.Options();
//      //开始读入图片，此时把options.inJustDecodeBounds 设回true了
//      newOpts.inJustDecodeBounds = true;
//      Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length,newOpts);//此时返回bm为空
////      bitmap = zoomBitmap(bitmap, IMG_WIDTH, IMG_WIDTH);
//      newOpts.inJustDecodeBounds = false;
//      int w = newOpts.outWidth;
//      int h = newOpts.outHeight;
//      
//     
//      newOpts.inSampleSize = 1;//设置缩放比例
////      //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
//      bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length,newOpts);//此时返回bm为空
//      
//      // 设置想要的大小
//      float newWidth = IMG_WIDTH;
//
//      float newHeight = IMG_WIDTH;
//
//      // 计算缩放比例
//      float scaleWidth = ((float) newWidth) / newOpts.outWidth;
//      float scaleHeight = ((float) newHeight) / newOpts.outHeight;
//      // 取得想要缩放的matrix参数
//      Matrix matrix = new Matrix();
//      matrix.postScale(scaleWidth, scaleHeight);
//      
//      bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix,
//      	      true);
//      return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
      
      
      
      BitmapFactory.Options newOpts = new BitmapFactory.Options();  
      newOpts.inJustDecodeBounds = true;//只读边,不读内容  
      Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length,newOpts);//此时返回bm为空

      newOpts.inJustDecodeBounds = false;  
      int w = newOpts.outWidth;  
      int h = newOpts.outHeight;  
      float hh = 480f;//  
      float ww = 320f;//  
      int be = 1;  
      if (w > h && w > ww) {  
          be = (int) (newOpts.outWidth / ww);  
      } else if (w < h && h > hh) {  
          be = (int) (newOpts.outHeight / hh);  
      }  
      if (be <= 0)  
          be = 1;  
      newOpts.inSampleSize = be;//设置采样率  
        
      newOpts.inPreferredConfig = Config.ARGB_8888;//该模式是默认的,可不设  
      newOpts.inPurgeable = true;// 同时设置才会有效  
      newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收  
        
      bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length,newOpts);//此时返回bm为空
     
      
//    return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩  
                                  //其实是无效的,大家尽管尝试  
      return compressImage(bitmap);  
  }
	
	public static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 80;
		while ( baos.toByteArray().length / 1024>100) {	//循环判断如果压缩后图片是否大于100kb,大于继续压缩		
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.PNG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;//每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
		return bitmap;
	}
	public static boolean compressImage2(Bitmap bitmap) throws Exception {
		try {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		int options = 100;
		int size = baos.toByteArray().length / 1024;
		while (size > 40 && options > 0) {
		baos.reset();// 重置baos即清空baos
		options -= 10;// 每次都减少10
		// 这里压缩options%，把压缩后的数据存放到baos中
		bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
		size = baos.toByteArray().length / 1024;
		}
		// 把压缩后的数据baos存放到ByteArrayInputStream中
		ByteArrayInputStream isBm = new ByteArrayInputStream(
		baos.toByteArray());
		// 把ByteArrayInputStream数据生成图片
		bitmap = BitmapFactory.decodeStream(isBm, null, null);
		if (size > 40) {
		return true;
		} else {
		return false;
		}

		} catch (Exception e) {
		throw e;
		}

		}


	// 位图转base64
	public static String getBase64String(Bitmap b) {
		if (b != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			b.compress(CompressFormat.JPEG, 50, bos);// 参数100表示不压缩
			byte[] bytes = bos.toByteArray();
			return Base64.encodeToString(bytes, Base64.DEFAULT);
		}
		return null;
	}
	
	/**
	 * 获取网络图片
	 */
	public static void getNetImage(String url,View view) {
		
	}
}
