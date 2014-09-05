package com.kelltontech.maxisgetit.ui.activities.matta;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kelltontech.framework.imageloader.ImageLoader;
import com.kelltontech.framework.utils.StringUtil;
import com.kelltontech.framework.utils.UiUtils;
import com.kelltontech.maxisgetit.R;
import com.kelltontech.maxisgetit.adapters.matta.MattaBannerViewAdapter;
import com.kelltontech.maxisgetit.adapters.matta.MattaHallListAdapter;
import com.kelltontech.maxisgetit.constants.AppConstants;
import com.kelltontech.maxisgetit.constants.Events;
import com.kelltontech.maxisgetit.constants.FlurryEventsConstants;
import com.kelltontech.maxisgetit.constants.matta.MattaConstants;
import com.kelltontech.maxisgetit.constants.matta.MattaEvents;
import com.kelltontech.maxisgetit.controllers.SubCategoryController;
import com.kelltontech.maxisgetit.controllers.TypeByCategoryController;
import com.kelltontech.maxisgetit.controllers.matta.MattaBoothListController;
import com.kelltontech.maxisgetit.controllers.matta.MattaPackageListController;
import com.kelltontech.maxisgetit.dao.CategoryGroup;
import com.kelltontech.maxisgetit.dao.CityOrLocality;
import com.kelltontech.maxisgetit.dao.matta.MattaBanner;
import com.kelltontech.maxisgetit.dao.matta.MattaHallList;
import com.kelltontech.maxisgetit.model.matta.booths.list.MattaBoothListResponse;
import com.kelltontech.maxisgetit.model.matta.packages.list.MattaPackageListResponse;
import com.kelltontech.maxisgetit.requests.matta.MattaBoothListRequest;
import com.kelltontech.maxisgetit.requests.matta.MattaPackageListRequest;
import com.kelltontech.maxisgetit.response.GenralListResponse;
import com.kelltontech.maxisgetit.response.matta.MattaHallListResponse;
import com.kelltontech.maxisgetit.ui.activities.AdvanceSelectCity;
import com.kelltontech.maxisgetit.ui.activities.AdvanceSelectLocalityActivity;
import com.kelltontech.maxisgetit.ui.activities.MaxisMainActivity;
import com.kelltontech.maxisgetit.ui.widgets.AnimatedLinearLayout;
import com.kelltontech.maxisgetit.utils.AnalyticsHelper;

public class MattaHallListActivity extends MaxisMainActivity  implements AnimationListener {
	private ListView mGridView;
	private AnimatedLinearLayout mSearchContainer;
	private AnimatedLinearLayout advanceSearchLayout;
	private AnimatedLinearLayout wholeSearchBoxContainer;
	private ImageView mSearchToggler;
	private TextView mHeaderTitle;
	private MattaHallListAdapter mMattaHallListAdapter;
	private MattaBannerViewAdapter mMatatBannerViewAdapter;
	private ImageView mSearchBtn;
	private EditText mSearchEditText;
	private MattaHallListResponse mMattaHallListResponse;
	private CategoryGroup mCategoryGroup;
	private MattaHallList mHallList;
	private MattaBoothListRequest boothListReq;
	private MattaPackageListRequest packageListReq;
	private ImageView mProfileIconView;
	private ImageView mHeaderBackButton;
	private ImageView mHomeIconView;

	private boolean isAdvanceSearchLayoutOpen = false;
	private TextView currentCity, currentLocality;
	private ImageView upArrow;
	private ArrayList<String> cityListString = new ArrayList<String>();
	private ArrayList<String> localityItems;
	ArrayList<CityOrLocality> cityList;
	private String selectedCity = "Entire Malaysia";
	private int city_id = -1;
	private int previousState, currentState;

	private ArrayList<String> selectedLocalityItems;
	ArrayList<CityOrLocality> localityList;
	ArrayList<String> ids = new ArrayList<String>();
	TextView mainSearchButton;
	ArrayList<String> selectedLocalityindex;
	//	private Animation inAnimation;
	//	private Animation outAnimation;

