package com.kelltontech.maxisgetit.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kelltontech.framework.model.MaxisResponse;
import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.framework.utils.UiUtils;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.PostReviewController;
import com.kelltontech.maxisgetit.dao.CompanyDetail;
import com.kelltontech.maxisgetit.dao.MaxisStore;
import com.kelltontech.maxisgetit.requests.PostReviewRequest;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class RateCompanyActivity extends MaxisMainActivity {
	private ImageView mHomeIconView, mProfileIconView;
	private EditText mSearchEditText;
	private ImageView mSearchBtn;
	private LinearLayout mSearchContainer;
	private ImageView mSearchToggler;
	private ImageView mHeaderBackButton;
	private RatingBar mCompanyRating;
	private EditText mEdtReviewText;
	private TextView mBtnSubmit;
	private CompanyDetail mCompanyDetail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rate_company);
		UiUtils.hideKeyboardOnTappingOutside(findViewById(R.id.rc_root_layout), this);
		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(RateCompanyActivity.this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		mSearchContainer=(LinearLayout) findViewById(R.id.search_box_container);
		mSearchToggler=(ImageView) findViewById(R.id.search_toggler);
		mSearchToggler.setOnClickListener(this);
		mHeaderBackButton = (ImageView) findViewById(R.id.header_btn_back);
		mHeaderBackButton.setOnClickListener(this);
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		
		mCompanyRating = (RatingBar) findViewById(R.id.rc_rating_comp);
		mEdtReviewText = (EditText) findViewById(R.id.rc_review_text);
		mBtnSubmit = (TextView) findViewById(R.id.rc_submit_button);
		mBtnSubmit.setOnClickListener(this);
		mEdtReviewText.clearFocus();
		mEdtReviewText.setOnTouchListener(new OnTouchListener() {
	           public boolean onTouch(View view, MotionEvent event) {
	                // TODO Auto-generated method stub
	                if (view.getId() ==R.id.rc_review_text) {
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
		Bundle bundle = getIntent().getExtras();
		mCompanyDetail = bundle.getParcelable(AppConstants.COMP_DETAIL_DATA);
		mSearchKeyword =  bundle.getString(AppConstants.GLOBAL_SEARCH_KEYWORD);
		
		if(!StringUtil.isNullOrEmpty(mSearchKeyword))
			mSearchEditText.setText(mSearchKeyword);
		
		((TextView)findViewById(R.id.header_title)).setText(mCompanyDetail.getTitle());
		
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
			Intent intentHome = new Intent(RateCompanyActivity.this, HomeActivity.class);
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
		case R.id.rc_submit_button:
			if(validateFields())
			{
				saveReview();
			}
			break;
		default:
			break;
		}

	}

	private void saveReview() {
		startSppiner();
		MaxisStore store = MaxisStore.getStore(this);
		PostReviewController controller = new PostReviewController(this, Events.POST_REVIEW);
		
		float rating = mCompanyRating.getRating();
		String review = (!StringUtil.isNullOrEmpty(mEdtReviewText.getText().toString())) ? mEdtReviewText.getText().toString() : "";
		String userId = (!StringUtil.isNullOrEmpty(store.getUserID())) ? store.getUserID() : "";
		PostReviewRequest postReviewRequest = new PostReviewRequest();
		postReviewRequest.setCatId(mCompanyDetail.getCatId());
		postReviewRequest.setCompId(mCompanyDetail.getId());
		postReviewRequest.setRating(rating+"");
		postReviewRequest.setReview(review);
		postReviewRequest.setUserId(userId);
		controller.requestService(postReviewRequest);
	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE||msg.arg2==Events.USER_DETAIL) {
			super.updateUI(msg);
		}else if (msg.arg2 == Events.POST_REVIEW) {
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
		}else if(event == Events.POST_REVIEW){
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
	
	private boolean validateFields() {
		if(mCompanyRating.getRating() < 1){
			showInfoDialog(getResources().getString(R.string.rc_select_stars));
			return false;
		}else {
			return true;
		}
			
	}
}

