package com.kelltontech.maxisgetit.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kelltontech.framework.utils.UiUtils;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.EditProfileController;
import com.kelltontech.maxisgetit.controllers.UserDetailController;
import com.kelltontech.maxisgetit.dao.CompanyDetail;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import com.kelltontech.maxisgetit.dao.SubCategory;
import com.kelltontech.maxisgetit.requests.EditProfileRequest;
import com.kelltontech.maxisgetit.response.ControlDetailResponse;
import com.kelltontech.maxisgetit.response.UserDetailResponse;
import com.kelltontech.maxisgetit.ui.widgets.CustomDialog;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class EditProfileActivity extends MaxisMainActivity {
	private int mActionType;
	private ImageView mHomeIconView, mProfileIconView;
	private UserDetailResponse mUserDetail;
	private EditText mEdEmail;
	private EditText mEdMobile;
	private EditText mEdUserName;
	private EditText mPwCurrentPass;
	private EditText mPwNewPass;
	private EditText mPwConfirmPass;
	private TextView mSaveBtn;
	private ImageView mSearchBtn;
	private EditText mSearchEditText;
	private LinearLayout mSearchContainer;
	private ImageView mSearchToggler;
	private ImageView mHeaderBackButton;
	private CompanyDetail mCompanyDetail;
	private ControlDetailResponse mTempletData;
	private SubCategory mSelectedCategory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);
		AnalyticsHelper.logEvent(FlurryEventsConstants.APPLICATION_EDIT_PROFILE, true);
		UiUtils.hideKeyboardOnTappingOutside(findViewById(R.id.epa_root_layout), this);
		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(EditProfileActivity.this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		mSearchContainer=(LinearLayout) findViewById(R.id.search_box_container);
		mSearchToggler=(ImageView) findViewById(R.id.search_toggler);
		mSearchToggler.setOnClickListener(this);
		mHeaderBackButton = (ImageView) findViewById(R.id.header_btn_back);
		mHeaderBackButton.setOnClickListener(this);
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);

		mEdUserName = (EditText) findViewById(R.id.epa_name);
		mEdEmail = (EditText) findViewById(R.id.epa_email);
		mEdMobile = (EditText) findViewById(R.id.epa_mobile);

		mUserDetail = getIntent().getParcelableExtra(AppConstants.USER_DETAIL_DATA);
		mEdUserName.setText(mUserDetail.getName());
		mEdEmail.setText(mUserDetail.getEmail());
		mEdMobile.setText(mUserDetail.getMobile());
		
		mPwCurrentPass=(EditText) findViewById(R.id.epa_current_pass);
		mPwNewPass=(EditText) findViewById(R.id.epa_new_pass);
		mPwConfirmPass=(EditText) findViewById(R.id.epa_confirm_pass);
		mSaveBtn = (TextView) findViewById(R.id.epa_save);
		mSaveBtn.setOnClickListener(this);
		
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			mActionType = bundle.getInt(AppConstants.ACTION_IDENTIFIER, AppConstants.ACTION_MY_ACCOUNT);
			if(mActionType == AppConstants.ACTION_REPORT_ERROR){
				mCompanyDetail = bundle.getParcelable(AppConstants.COMP_DETAIL_DATA);
			}else if(mActionType == AppConstants.ACTION_SELL_POST){
				mTempletData=bundle.getParcelable(AppConstants.TEMPLET_DATA);
				mSelectedCategory=bundle.getParcelable(AppConstants.SELECTED_CAT_DATA);
			}
		}
	}
	private void verifyAndSave(){
		String userName=mEdUserName.getText().toString().trim();
		mEdUserName.setText(userName);
		if(userName.length()==0){
			showInfoDialog(getResources().getString(R.string.enter_user_name));
			return;
		}
		String currentPass=mPwCurrentPass.getText().toString();
		String newPass=mPwNewPass.getText().toString();
		String confirmPass=mPwConfirmPass.getText().toString();
		if(currentPass.length()==0){
			showInfoDialog(getResources().getString(R.string.enter_current_passward));
			return;
		}
		if(newPass.length()==0 && confirmPass.length()==0){
			showInfoDialog(getResources().getString(R.string.enter_new_confirm));
			return;
		}
		if(newPass.length()==0){
			showInfoDialog(getResources().getString(R.string.enter_new));
			return;
		}	
		if(confirmPass.length()==0){
			showInfoDialog(getResources().getString(R.string.enter_confirm));
			return;
		}	
		if(currentPass.indexOf(' ')!=-1 || newPass.indexOf(' ')!=-1 || confirmPass.indexOf(' ')!=-1){
			showInfoDialog(getResources().getString(R.string.password_cant_blank));
			return;
		}
		if(!newPass.equals(confirmPass)){
			showInfoDialog(getResources().getString(R.string.password_doesnt_match));
			return;
		}
		
		EditProfileController epc=new EditProfileController(EditProfileActivity.this, Events.EDIT_PROFILE);
		startSppiner();
		String mobile = mUserDetail.getMobile().substring(1);
		
		EditProfileRequest editProfileRequest = new EditProfileRequest();
		editProfileRequest.setMobile(mobile);
		editProfileRequest.setUserName(userName);
		editProfileRequest.setCurrentPass(currentPass);
		editProfileRequest.setNewPass(newPass);
//		epc.requestService(userName, mobile, currentPass, newPass);
		epc.requestService(editProfileRequest);
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
			Intent intentHome = new Intent(EditProfileActivity.this, HomeActivity.class);
			intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentHome);
			break;
		case R.id.show_profile_icon:
			onProfileClick();
			break;
		case R.id.epa_save:
			AnalyticsHelper.logEvent(FlurryEventsConstants.EDIT_PROFILE_SUBMIT_CLICK);
			verifyAndSave();
			break;
		case R.id.search_icon_button: 
			mSearchEditText.setText(mSearchEditText.getText().toString().trim());
			performSearch(mSearchEditText.getText().toString());
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
			AnalyticsHelper.endTimedEvent(FlurryEventsConstants.APPLICATION_EDIT_PROFILE);
			this.finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE || msg.arg2 == Events.USER_DETAIL) {
			super.updateUI(msg);
			AnalyticsHelper.endTimedEvent(FlurryEventsConstants.APPLICATION_EDIT_PROFILE);
			finish();
		}else if (msg.arg2 == Events.EDIT_PROFILE) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				setResult(RESULT_OK);
				MaxisStore store=MaxisStore.getStore(EditProfileActivity.this);
				store.setUserName(mEdUserName.getText().toString());
				if(mUserDetail.isOTP())
					showSingleButtonConfirmationDialog(getResources().getString(R.string.details_updated_succ));
				else
					showFinalDialog(getResources().getString(R.string.details_updated_succ));
			}
		}
	}

	private void navigateToNextScreen() {
		switch (mActionType) {
		case AppConstants.ACTION_MY_ACCOUNT:
			UserDetailController controller = new UserDetailController(EditProfileActivity.this, Events.USER_DETAIL);
			startSppiner();
			controller.requestService(mUserDetail.getMobile().substring(1));
			break;
		case AppConstants.ACTION_SELL_POST:
			Intent intent = new Intent(EditProfileActivity.this, SaleTempletActivity.class);
			intent.putExtra(AppConstants.TEMPLET_DATA, mTempletData);
			intent.putExtra(AppConstants.SELECTED_CAT_DATA, mSelectedCategory);
			startActivity(intent);
			AnalyticsHelper.endTimedEvent(FlurryEventsConstants.APPLICATION_EDIT_PROFILE);
			finish();
			break;
		case AppConstants.ACTION_REPORT_ERROR:
			intent = new Intent(EditProfileActivity.this, ReportErrorActivity.class);
			intent.putExtra(AppConstants.COMP_DETAIL_DATA, mCompanyDetail);
			intent.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD, mSearchKeyword);
			startActivity(intent);
			AnalyticsHelper.endTimedEvent(FlurryEventsConstants.APPLICATION_EDIT_PROFILE);
			finish();
		}
	}
	
	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE || event == Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		}else if(event==Events.EDIT_PROFILE){
			handler.sendMessage((Message) screenData);
		}
	}
	
	@Override
	public void onPositiveDialogButton(int pDialogId) {
	if(pDialogId == CustomDialog.SINGLE_BUTTON_CONFIRMATION_DIALOG) {
		navigateToNextScreen();
	}
		super.onPositiveDialogButton(pDialogId);
	}
	
}
