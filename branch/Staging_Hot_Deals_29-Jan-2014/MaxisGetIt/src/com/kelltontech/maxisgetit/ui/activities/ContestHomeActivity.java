package com.kelltontech.maxisgetit.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.service.ContestLocationFinderService;
import com.kelltontech.maxisgetit.ui.widgets.CustomDialog;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

/**
 * This screen is Home Screen for Photo Contest.
 * It shows steps of uploading an image.
 */
public class ContestHomeActivity extends ContestBaseActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contest_home_activity);
		AnalyticsHelper.logEvent(FlurryEventsConstants.APPLICATION_CONTEST, true);
		((TextView)findViewById(R.id.header_title)).setText(getString(R.string.header_photo_contest));
		
		findViewById(R.id.header_btn_back).setOnClickListener(this);
		findViewById(R.id.goto_home_icon).setOnClickListener(this);
		findViewById(R.id.search_toggler).setVisibility(View.INVISIBLE);
		findViewById(R.id.show_profile_icon).setOnClickListener(this);
		
		findViewById(R.id.btn_capture_now).setOnClickListener(this);
		
	/*	findViewById(R.id.footer_facebook_icon).setOnClickListener(this);
		findViewById(R.id.footer_twitterIcon).setOnClickListener(this);*/
		
		findViewById(R.id.btn_contest_tnc).setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goto_home_icon: {
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			showHomeScreen();
			break;
		}
		case R.id.header_btn_back: {
			AnalyticsHelper.logEvent(FlurryEventsConstants.BACK_CLICK);
			AnalyticsHelper.endTimedEvent(FlurryEventsConstants.APPLICATION_CONTEST);
			this.finish();
			break;
		}
		case R.id.show_profile_icon: {
			AnalyticsHelper.logEvent(FlurryEventsConstants.SHOW_PROFILE_CLICK);
			onProfileClick();
			break;
		}
		/*case R.id.footer_facebook_icon: {
			AnalyticsHelper.onEvent(FlurryEventsConstants.FACEBOOK_CLICK);
			displayUrlInBrowser(AppConstants.FB_PAGE_URL, this);
			break;
		}
		case R.id.footer_twitterIcon: {
			AnalyticsHelper.onEvent(FlurryEventsConstants.TWITTER_CLICK);
			displayUrlInBrowser(AppConstants.TWITTER_PAGE_URL, this);
			break;
		}*/
		case R.id.btn_capture_now: {
			AnalyticsHelper.logEvent(FlurryEventsConstants.CAPTURE_NOW_CLICK);
//			if(!((LocationManager)getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//				showDialog(CustomDialog.COMMON_ERROR_DIALOG/*, bundle*/);
//			} else if(!AppSharedPreference.getBoolean(AppSharedPreference.LOCATION_PERMISSION_ENABLED, false, getApplicationContext())) {
//				showDialog(CustomDialog.LOCATION_USE_ALERT/*, bundle*/);
//			else {
//				startService(new Intent(ContestHomeActivity.this,ContestLocationFinderService.class));
//				startActivity(new Intent(ContestHomeActivity.this,ContestPoiSearchActivity.class));
//			}
			if( isLocationAvailable() ) {
				startActivity(new Intent(ContestHomeActivity.this,ContestPoiSearchActivity.class));
			}
			break;
		}
		case R.id.btn_contest_tnc:
			Intent intent = new Intent(ContestHomeActivity.this, TermsAndConditionActivity.class);
			intent.putExtra(AppConstants.TNC_FROM, AppConstants.TNC_FROM_CONTEST);
			startActivity(intent);
			//showToast(getResources().getString(R.string.under_implement));
			break;
		}
	}
	
	@Override
	public Activity getMyActivityReference() {
		return this;
	}
}