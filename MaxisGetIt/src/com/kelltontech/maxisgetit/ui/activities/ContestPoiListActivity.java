package com.kelltontech.maxisgetit.ui.activities;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.kelltontech.framework.model.IModel;
import com.kelltontech.framework.model.MyError;
import com.kelltontech.maxisgetit.BuildConfig;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.ContestListController;
import com.kelltontech.maxisgetit.controllers.ContestPoiController;
import com.kelltontech.maxisgetit.model.listModel.Category;
import com.kelltontech.maxisgetit.model.listModel.Distance;
import com.kelltontech.maxisgetit.model.listModel.RequestDistance;
import com.kelltontech.maxisgetit.model.listModel.ResponseList;
import com.kelltontech.maxisgetit.model.poiModel.RequestPoiSearch;
import com.kelltontech.maxisgetit.ui.adapter.ContestPoiListAdapter;
import com.kelltontech.maxisgetit.ui.widgets.ThresholdEditTextView;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;
import com.kelltontech.maxisgetit.utils.UiUtils;

/**
 * Screen shows list of POIs as selected on ContestPoiSearchActivity
 */
public class ContestPoiListActivity extends ContestBaseActivity {
	
	private final String LOG_TAG = "ContestPoiListActivity";
	
	/**
	 * Variables for POI List by category or distance (CatOrDist)
	 */
	private View					mLinearRootPoiByCatOrDist;
	private ContestPoiListAdapter	mListAdapterPoiByCatOrDist;
	private ResponseList			mResponseListPoiByCatOrDist;
	private ListView				mListViewPoiByCatOrDist;
	private Category				mSelectedCategory;
	
	/**
	 * Variables for POI search result List
	 */
	private ThresholdEditTextView	mSearchKeyEdtTxt;
	private ContestPoiListAdapter	mListAdapterPoiSearch;
	private ResponseList			mResponseListPoiSearch;
	private ListView				mListViewPoiSearch;
	
	/**
	 * Variables for pagination
	 */
	private boolean					mIsNextPageRequest;
	private int						mPageNumber;
	
	/**
	 * Variables for pagination and other functionalities
	 */
	private int						mInitialEventType;
	private int						mRequestedEventType;
	private int						mLastSuccededEventType;
	
