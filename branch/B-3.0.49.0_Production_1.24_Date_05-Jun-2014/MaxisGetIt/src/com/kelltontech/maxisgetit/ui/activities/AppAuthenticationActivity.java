package com.kelltontech.maxisgetit.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kelltontech.maxisgetit.R;
import com.kelltontech.framework.model.MaxisResponse;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.framework.utils.UiUtils;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.controllers.AppAuthenticationController;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;
import com.kelltontech.maxisgetit.utils.Utility;

public class AppAuthenticationActivity extends MaxisMainActivity {
	private ImageView mHomeIconView, mProfileIconView;
	private EditText mEdMobile;
	private TextView mConfirmBtn;
	private String mMobile;
	private MaxisStore mStore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_authentication);
		mStore = MaxisStore.getStore(AppAuthenticationActivity.this);
		UiUtils.hideKeyboardOnTappingOutside(findViewById(R.id.reg_root_layout), this);
		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mEdMobile = (EditText) findViewById(R.id.auth_mobile);
		mConfirmBtn = (TextView) findViewById(R.id.auth_confirm_button);
		mConfirmBtn.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		AnalyticsHelper.trackSession(AppAuthenticationActivity.this, AppConstants.AppAuthentication);
	}
	
	@Override
	public Activity getMyActivityReference() {
		return null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.auth_confirm_button:
			validateAndSubmit();
			break;
		default:
			break;
		}

	}

	private void validateAndSubmit() {
		mMobile = mEdMobile.getText().toString().trim();
		if(StringUtil.isNullOrEmpty(mMobile))
		{
			showInfoDialog(getString(R.string.number_empty));
			return;
		}
		else if (mMobile.length() <= 7 || mMobile.length() >= 12 || !mMobile.startsWith("1")) {
			showInfoDialog(getString(R.string.mobile_number_validation));
			return;
		}
		mMobile = Utility.getMobileNoForWS(this, mMobile);
		/*String version = null;
		PackageInfo pInfo;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			version = pInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}*/
		AppAuthenticationController appAuthController = new AppAuthenticationController(AppAuthenticationActivity.this, Events.APP_AUTHENTICATION);
		startSppiner();
		mStore.setAuthMobileNumber(mMobile);
		appAuthController.requestService(mMobile);
	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE || msg.arg2 == Events.USER_DETAIL) {
			super.updateUI(msg);
		} else if (msg.arg2 == Events.APP_AUTHENTICATION) {
			if (msg.arg1 != 0) {
				showInfoDialog((String) msg.obj);
			} else {
				MaxisResponse res = (MaxisResponse)msg.obj;
				String code = res.getServerMessage();
//				showInfoDialog(code);
				Intent intent=new Intent(AppAuthenticationActivity.this,AppActivationActivity.class);
				intent.putExtra(AppConstants.MOBILE_NUMBER, mMobile);
				intent.putExtra("temp_code", code);
				startActivityForResult(intent, 0);
				//finish();
			}
			stopSppiner();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == AppConstants.AR_AUTH_REQUEST_SUCCESS)
		{
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE || event == Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		} else if (event == Events.APP_AUTHENTICATION) {
			handler.sendMessage((Message) screenData);
		}
	}
}
