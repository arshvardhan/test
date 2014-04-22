package com.kelltontech.maxisgetit.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.ui.BaseMainActivity;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.CombindListingController;
import com.kelltontech.maxisgetit.controllers.UserDetailController;
import com.kelltontech.maxisgetit.dao.GPS_Data;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import com.kelltontech.maxisgetit.requests.CombinedListRequest;
import com.kelltontech.maxisgetit.response.CompanyListResponse;
import com.kelltontech.maxisgetit.response.UserDetailResponse;
import com.kelltontech.maxisgetit.ui.widgets.CustomDialog;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public abstract class MaxixBaseWithPopDownSearch extends BaseMainActivity {
	private CompanyListResponse mClResponse;
	private CombinedListRequest mListRequest;
	private MaxisStore mStore;

	private LinearLayout mSearchContainer;
	private ImageView mSearchToggler;
	private TextView mHeaderTitle;
	private ImageView mSearchBtn;
	private EditText mSearchEditText;
	private ImageView mHeaderBackButton;
	private ImageView mHomeIconView;

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
		mStore = MaxisStore.getStore(MaxixBaseWithPopDownSearch.this);
		// searchBtn = (ImageView) findViewById(R.id.search_icon_button);
		AnalyticsHelper.onActivityCreate();
	}

	@Override
	protected void onResume() {
		super.onResume();
		EditText editText = (EditText) findViewById(R.id.search_box);
		InputFilter filter = new InputFilter() {
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
				for (int i = start; i < end; i++) {
					// if (!Character.isLetterOrDigit(source.charAt(i)) &&
					// source.charAt(i) != '\'' && source.charAt(i) != '_' &&
					// source.charAt(i) != '-' && source.charAt(i) != ' '&&
					// source.charAt(i) != '&') {
					if (!Character.isLetterOrDigit(source.charAt(i)) && source.charAt(i) != '\'' && source.charAt(i) != '_' && source.charAt(i) != '-' && source.charAt(i) != ' ') {
						return "";
					}
				}
				return null;
			}
		};
		if (null != editText) {
			editText.setFilters(new InputFilter[] { filter });
		}
		if (mSearchContainer == null) {
			mSearchContainer = (LinearLayout) findViewById(R.id.search_box_container);
			mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
			mSearchBtn.setOnClickListener(this);
			mSearchEditText = (EditText) findViewById(R.id.search_box);
		}
		if (mSearchToggler == null || mHeaderTitle == null) {
			mSearchToggler = (ImageView) findViewById(R.id.search_toggler);
			mSearchToggler.setOnClickListener(this);
			mHeaderBackButton = (ImageView) findViewById(R.id.header_btn_back);
			mHeaderBackButton.setOnClickListener(this);
			mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
			mHomeIconView.setOnClickListener(this);
			mHeaderTitle = (TextView) findViewById(R.id.header_title);
		}

	}

	protected void performSearch(String searchText) {
		if (searchText == null || searchText.trim().equals("")) {
			showInfoDialog(getResources().getString(R.string.input_search));
			return;
		}
		startSppiner();
		mListRequest = new CombinedListRequest(MaxixBaseWithPopDownSearch.this);
		mListRequest.setBySearch(true);
		mListRequest.setCompanyListing(true);
		// String encodedText=URLEncoder.encode(searchText.trim());
		// mListRequest.setKeywordOrCategoryId(encodedText);
		mListRequest.setKeywordOrCategoryId(searchText.trim());
		mListRequest.setLatitude(GPS_Data.getLatitude());
		mListRequest.setLongitude(GPS_Data.getLongitude());

		CombindListingController listingController = new CombindListingController(MaxixBaseWithPopDownSearch.this, Events.COMBIND_LISTING_NEW_LISTING_PAGE);
		listingController.requestService(mListRequest);
	}

	protected void showCompanyDealListing(String categoryId, String categoryTitle, String thumbUrl, boolean iscompanyListing) {
		startSppiner();
		mListRequest = new CombinedListRequest(MaxixBaseWithPopDownSearch.this);
		mListRequest.setBySearch(false);
		mListRequest.setCompanyListing(iscompanyListing);
		mListRequest.setKeywordOrCategoryId(categoryId);
		mListRequest.setLatitude(GPS_Data.getLatitude());
		mListRequest.setLongitude(GPS_Data.getLongitude());
		mListRequest.setParentThumbUrl(thumbUrl);
		mListRequest.setCategoryTitle(categoryTitle);
		CombindListingController listingController = new CombindListingController(MaxixBaseWithPopDownSearch.this, Events.COMBIND_LISTING_NEW_LISTING_PAGE);
		listingController.requestService(mListRequest);
	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				Intent intent = new Intent(MaxixBaseWithPopDownSearch.this, CombindListActivity.class);
				intent.putExtra(AppConstants.COMP_LIST_DATA, mClResponse);
				intent.putExtra(AppConstants.DATA_LIST_REQUEST, mListRequest);
				startActivity(intent);
			}
			stopSppiner();
		} else if (msg.arg2 == Events.USER_DETAIL) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				Intent intent = new Intent(MaxixBaseWithPopDownSearch.this, MyAccountActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.putExtra(AppConstants.USER_DETAIL_DATA, (UserDetailResponse) msg.obj);
				startActivity(intent);
			}
			stopSppiner();
		}
	}

	protected void onProfileClick() {
		if (mStore.isLoogedInUser()) {
			UserDetailController controller = new UserDetailController(MaxixBaseWithPopDownSearch.this, Events.USER_DETAIL);
			controller.requestService(mStore.getUserMobileNumber());
			startSppiner();
			// Intent intentMyAccount = new Intent(MaxisMainActivity.this,
			// MyAccountActivity.class);
			// intentMyAccount.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			// startActivity(intentMyAccount);
		} else if (mStore.isRegisteredUser() && !mStore.isVerifiedUser()) {
			Intent intentverify = new Intent(MaxixBaseWithPopDownSearch.this, VerifyRegistrationActivity.class);
			intentverify.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentverify);
		} else {
			Intent intentLogin = new Intent(MaxixBaseWithPopDownSearch.this, LoginActivity.class);
			intentLogin.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentLogin);
		}
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.USER_DETAIL) {
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
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent intent = new Intent(MaxixBaseWithPopDownSearch.this, ActivityAccountSettings.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			break;
		}
		return true;
	}

	public void showDialogWithTitle(String title, String info) {
		CustomDialog customDialog = new CustomDialog(CustomDialog.INFO_DIALOG, MaxixBaseWithPopDownSearch.this);
		mDialog = customDialog.createDisclaimerDialog(title, info);
	}

	public void showInfoDialog(String info) {
		CustomDialog customDialog = new CustomDialog(CustomDialog.INFO_DIALOG, MaxixBaseWithPopDownSearch.this);
		mDialog = customDialog.createCustomDialog(info);
	}

	public void showFinalDialog(String info) {
		CustomDialog customDialog = new CustomDialog(CustomDialog.FINAL_DIALOG, MaxixBaseWithPopDownSearch.this);
		mDialog = customDialog.createCustomDialog(info);
	}

	public void showConfirmationDialog(int id, String info) {
		CustomDialog customDialog = new CustomDialog(id, MaxixBaseWithPopDownSearch.this);
		mDialog = customDialog.createCustomDialog(info);
	}

	public void onPositiveDialogButton(int id) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_toggler:
			AnalyticsHelper.logEvent(FlurryEventsConstants.HOME_SEARCH_CLICK);
			if (mSearchContainer.getVisibility() == View.VISIBLE) {
				mSearchContainer.setVisibility(View.GONE);
			} else {
				mSearchContainer.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.header_btn_back:
			AnalyticsHelper.logEvent(FlurryEventsConstants.BACK_CLICK);
			this.finish();
			break;
		case R.id.search_icon_button:
			AnalyticsHelper.logEvent(FlurryEventsConstants.HOME_SEARCH_CLICK);
			mSearchEditText.setText(mSearchEditText.getText().toString().trim());
			performSearch(mSearchEditText.getText().toString());
			break;
		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			Intent intentHome = new Intent(MaxixBaseWithPopDownSearch.this, HomeActivity.class);
			intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentHome);
			HomeActivity.fromHomeClick = true;
			break;
		}
	}

	public void onNegativeDialogbutton(int id) {
		mDialog.dismiss();
	}
}
