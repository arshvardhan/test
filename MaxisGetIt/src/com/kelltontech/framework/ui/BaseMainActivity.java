package com.kelltontech.framework.ui;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public abstract class BaseMainActivity extends FragmentActivity implements IScreen, OnClickListener, IActionController, OnKeyListener {
	protected ProgressDialog mProgressDialog;
	protected String mProgressDialogBodyText = "Loading...";
	protected Dialog mDialog = null;
	protected AlertDialog.Builder nativeDialog = null;
	protected EditText mSearchTextBox;
	protected boolean mProductBySearch;
	protected long mHitTime;

	protected Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if( ! isFinishing() ) {
				try {
					updateUI(msg);
				} catch(Exception e ) {
					e.printStackTrace();
				}
			}
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// SEARCH EDIT TEXT

	}

	protected void onLoad() {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater mInflater = getMenuInflater();
		return super.onCreateOptionsMenu(menu);
	}

	/*************************** Spinner Code Start Here **********************************/

	public void startSppiner(View view, String titleTxt, String bodyText, boolean isCancelable) {
		if (null == mProgressDialog) {
			mProgressDialog = ProgressDialog.show(this, titleTxt, bodyText, true);
			mProgressDialog.setOnKeyListener(this);
			mProgressDialog.setCancelable(isCancelable);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL | ProgressDialog.STYLE_SPINNER);
			if (null != view)
				mProgressDialog.setView(view);
		}
		if (!mProgressDialog.isShowing())
			mProgressDialog.show();
	}

	public void startSppiner(String titleTxt, String bodyText, boolean isCancelable) {
		startSppiner(null, titleTxt, bodyText, isCancelable);
	}

	public void startSppiner(boolean isCancelable) {
		startSppiner("", mProgressDialogBodyText, isCancelable);
	}

	public void startSppiner() {
		startSppiner(false);
	}

	public void stopSppiner() {
		if (null != mProgressDialog && mProgressDialog.isShowing())
			mProgressDialog.dismiss();
	}

	public void hideSppiner() {
		if (null != mProgressDialog && mProgressDialog.isShowing())
			mProgressDialog.hide();
	}

	public void showSppiner() {
		if (null != mProgressDialog)
			mProgressDialog.show();
		else if (mProgressDialog == null)
			startSppiner(true);
	}

	public void setProgressDialogBodyText(String progressDialogBodyText) {
		mProgressDialogBodyText = progressDialogBodyText;
	}

	/*************************** Spinner Code End Here **********************************/

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		// Checks the orientation of the screen
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			// Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
		}
		// Checks whether a hardware keyboard is available
		if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
			// Toast.makeText(this, "keyboard visible", //
			// Toast.LENGTH_SHORT).show();
		} else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
			// Toast.makeText(this, "keyboard hidden",//
			// Toast.LENGTH_SHORT).show();
		}
	}

	private static Toast mSingletonToast;

	/**
	 * This method show the pMessage as a immediate Toast for specified
	 * pDuration.
	 * 
	 * @param pMessage
	 *            - If it is null or blank, Toast will not be shown.
	 * @param pDuration
	 *            - Default value is Toast.LENGTH_LONG
	 */
	public void showToast(String pMessage, int pDuration) {
		if (StringUtil.isNullOrEmpty(pMessage))
			return;

		if (pDuration != Toast.LENGTH_LONG && pDuration != Toast.LENGTH_SHORT) {
			pDuration = Toast.LENGTH_LONG;
		}
		pDuration = Toast.LENGTH_LONG;
		if (mSingletonToast == null) {
			mSingletonToast = Toast.makeText(getApplicationContext(), pMessage, pDuration);
			// float density = getResources().getDisplayMetrics().density;
			mSingletonToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
		}
		mSingletonToast.setDuration(pDuration);
		mSingletonToast.setGravity(Gravity.CENTER, 0, 0);
		mSingletonToast.setText(pMessage);
		mSingletonToast.show();
	}

	public void showToast(String pMessage) {
		showToast(pMessage, 0);
	}

	public void showToast(int resID, int pDuration) {
		showToast(getResources().getString(resID), pDuration);
	}

	@Override
	public void updateUI(android.os.Message msg) {
	};

	@Override
	public void onBackPressed() {
		AnalyticsHelper.logEvent(FlurryEventsConstants.BACK_CLICK);
		super.onBackPressed();
	}

	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		return false;
	}

	public void onPositiveDialogButton(int id) {
		// TODO Auto-generated method stub
		
	}

	public void onNegativeDialogbutton(int id) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * @param url
	 */
	protected void openDeviceBrowser(String url, boolean withChooser) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		if( withChooser ) {
			startActivity(Intent.createChooser(browserIntent, getString(R.string.open_browser)));
		} else {
			startActivity(browserIntent);
		}
	}
}
