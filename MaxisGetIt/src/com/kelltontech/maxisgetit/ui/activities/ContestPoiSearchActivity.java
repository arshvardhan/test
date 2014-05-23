package com.kelltontech.maxisgetit.ui.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.kelltontech.framework.model.MyError;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.ContestListController;
import com.kelltontech.maxisgetit.controllers.ContestPoiController;
import com.kelltontech.maxisgetit.model.listModel.RequestDistance;
import com.kelltontech.maxisgetit.model.listModel.ResponseList;
import com.kelltontech.maxisgetit.model.poiModel.RequestPoiSearch;
import com.kelltontech.maxisgetit.model.poiModel.ResponsePoi;
import com.kelltontech.maxisgetit.service.AppSharedPreference;
import com.kelltontech.maxisgetit.service.ContestLocationFinderService;
import com.kelltontech.maxisgetit.ui.widgets.CustomDialog;
import com.kelltontech.maxisgetit.ui.widgets.ThresholdEditTextView;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;
import com.kelltontech.maxisgetit.utils.UiUtils;

/**
 * This screen shows options to search for POIs.
 */
public class ContestPoiSearchActivity extends ContestBaseActivity {

	private ThresholdEditTextView mSearchKeyEdtTxt;
	private int mCurrentEventType;
	private TextView mAddNewPoi;

	/* ImageView mLogo; */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// showDialog(CustomDialog.PROGRESS_DIALOG);
		startSppiner();
		startService(new Intent(ContestPoiSearchActivity.this,
				ContestLocationFinderService.class));

		setContentView(R.layout.contest_poi_search_activity);

		((TextView) findViewById(R.id.header_title))
		.setText(getString(R.string.header_photo_contest));

		findViewById(R.id.header_btn_back).setOnClickListener(this);
		findViewById(R.id.goto_home_icon).setOnClickListener(this);
		findViewById(R.id.search_toggler).setVisibility(View.INVISIBLE);
		findViewById(R.id.show_profile_icon).setOnClickListener(this);
		findViewById(R.id.add_new_poi_curve).setOnClickListener(this);
		mAddNewPoi = (TextView) findViewById(R.id.add_new_poi);
		mAddNewPoi.setOnClickListener(this);

		/*
		 * findViewById(R.id.footer_facebook_icon).setOnClickListener(this);
		 * findViewById(R.id.footer_twitterIcon).setOnClickListener(this); mLogo
		 * = (ImageView) findViewById(R.id.logo);
		 * mLogo.setOnClickListener(ContestPoiSearchActivity.this);
		 */

		UiUtils.hideKeyboardOnTappingOutside(findViewById(R.id.rootLayout),
				this);
		mLongitude = AppSharedPreference.getFloat(
				AppSharedPreference.LONGITUDE, 0.0f, getApplication());
		mLattitude = AppSharedPreference.getFloat(AppSharedPreference.LATITUDE,
				0.0f, getApplication());

		IntentFilter filterSend = new IntentFilter();
		filterSend.addAction("location");
		this.registerReceiver(locationReceiver, filterSend);

		TextView textView = (TextView) findViewById(R.id.tagTaxt);
		textView.setText(Html.fromHtml(getString(R.string.poi_middle_tga1)
				+ " <b>0 POI(s)</b> " + getString(R.string.poi_middle_tga2)
				+ "<br><br>" + getString(R.string.poi_middle_tga3)));

		findViewById(R.id.category_btn).setOnClickListener(this);
		findViewById(R.id.distance_btn).setOnClickListener(this);

