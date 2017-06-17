package com.ban.staveexercise.base;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

public class ComBase {
	public static int mode = 0;// 0：测试模式 1：发布模式

	public static String DATABASE_NAME = "ban.db";// 数据库名称

	public static float DPI = 0;

	private static ExecutorService executorService = Executors
			.newFixedThreadPool(10);// 线程池
	public static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static String DATABASE_FILE_PATH;
	public static int SCREEN_HEIGHT;
	public static int SCREEN_WIDTH;

	/**
	 * dp单位转px
	 * 
	 * @param dp
	 * @return px
	 */
	public static int dp2pix(float dp) {
		return (int) (DPI * dp);
	}

	/**
	 * 开一个线程运行事物
	 * 
	 * @param runnable
	 */
	public static void runInThead(Runnable runnable) {
		executorService.execute(runnable);
	}

	/**
	 * 判断网络是否打开
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConn(Context context) {
		boolean bisConnFlag = false;
		ConnectivityManager conManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo network = conManager.getActiveNetworkInfo();
		if (network != null) {
			bisConnFlag = conManager.getActiveNetworkInfo().isAvailable();
		}
		return bisConnFlag;
	}

	/**
	 * 判断wifi网络是否打开
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 判断移动网络是否打开
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	public static void initDB(Context context) {
		try {
			 String DATABASE_PATH = context.getFilesDir().getParentFile()
			 .getPath()
			 + "/databases";
//			String DATABASE_PATH = Environment.getExternalStorageDirectory()
//					.getAbsolutePath() + "/Ban";
			System.out.println(DATABASE_PATH);
			DATABASE_FILE_PATH = DATABASE_PATH + "/" + DATABASE_NAME;
			File dir = new File(DATABASE_PATH);

			if (!dir.exists()) {
				dir.mkdir();
			}
			if (!(new File(DATABASE_FILE_PATH)).exists()) {
				AssetManager a = context.getAssets();
				InputStream is = a.open("ban.db");
				FileOutputStream fos = new FileOutputStream(DATABASE_FILE_PATH);
				byte[] buffer = new byte[8192];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
				Log.i("ban", DATABASE_FILE_PATH + "拷贝成功");
			} else {
				Log.i("ban", DATABASE_FILE_PATH + "已存在，无需拷贝");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("ban", "拷贝数据库出错");
		}
	}
}
