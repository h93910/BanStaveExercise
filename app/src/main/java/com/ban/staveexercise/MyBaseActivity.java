package com.ban.staveexercise;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;

/**
 * 基础Activity
 * 
 * @author Ban
 * 
 */
public abstract class MyBaseActivity extends Activity {
	protected Activity activity = this;
	private Builder alertDialog;// 对话框
	private ProgressDialog progressDialog;// 进度框

	private OnClickListener positiveButtonListener = null;
	private OnClickListener negativeButtonListener = null;
	private MyRun myRun;

	private int okBtnTextRec = -1;
	private int cancelBtnTextRec = -1;

	private Handler handler = new Handler() {
		@SuppressWarnings("deprecation")
		@Override
		public void handleMessage(Message msg) {
			alertDialog = new AlertDialog.Builder(MyBaseActivity.this);
			switch (msg.what) {
			case 0:
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				break;
			case 1:
				if (null != progressDialog && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				progressDialog = new ProgressDialog(activity);
				progressDialog.setMessage(msg.obj.toString());
				if (msg.arg1 == 1) {
					progressDialog.setButton(
							getResources().getString(R.string.Sys_Cancel),
							new OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Message.obtain(handler, 0).sendToTarget();
								}
							});
					progressDialog.setCancelable(true);
				} else {
					progressDialog.setCancelable(false);
				}
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.setIndeterminate(false);
				progressDialog.show();
				break;
			case 2:
				OnClickListener[] a = (OnClickListener[]) msg.obj;
				if (a != null && a.length == 1) {
					alertDialog.setTitle(R.string.Sys_Warning)
							.setMessage(msg.arg1)
							.setPositiveButton(R.string.Sys_Yes, a[0]).show();
				} else {
					alertDialog.setTitle(R.string.Sys_Warning)
							.setMessage(msg.arg1)
							.setPositiveButton(R.string.Sys_Yes, null).show();
				}

				break;
			case 3:
				alertDialog.setTitle(R.string.Sys_Warning)
						.setMessage(msg.obj.toString())
						.setPositiveButton(R.string.Sys_Yes, null).show();

				break;
			case 4:
				OnClickListener[] b = (OnClickListener[]) msg.obj;

				alertDialog.setTitle(R.string.Sys_Warning).setMessage(msg.arg1)
						.setPositiveButton(R.string.Sys_Yes, b[0])
						.setNegativeButton(R.string.Sys_Cancel, null).show();

				break;
			case 5:
				alertDialog
						.setTitle(R.string.Sys_Warning)
						.setMessage(msg.obj.toString())
						.setPositiveButton(
								okBtnTextRec == -1 ? R.string.Sys_Yes
										: okBtnTextRec, positiveButtonListener)
						.setNegativeButton(
								cancelBtnTextRec == -1 ? R.string.Sys_Cancel
										: cancelBtnTextRec,
								negativeButtonListener).show();

				break;
			case 6:
				OnClickListener[] c = (OnClickListener[]) msg.obj;

				if (c != null && c.length == 1) {
					alertDialog.setTitle(msg.arg2).setMessage(msg.arg1)
							.setPositiveButton(R.string.Sys_Yes, c[0]).show();
				} else {
					alertDialog.setTitle(msg.arg2).setMessage(msg.arg1)
							.setPositiveButton(R.string.Sys_Yes, null).show();
				}

				break;
			case 7:
				if (myRun != null) {
					myRun.run();
				}

				break;
			case 8:
				alertDialog
						.setTitle(msg.arg2)
						.setMessage(msg.arg1)
						.setPositiveButton(R.string.Sys_Yes,
								positiveButtonListener)
						.setNegativeButton(R.string.Sys_Cancel,
								negativeButtonListener).setView((View) msg.obj)
						.show();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		alertDialog = new AlertDialog.Builder(this);
	}

	protected void onCreate(Bundle savedInstanceState, int layoutId) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setContentView(layoutId);
		alertDialog = new AlertDialog.Builder(this);
		inti();
	}

	/**
	 * 隐藏进度条框
	 */
	public void dismissProgressDialog() {
		Message.obtain(handler, 0).sendToTarget();
	}

	/**
	 * 显示进度条框
	 * 
	 * @param message
	 *            文本内容
	 * @param isShowCannelButton
	 *            是否可取消
	 */
	public void showProgressDialog(String message, boolean isShowCannelButton) {
		int showCannelButton = 0;
		if (isShowCannelButton) {
			showCannelButton = 1;
		}
		Message.obtain(handler, 1, showCannelButton, 0, message).sendToTarget();
	}

