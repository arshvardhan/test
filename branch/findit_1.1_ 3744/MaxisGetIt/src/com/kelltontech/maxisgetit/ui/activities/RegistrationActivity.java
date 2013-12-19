package com.kelltontech.maxisgetit.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kelltontech.framework.model.MaxisResponse;
import com.kelltontech.framework.utils.NativeHelper;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.framework.utils.UiUtils;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.RegistrationController;
import com.kelltontech.maxisgetit.dao.CompanyDetail;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import com.kelltontech.maxisgetit.dao.SubCategory;
import com.kelltontech.maxisgetit.requests.RegistrationRequest;
import com.kelltontech.maxisgetit.response.ControlDetailResponse;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;
import com.kelltontech.maxisgetit.utils.Utility;

public class RegistrationActivity extends MaxisMainActivity {
	private int mActionType;
	private ControlDetailResponse mTempletData;
	private SubCategory mSelectedCategory;
	
	private ImageView mHomeIconView, mProfileIconView;
	private EditText mEdName, mEdEmail, mEdPhone;
	private TextView mRegisterView;
	private RegistrationRequest mRequest;
	private LinearLayout mSearchContainer;
	private ImageView mSearchToggler;
	private ImageView mHeaderBackButton;
	private ImageView mSearchBtn;
	private EditText mSearchEditText;
	private CompanyDetail mCompanyDetail;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		UiUtils.hideKeyboardOnTappingOutside(findViewById(R.id.reg_root_layout), this);
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			mActionType = bundle.getInt(AppConstants.ACTION_IDENTIFIER, AppConstants.ACTION_MY_ACCOUNT);
			if(mActionType == AppConstants.ACTION_REPORT_ERROR){
				mCompanyDetail = bundle.getParcelable(AppConstants.COMP_DETAIL_DATA);
			}else{
				mTempletData=bundle.getParcelable(AppConstants.TEMPLET_DATA);
				mSelectedCategory=bundle.getParcelable(AppConstants.SELECTED_CAT_DATA);
			}
		}
		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
//		searchBtn = (ImageView) findViewById(R.id.search_icon_button);
//		searchBtn.setOnClickListener(RegistrationActivity.this);
//		searchEditText = (EditText) findViewById(R.id.search_box);
		mEdName = (EditText) findViewById(R.id.reg_name);
		mEdEmail = (EditText) findViewById(R.id.reg_email);
		mEdPhone = (EditText) findViewById(R.id.reg_mobile);
		String phNumber=NativeHelper.getMy10DigitPhoneNumber(RegistrationActivity.this);
		if(phNumber!=null){
			mEdPhone.setText(phNumber);
//			edPhone.setEnabled(false);
		}
		mRegisterView = (TextView) findViewById(R.id.register_button);
		mRegisterView.setOnClickListener(this);
		MaxisStore store=MaxisStore.getStore(RegistrationActivity.this);
		if(store.isRegisteredUser()){
			mEdName.setText(store.getUserName());
			mEdPhone.setText(store.getUserMobileNumberToDispaly());
			mEdEmail.setText(store.getUserEmail());
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
	}
	
	@Override
	public Activity getMyActivityReference() {
		return null;
	}

	private void register(String name, String mobileNumber, String email) {
		mobileNumber = Utility.getMobileNoForWS(this, mobileNumber);
		RegistrationController regcController = new RegistrationController(RegistrationActivity.this, Events.REGISTRATION);
		mRequest = new RegistrationRequest(RegistrationActivity.this, name, mobileNumber);
		if (email != null)
			mRequest.setEmail(email);
		startSppiner();
		regcController.requestService(mRequest);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.search_icon_button: searchEditText.setText(searchEditText.getText().toString().trim());
//			performSearch(searchEditText.getText().toString());
//			break;
		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			Intent intentHome = new Intent(RegistrationActivity.this, HomeActivity.class);
			intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentHome);
			break;
		case R.id.show_profile_icon:
			onProfileClick();
//			Intent intentRegister = new Intent(RegistrationActivity.this, RegistrationActivity.class);
//			intentRegister.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//			startActivity(intentRegister);
			break;
		case R.id.register_button:
			String name = mEdName.getText().toString().trim();
			mEdName.setText(name);
			if (name.equals("")){
				showAlertDialog("Please enter your name");
				return;
			}
			String mobile = mEdPhone.getText().toString();
			//if (mobile.length() < 10 || mobile.indexOf('+',1)!=-1 || (mobile.indexOf('+')!=-1 && mobile.length()<=10)){
			if (mobile.length() < 7 || mobile.length() > 12){
				showAlertDialog(getString(R.string.invalid_mobile));
				return;
			}
			String email = mEdEmail.getText().toString().trim();
			mEdEmail.setText(email);
			if (email.equals(""))
				email = null;
			else if(!NativeHelper.isValidEmail(email)){
				showAlertDialog("Please enter valid Email Id");
				return;
			}
			register(name, mobile, email);
			break;
		case R.id.search_toggler:
			AnalyticsHelper.logEvent(FlurryEventsConstants.HOME_SEARCH_CLICK);
			if(mSearchContainer.getVisibility()==View.VISIBLE){
				mSearchContainer.setVisibility(View.GONE);
			}else{
				mSearchContainer.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.search_icon_button: 
			mSearchEditText.setText(mSearchEditText.getText().toString().trim());
			performSearch(mSearchEditText.getText().toString());
			break;
		case R.id.header_btn_back:
			AnalyticsHelper.logEvent(FlurryEventsConstants.BACK_CLICK);
			this.finish();
			break;
		default:
			break;
		}

	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE||msg.arg2==Events.USER_DETAIL) {
			super.updateUI(msg);
		} else if (msg.arg2 == Events.REGISTRATION) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				Intent intent=new Intent(RegistrationActivity.this, LoginActivity.class);
				MaxisStore store=MaxisStore.getStore(RegistrationActivity.this);
				store.setUserMobileNumber(getString(R.string.country_code_excluding_plus) + mRequest.getMobileNumber().substring(2));
				store.setUserRegistered(true);
				store.setUserName(mRequest.getUserName());
				store.setUserEmailId(mRequest.getEmail());
				MaxisResponse res = (MaxisResponse) msg.obj ;
				if(!StringUtil.isNullOrEmpty(res.getServerMessage()))
					intent.putExtra(AppConstants.REGISTRATION_RESPONSE_MESSAGE, res.getServerMessage());
				if(mActionType==AppConstants.ACTION_SELL_POST){
					intent.putExtra(AppConstants.ACTION_IDENTIFIER, AppConstants.ACTION_SELL_POST);
					intent.putExtra(AppConstants.TEMPLET_DATA, mTempletData);
					intent.putExtra(AppConstants.SELECTED_CAT_DATA, mSelectedCategory);
				} else if(mActionType == AppConstants.ACTION_REPORT_ERROR){
					intent.putExtra(AppConstants.ACTION_IDENTIFIER, AppConstants.ACTION_REPORT_ERROR);
					intent.putExtra(AppConstants.COMP_DETAIL_DATA, mCompanyDetail);
					intent.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD, mSearchKeyword);
				}
				startActivity(intent);
				finish();
			}
			stopSppiner();
		}
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE||event==Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		} else if (event == Events.REGISTRATION) {
			handler.sendMessage((Message) screenData);
		}
	}
}
