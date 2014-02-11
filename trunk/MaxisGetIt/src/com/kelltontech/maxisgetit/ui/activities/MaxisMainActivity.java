package com.kelltontech.maxisgetit.ui.activities;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.ui.BaseMainActivity;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.CityAreaListController;
import com.kelltontech.maxisgetit.controllers.CombindListingController;
import com.kelltontech.maxisgetit.controllers.UserDetailController;
import com.kelltontech.maxisgetit.dao.GPS_Data;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import com.kelltontech.maxisgetit.requests.CombinedListRequest;
import com.kelltontech.maxisgetit.response.CompanyListResponse;
import com.kelltontech.maxisgetit.response.UserDetailResponse;
import com.kelltontech.maxisgetit.ui.widgets.CustomDialog;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

/**
 * Base class for all Activity of application.
 */
public abstract class MaxisMainActivity extends BaseMainActivity {
	private CompanyListResponse mClResponse;
	private CombinedListRequest mListRequest;
	private MaxisStore mStore;
	private String	mUrlToBeOpenedWithChooser;
	protected String mSearchKeyword;

	protected boolean isLocationAware() {
		return mStore.isLocalized();
	}

	/**
	 * @param listRequest
	 *            only if you are going to initiate a fresh deal listing or
	 *            company listing by category
	 */
	public void setRequest(CombinedListRequest listRequest) {
		this.mListRequest = listRequest;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mStore = MaxisStore.getStore(MaxisMainActivity.this);
		if(getIntent().getExtras() != null)
		{
			mSearchKeyword =  getIntent().getExtras().getString(AppConstants.GLOBAL_SEARCH_KEYWORD);
		}
		AnalyticsHelper.onActivityCreate();
		// searchBtn = (ImageView) findViewById(R.id.search_icon_button);
		
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		EditText editText = (EditText) findViewById(R.id.search_box);
		InputFilter filter = new InputFilter() {
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
				for (int i = start; i < end; i++) {
//					if (!Character.isLetterOrDigit(source.charAt(i)) && source.charAt(i) != '\'' && source.charAt(i) != '_' && source.charAt(i) != '-' && source.charAt(i) != ' '&& source.charAt(i) != '&') {
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
	
	/*@Override
	protected void onStart() {
		super.onStart();
		AnalyticsHelper.onActivityStart(this);
		AnalyticsHelper.setLogEnabled(true);
		AnalyticsHelper.getReleaseVersion();
		if(!StringUtil.isNullOrEmpty(mStore.getUserMobileNumber())){
		AnalyticsHelper.setUserID(mStore.getUserMobileNumber());
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		AnalyticsHelper.onActivityStop(this);
	}*/

	protected void performSearch(String searchText , String postJsonPayload) {
		if (searchText == null || searchText.trim().equals("")) {
			showInfoDialog(getResources().getString(R.string.input_search));
			return;
		}
		startSppiner();
		mListRequest = new CombinedListRequest(MaxisMainActivity.this);
		mListRequest.setBySearch(true);
		mListRequest.setCompanyListing(true);
//		String encodedText=URLEncoder.encode(searchText.trim());
//		mListRequest.setKeywordOrCategoryId(encodedText);
		mListRequest.setKeywordOrCategoryId(Uri.encode(searchText.trim()));
		
		HashMap<String,String>	map = new HashMap<String,String>();
		map.put(FlurryEventsConstants.HOME_SEARCH_TEXT, searchText);
		AnalyticsHelper.logEvent(FlurryEventsConstants.HOME_SCREEN_SEARCH,map);
		
		mListRequest.setLatitude(GPS_Data.getLatitude());
		mListRequest.setLongitude(GPS_Data.getLongitude());
		
		if(postJsonPayload!=null)
		mListRequest.setPostJsonPayload(postJsonPayload);
		

		CombindListingController listingController = new CombindListingController(MaxisMainActivity.this, Events.COMBIND_LISTING_NEW_LISTING_PAGE);
		listingController.requestService(mListRequest);
	}
	
//	protected void performSearch(String searchText) {
//		if (searchText == null || searchText.trim().equals("")) {
//			showInfoDialog(getResources().getString(R.string.input_search));
//			return;
//		}
//		startSppiner();
//		mListRequest = new CombinedListRequest(MaxisMainActivity.this);
//		mListRequest.setBySearch(true);
//		mListRequest.setCompanyListing(true);
////		String encodedText=URLEncoder.encode(searchText.trim());
////		mListRequest.setKeywordOrCategoryId(encodedText);
//		mListRequest.setKeywordOrCategoryId(Uri.encode(searchText.trim()));
//		
//		HashMap<String,String>	map = new HashMap<String,String>();
//		map.put(FlurryEventsConstants.HOME_SEARCH_TEXT, searchText);
//		AnalyticsHelper.logEvent(FlurryEventsConstants.HOME_SCREEN_SEARCH,map);
//		
//		mListRequest.setLatitude(GPS_Data.getLatitude());
//		mListRequest.setLongitude(GPS_Data.getLongitude());
//		
////		if(postJsonPayload!=null)
////		mListRequest.setPostJsonPayload(postJsonPayload);
//		
//
//		CombindListingController listingController = new CombindListingController(MaxisMainActivity.this, Events.COMBIND_LISTING_NEW_LISTING_PAGE);
//		listingController.requestService(mListRequest);
//	}

	protected void showCompanyDealListing(String categoryId, String categoryTitle,String thumbUrl, boolean iscompanyListing, String groupType, String groupActionType) {
		startSppiner();
		mListRequest = new CombinedListRequest(MaxisMainActivity.this);
		mListRequest.setBySearch(false);
		mListRequest.setCompanyListing(iscompanyListing);
		mListRequest.setKeywordOrCategoryId(categoryId);
		mListRequest.setLatitude(GPS_Data.getLatitude());
		mListRequest.setLongitude(GPS_Data.getLongitude());
		mListRequest.setParentThumbUrl(thumbUrl);
		mListRequest.setCategoryTitle(categoryTitle);
		mListRequest.setGroupType(groupType);
		mListRequest.setGroupActionType(groupActionType);
		CombindListingController listingController = new CombindListingController(MaxisMainActivity.this, Events.COMBIND_LISTING_NEW_LISTING_PAGE);
		listingController.requestService(mListRequest);
	}
	
	

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				Intent intent = new Intent(MaxisMainActivity.this, CombindListActivity.class);
				intent.putExtra(AppConstants.COMP_LIST_DATA, mClResponse);
				intent.putExtra(AppConstants.DATA_LIST_REQUEST, mListRequest);
				startActivity(intent);
			}
			stopSppiner();
		}else if (msg.arg2 == Events.USER_DETAIL) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				Intent intent = new Intent(MaxisMainActivity.this, MyAccountActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD, mSearchKeyword);
				intent.putExtra(AppConstants.USER_DETAIL_DATA, (UserDetailResponse)msg.obj);
				startActivity(intent);
			}
			stopSppiner();
		}
	}

	protected void onProfileClick() {
		if (mStore.isLoogedInUser()) {
			UserDetailController controller = new UserDetailController(MaxisMainActivity.this, Events.USER_DETAIL);
			controller.requestService(mStore.getUserMobileNumber());
			startSppiner();
//			Intent intentMyAccount = new Intent(MaxisMainActivity.this, MyAccountActivity.class);
//			intentMyAccount.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//			startActivity(intentMyAccount);
		} /*else if (mStore.isRegisteredUser() && !mStore.isVerifiedUser()) {
			Intent intentverify = new Intent(MaxisMainActivity.this, VerifyRegistrationActivity.class);
			intentverify.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentverify);
		} */else {
			Intent intentLogin = new Intent(MaxisMainActivity.this, LoginActivity.class);
			intentLogin.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intentLogin.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD, mSearchKeyword);
			startActivity(intentLogin);
		}
		AnalyticsHelper.logEvent(FlurryEventsConstants.SHOW_PROFILE_CLICK);
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
					message.obj = getResources().getString(R.string.communication_failure);
					// clResponse.getServerMessage() + " " +
					// clResponse.getErrorCode();
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
		showInfoDialog(message.toString());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/*MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main, menu);
		return true;*/
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent intent = new Intent(MaxisMainActivity.this, ActivityAccountSettings.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			break;
		}
		return true;
	}
	public void showDialogWithTitle(String title,String info) {
		CustomDialog customDialog = new CustomDialog(CustomDialog.INFO_DIALOG, MaxisMainActivity.this);
		mDialog = customDialog.createDisclaimerDialog(title, info);
	}
	public void showInfoDialog(String info) {
		CustomDialog customDialog = new CustomDialog(CustomDialog.INFO_DIALOG, MaxisMainActivity.this);
		mDialog = customDialog.createCustomDialog(info);
	}
	
	public void showExitAndBackDialog(String info) {
		CustomDialog customDialog = new CustomDialog(CustomDialog.TNC_DIALOG, MaxisMainActivity.this);
		mDialog = customDialog.createCustomDialog(info);
	}
		
	public void showFinalDialog(String info) {
		CustomDialog customDialog = new CustomDialog(CustomDialog.FINAL_DIALOG, MaxisMainActivity.this);
		mDialog = customDialog.createCustomDialog(info);
	}

	public void showConfirmationDialog(int id, String info) {
		CustomDialog customDialog = new CustomDialog(id, MaxisMainActivity.this);
		mDialog = customDialog.createCustomDialog(info);
	}
	
	public void showSingleButtonConfirmationDialog( String info) {
		CustomDialog customDialog = new CustomDialog(CustomDialog.SINGLE_BUTTON_CONFIRMATION_DIALOG, MaxisMainActivity.this);
		mDialog = customDialog.createCustomDialog(info);
	}
	
	public void showPlayStoreDialog(String info) {
		stopSppiner();
		CustomDialog customDialog = new CustomDialog(CustomDialog.PLAY_STORE_DIALOG, MaxisMainActivity.this);
		mDialog = customDialog.createCustomDialog(info);
	}

	@Override
	public void onPositiveDialogButton(int pDialogId) {
		switch(pDialogId) {
		case CustomDialog.LOCATION_DIALOG:
			Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS); //intent1.setClassName("com.android.settings", "com.android.settings.WirelessSettings"); 
			startActivity(Intent.createChooser(intent1, "Open Settings"));
			break;
		case CustomDialog.DATA_USAGE_DIALOG_FOR_FACEBOOK:
			openDeviceBrowser(AppConstants.FB_PAGE_URL, false);
			break;
		case CustomDialog.DATA_USAGE_DIALOG_FOR_TWITTER:
			openDeviceBrowser(AppConstants.TWITTER_PAGE_URL, false);
			break;
		case CustomDialog.DATA_USAGE_DIALOG_FOR_WEBSITE:
			openDeviceBrowser(mUrlToBeOpenedWithChooser, true);
			break;
		} 
	}
	
	@Override
	public void onNegativeDialogbutton(int id) {
		mDialog.dismiss();
	}
	
	/**
	 * To remove all screens from stack up to and excluding Home Screen 
	 */
	protected void showHomeScreen() {
		Intent intentHome = new Intent(this, HomeActivity.class);
		intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intentHome);
	}
	
	/**
	 * To check if location is available and show appropriate dialog
	 */ 
	protected  boolean isLocationAvailable() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			showConfirmationDialog(CustomDialog.LOCATION_DIALOG,getResources().getString(R.string.cd_error_enable_gps));
			//showAlertDialog(getResources().getString(R.string.cd_error_enable_gps));
			return false;
		}
		if(GPS_Data.getLatitude() == 0 || GPS_Data.getLongitude() == 0) {
			showAlertDialog(getResources().getString(R.string.cd_error_location_not_found));
			return false;
		}
		return true;
	}
	
	protected boolean isDialogToBeShown() {
		/*long savedUserTime = mStore.getUserDecisionTime();
		int noOfDays = (int)((System.currentTimeMillis() - savedUserTime) / (1000 * 60 *60 *24)) ;
		if(!mStore.isUserDecisionRemembered() || noOfDays > 7)
			return true;
		else
			return false;*/
		return !mStore.isUserDecisionRemembered();
	}
	
	/**
	 * @param url
	 */
	protected void checkPreferenceAndOpenBrowser(String url) {
		url = StringUtil.getFormattedURL(url);
		int dialogIdTobeShown = CustomDialog.DATA_USAGE_DIALOG_FOR_WEBSITE;
		if( url.indexOf("facebook") != -1 ) {
			dialogIdTobeShown = CustomDialog.DATA_USAGE_DIALOG_FOR_FACEBOOK;
		} else if( url.indexOf("twitter") != -1 ) {
			dialogIdTobeShown = CustomDialog.DATA_USAGE_DIALOG_FOR_TWITTER;
		}
		
		if( ! isDialogToBeShown() ) {
			if( dialogIdTobeShown == CustomDialog.DATA_USAGE_DIALOG_FOR_WEBSITE ) {
				openDeviceBrowser(url, true);
			} else {
				openDeviceBrowser(url, false);
			}
		} else {
			if( dialogIdTobeShown == CustomDialog.DATA_USAGE_DIALOG_FOR_WEBSITE ) {
				mUrlToBeOpenedWithChooser = url;
			}
			showConfirmationDialog(dialogIdTobeShown, getString(R.string.cd_msg_data_usage));
		}
	}
	
	/**
	 * @param url
	 */
	protected boolean overrideWebViewUrlLoading(String url) {
		Log.i("maxis", "Processing webview url click...");
        if (url != null && url.startsWith("http://")) {
        	if( url.indexOf( getString(R.string.findit_com)) == -1 ) {
        		checkPreferenceAndOpenBrowser(url);
        	} else {
        		openDeviceBrowser(url, true);
        	}
        	return true;
        } else {
            return false;
        }
	}
	public void setSearchCity() {
		// TODO
		CityAreaListController clController = new CityAreaListController(
				MaxisMainActivity.this, Events.CITY_LISTING);
		startSppiner();
		clController.requestService(null);

	}

	public void setSearchLocality(int city_id) {
		// TODO
		if (city_id != -1) {
			CityAreaListController clController = new CityAreaListController(
					MaxisMainActivity.this, Events.LOCALITY_LISTING);
			startSppiner();
			clController.requestService(city_id + "");
		} else {
			showAlertDialog("Please select a City.");
		}
	}
}
