package com.kelltontech.maxisgetit.ui.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.adapters.ReviewsListAdapter;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.CompanyReviewsController;
import com.kelltontech.maxisgetit.dao.CompanyDetail;
import com.kelltontech.maxisgetit.dao.CompanyReview;
import com.kelltontech.maxisgetit.requests.CompanyReviewsRequest;
import com.kelltontech.maxisgetit.response.CompanyReviewsListResponse;
import com.kelltontech.maxisgetit.response.UserDetailResponse;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class ViewAllReviewsActivity extends MaxisMainActivity {
	private ImageView mHomeIconView, mProfileIconView;
	private EditText mSearchEditText;
	private ImageView mSearchBtn;
	private LinearLayout mSearchContainer;
	private ImageView mSearchToggler;
	private ImageView mHeaderBackButton;
	
	private TextView mTxtTitle;
	private ListView mReviewsList;
	
	private ReviewsListAdapter mReviewsListAdapter;
	private int mCurrentPageNo;
	private int mTotalCount;
	private ArrayList<CompanyReview> mCompReviewsList;
	private CompanyDetail mCompanyDetail;
	private boolean loadingNextPageData = false;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_all_reviews);
		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);
		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(ViewAllReviewsActivity.this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		mSearchContainer=(LinearLayout) findViewById(R.id.search_box_container);
		mSearchToggler=(ImageView) findViewById(R.id.search_toggler);
		mSearchToggler.setOnClickListener(this);
		mHeaderBackButton = (ImageView) findViewById(R.id.header_btn_back);
		mHeaderBackButton.setOnClickListener(this);
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		
		Bundle bundle = getIntent().getExtras();
		mCompanyDetail = bundle.getParcelable(AppConstants.COMP_DETAIL_DATA);
		mSearchKeyword =  bundle.getString(AppConstants.GLOBAL_SEARCH_KEYWORD);
		
		if(!StringUtil.isNullOrEmpty(mSearchKeyword))
			mSearchEditText.setText(mSearchKeyword);
		
		mTxtTitle = (TextView) findViewById(R.id.avr_parent_title_field);
		mReviewsList = (ListView) findViewById(R.id.avr_reviews_list);
		((TextView) findViewById(R.id.header_title)).setText(mCompanyDetail.getTitle());
		mReviewsListAdapter = new ReviewsListAdapter(this);
		mReviewsList.setAdapter(mReviewsListAdapter);
		mCompReviewsList = new ArrayList<CompanyReview>();
		mReviewsList.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if((totalItemCount == firstVisibleItem + visibleItemCount) && (mCurrentPageNo * 10 < mTotalCount) && (!loadingNextPageData))
				{
					loadReviewsList(++mCurrentPageNo);
					loadingNextPageData = true;
				}
			}
		});
		
		mCurrentPageNo = 0;
		loadReviewsList(++mCurrentPageNo);
		
		
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
			Intent intentHome = new Intent(ViewAllReviewsActivity.this, HomeActivity.class);
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
		default:
			break;
		}

	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE||msg.arg2==Events.USER_DETAIL) {
			super.updateUI(msg);
		}else if (msg.arg2 == Events.COMP_REVIEW_LIST) {
			loadingNextPageData = false;
			if (msg.arg1 == 1) {
				--mCurrentPageNo;
				showInfoDialog((String) msg.obj);
			} else {
				CompanyReviewsListResponse response = (CompanyReviewsListResponse) msg.obj;
				if(response.getCompanyReviewList().size() > 0)
				{
					mTotalCount = response.getTotalRecords();
					mCompReviewsList.addAll(response.getCompanyReviewList());
					mReviewsListAdapter.setData(mCompReviewsList);
					mReviewsListAdapter.notifyDataSetChanged();
					
					mTxtTitle.setText(mTotalCount + " reviews found");
				}
				else if(response.getCompanyReviewList().size() == 0 && mCurrentPageNo == 1)
				{
					showFinalDialog(getResources().getString(R.string.no_result_found));
				}
				else if(response.getCompanyReviewList().size() == 0)
				{
					--mCurrentPageNo;
				}
			}
		}
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE||event==Events.USER_DETAIL) {
			super.setScreenData(screenData, event, time);
			return;
		}else if (event == Events.COMP_REVIEW_LIST) {
			handler.sendMessage((Message) screenData);
			stopSppiner();
		} 
	}
	
	private void loadReviewsList(int pageNumber)
	{
		CompanyReviewsController controller = new CompanyReviewsController(this, Events.COMP_REVIEW_LIST);
		CompanyReviewsRequest companyReviewsRequest = new CompanyReviewsRequest();
		companyReviewsRequest.setPageNumber(pageNumber);
		companyReviewsRequest.setCompanyId(mCompanyDetail.getId());
		companyReviewsRequest.setCategoryId(mCompanyDetail.getCatId());
		startSppiner();
		controller.requestService(companyReviewsRequest);
	}
}
