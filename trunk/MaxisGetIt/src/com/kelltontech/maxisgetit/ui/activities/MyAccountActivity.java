package com.kelltontech.maxisgetit.ui.activities;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
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
import com.kelltontech.maxisgetit.controllers.MyAccountDashboardController;
import com.kelltontech.maxisgetit.controllers.MyDealsController;
import com.kelltontech.maxisgetit.dao.CityOrLocality;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import com.kelltontech.maxisgetit.model.CatDetails;
import com.kelltontech.maxisgetit.model.MyAccountDashboardResponse;
import com.kelltontech.maxisgetit.model.MyAccountLifecycleResponse;
import com.kelltontech.maxisgetit.requests.FavCompanyListRequest;
import com.kelltontech.maxisgetit.response.ClassifiedListResponse;
import com.kelltontech.maxisgetit.response.CompanyListResponse;
import com.kelltontech.maxisgetit.response.DealsListResponse;
import com.kelltontech.maxisgetit.response.GenralListResponse;
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

	private boolean isAdvanceSearchLayoutOpen = false;
	private LinearLayout advanceSearchLayout;
	private TextView currentCity, currentLocality;
	private ImageView upArrow;
	private ArrayList<String> cityListString = new ArrayList<String>();
	private ArrayList<String> localityItems;
	ArrayList<CityOrLocality> cityList;
	private String selectedCity = "Entire Malaysia";
	private int city_id = -1;

	private ArrayList<String> selectedLocalityItems;
	ArrayList<CityOrLocality> localityList;
	ArrayList<String> ids = new ArrayList<String>();
	TextView mainSearchButton;
	ArrayList<String> selectedLocalityindex;
	LinearLayout wholeSearchBoxContainer;
	private TextView categoryChooser;
	private ArrayAdapter<String> mCategoryAdapter;
	private ArrayAdapter<String> mCompanyAdapter;
	private ArrayList<String> mCompany;
	private ArrayList<CatDetails> catDetails;
	private ArrayList<String> mCategory;
	private MyAccountDashboardResponse response;
	private MyAccountLifecycleResponse lifecycleResponse;
	private LinearLayout dashboardLayout;
	private String catId, compId;
	private String selectedCompany = "";
	private String selectedCategory = "";
	private int companyPos = 0;
	private int categoryPos = 0;
	TextView totalViews;
	TextView totalLeads;
	TextView myTotalViews;
	TextView myActiveVirtualNo;
	TextView myPlan;
	TextView leadCost;
	TextView companyChooser;
	TextView lifeCycle;
	//	TextView viewLifeCycle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mStore = MaxisStore.getStore(MyAccountActivity.this);
		setContentView(R.layout.activity_my_account);
		AnalyticsHelper.logEvent(FlurryEventsConstants.APPLICATION_MY_ACCOUNT, true);
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
		dashboardLayout = (LinearLayout) findViewById(R.id.dashboardLayout);
		dashboardLayout.setVisibility(View.GONE);

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
						"The Application will restart to perform the requested changes. Do you wish to continue?");
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
		// if (!StringUtil.isNullOrEmpty(mSearchKeyword))
		// mSearchEditText.setText(mSearchKeyword);

		advanceSearchLayout = (LinearLayout) findViewById(R.id.advanceSearch);
		advanceSearchLayout.setVisibility(View.GONE);

		upArrow = (ImageView) findViewById(R.id.upArrow);
		upArrow.setOnClickListener(this);

		currentCity = (TextView) findViewById(R.id.currentCity);
		currentLocality = (TextView) findViewById(R.id.currentLocality);
		currentCity.setText(Html
				.fromHtml("in " + "<b>" + selectedCity + "</b>"));

		currentCity.setOnClickListener(this);
		currentLocality.setOnClickListener(this);

		mainSearchButton = (TextView) findViewById(R.id.mainSearchButton);
		mainSearchButton.setOnClickListener(this);

		wholeSearchBoxContainer = (LinearLayout) findViewById(R.id.whole_search_box_container);
		mSearchEditText.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (!isAdvanceSearchLayoutOpen) {
					isAdvanceSearchLayoutOpen = true;
					advanceSearchLayout.setVisibility(View.VISIBLE);
				}
				return false;
			}
		});

		getDashboardData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		AnalyticsHelper.trackSession(MyAccountActivity.this, AppConstants.MyAccount);
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
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == 0) {
			mEdWelcomeUser.setText(getResources().getString(R.string.welcome)
					+ " " + mStore.getUserName());
			mUserDetail.setName(mStore.getUserName());
		} else if (resultCode == RESULT_OK
				&& requestCode == AppConstants.CITY_REQUEST) {
			if (!selectedCity
					.equalsIgnoreCase(data.getStringExtra("CITY_NAME"))) {
				localityItems = null;
				ids = null;
				selectedLocalityindex = null;
				currentLocality.setText("Choose your Area");
			}
			selectedCity = data.getStringExtra("CITY_NAME");
			currentCity.setText(Html.fromHtml("in " + "<b>" + selectedCity
					+ "</b>"));
			int index = data.getIntExtra("CITY_INDEX", 0);
			if (index == -1) {
				city_id = -1;
			} else {
				city_id = cityList.get(index).getId();
			}
		} else if (resultCode == RESULT_OK
				&& requestCode == AppConstants.LOCALITY_REQUEST) {
			String locality = "";

			selectedLocalityItems = data
					.getStringArrayListExtra("SELECTED_LOCALITIES");

			selectedLocalityindex = data
					.getStringArrayListExtra("SELECTED_LOCALITIES_INDEX");
			if (selectedLocalityItems != null
					&& selectedLocalityItems.size() > 0) {
				for (int i = 0; i < selectedLocalityItems.size(); i++) {

					if (i == selectedLocalityItems.size() - 1) {
						locality += selectedLocalityItems.get(i);
					} else {
						locality += selectedLocalityItems.get(i) + ",";
					}
				}
				currentLocality.setText(Html.fromHtml("Your Selected Area "
						+ "<b>" + locality + "</b>"));
			} else {
				currentLocality.setText("Choose your Area");
			}

			ids = new ArrayList<String>();

			if (selectedLocalityindex != null
					&& selectedLocalityindex.size() > 0) {
				for (int i = 0; i < selectedLocalityindex.size(); i++) {

					ids.add(String.valueOf(localityList.get(
							Integer.parseInt(selectedLocalityindex.get(i)))
							.getId()));
				}
			}

		} else if (resultCode == RESULT_OK && requestCode == 10) {
			selectedCompany = data.getStringExtra("ChosenCompanyOrCategory");
			companyPos = data.getIntExtra("Position", 0);
			setCompanyData();
//			companyChooser.setText(mCompany.get(companyPos));
//			catDetails = response.getData().get(companyPos).getCatdetails();
//			categoryChooser.setText(catDetails.get(0).getCatname());
//			mCategory = new ArrayList<String>();
//			for (int i = 0; i < catDetails.size(); i++) {
//				mCategory.add(catDetails.get(i).getCatname());
//			}
//
//			if (selectedCompany.trim().equals(
//					response.getData().get(companyPos).getCname().trim())) {
//				compId = response.getData().get(companyPos).getCid();
//			}
		} else if (resultCode == RESULT_OK && requestCode == 11) {
			selectedCategory = data.getStringExtra("ChosenCompanyOrCategory");
			categoryPos = data.getIntExtra("Position", 0);

			setCategoryData();
			// companyChooser.setText(mCompany.get(companyPos));
			// catDetails = response.getData().get(companyPos).getCatdetails();
//			categoryChooser.setText(catDetails.get(categoryPos).getCatname());
//
//			totalViews.setText(catDetails.get(categoryPos).getTotalView());
//			totalLeads.setText(catDetails.get(categoryPos).getTotalLeads());
//			myTotalViews.setText(catDetails.get(categoryPos).getMyTotalView());
//			myActiveVirtualNo.setText(catDetails.get(categoryPos)
//					.getMaxisDidNumber());
//			if (catDetails!= null && catDetails.get(categoryPos)!=null && StringUtil.isNullOrEmpty(catDetails.get(categoryPos).getMaxisDidNumber())) { 
//				lifeCycle.setVisibility(View.GONE);
//			} else {
//				lifeCycle.setVisibility(View.VISIBLE);
//			}
//			myPlan.setText(catDetails.get(categoryPos).getSubscription());
//			leadCost.setText(catDetails.get(categoryPos).getPrice());
//
//			if (selectedCategory.trim().equals(
//					response.getData().get(companyPos).getCatdetails()
//					.get(categoryPos).getCatname().trim())) {
//				catId = response.getData().get(companyPos).getCatdetails()
//						.get(categoryPos).getCatid();
//			}
		}

	}

	private void setCompanyData () {
		companyChooser.setText(mCompany.get(companyPos).trim());
		catDetails = response.getData().get(companyPos).getCatdetails();
		categoryChooser.setText(catDetails.get(0).getCatname().trim());
		mCategory = new ArrayList<String>();
		for (int i = 0; i < catDetails.size(); i++) {
			mCategory.add(catDetails.get(i).getCatname());
		}

		if (selectedCompany.trim().equals(
				response.getData().get(companyPos).getCname().trim())) {
			compId = response.getData().get(companyPos).getCid();
		}
		categoryPos = 0;
		setCategoryData();
	}
	
	private void setCategoryData() {
		categoryChooser.setText(catDetails.get(categoryPos).getCatname().trim());

		totalViews.setText(catDetails.get(categoryPos).getTotalView().trim());

		
		if (catDetails!= null && catDetails.get(categoryPos)!=null && (StringUtil.isNullOrEmpty(catDetails.get(categoryPos).getTotalLeads()) || (catDetails.get(categoryPos).getTotalLeads().equals("N.A.")))) { 
			totalLeads.setText("0");
		} else {
			totalLeads.setText(catDetails.get(categoryPos).getTotalLeads().trim());
		}
		
		myTotalViews.setText(catDetails.get(categoryPos).getMyTotalView().trim());
		myActiveVirtualNo.setText(catDetails.get(categoryPos)
				.getMaxisDidNumber().trim());
		if (catDetails!= null && catDetails.get(categoryPos)!=null && (StringUtil.isNullOrEmpty(catDetails.get(categoryPos).getMaxisDidNumber()) || (catDetails.get(categoryPos).getMaxisDidNumber().equals("N.A.")))) { 
			lifeCycle.setVisibility(View.GONE);
		} else {
			lifeCycle.setVisibility(View.VISIBLE);
		}
		myPlan.setText(catDetails.get(categoryPos).getSubscription());
		leadCost.setText(catDetails.get(categoryPos).getPrice().trim());

		if (selectedCategory.trim().equals(
				response.getData().get(companyPos).getCatdetails()
				.get(categoryPos).getCatname().trim())) {
			catId = response.getData().get(companyPos).getCatdetails()
					.get(categoryPos).getCatid();
		}
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
			showHomeScreen();
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
			AnalyticsHelper.logEvent(FlurryEventsConstants.MY_FAVOURITE_CLICK);
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
			if (wholeSearchBoxContainer.getVisibility() == View.VISIBLE) {
				wholeSearchBoxContainer.setVisibility(View.GONE);
			} else {
				wholeSearchBoxContainer.setVisibility(View.VISIBLE);
			}
			if (mSearchContainer.getVisibility() == View.VISIBLE) {
				mSearchContainer.setVisibility(View.GONE);
			} else {
				mSearchContainer.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.header_btn_back:
			AnalyticsHelper.logEvent(FlurryEventsConstants.BACK_CLICK);
			AnalyticsHelper.endTimedEvent(FlurryEventsConstants.APPLICATION_MY_ACCOUNT);
			this.finish();
			break;
		case R.id.mainSearchButton:
			mSearchEditText.setText(mSearchEditText.getText().toString().trim());
			String JSON_EXTRA = jsonForSearch();
			performSearch(mSearchEditText.getText().toString(), JSON_EXTRA);
			break;
		case R.id.amc_email_status:
			checkEmailAndPreferenceBeforeOpenBrowser();
			break;
		case R.id.upArrow:
			if (isAdvanceSearchLayoutOpen) {
				isAdvanceSearchLayoutOpen = false;
				advanceSearchLayout.setVisibility(View.GONE);
			}
			break;
		case R.id.currentCity:
			if (cityListString != null && cityListString.size() > 0) {
				localityItems = null;
				selectedLocalityindex = null;
				Intent cityIntent = new Intent(MyAccountActivity.this,AdvanceSelectCity.class);
				cityIntent.putExtra("CITY_LIST", cityListString);
				cityIntent.putExtra("SELECTED_CITY", selectedCity);
				startActivityForResult(cityIntent, AppConstants.CITY_REQUEST);
			} else {
				setSearchCity();
			}
			break;

		case R.id.currentLocality:
			if (localityItems != null && localityItems.size() > 0) {
				Intent localityIntent = new Intent(MyAccountActivity.this,
						AdvanceSelectLocalityActivity.class);
				localityIntent.putExtra("LOCALITY_LIST", localityItems);
				localityIntent.putStringArrayListExtra("LOCALITY_INDEX", selectedLocalityindex);
				startActivityForResult(localityIntent, AppConstants.LOCALITY_REQUEST);
			} else {
				setSearchLocality(city_id);
			}
			break;
		case R.id.life_cycle:
			Intent lifeCycleIntent = new Intent(MyAccountActivity.this, LifeCycleViewActivity.class);
			String lifeCycleURL = AppConstants.LIFE_CYCLE_WEBVIEW_SERVER  + compId + "/" + catId;
//			String lifeCycleURL = "http://192.168.13.117/FMS_WAP/wap/trunk/users/didLifeCycleApp/" + compId + "/" + catId;
			if (!StringUtil.isNullOrEmpty(lifeCycleURL)) {
				lifeCycleIntent.putExtra(AppConstants.LIFE_CYCLE_URL, lifeCycleURL);
				startActivityForResult(lifeCycleIntent,
						AppConstants.LIFE_CYCLE_SCREEN);
			}
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
		} else if (msg.arg2 == Events.CITY_LISTING) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				GenralListResponse glistRes = (GenralListResponse) msg.obj;
				cityList = glistRes.getCityOrLocalityList();
				Intent intent = new Intent(MyAccountActivity.this,
						AdvanceSelectCity.class);
				for (CityOrLocality cityOrLocality : cityList) {

					cityListString.add(cityOrLocality.getName());
				}
				localityItems = null;
				ids = null;
				selectedLocalityindex = null;
				currentLocality.setText("Choose your Area");
				intent.putExtra("CITY_LIST", cityListString);
				intent.putExtra("SELECTED_CITY", selectedCity);
				startActivityForResult(intent, AppConstants.CITY_REQUEST);
			}
			stopSppiner();
		} else if (msg.arg2 == Events.LOCALITY_LISTING) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				GenralListResponse glistRes = (GenralListResponse) msg.obj;
				localityList = glistRes.getCityOrLocalityList();
				Intent intent = new Intent(MyAccountActivity.this,
						AdvanceSelectLocalityActivity.class);
				localityItems = new ArrayList<String>();
				for (CityOrLocality dealCityOrLoc : localityList) {
					localityItems.add(dealCityOrLoc.getName());
				}
				intent.putExtra("LOCALITY_LIST", localityItems);
				intent.putStringArrayListExtra("LOCALITY_INDEX",
						selectedLocalityindex);
				startActivityForResult(intent, AppConstants.LOCALITY_REQUEST);

			}
		} else if (msg.arg2 == Events.MY_ACCOUNT_DASHBOARD) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
				dashboardLayout.setVisibility(View.GONE);
			} else {
				response = (MyAccountDashboardResponse) msg.obj;
				mCompany = new ArrayList<String>();
				if (response != null && response.getData() != null) {
					if (response.getData().size() > 0) {
						for (int i = 0; i < response.getData().size(); i++) {
							mCompany.add(response.getData().get(i).getCname());
						}
						setDashboardData();
					}
				}
			}
			stopSppiner();
		} else if (msg.arg2 == Events.MY_ACCOUNT_DASHBOARD_LIFECYCLE) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);

			} else {

				lifecycleResponse = (MyAccountLifecycleResponse) msg.obj;
				if (lifecycleResponse != null
						&& lifecycleResponse.getData() != null) {

					Intent intent = new Intent(MyAccountActivity.this,
							DidLifeCycleActivity.class);
					intent.putExtra("lifeCycleResponse", lifecycleResponse);
				}
				Log.e("manish", ":" + msg.obj);
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
		} else if (event == Events.MY_ACCOUNT_DASHBOARD) {
			MyAccountDashboardResponse dashboardResponse = (MyAccountDashboardResponse) screenData;
			Message message = new Message();
			message.arg2 = event;

			if (dashboardResponse.getError_code() == "1") {
				message.arg1 = 1;
				message.obj = getResources().getString(
						R.string.communication_failure);
			} else {
				message.arg1 = 0;
				message.obj = dashboardResponse;
			}
			handler.sendMessage(message);
		} else if (event == Events.MY_ACCOUNT_DASHBOARD_LIFECYCLE) {
			MyAccountLifecycleResponse accountLifecycleResponse = (MyAccountLifecycleResponse) screenData;
			Message message = new Message();
			message.arg2 = event;

			if (accountLifecycleResponse.getError_code() == "1") {
				message.arg1 = 1;
				message.obj = getResources().getString(
						R.string.communication_failure);
			} else {
				message.arg1 = 0;
				message.obj = accountLifecycleResponse;
			}
			handler.sendMessage(message);
		}

		else {
			handler.sendMessage((Message) screenData);
		}

	}

	public String jsonForSearch() {

		// {"city":{"city_id":5,"city_name":"adyui"},"locality":[{"locality_id":5,"locality_name":"adyui"},{"locality_id":5,"locality_name":"adyui"}]}
		JSONObject jArray = new JSONObject();
		try {

			if (city_id != -1) {
				JSONObject array = new JSONObject();
				array.put("city_id", city_id + "");
				array.put("city_name", selectedCity);

				jArray.put("city", array);

				if (ids != null && ids.size() > 0) {
					JSONArray jsonArray = new JSONArray();
					for (int i = 0; i < selectedLocalityItems.size(); i++) {
						JSONObject localityArray = new JSONObject();
						localityArray.put("locality_id", ids.get(i));
						localityArray.put("locality_name",
								selectedLocalityItems.get(i));
						jsonArray.put(localityArray);

					}
					jArray.put("locality", jsonArray);
				}
				return jArray.toString();

			}

			else {
				return null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void getDashboardData() {
		MyAccountDashboardController dashboardController = new MyAccountDashboardController(
				MyAccountActivity.this, Events.MY_ACCOUNT_DASHBOARD);
		startSppiner();
		mListRequest = new FavCompanyListRequest();
		mListRequest.setUserId(mUserDetail.getUserId());
		dashboardController.requestService(mListRequest);
	}

	public void setDashboardData() {

		dashboardLayout.setVisibility(View.VISIBLE);
		companyChooser = (TextView) findViewById(R.id.company_spinner);
		categoryChooser = (TextView) findViewById(R.id.category_spinner);
		totalViews = (TextView) findViewById(R.id.total_views);
		totalLeads = (TextView) findViewById(R.id.total_leads);
		myTotalViews = (TextView) findViewById(R.id.my_total_views);
		myActiveVirtualNo = (TextView) findViewById(R.id.my_active_virtual_no);
		myPlan = (TextView) findViewById(R.id.my_plan);
		leadCost = (TextView) findViewById(R.id.lead_cost);
		//		viewLifeCycle = (TextView) findViewById(R.id.life_cycle);
		lifeCycle = (TextView) findViewById(R.id.life_cycle);
		lifeCycle.setOnClickListener(this);

		// mCompanyAdapter = new ArrayAdapter<String>(this,
		// R.layout.spinner_item,
		// mCompany);
		// companyChooser.setAdapter(mCompanyAdapter);

		companyChooser.setText(mCompany.get(0));
		categoryChooser.setText(response.getData().get(0).getCatdetails()
				.get(0).getCatname());

		companyChooser.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intentCompSelector = new Intent(MyAccountActivity.this,
						CompanyOrCategoryFilterActivity.class);
				intentCompSelector.putStringArrayListExtra("compOrcat",
						mCompany);
				startActivityForResult(intentCompSelector, 10);

			}
		});

		// companyChooser.setOnItemSelectedListener(new OnItemSelectedListener()
		// {
		//
		// @Override
		// public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
		// long arg3) {
		// mCategory = new ArrayList<String>();
		// for (int i = 0; i < response.getData().get(pos).getCatdetails()
		// .size(); i++) {
		// mCategory.add(response.getData().get(pos).getCatdetails()
		// .get(i).getCatname());
		//
		// }
		// catDetails = response.getData().get(pos).getCatdetails();
		// mCategoryAdapter = new ArrayAdapter<String>(MyAccountActivity.this,
		// R.layout.spinner_item, mCategory);
		// categoryChooser.setAdapter(mCategoryAdapter);
		//
		// Intent intentCompSelector = new Intent(MyAccountActivity.this,
		// CompanyOrCategoryFilterActivity.class);
		// intentCompSelector.putStringArrayListExtra("compOrcat", mCompany);
		// startActivityForResult(intentCompSelector, 10);
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> arg0) {
		// }
		// });

		categoryChooser.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intentCompSelector = new Intent(MyAccountActivity.this,
						CompanyOrCategoryFilterActivity.class);
				intentCompSelector.putStringArrayListExtra("compOrcat",
						mCategory);
				startActivityForResult(intentCompSelector, 11);

			}
		});

		setCompanyData();
		setCategoryData();
		// categoryChooser.setOnItemSelectedListener(new
		// OnItemSelectedListener() {
		//
		// @Override
		// public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
		// long arg3) {
		// totalViews.setText(catDetails.get(pos).getTotalView());
		// totalLeads.setText(catDetails.get(pos).getTotalLeads());
		// myTotalViews.setText(catDetails.get(pos).getMyTotalView());
		// myActiveVirtualNo.setText(catDetails.get(pos)
		// .getMaxisDidNumber());
		// myPlan.setText(catDetails.get(pos).getSubscription());
		// leadCost.setText(catDetails.get(pos).getPrice());
		// catId = catDetails.get(pos).getCatid();
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> arg0) {
		// }
		// });

		/*		viewLifeCycle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Reusing PostReviewRequest here..
				PostReviewRequest request = new PostReviewRequest();
				request.setCatId(catId);
				request.setCompId(compId);

				MyDashboardLifeCycleController controller = new MyDashboardLifeCycleController(
						MyAccountActivity.this,
						Events.MY_ACCOUNT_DASHBOARD_LIFECYCLE);

				startSppiner();
				controller.requestService(request);

			}
		});*/
	}
}
