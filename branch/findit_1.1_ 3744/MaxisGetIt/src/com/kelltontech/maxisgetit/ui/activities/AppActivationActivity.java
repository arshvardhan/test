package com.kelltontech.maxisgetit.ui.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kelltontech.maxisgetit.R;
import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.utils.UiUtils;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.controllers.AppActivationController;
import com.kelltontech.maxisgetit.dao.CategoryGroup;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import com.kelltontech.maxisgetit.requests.ActivationRequest;
import com.kelltontech.maxisgetit.response.RootCategoryResponse;

public class AppActivationActivity extends MaxisMainActivity {
	private ImageView mHomeIconView, mProfileIconView;
	private String mMobile;
	private TextView codeSentText;
	private TextView mActivateBtn,mResendButton;
	private EditText mEdActivationCode;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_activation);
		UiUtils.hideKeyboardOnTappingOutside(findViewById(R.id.reg_root_layout), this);
		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		codeSentText=(TextView) findViewById(R.id.code_sent_to_mob);
		mHomeIconView.setOnClickListener(this);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mMobile= getIntent().getStringExtra(AppConstants.MOBILE_NUMBER);
		codeSentText.setText(getResources().getString(R.string.a_code_sent)+" +"+mMobile);
		mActivateBtn=(TextView) findViewById(R.id.aact_validate_button);
		mActivateBtn.setOnClickListener(this);
		mResendButton=(TextView) findViewById(R.id.aact_resend_button);
		mResendButton.setOnClickListener(this);
		//String code=getIntent().getStringExtra("temp_code");
		mEdActivationCode=(EditText) findViewById(R.id.aact_code);
		/*if(!StringUtil.isNullOrEmpty(code))
			mEdActivationCode.setText(code);*/
		findViewById(R.id.aact_resend_button).setOnClickListener(this);
	}

	@Override
	public Activity getMyActivityReference() {
		return null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.aact_validate_button:
			validateAndSendInput();
			break;
		case R.id.aact_resend_button:
			setResult(AppConstants.AR_AUTH_REQUEST_FAILURE);
			finish();
			break;
		default:
			break;
		}

	}
	private void validateAndSendInput(){
		String code=mEdActivationCode.getText().toString().trim();
		if(code.length()<1){
			showInfoDialog("Please enter the activation code");
			return;
		}
		AppActivationController actController=new AppActivationController(AppActivationActivity.this, Events.APP_VERIFICATION);
		startSppiner();
		ActivationRequest activationRequest = new ActivationRequest();
		activationRequest.setMobile(mMobile);
		activationRequest.setCode(code);
		actController.requestService(activationRequest);
	}
	
	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE||msg.arg2==Events.USER_DETAIL) {
			super.updateUI(msg);
		}else if(msg.arg2==Events.APP_VERIFICATION){
			stopSppiner();
			if(msg.arg1!=0){
				showInfoDialog((String) msg.obj);
			}else{
				MaxisStore store=MaxisStore.getStore(AppActivationActivity.this);
				store.setAppActivated(true);
				startActivity(new Intent(AppActivationActivity.this, TnCActivity.class));
				setResult(AppConstants.AR_AUTH_REQUEST_SUCCESS);
				finish();
				/*RootCategoryController controller = new RootCategoryController(AppActivationActivity.this, Events.ROOT_CATEGORY_EVENT);
				startSppiner();
				controller.requestService(null);*/
			}
		}else if (msg.arg2 == Events.ROOT_CATEGORY_EVENT) {
			if (msg.arg1 == 1) {
				showFinalDialog((String) msg.obj);
			} else {
				RootCategoryResponse categoriesResp = (RootCategoryResponse) msg.obj;
				ArrayList<CategoryGroup> categories = categoriesResp.getCategories();
				Intent intent = new Intent(AppActivationActivity.this, HomeActivity.class);
				intent.putParcelableArrayListExtra(AppConstants.DATA_CAT_LIST, categories);
				startActivity(intent);
				setResult(AppConstants.AR_AUTH_REQUEST_SUCCESS);
				finish();
			}
		}
	}
	
	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE||event==Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		}else if(event==Events.APP_VERIFICATION){
			handler.sendMessage((Message) screenData);
		}else{
			stopSppiner();
			Response response = (Response) screenData;
			Message message = new Message();
			message.arg2 = event;
			message.arg1 = 1;
			if (response.isError()) {
				message.obj = getResources().getString(R.string.communication_failure);
			} else {
				if (response.getPayload() instanceof RootCategoryResponse) {
					RootCategoryResponse categoriesResp = (RootCategoryResponse) response.getPayload();
					if (categoriesResp.isErrorFromServer()) {
						message.obj = getResources().getString(R.string.communication_failure);
					} else {
						if (categoriesResp.getCategories().size() < 1) {
							message.obj = getResources().getString(R.string.communication_failure);
						} else {
							message.arg1 = 0;
							message.obj = categoriesResp;
						}
					}
				} else {
					message.obj = getResources().getString(R.string.communication_failure);
				}
			}
			handler.sendMessage(message);
		}
	}
}
