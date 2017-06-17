package com.ban.staveexercise.base;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.ban.staveexercise.db.RecordDAO;

public class MyApplication extends Application {
	private Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();

		init();
	}

	private void init() {
		// 初始化键值对处理类
		MySampleDate.getInstance(context, "setting");

		// 设置分辨率,取屏宽高
		ComBase.DPI = getResources().getDisplayMetrics().density;
		ComBase.SCREEN_WIDTH = context.getResources().getDisplayMetrics().widthPixels;
		ComBase.SCREEN_HEIGHT = context.getResources().getDisplayMetrics().heightPixels;
		Log.d("系统分辨率", ComBase.DPI + "");

		// 初始化数据库
		ComBase.initDB(context);
		new RecordDAO(context);
	}
}
