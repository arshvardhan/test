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
import com.kelltontech.maxisgetit.adapters.matta.BoothListBannerViewAdapter;
import com.kelltontech.maxisgetit.adapters.matta.MattaBoothListAdapter;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.constants.matta.MattaConstants;
import com.kelltontech.maxisgetit.constants.matta.MattaEvents;
import com.kelltontech.maxisgetit.controllers.RefineAttributeController;
import com.kelltontech.maxisgetit.controllers.matta.MattaBoothDetailController;
import com.kelltontech.maxisgetit.controllers.matta.MattaBoothListController;
import com.kelltontech.maxisgetit.controllers.matta.MattaPackageListController;
import com.kelltontech.maxisgetit.dao.CityOrLocality;
import com.kelltontech.maxisgetit.model.matta.booths.detail.MattaBoothDetailResponse;
import com.kelltontech.maxisgetit.model.matta.booths.list.BoothListBanner;
import com.kelltontech.maxisgetit.model.matta.booths.list.BoothModel;
import com.kelltontech.maxisgetit.model.matta.booths.list.MattaBoothListResponse;
import com.kelltontech.maxisgetit.model.matta.packages.list.MattaPackageListResponse;
import com.kelltontech.maxisgetit.requests.matta.MattaBoothDetailRequest;
import com.kelltontech.maxisgetit.requests.matta.MattaBoothListRequest;
import com.kelltontech.maxisgetit.requests.matta.MattaFilterSearchRequest;
import com.kelltontech.maxisgetit.requests.matta.MattaPackageListRequest;
import com.kelltontech.maxisgetit.response.CompanyListResponse;
import com.kelltontech.maxisgetit.response.GenralListResponse;
import com.kelltontech.maxisgetit.response.RefineSelectorResponse;
import com.kelltontech.maxisgetit.response.SubCategoryResponse;
import com.kelltontech.maxisgetit.ui.activities.AdvanceSelectCity;
import com.kelltontech.maxisgetit.ui.activities.AdvanceSelectLocalityActivity;
import com.kelltontech.maxisgetit.ui.activities.MaxisMainActivity;
import com.kelltontech.maxisgetit.ui.widgets.CustomDialog;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class MattaBoothListActivity extends MaxisMainActivity implements OnClickListener {

	private LinearLayout 				advanceSearchLayout;
	private LinearLayout 				mSearchContainer;
	private LinearLayout 				wholeSearchBoxContainer;
	private RelativeLayout 				bannerViewLayout;
	private LinearLayout 				circleIndicator;
	private TextView 					mRecordsFoundView;
	private TextView 					mRefineSearchView;
	private TextView 					currentCity, currentLocality;
	private TextView 					mainSearchButton;
	private TextView 					mHeaderTitle;

	private ImageView 					mSearchToggler;
	private ImageView 					mSearchBtn;
	private ImageView 					upArrow;
	private ImageView 					mHomeIconView;
	private ImageView 					mProfileIconView;
	private ImageView 					mHeaderBackButton;

	private ListView 					mCompanyList;
	private EditText 					mSearchEditText;
	private ViewPager	 				bannerView;
	private MattaBoothListRequest 		mMattaBoothListRequest;
	private MattaPackageListRequest 	packageListRequest;
	private MattaBoothDetailRequest 	detailRequest;
	private RefineSelectorResponse 		mSelctorResp;
	private MattaBoothListResponse 		mMattaBoothListResponse;
	ArrayList<CityOrLocality> 			localityList;
	ArrayList<String> 					ids = new ArrayList<String>();
	ArrayList<String> 					selectedLocalityindex;
	ArrayList<CityOrLocality> 			cityList;
	private ArrayList<String> 			selectedLocalityItemsforHeader = new ArrayList<String>();
	//	private ArrayList<String> 			selectedLocalityItems = new ArrayList<String>();
	private ArrayList<String> 			cityListString	= new ArrayList<String>();
	private ArrayList<String> 			localityItems;
	private int 						totalBanners;
	private MattaBoothListAdapter 		mMattaBoothListAdapter;
	private BoothListBannerViewAdapter 	mMattaBannerViewAdapter;
	private String 						selectedCityforHeader = "Entire Malaysia";
	private String 						selectedCity = "Entire Malaysia";
	//	private String 						mMattaThumbUrl;
	private boolean 					loadingNextPageData;
	private boolean 					isModifySearchDialogOpen;
	private boolean 					mScrollUp;
	private boolean 					isAdvanceSearchLayoutOpen = false;
	private boolean 					stopSliding = false;
	boolean 							isFirstTime = false;
	boolean 							isSuccessfull = false;
	boolean 							isFromSearch = false;
	private int 						city_id = -1;
	private int 						previousState, currentState;
	private int 						flipperVisibleItemPosition = 0;
	private Runnable 					animateViewPager;
	private Handler 					bannerHandler;
	private static final long 			ANIM_VIEWPAGER_DELAY = 5000;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				Bundle bundle 						= 		data.getExtras();
				mMattaBoothListResponse 			= 		(MattaBoothListResponse) bundle.getSerializable(MattaConstants.DATA_MATTA_BOOTH_LIST_RESPONSE);
				mMattaBoothListRequest 				= 		(MattaBoothListRequest) bundle.getSerializable(MattaConstants.DATA_MATTA_BOOTH_LIST_REQUEST);
				mSelctorResp 						= 		bundle.getParcelable(AppConstants.REFINE_ATTR_RESPONSE);
				mRecordsFoundView.setText(mMattaBoothListResponse.getResults().getTotalRecordsFound() + " " + getResources().getString(R.string.matta_record_found));
				mMattaBoothListRequest.setSearchRefined(true);
				if (StringUtil.isNullOrEmpty(mMattaBoothListResponse.getResults().getTotalRecordsFound()) 
						|| mMattaBoothListResponse.getResults().getTotalRecordsFound().equals("0") )
					showInfoDialog(getResources().getString(R.string.no_result_found));

				showHideBanner();

				ArrayList<BoothModel> mBoothModel = 		mMattaBoothListResponse.getResults().getBooth();
				mMattaBoothListAdapter.setData(mBoothModel);
				mMattaBoothListAdapter.notifyDataSetChanged();
				mCompanyList.setAdapter(mMattaBoothListAdapter);
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
			//			selectedLocalityItems = selectedLocalityItemsforHeader;
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
		setContentView(R.layout.activity_matta_booth_list);
		AnalyticsHelper.logEvent(FlurryEventsConstants.MATTA_BOOTH_LISTING, true);
		UiUtils.hideKeyboardOnTappingOutside(findViewById(R.id.cl_root_layout),this);
		ImageLoader.initialize(MattaBoothListActivity.this);

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
		mSearchBtn.setOnClickListener(MattaBoothListActivity.this);
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
			mMattaBoothListRequest 		= (MattaBoothListRequest) bundle.getSerializable(MattaConstants.DATA_MATTA_BOOTH_LIST_REQUEST);
			mMattaBoothListResponse  	= (MattaBoothListResponse) bundle.getSerializable(MattaConstants.DATA_MATTA_BOOTH_LIST_RESPONSE);
		}
		if (mMattaBoothListRequest != null && !StringUtil.isNullOrEmpty(mMattaBoothListRequest.getmHallTitle())) {
			mHeaderTitle.setText(Html.fromHtml(mMattaBoothListRequest.getmHallTitle()));
			//			mMattaThumbUrl = mMattaBoothListRequest.getmMattaThumbUrl();
		}

		mRecordsFoundView.setText((!StringUtil.isNullOrEmpty(mMattaBoothListResponse.getResults().getTotalRecordsFound()) ? mMattaBoothListResponse.getResults().getTotalRecordsFound() : "0") + " " + getResources().getString(R.string.matta_record_found));

		ArrayList<BoothModel> boothInfo 			= 		mMattaBoothListResponse.getResults().getBooth();
		mMattaBoothListAdapter 						= 		new MattaBoothListAdapter(MattaBoothListActivity.this);
		mMattaBoothListAdapter.setData(boothInfo);
		mMattaBoothListAdapter.notifyDataSetChanged();
		mCompanyList.setAdapter(mMattaBoothListAdapter);

		mCompanyList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				MattaBoothDetailController boothDetailcontroller = new MattaBoothDetailController(MattaBoothListActivity.this, MattaEvents.MATTA_BOOTH_DETAIL_EVENT);
				detailRequest = new MattaBoothDetailRequest();
				detailRequest.setCompanyId(!StringUtil.isNullOrEmpty(((BoothModel) mMattaBoothListAdapter.getItem(arg2)).getCId()) ? ((BoothModel) mMattaBoothListAdapter.getItem(arg2)).getCId() : "");
				detailRequest.setSource(!StringUtil.isNullOrEmpty(((BoothModel) mMattaBoothListAdapter.getItem(arg2)).getSource()) ? ((BoothModel) mMattaBoothListAdapter.getItem(arg2)).getSource() : "");
				detailRequest.setHallId(!StringUtil.isNullOrEmpty(((BoothModel) mMattaBoothListAdapter.getItem(arg2)).getHallId()) ? ((BoothModel) mMattaBoothListAdapter.getItem(arg2)).getHallId() : "");
				startSppiner();
				boothDetailcontroller.requestService(detailRequest);
			}
		});

		mCompanyList.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView listView, int scrollState) { }
			@Override
			public void onScroll(AbsListView listView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				Log.w("", "firstVisibleItem " + firstVisibleItem + " visibleItemCount " + visibleItemCount + " totalItemCount " + totalItemCount);
				int number = firstVisibleItem + visibleItemCount;
				if (mMattaBoothListResponse != null) {
					if ((number) % Integer.parseInt(mMattaBoothListResponse.getResults().getRecords_Per_Page()) == 0
							&& number > 0
							&& number == totalItemCount
							&& totalItemCount < Integer.parseInt(mMattaBoothListResponse.getResults().getTotalRecordsFound())) {
						if (loadingNextPageData)
							return;
						loadingNextPageData = true;
						if (mMattaBoothListResponse.getResults().getPageNumber() < MattaConstants.MAX_RECORD_COUNT / 10)
							loadPageData(mMattaBoothListResponse.getResults().getPageNumber() + 1);
					} else if (number >= MattaConstants.MAX_RECORD_COUNT && !isModifySearchDialogOpen && mScrollUp && Integer.parseInt(mMattaBoothListResponse.getResults().getTotalRecordsFound()) > 100) {
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

	public void onDealsButtonClick(int position) {
		MattaPackageListController packageListController = new MattaPackageListController(MattaBoothListActivity.this, MattaEvents.MATTA_PACKAGE_LIST_EVENT);
		packageListRequest = new MattaPackageListRequest();
		String companyId = mMattaBoothListResponse.getResults().getBooth().get(position).getCId();
		String hallId = mMattaBoothListResponse.getResults().getBooth().get(position).getHallId();
		String source = mMattaBoothListResponse.getResults().getBooth().get(position).getSource();
		packageListRequest.setCompanyId((!StringUtil.isNullOrEmpty(companyId)) ? companyId : "");
		packageListRequest.setSource(!StringUtil.isNullOrEmpty(source) ? source : "");
		packageListRequest.setHallId(!StringUtil.isNullOrEmpty(hallId) ? hallId : "");
		packageListRequest.setListType((!StringUtil.isNullOrEmpty(mMattaBoothListRequest.getListType())) ? mMattaBoothListRequest.getListType() : "");
		packageListRequest.setmHallTitle((!StringUtil.isNullOrEmpty(mMattaBoothListRequest.getmHallTitle())) ? mMattaBoothListRequest.getmHallTitle(): "");
		packageListRequest.setmMattaThumbUrl((!StringUtil.isNullOrEmpty(mMattaBoothListRequest.getmMattaThumbUrl())) ? mMattaBoothListRequest.getmMattaThumbUrl(): "");
		packageListController.requestService(packageListRequest);
		startSppiner();
	}

	private void showHideBanner() {
		try {
		if (mMattaBoothListResponse != null 
				&& mMattaBoothListResponse.getResults().getBanners() != null 
				&& mMattaBoothListResponse.getResults().getBanners().size() > 0
				&& mMattaBoothListResponse.getResults().getBanners().get(0)  != null
				&& mMattaBoothListResponse.getResults().getBanners().get(0).getBanner() != null 
				&& mMattaBoothListResponse.getResults().getBanners().get(0).getBanner().size() > 0) {
			ImageLoader.initialize(MattaBoothListActivity.this);
			ArrayList<BoothListBanner> bannerList = (ArrayList<BoothListBanner>) mMattaBoothListResponse.getResults().getBanners().get(0).getBanner();
			totalBanners = bannerList.size();
			bannerViewLayout.setVisibility(View.VISIBLE);
			mMattaBannerViewAdapter = new BoothListBannerViewAdapter(getSupportFragmentManager(), bannerList, this, MattaConstants.FLOW_FROM_MATTA_BOOTH_LIST);
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
	} catch (Exception e) {
		AnalyticsHelper.onError(FlurryEventsConstants.SHOW_HIDE_BANNER_ERROR, " MattaBoothListActivity: " + AppConstants.SHOW_HIDE_BANNER_ERROR_MSG, e);
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
		AnalyticsHelper.trackSession(MattaBoothListActivity.this, MattaConstants.Matta_Booth_Listing);
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
				|| event == MattaEvents.MATTA_BANNER_LANDING_PKG_LIST_EVENT
				|| event == MattaEvents.MATTA_BANNER_LANDING_BOOTH_DETAIL_EVENT) {
			super.setScreenData(screenData, event, time);
			return;
		} else if (event == MattaEvents.MATTA_FILTER_SEARCH_EVENT) {
			Message catRefine = (Message) screenData;
			handler.sendMessage(catRefine);
			return;
		} else if (event == Events.CITY_LISTING || event == Events.LOCALITY_LISTING) {
			Message message = (Message) screenData;
			handler.sendMessage(message);
		} else if (event == MattaEvents.MATTA_PACKAGE_LIST_EVENT) {
			MattaPackageListResponse packageListRes = (MattaPackageListResponse) screenData;
			Message message = new Message();
			message.arg2 = event;
			if ((packageListRes.getResults() != null) && (!StringUtil.isNullOrEmpty(packageListRes.getResults().getError_Code())) && (packageListRes.getResults().getError_Code().equals("0"))) {
				if (packageListRes.getResults().getPackage().size() < 1 
						|| StringUtil.isNullOrEmpty(packageListRes.getResults().getTotal_Records_Found()) 
						|| packageListRes.getResults().getTotal_Records_Found().equals("0")) {
					message.arg1 = 1;
					message.obj = new String("No Result Found");
				} else {
					message.arg1 = 0;
					message.obj = packageListRes;
				}
			} else {
				message.arg1 = 1;
				message.obj = getResources().getString(R.string.communication_failure);
			}
			handler.sendMessage(message);
			return;			
		} else if (event == MattaEvents.MATTA_BOOTH_LIST_EVENT) {
			MattaBoothListResponse boothListRes = (MattaBoothListResponse) screenData;
			Message message = new Message();
			message.arg2 = event;
			if ((boothListRes.getResults() != null) && (!StringUtil.isNullOrEmpty(boothListRes.getResults().getError_Code())) && (boothListRes.getResults().getError_Code().equals("0"))) {
				if (boothListRes.getResults().getBooth().size() < 1 
						|| StringUtil.isNullOrEmpty(boothListRes.getResults().getTotalRecordsFound()) 
						|| boothListRes.getResults().getTotalRecordsFound().equals("0")) {
					message.arg1 = 1;
					message.obj = new String("No Result Found");
				} else {
					message.arg1 = 0;
					message.obj = boothListRes;
				}
			} else {
				message.arg1 = 1;
				message.obj = getResources().getString(R.string.communication_failure);
			}
			handler.sendMessage(message);
			return;			
		} else if (event == MattaEvents.MATTA_BOOTH_DETAIL_EVENT) {
			MattaBoothDetailResponse boothDetail = (MattaBoothDetailResponse) screenData;
			Message message = new Message();
			message.arg2 = event;
			if ((boothDetail.getResults() != null) && (!StringUtil.isNullOrEmpty(boothDetail.getResults().getError_Code())) && (boothDetail.getResults().getError_Code().equals("0"))) {
				if (boothDetail.getResults().getCompany() == null || StringUtil.isNullOrEmpty(boothDetail.getResults().getCompany().getCName())) {
					message.arg1 = 1;
					message.obj = new String("No Result Found");
				} else {
					message.arg1 = 0;
					message.obj = boothDetail;
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
				|| msg.arg2 == MattaEvents.MATTA_BANNER_LANDING_PKG_LIST_EVENT
				|| msg.arg2 == MattaEvents.MATTA_BANNER_LANDING_BOOTH_DETAIL_EVENT) {
			super.updateUI(msg);
		} else if (msg.arg2 == MattaEvents.MATTA_BOOTH_DETAIL_EVENT) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				MattaBoothDetailResponse detailResponse = (MattaBoothDetailResponse) msg.obj;
				if (!StringUtil.isNullOrEmpty(detailResponse.getResults().getCompany().getCName())) {
					Intent intent = new Intent(MattaBoothListActivity.this, MattaBoothDetailActivity.class);
					if (!StringUtil.isNullOrEmpty(mMattaBoothListRequest.getKeyword()))
						intent.putExtra(AppConstants.GLOBAL_SEARCH_KEYWORD,mMattaBoothListRequest.getKeyword());
					intent.putExtra(MattaConstants.DATA_MATTA_BOOTH_DETAIL_RESPONSE, detailResponse);
					intent.putExtra(MattaConstants.DATA_MATTA_BOOTH_DETAIL_REQUEST, detailRequest);
					startActivity(intent);
				} else {
					showInfoDialog(getResources().getString(R.string.no_result_found));
				}
			}
			stopSppiner();
		} else if (msg.arg2 == MattaEvents.MATTA_PACKAGE_LIST_EVENT) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				MattaPackageListResponse packageResponse = (MattaPackageListResponse) msg.obj;
				Intent packageListIntent = new Intent(MattaBoothListActivity.this, MattaPackageListActivity.class);
				packageListIntent.putExtra(MattaConstants.DATA_MATTA_PACKAGE_LIST_REQUEST, packageListRequest);
				packageListIntent.putExtra(MattaConstants.DATA_MATTA_PACKAGE_LIST_RESPONSE, packageResponse);
				startActivity(packageListIntent);
			}
			stopSppiner();
		} else if (msg.arg2 == MattaEvents.MATTA_BOOTH_LIST_EVENT) {
			loadingNextPageData = false;
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				MattaBoothListResponse oldResponse = mMattaBoothListResponse;
				mMattaBoothListResponse = (MattaBoothListResponse) msg.obj;
				oldResponse.getResults().appendBoothListAtEnd(mMattaBoothListResponse.getResults().getBooth());
				mMattaBoothListResponse.getResults().setBooth(oldResponse.getResults().getBooth());
				mMattaBoothListAdapter.setData(mMattaBoothListResponse.getResults().getBooth());
				mMattaBoothListAdapter.notifyDataSetChanged();
			}
			stopSppiner();
		} else if (msg.arg2 == MattaEvents.MATTA_FILTER_SEARCH_EVENT) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				displayRefineWithAttributeSpinnersPreloaded((RefineSelectorResponse) msg.obj,MattaFilterSearchActivity.ATTR_SELECTION_BY_SEARCH);
			}
			stopSppiner();
		} else if (msg.arg2 == Events.CITY_LISTING) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				GenralListResponse glistRes = (GenralListResponse) msg.obj;
				cityList = glistRes.getCityOrLocalityList();
				Intent intent = new Intent(MattaBoothListActivity.this, AdvanceSelectCity.class);
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
				Intent intent = new Intent(MattaBoothListActivity.this, AdvanceSelectLocalityActivity.class);
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
				Intent intent = new Intent(MattaBoothListActivity.this, MattaPackageListActivity.class);
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
		MattaBoothListController boothListController = new MattaBoothListController(MattaBoothListActivity.this, MattaEvents.MATTA_BOOTH_LIST_EVENT);
		mMattaBoothListRequest.setPageNumber(pageNumber);
		startSppiner();
		boothListController.requestService(mMattaBoothListRequest);
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
			if (StringUtil.isNullOrEmpty(mMattaBoothListResponse.getResults().getTotalRecordsFound()) 
					|| mMattaBoothListResponse.getResults().getTotalRecordsFound().equals("0") ) {
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
			mMattaBoothListRequest.setKeyword(mSearchEditText.getText().toString());
			mMattaBoothListRequest.setCategoryTitle(mSearchEditText.getText().toString());
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
				Intent cityIntent = new Intent(MattaBoothListActivity.this, AdvanceSelectCity.class);
				cityIntent.putExtra("CITY_LIST", cityListString);
				cityIntent.putExtra("SELECTED_CITY", selectedCityforHeader);
				startActivityForResult(cityIntent, AppConstants.CITY_REQUEST);
			} else
				setSearchCity();
			break;
		case R.id.currentLocality:
			if (localityItems != null && localityItems.size() > 0) {
				Intent localityIntent = new Intent(MattaBoothListActivity.this, AdvanceSelectLocalityActivity.class);
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
			displayRefineWithAttributeSpinnersPreloaded(mSelctorResp, MattaFilterSearchActivity.ATTR_SELECTION_BY_SEARCH);
		else 
			fetchRefineAttribute();
	} 

	private void fetchRefineAttribute() {
		RefineAttributeController refineController = new RefineAttributeController(MattaBoothListActivity.this, MattaEvents.MATTA_FILTER_SEARCH_EVENT);
		startSppiner();
		MattaFilterSearchRequest refineSearchRequest = new MattaFilterSearchRequest(MattaBoothListActivity.this);
		refineSearchRequest.setId(mMattaBoothListRequest.getHallId());
		refineSearchRequest.setsearchType(MattaConstants.MattaFilterBooth);
		refineSearchRequest.setSource(mMattaBoothListResponse.getResults().getBooth().get(0).getSource());
		refineController.requestService(refineSearchRequest);
	}

	private void displayRefineWithAttributeSpinnersPreloaded(RefineSelectorResponse selectorRes, int selectionMode) {
		Intent intent = new Intent(MattaBoothListActivity.this, MattaFilterSearchActivity.class);
		intent.putExtra(AppConstants.REFINE_ATTR_RESPONSE, selectorRes);
		intent.putExtra(MattaFilterSearchActivity.SELECTOR_MODE, selectionMode);
		intent.putExtra(MattaConstants.DATA_MATTA_BOOTH_LIST_REQUEST, mMattaBoothListRequest);
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