		mSearchKeyEdtTxt = (ThresholdEditTextView) findViewById(R.id.searchEdtTxt);
		mSearchKeyEdtTxt
		.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					String _key = mSearchKeyEdtTxt.getText().toString()
							.trim();
					if (_key.length() == 0) {
						Toast.makeText(ContestPoiSearchActivity.this,
								getString(R.string.input_search),
								Toast.LENGTH_SHORT).show();
					} else {
						mCurrentEventType = Events.POI_SEARCH_EVENT;
						onLoad();
					}
					return true;
				}
				return false;
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		AnalyticsHelper.trackSession(ContestPoiSearchActivity.this, AppConstants.POI_Screen);
	}

	@Override
	protected void onLoad() {
		switch (mCurrentEventType) {
		case Events.POI_COUNT_EVENT: {
			// showDialog(CustomDialog.PROGRESS_DIALOG);
			startSppiner();
			ContestPoiController contestPoiController = new ContestPoiController(
					this, Events.POI_COUNT_EVENT);
			RequestPoiSearch requestPoiSearch = new RequestPoiSearch();
			requestPoiSearch.setLatitude(mLattitude);
			requestPoiSearch.setLongitude(mLongitude);
			contestPoiController.requestService(requestPoiSearch);
			break;
		}
		case Events.POI_SEARCH_EVENT: {
			// showDialog(CustomDialog.PROGRESS_DIALOG);
			startSppiner();
			UiUtils.hideSoftKeyboard(this);
			ContestPoiController contestPoiController = new ContestPoiController(
					this, Events.POI_SEARCH_EVENT);
			RequestPoiSearch requestPoiSearch = new RequestPoiSearch();
			requestPoiSearch.setLatitude(mLattitude);
			requestPoiSearch.setLongitude(mLongitude);
			String searchTxt = mSearchKeyEdtTxt.getText().toString().trim();
			requestPoiSearch.setSearchText(searchTxt);
			requestPoiSearch.setPageNumber(1);
			requestPoiSearch
			.setRecordsPerPage(AppConstants.PAGE_SIZE_POI_LIST_SEACH_RESULTS);
			contestPoiController.requestService(requestPoiSearch);
			break;
		}
		}
	}

	/**
	 * This method is used for location updates
	 */
	private void locationUpdate() {
		try {
			unregisterReceiver(locationReceiver);
		} catch (Exception e) {
			// AnalyticsHelper.onError(FlurryEventsConstants.LOCATION_RECIEVER_UNREGISTER_ERR,
			// "ContestPoiSearchActivity : " +
			// AppConstants.LOCATION_RECEIVER_UNREGISTER_ERROR_MSG, e);
		}
		long locationOld = 10 * 60 * 1000;
		if (locationOld <= AppSharedPreference.getLong(
				AppSharedPreference.LOCATION_TIME, System.currentTimeMillis(),
				getApplication())) {
			getLocation();
			mCurrentEventType = Events.POI_COUNT_EVENT;
			onLoad();
		} else {
			// removeDialog(CustomDialog.PROGRESS_DIALOG);
			stopSppiner();
			Toast.makeText(getApplicationContext(),
					"Could not find your Location.", Toast.LENGTH_SHORT).show();
			finish();
		}
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
			this.onBackPressed();
			break;
		}
		case R.id.show_profile_icon: {
			onProfileClick();
			break;
		}
		case R.id.category_btn: {
			AnalyticsHelper.logEvent(FlurryEventsConstants.CATEGORY_CLICK);
			mCurrentEventType = Events.CATEGORY_LIST_EVENT;
			// showDialog(CustomDialog.PROGRESS_DIALOG);
			startSppiner();
			ContestListController contestListController = new ContestListController(
					this, Events.CATEGORY_LIST_EVENT);
			RequestDistance distance1 = new RequestDistance();
			getLocation();
			distance1.setLatitude(mLattitude);
			distance1.setLongitude(mLongitude);
			distance1.setPageNumber(1);
			distance1
			.setRecordsPerPage(AppConstants.PAGE_SIZE_POI_LIST_BY_DISTANCE);
			contestListController.requestService(distance1);
			break;
		}
		case R.id.distance_btn: {
			AnalyticsHelper.logEvent(FlurryEventsConstants.DISTANCE_CLICK);
			mCurrentEventType = Events.DISTANCE_LIST_EVENT;
			// showDialog(CustomDialog.PROGRESS_DIALOG);
			startSppiner();
			ContestListController listController1 = new ContestListController(
					this, Events.DISTANCE_LIST_EVENT);
			RequestDistance distance = new RequestDistance();
			getLocation();
			distance.setLatitude(mLattitude);
			distance.setLongitude(mLongitude);
			distance.setPageNumber(1);
			distance.setRecordsPerPage(AppConstants.PAGE_SIZE_POI_LIST_BY_DISTANCE);
			listController1.requestService(distance);
			break;
		}
		case R.id.add_new_poi:
		case R.id.add_new_poi_curve: {
			AnalyticsHelper.logEvent(FlurryEventsConstants.ADD_NEW_POI);
			mAddNewPoi.setPressed(true);
			addNewPOIClick();
			break;
		}
		/*
		 * case R.id.footer_facebook_icon: {
		 * AnalyticsHelper.logEvent(FlurryEventsConstants.FACEBOOK_CLICK);
		 * checkPreferenceAndOpenBrowser(AppConstants.FB_PAGE_URL); break; }
		 * case R.id.footer_twitterIcon: {
		 * AnalyticsHelper.logEvent(FlurryEventsConstants.TWITTER_CLICK);
		 * checkPreferenceAndOpenBrowser(AppConstants.TWITTER_PAGE_URL); break;
		 * } case R.id.logo: { startActivity(new
		 * Intent(ContestPoiSearchActivity.this, GetItInfoActivity.class));
		 * break; }
		 */
		}
	}

	@Override
	public void setScreenData(Object screenData, int event, long hitTime) {
		if (event == Events.USER_DETAIL) {
			super.setScreenData(screenData, event, hitTime);
		} else if (mHitTime == hitTime) {
			Message message = new Message();
			message.obj = screenData;
			message.what = event;
			handler.sendMessage(message);
		}
	}

	@Override
	public Activity getMyActivityReference() {
		return null;
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.obj instanceof MyError) {
				// removeDialog(CustomDialog.PROGRESS_DIALOG);
				stopSppiner();
				switch (((MyError) msg.obj).getErrorcode()) {
				case MyError.NETWORK_NOT_AVAILABLE:
					Toast.makeText(getApplicationContext(),
							"Network not available.", Toast.LENGTH_LONG).show();
					break;
				case MyError.EXCEPTION:
				case MyError.UNDEFINED:
					Toast.makeText(getApplicationContext(),
							"Server not responding.", Toast.LENGTH_LONG).show();
					break;
				}
				if (mCurrentEventType == Events.POI_COUNT_EVENT) {
					finish();
				}
			} else if (msg.obj instanceof ResponseList) {
				ResponseList responselist = (ResponseList) msg.obj;
				if (responselist.getList() != null
						&& responselist.getList().size() > 0) {
					Intent poiListIntent = new Intent(
							ContestPoiSearchActivity.this,
							ContestPoiListActivity.class);
					poiListIntent.putExtra(AppConstants.LIST_RESPONSE_PARCEL,
							responselist);
					poiListIntent.putExtra(AppConstants.LIST_RESPONSE_TYPE,
							mCurrentEventType);
					poiListIntent.putExtra(AppConstants.EXTRA_SEARCH_KEYWORD,
							mSearchKeyEdtTxt.getText().toString());
					startActivity(poiListIntent);
					// removeDialog(CustomDialog.PROGRESS_DIALOG);
					stopSppiner();
				} else {
					// removeDialog(CustomDialog.PROGRESS_DIALOG);
					stopSppiner();
					if (mCurrentEventType == Events.POI_SEARCH_EVENT) {
						// Toast.makeText(getApplicationContext(),getString(R.string.no_result_found),
						// Toast.LENGTH_LONG).show();
						showConfirmationDialog(
								CustomDialog.ADD_NEW_POI_CONFIRMATION_DIALOG,
								getString(R.string.dialog_anp_confirmation));
					} else {
						Toast.makeText(getApplicationContext(),
								getString(R.string.server_not_responding),
								Toast.LENGTH_LONG).show();
					}
				}
			} else if (msg.obj instanceof ResponsePoi) {
				// if(removeDialog)
				// removeDialog(CustomDialog.PROGRESS_DIALOG);
				stopSppiner();
				// else removeDialog=true;
				ResponsePoi responselist = (ResponsePoi) msg.obj;
				TextView textView = (TextView) findViewById(R.id.tagTaxt);
				textView.setText(Html
						.fromHtml(getString(R.string.poi_middle_tga1) + " <b>"
								+ responselist.getPoiCount() + " POI(s) </b>"
								+ getString(R.string.poi_middle_tga2)
								+ "<br><br>"
								+ getString(R.string.poi_middle_tga3)));
			}
		}
	};

	private BroadcastReceiver locationReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			locationUpdate();
		}
	};

	@Override
	protected void onStop() {
		try {
			unregisterReceiver(locationReceiver);
		} catch (Exception e) {
			// AnalyticsHelper.onError(FlurryEventsConstants.LOCATION_RECIEVER_UNREGISTER_ERR,
			// "ContestPoiSearchActivity : " +
			// AppConstants.LOCATION_RECEIVER_UNREGISTER_ERROR_MSG, e);
		}
		super.onStop();
	}

	private void addNewPOIClick() {
		AnalyticsHelper.logEvent(FlurryEventsConstants.ADD_NEW_POI);
		mCurrentEventType = Events.ADD_NEW_POI_EVENT;
		showImageUploadDialog(CustomDialog.UPLOAD_CONTEST_IMAGE_DIALOG, "");
	}

	@Override
	public void onPositiveDialogButton(int id) {
		if (id == CustomDialog.ADD_NEW_POI_CONFIRMATION_DIALOG) {
			addNewPOIClick();
		} else if(id == CustomDialog.UPLOAD_CONTEST_IMAGE_DIALOG){
			Intent addNewPOIIntent = new Intent(ContestPoiSearchActivity.this, ContestAddNewPoiActivity.class);
			addNewPOIIntent.putExtra(AppConstants.POST_IMAGE_REQUEST_KEY,AppConstants.GALLERY_REQUEST);
			addNewPOIIntent.putExtra(AppConstants.ADD_NEW_POI_KEY,mCurrentEventType);
			startActivity(addNewPOIIntent);
		} else {
			super.onPositiveDialogButton(id);
		}
	}

	@Override
	public void onNegativeDialogbutton(int id) {
		if(id == CustomDialog.UPLOAD_CONTEST_IMAGE_DIALOG){
			Intent addNewPOIIntent = new Intent(ContestPoiSearchActivity.this,ContestAddNewPoiActivity.class);
			addNewPOIIntent.putExtra(AppConstants.POST_IMAGE_REQUEST_KEY, AppConstants.CAMERA_REQUEST);
			addNewPOIIntent.putExtra(AppConstants.ADD_NEW_POI_KEY, mCurrentEventType);
			startActivity(addNewPOIIntent);
		} else {
			super.onNegativeDialogbutton(id);
		}
	}

}
