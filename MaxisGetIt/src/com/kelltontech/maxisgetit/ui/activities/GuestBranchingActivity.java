package com.kelltontech.maxisgetit.ui.activities;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kelltontech.framework.db.MyApplication;
import com.kelltontech.framework.utils.NativeHelper;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.LoginController;
import com.kelltontech.maxisgetit.dao.CityOrLocality;
import com.kelltontech.maxisgetit.dao.CompanyDetail;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import com.kelltontech.maxisgetit.dao.SubCategory;
import com.kelltontech.maxisgetit.db.CityTable;
import com.kelltontech.maxisgetit.db.FavCompanysTable;
import com.kelltontech.maxisgetit.requests.LoginRequest;
import com.kelltontech.maxisgetit.response.ControlDetailResponse;
import com.kelltontech.maxisgetit.response.GenralListResponse;
import com.kelltontech.maxisgetit.response.UserDetailResponse;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;
import com.kelltontech.maxisgetit.utils.Utility;

public class GuestBranchingActivity extends MaxisMainActivity {

	private ImageView mHomeIconView, mProfileIconView;
	private TextView mLoginLink, mRegisterLink, mGuestLink;
	private SubCategory mSelectedCategory;
	private ControlDetailResponse mTempletData;
	private MaxisStore mStore;
	private EditText mEdPasswd, mEdPhone;
	private TextView mLoginView;
	private TextView mSignUpBtn;
	private String mMobileNumber;
	private String mPassword;
	private LinearLayout mSearchContainer;
	private RelativeLayout mGuestSeparator;
	private TextView mGuestUsrBtn;
	private TextView mLoginText;
	private ImageView mSearchToggler;
	private ImageView mHeaderBackButton;
	private ImageView mSearchBtn;
	private EditText mSearchEditText;
	private int mActionType;
	private boolean isFromDetailScreen;
	private boolean isFromCompDetailAddFav;
	private boolean isForError;
	private CompanyDetail mCompanyDetail;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		AnalyticsHelper.logEvent(FlurryEventsConstants.APPLICATION_LOGIN, true);
		mGuestSeparator = (RelativeLayout) findViewById(R.id.separator_layout);
		mGuestUsrBtn = (TextView) findViewById(R.id.login_guest_user_button);
		mLoginText = (TextView) findViewById(R.id.login_txt_info);
		mGuestUsrBtn.setOnClickListener(this);

		Bundle bundle = getIntent().getExtras();
		isFromDetailScreen = bundle.getBoolean(AppConstants.IS_FROM_DETAIL);
		isFromCompDetailAddFav = bundle
				.getBoolean(AppConstants.IS_FROM_COMP_DETAIL_ADD_FAV);
		isForError = bundle.getBoolean(AppConstants.IS_FOR_ERROR_LOG);

		if (isForError == true) {
			mLoginText
					.setText(getString(R.string.we_suggest_to_login_error_log));
		} else {
			mLoginText.setText(getString(R.string.we_suggest_to_login));
		}

		mStore = MaxisStore.getStore(GuestBranchingActivity.this);
		if (isFromDetailScreen) {
			mActionType = AppConstants.ACTION_REPORT_ERROR;
			mCompanyDetail = bundle
					.getParcelable(AppConstants.COMP_DETAIL_DATA);
		} else if (isFromCompDetailAddFav) {
			mGuestSeparator.setVisibility(View.GONE);
			mGuestUsrBtn.setVisibility(View.GONE);
			mActionType = AppConstants.ACTION_ADD_FAV;
			// mCompanyDetail =
			// bundle.getParcelable(AppConstants.COMP_DETAIL_DATA);

		} else {
			mActionType = AppConstants.ACTION_SELL_POST;
			mSelectedCategory = bundle
					.getParcelable(AppConstants.SELECTED_CAT_DATA);
			mTempletData = bundle.getParcelable(AppConstants.TEMPLET_DATA);
		}
		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mEdPasswd = (EditText) findViewById(R.id.login_pass);
		mEdPhone = (EditText) findViewById(R.id.login_mobile);
		String phNumber = NativeHelper
				.getMy10DigitPhoneNumber(GuestBranchingActivity.this);
		if (phNumber != null) {
			mEdPhone.setText(phNumber);
		}
		mLoginView = (TextView) findViewById(R.id.login_button);
		mLoginView.setOnClickListener(this);
		mSignUpBtn = (TextView) findViewById(R.id.signup_button);
		mSignUpBtn.setOnClickListener(this);
		MaxisStore store = MaxisStore.getStore(GuestBranchingActivity.this);
		if (store.isRegisteredUser()) {
			mEdPhone.setText(store.getUserMobileNumberToDispaly());
		}

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

