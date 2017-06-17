package com.ban.staveexercise.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 键值对信息的处理类
 * 
 * @author Ban
 * 
 */
public class MySampleDate {
	private static SharedPreferences sharedPreferences;

	private MySampleDate(Context context, String fileName) {
		sharedPreferences = context.getSharedPreferences(fileName,
				Context.MODE_PRIVATE);
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 *            上下文
	 * @param fileName
	 *            设置的文件名
	 */
	public static void getInstance(Context context, String fileName) {
		new MySampleDate(context, fileName);
	}

	/**
	 * 获取字符串信息值
	 * 
	 * @param key
	 *            键
	 * @return 对应的键的值,如不存在,则返回"";
	 */
	public static String getStringValue(String key) {
		return sharedPreferences.getString(key, "");
	}

	/**
	 * 获取整型信息值
	 *
	 * @param key
	 *            键
	 * @return 对应的键的值,如不存在,则返回"";
	 */
	public static int getIntValue(String key) {
		return sharedPreferences.getInt(key,0);
    }
	/**
	 * 获取字符串信息值
	 * 
	 * @param key
	 *            键
	 * @return 对应的键的值,如不存在,则返回"";
	 */
	public static boolean getBooleanValue(String key) {
		return sharedPreferences.getBoolean(key, false);
	}

	/**
	 * 保存键值对,可加入String,Boolean,Integer类型
	 * 
	 * @param key
	 *            键
	 * @param value
	 *            值
	 */
	public static void saveInfo(String key, Object value) {
		Editor editor = sharedPreferences.edit();
		String type = value.getClass().getSimpleName();
		if (type.equals("String")) {
			editor.putString(key, value.toString());
		} else if (type.equals("Boolean")) {
			editor.putBoolean(key, (Boolean) value);
		} else if (type.equals("Integer")) {
			editor.putInt(key, (Integer) value);
		}
		editor.commit();
	}

	/**
	 * 删除保存信息的对应于所传值的键值对
	 * 
	 * @param key
	 *            键
	 */
	public static void deleteListInfo(String key) {
		Editor editor = sharedPreferences.edit();
		editor.remove(key);
		editor.commit();
	}
}
