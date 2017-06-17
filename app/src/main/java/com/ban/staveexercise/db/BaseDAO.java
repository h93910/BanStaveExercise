package com.ban.staveexercise.db;

import com.ban.staveexercise.base.ComBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public abstract class BaseDAO<T> extends SQLiteOpenHelper {
	public static SQLiteDatabase database;
	protected String tableName;

	public BaseDAO(Context context) {
		super(context, ComBase.DATABASE_FILE_PATH, null, 1);
		if (database == null) {
			database = getReadableDatabase();
		}
	}

	public BaseDAO(Context context, String tableName) {
		super(context, ComBase.DATABASE_FILE_PATH, null, 1);
		if (database == null) {
			database = getReadableDatabase();
		}
		this.tableName = tableName;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	/**
	 * 插入一条新记录
	 * 
	 * @param valueName
	 *            列名
	 * @param value
	 *            属性
	 * @return 操作是否成功
	 */
	public boolean insertNew(String[] valueName, String[] value) {
		if (valueName.length != value.length) {
			Log.e("ban", "insert 数据名与值数量不一");
			return false;
		}
		ContentValues contentValues = new ContentValues();
		for (int i = 0; i < valueName.length; i++) {
			contentValues.put(valueName[i], value[i]);
		}
		if (database.insert(tableName, null, contentValues) == -1) {
			Log.e("ban", tableName + "插入数据失败");
			return false;
		}

		return true;
	}

	/**
	 * 删除一条数据
	 * 
	 * @param valueName
	 *            条件名
	 * @param value
	 *            条件值
	 * @return 操作是否成功
	 */
	public boolean delete(String[] valueName, String value[]) {
		if (valueName.length != value.length) {
			Log.e("ban", "delete 数据名与值数量不一");
			return false;
		}
		String whereClause = "";
		for (int i = 0; i < valueName.length; i++) {
			if (i != 0) {
				whereClause += " and ";
			}
			whereClause += valueName + "=?";
		}

		if (database.delete(tableName, whereClause, value) == -1) {
			Log.e("ban", tableName + "删除数据失败");
			return false;
		}

		return true;
	}

	/**
	 * 更新数据
	 * 
	 * @param valueName
	 *            要更新数据的列名
	 * @param value
	 *            要更新的数据
	 * @param whereClause
	 *            条件的列名
	 * @param whereArgs
	 *            条件值
	 * @return 操作是否成功
	 */
	public boolean update(String[] valueName, String value[],
			String whereClause, String[] whereArgs) {
		if (valueName.length != value.length) {
			Log.e("ban", "update 数据名与值数量不一");
			return false;
		}
		ContentValues contentValues = new ContentValues();
		for (int i = 0; i < valueName.length; i++) {
			contentValues.put(valueName[i], value[i]);
		}
		if (database.update(tableName, contentValues, whereClause, whereArgs) == -1) {
			Log.e("ban", tableName + "更新数据失败");
			return false;
		}

		return true;
	}
	
	public abstract T getFromCursor(Cursor cursor);
}
