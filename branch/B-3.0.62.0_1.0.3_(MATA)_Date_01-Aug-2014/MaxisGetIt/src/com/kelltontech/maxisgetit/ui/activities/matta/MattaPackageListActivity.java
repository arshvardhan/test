package com.kelltontech.maxisgetit.ui.activities.matta;

import java.util.ArrayList;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kelltontech.framework.imageloader.ImageLoader;
import com.kelltontech.framework.model.Response;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.framework.utils.UiUtils;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.adapters.matta.MattaPackageListAdapter;
import com.kelltontech.maxisgetit.adapters.matta.PackageListBannerViewAdapter;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.constants.matta.MattaConstants;
import com.kelltontech.maxisgetit.constants.matta.MattaEvents;
import com.kelltontech.maxisgetit.controllers.RefineAttributeController;
import com.kelltontech.maxisgetit.controllers.matta.MattaPackageDetailController;
import com.kelltontech.maxisgetit.controllers.matta.MattaPackageListController;
import com.kelltontech.maxisgetit.dao.CityOrLocality;
import com.kelltontech.maxisgetit.dao.CompanyDetail;
import com.kelltontech.maxisgetit.model.matta.packages.detail.MattaPackageDetailResponse;
import com.kelltontech.maxisgetit.model.matta.packages.list.MattaPackageListResponse;
import com.kelltontech.maxisgetit.model.matta.packages.list.PackageListBanner;
import com.kelltontech.maxisgetit.model.matta.packages.list.PackageModel;
import com.kelltontech.maxisgetit.requests.matta.MattaFilterSearchRequest;
import com.kelltontech.maxisgetit.requests.matta.MattaPackageDetailRequest;
import com.kelltontech.maxisgetit.requests.matta.MattaPackageListRequest;
import com.kelltontech.maxisgetit.response.GenralListResponse;
import com.kelltontech.maxisgetit.response.RefineSelectorResponse;
import com.kelltontech.maxisgetit.ui.activities.AdvanceSelectCity;
import com.kelltontech.maxisgetit.ui.activities.AdvanceSelectLocalityActivity;
import com.kelltontech.maxisgetit.ui.activities.MaxisMainActivity;
import com.kelltontech.maxisgetit.ui.widgets.AnimatedLinearLayout;
import com.kelltontech.maxisgetit.ui.widgets.CustomDialog;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class MattaPackageListActivity extends MaxisMainActivity implements OnClickListener {

	private LinearLayout 					advanceSearchLayout;
	private LinearLayout 					mSearchContainer;
	private AnimatedLinearLayout 			wholeSearchBoxContainer;
	private RelativeLayout 					bannerViewLayout;
	private LinearLayout 					circleIndicator;
	private TextView 						mRecordsFoundView;
	private TextView 						mRefineSearchView;
	private TextView 						currentCity, currentLocality;
	private TextView 						mainSearchButton;
	private TextView 						mHeaderTitle;

	private ImageView 						mSearchToggler;
	private ImageView 						mSearchBtn;
	private ImageView 						upArrow;
	private ImageView 						mHomeIconView;
	private ImageView 						mProfileIconView;
	private ImageView 						mHeaderBackButton;

	private ListView 						mCompanyList;
	private EditText 						mSearchEditText;
	private ViewPager	 					bannerView;
	private MattaPackageListRequest 		mMattaPackageListRequest;
	private MattaPackageDetailRequest 		mMattaPackageDetailRequest;
	private RefineSelectorResponse 			mSelctorResp;
	//	private RefineCategoryResponse 			mCatResponse;
	private MattaPackageListResponse 		mMattaPackageListResponse;
//	private SelectorDAO 					mLocalitySelectorDao;
	ArrayList<CityOrLocality> 				localityList;
	ArrayList<String> 						ids = new ArrayList<String>();
	ArrayList<String> 						selectedLocalityindex;
	ArrayList<CityOrLocality> 				cityList;
	private ArrayList<String> 				selectedLocalityItemsforHeader = new ArrayList<String>();
	private ArrayList<String> 				selectedLocalityItems = new ArrayList<String>();
	private ArrayList<String> 				cityListString	= new ArrayList<String>();
	private ArrayList<String> 				localityItems;
	private int 							totalBanners;
	private MattaPackageListAdapter 		mMattaPackageListAdapter;
	private PackageListBannerViewAdapter 	mMattaBannerViewAdapter;
	private String 							selectedCityforHeader = "Entire Malaysia";
	private String 							selectedCity = "Entire Malaysia";
//	private String 							mMattaThumbUrl;
	private boolean 						loadingNextPageData;
	private boolean 						isModifySearchDialogOpen;
	private boolean 						mScrollUp;
	private boolean 						isAdvanceSearchLayoutOpen = false;
	private boolean 						stopSliding = false;
	boolean 								isFirstTime = false;
	boolean 								isSuccessfull = false;
	boolean 								isFromSearch = false;
	private int 							city_id = -1;
	private int 							previousState, currentState;
	private int 							flipperVisibleItemPosition = 0;
	private Runnable 						animateViewPager;
	private Handler 						bannerHandler;
	private static final long 				ANIM_VIEWPAGER_DELAY = 5000;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				Bundle bundle 						= 		data.getExtras();
				mMattaPackageListResponse 			= 		(MattaPackageListResponse) bundle.getSerializable(MattaConstants.DATA_MATTA_PACKAGE_LIST_RESPONSE);
				mMattaPackageListRequest 			= 		(MattaPackageListRequest) bundle.getSerializable(MattaConstants.DATA_MATTA_PACKAGE_LIST_REQUEST);
				mSelctorResp 						= 		bundle.getParcelable(AppConstants.REFINE_ATTR_RESPONSE);
				mRecordsFoundView.setText(mMattaPackageListResponse.getResults().getTotal_Records_Found() + " " + getResources().getString(R.string.matta_record_found));
				mMattaPackageListRequest.setSearchRefined(true);
				if (StringUtil.isNullOrEmpty(mMattaPackageListResponse.getResults().getTotal_Records_Found()) 
						|| mMattaPackageListResponse.getResults().getTotal_Records_Found().equals("0") )
					showInfoDialog(getResources().getString(R.string.no_result_found));

				showHideBanner();

				ArrayList<PackageModel> mPackageModel = 		mMattaPackageListResponse.getResults().getPackage();
				mMattaPackageListAdapter.setData(mPackageModel);
				mMattaPackageListAdapter.notifyDataSetChanged();
				mCompanyList.setAdapter(mMattaPackageListAdapter);
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
		} 
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_matta_package_list);
		AnalyticsHelper.logEvent(FlurryEventsConstants.MATTA_PACKAGE_LISTING, true);
		UiUtils.hideKeyboardOnTappingOutside(findViewById(R.id.cl_root_layout),this);
		ImageLoader.initialize(MattaPackageListActivity.this);

		advanceSearchLayout 	= (LinearLayout) 	findViewById(R.id.advanceSearch);		
		wholeSearchBoxContainer = (AnimatedLinearLayout) 	findViewById(R.id.whole_search_box_container);
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
		currentCity 			= (TextView) 		findViewById(R.id.currentCity);
		currentLocality 		= (TextView) 		findViewById(R.id.currentLocality);
		mainSearchButton 		= (TextView) 		findViewById(R.id.mainSearchButton);
		mHeaderTitle 			= (TextView) 		findViewById(R.id.header_title);
		mRecordsFoundView 		= (TextView) 		findViewById(R.id.col_records_found);
		mCompanyList 			= (ListView) 		findViewById(R.id.col_company_list);
		mSearchToggler 			= (ImageView) 		findViewById(R.id.search_toggler);
		findViewById(R.id.play_pasue_icon).setOnClickListener(this);
		mProfileIconView.setOnClickListener(this);
		mHeaderBackButton.setOnClickListener(this);
		mHomeIconView.setOnClickListener(this);
		mSearchBtn.setOnClickListener(MattaPackageListActivity.this);
		upArrow.setOnClickListener(this);
		mRefineSearchView.setOnClickListener(this);
		currentCity.setOnClickListener(this);
		currentLocality.setOnClickListener(this);
		mainSearchButton.setOnClickListener(this);
		mSearchToggler.setOnClickListener(this);
		advanceSearchLayout.setVisibility(View.GONE);
		currentCity.setText(Html.fromHtml("in " + "<b>" + selectedCityforHeader + "</b>"));

		if(getIntent().getExtras() != null) {
			Bundle bundle 	= getIntent().getExtras();
			mMattaPackageListRequest 	= (MattaPackageListRequest) bundle.getSerializable(MattaConstants.DATA_MATTA_PACKAGE_LIST_REQUEST);
			mMattaPackageListResponse  	= (MattaPackageListResponse) bundle.getSerializable(MattaConstants.DATA_MATTA_PACKAGE_LIST_RESPONSE);
		}

		mHeaderTitle.setText(Html.fromHtml("Tour & Travel Packages"));

