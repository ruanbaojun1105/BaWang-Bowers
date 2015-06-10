package com.bw.database;

import java.util.ArrayList;
import java.util.List;

import com.bw.bean.BW_history;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class HistoryDAO {
	private DBOpenHelper helper;// 创建DBOpenHelper对象
	private SQLiteDatabase db;// 创建SQLiteDatabase对象

	public HistoryDAO(Context context)// 定义构造函数
	{
		helper = new DBOpenHelper(context);// 初始化DBOpenHelper对象
	}

	/*
	*//**
	 * 添加便签信息
	 * 
	 * @param bw_history
	 */
	/*
	 * public void add(bw_history bw_history) { db =
	 * helper.getWritableDatabase();// 初始化SQLiteDatabase对象
	 * db.execSQL("insert into bw_history (_id,flag,weburl) values (?,?,?)", new
	 * Object[] { bw_history.getid(), bw_history.getFlag(),
	 * bw_history.getWeburl() });// 执行添加便签信息操作 }
	 */
	/**
	 * 添加便签信息
	 * 
	 * @param bw_history
	 */
	public void add(BW_history bw_history) {
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		db.execSQL("insert into bw_history (flag,weburl) values (?,?)",
				new Object[] {
						// bw_history.getid(),
						bw_history.getFlag(), bw_history.getWeburl() });// 执行添加便签信息操作
	}

	/**
	 * 更新便签信息
	 * 
	 * @param bw_history
	 */
	public void update(BW_history bw_history) {
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		db.execSQL("update bw_history set flag = ?, weburl = ? where _id = ?",
				new Object[] { bw_history.getFlag(), bw_history.getWeburl(),
						bw_history.getid() });// 执行修改便签信息操作
	}

	/**
	 * 查找便签信息
	 * 
	 * @param id
	 * @return
	 */
	public BW_history find(int id) {
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		Cursor cursor = db.rawQuery(
				"select _id,flag,weburl from bw_history where _id = ?",
				new String[] { String.valueOf(id) });// 根据编号查找便签信息，并存储到Cursor类中
		if (cursor.moveToNext())// 遍历查找到的便签信息
		{
			// 将遍历到的便签信息存储到bw_history类中
			return new BW_history(cursor.getInt(cursor.getColumnIndex("_id")),
					cursor.getString(cursor.getColumnIndex("flag")),
					cursor.getString(cursor.getColumnIndex("weburl")));
		}
		return null;// 如果没有信息，则返回null
	}

	/**
	 * 刪除便签信息
	 * 
	 * @param ids
	 */
	public void detele(Integer... ids) {
		if (ids.length > 0)// 判断是否存在要删除的id
		{
			StringBuffer sb = new StringBuffer();// 创建StringBuffer对象
			for (int i = 0; i < ids.length; i++)// 遍历要删除的id集合
			{
				sb.append('?').append(',');// 将删除条件添加到StringBuffer对象中
			}
			sb.deleteCharAt(sb.length() - 1);// 去掉最后一个“,“字符
			db = helper.getWritableDatabase();// 创建SQLiteDatabase对象
			// 执行删除便签信息操作
			db.execSQL("delete from bw_history where _id in (" + sb + ")",
					(Object[]) ids);
		}
	}

	/**
	 * 获取便签信息
	 * 
	 * @param start
	 *            起始位置
	 * @param count
	 *            每页显示数量
	 * @return
	 */
	public List<BW_history> getScrollData(int start, int count) {
		List<BW_history> lisbw_historys = new ArrayList<BW_history>();// 创建集合对象
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		// 获取所有便签信息
		Cursor cursor = db.rawQuery("select * from bw_history limit ?,?",
				new String[] { String.valueOf(start), String.valueOf(count) });
		while (cursor.moveToNext())// 遍历所有的便签信息
		{
			// 将遍历到的便签信息添加到集合中
			lisbw_historys.add(new BW_history(cursor.getInt(cursor
					.getColumnIndex("_id")), cursor.getString(cursor
					.getColumnIndex("flag")), cursor.getString(cursor
					.getColumnIndex("weburl"))));
		}
		return lisbw_historys;// 返回集合
	}

	/**
	 * 获取总记录数
	 * 
	 * @return
	 */
	public int getCount() {
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		Cursor cursor = db.rawQuery("select count(_id) from bw_history", null);// 获取便签信息的记录数
		if (cursor.moveToNext())// 判断Cursor中是否有数据
		{
			return cursor.getInt(0);// 返回总记录数
		}
		return 0;// 如果没有数据，则返回0
	}

	/**
	 * 获取便签最大编号
	 * 
	 * @return
	 */
	public int getMaxId() {
		db = helper.getWritableDatabase();// 初始化SQLiteDatabase对象
		Cursor cursor = db.rawQuery("select max(_id) from bw_history", null);// 获取便签信息表中的最大编号
		while (cursor.moveToLast()) {// 访问Cursor中的最后一条数据
			return cursor.getInt(0);// 获取访问到的数据，即最大编号
		}
		return 0;// 如果没有数据，则返回0
	}
	/**
	 * 关闭数据库
	 */
	public void closeSQL() {
		db = helper.getWritableDatabase();
		db.close();
	}
}
