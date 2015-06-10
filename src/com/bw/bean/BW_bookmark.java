package com.bw.bean;

public class BW_bookmark {

	private int _id;// 缓存网址编号
	private String flag;// 网页名称
	private String weburl;// 网址
	private String time;// 网址

	public BW_bookmark()// 默认构造函数
	{
		super();
	}

	// 定义有参构造函数，用来初始化网址信息实体类中的各个字段
	public BW_bookmark(int id, String flag, String weburl, String time) {
		super();
		this._id = id;// 为缓存网址编号赋值
		this.flag = flag;// 为网页名称赋值
		this.weburl = weburl;// 为网址赋值
		this.time = time;// 记录时间
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getid() {
		return _id;
	}

	public void setid(int id) {
		this._id = id;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getWeburl() {
		return weburl;
	}

	public void setWeburl(String weburl) {
		this.weburl = weburl;
	}

}
