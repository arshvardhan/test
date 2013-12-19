package com.kelltontech.maxisgetit.ui.activities;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kelltontech.framework.utils.NativeHelper;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.LoginController;
import com.kelltontech.maxisgetit.dao.CompanyDetail;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import com.kelltontech.maxisgetit.dao.SubCategory;
import com.kelltontech.maxisgetit.requests.LoginRequest;
import com.kelltontech.maxisgetit.response.ControlDetailResponse;
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
	private ImageView mSearchToggler;
	private ImageView mHeaderBackButton;
	private ImageView mSearchBtn;
	private EditText mSearchEditText;
	private int mActionType;
	private boolean isFromDetailScreen;
	private CompanyDetail mCompanyDetail;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		AnalyticsHelper.logEvent(FlurryEventsConstants.APPLICATION_LOGIN, true);
		Bundle bundle = getIntent().getExtras();
		isFromDetailScreen = bundle.getBoolean(AppConstants.IS_FROM_DETAIL);
		
		mStore = MaxisStore.getStore(GuestBranchingActivity.this);
		if(isFromDetailScreen) {
			mActionType = AppConstants.ACTION_REPORT_ERROR;
			mCompanyDetail = bundle.getParcelable(AppConstants.COMP_DETAIL_DATA);
		}else {
			mActionType = AppConstants.ACTION_SELL_POST;
			mSelectedCategory = bundle.getParcelable(AppConstants.SELECTED_CAT_DATA);
			mTempletData = bundle.getParcelable(AppConstants.TEMPLET_DATA);
		}
		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mEdPasswd = (EditText) findViewById(R.id.login_pass);
		mEdPhone = (EditText) findViewById(R.id.login_mobile);
		String phNumber = NativeHelper.getMy10DigitPhoneNumber(GuestBranchingActivity.this);
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
		
		findViewById(R.id.login_guest_user_button).setOnClickListener(this);
		findViewById(R.id.login_forget_password).setOnClickListener(this);
		/*mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mLoginLink = (TextView) findViewById(R.id.agb_login_link);
		mLoginLink.setOnClickListener(this);
		mRegisterLink = (TextView) findViewById(R.id.agb_register_link);
		mRegisterLink.setOnClickListener(this);
		mGuestLink = (TextView) findViewById(R.id.agb_guest_link);
		mGuestLink.setOnClickListener(this);*/
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
			Intent intentHome = new Intent(GuestBranchingActivity.this, HomeActivity.class);
			intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentHome);
			break;
		case R.id.show_profile_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.SHOW_PROFILE_CLICK);
			onProfileClick();
			break;
		case R.id.agb_guest_link:
			if(!isFromDetailScreen){
				Intent intent = new Intent(GuestBranchingActivity.this, SaleTempletActivity.class);
				intent.putExtra(AppConstants.TEMPLET_DATA, mTempletData);
				intent.putExtra(AppConstants.SELECTED_CAT_DATA, mSelectedCategory);
				startActivity(intent);
			}
			else{
				Intent intent = new Intent(GuestBranchingActivity.this, ReportErrorActivity.class);
				intent.putExtra(AppConstants.COMP_DETAIL_DATA, mCompanyDetail);
				intent.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD, mSearchKeyword);
				startActivity(intent);
			}
			AnalyticsHelper.endTimedEvent(FlurryEventsConstants.APPLICATION_LOGIN);
			finish();
			break;
		case R.id.agb_login_link:
			Intent intentLogin = new Intent(GuestBranchingActivity.this, LoginActivity.class);
			intentLogin.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intentLogin.putExtra(AppConstants.ACTION_IDENTIFIER, AppConstants.ACTION_SELL_POST);
			intentLogin.putExtra(AppConstants.TEMPLET_DATA, mTempletData);
			intentLogin.putExtra(AppConstants.SELECTED_CAT_DATA, mSelectedCategory);
			startActivity(intentLogin);
			break;
		case R.id.agb_register_link:
			Intent intentReg;
			/*if(mStore.isRegisteredUser() && !mStore.isVerifiedUser()){
				intentReg = new Intent(GuestBranchingActivity.this, VerifyRegistrationActivity.class);
			}else{
				intentReg = new Intent(GuestBranchingActivity.this, RegistrationActivity.class);
			}*/
			intentReg = new Intent(GuestBranchingActivity.this, VerifyRegistrationActivity.class);
			intentReg.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intentReg.putExtra(AppConstants.ACTION_IDENTIFIER, AppConstants.ACTION_SELL_POST);
			intentReg.putExtra(AppConstants.TEMPLET_DATA, mTempletData);
			intentReg.putExtra(AppConstants.SELECTED_CAT_DATA, mSelectedCategory);
			startActivity(intentReg);
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
			{
				AnalyticsHelper.logEvent(FlurryEventsConstants.NEW_USER_REGISTER_NOW_CLICK);
				AnalyticsHelper.endTimedEvent(FlurryEventsConstants.APPLICATION_LOGIN);
				finish();
				Intent intentReg1;
				/*if(mStore.isRegisteredUser() && !mStore.isVerifiedUser()){
					intentReg1 = new Intent(GuestBranchingActivity.this, VerifyRegistrationActivity.class);
				}else{
					intentReg1 = new Intent(GuestBranchingActivity.this, RegistrationActivity.class);
				}*/
				intentReg1 = new Intent(GuestBranchingActivity.this, RegistrationActivity.class);
				intentReg1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				if(mActionType==AppConstants.ACTION_SELL_POST){
					intentReg1.putExtra(AppConstants.ACTION_IDENTIFIER, AppConstants.ACTION_SELL_POST);
					intentReg1.putExtra(AppConstants.TEMPLET_DATA, mTempletData);
					intentReg1.putExtra(AppConstants.SELECTED_CAT_DATA, mSelectedCategory);
				}
				else if(mActionType == AppConstants.ACTION_REPORT_ERROR)
				{
					intentReg1.putExtra(AppConstants.ACTION_IDENTIFIER, AppConstants.ACTION_REPORT_ERROR);
					intentReg1.putExtra(AppConstants.COMP_DETAIL_DATA, mCompanyDetail);
					intentReg1.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD, mSearchKeyword);
				}
				startActivity(intentReg1);
				break;
			}
			case R.id.search_toggler:
				AnalyticsHelper.logEvent(FlurryEventsConstants.HOME_SEARCH_CLICK);
				if(mSearchContainer.getVisibility()==View.VISIBLE){
					mSearchContainer.setVisibility(View.GONE);
				}else{
					mSearchContainer.setVisibility(View.VISIBLE);
				}
				break;
			case R.id.header_btn_back:
				AnalyticsHelper.logEvent(FlurryEventsConstants.BACK_CLICK);
				AnalyticsHelper.endTimedEvent(FlurryEventsConstants.APPLICATION_LOGIN);
				this.finish();
				break;
			case R.id.login_guest_user_button:
				AnalyticsHelper.logEvent(FlurryEventsConstants.PROCEED_AS_GUEST_CLICK);
				if(!isFromDetailScreen){
					Intent intent = new Intent(GuestBranchingActivity.this, SaleTempletActivity.class);
					intent.putExtra(AppConstants.TEMPLET_DATA, mTempletData);
					intent.putExtra(AppConstants.SELECTED_CAT_DATA, mSelectedCategory);
					startActivity(intent);
				}
				else{
					Intent intent = new Intent(GuestBranchingActivity.this, ReportErrorActivity.class);
					intent.putExtra(AppConstants.COMP_DETAIL_DATA, mCompanyDetail);
					intent.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD, mSearchKeyword);
					startActivity(intent);
				}
				AnalyticsHelper.endTimedEvent(FlurryEventsConstants.APPLICATION_LOGIN);
				finish();
				break;
			case R.id.search_icon_button: 
				mSearchEditText.setText(mSearchEditText.getText().toString().trim());
				performSearch(mSearchEditText.getText().toString());
				break;
			case R.id.login_forget_password:
				startActivity(new Intent(this,ForgetPasswordActivity.class));
				AnalyticsHelper.logEvent(FlurryEventsConstants.FORGOT_PASSWORD_CLICK);
				break;
			default:
				break;
		}
	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE || msg.arg2 == Events.USER_DETAIL) {
			super.updateUI(msg);
		}else if (msg.arg2 == Events.LOGIN) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				stopSppiner();
				saveUserInfo((UserDetailResponse) msg.obj);
				navigateToNextScreen();
				return;
			}
			stopSppiner();
		} else if (msg.arg2 == Events.USER_DETAIL) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				Intent intent = new Intent(GuestBranchingActivity.this, MyAccountActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.putExtra(AppConstants.USER_DETAIL_DATA, (UserDetailResponse)msg.obj);
				saveUserInfo((UserDetailResponse)msg.obj);
				startActivity(intent);
				AnalyticsHelper.endTimedEvent(FlurryEventsConstants.APPLICATION_LOGIN);
				finish();
			}
		}
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE || event == Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		} else if (event == Events.LOGIN) {
			handler.sendMessage((Message) screenData);
		} else if(event==Events.USER_DETAIL){
			handler.sendMessage((Message) screenData);
		}
	}
	private void login() {
		LoginController loginController = new LoginController(GuestBranchingActivity.this, Events.LOGIN);
		startSppiner();
		//loginController.requestService(mMobileNumber, mPassword);
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setMobile(mMobileNumber);
		loginRequest.setPassword(mPassword);
		loginController.requestService(loginRequest);
	}
	
	private void saveUserInfo(UserDetailResponse userdDetail){
		MaxisStore store = MaxisStore.getStore(GuestBranchingActivity.this);
		store.setUserMobileNumber(userdDetail.getMobile().substring(1));
		store.setUserID(userdDetail.getUserId());
		store.setUserName(userdDetail.getName());
		store.setLoggedInUser(true);
		store.setUserCompany(userdDetail.isCompany());
		// Flurry Event for Logged_In user
		if (!StringUtil.isNullOrEmpty(store.getUserMobileNumber()) && !StringUtil.isNullOrEmpty(store.getUserName().trim())) {
			HashMap<String,String>	map = new HashMap<String,String>();
			map.put(FlurryEventsConstants.Logged_In_User_Mobile_No, store.getUserMobileNumber());
			map.put(FlurryEventsConstants.Logged_In_User_Name, store.getUserName());
			AnalyticsHelper.logEvent(FlurryEventsConstants.USER_LOGGED_IN, map, true);
			}
	}
	private void navigateToNextScreen() {
		switch (mActionType) {
		case AppConstants.ACTION_SELL_POST:
			Intent intent = new Intent(GuestBranchingActivity.this, SaleTempletActivity.class);
			intent.putExtra(AppConstants.TEMPLET_DATA, mTempletData);
			intent.putExtra(AppConstants.SELECTED_CAT_DATA, mSelectedCategory);
			startActivity(intent);
			break;
		case AppConstants.ACTION_REPORT_ERROR :
			intent = new Intent(GuestBranchingActivity.this, ReportErrorActivity.class);
			intent.putExtra(AppConstants.COMP_DETAIL_DATA, mCompanyDetail);
			intent.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD, mSearchKeyword);
			startActivity(intent);
			break;
		}
		AnalyticsHelper.endTimedEvent(FlurryEventsConstants.APPLICATION_LOGIN);
		finish();
	}

}
