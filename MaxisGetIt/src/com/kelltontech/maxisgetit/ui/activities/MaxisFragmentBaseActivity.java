package com.kelltontech.maxisgetit.ui.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.ui.IActionController;
import com.kelltontech.framework.ui.IScreen;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.controllers.CombindListingController;
import com.kelltontech.maxisgetit.controllers.UserDetailController;
import com.kelltontech.maxisgetit.dao.GPS_Data;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import com.kelltontech.maxisgetit.requests.CombinedListRequest;
import com.kelltontech.maxisgetit.response.CompanyListResponse;
import com.kelltontech.maxisgetit.response.UserDetailResponse;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public abstract class MaxisFragmentBaseActivity extends FragmentActivity implements IScreen, OnClickListener, IActionController, OnKeyListener {
	protected ProgressDialog mProgressDialog;
	protected String mProgressDialogBodyText = "Loading...";
	protected Dialog mDialog;
	protected EditText mSearchTextBox;
	protected boolean mProductBySearch;
	protected long mHitTime;
	private CompanyListResponse mClResponse;
	private CombinedListRequest mListRequest;
	private MaxisStore mStore;

	protected boolean isLocationAware() {
		return mStore.isLocalized();
	}

	protected Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			updateUI(msg);
		}
	};
	protected void onProfileClick() {
		if (mStore.isLoogedInUser()) {
			UserDetailController controller = new UserDetailController(MaxisFragmentBaseActivity.this, Events.USER_DETAIL);
			controller.requestService(mStore.getUserMobileNumber());
			startSppiner();
		} /*else if (mStore.isRegisteredUser() && !mStore.isVerifiedUser()) {
			Intent intentverify = new Intent(MaxisFragmentBaseActivity.this, VerifyRegistrationActivity.class);
			intentverify.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentverify);
		}*/ else {
			
			Intent intentLogin = new Intent(MaxisFragmentBaseActivity.this, LoginActivity.class);
			intentLogin.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentLogin);
		}
	}
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mStore = MaxisStore.getStore(MaxisFragmentBaseActivity.this);
		// SEARCH EDIT TEXT
		AnalyticsHelper.onActivityCreate();
	}

	protected void onLoad() {

	}
	@Override
	protected void onResume() {
		super.onResume();
		EditText editText = (EditText) findViewById(R.id.search_box);
		InputFilter filter = new InputFilter() {
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
				for (int i = start; i < end; i++) {
					if (!Character.isLetterOrDigit(source.charAt(i)) && source.charAt(i) != '\'' && source.charAt(i) != '_' && source.charAt(i) != '-' && source.charAt(i) != ' ') {
						return "";
					}
				}
				return null;
			}
		};
		if (null != editText) {
			//editText.setFilters(new InputFilter[] { filter });
		}

	}
	
	@Override
	protected void onStart() {
		super.onStart();
		AnalyticsHelper.onActivityStart(this);
		AnalyticsHelper.getReleaseVersion();
		if(!StringUtil.isNullOrEmpty(mStore.getUserMobileNumber())){
		AnalyticsHelper.setUserID(mStore.getUserMobileNumber());
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		AnalyticsHelper.onActivityStop(this);
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
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE) {
			if (msg.arg1 == 1) {
				showToast((String) msg.obj);
			} else {
				Intent intent = new Intent(MaxisFragmentBaseActivity.this, CombindListActivity.class);
				intent.putExtra(AppConstants.COMP_LIST_DATA, mClResponse);
				intent.putExtra(AppConstants.DATA_LIST_REQUEST, mListRequest);
				startActivity(intent);
			}
			stopSppiner();
		}else if (msg.arg2 == Events.USER_DETAIL) {
			if (msg.arg1 == 1) {
				showToast((String) msg.obj);
			} else {
				Intent intent = new Intent(MaxisFragmentBaseActivity.this, MyAccountActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.putExtra(AppConstants.USER_DETAIL_DATA, (UserDetailResponse)msg.obj);
				startActivity(intent);
			}
			stopSppiner();
		}
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if(event==Events.USER_DETAIL){
			handler.sendMessage((Message) screenData);
			return;
		}
		System.out.println(screenData);
		Response response = (Response) screenData;
		Message message = new Message();
		message.arg2 = event;
		message.arg1 = 1;
		if (response.isError()) {
			message.obj = response.getErrorText() + " " + response.getErrorCode();
		} else {
			if (response.getPayload() instanceof CompanyListResponse) {
				mClResponse = (CompanyListResponse) response.getPayload();
				if (mClResponse.getErrorCode() != 0) {
					message.obj =getResources().getString(R.string.communication_failure); 
//							clResponse.getServerMessage() + " " + clResponse.getErrorCode();
				} else {
					if (mClResponse.getCompanyArrayList().size() < 1) {
						message.obj = new String("No Result Found");
					} else {
						message.arg1 = 0;
						message.obj = mClResponse;
					}
				}
			} else {
				message.obj = new String("Internal Error");
			}
		}
		handler.sendMessage(message);
	}

	protected void showAlertDialog(CharSequence message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(MaxisFragmentBaseActivity.this);
		builder.setMessage(message).setTitle(getResources().getString(R.string.app_name));
		// builder.setPositiveButton("Ok", new OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// mDialog.dismiss();
		// }
		// });
		mDialog = builder.create();
		mDialog.show();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	protected void performSearch(String searchText) {
		if (searchText == null || searchText.trim().equals("")) {
			Toast.makeText(MaxisFragmentBaseActivity.this, getResources().getString(R.string.input_search), Toast.LENGTH_SHORT).show();
			return;
		}
		startSppiner();
		mListRequest = new CombinedListRequest(MaxisFragmentBaseActivity.this);
		mListRequest.setBySearch(true);
		mListRequest.setCompanyListing(true);
		mListRequest.setKeywordOrCategoryId(searchText.trim());
		mListRequest.setLatitude(GPS_Data.getLatitude());
		mListRequest.setLongitude(GPS_Data.getLongitude());
		CombindListingController listingController = new CombindListingController(MaxisFragmentBaseActivity.this, Events.COMBIND_LISTING_NEW_LISTING_PAGE);
		listingController.requestService(mListRequest);
	}

	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		return false;
	}
	public static void displayUrlInBrowser(String url,Context context){
		if (!url.startsWith("http://") && !url.startsWith("https://"))
			   url = "http://" + url;
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		context.startActivity(browserIntent);
	}
	public void onPositiveDialogButton(int dialogButton) {
		// TODO Auto-generated method stub
		
	}
}
