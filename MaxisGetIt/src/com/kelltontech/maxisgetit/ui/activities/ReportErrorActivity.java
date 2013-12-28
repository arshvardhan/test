package com.kelltontech.maxisgetit.ui.activities;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.kelltontech.maxisgetit.R;
import com.kelltontech.framework.model.MaxisResponse;
import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.framework.utils.UiUtils;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.PostErrorController;
import com.kelltontech.maxisgetit.controllers.ReportErrorMandatoryFieldsController;
import com.kelltontech.maxisgetit.dao.CompanyDetail;
import com.kelltontech.maxisgetit.dao.KeyValuePair;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import com.kelltontech.maxisgetit.dao.ReportErrorMandatoryFields;
import com.kelltontech.maxisgetit.requests.PostErrorRequest;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;
import com.kelltontech.maxisgetit.utils.Utility;

public class ReportErrorActivity extends MaxisMainActivity {
	private ImageView mHomeIconView, mProfileIconView;
	private EditText mSearchEditText;
	private ImageView mSearchBtn;
	private LinearLayout mSearchContainer;
	private ImageView mSearchToggler;
	private ImageView mHeaderBackButton;
	
	
	private Spinner mMandatoryFieldsDropDown;
	private EditText mErrorReportText;
	private CompanyDetail mCompanyDetail;
	private MaxisStore mStore;
	private EditText mEdUserName;
	private EditText mEdUserMobile;
	private LinearLayout mGuestDetailController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_error);
		UiUtils.hideKeyboardOnTappingOutside(findViewById(R.id.are_root_layout), this);
		mStore = MaxisStore.getStore(ReportErrorActivity.this);
		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(ReportErrorActivity.this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		mSearchContainer=(LinearLayout) findViewById(R.id.search_box_container);
		mSearchToggler=(ImageView) findViewById(R.id.search_toggler);
		mSearchToggler.setOnClickListener(this);
		mHeaderBackButton = (ImageView) findViewById(R.id.header_btn_back);
		mHeaderBackButton.setOnClickListener(this);
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		mEdUserName = (EditText) findViewById(R.id.atl_user_name);
		mEdUserMobile = (EditText) findViewById(R.id.atl_user_mobile);
		mGuestDetailController = (LinearLayout) findViewById(R.id.atl_guest_detail_controller);

		mMandatoryFieldsDropDown = (Spinner)findViewById(R.id.are_error_chooser);
		mErrorReportText = (EditText)findViewById(R.id.are_errror_report_text);
		mErrorReportText.clearFocus();
		mErrorReportText.setOnTouchListener(new OnTouchListener() {
	           public boolean onTouch(View view, MotionEvent event) {
	                // TODO Auto-generated method stub
	                if (view.getId() ==R.id.are_errror_report_text) {
	                    view.getParent().requestDisallowInterceptTouchEvent(true);
	                    switch (event.getAction()&MotionEvent.ACTION_MASK){
	                    case MotionEvent.ACTION_UP:
	                        view.getParent().requestDisallowInterceptTouchEvent(false);
	                        break;
	                    }
	                }
	                return false;
	            }
	        });
		
		/*mErrorReportText.setScroller(new Scroller(this)); 
		//mErrorReportText.setMaxLines(1); 
		mErrorReportText.setVerticalScrollBarEnabled(true); 
		mErrorReportText.setMovementMethod(new ScrollingMovementMethod()); */
		
		Bundle bundle = getIntent().getExtras();
		mCompanyDetail = bundle.getParcelable(AppConstants.COMP_DETAIL_DATA);
		mSearchKeyword =  bundle.getString(AppConstants.GLOBAL_SEARCH_KEYWORD);
		
		if(!StringUtil.isNullOrEmpty(mSearchKeyword))
			mSearchEditText.setText(mSearchKeyword);
		
		((TextView)findViewById(R.id.header_title)).setText(mCompanyDetail.getTitle());
		
		findViewById(R.id.are_submit_button).setOnClickListener(this);
		
		ReportErrorMandatoryFieldsController controller = new ReportErrorMandatoryFieldsController(this, Events.REPORT_ERROR_STATUS);
		controller.requestService(null);
		startSppiner();
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(mStore.isLoogedInUser()) {
			mGuestDetailController.setVisibility(View.GONE);}
		else{
			mGuestDetailController.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public Activity getMyActivityReference() {
		return null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_icon_button: 
			mSearchEditText.setText(mSearchEditText.getText().toString().trim());
			performSearch(mSearchEditText.getText().toString());
			break;
		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			Intent intentHome = new Intent(ReportErrorActivity.this, HomeActivity.class);
			intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentHome);
			break;
		case R.id.show_profile_icon:
			onProfileClick();
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
		case R.id.are_submit_button : 
			if(validateFields())
			{
				saveError();
			}
			break;
		default:
			break;
		}

	}

	private void saveError() {
		startSppiner();
		MaxisStore store = MaxisStore.getStore(this);
		PostErrorController controller = new PostErrorController(this, Events.SAVE_ERROR_STATUS);
		
		String errorStatus = ((KeyValuePair)mMandatoryFieldsDropDown.getSelectedItem()).getKey();
		String remark = (!StringUtil.isNullOrEmpty(mErrorReportText.getText().toString())) ? mErrorReportText.getText().toString() : "";
		String userid = (!StringUtil.isNullOrEmpty(store.getUserID())) ? store.getUserID() : "";
		//controller.requestService(errorStatus, remark, userid, mCompanyDetail.getId(), mCompanyDetail.getCatId());
		PostErrorRequest errorRequest  = new PostErrorRequest();
		errorRequest.setCatId(mCompanyDetail.getCatId());
		errorRequest.setCompId(mCompanyDetail.getId());
		errorRequest.setErrorStatus(errorStatus);
		errorRequest.setRemark(remark);
		errorRequest.setUserId(userid);
		if(!mStore.isLoogedInUser()){
		errorRequest.setName(mEdUserName.getText().toString().trim());
		errorRequest.setMobile(Utility.getMobileNoForWS(this, mEdUserMobile.getText().toString().trim()));
		}else{
			errorRequest.setName(mStore.getUserName());
			errorRequest.setMobile(Utility.getMobileNoForWS(this,mStore.getUserMobileNumber()));
		}
		controller.requestService(errorRequest);
		
		
	}

	private boolean validateFields() {
			if(mMandatoryFieldsDropDown.getSelectedItemPosition() < 1){
				showInfoDialog(getResources().getString(R.string.are_select_reason));
				return false;
			}else if(StringUtil.isNullOrEmpty(mErrorReportText.getText().toString())){
				showInfoDialog(getResources().getString(R.string.are_fill_error_des));
				return false;
			}else if (!mStore.isLoogedInUser()) {
				String uName = mEdUserName.getText().toString().trim();
				String mobile = mEdUserMobile.getText().toString();
				mEdUserName.setText(uName);
				if (uName.equals("")) {
					showInfoDialog(getString(R.string.name_empty));
					return false;
				}
				//if (mobile.length() < 10 || mobile.indexOf('+',1)!=-1 || (mobile.indexOf('+')!=-1 && mobile.length()<=10)) {
				if (mobile.equals("")) {
					showInfoDialog(getString(R.string.number_empty));
					return false;
				}
				else if(mobile.length() < 7 || mobile.length() > 12)
				{
					showInfoDialog(getString(R.string.invalid_mobile));
					return false;
				}
				else
				{
					return true;
				}
			}
			else {
				return true;
			}
	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE||msg.arg2==Events.USER_DETAIL) {
			super.updateUI(msg);
		}else if (msg.arg2 == Events.REPORT_ERROR_STATUS) {
			if (msg.arg1 == 1) {
				showFinalDialog((String) msg.obj);
			} else {
				ReportErrorMandatoryFields mandatoryFieldsList = (ReportErrorMandatoryFields)msg.obj;
				addDefaultSelect(mandatoryFieldsList.getErrorStatusList());
				ArrayAdapter<KeyValuePair> mandatoryFieldsListAdapter = new ArrayAdapter<KeyValuePair>(ReportErrorActivity.this, R.layout.spinner_item, mandatoryFieldsList.getErrorStatusList());
				mMandatoryFieldsDropDown.setAdapter(mandatoryFieldsListAdapter);
				mMandatoryFieldsDropDown.setFocusable(true);
			}
			stopSppiner();
		}else if (msg.arg2 == Events.SAVE_ERROR_STATUS) {
			if (msg.arg1 != 0) {
				showFinalDialog((String) msg.obj);
			} else {
				MaxisResponse res = (MaxisResponse)msg.obj;
				showFinalDialog(res.getServerMessage());
			}
			stopSppiner();
		}
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE||event==Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		}else if(event == Events.REPORT_ERROR_STATUS){
			Response response = (Response) screenData;
			Message message = new Message();
			message.arg2 = event;
			message.arg1 = 1;
			if (response.isError()) {
				message.obj = getResources().getString(R.string.communication_failure);
			} else {
				if (response.getPayload() instanceof ReportErrorMandatoryFields) {
					ReportErrorMandatoryFields reportErrorResp = (ReportErrorMandatoryFields) response.getPayload();
					if (reportErrorResp.isErrorFromServer()) {
						message.obj = getResources().getString(R.string.communication_failure);
					} else {
						if (reportErrorResp.getErrorStatusList().size() < 1) {
							message.obj = getResources().getString(R.string.communication_failure);
						} else {
							message.arg1 = 0;
							message.obj = reportErrorResp;
						}
					}
				} else {
					message.obj = getResources().getString(R.string.communication_failure);
				}
			}
			handler.sendMessage(message);
		}else if(event == Events.SAVE_ERROR_STATUS){
			Response response = (Response) screenData;
			Message message = new Message();
			message.arg2 = event;
			message.arg1 = 1;
			if (response.isError()) {
				message.obj = getResources().getString(R.string.communication_failure);
			} else {
				if (response.getPayload() instanceof MaxisResponse) {
					MaxisResponse maxisResponse = (MaxisResponse) response.getPayload();
					if (maxisResponse.isErrorFromServer()) {
						message.obj = getResources().getString(R.string.communication_failure);
					} else {
						
							message.arg1 = 0;
							message.obj = maxisResponse;
					}
				} else {
					message.obj = getResources().getString(R.string.communication_failure);
				}
			}
			handler.sendMessage(message);
		}
	}
	
	private void addDefaultSelect(ArrayList<KeyValuePair> mandatoryFieldList) {
		if (mandatoryFieldList != null) {
			KeyValuePair temp = new KeyValuePair();
			temp.setKey("0");
			temp.setValue("Select");
			mandatoryFieldList.add(0, temp);
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		 InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		 imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		 return true;
	}
	
}