//		if (mMattaPackageListRequest != null) {
//			mMattaThumbUrl = mMattaPackageListRequest.getmMattaThumbUrl();
//		}

		if (mMattaPackageListRequest != null && !StringUtil.isNullOrEmpty(mMattaPackageListRequest.getPostJsonPayload())) {
			try {
				JSONObject jsonObject = new JSONObject(mMattaPackageListRequest.getPostJsonPayload());
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

		mRecordsFoundView.setText(mMattaPackageListResponse.getResults().getTotal_Records_Found() + " " + getResources().getString(R.string.matta_record_found));

		ArrayList<PackageModel> boothInfo 			= 		mMattaPackageListResponse.getResults().getPackage();
		mMattaPackageListAdapter 					= 		new MattaPackageListAdapter(MattaPackageListActivity.this);
		mMattaPackageListAdapter.setData(boothInfo);
		mMattaPackageListAdapter.notifyDataSetChanged();
		mCompanyList.setAdapter(mMattaPackageListAdapter);

		mCompanyList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 == (mMattaPackageListAdapter.getCount() - 1) && arg2 != 0 && Integer.parseInt(mMattaPackageListResponse.getResults().getTotal_Records_Found()) > 10) { 
				} else {
					MattaPackageDetailController packageDetailcontroller = new MattaPackageDetailController(MattaPackageListActivity.this, MattaEvents.MATTA_PACKAGE_DETAIL_EVENT);
					mMattaPackageDetailRequest = new MattaPackageDetailRequest();
					mMattaPackageDetailRequest.setPackageId(!StringUtil.isNullOrEmpty(((PackageModel) mMattaPackageListAdapter.getItem(arg2)).getId()) ? ((PackageModel) mMattaPackageListAdapter.getItem(arg2)).getId() : "");
					mMattaPackageDetailRequest.setSource(!StringUtil.isNullOrEmpty(((PackageModel) mMattaPackageListAdapter.getItem(arg2)).getSource()) ? ((PackageModel) mMattaPackageListAdapter.getItem(arg2)).getSource() : "");
					startSppiner();
					packageDetailcontroller.requestService(mMattaPackageDetailRequest);
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
				if (mMattaPackageListResponse != null) {
					if ((number) % Integer.parseInt(mMattaPackageListResponse.getResults().getRecords_Per_Page()) == 0
							&& number > 0
							&& number == totalItemCount
							&& totalItemCount < Integer.parseInt(mMattaPackageListResponse.getResults().getTotal_Records_Found())) {
						if (loadingNextPageData)
							return;
						loadingNextPageData = true;
						if (mMattaPackageListResponse.getResults().getPage_Number() < MattaConstants.MAX_RECORD_COUNT / 10)
							loadPageData(mMattaPackageListResponse.getResults().getPage_Number() + 1);
					} else if (number >= MattaConstants.MAX_RECORD_COUNT && !isModifySearchDialogOpen && mScrollUp && Integer.parseInt(mMattaPackageListResponse.getResults().getTotal_Records_Found()) > 100) {
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

		//		addAnEmptyRow();

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

	private void showHideBanner() {
		if (mMattaPackageListResponse != null 
				&& mMattaPackageListResponse.getResults().getBanners() != null 
				&& mMattaPackageListResponse.getResults().getBanners().size() > 0
				&& mMattaPackageListResponse.getResults().getBanners().get(0) != null
				&& mMattaPackageListResponse.getResults().getBanners().get(0).getBanner() != null 
				&& mMattaPackageListResponse.getResults().getBanners().get(0).getBanner().size() > 0) {
			ImageLoader.initialize(MattaPackageListActivity.this);
			ArrayList<PackageListBanner> bannerList = (ArrayList<PackageListBanner>) mMattaPackageListResponse.getResults().getBanners().get(0).getBanner();
			totalBanners = bannerList.size();
			bannerViewLayout.setVisibility(View.VISIBLE);
			mMattaBannerViewAdapter = new PackageListBannerViewAdapter(getSupportFragmentManager(), bannerList, this, MattaConstants.FLOW_FROM_MATTA_PACKAGE_LIST);
			mMattaBannerViewAdapter.notifyDataSetChanged();
			bannerView.removeAllViews();
			bannerView.setAdapter(mMattaBannerViewAdapter);
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

	@Override
	protected void onResume() {
		showHideBanner();
		super.onResume();
		AnalyticsHelper.trackSession(MattaPackageListActivity.this, MattaConstants.Matta_Package_Listing);
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
				|| event == Events.BANNER_LANDING_SEARCH_EVENT ) {
			super.setScreenData(screenData, event, time);
			return;
		} else if (event == MattaEvents.MATTA_FILTER_SEARCH_EVENT) {
			Message catRefine = (Message) screenData;
			handler.sendMessage(catRefine);
			return;
		} else if (event == Events.CITY_LISTING || event == Events.LOCALITY_LISTING) {
			Message message = (Message) screenData;
			handler.sendMessage(message);
			return;
		} else if (event == MattaEvents.MATTA_PACKAGE_LIST_EVENT) {
			MattaPackageListResponse packageListResponse = (MattaPackageListResponse) screenData;
			Message message = new Message();
			message.arg2 = event;
			if ((packageListResponse.getResults() != null) && (!StringUtil.isNullOrEmpty(packageListResponse.getResults().getError_Code())) && (packageListResponse.getResults().getError_Code().equals("0"))) {
				if (packageListResponse.getResults().getPackage().size() < 1 
						|| StringUtil.isNullOrEmpty(packageListResponse.getResults().getTotal_Records_Found()) 
						|| packageListResponse.getResults().getTotal_Records_Found().equals("0")) {
					message.arg1 = 1;
					message.obj = new String("No Result Found");
				} else {
					message.arg1 = 0;
					message.obj = packageListResponse;
				}
			} else {
				message.arg1 = 1;
				message.obj = getResources().getString(R.string.communication_failure);
			}
			handler.sendMessage(message);
			return;			
		} else if (event == MattaEvents.MATTA_PACKAGE_DETAIL_EVENT) {
			MattaPackageDetailResponse packageDetailResponse = (MattaPackageDetailResponse) screenData;
			Message message = new Message();
			message.arg2 = event;
			if ((packageDetailResponse.getResults() != null) && (!StringUtil.isNullOrEmpty(packageDetailResponse.getResults().getError_Code())) && (packageDetailResponse.getResults().getError_Code().equals("0"))) {
				if (packageDetailResponse.getResults().getPackage() == null 
						|| StringUtil.isNullOrEmpty(packageDetailResponse.getResults().getPackage().getCName())) {
					message.arg1 = 1;
					message.obj = new String("No Result Found");
				} else {
					message.arg1 = 0;
					message.obj = packageDetailResponse;
				}
			} else {
				message.arg1 = 1;
				message.obj = getResources().getString(R.string.communication_failure);
			}
			handler.sendMessage(message);
			return;			
		} else {
			Response response = (Response) screenData;
			Message message = new Message();
			message.arg2 = event;
			message.arg1 = 1;
			if (response.isError()) {
				message.obj = response.getErrorText();
			} else {
				if (response.getPayload() instanceof MattaPackageListResponse) {
					MattaPackageListResponse packageListResponse = (MattaPackageListResponse) response.getPayload();
					if (Integer.parseInt(packageListResponse.getResults().getError_Code()) != 0) {
						message.obj = getResources().getString(R.string.communication_failure);
					} else {
						message.arg1 = 0;
						message.obj = packageListResponse;
					}
				} else {
					message.obj = new String(getResources().getString(R.string.communication_failure));
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
				|| msg.arg2 == Events.BANNER_LANDING_SEARCH_EVENT) {
			super.updateUI(msg);
		} else if (msg.arg2 == MattaEvents.MATTA_PACKAGE_DETAIL_EVENT) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				MattaPackageDetailResponse detailResponse = (MattaPackageDetailResponse) msg.obj;
				if (!StringUtil.isNullOrEmpty(detailResponse.getResults().getPackage().getId())) {
					Intent intent = new Intent(MattaPackageListActivity.this, MattaPackageDetailActivity.class);
					if (!StringUtil.isNullOrEmpty(mMattaPackageListRequest.getKeyword()))
						intent.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD,mMattaPackageListRequest.getKeyword());
					intent.putExtra(MattaConstants.DATA_MATTA_PACKAGE_DETAIL_RESPONSE, detailResponse);
					intent.putExtra(MattaConstants.DATA_MATTA_PACKAGE_DETAIL_REQUEST, mMattaPackageDetailRequest);
					startActivity(intent);
				} else {
					showInfoDialog(getResources().getString(R.string.no_result_found));
				}
			}
			stopSppiner();
		} else if (msg.arg2 == MattaEvents.MATTA_PACKAGE_LIST_EVENT) {
			loadingNextPageData = false;
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				MattaPackageListResponse oldResponse = mMattaPackageListResponse;
				mMattaPackageListResponse = (MattaPackageListResponse) msg.obj;
				oldResponse.getResults().appendPackageListAtEnd(mMattaPackageListResponse.getResults().getPackage());
				mMattaPackageListResponse.getResults().setPackage(oldResponse.getResults().getPackage());
				mMattaPackageListAdapter.setData(mMattaPackageListResponse.getResults().getPackage());
				mMattaPackageListAdapter.notifyDataSetChanged();
			}
			stopSppiner();
		} else if (msg.arg2 == MattaEvents.MATTA_FILTER_SEARCH_EVENT) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				displayRefineWithAttributeSpinnersPreloaded((RefineSelectorResponse) msg.obj, MattaFilterSearchActivity.ATTR_SELECTION);
			}
			stopSppiner();
		} else if (msg.arg2 == Events.CITY_LISTING) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				GenralListResponse glistRes = (GenralListResponse) msg.obj;
				cityList = glistRes.getCityOrLocalityList();
				Intent intent = new Intent(MattaPackageListActivity.this, AdvanceSelectCity.class);
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
				Intent intent = new Intent(MattaPackageListActivity.this, AdvanceSelectLocalityActivity.class);
				localityItems = new ArrayList<String>();
				for (CityOrLocality dealCityOrLoc : localityList) {
					localityItems.add(dealCityOrLoc.getName());
				}
				intent.putExtra("LOCALITY_LIST", localityItems);
				intent.putStringArrayListExtra("LOCALITY_INDEX", selectedLocalityindex);
				intent.putExtra("SELECTED_LOCALITIES", selectedLocalityItemsforHeader);
				startActivityForResult(intent, AppConstants.LOCALITY_REQUEST);
			}
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
		MattaPackageListController packageListController = new MattaPackageListController(MattaPackageListActivity.this, MattaEvents.MATTA_PACKAGE_LIST_EVENT);
		mMattaPackageListRequest.setPageNumber(pageNumber);
		packageListController.requestService(mMattaPackageListRequest);
		startSppiner();
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
			if (StringUtil.isNullOrEmpty(mMattaPackageListResponse.getResults().getTotal_Records_Found()) 
					|| mMattaPackageListResponse.getResults().getTotal_Records_Found().equals("0") ) {
				showInfoDialog(getResources().getString(R.string.no_result_found));
			} else {
				refineSearch();
			}
			AnalyticsHelper.logEvent(FlurryEventsConstants.MODIFY_SEARCH_CLICK);
			break;
		case R.id.mainSearchButton:
			mSearchEditText.setText(mSearchEditText.getText().toString().trim());
			String JSON_EXTRA = jsonForSearch();
			performSearch(mSearchEditText.getText().toString(), JSON_EXTRA, Events.COMBIND_LISTING_NEW_LISTING_PAGE);
			mMattaPackageListRequest.setKeyword(mSearchEditText.getText().toString());
			mMattaPackageListRequest.setCategoryTitle(mSearchEditText.getText().toString());
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
				Intent cityIntent = new Intent(MattaPackageListActivity.this, AdvanceSelectCity.class);
				cityIntent.putExtra("CITY_LIST", cityListString);
				cityIntent.putExtra("SELECTED_CITY", selectedCityforHeader);
				startActivityForResult(cityIntent, AppConstants.CITY_REQUEST);
			} else
				setSearchCity();
			break;
		case R.id.currentLocality:
			if (localityItems != null && localityItems.size() > 0) {
				Intent localityIntent = new Intent(MattaPackageListActivity.this, AdvanceSelectLocalityActivity.class);
				localityIntent.putExtra("LOCALITY_LIST", localityItems);
				localityIntent.putExtra("SELECTED_LOCALITIES", selectedLocalityItemsforHeader);
				localityIntent.putStringArrayListExtra("LOCALITY_INDEX", selectedLocalityindex);
				startActivityForResult(localityIntent, AppConstants.LOCALITY_REQUEST);
			} else 
				setSearchLocality(city_id);
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
		if (mSelctorResp != null) 
			displayRefineWithAttributeSpinnersPreloaded(mSelctorResp, MattaFilterSearchActivity.ATTR_SELECTION);
		else 
			fetchRefineAttribute();
	} 

	private void fetchRefineAttribute() {
		RefineAttributeController refineController = new RefineAttributeController(MattaPackageListActivity.this, MattaEvents.MATTA_FILTER_SEARCH_EVENT);
		startSppiner();
		MattaFilterSearchRequest refineSearchRequest = new MattaFilterSearchRequest(MattaPackageListActivity.this);
		refineSearchRequest.setHallId(mMattaPackageListRequest.getHallId());
		refineSearchRequest.setId(mMattaPackageListRequest.getCompanyId());
		refineSearchRequest.setSource(mMattaPackageListResponse.getResults().getPackage().get(0).getSource());
		refineSearchRequest.setsearchType(MattaConstants.MattaFilterPackage);
		refineController.requestService(refineSearchRequest);
	}

	private void displayRefineWithAttributeSpinnersPreloaded(RefineSelectorResponse selectorRes, int selectionMode) {
		Intent intent = new Intent(MattaPackageListActivity.this, MattaFilterSearchActivity.class);
		intent.putExtra(AppConstants.REFINE_ATTR_RESPONSE, selectorRes);
		intent.putExtra(MattaFilterSearchActivity.SELECTOR_MODE, selectionMode);
		intent.putExtra(MattaConstants.DATA_MATTA_PACKAGE_LIST_REQUEST, mMattaPackageListRequest);
		startActivityForResult(intent, 1);
	}

	@Override
	public void onPositiveDialogButton(int id) {
		if (id == CustomDialog.CONFIRMATION_DIALOG) {
			refineSearch();
			isModifySearchDialogOpen = false;
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

	@Override
	public void onBackPressed() {
		MaxisMainActivity.isCitySelected = false;
		super.onBackPressed();
	}
}