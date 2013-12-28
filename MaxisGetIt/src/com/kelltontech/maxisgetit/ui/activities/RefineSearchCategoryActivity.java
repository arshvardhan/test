package com.kelltontech.maxisgetit.ui.activities;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.kelltontech.framework.imageloader.ImageLoader;
import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.utils.UiUtils;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.CombindListingController;
import com.kelltontech.maxisgetit.dao.CategoryRefine;
import com.kelltontech.maxisgetit.requests.CombinedListRequest;
import com.kelltontech.maxisgetit.response.CompanyListResponse;
import com.kelltontech.maxisgetit.response.RefineCategoryResponse;
import com.kelltontech.maxisgetit.response.RefineSelectorResponse;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class RefineSearchCategoryActivity extends MaxisMainActivity {
	private Spinner mCatSelector;
	private ImageView mSearchBtn;
	private EditText mSearchEditText;
	private ImageView mHomeIconView, mProfileIconView;
	private RefineSelectorResponse mSelctorResp;
	private RefineCategoryResponse mCatResponse;
	private CombinedListRequest mClRequest;
	private CompanyListResponse mClResponse;
	private TextView refineBtn;
	private LinearLayout mSearchContainer;
	private ImageView mSearchToggler;
	private ImageView mHeaderBackButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_refine_category);
		UiUtils.hideKeyboardOnTappingOutside(findViewById(R.id.rc_root_layout), this);
		ImageLoader.initialize(RefineSearchCategoryActivity.this);
		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(RefineSearchCategoryActivity.this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		mSearchContainer=(LinearLayout) findViewById(R.id.search_box_container);
		mSearchToggler=(ImageView) findViewById(R.id.search_toggler);
		mSearchToggler.setOnClickListener(this);
		mHeaderBackButton = (ImageView) findViewById(R.id.header_btn_back);
		mHeaderBackButton.setOnClickListener(this);
		refineBtn = (TextView) findViewById(R.id.rc_refine_btn);
		refineBtn.setOnClickListener(this);
		Bundle bundle = getIntent().getExtras();
		mClRequest = bundle.getParcelable(AppConstants.DATA_LIST_REQUEST);
		mClRequest.setPageNumber(1);
		if(mClRequest.isBySearch())
			mSearchEditText.setText(mClRequest.getKeywordOrCategoryId());
		mCatResponse = (RefineCategoryResponse) bundle.get(AppConstants.REFINE_CAT_RESPONSE);
		showCategorySpinner();
	}

	private void showCategorySpinner() {
		if (mCatResponse == null)
			return;
		ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner_item, mCatResponse.getCategories());
		mCatSelector = (Spinner) findViewById(R.id.rc_cat_spinner);
		mCatSelector.setAdapter(adapter);
		mCatSelector.setSelection(mCatResponse.getSelectedCategoryIndex());
		mCatSelector.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				{
					mCatResponse.setSelectedCategoryIndex(position);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				showInfoDialog("nothing selected");
			}
		});
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE || event == Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		} else if (event == Events.REFINE_ATTRIBUTES) {
			Message catRefine = (Message) screenData;
			handler.sendMessage(catRefine);
		} else if (event == Events.REFINE_SEARCH_RESULT) {
			Response response = (Response) screenData;
			Message message = new Message();
			message.arg2 = event;
			message.arg1 = 1;
			if (response.isError()) {
				message.obj = response.getErrorText() + " " + response.getErrorCode();
			} else {
				if (response.getPayload() instanceof CompanyListResponse) {
					mClResponse = (CompanyListResponse) response.getPayload();
					if (mClResponse.getErrorCode() != 0) {
						message.obj = getResources().getString(R.string.communication_failure);
					} else {
						if (mClResponse.getCompanyArrayList().size() < 1) {
							message.obj = new String("No Result Found");
						} else {
							message.arg1 = 0;
							message.obj = mClResponse;
						}
					}
				} else {
					message.obj = new String("Internal Error");
				}
			}
			handler.sendMessage(message);
		}
	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE || msg.arg2 == Events.USER_DETAIL) {
			super.updateUI(msg);
		}else if (msg.arg2 == Events.REFINE_SEARCH_RESULT) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				mClResponse = (CompanyListResponse) msg.obj;
				Intent intent = new Intent();
				intent.putExtra(AppConstants.COMP_LIST_DATA, mClResponse);
				intent.putExtra(AppConstants.DATA_LIST_REQUEST, mClRequest);
				intent.putExtra(AppConstants.REFINE_ATTR_RESPONSE, mSelctorResp);
				intent.putExtra(AppConstants.REFINE_CAT_RESPONSE, mCatResponse);
				setResult(RESULT_OK, intent);
				finish();
			}
		}
	}

	@Override
	public Activity getMyActivityReference() {
		return null;
	}

	private JSONObject verifyAndFilter() throws JSONException {
		JSONObject jsonSelector = new JSONObject();
		JSONObject jsonObject = new JSONObject();
		if (mClRequest.isBySearch() || (mClRequest.getGroupActionType().trim().equalsIgnoreCase(AppConstants.GROUP_ACTION_TYPE_CATEGORY_LIST_FOR_GROUP) && (mClRequest.getGroupType().trim().equalsIgnoreCase(AppConstants.GROUP_TYPE_CATEGORY)))) {
			if (mCatSelector.getSelectedItemPosition() < 1) {
				showInfoDialog("Please select category.");
				return null;
			}
			CategoryRefine catRef = mCatResponse.getCategories().get(mCatSelector.getSelectedItemPosition());
			mClRequest.setSelectedCategoryBySearch(catRef.getCategoryId(),catRef.getCategoryTitle());
			if(!mClRequest.isBySearch())
				mClRequest.setKeywordOrCategoryId(catRef.getCategoryId());
//			jsonObject.put("category_id", catRef.getCategoryId());
		} 
//		else
//			jsonObject.put("category_id", clRequest.getKeywordOrCategoryId());
		jsonObject.put("selector", jsonSelector);
		System.out.println(mSelctorResp);
		System.out.println(jsonObject);
		return jsonObject;
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
			Intent intentHome = new Intent(RefineSearchCategoryActivity.this, HomeActivity.class);
			intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intentHome);
			break;
		case R.id.show_profile_icon:
			onProfileClick();
			break;
		case R.id.rc_refine_btn:
			try {
				JSONObject postData = verifyAndFilter();
				if (postData != null) {
					mClRequest.setPostJsonPayload(postData.toString());
					CombindListingController controller = new CombindListingController(RefineSearchCategoryActivity.this, Events.REFINE_SEARCH_RESULT);
					startSppiner();
					controller.requestService(mClRequest);
				}
			} catch (JSONException e) {
				AnalyticsHelper.onError(FlurryEventsConstants.DATA_VALIDATION_ERR, "RefineSearchCategoryActivity : " + AppConstants.DATA_VALIDATION_ERROR_MSG, e);
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
			AnalyticsHelper.logEvent(FlurryEventsConstants.BACK_CLICK);
			this.finish();
			break;
		}
	}

}