		findViewById(R.id.login_forget_password).setOnClickListener(this);

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
				// TODO Auto-generated method stub

				if (!isAdvanceSearchLayoutOpen) {
					isAdvanceSearchLayoutOpen = true;
					advanceSearchLayout.setVisibility(View.VISIBLE);
				}
				return false;
			}
		});

		/*
		 * mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		 * mHomeIconView.setOnClickListener(this); mProfileIconView =
		 * (ImageView) findViewById(R.id.show_profile_icon);
		 * mProfileIconView.setOnClickListener(this); mLoginLink = (TextView)
		 * findViewById(R.id.agb_login_link);
		 * mLoginLink.setOnClickListener(this); mRegisterLink = (TextView)
		 * findViewById(R.id.agb_register_link);
		 * mRegisterLink.setOnClickListener(this); mGuestLink = (TextView)
		 * findViewById(R.id.agb_guest_link);
		 * mGuestLink.setOnClickListener(this);
		 */
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		AnalyticsHelper.trackSession(GuestBranchingActivity.this, AppConstants.Login_screen);
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
			AnalyticsHelper.logEvent(FlurryEventsConstants.SHOW_PROFILE_CLICK);
			onProfileClick();
			break;
		case R.id.agb_guest_link:
			if (!isFromDetailScreen) {
				Intent intent = new Intent(GuestBranchingActivity.this,
						SaleTempletActivity.class);
				intent.putExtra(AppConstants.TEMPLET_DATA, mTempletData);
				intent.putExtra(AppConstants.SELECTED_CAT_DATA,
						mSelectedCategory);
				startActivity(intent);
			} else {
				Intent intent = new Intent(GuestBranchingActivity.this,
						ReportErrorActivity.class);
				intent.putExtra(AppConstants.COMP_DETAIL_DATA, mCompanyDetail);
				intent.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD,
						mSearchKeyword);
				startActivity(intent);
			}
			AnalyticsHelper
					.endTimedEvent(FlurryEventsConstants.APPLICATION_LOGIN);
			finish();
			break;
		case R.id.agb_login_link:
			Intent intentLogin = new Intent(GuestBranchingActivity.this,
					LoginActivity.class);
			intentLogin.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intentLogin.putExtra(AppConstants.ACTION_IDENTIFIER,
					AppConstants.ACTION_SELL_POST);
			intentLogin.putExtra(AppConstants.TEMPLET_DATA, mTempletData);
			intentLogin.putExtra(AppConstants.SELECTED_CAT_DATA,
					mSelectedCategory);
			startActivity(intentLogin);
			break;
		case R.id.agb_register_link:
			Intent intentReg;
			/*
			 * if(mStore.isRegisteredUser() && !mStore.isVerifiedUser()){
			 * intentReg = new Intent(GuestBranchingActivity.this,
			 * VerifyRegistrationActivity.class); }else{ intentReg = new
			 * Intent(GuestBranchingActivity.this, RegistrationActivity.class);
			 * }
			 */
			intentReg = new Intent(GuestBranchingActivity.this,
					VerifyRegistrationActivity.class);
			intentReg.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intentReg.putExtra(AppConstants.ACTION_IDENTIFIER,
					AppConstants.ACTION_SELL_POST);
			intentReg.putExtra(AppConstants.TEMPLET_DATA, mTempletData);
			intentReg.putExtra(AppConstants.SELECTED_CAT_DATA,
					mSelectedCategory);
			startActivity(intentReg);
			break;
		case R.id.login_button:
			String mobile = mEdPhone.getText().toString();
			// if (mobile.length() < 10 || mobile.indexOf('+',1)!=-1 ||
			// (mobile.indexOf('+')!=-1 && mobile.length()<=10)) {
			if (mobile.length() <= 7 || mobile.length() >= 12 || mobile.startsWith("0")) {
				showAlertDialog(getString(R.string.invalid_mobile));
				return;
			}
			String passwd = mEdPasswd.getText().toString();
			if (passwd.indexOf(' ') != -1 || passwd.equals("")) {
				showAlertDialog("Please enter valid Password without blank spaces");
				return;
			}
			mMobileNumber = Utility.getMobileNoForWS(this, mobile);
			mPassword = passwd;
			login();
			break;
		case R.id.signup_button: {
			AnalyticsHelper
					.logEvent(FlurryEventsConstants.NEW_USER_REGISTER_NOW_CLICK);
			AnalyticsHelper
					.endTimedEvent(FlurryEventsConstants.APPLICATION_LOGIN);
			finish();
			Intent intentReg1;
			/*
			 * if(mStore.isRegisteredUser() && !mStore.isVerifiedUser()){
			 * intentReg1 = new Intent(GuestBranchingActivity.this,
			 * VerifyRegistrationActivity.class); }else{ intentReg1 = new
			 * Intent(GuestBranchingActivity.this, RegistrationActivity.class);
			 * }
			 */
			intentReg1 = new Intent(GuestBranchingActivity.this,
					RegistrationActivity.class);
			intentReg1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			if (mActionType == AppConstants.ACTION_SELL_POST) {
				intentReg1.putExtra(AppConstants.ACTION_IDENTIFIER,
						AppConstants.ACTION_SELL_POST);
				intentReg1.putExtra(AppConstants.TEMPLET_DATA, mTempletData);
				intentReg1.putExtra(AppConstants.SELECTED_CAT_DATA,
						mSelectedCategory);
			} else if (mActionType == AppConstants.ACTION_REPORT_ERROR) {
				intentReg1.putExtra(AppConstants.ACTION_IDENTIFIER,
						AppConstants.ACTION_REPORT_ERROR);
				intentReg1.putExtra(AppConstants.COMP_DETAIL_DATA,
						mCompanyDetail);
				intentReg1.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD,
						mSearchKeyword);
			} else if (mActionType == AppConstants.ACTION_ADD_FAV) {
				intentReg1.putExtra(AppConstants.ACTION_IDENTIFIER,
						AppConstants.ACTION_ADD_FAV);
				intentReg1.putExtra(AppConstants.IS_FROM_COMP_DETAIL_ADD_FAV,
						true);
				// intentReg1.putExtra(AppConstants.COMP_DETAIL_DATA,
				// mCompanyDetail);
				// intentReg1.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD,
				// mSearchKeyword);
			}
			startActivity(intentReg1);
			break;
		}
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
			AnalyticsHelper
					.endTimedEvent(FlurryEventsConstants.APPLICATION_LOGIN);
			this.finish();
			break;
		case R.id.login_guest_user_button:
			AnalyticsHelper
					.logEvent(FlurryEventsConstants.PROCEED_AS_GUEST_CLICK);
			if (!isFromDetailScreen) {
				Intent intent = new Intent(GuestBranchingActivity.this,
						SaleTempletActivity.class);
				intent.putExtra(AppConstants.TEMPLET_DATA, mTempletData);
				intent.putExtra(AppConstants.SELECTED_CAT_DATA,
						mSelectedCategory);
				startActivity(intent);
			} else {
				Intent intent = new Intent(GuestBranchingActivity.this,
						ReportErrorActivity.class);
				intent.putExtra(AppConstants.COMP_DETAIL_DATA, mCompanyDetail);
				intent.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD,
						mSearchKeyword);
				startActivity(intent);
			}
			AnalyticsHelper
					.endTimedEvent(FlurryEventsConstants.APPLICATION_LOGIN);
			finish();
			break;
		case R.id.mainSearchButton:
			mSearchEditText
					.setText(mSearchEditText.getText().toString().trim());

			String JSON_EXTRA = jsonForSearch();
			performSearch(mSearchEditText.getText().toString(), JSON_EXTRA, Events.COMBIND_LISTING_NEW_LISTING_PAGE);
			break;
		case R.id.login_forget_password:
			startActivity(new Intent(this, ForgetPasswordActivity.class));
			AnalyticsHelper
					.logEvent(FlurryEventsConstants.FORGOT_PASSWORD_CLICK);
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
				Intent cityIntent = new Intent(GuestBranchingActivity.this,
						AdvanceSelectCity.class);
				cityIntent.putExtra("CITY_LIST", cityListString);
				cityIntent.putExtra("SELECTED_CITY", selectedCity);
				startActivityForResult(cityIntent, AppConstants.CITY_REQUEST);
			} else {
				setSearchCity();
			}
			break;

		case R.id.currentLocality:
			if (localityItems != null && localityItems.size() > 0) {
				Intent localityIntent = new Intent(GuestBranchingActivity.this,
						AdvanceSelectLocalityActivity.class);
				localityIntent.putExtra("LOCALITY_LIST", localityItems);
				localityIntent.putStringArrayListExtra("LOCALITY_INDEX",
						selectedLocalityindex);
				startActivityForResult(localityIntent,
						AppConstants.LOCALITY_REQUEST);
			} else {
				setSearchLocality(city_id);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| msg.arg2 == Events.USER_DETAIL) {
			super.updateUI(msg);
		} else if (msg.arg2 == Events.LOGIN) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				stopSppiner();
				saveUserInfoWithFavCompanies((UserDetailResponse) msg.obj);
				navigateToNextScreen();
				return;
			}
			stopSppiner();
		} else if (msg.arg2 == Events.USER_DETAIL) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				Intent intent = new Intent(GuestBranchingActivity.this,
						MyAccountActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.putExtra(AppConstants.USER_DETAIL_DATA,
						(UserDetailResponse) msg.obj);
				saveUserInfo((UserDetailResponse) msg.obj);
				startActivity(intent);
				AnalyticsHelper
						.endTimedEvent(FlurryEventsConstants.APPLICATION_LOGIN);
				finish();
			}
		} else if (msg.arg2 == Events.CITY_LISTING) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				CityTable cityTable = new CityTable(
						(MyApplication) getApplication());
				GenralListResponse glistRes = (GenralListResponse) msg.obj;
				// cityTable.addCityList(glistRes.getCityOrLocalityList());
				cityList = glistRes.getCityOrLocalityList();
				// inflateCityList(cityList);
				Intent intent = new Intent(GuestBranchingActivity.this,
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
				Intent intent = new Intent(GuestBranchingActivity.this,
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
		}
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| event == Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		} else if (event == Events.LOGIN) {
			handler.sendMessage((Message) screenData);
		} else if (event == Events.USER_DETAIL) {
			handler.sendMessage((Message) screenData);
		} else if (event == Events.CITY_LISTING
				|| event == Events.LOCALITY_LISTING) {
			Message message = (Message) screenData;
			handler.sendMessage(message);
		}
	}

	private void login() {
		LoginController loginController = new LoginController(
				GuestBranchingActivity.this, Events.LOGIN);
		startSppiner();
		// loginController.requestService(mMobileNumber, mPassword);
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setMobile(mMobileNumber);
		loginRequest.setPassword(mPassword);
		loginController.requestService(loginRequest);
	}

	private void saveUserInfo(UserDetailResponse userdDetail) {
		MaxisStore store = MaxisStore.getStore(GuestBranchingActivity.this);
		store.setUserMobileNumber(userdDetail.getMobile().substring(1));
		store.setUserID(userdDetail.getUserId());
		store.setUserName(userdDetail.getName());
		store.setLoggedInUser(true);
		store.setUserCompany(userdDetail.isCompany());

		// Flurry Event for Logged_In user
		if (!StringUtil.isNullOrEmpty(store.getUserMobileNumber())
				&& !StringUtil.isNullOrEmpty(store.getUserName().trim())) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(FlurryEventsConstants.Logged_In_User_Mobile_No,
					store.getUserMobileNumber());
			map.put(FlurryEventsConstants.Logged_In_User_Name,
					store.getUserName());
			AnalyticsHelper.logEvent(FlurryEventsConstants.USER_LOGGED_IN, map,
					true);
		}
	}

	private void saveUserInfoWithFavCompanies(UserDetailResponse userdDetail) {
		saveUserInfo(userdDetail);
		FavCompanysTable favCompany = new FavCompanysTable(
				(MyApplication) getApplication());
		favCompany.deleteAll();
		favCompany.addFavCompaniesList(userdDetail.getCompanyIdCategoryId());
	}

	private void navigateToNextScreen() {
		switch (mActionType) {
		case AppConstants.ACTION_SELL_POST:
			Intent intent = new Intent(GuestBranchingActivity.this,
					SaleTempletActivity.class);
			intent.putExtra(AppConstants.TEMPLET_DATA, mTempletData);
			intent.putExtra(AppConstants.SELECTED_CAT_DATA, mSelectedCategory);
			startActivity(intent);
			break;
		case AppConstants.ACTION_REPORT_ERROR:
			intent = new Intent(GuestBranchingActivity.this,
					ReportErrorActivity.class);
			intent.putExtra(AppConstants.COMP_DETAIL_DATA, mCompanyDetail);
			intent.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD, mSearchKeyword);
			startActivity(intent);
			break;
		case AppConstants.ACTION_ADD_FAV:
			break;
		}
		AnalyticsHelper.endTimedEvent(FlurryEventsConstants.APPLICATION_LOGIN);
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == AppConstants.AR_REPORT_ERROR_SUCCESS) {
			showInfoDialog(getResources()
					.getString(R.string.are_error_reported));
		} else if (resultCode == AppConstants.AR_REPORT_ERROR_FAILURE) {
			showInfoDialog(getResources().getString(R.string.are_error_occured));
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
			if(index==-1)
			{
				city_id =-1;
			}else
			{
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

			} else {
				return null;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
