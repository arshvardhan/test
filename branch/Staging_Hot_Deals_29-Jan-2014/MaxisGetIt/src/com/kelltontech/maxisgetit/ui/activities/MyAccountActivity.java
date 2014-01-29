package com.kelltontech.maxisgetit.ui.activities;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.ClassifiedListController;
import com.kelltontech.maxisgetit.controllers.FavouriteController;
import com.kelltontech.maxisgetit.controllers.LocalSearchController;
import com.kelltontech.maxisgetit.controllers.MyDealsController;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import com.kelltontech.maxisgetit.requests.CombinedListRequest;
import com.kelltontech.maxisgetit.requests.FavCompanyListRequest;
import com.kelltontech.maxisgetit.response.ClassifiedListResponse;
import com.kelltontech.maxisgetit.response.CompanyListResponse;
import com.kelltontech.maxisgetit.response.DealsListResponse;
import com.kelltontech.maxisgetit.response.LocalSearchResponse;
import com.kelltontech.maxisgetit.response.UserDetailResponse;
import com.kelltontech.maxisgetit.ui.widgets.CustomDialog;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class MyAccountActivity extends MaxisMainActivity {
	private ImageView mHomeIconView, mProfileIconView;
	private UserDetailResponse mUserDetail;
	private RadioGroup mLangContainerRG;
	private TextView mEdWelcomeUser;
	private TextView mEdEmail;
	private TextView mEdMobile;
	private TextView mTxtEmailStatus;
	private TextView mTxtMobileStatus;
	private boolean mIsCurrentlyEngSelected;
	private MaxisStore mStore;
	private RadioButton mEnRadioButton;
	private RadioButton mMalayRadioButton;
	private ImageView mEditProfile, mLogout;
	private TextView mLocalSearch, mClassified, mBestPrice, mShortlisted,
			mManageAlert, mDeals;
	private LinearLayout mSearchContainer;
	private ImageView mSearchToggler;
	private ImageView mHeaderBackButton;
	private ImageView mSearchBtn;
	private EditText mSearchEditText;

	private CompanyListResponse mClResponse;
	private FavCompanyListRequest mListRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mStore = MaxisStore.getStore(MyAccountActivity.this);
		setContentView(R.layout.activity_my_account);
		AnalyticsHelper.logEvent(FlurryEventsConstants.APPLICATION_MY_ACCOUNT,
				true);
		mLocalSearch = (TextView) findViewById(R.id.amc_local_search);
		mLocalSearch.setOnClickListener(this);
		mClassified = (TextView) findViewById(R.id.amc_classified);
		mClassified.setOnClickListener(this);
		mBestPrice = (TextView) findViewById(R.id.amc_best_price);
		mBestPrice.setOnClickListener(this);
		mShortlisted = (TextView) findViewById(R.id.amc_shortlisted);
		mShortlisted.setOnClickListener(this);
		mManageAlert = (TextView) findViewById(R.id.amc_manage_alert);
		mManageAlert.setOnClickListener(this);
		mDeals = (TextView) findViewById(R.id.amc_deals);
		mDeals.setOnClickListener(this);
		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mUserDetail = getIntent().getParcelableExtra(
				AppConstants.USER_DETAIL_DATA);
		mLangContainerRG = (RadioGroup) findViewById(R.id.amc_lang_switcher);
		mEdWelcomeUser = (TextView) findViewById(R.id.amc_name);
		mEdEmail = (TextView) findViewById(R.id.amc_email);
		mTxtEmailStatus = (TextView) findViewById(R.id.amc_email_status);
		mTxtEmailStatus.setOnClickListener(this);

		mEdMobile = (TextView) findViewById(R.id.amc_mobile);
		mTxtMobileStatus = (TextView) findViewById(R.id.amc_mobile_status);
		mEnRadioButton = (RadioButton) findViewById(R.id.amc_lang_en);
		mMalayRadioButton = (RadioButton) findViewById(R.id.amc_lang_ms);
		mIsCurrentlyEngSelected = mStore.isEnglishSelected();
		if (mIsCurrentlyEngSelected) {
			mEnRadioButton.setChecked(true);
		} else
			mMalayRadioButton.setChecked(true);
		mLangContainerRG
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						showConfirmationDialog(
								CustomDialog.CONFIRMATION_DIALOG,
								"The Application will restart to perform the requested cheanges. Do you wish to continue?");
					}
				});
		if (mUserDetail != null) {
			mEdWelcomeUser.setText(getResources().getString(R.string.welcome)
					+ " " + mUserDetail.getName());
			if (mUserDetail.getEmail() != null
					&& !mUserDetail.getEmail().trim().equals("")) {
				mEdEmail.setText(mUserDetail.getEmail());
				if (mUserDetail.isVerifiedEmail()) {
					mTxtEmailStatus.setVisibility(View.VISIBLE);
					mTxtEmailStatus.setText(Html.fromHtml("<u>"
							+ getResources().getString(R.string.verified)
							+ "</u>"));
					mTxtEmailStatus.setTextColor(getResources().getColor(
							R.color.maxis_green));
				}
			} else {
				mTxtEmailStatus.setVisibility(View.INVISIBLE);
			}
			if (mUserDetail.getMobile() != null
					&& !mUserDetail.getMobile().trim().equals("")) {
				mEdMobile.setText(mUserDetail.getMobile());
				if (mUserDetail.isVerifiedMobile()) {
					mTxtMobileStatus.setText(Html.fromHtml("<u>"
							+ getResources().getString(R.string.verified)
							+ "</u>"));
					mTxtMobileStatus.setTextColor(getResources().getColor(
							R.color.maxis_green));
				}
			}
		}
		mEditProfile = (ImageView) findViewById(R.id.amc_edit_profile);
		mEditProfile.setOnClickListener(this);
		mLogout = (ImageView) findViewById(R.id.amc_logout);
		mLogout.setOnClickListener(this);

		mSearchContainer = (LinearLayout) findViewById(R.id.search_box_container);
		mSearchToggler = (ImageView) findViewById(R.id.search_toggler);
		mSearchToggler.setOnClickListener(this);
		mHeaderBackButton = (ImageView) findViewById(R.id.header_btn_back);
		mHeaderBackButton.setOnClickListener(this);
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		if (!StringUtil.isNullOrEmpty(mSearchKeyword))
			mSearchEditText.setText(mSearchKeyword);
	}

	@Override
	public void onPositiveDialogButton(int id) {
		if (id == CustomDialog.CONFIRMATION_DIALOG) {
			saveSettings();
		} else {
			super.onPositiveDialogButton(id);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			mEdWelcomeUser.setText(getResources().getString(R.string.welcome)
					+ " " + mStore.getUserName());
			mUserDetail.setName(mStore.getUserName());
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void saveSettings() {
		if (mLangContainerRG.getCheckedRadioButtonId() == R.id.as_lang_en) {
			mStore.setEnglishSelected(true);
		} else {
			mStore.setEnglishSelected(false);
		}
		setLocale(mStore.getLocaleCode());
	}

	public void setLocale(String lang) {
		Locale myLocale = new Locale(lang);
		Resources res = getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		Configuration conf = res.getConfiguration();
		conf.locale = myLocale;
		res.updateConfiguration(conf, dm);
		Intent refresh = new Intent(this, HomeActivity.class);
		refresh.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		refresh.putExtra(AppConstants.RESET, true);
		startActivity(refresh);
	}

	@Override
	public Activity getMyActivityReference() {
		return null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			Intent intentHome = new Intent(MyAccountActivity.this,
					HomeActivity.class);
			intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentHome);
			break;
		case R.id.show_profile_icon:
			break;
		case R.id.amc_edit_profile:
			AnalyticsHelper.logEvent(FlurryEventsConstants.EDIT_PROFILE_CLICK);
			Intent intentProfile = new Intent(MyAccountActivity.this,
					EditProfileActivity.class);
			intentProfile.putExtra(AppConstants.USER_DETAIL_DATA, mUserDetail);
			startActivityForResult(intentProfile, 0);
			break;
		case R.id.amc_local_search:
			AnalyticsHelper.logEvent(FlurryEventsConstants.MY_COMPANY_CLICK);
			if (mUserDetail.isCompany()) {
				LocalSearchController lSearchController = new LocalSearchController(
						MyAccountActivity.this, Events.LOCAL_SEARCH);
				startSppiner();
				lSearchController.requestService(mUserDetail.getMobile());
			} else {
				showInfoDialog("You are not authenticated to perform this Action");
			}
			break;
		case R.id.amc_classified:
			AnalyticsHelper
					.logEvent(FlurryEventsConstants.MY_CLASSIFIEDS_CLICK);
			ClassifiedListController clController = new ClassifiedListController(
					MyAccountActivity.this, Events.CLASSIFIED_LIST);
			startSppiner();
			clController.requestService(mUserDetail.getUserId());
			break;
		case R.id.amc_manage_alert:
			showAlertDialog(getResources().getString(R.string.under_implement));
			break;
		case R.id.amc_deals:
			AnalyticsHelper.logEvent(FlurryEventsConstants.MY_DEALS_CLICK);
			if (mUserDetail.isCompany()) {
				MyDealsController mdController = new MyDealsController(
						MyAccountActivity.this, Events.MY_DEALS_LIST);
				startSppiner();
				mdController.requestService(mUserDetail.getUserId());
			} else {
				showInfoDialog("You are not authenticated to perform this Action");
			}
			break;
		case R.id.amc_shortlisted:
			// showAlertDialog(getResources().getString(R.string.under_implement));
			FavouriteController mfController = new FavouriteController(
					MyAccountActivity.this, Events.FAV_COMP_LIST);
			startSppiner();
			mListRequest = new FavCompanyListRequest();
			mListRequest.setUserId(mUserDetail.getUserId());

			mfController.requestService(mListRequest);

			break;
		case R.id.amc_best_price:
			showAlertDialog(getResources().getString(R.string.under_implement));
			break;
		case R.id.amc_logout:
			mStore.setLoggedInUser(false);
			mStore.setVerifiedUser(false);
			mStore.setUserRegistered(false);
			mStore.setUserEmailId("");
			mStore.setUserID("");
			AnalyticsHelper.logEvent(FlurryEventsConstants.LOGOUT_CLICK);
			AnalyticsHelper
					.endTimedEvent(FlurryEventsConstants.APPLICATION_MY_ACCOUNT);
			finish();
			// showFinalDialog("Logout successful");
			break;
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
			AnalyticsHelper
					.endTimedEvent(FlurryEventsConstants.APPLICATION_MY_ACCOUNT);
			this.finish();
			break;
		case R.id.search_icon_button:
			mSearchEditText
					.setText(mSearchEditText.getText().toString().trim());
			performSearch(mSearchEditText.getText().toString());
			break;
		case R.id.amc_email_status:
			checkEmailAndPreferenceBeforeOpenBrowser();
			break;
		default:
			break;
		}

	}

	private void checkEmailAndPreferenceBeforeOpenBrowser() {
		if (!mUserDetail.isVerifiedEmail()) {
			if (!StringUtil.isNullOrEmpty(mUserDetail.getEmail())) {
				String domain = mUserDetail.getEmail().substring(
						mUserDetail.getEmail().indexOf("@") + 1);
				;
				String url = getResources().getString(R.string.domain_prefix)
						+ domain;
				if (url.indexOf(getString(R.string.findit_com)) == -1) {
					checkPreferenceAndOpenBrowser(url);
				} else {
					openDeviceBrowser(url, true);
				}
				AnalyticsHelper
						.logEvent(FlurryEventsConstants.VERIFY_EMAIL_CLICK);
			}
		}
	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| msg.arg2 == Events.USER_DETAIL) {
			super.updateUI(msg);
		} else if (msg.arg2 == Events.LOCAL_SEARCH) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				Intent intent = new Intent(MyAccountActivity.this,
						LocalSearchActivity.class);
				intent.putExtra(AppConstants.LOCAL_SEARCH_RESPONSE,
						(LocalSearchResponse) msg.obj);
				startActivity(intent);
			}
			stopSppiner();
		} else if (msg.arg2 == Events.CLASSIFIED_LIST) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				Intent intent = new Intent(MyAccountActivity.this,
						ClassifiedScrollActivity.class);
				intent.putExtra(AppConstants.CLASSIFIED_LIST_RESPONSE,
						(ClassifiedListResponse) msg.obj);
				startActivity(intent);
			}
			stopSppiner();
		} else if (msg.arg2 == Events.MY_DEALS_LIST) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				Intent intent = new Intent(MyAccountActivity.this,
						MyDealsActivity.class);
				intent.putExtra(AppConstants.MY_DEALS_RESPONSE,
						(DealsListResponse) msg.obj);
				intent.putExtra(AppConstants.USER_ID, mUserDetail.getUserId());
				startActivity(intent);
			}
			stopSppiner();
		} else if (msg.arg2 == Events.FAV_COMP_LIST) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				Intent intent = new Intent(MyAccountActivity.this,
						FavCompanyListActivity.class);
				intent.putExtra(AppConstants.COMP_LIST_DATA, mClResponse);
				intent.putExtra("UserID", mUserDetail.getUserId());
				// intent.putExtra(AppConstants.DATA_LIST_REQUEST,
				// mListRequest);
				startActivity(intent);

			}
			stopSppiner();
		}
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| event == Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		} else if (event == Events.LOCAL_SEARCH) {
			handler.sendMessage((Message) screenData);
		} else if (event == Events.CLASSIFIED_LIST) {
			handler.sendMessage((Message) screenData);
		} else if (event == Events.MY_DEALS_LIST) {
			handler.sendMessage((Message) screenData);
		} else if (event == Events.FAV_COMP_LIST) {
			Response response = (Response) screenData;
			Message message = new Message();
			message.arg2 = event;
			message.arg1 = 1;
			if (response.isError()) {
				message.obj = response.getErrorText() + " "
						+ response.getErrorCode();
			} else {
				if (response.getPayload() instanceof CompanyListResponse) {
					mClResponse = (CompanyListResponse) response.getPayload();
					if (mClResponse.getErrorCode() != 0) {
						message.obj = getResources().getString(
								R.string.communication_failure);
						// clResponse.getServerMessage() + " " +
						// clResponse.getErrorCode();
						Log.e("maxis", "payload:" + response.getPayload());

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

	}
}
