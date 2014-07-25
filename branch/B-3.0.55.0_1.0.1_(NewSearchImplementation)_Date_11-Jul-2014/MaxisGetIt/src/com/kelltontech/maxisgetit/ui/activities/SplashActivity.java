package com.kelltontech.maxisgetit.ui.activities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import mobi.vserv.android.trackingengine.VservTrackingManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.RootCategoryController;
import com.kelltontech.maxisgetit.controllers.SessionTokenController;
import com.kelltontech.maxisgetit.dao.CategoryGroup;
import com.kelltontech.maxisgetit.dao.GPS_Data;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import com.kelltontech.maxisgetit.response.RootCategoryResponse;
import com.kelltontech.maxisgetit.service.AppSharedPreference;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;
import com.kelltontech.maxisgetit.utils.SessionIdentifierGenerator;

@SuppressLint("NewApi")
public class SplashActivity extends MaxisMainActivity {
	MaxisStore mStore;
	@SuppressLint("NewApi")
	public class BGAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// try {
			// Thread.sleep(3000);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
			SystemClock.sleep(3000);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			if(!mStore.isAppActivated())
			{
				Intent intent=new Intent(SplashActivity.this, AppAuthenticationActivity.class);
				startActivity(intent);
				AnalyticsHelper.endTimedEvent(FlurryEventsConstants.APPLICATION_LAUNCH);
				finish();
			}
			else if(!mStore.isTnCAccepted())
			{
				Intent intent=new Intent(SplashActivity.this, TnCActivity.class);
				startActivity(intent);
				AnalyticsHelper.endTimedEvent(FlurryEventsConstants.APPLICATION_LAUNCH);
				finish();
			}
			/*Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
			startActivity(intent);
			finish();*/
		}

		@SuppressLint("NewApi")
		@Override
		protected void onCancelled() {
			super.onCancelled();
			Log.d("async task killed", "killed async");
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * A unique random number is generated and assigned in the AnalyticsHelper.SESSION_IDENTIFIER to pass as a parameter in Session Tracking Service Request
		 */	
		AnalyticsHelper.SESSION_IDENTIFIER = String.valueOf(System.currentTimeMillis()) + "_" + SessionIdentifierGenerator.nextSessionId();

		mStore = MaxisStore.getStore(SplashActivity.this);
		setContentView(R.layout.activity_splash);
		AnalyticsHelper.logEvent(FlurryEventsConstants.APPLICATION_LAUNCH, true);

		if (isLocationAware())
			setCurrrentLocation();
		else {
			GPS_Data.resetCordinates();
		}

		String sessionId = AppSharedPreference.getString(AppSharedPreference.API_SESSION_ID, null, this);
		String authToken = AppSharedPreference.getString(AppSharedPreference.API_TOKEN, null, this);
		if( StringUtil.isNullOrEmpty(sessionId) || StringUtil.isNullOrEmpty(authToken) ) {
			SessionTokenController tokenController = new SessionTokenController(this, this, Events.SESSION_TOKEN_EVENT);
			tokenController.requestService(null);
		} else {
			continueWithSessionAndToken();
		}

		printHashKey();
		
		// The following method call is made to track App installs via Vserv App Promotion and Campaign
	    VservTrackingManager.process(this);
	}

	@Override
	protected void onStart() {
		AnalyticsHelper.setContinueSession(5);
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// For session tracking on server side as well as for App installation and App launch tracking using Facebook SDK
		AnalyticsHelper.trackSession(SplashActivity.this, AppConstants.Splash_Screen);
	}

	/**
	 * to be called when we have sessionId and authToken for API access
	 */
	private void continueWithSessionAndToken() {
		if (mStore.isAppActivated() && mStore.isTnCAccepted()) {
			mStore.setAppActivated(true);
			RootCategoryController controller = new RootCategoryController(SplashActivity.this, Events.ROOT_CATEGORY_EVENT);
			controller.requestService(null);
		} else {
			new BGAsyncTask().execute();
		}
	}

	private void setCurrrentLocation() {
		// new Thread(new Runnable() {
		// public void run() {
		try {
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			String provider = locationManager.getBestProvider(new Criteria(), true);
			if (null != provider) {
				Location locations = locationManager.getLastKnownLocation(provider);
				List<String> providerList = locationManager.getAllProviders();
				if (null != locations && null != providerList && providerList.size() > 0) {
					double latitude = locations.getLatitude();
					double longitude = locations.getLongitude();
					GPS_Data.setLatitude(latitude);
					GPS_Data.setLongitude(longitude);
					// showToast(latitude+" "+longitude);
				}
			}
			if (GPS_Data.getLatitude() == 0 && GPS_Data.getLongitude() == 0) {
				if (null != provider) {
					Location locations = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					List<String> providerList = locationManager.getAllProviders();
					if (null != locations && null != providerList && providerList.size() > 0) {
						double latitude = locations.getLatitude();
						double longitude = locations.getLongitude();
						GPS_Data.setLatitude(latitude);
						GPS_Data.setLongitude(longitude);
						// showToast(latitude + " " + longitude);
					}
				}
			}
		} catch (Exception e) {
			AnalyticsHelper.onError(FlurryEventsConstants.CURRENT_LOCATION_NOT_SET_ERR, "SplashActivity" + AppConstants.CURRENT_LOCATION_NOT_SET_ERROR_MSG, e);
		}
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		Message message = new Message();
		message.arg2 = event;
		message.arg1 = 1;

		if( event == Events.SESSION_TOKEN_EVENT ) {
			boolean sessionTokenSuccess = false;
			if( screenData instanceof Boolean ) {
				sessionTokenSuccess = (Boolean)screenData;
			}
			if( sessionTokenSuccess ) {
				message.arg1 = 0;
			} else {
				message.obj = getResources().getString(R.string.network_unavailable);
			}
		} else if (event == Events.ROOT_CATEGORY_EVENT ) {
			Response response = (Response) screenData;

			if (response.isError()) {
				message.obj = getResources().getString(R.string.network_unavailable);
			} else {
				if (response.getPayload() instanceof RootCategoryResponse) {
					RootCategoryResponse categoriesResp = (RootCategoryResponse) response.getPayload();
					if (categoriesResp.isErrorFromServer()) {
						message.obj = getResources().getString(R.string.network_unavailable);
					} else {
						if (categoriesResp.getCategories().size() < 1) {
							message.obj = getResources().getString(R.string.network_unavailable);
						} else {
							message.arg1 = 0;
							message.obj = categoriesResp;
						}
					}
				} else {
					message.obj = getResources().getString(R.string.network_unavailable);
				}
			}
		}
		handler.sendMessage(message);
	}

	@Override
	public Activity getMyActivityReference() {
		return null;
	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.SESSION_TOKEN_EVENT) {
			if (msg.arg1 == 1) {
				showFinalDialog((String) msg.obj);
			} else {
				continueWithSessionAndToken();
			}
		} else if (msg.arg2 == Events.ROOT_CATEGORY_EVENT) {
			if (msg.arg1 == 1) {
				showFinalDialog((String) msg.obj);
			} else {
				RootCategoryResponse categoriesResp = (RootCategoryResponse) msg.obj;
				ArrayList<CategoryGroup> categories = categoriesResp.getCategories();
				Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
				intent.putParcelableArrayListExtra(AppConstants.DATA_CAT_LIST, categories);
				//				startService(new Intent(SplashActivity.this,LocationFinderService.class));
				startActivity(intent);
				AnalyticsHelper.endTimedEvent(FlurryEventsConstants.APPLICATION_LAUNCH);
				finish();
			}
		}
	}

	@Override
	public void onClick(View v) {
	}

	public void printHashKey() {
		try { 
			PackageInfo info =
					getPackageManager().getPackageInfo("com.kelltontech.maxisgetit", PackageManager.GET_SIGNATURES); 
			for (Signature signature : info.signatures) { 
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("TEMPTAGHASH KEY:",Base64.encodeToString(md.digest(), Base64.DEFAULT)); 
			} 
		} catch (NameNotFoundException e) { } 
		catch (NoSuchAlgorithmException e) {
		} 
	}

}