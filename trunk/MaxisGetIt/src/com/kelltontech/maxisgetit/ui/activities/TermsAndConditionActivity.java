package com.kelltontech.maxisgetit.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class TermsAndConditionActivity extends MaxisMainActivity {
	private RelativeLayout mErrorLayout;
	private WebView mWebview;
	private boolean isErrorOccured = false;
	private int mTnCOf;
	private String mTnCPageUrl;
	private String urlData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tnc);
		findViewById(R.id.atnc_accept).setVisibility(View.INVISIBLE);
		findViewById(R.id.atnc_reject).setVisibility(View.INVISIBLE);
		Button backButton = (Button) findViewById(R.id.atnc_back);
		backButton.setVisibility(View.VISIBLE);
		backButton.setOnClickListener(this);
		mTnCPageUrl = AppConstants.TNC_PAGE_URL;

		if (getIntent().getExtras() != null) {
			mTnCOf = getIntent().getExtras().getInt(AppConstants.TNC_FROM);
			urlData = getIntent().getExtras().getString("TnC_Data");
			setPageUrl();
		}

		findViewById(R.id.atnc_try_again).setOnClickListener(this);
		mErrorLayout = (RelativeLayout) findViewById(R.id.atnc_error_layout);

		mWebview = ((WebView) findViewById(R.id.atnc_info));// .loadUrl(AppConstants.TNC_PAGE_URL);
		WebSettings settings = mWebview.getSettings();
		settings.setJavaScriptEnabled(true);
		mWebview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

		startSppiner();

		mWebview.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return overrideWebViewUrlLoading(url);
			}

			public void onPageFinished(WebView view, String url) {
				Log.i("maxis", "Finished loading URL: " + url);
				stopSppiner();
				if (!isErrorOccured) {
					showWebView();
				} else {
					hideWebView();
				}
			}

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Log.i("maxis", "Error: " + description);
				showInfoDialog(getResources().getString(
						R.string.atnc_error_loading_page));
				hideWebView();
				isErrorOccured = true;
			}

		});
		if (mTnCOf != AppConstants.TNC_FROM_DEAL) {
			mWebview.loadUrl(mTnCPageUrl);
		} else {
			mWebview.loadDataWithBaseURL("", mTnCPageUrl, "text/html", "UTF-8",
					"");
			// ((TextView)findViewById(R.id.terms_text_view)).setText(Html.fromHtml(getResources().getString(R.string.terms_condition_text)));
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		AnalyticsHelper.trackSession(TermsAndConditionActivity.this, AppConstants.TnC);
	}
	
	private void setPageUrl() {
		switch (mTnCOf) {
		case AppConstants.TNC_FROM_COMP:
			mTnCPageUrl = AppConstants.TNC_PAGE_URL+"&"+AppConstants.KEY_PAGE_REVIEW+"="+AppConstants.TnC+"";
			break;
		case AppConstants.TNC_FROM_CONTEST:
			mTnCPageUrl = AppConstants.TNC_CONTEST_PAGE_URL+"&"+AppConstants.KEY_PAGE_REVIEW+"="+AppConstants.TnC+"";;
			break;
			
		case AppConstants.TNC_FROM_DEAL :
			mTnCPageUrl  = urlData;
			break;
		default:
			break;
		}
		
	}

	@Override
	public Activity getMyActivityReference() {
		return null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.atnc_back:
			mWebview.clearCache(true);
			finish();
			break;
		case R.id.atnc_try_again:
			showSppiner();
			isErrorOccured = false;
			mWebview.loadUrl(mTnCPageUrl);
			/*
			 * showNavigationButtons(); showWebView();
			 */

			break;
		default:
			break;
		}
	}

	@Override
	public void updateUI(Message msg) {
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
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
