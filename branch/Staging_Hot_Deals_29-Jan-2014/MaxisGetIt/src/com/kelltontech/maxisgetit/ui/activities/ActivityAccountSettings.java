package com.kelltontech.maxisgetit.ui.activities;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kelltontech.framework.utils.UiUtils;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import com.kelltontech.maxisgetit.ui.widgets.CustomDialog;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class ActivityAccountSettings extends MaxisMainActivity {
	private RadioGroup mLangSettingRG;
	private RadioButton mEngRadioButton;
	private RadioButton mMalayRadioButton;
	private TextView mSaveLangLocaleBtn;
	private TextView mLogoutBtn;;
	private CheckBox mLocationCheck;
	private MaxisStore mStore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mStore = MaxisStore.getStore(ActivityAccountSettings.this);
		setContentView(R.layout.activity_account_settings);
		UiUtils.hideKeyboardOnTappingOutside(findViewById(R.id.reg_root_layout), this);
		mLangSettingRG = (RadioGroup) findViewById(R.id.as_lang_switcher);
		mEngRadioButton = (RadioButton) findViewById(R.id.as_lang_en);
		mMalayRadioButton = (RadioButton) findViewById(R.id.as_lang_ms);
		mLocationCheck = (CheckBox) findViewById(R.id.as_location);
		mSaveLangLocaleBtn = (TextView) findViewById(R.id.as_save_lang_loc);
		mSaveLangLocaleBtn.setOnClickListener(this);
		mLogoutBtn = (TextView) findViewById(R.id.as_logout);
		if (mStore.isLoogedInUser())
			mLogoutBtn.setOnClickListener(this);
		else
			mLogoutBtn.setVisibility(View.GONE);
		initSettings();
	}

	private void initSettings() {
		boolean isLocationaware = mStore.isLocalized();
		mLocationCheck.setChecked(isLocationaware);
		boolean isEnSelected = mStore.isEnglishSelected();
		if (isEnSelected) {
			mEngRadioButton.setChecked(true);
		} else
			mMalayRadioButton.setChecked(true);
	}

	@Override
	public Activity getMyActivityReference() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.as_logout:
			mStore.setLoggedInUser(false);
			mStore.setVerifiedUser(false);
			mStore.setUserRegistered(false);
			mStore.setUserEmailId("");
			showInfoDialog("Logout successful");
			mLogoutBtn.setVisibility(View.GONE);
			AnalyticsHelper.logEvent(FlurryEventsConstants.LOGOUT_CLICK);
			break;
		case R.id.as_save_lang_loc:
			showConfirmationDialog(CustomDialog.CONFIRMATION_DIALOG, "The Application will restart to perform the requested changes. Do you wish to continue?");
			break;
		default:
			break;
		}
	}

	@Override
	public void onPositiveDialogButton(int id) {
		if (id == CustomDialog.CONFIRMATION_DIALOG) {
			saveSettings();
		}
	}

	public void setLocale(String lang) {
		Locale myLocale = new Locale(lang);
		Resources res = getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		Configuration conf = res.getConfiguration();
		conf.locale = myLocale;
		res.updateConfiguration(conf, dm);
		Intent refresh = new Intent(this, HomeActivity.class);
		refresh.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		refresh.putExtra(AppConstants.RESET, true);
		startActivity(refresh);
	}

	private void saveSettings() {
		if (mLangSettingRG.getCheckedRadioButtonId() == R.id.as_lang_en) {
			mStore.setEnglishSelected(true);
		} else {
			mStore.setEnglishSelected(false);
		}
		mStore.setLocalized(mLocationCheck.isChecked());
		setLocale(mStore.getLocaleCode());
	}

}
