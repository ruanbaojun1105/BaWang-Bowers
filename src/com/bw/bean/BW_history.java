package com.bw.bean;

public class BW_history {
	private int _id;// 缓存网址编号
	private String flag;// 网页名称
	private String weburl;// 网址

	// 定义有参构造函数，用来初始化网址信息实体类中的各个字段
	public BW_history(int id, String flag, String string2) {
		super();
		this._id = id;// 为缓存网址编号赋值
		this.flag = flag;// 为网页名称赋值
		this.weburl = string2;// 为网址赋值
	}

	public BW_history()// 默认构造函数
	{
		super();
	}

	public String getWeburl() {
		return weburl;
	}

	public void setWeburl(String weburl) {
		this.weburl = weburl;
	}

	public int getid()// 设置缓存网址编号的可读属性
	{
		return _id;
	}

	public void setid(int id)// 设置缓存网址编号的可写属性
	{
		this._id = id;
	}

	public String getFlag()// 设置网址信息的可读属性
	{
		return flag;
	}

	public void setFlag(String flag)// 设置网址信息的可写属性
	{
		this.flag = flag;
	}
}