	private ViewPager bannerView;
	private int totalBanners;
	private RelativeLayout bannerViewLayout;
	private LinearLayout circleIndicator;
	private int flipperVisibleItemPosition = 0;
	boolean stopSliding = false;
	private Runnable animateViewPager;
	private Handler bannerHandler;
	private static final long ANIM_VIEWPAGER_DELAY = 5000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_matta_hall_list);
		ImageLoader.initialize(MattaHallListActivity.this);

		// loading the animation
		//        inAnimation 	= 	AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
		//        outAnimation 	= 	AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);

		// set animation listener
		//        inAnimation.setAnimationListener(this);
		//        outAnimation.setAnimationListener(this);

		AnalyticsHelper.logEvent(FlurryEventsConstants.MATTA_HALL_LISTING);

		if(getIntent().getExtras() != null) {
			Bundle bundle = getIntent().getExtras();
			mMattaHallListResponse = bundle.getParcelable(MattaConstants.DATA_MATTA_HALL_LIST_RESPONSE);
			mCategoryGroup = bundle.getParcelable(MattaConstants.DATA_MATTA_BOOTH_LIST_TITLE);
		}

		UiUtils.hideKeyboardOnTappingOutside(findViewById(R.id.subcat_root_layout), this);

		mProfileIconView = (ImageView) findViewById(R.id.show_profile_icon);
		mProfileIconView.setOnClickListener(this);
		mSearchBtn = (ImageView) findViewById(R.id.search_icon_button);
		mSearchBtn.setOnClickListener(MattaHallListActivity.this);
		mSearchEditText = (EditText) findViewById(R.id.search_box);
		mSearchContainer = (AnimatedLinearLayout) findViewById(R.id.search_box_container);
		//		mSearchContainer.setInAnimation(inAnimation);
		//		mSearchContainer.setOutAnimation(outAnimation);
		mSearchToggler = (ImageView) findViewById(R.id.search_toggler);
		mSearchToggler.setOnClickListener(this);
		mHeaderBackButton = (ImageView) findViewById(R.id.header_btn_back);
		mHeaderBackButton.setOnClickListener(this);

		mHomeIconView = (ImageView) findViewById(R.id.goto_home_icon);
		mHomeIconView.setOnClickListener(this);

		mHeaderTitle = (TextView) findViewById(R.id.header_title);

		advanceSearchLayout = (AnimatedLinearLayout) findViewById(R.id.advanceSearch);
		advanceSearchLayout.setVisibility(View.GONE);
		//		advanceSearchLayout.setInAnimation(inAnimation);
		//		advanceSearchLayout.setOutAnimation(outAnimation);

		upArrow = (ImageView) findViewById(R.id.upArrow);
		upArrow.setOnClickListener(this);

		mHeaderTitle.setText(Html.fromHtml("<b>" + mCategoryGroup.getCategoryTitle() + "</b>"));

		currentCity = (TextView) findViewById(R.id.currentCity);
		currentLocality = (TextView) findViewById(R.id.currentLocality);
		currentCity.setText(Html.fromHtml("in " + "<b>" + selectedCity + "</b>"));

		currentCity.setOnClickListener(this);
		currentLocality.setOnClickListener(this);

		mainSearchButton = (TextView) findViewById(R.id.mainSearchButton);
		mainSearchButton.setOnClickListener(this);

		wholeSearchBoxContainer = (AnimatedLinearLayout) findViewById(R.id.whole_search_box_container);
		//		wholeSearchBoxContainer.setInAnimation(inAnimation);
		//		wholeSearchBoxContainer.setOutAnimation(outAnimation);



		mGridView = (ListView) findViewById(R.id.grid_list);
		mMattaHallListAdapter = new MattaHallListAdapter(MattaHallListActivity.this);
		mMattaHallListAdapter.setData(mMattaHallListResponse.getMattaHallList());
		mGridView.setAdapter(mMattaHallListAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View arg1, int position, long arg3) {
				gotoScreen(adapter, position);
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

		bannerViewLayout = (RelativeLayout) findViewById(R.id.bannerView_LnrLayout);
		bannerView = (ViewPager) findViewById(R.id.subcategory_banner);
		bannerView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				if (totalBanners > 1) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_CANCEL:
						break;
					case MotionEvent.ACTION_UP:
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

		circleIndicator = (LinearLayout) findViewById(R.id.indicatorlinearlayout);
		((ImageView) findViewById(R.id.play_pasue_icon)).setOnClickListener(this);
	}

	private void showHideBanner() {
		try {
		if (mMattaHallListResponse != null && mMattaHallListResponse.getBannerList() != null && mMattaHallListResponse.getBannerList().size() > 0) {
			ArrayList<MattaBanner> bannerList = mMattaHallListResponse.getBannerList();
			totalBanners = bannerList.size();
			bannerViewLayout.setVisibility(View.VISIBLE);
			mMatatBannerViewAdapter = new MattaBannerViewAdapter(getSupportFragmentManager(), bannerList, this, MattaConstants.FLOW_FROM_MATTA_HALL_LIST);
			mMatatBannerViewAdapter.notifyDataSetChanged();
			bannerView.removeAllViews();			
			bannerView.setAdapter(mMatatBannerViewAdapter);
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
			AnalyticsHelper.onError(FlurryEventsConstants.SHOW_HIDE_BANNER_ERROR, " MattaHallListActivity: " + AppConstants.SHOW_HIDE_BANNER_ERROR_MSG, e);
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
		AnalyticsHelper.trackSession(MattaHallListActivity.this, MattaConstants.Matta_Hall_Listing);
	}

	@Override
	protected void onPause() {
		if (bannerHandler != null)
			bannerHandler.removeCallbacks(animateViewPager);
		super.onPause();
	}

	protected void gotoScreen(AdapterView<?> adapter, int position) {
		mHallList = (MattaHallList) adapter.getItemAtPosition(position);

		if (!StringUtil.isNullOrEmpty(mHallList.getmHallListType().trim())) {

			if (MattaConstants.MattaBoothListType.equals(mHallList.getmHallListType())) {
				showBoothListing(mHallList);
			} else if (MattaConstants.MattaPackageListType.equals(mHallList.getmHallListType())) {
				showPackageListing(mHallList);
			}
		}
	}

	private void showBoothListing(MattaHallList hallList) {
		MattaBoothListController boothListController = new MattaBoothListController(MattaHallListActivity.this, MattaEvents.MATTA_BOOTH_LIST_EVENT);
		boothListReq = new MattaBoothListRequest();
		boothListReq.setSource(hallList.getmHallSource());
		boothListReq.setListType(hallList.getmHallListType());
		boothListReq.setHallId(hallList.getmHallId());
		boothListReq.setmHallTitle(hallList.getmHallName());
		boothListReq.setmMattaThumbUrl(hallList.getmHallImage());
		boothListController.requestService(boothListReq);
		startSppiner();
	}

	private void showPackageListing(MattaHallList hallList) {
		MattaPackageListController packageListController = new MattaPackageListController(MattaHallListActivity.this, MattaEvents.MATTA_PACKAGE_LIST_EVENT);
		packageListReq = new MattaPackageListRequest();
		packageListReq.setSource(hallList.getmHallSource());
		packageListReq.setListType(hallList.getmHallListType());
		//		packageListReq.setHallId(hallList.getmHallId()); // this node is removed after discussion with Ankesh and Arvind Gupta
		packageListReq.setmHallTitle(hallList.getmHallName());
		packageListReq.setmMattaThumbUrl(hallList.getmHallImage());
		packageListController.requestService(packageListReq);
		startSppiner();
	}

	@Override
	public void setScreenData(Object screenData, int event, long time) {
		if (event == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| event == Events.USER_DETAIL
				|| event == Events.BANNER_NAVIGATION_EVENT
				|| event == Events.BANNER_LANDING_DEAL_DETAIL_EVENT 
				|| event == Events.BANNER_LANDING_COMPANY_DETAIL_EVENT
				|| event == Events.BANNER_LANDING_COMPANY_LISTING_EVENT
				|| event == Events.BANNER_LANDING_SEARCH_EVENT
				|| event == MattaEvents.MATTA_BANNER_LANDING_PKG_LIST_EVENT
				|| event == MattaEvents.MATTA_BANNER_LANDING_BOOTH_DETAIL_EVENT) {
			super.setScreenData(screenData, event, time);
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
		} else {
			handler.sendMessage((Message) screenData);
		}
	}

	@Override
	public Activity getMyActivityReference() {
		return null;
	}

	@Override
	public void updateUI(Message msg) {
		if (msg.arg2 == Events.COMBIND_LISTING_NEW_LISTING_PAGE
				|| msg.arg2 == Events.USER_DETAIL
				|| msg.arg2 == Events.BANNER_NAVIGATION_EVENT
				|| msg.arg2 == Events.BANNER_LANDING_COMPANY_DETAIL_EVENT
				|| msg.arg2 == Events.BANNER_LANDING_DEAL_DETAIL_EVENT
				|| msg.arg2 == Events.BANNER_LANDING_COMPANY_LISTING_EVENT
				|| msg.arg2 == Events.BANNER_LANDING_SEARCH_EVENT
				|| msg.arg2 == MattaEvents.MATTA_BANNER_LANDING_PKG_LIST_EVENT
				|| msg.arg2 == MattaEvents.MATTA_BANNER_LANDING_BOOTH_DETAIL_EVENT) {
			super.updateUI(msg);
		} else if (msg.arg2 == Events.CITY_LISTING) {
			stopSppiner();
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				GenralListResponse glistRes = (GenralListResponse) msg.obj;
				cityList = glistRes.getCityOrLocalityList();
				Intent intent = new Intent(MattaHallListActivity.this,AdvanceSelectCity.class);
				for (CityOrLocality cityOrLocality : cityList) {
					cityListString.add(cityOrLocality.getName());
				}
				localityItems = null;
				ids = null;
				selectedLocalityindex = null;
				currentLocality.setText("Choose your Area");
				intent.putExtra("CITY_LIST", cityListString);
				intent.putExtra("SELECTED_CITY", selectedCity);
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
				Intent intent = new Intent(MattaHallListActivity.this, AdvanceSelectLocalityActivity.class);
				localityItems = new ArrayList<String>();
				for (CityOrLocality dealCityOrLoc : localityList) {
					localityItems.add(dealCityOrLoc.getName());
				}
				intent.putExtra("LOCALITY_LIST", localityItems);
				intent.putStringArrayListExtra("LOCALITY_INDEX", selectedLocalityindex);
				startActivityForResult(intent, AppConstants.LOCALITY_REQUEST);
			}
		} else if (msg.arg2 == MattaEvents.MATTA_BOOTH_LIST_EVENT) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				MattaBoothListResponse boothRes = (MattaBoothListResponse) msg.obj;
				Intent boothListIntent = new Intent(MattaHallListActivity.this, MattaBoothListActivity.class);
				boothListIntent.putExtra(MattaConstants.DATA_MATTA_BOOTH_LIST_REQUEST, boothListReq);
				boothListIntent.putExtra(MattaConstants.DATA_MATTA_BOOTH_LIST_RESPONSE, boothRes);
				startActivity(boothListIntent);
			}
			stopSppiner();
		} else if (msg.arg2 == MattaEvents.MATTA_PACKAGE_LIST_EVENT) {
			if (msg.arg1 == 1) {
				showInfoDialog((String) msg.obj);
			} else {
				MattaPackageListResponse packageRes = (MattaPackageListResponse) msg.obj;
				Intent packageListIntent = new Intent(MattaHallListActivity.this, MattaPackageListActivity.class);
				packageListIntent.putExtra(MattaConstants.DATA_MATTA_PACKAGE_LIST_REQUEST, packageListReq);
				packageListIntent.putExtra(MattaConstants.DATA_MATTA_PACKAGE_LIST_RESPONSE, packageRes);
				startActivity(packageListIntent);
			}
			stopSppiner();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_toggler:
			AnalyticsHelper.logEvent(FlurryEventsConstants.HOME_SEARCH_CLICK);
			if (wholeSearchBoxContainer.getVisibility() == View.VISIBLE) {
				wholeSearchBoxContainer.setVisibility(View.GONE);
				//				overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
			} else {
				wholeSearchBoxContainer.setVisibility(View.VISIBLE);
				//				overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
			}
			if (mSearchContainer.getVisibility() == View.VISIBLE) {
				mSearchContainer.setVisibility(View.GONE);
			} else {
				mSearchContainer.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.header_btn_back:
			AnalyticsHelper.logEvent(FlurryEventsConstants.BACK_CLICK);
			this.finish();
			break;
		case R.id.mainSearchButton:
			mSearchEditText.setText(mSearchEditText.getText().toString().trim());
			String JSON_EXTRA = jsonForSearch();
			performSearch(mSearchEditText.getText().toString(), JSON_EXTRA, Events.COMBIND_LISTING_NEW_LISTING_PAGE);
			break;
		case R.id.show_profile_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.SHOW_PROFILE_CLICK);
			onProfileClick();
			break;
		case R.id.goto_home_icon:
			AnalyticsHelper.logEvent(FlurryEventsConstants.GO_TO_HOME_CLICK);
			showHomeScreen();
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
				Intent cityIntent = new Intent(MattaHallListActivity.this,
						AdvanceSelectCity.class);
				cityIntent.putExtra("CITY_LIST", cityListString);
				cityIntent.putExtra("SELECTED_CITY", selectedCity);
				startActivityForResult(cityIntent, AppConstants.CITY_REQUEST);
			} else {
				setSearchCity();
			}
			break;
		case R.id.currentLocality:
			if (localityItems != null && localityItems.size() > 0) {
				Intent localityIntent = new Intent(
						MattaHallListActivity.this,
						AdvanceSelectLocalityActivity.class);
				localityIntent.putExtra("LOCALITY_LIST", localityItems);
				localityIntent.putStringArrayListExtra("LOCALITY_INDEX",
						selectedLocalityindex);
				startActivityForResult(localityIntent,
						AppConstants.LOCALITY_REQUEST);
			} else {
				setSearchLocality(city_id);
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

	protected void showSaleTemplateTypeScreen(String categoryId) {
		TypeByCategoryController tBCController = new TypeByCategoryController(MattaHallListActivity.this, Events.TYPE_BY_CATEGORY_EVENT);
		startSppiner();
		tBCController.requestService(categoryId);
	}

	protected void showSubcategories(CategoryGroup cat) {
		SubCategoryController controller = new SubCategoryController(MattaHallListActivity.this, Events.SUBCATEGORY_EVENT);
		controller.requestService(cat);
		startSppiner();
	}

	@Override
	protected void onDestroy() {
//		ImageLoader.clearCache();
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);
		if (resultCode == RESULT_OK && reqCode == AppConstants.CITY_REQUEST) {
			if (!selectedCity.equalsIgnoreCase(data.getStringExtra("CITY_NAME"))) {
				localityItems = null;
				ids = null;
				selectedLocalityindex = null;
				currentLocality.setText("Choose your Area");
			}
			selectedCity = data.getStringExtra("CITY_NAME");
			currentCity.setText(Html.fromHtml("in " + "<b>" + selectedCity + "</b>"));
			int index = data.getIntExtra("CITY_INDEX", 0);
			if (index == -1)
				city_id = -1;
			else 
				city_id = cityList.get(index).getId();

		} else if (resultCode == RESULT_OK && reqCode == AppConstants.LOCALITY_REQUEST) {
			String locality = "";
			selectedLocalityItems = data.getStringArrayListExtra("SELECTED_LOCALITIES");
			selectedLocalityindex = data.getStringArrayListExtra("SELECTED_LOCALITIES_INDEX");
			if (selectedLocalityItems != null
					&& selectedLocalityItems.size() > 0) {
				for (int i = 0; i < selectedLocalityItems.size(); i++) {
					if (i == selectedLocalityItems.size() - 1) {
						locality += selectedLocalityItems.get(i);
					} else {
						locality += selectedLocalityItems.get(i) + ",";
					}
				}
				currentLocality.setText(Html.fromHtml("Your Selected Area " + "<b>" + locality + "</b>"));
			} else {
				currentLocality.setText("Choose your Area");
			}

			ids = new ArrayList<String>();

			if (selectedLocalityindex != null && selectedLocalityindex.size() > 0) {
				for (int i = 0; i < selectedLocalityindex.size(); i++) {
					ids.add(String.valueOf(localityList.get(Integer.parseInt(selectedLocalityindex.get(i))).getId()));
				}
			}
		}
	}

	public String jsonForSearch() {
		JSONObject jArray = new JSONObject();
		try {
			if (city_id != -1) {
				JSONObject array = new JSONObject();
				array.put("city_id", city_id + "");
				array.put("city_name", selectedCity);
				jArray.put("city", array);
				if (ids != null && ids.size() > 0) {
					JSONArray jsonArray = new JSONArray();
					for (int i = 0; i < selectedLocalityItems.size(); i++) {
						JSONObject localityArray = new JSONObject();
						localityArray.put("locality_id", ids.get(i));
						localityArray.put("locality_name",selectedLocalityItems.get(i));
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
	public void onAnimationStart(Animation animation) { }

	@Override
	public void onAnimationEnd(Animation animation) { }

	@Override
	public void onAnimationRepeat(Animation animation) { }

}
