package com.kelltontech.maxisgetit.ui.activities;

import com.kelltontech.framework.utils.UiUtils;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.VerifyRegistrationController;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import com.kelltontech.maxisgetit.dao.SubCategory;
import com.kelltontech.maxisgetit.requests.ActivationRequest;
import com.kelltontech.maxisgetit.response.ControlDetailResponse;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;
import com.kelltontech.maxisgetit.utils.Utility;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VerifyRegistrationActivity extends MaxisMainActivity {
	private int mActionType = AppConstants.ACTION_MY_ACCOUNT;
	private ControlDetailResponse mTempletData;
	private SubCategory mSelectedCategory;

	private ImageView mHomeIconView, mProfileIconView;
	private EditText mEdVerificationCode;
	private TextView mContinueBtn;
	private String mMobile;
	private ImageView mChangeMobileBtn;
	private EditText mMobileNumberView;
	private TextView mWelcomeMsgVu;
	private MaxisStore mStore;
	private LinearLayout mSearchContainer;
	private ImageView mSearchToggler;
	private ImageView mHeaderBackButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verify_registration);
		UiUtils.hideKeyboardOnTappingOutside(findViewById(R.id.avr_root_layout), this);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mActionType = bundle.getInt(AppConstants.ACTION_IDENTIFIER, AppConstants.ACTION_MY_ACCOUNT);
			mTempletData = bundle.getParcelable(AppConstants.TEMPLET_DATA);
			mSelectedCategory = bundle.getParcelable(AppConstants.SELECTED_CAT_DATA);
		}
		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mStore = MaxisStore.getStore(VerifyRegistrationActivity.this);
		mEdVerificationCode = (EditText) findViewById(R.id.avr_verify_code);
		mContinueBtn = (TextView) findViewById(R.id.avr_verify_btn);
		mContinueBtn.setOnClickListener(this);
		mMobileNumberView = (EditText) findViewById(R.id.avr_mobile);
		mMobileNumberView.setText(mStore.getUserMobileNumberToDispaly());
		mChangeMobileBtn = (ImageView) findViewById(R.id.avr_cross_mobile);
		mChangeMobileBtn.setOnClickListener(this);
		mWelcomeMsgVu = (TextView) findViewById(R.id.avr_welcome_msg);
		mWelcomeMsgVu.setText(getResources().getString(R.string.welcome_to) + " " + getResources().getString(R.string.app_name));
		
		mSearchContainer=(LinearLayout) findViewById(R.id.search_box_container);
		mSearchToggler=(ImageView) findViewById(R.id.search_toggler);
		mSearchToggler.setOnClickListener(this);
		mHeaderBackButton = (ImageView) findViewById(R.id.header_btn_back);
		mHeaderBackButton.setOnClickListener(this);
		showInfoDialog(getResources().getString(R.string.msg_verification_code));
	}

	@Override
	public Activity getMyActivityReference() {
		return null;
	}

	private void doVerification() {

		mMobile = Utility.getMobileNoForWS(this, mStore.getUserMobileNumberToDispaly());
		String verString = mEdVerificationCode.getText().toString().trim();
		if (verString.length() == 0)
			showInfoDialog(getResources().getString(R.string.enter_verification_code));
		else {
			VerifyRegistrationController controller = new VerifyRegistrationController(VerifyRegistrationActivity.this, Events.VERIFY_REGISTRATION);
			startSppiner();
			ActivationRequest activationRequest = new ActivationRequest();
			activationRequest.setCode(verString);
			activationRequest.setMobile(mMobile);
			controller.requestService(activationRequest);
		}
	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.VERIFY_REGISTRATION) {
			if (msg.arg1 != 0) {
				showInfoDialog((String) msg.obj);
			} else {
//				showFinalDialog("Registration Successful");
				finish();
				MaxisStore store = MaxisStore.getStore(VerifyRegistrationActivity.this);
				store.setVerifiedUser(true);
				Intent intentLogin = new Intent(VerifyRegistrationActivity.this, LoginActivity.class);
				if (mActionType == AppConstants.ACTION_SELL_POST) {
					intentLogin.putExtra(AppConstants.ACTION_IDENTIFIER, AppConstants.ACTION_SELL_POST);
					intentLogin.putExtra(AppConstants.TEMPLET_DATA, mTempletData);
					intentLogin.putExtra(AppConstants.SELECTED_CAT_DATA, mSelectedCategory);
				}
				intentLogin.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intentLogin);
			}
			stopSppiner();
		}
		super.updateUI(msg);
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.VERIFY_REGISTRATION) {
			handler.sendMessage((Message) screenData);
		} else {
			super.setScreenData(screenData, event, time);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			Intent intentHome = new Intent(VerifyRegistrationActivity.this, HomeActivity.class);
			intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentHome);
			break;
		case R.id.show_profile_icon:
			onProfileClick();
			break;
		case R.id.avr_verify_btn:
			doVerification();
			break;
		case R.id.avr_cross_mobile:
			Intent intentverify = new Intent(VerifyRegistrationActivity.this, RegistrationActivity.class);
			intentverify.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentverify);
			finish();
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
			AnalyticsHelper.logEvent(FlurryEventsConstants.BACK_CLICK);
			this.finish();
			break;
		default:
			break;
		}
	}

}