	/**
	 * 显示提示对话框，两个按钮，有确定事件
	 * 
	 * @param messageResoure
	 *            文本内容资源ID
	 * @param listeners
	 *            可变参数，只响应第一个侦听为确定事件
	 */
	public void showWarningMessage(int messageResoure,
			OnClickListener... listeners) {
		Message.obtain(handler, 2, messageResoure, 0, listeners).sendToTarget();
	}

	/**
	 * 显示提示对话框，只有确定按钮,但无事件
	 * 
	 * @param messageResoure
	 *            文本内容
	 */
	public void showWarningMessage(String messageResoure) {
		Message.obtain(handler, 3, messageResoure).sendToTarget();
	}

	/**
	 * 显示确认对话框，只有确定事件
	 * 
	 * @param messageResoure
	 *            文本内容资源ID
	 * @param listeners
	 *            可变参数，可只加确定按钮的事件，也可再多加取消按钮的事件
	 */
	public void showConfirmMessage(int messageResoure,
			OnClickListener... listeners) {
		Message.obtain(handler, 4, messageResoure, 0, listeners).sendToTarget();
	}

	/**
	 * 显示确认对话框
	 * 
	 * @param messageResoure
	 *            文本内容
	 * @param listeners
	 *            可变参数，可只加确定按钮的事件，也可再多加取消按钮的事件
	 */
	public void showConfirmMessage(String messageResoure,
			OnClickListener... listeners) {
		positiveButtonListener = null;
		negativeButtonListener = null;

		if (listeners.length != 0) {
			positiveButtonListener = listeners[0];
			if (listeners.length > 1) {
				negativeButtonListener = listeners[1];
			}
		}
		Message.obtain(handler, 5, messageResoure).sendToTarget();
	}

	/**
	 * 显示自定标题的对话框
	 * 
	 * @param messageResoure
	 *            文本内容的资源ID
	 * @param title
	 *            标题内容的资源ID
	 * @param listeners
	 *            可变参数，只可加确定按钮的事件，否定不响应
	 */
	public void showTitleMessage(int messageResoure, int title,
			OnClickListener... listeners) {
		Message.obtain(handler, 6, messageResoure, title, listeners)
				.sendToTarget();
	}

	/**
	 * 显示确认对话框，可自定 确定 和 取消按钮
	 * 
	 * @param messageResoure
	 *            文本内容的资源ID
	 * @param okBtnTextRec
	 *            确定 按钮的文本资源ID
	 * @param cancelBtnTextRec
	 *            取消 按钮的文本资源ID
	 * @param listeners
	 *            可变参数，可只加确定按钮的事件，也可再多加取消按钮的事件
	 */
	public void showConfirmMessage(int messageResoure, int okBtnTextRec,
			int cancelBtnTextRec, OnClickListener... listeners) {
		this.okBtnTextRec = okBtnTextRec;
		this.cancelBtnTextRec = cancelBtnTextRec;
		showConfirmMessage(getString(messageResoure), listeners);
	}

	/**
	 * 有关主线程UI更新的操作
	 * 
	 * @param myRun
	 */
	public void run(MyRun myRun) {
		this.myRun = myRun;
		Message.obtain(handler, 7).sendToTarget();
	}

	/**
	 * 显示自定内容的对话框
	 * 
	 * @param messageResoure
	 *            文本内容的资源ID
	 * @param title
	 *            标题内容的资源ID
	 * @param view
	 *            自定义内容view
	 * @param listeners
	 *            可变参数，只可加确定按钮的事件，否定不响应
	 */
	public void showCustomDialog(int messageResoure, int title, View view,
			OnClickListener... listeners) {
		positiveButtonListener = null;
		negativeButtonListener = null;

		if (listeners.length != 0) {
			positiveButtonListener = listeners[0];
			if (listeners.length > 1) {
				negativeButtonListener = listeners[1];
			}
		}
		Message.obtain(handler, 8, messageResoure, title, view).sendToTarget();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (progressDialog != null) {
			progressDialog.cancel();
		}
	}

	public void inti() {
		// if (findViewById(R.id.back) != null) {
		// android.view.View.OnClickListener listener = new
		// android.view.View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// int id = v.getId();
		// if (id == R.id.back) {
		// finish();
		// } else {
		// }
		// }
		// };
		// findViewById(R.id.back).setOnClickListener(listener);
		// }

		initView();
		initButton();
		initListView();
	}

	public abstract void initView();

	public abstract void initButton();

	public abstract void initListView();

	/**
	 * 主线程操作接口
	 * 
	 * @author Ban
	 * 
	 */
	public interface MyRun {
		void run();
	}

}
