package com.kelltontech.maxisgetit.ui.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.RenderPriority;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.kelltontech.framework.model.Response;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.controllers.RootCategoryController;
import com.kelltontech.maxisgetit.dao.CategoryGroup;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import com.kelltontech.maxisgetit.response.RootCategoryResponse;
import com.kelltontech.maxisgetit.ui.widgets.CustomDialog;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class TnCActivity extends MaxisMainActivity{

	private WebView mWebview;
	private MaxisStore mStore;
	private Button mBtnAccept;
	private Button mBtnReject;
	private RelativeLayout mErrorLayout;
	protected boolean isErrorOccured = false;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tnc);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		mStore = MaxisStore.getStore(TnCActivity.this);

		mBtnAccept = (Button)findViewById(R.id.atnc_accept);
		mBtnAccept.setOnClickListener(this);

		mBtnReject = (Button)findViewById(R.id.atnc_reject);
		mBtnReject.setOnClickListener(this);

		findViewById(R.id.atnc_try_again).setOnClickListener(this);

		mErrorLayout = (RelativeLayout)findViewById(R.id.atnc_error_layout);

		mWebview = ((WebView)findViewById(R.id.atnc_info));//.loadUrl(AppConstants.TNC_PAGE_URL);
		WebSettings settings = mWebview.getSettings();
		settings.setJavaScriptEnabled(true);
		mWebview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		mWebview.getSettings().setRenderPriority(RenderPriority.HIGH);
		mWebview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		hideNavigationButtons();
		startSppiner();

		mWebview.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return overrideWebViewUrlLoading(url);
			}

			public void onPageFinished(WebView view, String url) {
				Log.i("maxis", "Finished loading URL: " +url);
				stopSppiner();
				if(!isErrorOccured)
				{
					showNavigationButtons();
					showWebView();
				}
				else
				{
					hideWebView();
					hideNavigationButtons();
				}
			}

			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				Log.i("maxis", "Error: " + description);
				showInfoDialog(getResources().getString(R.string.atnc_error_loading_page));
				hideWebView();
				hideNavigationButtons();

				isErrorOccured = true;
				//webview.loadUrl(AppConstants.TNC_PAGE_URL);
			}
		});
		mWebview.loadUrl(AppConstants.TNC_PAGE_URL);
	}

	@Override
	protected void onResume() {
		super.onResume();
		AnalyticsHelper.trackSession(TnCActivity.this, AppConstants.TnC);
	}

	@Override
	public Activity getMyActivityReference() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.atnc_accept:	
			mStore.setTnCStatus(true);
			startSppiner();
			RootCategoryController controller = new RootCategoryController(TnCActivity.this, Events.ROOT_CATEGORY_EVENT);
			controller.requestService(null);
			break;
		case R.id.atnc_reject:
			showExitAndBackDialog(getResources().getString(R.string.atnc_exit_confirmation));
			break;
		case R.id.atnc_try_again:
			showSppiner();
			isErrorOccured = false;
			mWebview.loadUrl(AppConstants.TNC_PAGE_URL);
			/*showNavigationButtons();
        	showWebView();*/

			break;
		default:
			break;
		}

	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		Response response = (Response) screenData;
		Message message = new Message();
		message.arg2 = event;
		message.arg1 = 1;
		if (response.isError()) {
			message.obj = getResources().getString(R.string.communication_failure);
		} else {
			if (response.getPayload() instanceof RootCategoryResponse) {
				RootCategoryResponse categoriesResp = (RootCategoryResponse) response.getPayload();
				if (categoriesResp.isErrorFromServer()) {
					message.obj = getResources().getString(R.string.communication_failure);
				} else {
					if (categoriesResp.getCategories().size() < 1) {
						message.obj = getResources().getString(R.string.communication_failure);
					} else {
						message.arg1 = 0;
						message.obj = categoriesResp;
					}
				}
			} else {
				message.obj = getResources().getString(R.string.communication_failure);
			}
		}
		handler.sendMessage(message);
	}


	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.ROOT_CATEGORY_EVENT) {
			if (msg.arg1 == 1) {
				showFinalDialog((String) msg.obj);
			} else {
				mWebview.clearCache(true);
				RootCategoryResponse categoriesResp = (RootCategoryResponse) msg.obj;
				ArrayList<CategoryGroup> categories = categoriesResp.getCategories();
				Intent intent = new Intent(TnCActivity.this, HomeActivity.class);
				intent.putParcelableArrayListExtra(AppConstants.DATA_CAT_LIST, categories);
				startActivity(intent);
				finish();
			}
			stopSppiner();
		}
	}

	@Override
	public void onPositiveDialogButton(int id) {
		if(id == CustomDialog.TNC_DIALOG)
			finish();
		else if(id == CustomDialog.SINGLE_BUTTON_CONFIRMATION_DIALOG)
		{
			mWebview.loadUrl(AppConstants.TNC_PAGE_URL);
			showNavigationButtons();
		}
		super.onPositiveDialogButton(id);
	}


	private void showNavigationButtons() {
		mBtnAccept.setVisibility(View.VISIBLE);
		mBtnReject.setVisibility(View.VISIBLE);		
	}

	private void hideNavigationButtons() {
		mBtnAccept.setVisibility(View.GONE);
		mBtnReject.setVisibility(View.GONE);

	}

	private void showWebView() {
		mErrorLayout.setVisibility(View.GONE);
		mWebview.setVisibility(View.VISIBLE);
	}
	private void hideWebView() {
		mErrorLayout.setVisibility(View.VISIBLE);
		mWebview.setVisibility(View.GONE);
	}


}