	ImageView mLogo;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contest_poi_list_activity);
		
		((TextView)findViewById(R.id.header_title)).setText(getString(R.string.header_photo_contest));
		
		findViewById(R.id.header_btn_back).setOnClickListener(this);
		findViewById(R.id.goto_home_icon).setOnClickListener(this);
		findViewById(R.id.search_toggler).setVisibility(View.INVISIBLE);
		findViewById(R.id.show_profile_icon).setOnClickListener(this);
		
		findViewById(R.id.footer_facebook_icon).setOnClickListener(this);
		findViewById(R.id.footer_twitterIcon).setOnClickListener(this);
		
		/********** called to hide keyboard  ************/
		UiUtils.hideKeyboardOnTappingOutside(findViewById(R.id.rootLayout), this);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		mLinearRootPoiByCatOrDist = findViewById(R.id.linear_root_poi_by_category_or_distance);
		
		mLogo = (ImageView) findViewById(R.id.logo);
		mLogo.setOnClickListener(ContestPoiListActivity.this);
		mListViewPoiByCatOrDist = (ListView) findViewById(R.id.listView);
		mListViewPoiByCatOrDist.setCacheColorHint(Color.TRANSPARENT);
		mListViewPoiByCatOrDist.requestFocus(0);
		
		mListViewPoiByCatOrDist.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				IModel iModel = mResponseListPoiByCatOrDist.getList().get(position);
				if( mInitialEventType == Events.DISTANCE_LIST_EVENT ) {
					startContestImageUploadActivity(iModel);
				} else {
					mSelectedCategory = (Category) iModel;
					mRequestedEventType = Events.DISTANCE_LIST_EVENT;
					onLoad();
				}
			}
		});

		mListViewPoiByCatOrDist.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView listView, int scrollState) {
				// nothing to do here
			}

			@Override
			public void onScroll(AbsListView listView, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
				if( mInitialEventType == Events.CATEGORY_LIST_EVENT ) {
					return;
				}
				if( mResponseListPoiByCatOrDist == null || totalItemCount == (mResponseListPoiByCatOrDist).getTotalRecord() ) {
					return;
				}
				if( BuildConfig.DEBUG ) {
					Log.i(LOG_TAG,"first: " + firstVisibleItem + ", visible: " + visibleItemCount + ", total: " + totalItemCount );
				}
				if( mIsNextPageRequest || firstVisibleItem + visibleItemCount < totalItemCount ) {
					return;
				}
				startSppiner();
				mPageNumber = totalItemCount/AppConstants.PAGE_SIZE_POI_LIST_BY_DISTANCE+1;
				mIsNextPageRequest = true;
				mRequestedEventType = mInitialEventType;
				onLoad();
			}
		});

		mListViewPoiSearch=(ListView) findViewById(R.id.searchListView);
		mListViewPoiSearch.setCacheColorHint(Color.TRANSPARENT);
		mListViewPoiSearch.requestFocus(0);
		mListViewPoiSearch.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				startContestImageUploadActivity(mResponseListPoiSearch.getList().get(position));
			}
		});

		mListViewPoiSearch.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView listView, int scrollState) {
				// nothing to do here
			}
			@Override
			public void onScroll(AbsListView listView, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
				if( mResponseListPoiSearch == null || totalItemCount == (mResponseListPoiSearch).getTotalRecord() ) {
					return;
				}
				if( BuildConfig.DEBUG ) {
					Log.i(LOG_TAG,"first: " + firstVisibleItem + ", visible: " + visibleItemCount + ", total: " + totalItemCount );
				}
				if( mIsNextPageRequest || firstVisibleItem + visibleItemCount < totalItemCount ) {
					return;
				}
				startSppiner();
				mPageNumber = totalItemCount / AppConstants.PAGE_SIZE_POI_LIST_SEACH_RESULTS + 1;
				mIsNextPageRequest = true;
				mRequestedEventType = Events.POI_SEARCH_EVENT;
				onLoad();
			}
		});

		mSearchKeyEdtTxt = (ThresholdEditTextView)findViewById(R.id.searchEdtTxt);
		mSearchKeyEdtTxt.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if( actionId == EditorInfo.IME_ACTION_SEARCH) {
					String _key = mSearchKeyEdtTxt.getText().toString().trim();
					if( _key.length() == 0 ) {
						Toast.makeText(ContestPoiListActivity.this, getString(R.string.input_search), Toast.LENGTH_SHORT).show();
					} else {
						mRequestedEventType = Events.POI_SEARCH_EVENT;
						onLoad();
					}
					return true;
				}
				return false;
			}
		});
		
		mInitialEventType = getIntent().getIntExtra(AppConstants.LIST_RESPONSE_TYPE, 0);
		
		if( mInitialEventType == Events.POI_SEARCH_EVENT ) {
			mResponseListPoiSearch = getIntent().getParcelableExtra(AppConstants.LIST_RESPONSE_PARCEL);
			String initialSearchKeyword = getIntent().getStringExtra(AppConstants.EXTRA_SEARCH_KEYWORD);
			mSearchKeyEdtTxt.setText(initialSearchKeyword);
		} else {
			mResponseListPoiByCatOrDist = getIntent().getParcelableExtra(AppConstants.LIST_RESPONSE_PARCEL);
			mSelectedCategory = getIntent().getParcelableExtra(AppConstants.EXTRA_SELECTED_CATEGORY);
			
			mListAdapterPoiByCatOrDist = new ContestPoiListAdapter(this, mResponseListPoiByCatOrDist, mInitialEventType);
			mListViewPoiByCatOrDist.setAdapter(mListAdapterPoiByCatOrDist);
			
			TextView textViewTagText = (TextView) findViewById(R.id.tagTaxt);
			textViewTagText.setText(mInitialEventType == Events.CATEGORY_LIST_EVENT?getText(R.string.list_by_category):getText(R.string.list_by_distance));
		}
		
		mListAdapterPoiSearch = new ContestPoiListAdapter(this, mResponseListPoiSearch, Events.POI_SEARCH_EVENT);
		mListViewPoiSearch.setAdapter(mListAdapterPoiSearch);
		
		mLastSuccededEventType = mInitialEventType;
		switchScreenMode();
	}

	/**
	 * @param iModel
	 */
	private void startContestImageUploadActivity(IModel iModel) {
		Distance distance = (Distance) iModel;
		Intent poiListIntent = new Intent(ContestPoiListActivity.this,ContestUploadImageActivity.class);
		poiListIntent.putExtra(AppConstants.CID_KEY, distance.getcId());
		poiListIntent.putExtra(AppConstants.CATEGORY_ID_KEY, distance.getCategoryId());
		poiListIntent.putExtra(AppConstants.COMPANY_NAME_KEY, distance.getCompanyName());
		startActivity(poiListIntent);
	}

	/**
	 * 
	 * @param eventType
	 * @param obj
	 */
	@Override
	protected void onLoad() {
		getLocation();
		switch (mRequestedEventType) {
		case Events.DISTANCE_LIST_EVENT: 
		case Events.CATEGORY_LIST_EVENT:
		{
			//showDialog(CustomDialog.PROGRESS_DIALOG);
			startSppiner();
			ContestListController listController1=new ContestListController(this, mRequestedEventType);
			RequestDistance distance=new RequestDistance();
			distance.setPageNumber(mPageNumber==0?1:mPageNumber);
			distance.setRecordsPerPage(AppConstants.PAGE_SIZE_POI_LIST_BY_DISTANCE);
			distance.setLatitude(mLattitude);
			distance.setLongitude(mLongitude);
			if( mRequestedEventType == Events.DISTANCE_LIST_EVENT && mSelectedCategory != null ) {
				try {
					distance.setCategoryId(Integer.parseInt(mSelectedCategory.getCategoryId()));
				} catch( Exception e) {
					// ignore
					e.printStackTrace();
				}
			}
			listController1.requestService(distance);
			break;
		}
		case Events.POI_SEARCH_EVENT:
		{
			UiUtils.hideSoftKeyboard(this);
			//showDialog(CustomDialog.PROGRESS_DIALOG);
			startSppiner();
			ContestPoiController contestPoiController=new ContestPoiController(this, Events.POI_SEARCH_EVENT);
			RequestPoiSearch requestPoiSearch=new RequestPoiSearch();
			requestPoiSearch.setLatitude(mLattitude);
			requestPoiSearch.setLongitude(mLongitude);
			String searchTxt=mSearchKeyEdtTxt.getText().toString().trim();
			requestPoiSearch.setSearchText(searchTxt);
			
			HashMap<String,String>	map = new HashMap<String,String>();
			map.put(FlurryEventsConstants.CONTEST_SEARCH_TEXT, searchTxt);
			AnalyticsHelper.logEvent(FlurryEventsConstants.CONTEST_POI_SEARCH,map);
			requestPoiSearch.setPageNumber(mPageNumber==0?1:mPageNumber);
			requestPoiSearch.setRecordsPerPage(AppConstants.PAGE_SIZE_POI_LIST_SEACH_RESULTS);
			contestPoiController.requestService(requestPoiSearch);
			break;
		}
		}	
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goto_home_icon: {
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			showHomeScreen();
			break;
		}
		case R.id.header_btn_back: {
			AnalyticsHelper.logEvent(FlurryEventsConstants.BACK_CLICK);
			this.onBackPressed();
			break;
		}
		case R.id.show_profile_icon: {
			AnalyticsHelper.logEvent(FlurryEventsConstants.SHOW_PROFILE_CLICK);
			onProfileClick();
			break;
		}
		case R.id.footer_facebook_icon: {
			AnalyticsHelper.logEvent(FlurryEventsConstants.FACEBOOK_CLICK);
			checkPreferenceAndOpenBrowser(AppConstants.FB_PAGE_URL);
			break;
		}
		case R.id.footer_twitterIcon: {
			AnalyticsHelper.logEvent(FlurryEventsConstants.TWITTER_CLICK);
			checkPreferenceAndOpenBrowser(AppConstants.TWITTER_PAGE_URL);
			break;
		}
		case R.id.logo:
			startActivity(new Intent(ContestPoiListActivity.this, GetItInfoActivity.class));
			break;
		}
	}

	@Override
	public void setScreenData(Object screenData, int event, long hitTime) {
		if (event == Events.USER_DETAIL) {
			super.setScreenData(screenData, event, hitTime);
		} else if(mHitTime == hitTime) {
			Message message=new Message();
			message.obj=screenData;
			message.what=event;
			handler.sendMessage(message);
		}
	}

	private Handler handler=new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			if(msg.obj instanceof MyError)
			{
				//removeDialog(CustomDialog.PROGRESS_DIALOG);
				stopSppiner();
				switch (((MyError)msg.obj).getErrorcode()) {
				case MyError.NETWORK_NOT_AVAILABLE:
					Toast.makeText(getApplicationContext(),"Network not available.", Toast.LENGTH_LONG).show();
					break;
				case  MyError.EXCEPTION:
				case MyError.UNDEFINED:
					Toast.makeText(getApplicationContext(),"Server not responding.", Toast.LENGTH_LONG).show();
					break;
				}
			}
			else if(msg.obj instanceof ResponseList && mRequestedEventType == Events.DISTANCE_LIST_EVENT )
			{
				ResponseList responselist=(ResponseList) msg.obj;
				if(responselist.getList()!=null&&responselist.getList().size()>0)
				{
					if( mIsNextPageRequest ) {
						mLastSuccededEventType = mRequestedEventType;
						mResponseListPoiByCatOrDist.getList().addAll(responselist.getList());
						mListAdapterPoiByCatOrDist.notifyDataSetChanged();
					} else {
						Intent poiListIntent = new Intent(ContestPoiListActivity.this,ContestPoiListActivity.class);
						poiListIntent.putExtra(AppConstants.EXTRA_SELECTED_CATEGORY, mSelectedCategory);
						poiListIntent.putExtra(AppConstants.LIST_RESPONSE_PARCEL, responselist);
						poiListIntent.putExtra(AppConstants.LIST_RESPONSE_TYPE, responselist.getEventType());
						startActivity(poiListIntent);
					}
					stopSppiner();
				}
				else
				{
					//removeDialog(CustomDialog.PROGRESS_DIALOG);
					stopSppiner();
					Toast.makeText(getApplicationContext(),getString(R.string.server_not_responding), Toast.LENGTH_LONG).show();	
				}
			}
			else if(msg.obj instanceof ResponseList && mRequestedEventType == Events.POI_SEARCH_EVENT )
			{
				//removeDialog(CustomDialog.PROGRESS_DIALOG);
				stopSppiner();
				ResponseList responselist = (ResponseList) msg.obj;
				if(responselist.getList() != null && responselist.getList().size() > 0)
				{
					if( mIsNextPageRequest ) {
						mListAdapterPoiSearch.addData(responselist);
						mListAdapterPoiSearch.notifyDataSetChanged();
					} else {
						mResponseListPoiSearch = responselist;
						mListAdapterPoiSearch.setData(responselist);
						mListAdapterPoiSearch.notifyDataSetChanged();
						mLastSuccededEventType = mRequestedEventType;
						switchScreenMode();
					}
				}
				else {
					if( mIsNextPageRequest ) {
						Toast.makeText(ContestPoiListActivity.this, getString(R.string.toast_some_error_try_again), Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(ContestPoiListActivity.this, getString(R.string.no_result_found), Toast.LENGTH_LONG).show();
//						mResponseListPoiSearch = responselist;
//						mListAdapterPoiSearch.clearData();
//						mListAdapterPoiSearch.notifyDataSetChanged();
//						switchScreenMode();
					}
				}
			}
			
			mIsNextPageRequest = false;
		}
	};

	@Override
	public Activity getMyActivityReference() {
		return this;
	}

	@Override
	public void onBackPressed() {
		if( mLastSuccededEventType != mInitialEventType ) {
			mLastSuccededEventType = mInitialEventType;
			switchScreenMode();
		} else {
			super.onBackPressed();
		}
	}
	
	/**
	 * Switch mode of screen between Events.POI_SEARCH_EVENT,
	 * Events.DISTANCE_LIST_EVENT and Events.CATEGORY_LIST_EVENT
	 * @return true if mode is switched
	 */
	private boolean switchScreenMode() {
		switch(mLastSuccededEventType) {
		case Events.POI_SEARCH_EVENT: {
			mListViewPoiSearch.setVisibility(View.VISIBLE);
			mLinearRootPoiByCatOrDist.setVisibility(View.GONE);
			break;
		}
		case Events.DISTANCE_LIST_EVENT:
		case Events.CATEGORY_LIST_EVENT: {
			mListViewPoiSearch.setVisibility(View.GONE);
			mLinearRootPoiByCatOrDist.setVisibility(View.VISIBLE);
			break;
		}
		}
		return true;
	}
}
