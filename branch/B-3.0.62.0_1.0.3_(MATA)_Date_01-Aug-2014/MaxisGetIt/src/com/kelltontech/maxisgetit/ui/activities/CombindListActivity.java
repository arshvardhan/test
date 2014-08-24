package com.kelltontech.maxisgetit.ui.activities;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.kelltontech.framework.imageloader.ImageLoader;
import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.framework.utils.UiUtils;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.adapters.BannerViewAdapter;
import com.kelltontech.maxisgetit.adapters.CompanyListAdapter;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.controllers.CombindListingController;
import com.kelltontech.maxisgetit.controllers.CompanyDetailController;
import com.kelltontech.maxisgetit.controllers.RefineAttributeController;
import com.kelltontech.maxisgetit.controllers.SubCategoryController;
import com.kelltontech.maxisgetit.dao.Banner;
import com.kelltontech.maxisgetit.dao.CategoryGroup;
import com.kelltontech.maxisgetit.dao.CategoryRefine;
import com.kelltontech.maxisgetit.dao.CityOrLocality;
import com.kelltontech.maxisgetit.dao.CompanyDesc;
import com.kelltontech.maxisgetit.dao.CompanyDetail;
import com.kelltontech.maxisgetit.dao.GPS_Data;
import com.kelltontech.maxisgetit.dao.SearchAttribute;
import com.kelltontech.maxisgetit.dao.SelectorDAO;
import com.kelltontech.maxisgetit.requests.CombinedListRequest;
import com.kelltontech.maxisgetit.requests.DetailRequest;
import com.kelltontech.maxisgetit.requests.RefineSearchRequest;
import com.kelltontech.maxisgetit.response.CompanyListResponse;
import com.kelltontech.maxisgetit.response.GenralListResponse;
import com.kelltontech.maxisgetit.response.RefineCategoryResponse;
import com.kelltontech.maxisgetit.response.RefineSelectorResponse;
import com.kelltontech.maxisgetit.response.SubCategoryResponse;
import com.kelltontech.maxisgetit.ui.widgets.CustomDialog;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class CombindListActivity extends MaxisMainActivity implements OnClickListener {

	private LinearLayout 			mDealsFooter;
	private LinearLayout 			mListFooter;
	private LinearLayout 			advanceSearchLayout;
	private LinearLayout 			mSearchContainer;
	private LinearLayout 			wholeSearchBoxContainer;
	private RelativeLayout 			bannerViewLayout;
	private LinearLayout 			circleIndicator;
	private TextView 				mRecordsFoundView;
	private TextView 				mRefineSearchView;
	private TextView 				mDealBtnView;
	private TextView 				mViewAllOnMap;
	private TextView 				currentCity, currentLocality;
	private TextView 				mainSearchButton;
	private TextView 				mHeaderTitle;
	private TextView 				nearMe;

	private ImageView 				mSearchToggler;
	private ImageView 				mSearchBtn;
	private ImageView 				upArrow;
	private ImageView 				mHomeIconView;
	private ImageView 				mProfileIconView;
	private ImageView 				mHeaderBackButton;

	private ListView 				mCompanyList;
	private EditText 				mSearchEditText;
	private Spinner 				mDistanceChooser;
	private TextView 				mSearchCriteriaChooser;
	private ViewPager	 			bannerView;
	private CombinedListRequest 	mClRequest;
	private CombinedListRequest 	mdealRequest;
	private RefineSelectorResponse 	mSelctorResp;
	private RefineCategoryResponse 	mCatResponse;
	private CompanyListResponse 	mClResponse;
	private CompanyListResponse 	mDealResponse;
	private SelectorDAO 			mLocalitySelectorDao;
	ArrayList<CityOrLocality> 		localityList;
	ArrayList<String> 				ids = new ArrayList<String>();
	ArrayList<String> 				selectedLocalityindex;
	ArrayList<CityOrLocality> 		cityList;
	private ArrayList<String> 		selectedLocalityItemsforHeader = new ArrayList<String>();
	private ArrayList<String> 		selectedLocalityItems = new ArrayList<String>();
	private ArrayList<String> 		cityListString	= new ArrayList<String>();
	private ArrayList<String> 		localityItems;
	private int 					totalBanners;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	ArrayList<String> 				filter = new ArrayList(Arrays.asList("1 km", "2 km", "3 km", "4 km", "5 km", "10 km", "15 km", "20 km", "All"));
	private CompanyListAdapter 		mCompListAdapter;
	private BannerViewAdapter 		bannerViewAdapter;
	private ArrayAdapter<String> 	mDistanceFilterAdapter;
	private String 					selectedCityforHeader = "Entire Malaysia";
	private String 					selectedCity = "Entire Malaysia";
	private String 					mPreviousDistance;
	private String 					mCategoryThumbUrl;
	private String 					mPreviousSelectedSearchCriteria = "";
	private boolean 				loadingNextPageData;
	private boolean 				isModifySearchDialogOpen;
	private boolean 				mScrollUp;
	private boolean 				mIsFreshSearch = true;
	private boolean 				isAdvanceSearchLayoutOpen = false;
	private boolean 				stopSliding = false;
	boolean 						isFirstTime = false;
	boolean 						isSuccessfull = false;
	boolean 						isFromNearMe = false;
	boolean 						isFromSearch = false;
	private int 					city_id = -1;
	private int 					mPreviousSelectedIndex;
	private int 					previousState, currentState;
	private int 					flipperVisibleItemPosition = 0;
	private Runnable 				animateViewPager;
	private Handler 				bannerHandler;
	private static final long 		ANIM_VIEWPAGER_DELAY = 5000;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				mIsFreshSearch = false;
				mRefineSearchView.setText(getResources().getString(R.string.cl_modify_search));
				Bundle bundle 						= 		data.getExtras();
				mClResponse 						= 		bundle.getParcelable(AppConstants.COMP_LIST_DATA);
				mLocalitySelectorDao 				= 		bundle.getParcelable(AppConstants.LOCALITY_DAO_DATA);
				mClRequest 							= 		bundle.getParcelable(AppConstants.DATA_LIST_REQUEST);
				mSelctorResp 						= 		bundle.getParcelable(AppConstants.REFINE_ATTR_RESPONSE);
				mCatResponse 						= 		bundle.getParcelable(AppConstants.REFINE_CAT_RESPONSE);
				mRecordsFoundView.setText(mClResponse.getTotalrecordFound() + " " + getResources().getString(R.string.record_found));
				mClRequest.setSearchRefined(true);
				showHideBanner();
				showHideSearchCriteriaChooser();
				addAnEmptyRow();

				mIsFreshSearch = (mClResponse != null && mClResponse.getCategoryList() != null && mClResponse.getCategoryList().size() > 1) ? true : false;

				if (mIsFreshSearch) {
					if (mClResponse.getCategoryList() != null && mClResponse.getCategoryList().size() == 1) {
						mIsFreshSearch 				= 		false;
						CategoryRefine tempCatref 	= 		mClResponse.getCategoryList().get(0);
						mClRequest.setSelectedCategoryBySearch(tempCatref.getCategoryId(), tempCatref.getCategoryTitle());
					} else
						mRefineSearchView.setText(getResources().getString(R.string.cl_filter_by));
				}

				ArrayList<CompanyDesc> compListData = 		mClResponse.getCompanyArrayList();
				mCompListAdapter.setData(compListData);
				mCompListAdapter.notifyDataSetChanged();
				mCompanyList.setAdapter(mCompListAdapter);
				if ((!StringUtil.isNullOrEmpty(mClRequest.getStampId())) && (!StringUtil.isNullOrEmpty(mClRequest.getSearchCriteria()))) {
					mDistanceChooser.setVisibility(View.GONE);
					nearMe.setVisibility(View.GONE);
					mSearchCriteriaChooser.setVisibility(View.GONE);
				} else {
					if (MaxisMainActivity.isCitySelected) {
						mDistanceChooser.setVisibility(View.GONE);
						nearMe.setVisibility(View.VISIBLE);
					} else {
						if (GPS_Data.getLatitude() == 0 || GPS_Data.getLongitude() == 0) {
							nearMe.setVisibility(View.VISIBLE);
							mDistanceChooser.setVisibility(View.GONE);
						} else {
							nearMe.setVisibility(View.GONE);
							mDistanceChooser.setVisibility(View.VISIBLE);
						}
						isSuccessfull 				= 		true;
						String search_distance 		= 		mClResponse.getSearch_distance();
						int index 					= 		setDistanceFilter(search_distance);
						mDistanceChooser.setSelection(index);
					}
				}
			}
		} else if (resultCode == RESULT_OK && requestCode == AppConstants.CITY_REQUEST) {
			if (!selectedCityforHeader.equalsIgnoreCase(data.getStringExtra("CITY_NAME"))) {
				localityItems 						= 		null;
				ids 								= 		null;
				selectedLocalityindex 				= 		null;
				currentLocality.setText("Choose your Area");
			}
			selectedCityforHeader 					= 		data.getStringExtra("CITY_NAME");
			selectedCity 							= 		data.getStringExtra("CITY_NAME");
			currentCity.setText(Html.fromHtml("in " + "<b>" + selectedCityforHeader + "</b>"));
			int index 								= 		data.getIntExtra("CITY_INDEX", 0);
			if (index == -1) 
				city_id = -1;
			else 
				city_id = cityList.get(index).getId();
		} else if (resultCode == RESULT_OK && requestCode == AppConstants.LOCALITY_REQUEST) {
			String locality = "";
			selectedLocalityItemsforHeader = data.getStringArrayListExtra("SELECTED_LOCALITIES");
			selectedLocalityItems = selectedLocalityItemsforHeader;
			selectedLocalityindex = data.getStringArrayListExtra("SELECTED_LOCALITIES_INDEX");
			if (selectedLocalityItemsforHeader != null && selectedLocalityItemsforHeader.size() > 0) {
				for (int i = 0; i < selectedLocalityItemsforHeader.size(); i++) {
					if (i == selectedLocalityItemsforHeader.size() - 1)
						locality += selectedLocalityItemsforHeader.get(i);
					else
						locality += selectedLocalityItemsforHeader.get(i) + ",";
				}
				currentLocality.setText(Html.fromHtml("Your Selected Area " + "<b>" + locality + "</b>"));
			} else
				currentLocality.setText("Choose your Area");
			ids = new ArrayList<String>();
			if (selectedLocalityindex != null && selectedLocalityindex.size() > 0) {
				for (int i = 0; i < selectedLocalityindex.size(); i++) {
					ids.add(String.valueOf(localityList.get(Integer.parseInt(selectedLocalityindex.get(i))).getId()));
				}
			}
		} else if (resultCode == RESULT_OK && requestCode == AppConstants.SEARCH_CRITERIA_REQUEST) {
			try {
				SearchAttribute searchAttribute = ((data.getParcelableExtra("ChosenSearchCriteria") == null) ? null : (SearchAttribute) data.getParcelableExtra("ChosenSearchCriteria"));
				String selectedItem = searchAttribute.getType();
				mPreviousSelectedSearchCriteria = StringUtil.isNullOrEmpty(mClResponse.getDisplayIn().getType()) ? "" : mClResponse.getDisplayIn().getType();
				if(!selectedItem.equals(mPreviousSelectedSearchCriteria)) {
					for (SearchAttribute searchAttrib : mClResponse.getSearchAttributeList()) {
						if(selectedItem.equals(searchAttrib.getType())) {
							mClRequest.setSearchIn(searchAttrib.getType());
							mClRequest.setKeywordOrCategoryId(searchAttrib.getKeyword());
							break;
						}
					}
					mClRequest.setPageNumber(1);
					CombindListingController listingController = new CombindListingController(CombindListActivity.this, Events.COMBIND_LISTING_NEW_LISTING_PAGE);
					startSppiner();
					listingController.requestService(mClRequest);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_combind_list);
		AnalyticsHelper.logEvent(FlurryEventsConstants.APPLICATION_COMBINED_LIST, true);
		UiUtils.hideKeyboardOnTappingOutside(findViewById(R.id.cl_root_layout),this);
		ImageLoader.initialize(CombindListActivity.this);

		mDealsFooter 			= (LinearLayout) 	findViewById(R.id.cl_deals_footer);
		mListFooter 			= (LinearLayout) 	findViewById(R.id.cl_company_list_footer);
		advanceSearchLayout 	= (LinearLayout) 	findViewById(R.id.advanceSearch);		
		wholeSearchBoxContainer = (LinearLayout) 	findViewById(R.id.whole_search_box_container);
		mSearchContainer 		= (LinearLayout) 	findViewById(R.id.search_box_container);
		circleIndicator 		= (LinearLayout) 	findViewById(R.id.indicatorlinearlayout);
		bannerViewLayout 		= (RelativeLayout) 	findViewById(R.id.bannerView_LnrLayout);
		bannerView 				= (ViewPager) 		findViewById(R.id.subcategory_banner);
		mProfileIconView 		= (ImageView) 		findViewById(R.id.show_profile_icon);
		mHeaderBackButton 		= (ImageView) 		findViewById(R.id.header_btn_back);
		mHomeIconView 			= (ImageView) 		findViewById(R.id.goto_home_icon);
		mSearchBtn 				= (ImageView) 		findViewById(R.id.search_icon_button);
		upArrow 				= (ImageView) 		findViewById(R.id.upArrow);
		mSearchEditText 		= (EditText) 		findViewById(R.id.search_box);
		mRefineSearchView 		= (TextView) 		findViewById(R.id.col_refine_search);
		mViewAllOnMap 			= (TextView) 		findViewById(R.id.col_view_on_map);
		currentCity 			= (TextView) 		findViewById(R.id.currentCity);
		currentLocality 		= (TextView) 		findViewById(R.id.currentLocality);
		mainSearchButton 		= (TextView) 		findViewById(R.id.mainSearchButton);
		mHeaderTitle 			= (TextView) 		findViewById(R.id.header_title);
		mRecordsFoundView 		= (TextView) 		findViewById(R.id.col_records_found);
		mDealBtnView 			= (TextView) 		findViewById(R.id.col_deal_btn);
		nearMe 					= (TextView) 		findViewById(R.id.nearMe);
		mCompanyList 			= (ListView) 		findViewById(R.id.col_company_list);
		mSearchToggler 			= (ImageView) 		findViewById(R.id.search_toggler);
		mDistanceChooser 		= (Spinner) 		findViewById(R.id.distance_filter_chooser);
		mSearchCriteriaChooser 	= (TextView) 		findViewById(R.id.search_criteria_chooser);
		findViewById(R.id.col_view_on_map1).setOnClickListener(this);
		findViewById(R.id.col_refine_search1).setOnClickListener(this);
		findViewById(R.id.play_pasue_icon).setOnClickListener(this);

		mProfileIconView.setOnClickListener(this);
		mHeaderBackButton.setOnClickListener(this);
		mHomeIconView.setOnClickListener(this);
		mSearchBtn.setOnClickListener(CombindListActivity.this);
		upArrow.setOnClickListener(this);
		mRefineSearchView.setOnClickListener(this);
		mViewAllOnMap.setOnClickListener(this);
		currentCity.setOnClickListener(this);
		currentLocality.setOnClickListener(this);
		mainSearchButton.setOnClickListener(this);
		mDealBtnView.setOnClickListener(this);
		mSearchToggler.setOnClickListener(this);
		nearMe.setOnClickListener(this);

		advanceSearchLayout.setVisibility(View.GONE);
		currentCity.setText(Html.fromHtml("in " + "<b>" + selectedCityforHeader + "</b>"));

		Bundle bundle 	= getIntent().getExtras();
		mClRequest 		= bundle.getParcelable(AppConstants.DATA_LIST_REQUEST);
		mClResponse 	= bundle.getParcelable(AppConstants.COMP_LIST_DATA);

		if (mClRequest != null && !StringUtil.isNullOrEmpty(mClRequest.getCategoryTitle())) {
			mHeaderTitle.setText(Html.fromHtml(mClRequest.getCategoryTitle()));
			mCategoryThumbUrl = mClRequest.getParentThumbUrl();
		}

		if (mClRequest != null && !StringUtil.isNullOrEmpty(mClRequest.getPostJsonPayload())) {
			try {
				JSONObject jsonObject = new JSONObject(mClRequest.getPostJsonPayload());
				JSONObject jObject = jsonObject.getJSONObject("city");
				int cityId = jObject.getInt("city_id");
				String city_name = jObject.getString("city_name");
				selectedCityforHeader = city_name;
				selectedCity = city_name;
				city_id = cityId;
				currentCity.setText(Html.fromHtml("in " + "<b>" + selectedCityforHeader + "</b>"));
				JSONArray jArray = jsonObject.getJSONArray("locality");
				ids.clear();
				selectedLocalityItemsforHeader.clear();
				for (int i = 0; i < jArray.length(); i++) {
					try {
						JSONObject object = jArray.getJSONObject(i);
						int locality_id = object.getInt("locality_id");
						String locality_name = object.getString("locality_name");
						ids.add(locality_id + "");
						selectedLocalityItemsforHeader.add(locality_name);
						selectedLocalityItems.add(locality_name);
					} catch (JSONException e) { }
				}
				String locality = "";
				if (selectedLocalityItemsforHeader != null && selectedLocalityItemsforHeader.size() > 0) {
					for (int i = 0; i < selectedLocalityItemsforHeader.size(); i++) {
						if (i == selectedLocalityItemsforHeader.size() - 1) 
							locality += selectedLocalityItemsforHeader.get(i);
						else
							locality += selectedLocalityItemsforHeader.get(i) + ",";
					}
					currentLocality.setText(Html.fromHtml("Your Selected Area " + "<b>" + locality + "</b>"));
				} else
					currentLocality.setText("Choose your Area");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		if (mClRequest.isBySearch()) {
			if (mIsFreshSearch) {
				if (mClResponse.getCategoryList() != null && mClResponse.getCategoryList().size() == 1) {
					mIsFreshSearch 				= 		false;
					CategoryRefine tempCatref 	= 		mClResponse.getCategoryList().get(0);
					mClRequest.setSelectedCategoryBySearch(tempCatref.getCategoryId(), tempCatref.getCategoryTitle());
				} else
					mRefineSearchView.setText(getResources().getString(R.string.cl_filter_by));
			}
			if (mClRequest != null && !StringUtil.isNullOrEmpty(mClRequest.getKeywordOrCategoryId())) {
				mHeaderTitle.setText(Html.fromHtml(mClRequest.getKeywordOrCategoryId()));
				mSearchEditText.setText(Html.fromHtml(mClRequest.getKeywordOrCategoryId()));
			}
		} else if ((mClRequest.getGroupActionType().trim().equalsIgnoreCase(AppConstants.GROUP_ACTION_TYPE_CATEGORY_LIST_FOR_GROUP) 
				&& (mClRequest.getGroupType().trim().equalsIgnoreCase(AppConstants.GROUP_TYPE_CATEGORY)))) {
			if (mIsFreshSearch) {
				if (mClResponse.getCategoryList() != null && mClResponse.getCategoryList().size() == 1) {
					mIsFreshSearch 				= 		false;
					CategoryRefine tempCatref 	= 		mClResponse.getCategoryList().get(0);
					mClRequest.setSelectedCategoryBySearch(tempCatref.getCategoryId(),tempCatref.getCategoryTitle());
				} else
					mRefineSearchView.setText(getResources().getString(R.string.cl_filter_by));
			}
		} else
			mHeaderTitle.setVisibility(View.VISIBLE);

		if (mClRequest.isCompanyListing()) {
			mDealsFooter.setVisibility(View.GONE);
			mListFooter.setVisibility(View.VISIBLE);
		} else {
			mDealsFooter.setVisibility(View.VISIBLE);
			mListFooter.setVisibility(View.GONE);
		}

		mRecordsFoundView.setText(mClResponse.getTotalrecordFound() + " " + getResources().getString(R.string.record_found));

		ArrayList<CompanyDesc> compListData 	= 		mClResponse.getCompanyArrayList();
		mCompListAdapter 						= 		new CompanyListAdapter(CombindListActivity.this, mClRequest.isCompanyListing());
		mCompListAdapter.setData(compListData);
		mCompListAdapter.notifyDataSetChanged();
		mCompanyList.setAdapter(mCompListAdapter);

		mCompanyList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 == (mCompListAdapter.getCount() - 1) && arg2 != 0 && mClResponse.getTotalrecordFound() > 10) { 
				} else {
					CompanyDetailController controller = new CompanyDetailController(CombindListActivity.this, Events.COMPANY_DETAIL);
					String id = ((CompanyDesc) mCompListAdapter.getItem(arg2)).getCompId();
					DetailRequest detailRequest = new DetailRequest(CombindListActivity.this, id, !mClRequest.isCompanyListing(),((CompanyDesc) mCompListAdapter.getItem(arg2)).getCat_id());
					startSppiner();
					controller.requestService(detailRequest);
				}
			}
		});

		mCompanyList.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView listView, int scrollState) { }
			@Override
			public void onScroll(AbsListView listView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				Log.w("", "firstVisibleItem " + firstVisibleItem + " visibleItemCount " + visibleItemCount + " totalItemCount " + totalItemCount);
				int number = firstVisibleItem + visibleItemCount;
				if (mClResponse != null) {
					if ((number) % mClResponse.getRecordsPerPage() == 0
							&& number > 0
							&& number == totalItemCount
							&& totalItemCount < mClResponse.getTotalrecordFound()) {
						Log.d("maxis", "list detail before next page" + mClResponse.getPageNumber() + "  " + totalItemCount + " " + mClResponse.getCompanyArrayList().size());
						if (loadingNextPageData)
							return;
						loadingNextPageData = true;
						if (mClResponse.getPageNumber() < AppConstants.MAX_RECORD_COUNT / 10)
							loadPageData(mClResponse.getPageNumber() + 1);
					} else if (number >= AppConstants.MAX_RECORD_COUNT && !isModifySearchDialogOpen && mScrollUp && mClResponse.getTotalrecordFound() > 100) {
						showConfirmationDialog(CustomDialog.CONFIRMATION_DIALOG, getResources().getString(R.string.modify_to_filter));
						isModifySearchDialogOpen = true;
						AnalyticsHelper.logEvent(FlurryEventsConstants.COMBINED_LIST_VISITED_ITEMS_EXCEEDED_70);
					}
				}
			}
		});

		mCompanyList.setOnTouchListener(new OnTouchListener() {
			private float mInitialX;
			private float mInitialY;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mInitialX = event.getX();
					mInitialY = event.getY();
					mScrollUp = false;
					// mScrollDown = true;
					return true;
				case MotionEvent.ACTION_MOVE:
					final float x = event.getX();
					final float y = event.getY();
					final float yDiff = y - mInitialY;
					if (yDiff > 0.0) {
						Log.d("maxis", "SCROLL DOWN");
						mScrollUp = false;
						break;
					} else if (yDiff < 0.0) {
						Log.d("maxis", "SCROLL up");
						mScrollUp = true;
						break;
					}
					break;
				}
				return false;
			}
		});

		mSearchEditText.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (!isAdvanceSearchLayoutOpen) {
					isAdvanceSearchLayoutOpen = true;
					advanceSearchLayout.setVisibility(View.VISIBLE);
				}
				return false;
			}
		});

		addAnEmptyRow();

		mSearchCriteriaChooser.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent searchCriteriaIntent = new Intent(CombindListActivity.this, SearchCriteriaChooserActivity.class);
				searchCriteriaIntent.putParcelableArrayListExtra("SearchCriteria", mClResponse.getSearchAttributeList());
				searchCriteriaIntent.putExtra("displayIn", mClResponse.getDisplayIn().getType().trim());
				startActivityForResult(searchCriteriaIntent, AppConstants.SEARCH_CRITERIA_REQUEST);
			}
		});

		showHideSearchCriteriaChooser();

		if ((!StringUtil.isNullOrEmpty(mClRequest.getStampId())) /*&& (!StringUtil.isNullOrEmpty(mClRequest.getSearchCriteria()))*/) {
			mDistanceChooser.setVisibility(View.GONE);
			nearMe.setVisibility(View.GONE);
			mSearchCriteriaChooser.setVisibility(View.GONE);
		} else {
			if (MaxisMainActivity.isCitySelected || mClRequest.getLongitude() == 0.0 || mClRequest.getLatitude() == 0.0) {
				mDistanceChooser.setVisibility(View.GONE);
				nearMe.setVisibility(View.VISIBLE);
			} else {
				nearMe.setVisibility(View.GONE);
				mDistanceChooser.setVisibility(View.VISIBLE);
			}
		}

		mDistanceFilterAdapter = new ArrayAdapter<String>(CombindListActivity.this, R.layout.spinner_item, filter);
		mDistanceChooser.setAdapter(mDistanceFilterAdapter);
		String search_distance = mClResponse.getSearch_distance();
		mDistanceChooser.setSelection(setDistanceFilter(search_distance));
		mDistanceChooser.setOnItemSelectedListener(new OnItemSelectedListener() {
			String distance = "";
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				if (filter.get(pos).equalsIgnoreCase("All")) {
					distance = "1000000";
				} else {
					distance = filter.get(pos).replace(" km", "");
				}
				if (isFirstTime) {
					if (mPreviousSelectedIndex != pos && !isFromNearMe) {
						mClRequest.setSearch_distance(distance);
						mClRequest.setPageNumber(1);
						CombindListingController listingController = new CombindListingController(CombindListActivity.this, Events.COMBIND_LISTING_NEW_LISTING_PAGE);
						startSppiner();
						listingController.requestService(mClRequest);
					} else {
						// Do NOthing
					}
				} else {
					isFirstTime = true;
					mClRequest.setSearch_distance(distance);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) { }
		});

		bannerView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				if (totalBanners > 1) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_CANCEL:
						break;
					case MotionEvent.ACTION_UP:
						// calls when touch release on ViewPager
						circleIndicator.setVisibility(View.GONE);
						if (((ImageView) findViewById(R.id.play_pasue_icon)).getBackground() != null 
								&& ((ImageView) findViewById(R.id.play_pasue_icon)).getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.banner_pause).getConstantState())) {
							stopSliding = false;
							runnable(totalBanners);
							bannerHandler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
						}
						break;
					case MotionEvent.ACTION_MOVE:
						// calls when ViewPager touch
						circleIndicator.setVisibility(View.VISIBLE);
						if (bannerHandler != null && stopSliding == false 
								&& ((ImageView) findViewById(R.id.play_pasue_icon)).getBackground() != null 
								&& ((ImageView) findViewById(R.id.play_pasue_icon)).getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.banner_pause).getConstantState())) {
							stopSliding = true;
							bannerHandler.removeCallbacks(animateViewPager);
						}
						break;
					}
				}
				return false;
			}
		});

		bannerView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int pageSelected) { 
				indicatorchange(pageSelected);
				flipperVisibleItemPosition = pageSelected;
			}

			@Override
			public void onPageScrolled(int pageSelected, float positionOffset, int positionOffsetPixel) { 
				bannerView.getParent().requestDisallowInterceptTouchEvent(true);
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				int currentPage = bannerView.getCurrentItem();
				if (currentPage == totalBanners-1 || currentPage == 0) {
					previousState = currentState;
					currentState = state;
					if (previousState == 1 && currentState == 0) {
						bannerView.setCurrentItem(currentPage == 0 ? totalBanners-1 : 0, false);
					}
				}
			}
		});	
	}

	private void showHideSearchCriteriaChooser() {
		if ((mClResponse != null) 
				&& (mClResponse.getSearchAttributeList() != null) 
				&& (mClResponse.getSearchAttributeList().size() > 0)) {
			if(mClResponse.getSearchAttributeList().size() > 1) {
				mSearchCriteriaChooser.setBackgroundResource(R.drawable.search_criteria_enable);
				mSearchCriteriaChooser.setVisibility(View.VISIBLE);
			} else {
				mSearchCriteriaChooser.setBackgroundResource(R.drawable.search_criteria_disable);
				mSearchCriteriaChooser.setVisibility(View.GONE);
			}

			if (mClResponse.getDisplayIn() != null 
					&& "Stamp".equalsIgnoreCase(mClResponse.getDisplayIn().getType()) 
					&& mClResponse.getCompanyArrayList() != null 
					&& mClResponse.getCompanyArrayList().get(0).isStamp()) {
				mRefineSearchView.setVisibility(View.GONE);
				((TextView) findViewById (R.id.col_deal_btn)).setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 75));
				((TextView) findViewById (R.id.col_view_on_map)).setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 75));
			} else {
				mRefineSearchView.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 50));
				((TextView) findViewById (R.id.col_deal_btn)).setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 50));
				((TextView) findViewById (R.id.col_view_on_map)).setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 50));
				mRefineSearchView.setVisibility(View.VISIBLE);
			}
		}else {
			mSearchCriteriaChooser.setVisibility(View.GONE);
		}
	}

	private void showHideBanner() {
		if (mClResponse != null && mClResponse.getBannerList() != null && mClResponse.getBannerList().size() > 0) {
			ImageLoader.initialize(CombindListActivity.this);
			ArrayList<Banner> bannerList = mClResponse.getBannerList();
			totalBanners = bannerList.size();
			bannerViewLayout.setVisibility(View.VISIBLE);
			bannerViewAdapter = new BannerViewAdapter(getSupportFragmentManager(), bannerList, this, AppConstants.FLOW_FROM_COMBIND_LIST);
			bannerViewAdapter.notifyDataSetChanged();
			bannerView.removeAllViews();
			bannerView.setAdapter(bannerViewAdapter);
			if (totalBanners > 1) {
				addImage();
				((ImageView) findViewById(R.id.play_pasue_icon)).setVisibility(View.VISIBLE);
				runnable(bannerList.size());
				bannerHandler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
			} else {
				((ImageView) findViewById(R.id.play_pasue_icon)).setVisibility(View.GONE);
			}
		} else {
			bannerViewLayout.setVisibility(View.GONE);
		}
	}

	public void runnable(final int size) {
		bannerHandler = new Handler();
		animateViewPager = new Runnable() {
			public void run() {
				if (!stopSliding) {
					if (bannerView.getCurrentItem() == size - 1) {
						bannerView.setCurrentItem(0, false);
					} else {
						bannerView.setCurrentItem(bannerView.getCurrentItem() + 1, true);
					}
					bannerHandler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
				}
			}
		};
	}

	private void indicatorchange(int pos) {
		for (int i = 0; i < totalBanners; i++) {
			circleIndicator.getChildAt(i).setBackgroundResource(R.drawable.circle_white);
		}
		circleIndicator.getChildAt(pos).setBackgroundResource(R.drawable.circle_blue);
	}

	private void addImage() {
		circleIndicator.removeAllViews();
		for (int i = 0; i < totalBanners; i++) {
			ImageView image = new ImageView(this);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutParams.setMargins(0, 0, (int) (5 * getResources().getDisplayMetrics().density), 0);
			int padding = (int) (3 * getResources().getDisplayMetrics().density);
			image.setPadding(padding, padding, padding, padding);
			image.setLayoutParams(layoutParams);
			circleIndicator.addView(image, i);
		}
		indicatorchange(flipperVisibleItemPosition);
	}

	private void addAnEmptyRow() {
		if (mClResponse.getTotalrecordFound() <= mClResponse.getRecordsPerPage() && mClResponse.getTotalrecordFound() > 4) {
			// Add one blank record.
			CompanyDesc desc = new CompanyDesc();
			desc.setCompId("-1");
			mClResponse.getCompanyArrayList().add(desc);
		}
	}

	public void viewMoreCopmay(CompanyDesc compDesc) {
		if (mClRequest.isBySearch()) {
			performSearch(compDesc.getStampId()+"-"+ mClResponse.getDisplayIn().getKeyword(), mClResponse.getDisplayIn().getType(), Events.COMBIND_LISTING_VIEW_MORE_COMPANY_EVENT);
		} else {
			performSearch(compDesc.getStampId()+"-"+"null", null, Events.COMBIND_LISTING_VIEW_MORE_COMPANY_EVENT);
		}
	}

	@Override
	protected void onResume() {
		showHideBanner();
		super.onResume();
		if (mClResponse.getDisplayIn() != null 
				&& (!StringUtil.isNullOrEmpty(mClResponse.getDisplayIn().getType())) 
				&& "Stamp".equalsIgnoreCase(mClResponse.getDisplayIn().getType())) {
			AnalyticsHelper.trackSession(CombindListActivity.this, AppConstants.Stamp_Company_listing);
		} else {
			AnalyticsHelper.trackSession(CombindListActivity.this, AppConstants.Company_listing);
		}
	}

	@Override
	protected void onPause() {
		if (bannerHandler != null)
			bannerHandler.removeCallbacks(animateViewPager);
		super.onPause();
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.USER_DETAIL 
				|| (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE && isFromSearch)
				|| event == Events.BANNER_NAVIGATION_EVENT
				|| event == Events.BANNER_LANDING_DEAL_DETAIL_EVENT
				|| event == Events.BANNER_LANDING_COMPANY_DETAIL_EVENT
				|| event == Events.BANNER_LANDING_COMPANY_LISTING_EVENT
				|| event == Events.BANNER_LANDING_SEARCH_EVENT
				|| event == Events.COMBIND_LISTING_VIEW_MORE_COMPANY_EVENT) {
			super.setScreenData(screenData, event, time);
			return;
		} else if (event == Events.REFINE_ATTRIBUTES) {
			Message catRefine = (Message) screenData;
			handler.sendMessage(catRefine);
			return;
		} else if (event == Events.CITY_LISTING || event == Events.LOCALITY_LISTING) {
			Message message = (Message) screenData;
			handler.sendMessage(message);
		} else if (event == Events.DEALCATEGORY_EVENT) {
			Message message = (Message) screenData;
			handler.sendMessage(message);
		} else {
			Response response = (Response) screenData;
			Message message = new Message();
			message.arg2 = event;
			message.arg1 = 1;
			if (response.isError()) {
				message.obj = response.getErrorText();
			} else {
				if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE || event == Events.COMBIND_LISTING_PAGINATION) {
					if (response.getPayload() instanceof CompanyListResponse) {
						CompanyListResponse compListResponse = (CompanyListResponse) response.getPayload();
						if (compListResponse.getErrorCode() != 0) {
							message.obj = getResources().getString(R.string.communication_failure);
						} else {
							if (compListResponse.getCompanyArrayList().size() < 1) {
								message.obj = new String(getResources().getString(R.string.no_result_found));
							} else {
								message.arg1 = 0;
								message.obj = compListResponse;
							}
						}
					} else {
						message.obj = new String(getResources().getString(R.string.communication_failure));
					}
				} else if (event == Events.COMBIND_DEAL_LISTING_NEW_LISTING_PAGE) {
					if (response.getPayload() instanceof CompanyListResponse) {
						mDealResponse = (CompanyListResponse) response.getPayload();
						if (mDealResponse.getErrorCode() != 0) {
							message.obj = getResources().getString(R.string.communication_failure);
						} else {
							if (mDealResponse.getCompanyArrayList().size() < 1) {
								message.obj = new String("No Result Found");
							} else {
								message.arg1 = 0;
								message.obj = mDealResponse;
							}
						}
					} else {
						message.obj = new String("Internal Error");
					}
				} else {
					if (response.getPayload() instanceof CompanyDetail) {
						CompanyDetail compDetail = (CompanyDetail) response.getPayload();
						if (compDetail.getErrorCode() != 0) {
							message.obj = getResources().getString(R.string.communication_failure);
						} else {
							message.arg1 = 0;
							message.obj = compDetail;
						}
					} else {
						message.obj = new String(getResources().getString(R.string.communication_failure));
					}
				}
			}
			handler.sendMessage(message);
		}
	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.USER_DETAIL 
				|| msg.arg2 == Events.BANNER_NAVIGATION_EVENT
				|| msg.arg2 == Events.BANNER_LANDING_COMPANY_DETAIL_EVENT
				|| msg.arg2 == Events.BANNER_LANDING_DEAL_DETAIL_EVENT
				|| msg.arg2 == Events.BANNER_LANDING_COMPANY_LISTING_EVENT
				|| msg.arg2 == Events.BANNER_LANDING_SEARCH_EVENT
				|| msg.arg2 == Events.COMBIND_LISTING_VIEW_MORE_COMPANY_EVENT) {
			super.updateUI(msg);
		} else if (msg.arg2 == Events.COMPANY_DETAIL) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				CompanyDetail compListResp = (CompanyDetail) msg.obj;
				if (!StringUtil.isNullOrEmpty(compListResp.getId())) {
					Intent intent = new Intent(CombindListActivity.this,CompanyDetailActivity.class);
					if (mClRequest.isBySearch())
						intent.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD,mClRequest.getKeywordOrCategoryId());
					intent.putExtra(AppConstants.COMP_DETAIL_DATA, compListResp);
					intent.putExtra(AppConstants.THUMB_URL, mCategoryThumbUrl);
					intent.putExtra(AppConstants.IS_DEAL_LIST,!mClRequest.isCompanyListing());
					startActivity(intent);
				} else {
					showInfoDialog(getResources().getString(R.string.no_result_found));
				}
			}
			stopSppiner();
		} else if (msg.arg2 == Events.COMBIND_LISTING_PAGINATION) {
			loadingNextPageData = false;
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				CompanyListResponse oldResponse = mClResponse;
				mClResponse = (CompanyListResponse) msg.obj;
				oldResponse.appendCompListAtEnd(mClResponse.getCompanyArrayList(), false);
				mClResponse.setCompanyList(oldResponse.getCompanyArrayList());
				if (mClResponse.getPageNumber() == 10 || mClResponse.getTotalrecordFound() == mClResponse.getCompanyArrayList().size()) {
					// Add one blank record.
					CompanyDesc desc = new CompanyDesc();
					desc.setCompId("-1");
					mClResponse.getCompanyArrayList().add(desc);
				}
				mCompListAdapter.setData(mClResponse.getCompanyArrayList());
				mCompListAdapter.notifyDataSetChanged();
				//				mCompanyList.setAdapter(mCompListAdapter);
			}
			stopSppiner();
		} else if ((msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE && !isFromSearch) || (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE && isFromNearMe)) {
			isFromNearMe = false;
			mClRequest.setFromNearMe(isFromNearMe);

			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
				isSuccessfull = false;
				mDistanceChooser.setSelection(mPreviousSelectedIndex);
				mClRequest.setSearch_distance(mPreviousDistance);
			} else {
				isSuccessfull = true;
				mClResponse = (CompanyListResponse) msg.obj;
				String search_distance = mClResponse.getSearch_distance();
				int index = setDistanceFilter(search_distance);
				mDistanceChooser.setSelection(index);
				mClRequest.setSearch_distance(search_distance);

				showHideBanner();

				if ((!StringUtil.isNullOrEmpty(mClRequest.getStampId())) && (!StringUtil.isNullOrEmpty(mClRequest.getSearchCriteria()))) {
					mDistanceChooser.setVisibility(View.GONE);
					nearMe.setVisibility(View.GONE);
					mSearchCriteriaChooser.setVisibility(View.GONE);
				} else {
					if (MaxisMainActivity.isCitySelected) {
						mDistanceChooser.setVisibility(View.GONE);
						nearMe.setVisibility(View.VISIBLE);
					} else {
						if (GPS_Data.getLatitude() == 0 || GPS_Data.getLongitude() == 0) {
							nearMe.setVisibility(View.VISIBLE);
							mDistanceChooser.setVisibility(View.GONE);
						} else {
							nearMe.setVisibility(View.GONE);
							mDistanceChooser.setVisibility(View.VISIBLE);
						}
					}
				}

				showHideSearchCriteriaChooser();

				mIsFreshSearch = (mClResponse != null && mClResponse.getCategoryList() != null && mClResponse.getCategoryList().size() > 1) ? true : false;

//				if (mIsFreshSearch || !StringUtil.isNullOrEmpty(mClRequest.getSearchIn())) {
					if (mClResponse.getCategoryList() != null && mClResponse.getCategoryList().size() == 1) {
						mIsFreshSearch 				= 		false;
						CategoryRefine tempCatref 	= 		mClResponse.getCategoryList().get(0);
						mClRequest.setSelectedCategoryBySearch(tempCatref.getCategoryId(), tempCatref.getCategoryTitle());
						mRefineSearchView.setText(getResources().getString(R.string.cl_modify_search));
					} else
						mRefineSearchView.setText(getResources().getString(R.string.cl_filter_by));
//				}

				if (mClResponse.getPageNumber() == 10 || mClResponse.getTotalrecordFound() == mClResponse.getCompanyArrayList().size()) { }
				mRecordsFoundView.setText(mClResponse.getTotalrecordFound() + " " + getResources().getString(R.string.record_found));
				mCompListAdapter.setData(mClResponse.getCompanyArrayList());
				mCompListAdapter.notifyDataSetChanged();
				mCompanyList.setAdapter(mCompListAdapter);
				System.out.println(mClResponse.getPageNumber());
				if (mClRequest != null && !StringUtil.isNullOrEmpty(mClRequest.getCategoryTitle())) {
					mHeaderTitle.setText(Html.fromHtml(mClRequest.getCategoryTitle()));
					mCategoryThumbUrl = mClRequest.getParentThumbUrl();
				}

				addAnEmptyRow();
			}
			stopSppiner();
		} else if (msg.arg2 == Events.REFINE_ATTRIBUTES) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				if (mClRequest.isBySearch())
					displayRefineWithAttributeSpinnersPreloaded((RefineSelectorResponse) msg.obj,RefineSearchActivity.ATTR_SELECTION_BY_SEARCH);
				else
					displayRefineWithAttributeSpinnersPreloaded((RefineSelectorResponse) msg.obj,RefineSearchActivity.ATTR_SELECTION);
			}
			stopSppiner();
		} else if (msg.arg2 == Events.COMBIND_DEAL_LISTING_NEW_LISTING_PAGE) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				Intent intent = new Intent(CombindListActivity.this, DealsActivity.class);
				intent.putExtra(AppConstants.DATA_LIST_REQUEST, mdealRequest);
				intent.putExtra(AppConstants.COMP_LIST_DATA, mDealResponse);
				startActivity(intent);
			}
			stopSppiner();
		} else if (msg.arg2 == Events.CITY_LISTING) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				GenralListResponse glistRes = (GenralListResponse) msg.obj;
				cityList = glistRes.getCityOrLocalityList();
				Intent intent = new Intent(CombindListActivity.this, AdvanceSelectCity.class);
				for (CityOrLocality cityOrLocality : cityList) {
					cityListString.add(cityOrLocality.getName());
				}
				localityItems = null;
				ids = null;
				selectedLocalityindex = null;
				currentLocality.setText("Choose your Area");
				intent.putExtra("CITY_LIST", cityListString);
				intent.putExtra("SELECTED_CITY", selectedCityforHeader);
				startActivityForResult(intent, AppConstants.CITY_REQUEST);
			}
			stopSppiner();
		} else if (msg.arg2 == Events.LOCALITY_LISTING) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				GenralListResponse glistRes = (GenralListResponse) msg.obj;
				localityList = glistRes.getCityOrLocalityList();
				Intent intent = new Intent(CombindListActivity.this, AdvanceSelectLocalityActivity.class);
				localityItems = new ArrayList<String>();
				for (CityOrLocality dealCityOrLoc : localityList) {
					localityItems.add(dealCityOrLoc.getName());
				}
				intent.putExtra("LOCALITY_LIST", localityItems);
				intent.putStringArrayListExtra("LOCALITY_INDEX", selectedLocalityindex);
				intent.putExtra("SELECTED_LOCALITIES", selectedLocalityItemsforHeader);
				startActivityForResult(intent, AppConstants.LOCALITY_REQUEST);
			}
		} else if (msg.arg2 == Events.DEALCATEGORY_EVENT) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				SubCategoryResponse categoriesResp = (SubCategoryResponse) msg.obj;
				Intent intent = new Intent(CombindListActivity.this, DealsActivity.class);
				intent.putExtra(AppConstants.DATA_SUBCAT_RESPONSE, categoriesResp);
				startActivity(intent);
			}
			stopSppiner();
		} else if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE && isFromSearch) {
			isFromSearch = false;
			super.updateUI(msg);
		} 
	}

	@Override
	public Activity getMyActivityReference() {
		return null;
	}

	private void loadPageData(int pageNumber) {
		CombindListingController listingController = new CombindListingController(CombindListActivity.this, Events.COMBIND_LISTING_PAGINATION);
		mClRequest.setPageNumber(pageNumber);
		if(mClResponse != null && mClResponse.getDisplayIn() != null) {
			mClRequest.setSearchIn(mClResponse.getDisplayIn().getType());
			mClRequest.setKeywordOrCategoryId(mClResponse.getDisplayIn().getKeyword());
		}
		startSppiner();
		listingController.requestService(mClRequest);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_toggler:
			AnalyticsHelper.logEvent(FlurryEventsConstants.HOME_SEARCH_CLICK);
			if (wholeSearchBoxContainer.getVisibility() == View.VISIBLE) 
				wholeSearchBoxContainer.setVisibility(View.GONE);
			else
				wholeSearchBoxContainer.setVisibility(View.VISIBLE);
			if (mSearchContainer.getVisibility() == View.VISIBLE)
				mSearchContainer.setVisibility(View.GONE);
			else
				mSearchContainer.setVisibility(View.VISIBLE);
			break;
		case R.id.header_btn_back:
			AnalyticsHelper.logEvent(FlurryEventsConstants.BACK_CLICK);
			AnalyticsHelper.endTimedEvent(FlurryEventsConstants.APPLICATION_COMBINED_LIST);
			MaxisMainActivity.isCitySelected = false;
			this.finish();
			break;
		case R.id.col_refine_search:
		case R.id.col_refine_search1:
			refineSearch();
			AnalyticsHelper.logEvent(FlurryEventsConstants.MODIFY_SEARCH_CLICK);
			break;
		case R.id.col_deal_btn:
			CategoryGroup cat = new CategoryGroup();
			cat.setmGroupActionType(AppConstants.GROUP_ACTION_TYPE_DEAL);
			cat.setMgroupType(AppConstants.GROUP_TYPE_CATEGORY);
			cat.setCategoryId("");
			cat.setCategoryTitle("");
			showDealcategories(cat);
			AnalyticsHelper.logEvent(FlurryEventsConstants.HOT_DEALS_CLICK);
			break;
		case R.id.mainSearchButton:
			mSearchEditText.setText(mSearchEditText.getText().toString().trim());
			String JSON_EXTRA = jsonForSearch();
			performSearch(mSearchEditText.getText().toString(), JSON_EXTRA, Events.COMBIND_LISTING_NEW_LISTING_PAGE);
			mClRequest.setKeywordOrCategoryId(mSearchEditText.getText().toString());
			mClRequest.setCategoryTitle(mSearchEditText.getText().toString());
			isFromSearch = true;
			break;
		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			showHomeScreen();
			MaxisMainActivity.isCitySelected = false;
			break;
		case R.id.show_profile_icon:
			onProfileClick();
			break;
		case R.id.col_view_on_map:
		case R.id.col_view_on_map1:
			AnalyticsHelper.logEvent(FlurryEventsConstants.VIEW_ON_MAP_CLICK);
			if (isDialogToBeShown())
				showConfirmationDialog(CustomDialog.DATA_USAGE_DIALOG, getResources().getString(R.string.cd_msg_data_usage));
			else
				showMap();
			break;
		case R.id.upArrow:
			if (isAdvanceSearchLayoutOpen) {
				isAdvanceSearchLayoutOpen = false;
				advanceSearchLayout.setVisibility(View.GONE);
			}
			break;
		case R.id.currentCity:
			if (cityListString != null && cityListString.size() > 0) {
				localityItems = null;
				selectedLocalityindex = null;
				Intent cityIntent = new Intent(CombindListActivity.this, AdvanceSelectCity.class);
				cityIntent.putExtra("CITY_LIST", cityListString);
				cityIntent.putExtra("SELECTED_CITY", selectedCityforHeader);
				startActivityForResult(cityIntent, AppConstants.CITY_REQUEST);
			} else
				setSearchCity();
			break;
		case R.id.currentLocality:
			if (localityItems != null && localityItems.size() > 0) {
				Intent localityIntent = new Intent(CombindListActivity.this, AdvanceSelectLocalityActivity.class);
				localityIntent.putExtra("LOCALITY_LIST", localityItems);
				localityIntent.putExtra("SELECTED_LOCALITIES", selectedLocalityItemsforHeader);
				localityIntent.putStringArrayListExtra("LOCALITY_INDEX", selectedLocalityindex);
				startActivityForResult(localityIntent, AppConstants.LOCALITY_REQUEST);
			} else 
				setSearchLocality(city_id);
			break;
		case R.id.nearMe:
			if (mClRequest.getLongitude() == 0.0 || mClRequest.getLatitude() == 0.0)
				showAlertDialog(getResources().getString(R.string.location_unavailable_switch_gps));
			else {
				mSelctorResp = null;
				selectedCity = null;
				selectedLocalityItems = null;
				mDistanceChooser.setVisibility(View.VISIBLE);
				nearMe.setVisibility(View.GONE);
				mClRequest.setSearch_distance("");
				mClRequest.setPageNumber(1);
				mClRequest.setPostJsonPayload("");
				
				MaxisMainActivity.isCitySelected = false;
				isFromNearMe = true;
				mClRequest.setFromNearMe(isFromNearMe);
				CombindListingController listingController = new CombindListingController(CombindListActivity.this, Events.COMBIND_LISTING_NEW_LISTING_PAGE);
				startSppiner();
				listingController.requestService(mClRequest);
			}
			break;
		case R.id.play_pasue_icon:
			playPauseBanner();
		}
	}

	private void playPauseBanner() {
		if (!stopSliding) {
			if (bannerHandler != null && stopSliding == false) {
				((ImageView) findViewById(R.id.play_pasue_icon)).setBackgroundResource(R.drawable.banner_play);
				stopSliding = true;
				bannerHandler.removeCallbacks(animateViewPager);
			}
		} else {
			if (totalBanners > 0) {
				((ImageView) findViewById(R.id.play_pasue_icon)).setBackgroundResource(R.drawable.banner_pause);
				stopSliding = false;
				bannerHandler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
			}
		}
	}

	private void refineSearch() {
		if (mClRequest.isBySearch() 
				|| (mClRequest.getGroupActionType().trim().equalsIgnoreCase(AppConstants.GROUP_ACTION_TYPE_CATEGORY_LIST_FOR_GROUP) 
						&& (mClRequest.getGroupType().trim().equalsIgnoreCase(AppConstants.GROUP_TYPE_CATEGORY)))) {
			if (mIsFreshSearch) {
				mCatResponse = new RefineCategoryResponse();
				if (mClResponse.getCategoryList() == null || mClResponse.getCategoryList().size() < 1) {
					showAlertDialog(getResources().getString(R.string.category_list_not_found));
					return;
				}
				mCatResponse.setCategories(mClResponse.getCategoryList());
				Intent intent = new Intent(CombindListActivity.this, RefineSearchCategoryActivity.class);
				intent.putExtra(AppConstants.REFINE_CAT_RESPONSE, mCatResponse);
				intent.putExtra(AppConstants.DATA_LIST_REQUEST, mClRequest);
				intent.putExtra("SELECTED_CITY", selectedCity);
				intent.putExtra("SELECTED_LOCALITY", selectedLocalityItems);
				intent.putExtra("SELECTED_CITY_FOR_HEADERS", selectedCityforHeader);
				intent.putExtra("SELECTED_LOCALITY_FOR_HEADERS", selectedLocalityItemsforHeader);
				startActivityForResult(intent, 1);
			} else {
				if (mSelctorResp != null) 
					displayRefineWithAttributeSpinnersPreloaded(mSelctorResp, RefineSearchActivity.ATTR_SELECTION_BY_SEARCH);
				else 
					fetchRefineAttribute(mClRequest.getSelectedCategoryBySearch());
			}
		} else {
			if (mSelctorResp != null) 
				displayRefineWithAttributeSpinnersPreloaded(mSelctorResp, RefineSearchActivity.ATTR_SELECTION);
			else 
				fetchRefineAttribute(mClRequest.getKeywordOrCategoryId());
		}
	}

	private void fetchRefineAttribute(String categoryId) {
		RefineAttributeController refineController = new RefineAttributeController(CombindListActivity.this, Events.REFINE_ATTRIBUTES);
		startSppiner();
		RefineSearchRequest refineSearchRequest = new RefineSearchRequest();
		refineSearchRequest.setCategoryId(categoryId);
		if(mClRequest.isBySearch()) {
			if((!StringUtil.isNullOrEmpty(mClRequest.getSearchCriteria()) 
					&& ("Stamp".equalsIgnoreCase(mClRequest.getSearchCriteria())))) {
				refineSearchRequest.setStampId(mClRequest.getStampId());
			} else {
				refineSearchRequest.setSearchKeyword(mClRequest.getKeywordOrCategoryId());
				refineSearchRequest.setSearchIn(mClRequest.getSearchIn());
			}
		} else { 
			refineSearchRequest.setSearchKeyword("");
		}
		refineSearchRequest.setDeal(!mClRequest.isCompanyListing());
		refineController.requestService(refineSearchRequest);
	}

	private void displayRefineWithAttributeSpinnersPreloaded(RefineSelectorResponse selectorRes, int selectionMode) {
		Intent intent = new Intent(CombindListActivity.this, RefineSearchActivity.class);
		intent.putExtra(AppConstants.REFINE_ATTR_RESPONSE, selectorRes);
		intent.putExtra(RefineSearchActivity.SELECTOR_MODE, selectionMode);
		intent.putExtra(AppConstants.DATA_LIST_REQUEST, mClRequest);
		intent.putExtra(AppConstants.THUMB_URL, mCategoryThumbUrl);
		intent.putExtra(AppConstants.LOCALITY_DAO_DATA, mLocalitySelectorDao);
		if(mClResponse.getCategoryList().size() > 0) 
			intent.putExtra("categoryId", mClResponse.getCategoryList().get(0).getCategoryId());
		startActivityForResult(intent, 1);
	}

	@Override
	public void onPositiveDialogButton(int id) {
		if (id == CustomDialog.CONFIRMATION_DIALOG) {
			refineSearch();
			isModifySearchDialogOpen = false;
		} else if (id == CustomDialog.DATA_USAGE_DIALOG) {
			showMap();
		} else {
			super.onPositiveDialogButton(id);
		}
	}

	@Override
	public void onNegativeDialogbutton(int id) {
		loadingNextPageData = false;
		isModifySearchDialogOpen = false;
		super.onNegativeDialogbutton(id);
	}

	private void showMap() {
		if (isLocationAvailable()) {
			Intent intent = new Intent(CombindListActivity.this,ViewAllOnMapActivity.class);
			intent.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD,mClRequest.getKeywordOrCategoryId());
			intent.putExtra(AppConstants.MAP_ALL_TITLE, mHeaderTitle.getText().toString().trim());
			intent.putExtra(AppConstants.IS_SEARCH_KEYWORD,mClRequest.isBySearch());
			intent.putExtra(AppConstants.IS_DEAL_LIST,!mClRequest.isCompanyListing());
			intent.putParcelableArrayListExtra(AppConstants.COMP_DETAIL_LIST,mClResponse.getCompanyArrayList());
			startActivity(intent);
		}
	}

	public String jsonForSearch() {
		JSONObject jArray = new JSONObject();
		try {
			if (city_id != -1) {
				JSONObject array = new JSONObject();
				array.put("city_id", city_id + "");
				array.put("city_name", selectedCityforHeader);
				jArray.put("city", array);

				if (ids != null && ids.size() > 0) {
					JSONArray jsonArray = new JSONArray();
					for (int i = 0; i < selectedLocalityItemsforHeader.size(); i++) {
						JSONObject localityArray = new JSONObject();
						localityArray.put("locality_id", ids.get(i));
						localityArray.put("locality_name",selectedLocalityItemsforHeader.get(i));
						jsonArray.put(localityArray);
					}
					jArray.put("locality", jsonArray);
				}
				return jArray.toString();
			} else {
				return null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	protected void showDealcategories(CategoryGroup cat) {
		SubCategoryController controller = new SubCategoryController(CombindListActivity.this, Events.DEALCATEGORY_EVENT);
		controller.requestService(cat);
		controller.isForDeal = true;
		startSppiner();
	}

	@SuppressLint("NewApi")
	public int setDistanceFilter(String search_distance) {
		int index = 0;
		if (search_distance == null || Integer.valueOf(search_distance) >= 21 || search_distance.isEmpty()) {
			index = 8;
			mPreviousSelectedIndex = 8;
			mPreviousDistance = "100000";
		} else {
			search_distance = search_distance + " km";
			index = filter.indexOf(search_distance);
			mPreviousSelectedIndex = index;
			mPreviousDistance = search_distance;
		}
		return index;
	}

	@Override
	public void onBackPressed() {
		MaxisMainActivity.isCitySelected = false;
		super.onBackPressed();
	}
}