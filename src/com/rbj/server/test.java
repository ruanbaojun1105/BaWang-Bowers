package com.rbj.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s1 = "/a/b/a/";
		String[] str;
		str = s1.split("/");
		String s2 = "";
		for (int i = 0; i < str.length; i++) {
			s2 += (str[i] + "/");
		}

		System.out.println(s2 + str.length);

		try {

			// 百度图标的地址

			URL url = new URL("http://www.baidu.com/img/baidu_sylogo1.gif");

			InputStream inputStream = url.openConnection().getInputStream();

			FileOutputStream outputStream = new FileOutputStream(new File(
					"D:\\baidu.gif"));

			byte[] buffer = new byte[1024];

			int num = 0;

			while ((num = inputStream.read(buffer)) != -1)

			{

				outputStream.write(buffer, 0, num);

				outputStream.flush();

			}

			inputStream.close();

			outputStream.close();

		} catch (Exception e) {

			e.printStackTrace();

		}
	}
}
