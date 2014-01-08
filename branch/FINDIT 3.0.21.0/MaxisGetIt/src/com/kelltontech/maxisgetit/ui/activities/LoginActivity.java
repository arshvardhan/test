package com.kelltontech.maxisgetit.ui.activities;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kelltontech.framework.db.MyApplication;
import com.kelltontech.framework.utils.NativeHelper;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.framework.utils.UiUtils;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.LoginController;
import com.kelltontech.maxisgetit.controllers.UserDetailController;
import com.kelltontech.maxisgetit.dao.CompanyDetail;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import com.kelltontech.maxisgetit.dao.SubCategory;
import com.kelltontech.maxisgetit.db.CityTable;
import com.kelltontech.maxisgetit.db.FavCompanysTable;
import com.kelltontech.maxisgetit.requests.LoginRequest;
import com.kelltontech.maxisgetit.response.ControlDetailResponse;
import com.kelltontech.maxisgetit.response.GenralListResponse;
import com.kelltontech.maxisgetit.response.UserDetailResponse;
import com.kelltontech.maxisgetit.ui.widgets.CustomDialog;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;
import com.kelltontech.maxisgetit.utils.Utility;

public class LoginActivity extends MaxisMainActivity {
	private int mActionType=AppConstants.ACTION_MY_ACCOUNT;
	private ImageView mHomeIconView, mProfileIconView;
	private EditText mEdPasswd, mEdPhone;
	private TextView mLoginView;
	private TextView mSignUpBtn;
	private String mMobileNumber;
	private String mPassword;
	private ControlDetailResponse mTempletData;
	private SubCategory mSelectedCategory;
	private MaxisStore mStore;
	private LinearLayout mSearchContainer;
	private RelativeLayout mGuestSeparator;
	private TextView mGuestUsrBtn;
	private ImageView mSearchToggler;
	private ImageView mHeaderBackButton;
	private ImageView mSearchBtn;
	private EditText mSearchEditText;
	private String mRegistrationMessage;
	private TextView mHeaderTitle;
	private UserDetailResponse mUserDetail;
	private CompanyDetail mCompanyDetail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		AnalyticsHelper.logEvent(FlurryEventsConstants.APPLICATION_LOGIN, true);
		mStore= MaxisStore.getStore(LoginActivity.this);
		UiUtils.hideKeyboardOnTappingOutside(findViewById(R.id.login_root_layout), this);
		mGuestSeparator = (RelativeLayout) findViewById(R.id.separator_layout);
		mGuestUsrBtn = (TextView) findViewById(R.id.login_guest_user_button);
		mGuestUsrBtn.setOnClickListener(this);
		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mHeaderTitle=(TextView) findViewById(R.id.header_title);
		mHeaderTitle.setText(Html.fromHtml(getResources().getString(R.string.login_title)));
		
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			mActionType = bundle.getInt(AppConstants.ACTION_IDENTIFIER, AppConstants.ACTION_MY_ACCOUNT);
			if(mActionType == AppConstants.ACTION_REPORT_ERROR){
				mCompanyDetail = bundle.getParcelable(AppConstants.COMP_DETAIL_DATA);
			} else if(mActionType == AppConstants.ACTION_ADD_FAV){
				mGuestSeparator.setVisibility(View.GONE);
				mGuestUsrBtn.setVisibility(View.GONE);
			} else{
				mTempletData=bundle.getParcelable(AppConstants.TEMPLET_DATA);
				mSelectedCategory=bundle.getParcelable(AppConstants.SELECTED_CAT_DATA);
				mRegistrationMessage = bundle.getString(AppConstants.REGISTRATION_RESPONSE_MESSAGE);
			}
		}
		// searchBtn = (ImageView) findViewById(R.id.search_icon_button);
		// searchBtn.setOnClickListener(LoginActivity.this);
		// searchEditText = (EditText) findViewById(R.id.search_box);
		mEdPasswd = (EditText) findViewById(R.id.login_pass);
		mEdPhone = (EditText) findViewById(R.id.login_mobile);
		String phNumber = NativeHelper.getMy10DigitPhoneNumber(LoginActivity.this);
		if (phNumber != null) {
			mEdPhone.setText(phNumber);
		}
		mLoginView = (TextView) findViewById(R.id.login_button);
		mLoginView.setOnClickListener(this);
		mSignUpBtn = (TextView) findViewById(R.id.signup_button);
		mSignUpBtn.setOnClickListener(this);
		MaxisStore store = MaxisStore.getStore(LoginActivity.this);
		if (store.isRegisteredUser()) {
			mEdPhone.setText(store.getUserMobileNumberToDispaly());
		}
		
		mSearchContainer=(LinearLayout) findViewById(R.id.search_box_container);
		mSearchToggler=(ImageView) findViewById(R.id.search_toggler);
		mSearchToggler.setOnClickListener(this);
		mHeaderBackButton = (ImageView) findViewById(R.id.header_btn_back);
		mHeaderBackButton.setOnClickListener(this);
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		if(!StringUtil.isNullOrEmpty(mSearchKeyword))
			mSearchEditText.setText(mSearchKeyword);
		
		findViewById(R.id.login_forget_password).setOnClickListener(this);
		
		if(!StringUtil.isNullOrEmpty(mRegistrationMessage)){
			showInfoDialog(mRegistrationMessage);
		}
		
	}

	@Override
	public Activity getMyActivityReference() {
		// TODO Auto-generated method stub
		return null;
	}

	private void login() {
		LoginController loginController = new LoginController(LoginActivity.this, Events.LOGIN);
		startSppiner();
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setMobile(mMobileNumber);
		loginRequest.setPassword(mPassword);
		loginController.requestService(loginRequest);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// case R.id.search_icon_button: searchEditText.setText(searchEditText.getText().toString().trim());
		// performSearch(searchEditText.getText().toString());
		// break;
		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			Intent intentHome = new Intent(LoginActivity.this, HomeActivity.class);
			intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentHome);
			break;
		case R.id.show_profile_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.SHOW_PROFILE_CLICK);
			showInfoDialog("Please enter the account details or signup");
			break;
		case R.id.login_button:
			String mobile = mEdPhone.getText().toString();
			//if (mobile.length() < 10 || mobile.indexOf('+',1)!=-1 || (mobile.indexOf('+')!=-1 && mobile.length()<=10)) {
			if (mobile.length() < 7 || mobile.length()>12) {
				showAlertDialog(getString(R.string.invalid_mobile));
				return;
			}
			String passwd = mEdPasswd.getText().toString();
			if (passwd.indexOf(' ')!=-1|| passwd.equals("")) {
				showAlertDialog("Please enter valid Password without blank spaces");
				return;
			}
			mMobileNumber = Utility.getMobileNoForWS(this, mobile);
			mPassword = passwd;
			login();
			break;
		case R.id.signup_button:
			AnalyticsHelper.endTimedEvent(FlurryEventsConstants.APPLICATION_LOGIN);
			AnalyticsHelper.logEvent(FlurryEventsConstants.NEW_USER_REGISTER_NOW_CLICK);
			finish();
			Intent intentReg;
			/*if(mStore.isRegisteredUser() && !mStore.isVerifiedUser()){
				intentReg = new Intent(LoginActivity.this, VerifyRegistrationActivity.class);
			}else{
				intentReg = new Intent(LoginActivity.this, RegistrationActivity.class);
			}*/
			intentReg = new Intent(LoginActivity.this, RegistrationActivity.class);
			intentReg.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			if(mActionType==AppConstants.ACTION_SELL_POST){
				intentReg.putExtra(AppConstants.ACTION_IDENTIFIER, AppConstants.ACTION_SELL_POST);
				intentReg.putExtra(AppConstants.TEMPLET_DATA, mTempletData);
				intentReg.putExtra(AppConstants.SELECTED_CAT_DATA, mSelectedCategory);
				startActivity(intentReg);
			} else if(mActionType == AppConstants.ACTION_REPORT_ERROR){
				intentReg.putExtra(AppConstants.ACTION_IDENTIFIER, AppConstants.ACTION_REPORT_ERROR);
				intentReg.putExtra(AppConstants.COMP_DETAIL_DATA, mCompanyDetail);
				intentReg.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD, mSearchKeyword);
				startActivity(intentReg);
				finish();
			} else if(mActionType == AppConstants.ACTION_ADD_FAV){
				intentReg.putExtra(AppConstants.ACTION_IDENTIFIER, AppConstants.ACTION_ADD_FAV);
				intentReg.putExtra(AppConstants.IS_FROM_COMP_DETAIL_ADD_FAV, true);
				startActivity(intentReg);
				finish();
			} else {
				startActivity(intentReg);				
			}
			break;
		case R.id.search_toggler:
			AnalyticsHelper.logEvent(FlurryEventsConstants.HOME_SEARCH_CLICK);
			if(mSearchContainer.getVisibility()==View.VISIBLE){
				mSearchContainer.setVisibility(View.GONE);
			}else{
				mSearchContainer.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.header_btn_back:
		case R.id.login_guest_user_button:
			AnalyticsHelper.endTimedEvent(FlurryEventsConstants.APPLICATION_LOGIN);
			AnalyticsHelper.logEvent(FlurryEventsConstants.PROCEED_AS_GUEST_CLICK);
			this.finish();
			break;
		case R.id.search_icon_button: 
			mSearchEditText.setText(mSearchEditText.getText().toString().trim());
			performSearch(mSearchEditText.getText().toString());
			break;
		case R.id.login_forget_password:
			startActivity(new Intent(this,ForgetPasswordActivity.class));
			break;
		default:
			break;
		}

	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE) {
			super.updateUI(msg);
		} else if (msg.arg2 == Events.LOGIN) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				stopSppiner();
				mUserDetail = (UserDetailResponse) msg.obj;
				saveUserInfoWithFavCompanies(mUserDetail);
				if (!StringUtil.isNullOrEmpty(mUserDetail.getMobile()) && !StringUtil.isNullOrEmpty(mUserDetail.getName().trim())) {
					HashMap<String,String>	map = new HashMap<String,String>();
					map.put(FlurryEventsConstants.Logged_In_User_Mobile_No, mUserDetail.getMobile());
					map.put(FlurryEventsConstants.Logged_In_User_Name, mUserDetail.getName());
					AnalyticsHelper.logEvent(FlurryEventsConstants.USER_LOGGED_IN, map);
					}
				if(!mUserDetail.isOTP()) {                         //
					navigateToNextScreen();
				} else {
					showConfirmationDialog(CustomDialog.CHANGE_PASSWORD_DIALOG, getResources().getString(R.string.login_change_password_request));
				}
				return;
			}
			stopSppiner();
		} else if (msg.arg2 == Events.USER_DETAIL) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				Intent intent = new Intent(LoginActivity.this, MyAccountActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.putExtra(AppConstants.USER_DETAIL_DATA, (UserDetailResponse)msg.obj);
				saveUserInfo((UserDetailResponse)msg.obj);
				startActivity(intent);
				AnalyticsHelper.endTimedEvent(FlurryEventsConstants.APPLICATION_LOGIN);
				finish();
			}
			stopSppiner();
		}
	}
	private void saveUserInfo(UserDetailResponse userdDetail){
		MaxisStore store = MaxisStore.getStore(LoginActivity.this);
		store.setUserMobileNumber(userdDetail.getMobile().substring(1));
		store.setUserID(userdDetail.getUserId());
		store.setUserName(userdDetail.getName());
		store.setLoggedInUser(true);
		store.setUserCompany(userdDetail.isCompany());
		
		// Flurry Event for Logged_In user
		if (!StringUtil.isNullOrEmpty(store.getUserMobileNumber()) && !StringUtil.isNullOrEmpty(store.getUserName().trim())) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(FlurryEventsConstants.Logged_In_User_Mobile_No, store.getUserMobileNumber());
			map.put(FlurryEventsConstants.Logged_In_User_Name, store.getUserName());
			AnalyticsHelper.logEvent(FlurryEventsConstants.USER_LOGGED_IN, map, true);
		}
	}
	
	private void saveUserInfoWithFavCompanies(UserDetailResponse userdDetail){
		saveUserInfo(userdDetail);
		FavCompanysTable favCompany=new FavCompanysTable((MyApplication) getApplication());
		favCompany.deleteAll();
		favCompany.addFavCompaniesList(userdDetail.getCompanyIdCategoryId());
	}
	
	private void navigateToNextScreen() {
		switch (mActionType) {
		case AppConstants.ACTION_MY_ACCOUNT:
			UserDetailController controller = new UserDetailController(LoginActivity.this, Events.USER_DETAIL);
			startSppiner();
			controller.requestService(mMobileNumber);
			break;
		case AppConstants.ACTION_SELL_POST:
			Intent intent = new Intent(LoginActivity.this, SaleTempletActivity.class);
			intent.putExtra(AppConstants.TEMPLET_DATA, mTempletData);
			intent.putExtra(AppConstants.SELECTED_CAT_DATA, mSelectedCategory);
			startActivity(intent);
			AnalyticsHelper.endTimedEvent(FlurryEventsConstants.APPLICATION_LOGIN);
			finish();
			break;
		case AppConstants.ACTION_REPORT_ERROR:
			Intent intentError = new Intent(LoginActivity.this, ReportErrorActivity.class);
			intentError.putExtra(AppConstants.COMP_DETAIL_DATA, mCompanyDetail);
			intentError.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD, mSearchKeyword);
			startActivity(intentError);
			finish();
			break;
		case AppConstants.ACTION_ADD_FAV:
			finish();
			break;
		}
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE||event==Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		} else if (event == Events.LOGIN) {
			handler.sendMessage((Message) screenData);
		} else if(event==Events.USER_DETAIL){
			handler.sendMessage((Message) screenData);
		}
	}
	
	@Override
	public void onPositiveDialogButton(int pDialogId) {
		if(pDialogId == CustomDialog.CHANGE_PASSWORD_DIALOG){
			Intent intentProfile = new Intent(this, EditProfileActivity.class);
			if(mActionType == AppConstants.ACTION_SELL_POST)
			{
				intentProfile.putExtra(AppConstants.TEMPLET_DATA, mTempletData);
				intentProfile.putExtra(AppConstants.SELECTED_CAT_DATA, mSelectedCategory);
			}
			else if(mActionType == AppConstants.ACTION_REPORT_ERROR){
				intentProfile.putExtra(AppConstants.COMP_DETAIL_DATA, mCompanyDetail);
				intentProfile.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD, mSearchKeyword);
			}
			intentProfile.putExtra(AppConstants.USER_DETAIL_DATA, mUserDetail);
			intentProfile.putExtra(AppConstants.ACTION_IDENTIFIER, mActionType);
			startActivity(intentProfile);
			AnalyticsHelper.endTimedEvent(FlurryEventsConstants.APPLICATION_LOGIN);
			finish();				//
		} else{
			super.onPositiveDialogButton(pDialogId);
		}
	}
	
	@Override
	public void onNegativeDialogbutton(int id) {
		if(id == CustomDialog.CHANGE_PASSWORD_DIALOG){
			navigateToNextScreen();
		} else{
			super.onNegativeDialogbutton(id);
		}
	}
	
	/*@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 0)
		{
			navigateToNextScreen();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}*/
}
